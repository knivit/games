package com.tsoft.game.loderunner;

import com.tsoft.game.Assert;

import java.util.Arrays;

public class RobotBehaviour {
    public int probabilityLadderAffectsHuntingAction = 80;
    public int probabilityLadderAffectsGoAction = 50;

    // actions must be ordered by AccountType enums
    private Action[] actions = new Action[] {
        new Action(ActionType.GO_LEFT, 10, 40),
        new Action(ActionType.GO_RIGHT, 10, 40),
        new Action(ActionType.GO_UP, 10, 10),
        new Action(ActionType.GO_DOWN, 10, 10),
        new Action(ActionType.WAIT, 5, 5),
        new Action(ActionType.HUNTING_PLAYER, 55, 80)
    };

    public enum ActionType {
        GO_LEFT(false),
        GO_RIGHT(false),
        GO_UP(false),
        GO_DOWN(false),
        WAIT(false),
        HUNTING_PLAYER(true);

        private boolean isRelatedToPlayer;

        ActionType(boolean isRelatedToPlayer) {
            this.isRelatedToPlayer = isRelatedToPlayer;
        }

        public boolean isRelatedToPlayer() {
            return isRelatedToPlayer;
        }
    }

    public static class Action {
        public ActionType actionType;
        public int probability;
        private int maxDuration;

        public int duration;

        public Action(ActionType actionType, int probability, int maxDuration) {
            this.actionType = actionType;
            this.probability = probability;
            this.maxDuration = maxDuration;
        }

        public void updateDuration() {
            duration = (int)(Math.random() * maxDuration) + 1;
        }

        public Action getInitializedClone() {
            Action clone = new Action(actionType, probability, maxDuration);
            clone.updateDuration();
            return clone;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Action action = (Action) o;

            if (actionType != action.actionType) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return actionType != null ? actionType.hashCode() : 0;
        }

        public String getLogString() {
            return "Action {" +
                    "actionType=" + actionType.name() +
                    ", probability=" + probability +
                    ", maxDuration=" + maxDuration +
                    ", duration=" + duration +
                    '}';
        }
    }

    public Action getRandomAction(boolean includePlayerActions) {
        int probability = (int)(Math.random() * 100);
        int n = 0;
        while (true) {
            Action action = actions[n];
            if (!includePlayerActions && action.actionType.isRelatedToPlayer()) {
                // skip the action related to a player
            } else {
                probability -= action.probability;
                if (probability < 0) {
                    break;
                }
            }

            n ++;
            if (n >= actions.length) {
                n = 0;
            }
        }

        Action action = getClonedAction(n);

        return action;
    }

    public Action getAction(ActionType actionType) {
        for (Action action : actions) {
            if (action.actionType == actionType) {
                return action.getInitializedClone();
            }
        }

        throw new IllegalArgumentException("Unknown actionType=" + actionType.name());
    }

    public boolean loadProperty(String name, String value) {
        if ("probabilityLadderAffectsHuntingAction".equalsIgnoreCase(name)) {
            probabilityLadderAffectsHuntingAction = Integer.parseInt(value);
            return true;
        }

        if ("probabilityLadderAffectsGoAction".equalsIgnoreCase(name)) {
            probabilityLadderAffectsGoAction = Integer.parseInt(value);
            return true;
        }

        for (ActionType actionType : ActionType.values()) {
            if (actionType.name().equalsIgnoreCase(name)) {
                int n = value.indexOf(',');
                int probability = Integer.parseInt(value.substring(0, n).trim());
                int maxDuration = Integer.parseInt(value.substring(n + 1).trim());
                actions[actionType.ordinal()].probability = probability;
                actions[actionType.ordinal()].maxDuration = maxDuration;
                return true;
            }
        }

        return false;
    }

    private Action getClonedAction(int n) {
        Assert.isTrue(n >= 0 && n < actions.length, "Invalid action number=" + n + ", must be [0, " + (actions.length - 1) + "]");
        return actions[n].getInitializedClone();
    }

    public String getLogString() {
        StringBuilder buf = new StringBuilder("RobotBehaviour {\n");
        buf.append("probabilityLadderAffectsHuntingAction=").append(probabilityLadderAffectsHuntingAction).append('\n');
        buf.append(", probabilityLadderAffectsGoAction=").append(probabilityLadderAffectsGoAction).append('\n');
        buf.append(", actions= {\n");
        for (Action action : actions) {
            buf.append("  ").append(action.getLogString()).append('\n');
        }
        buf.append(" }\n");
        buf.append('}');
        return buf.toString();
    }
}
