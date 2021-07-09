package controller;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class SoundManager extends Application {

    MediaPlayer mediaPlayer;

    public void playSound(String location) {
        if (new File(location).exists()) System.out.println("file is here");
        Media hit = new Media(new File(location).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.play();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        playSound("musics/ForestWalk.mp3");
    }
}
