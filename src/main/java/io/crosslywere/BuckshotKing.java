package io.crosslywere;

import io.crosslywere.engine.Game;

public class BuckshotKing extends Game {

    private BuckshotKing() {
        super(800, 600, "Buckshot King");
    }

    public static void main(String[] args) {
        new BuckshotKing().run();
    }

}
