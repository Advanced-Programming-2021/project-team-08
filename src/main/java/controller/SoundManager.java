package controller;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class SoundManager {

    MediaPlayer mediaPlayer;

    public void playSound(String location) {
        Media hit = new Media(new File(location).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }

    public void playMusic(String location) {
        Media hit = new Media(new File(location).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.play();
    }

    public void playBackgroundSound() {
        Media hit = new Media(new File("musics/ForestWalk.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.play();
    }

}
