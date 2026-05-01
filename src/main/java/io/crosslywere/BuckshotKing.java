package io.crosslywere;

import java.util.List;

import io.crosslywere.engine.Game;
import io.crosslywere.engine.core.SceneTree;
import io.crosslywere.engine.core.entity.Scene;

public class BuckshotKing extends Game {

    private BuckshotKing() {
        super(800, 600, "Buckshot King");
        super.initialScene = "menu";
    }

    @Override
    protected SceneTree createSceneTree() {
        return SceneTree.from(List.of(menu(), game()));
    }

    private Scene menu() {
        var menu = new Scene("menu");
        return menu;
    }

    private Scene game() {
        var game = new Scene("mfme");
        return game;
    }

    public static void main(String[] args) {
        new BuckshotKing().run();
    }

}
