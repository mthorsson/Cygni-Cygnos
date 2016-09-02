package se.cygni.cygnos.service;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.springframework.stereotype.Service;
import se.cygni.cygnos.model.PlayerState;
import se.cygni.cygnos.model.Track;

import java.io.File;
import java.net.URL;

@Service
public class Mp3PlayerService {

    public static final int FADE_TIME_SECS = 5;
    private MediaPlayer mediaPlayer;
    private double volume = 0.5;
    private PlayerState state = PlayerState.Stopped;
    private final String tmpDir = System.getProperty("java.io.tmpdir");


    public void pause() {
        if (mediaPlayer != null) {

            switch (state) {
                case Paused:
                    fadeIn();
                    this.state = PlayerState.Playing;
                    break;
                case Playing:
                    fadeOut();
                    this.state = PlayerState.Paused;
                    break;
            }

        } else {
            this.state = PlayerState.Stopped;
        }
    }

    private void fadeIn() {
        mediaPlayer.setVolume(0.0);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(FADE_TIME_SECS),
                        new KeyValue(mediaPlayer.volumeProperty(), 1.0)));
        timeline.play();
    }

    private void fadeOut() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(FADE_TIME_SECS),
                        new KeyValue(mediaPlayer.volumeProperty(), 0)));

        timeline.play();
    }

    public void play(Track track) throws Exception {

        // MediaPlayer can't fetch resources over https
        // Store the file temporarily on disk...
        Downloader downloader = new Downloader();
        String fileName = tmpDir + track.getId() + ".mp3";
        if (!new File(fileName).exists()) {
            downloader.download(new URL(track.getPreviewUrl()), new File(fileName));
        }

        if (mediaPlayer != null) {
            stop();
        }

        // Now read
        mediaPlayer = new MediaPlayer(new Media(new File(fileName).toURI().toString()));

        fadeIn();

        mediaPlayer.setOnEndOfMedia(() -> {
            state = PlayerState.Stopped;
            //try {
            //    Mp3PlayerService.this.play(track);
            //} catch (Exception e) {
            //    e.printStackTrace();
            //}
        });
        mediaPlayer.play();

        this.state = PlayerState.Playing;
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        mediaPlayer = null;
        this.state = PlayerState.Stopped;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public PlayerState getState() {
        return state;
    }
}
