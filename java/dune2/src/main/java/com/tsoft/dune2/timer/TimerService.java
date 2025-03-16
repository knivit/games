package com.tsoft.dune2.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.tsoft.dune2.timer.TimerType.TIMER_GAME;
import static com.tsoft.dune2.timer.TimerType.TIMER_GUI;

public class TimerService {

    public static volatile long g_timerGUI = 0;           /*!< Tick counter. Increases with 1 every tick when Timer 1 is enabled. Used for GUI. */
    public static volatile long g_timerGame = 0;          /*!< Tick counter. Increases with 1 every tick when Timer 2 is enabled. Used for game timing (units, ..). */
    public static volatile long g_timerInput = 0;         /*!< Tick counter. Increases with 1 every tick. Used for input timing. */
    static volatile long g_timerSleep = 0;                /*!< Tick counter. Increases with 1 every tick. Used for sleeping. */
    public static volatile long g_timerTimeout = 0;       /*!< Tick counter. Decreases with 1 every tick when non-zero. Used to timeout. */

    public static int s_timersActive = 0;

    static HANDLE s_timerMainThread = null;
    static HANDLE s_timerThread = null;

    static List<TimerNode> s_timerNodes = new ArrayList<>();
    static int s_timerNodeCount = 0;
    static int s_timerNodeSize  = 0;

    static long s_timerLastTime;

    static long s_timerSpeed = 1000000 / 120; /* Our timer runs at 120Hz */

    static long Timer_GetTime() {
        return System.currentTimeMillis();
    }

    /**
     * Run the timer interrupt handler.
     */
    private static volatile boolean timerLock;

    static void Timer_InterruptRun(int arg) {
        long new_time, usec_delta, delta;

        /* Lock the timer, to avoid double-calls */
        timerLock = false;
        if (timerLock) return;
        timerLock = true;

        /* Calculate the time between calls */
        new_time = Timer_GetTime();
        usec_delta = (new_time - s_timerLastTime) * 1000;
        s_timerLastTime = new_time;

        /* Walk all our timers, see which (and how often) it should be triggered */
        for (TimerNode node : s_timerNodes) {
            delta = usec_delta;

            /* No delay means: as often as possible, but don't worry about it */
            if (node.usec_delay == 0) {
                node.callback.get();
                continue;
            }

            if (node.callonce) {
                if (node.usec_left <= delta) {
                    delta -= node.usec_left;
                    node.usec_left = node.usec_delay;
                    if(arg == 0) node.callback.get();
                    while (node.usec_left <= delta) delta -= node.usec_left;
                }
            } else while (node.usec_left <= delta) {
                delta -= node.usec_left;
                node.usec_left = node.usec_delay;
                node.callback.get();
            }

            node.usec_left -= delta;
        }

        timerLock = false;
    }

    static void SleepAndProcessBackgroundTasks() {
        Timer_InterruptRun(0);
    }

    static volatile int s_timer_count = 0;

    static void Timer_Handler(int sig) {
        /* indicate that Timer_InterruptRun() should be executed */
        s_timer_count++;
    }

    static void SleepAndProcessBackgroundTasks() {
        while (s_timer_count == 0) {
            Sleep(2); /* TODO : use a semaphore */
        }
        /* timer signal SIGALRM has been triggered */
        if (s_timer_count > 1) {
            Warning("s_timer_count = %d\n", (int)s_timer_count);
        }

        s_timer_count = 0;
        Timer_InterruptRun(0);
        if (s_timer_count > 0) {
            /* one more iteration if SIGALRM has been triggered
             * during Timer_InterruptRun() */
            s_timer_count = 0;
            Timer_InterruptRun(1);	/* don't run "callonce" timers */
        }
    }

    static void CALLBACK Timer_InterruptWindows(LPVOID arg, booleanEAN TimerOrWaitFired) {
        SuspendThread(s_timerMainThread);
        s_timer_count++;
        ResumeThread(s_timerMainThread);
    }

    /**
     * Suspend the timer interrupt handling.
     */
    static void Timer_InterruptSuspend() {
        if (s_timerThread != null) DeleteTimerQueueTimer(null, s_timerThread, null);
        s_timerThread = null;
    }

    /**
     * Resume the timer interrupt handling.
     */
    static void Timer_InterruptResume() {
        long timerTime = s_timerSpeed / 1000;
        CreateTimerQueueTimer(s_timerThread, null, Timer_InterruptWindows, null, timerTime, timerTime, WT_EXECUTEINTIMERTHREAD);
    }

    /**
     * Initialize the timer.
     */
    public static void Timer_Init() {
        s_timerLastTime = Timer_GetTime();

        DuplicateHandle(GetCurrentProcess(), GetCurrentThread(), GetCurrentProcess(), &s_timerMainThread, 0, FALSE, DUPLICATE_SAME_ACCESS);

        Timer_InterruptResume();
    }

    /**
     * Uninitialize the timer.
     */
    public static void Timer_Uninit() {
        CloseHandle(s_timerMainThread);

        s_timerNodes.clear();
        s_timerNodes = null;
        s_timerNodeCount = 0;
        s_timerNodeSize = 0;
    }

    /**
     * Add a timer.
     * @param callback the callback for the timer.
     * @param usec_delay The interval of the timer.
     */
    public static void Timer_Add(Supplier<Void> callback, long usec_delay, boolean callonce) {
        TimerNode node;
        if (s_timerNodeCount == s_timerNodeSize) {
            s_timerNodeSize ++;
            s_timerNodes.add(new TimerNode());
        }

        node = s_timerNodes.get(s_timerNodeCount++);

        node.usec_left = usec_delay;
        node.usec_delay = usec_delay;
        node.callback = callback;
        node.callonce = callonce;
    }

    /**
     * Change the interval of a timer.
     * @param callback The callback to change the timer of.
     * @param usec_delay The interval.
     */
    static void Timer_Change(Supplier<Void> callback, long usec_delay) {
        for (TimerNode node : s_timerNodes) {
            if (node.callback == callback) {
                node.usec_delay = usec_delay;
                return;
            }
        }
    }

    /**
     * Remove a timer from the queue.
     * @param callback Which callback to remove.
     */
    static void Timer_Remove(Supplier<Void> callback) {
        int i;
        TimerNode[] node = s_timerNodes;
        for (i = 0; i < s_timerNodeCount; i++) {
            if (node[i].callback == callback) {
			    node[i] = s_timerNodes[--s_timerNodeCount];
                return;
            }
        }
    }

    /**
     * Handle game timers.
     */
    static void Timer_Tick() {
        if ((s_timersActive & TIMER_GUI)  != 0) g_timerGUI++;
        if ((s_timersActive & TIMER_GAME) != 0) g_timerGame++;
        g_timerInput++;
        g_timerSleep++;

        if (g_timerTimeout != 0) g_timerTimeout--;
    }

    /**
     * Set timers on and off.
     *
     * @param timer The timer to switch.
     * @param set True sets the timer on, false sets it off.
     * @return True if timer was set, false if it was not set.
     */
    public static boolean Timer_SetTimer(int timer, boolean set) {
        int t = (1 << (timer - 1));
        boolean ret = (s_timersActive & t) != 0;

        if (set) {
            s_timersActive |= t;
        } else {
            s_timersActive &= ~t;
        }

        return ret;
    }

    /**
     * Sleep for an amount of ticks.
     * @param ticks The amount of ticks to sleep.
     */
    public static void Timer_Sleep(int ticks) {
        long tick = g_timerSleep + ticks;
        while (tick >= g_timerSleep) sleepIdle();
    }
}
