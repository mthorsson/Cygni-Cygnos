package se.cygni.cygnos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import se.cygni.cygnos.model.PlayerState;
import se.cygni.cygnos.model.Track;
import se.cygni.cygnos.service.Mp3PlayerService;

import java.net.URL;

@RestController
@RequestMapping("/api/loopingplayer")
public class LoopingPlayerController {

    private final Mp3PlayerService player;

    @Autowired
    public LoopingPlayerController(Mp3PlayerService player) {
        this.player = player;
    }

    @RequestMapping("/state")
    public PlayerState state() {
        return player.getState();
    }

    @RequestMapping(path = "/pause",
            method = RequestMethod.POST)
    public void pause() {
        player.pause();
    }

    @RequestMapping(value = "/playloop",
            method = RequestMethod.POST)
    public void playLoop() throws Exception {

        // https://p.scdn.co/mp3-preview/6e742d165dc1d9371bc43b0c99b97763f7ce26e8

        Track t = new Track("6e742d165dc1d9371bc43b0c99b97763f7ce26e8",
                "https://p.scdn.co/mp3-preview/6e742d165dc1d9371bc43b0c99b97763f7ce26e8", "Bamse", null, null);
        player.play(t);
        // Verify correct url
    }

    @RequestMapping(path = "/stop",
            method = RequestMethod.POST)
    public void stop() {
        player.stop();
    }
}
