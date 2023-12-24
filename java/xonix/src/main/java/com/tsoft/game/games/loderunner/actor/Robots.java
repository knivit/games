package com.tsoft.game.games.loderunner.actor;

import java.awt.*;
import java.util.ArrayList;

import static com.tsoft.game.games.loderunner.LRGameState.world;

public class Robots {

    private ArrayList<Robot> robots = new ArrayList<>();

    private int numberOfRobots = 4;
    private RobotBehaviour behaviour = new RobotBehaviour();

    public Robots() {
        world.setRobots(this);

        for (String propertyName : world.properties.keySet()) {
            loadProperty(propertyName, world.properties.get(propertyName));
        }
    }

    public void move() {
        if (robots.size() < numberOfRobots) {
            addRobot();
        }

        for (Robot robot : robots) {
            robot.move();
        }
    }

    private void addRobot() {
        int n = (int)(Math.random() * world.robotStartPlaces.size());
        Point startPlace = world.robotStartPlaces.get(n);

        Robot robot = new Robot(startPlace.x, startPlace.y, behaviour);
        robots.add(robot);
    }

    private boolean loadProperty(String name, String value) {
        if ("numberOfRobots".equalsIgnoreCase(name)) {
            numberOfRobots = Integer.parseInt(value);
            return true;
        }

        return behaviour.loadProperty(name, value);
    }

    public String getLogString() {
        StringBuilder buf = new StringBuilder("Robots {");
        buf.append("numberOfRobots=").append(numberOfRobots).append('\n');
        buf.append(", behaviour=").append(behaviour.getLogString()).append('\n');
        buf.append(", robots= {\n");
        for (Robot robot : robots) {
            buf.append("  ").append(robot.getLogString()).append('\n');
        }
        buf.append(" }\n");
        buf.append('}');

        return buf.toString();
    }
}
