package se.cygni.cygnos.service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.springframework.stereotype.Service;
import se.cygni.cygnos.model.PlayerState;
import se.cygni.cygnos.model.Track;

import java.io.File;
import java.net.URL;

@Service
public class Mp3PlayerService {

    private MediaPlayer mediaPlayer;
    private double volume = 0.5;
    private PlayerState state = PlayerState.Stopped;
    private final String tmpDir = System.getProperty("java.io.tmpdir");


    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            switch (state) {
                case Paused: mediaPlayer.play(); this.state = PlayerState.Playing; break;
                case Playing: mediaPlayer.pause(); this.state = PlayerState.Paused; break;
            }

        } else {
            this.state = PlayerState.Stopped;
        }
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
        mediaPlayer.setVolume(volume);
        mediaPlayer.setOnEndOfMedia(() -> {
            state = PlayerState.Stopped;
            try {
                Mp3PlayerService.this.play(track);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
