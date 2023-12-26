package com.tsoft.game.utils;

import java.util.ArrayList;
import java.util.List;

public class TextCursor {

    private static class MenuItem {
        int x, y, len;
    }

    private final List<MenuItem> items = new ArrayList<>();

    public void init() {
        items.clear();
    }

    public void add(String item) {

    }
}
