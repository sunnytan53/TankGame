package tankgame.game.effect;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Store the audio and play when called
 */
public class Sound {
    private Clip clip;

    public Sound(String path) {
        try {
            clip = AudioSystem.getClip();
            clip.open(Resource.readResourceMusic(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        try {
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
