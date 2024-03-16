package com.tsoft.dune2.timer;

import java.util.function.Supplier;

public class TimerNode {

    public long usec_left;
    public long usec_delay;
    public Supplier<Void> callback;
    public boolean callonce;
}
