package se.cygni.cygnos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import se.cygni.cygnos.model.PlayerState;
import se.cygni.cygnos.model.Track;
import se.cygni.cygnos.service.Mp3PlayerService;

import java.util.Arrays;
import java.util.List;

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

    List<Track> tracks = Arrays.asList(

            new Track("6e742d165dc1d9371bc43b0c99b97763f7ce26e8", "https://p.scdn.co/mp3-preview/6e742d165dc1d9371bc43b0c99b97763f7ce26e8", "Bamse", null, null),
            new Track("eead87be4d0259737de8615f3720f4517e3e100d", "https://p.scdn.co/mp3-preview/eead87be4d0259737de8615f3720f4517e3e100d", "John Blund", null, null)
    );

    @RequestMapping(value = "/playloop",
            method = RequestMethod.POST)
    public void playLoop() throws Exception {
        player.play(tracks.get(0));
    }

    @RequestMapping(path = "/stop",
            method = RequestMethod.POST)
    public void stop() {
        player.stop();
    }
}
