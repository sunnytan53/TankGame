package tankgame.game.effect;

import tankgame.game.TRE;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

public class Resource {
    public static BufferedImage readResourceImage(String name) {
        try {
            return read(TRE.class.getClassLoader().getResource(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AudioInputStream readResourceMusic(String name) {
        try {
            return AudioSystem.getAudioInputStream(TRE.class.getClassLoader().getResource(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
