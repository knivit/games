package com.tsoft.game.games.loderunner.actor;

import java.awt.*;
import java.util.ArrayList;

import static com.tsoft.game.games.loderunner.LodeRunner.state;

public class Robots {

    private final ArrayList<Robot> robots = new ArrayList<>();

    private final RobotBehaviour behaviour = new RobotBehaviour();

    private int numberOfRobots = 4;

    public Robots() {
        state.world.setRobots(this);

        for (String propertyName : state.world.properties.keySet()) {
            loadProperty(propertyName, state.world.properties.get(propertyName));
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
        int n = (int)(Math.random() * state.world.robotStartPlaces.size());
        Point startPlace = state.world.robotStartPlaces.get(n);

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
}
