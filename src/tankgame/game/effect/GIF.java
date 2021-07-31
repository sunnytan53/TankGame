package tankgame.game.effect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GIF {
    private static BufferedImage gif;
    private int x, y;
    private int tick = -1;

    public GIF(String path) {
        gif = Resource.readResourceImage(path);
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        this.tick = 0;
    }

    public void play(Graphics g) {
        if (this.tick > -1) {
            tick++;
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(gif, this.x, this.y, null);
            if (tick > 100) {
                tick = -1;
            }
        }
    }
}
