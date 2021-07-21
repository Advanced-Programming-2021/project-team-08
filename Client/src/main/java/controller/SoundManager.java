package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundManager {
    private static boolean mute;

    static MediaPlayer mediaPlayer;
    Media media;

    public static void setMute(boolean mute) {
        if(mute){
            mediaPlayer.setVolume(0);
        }else {
            mediaPlayer.setVolume(1);
        }
        SoundManager.mute = mute;
    }

    public static boolean isMute() {
        return mute;
    }

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
        media = new Media(new File("musics/ForestWalk.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

}
