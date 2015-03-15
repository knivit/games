package com.tsoft.game.digger;

public class World {
    public Bags bags;
    public Main main;
    public Sound sound;
    public Monsters monsters;
    public Scores scores;
    public Sprite sprite;
    public Drawing drawing;
    public Input input;
    public Pc pc;
    public Emerald emerald;
    public Digger digger;

    public World() {
        digger = new Digger(this);
        bags = new Bags(this);
        main = new Main(this);
        sound = new Sound(this);
        monsters = new Monsters(this);
        scores = new Scores(this);
        sprite = new Sprite(this);
        drawing = new Drawing(this);
        input = new Input(this);
        pc = new Pc(this);
        emerald = new Emerald(this);
    }
}
