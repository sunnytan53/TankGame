package tankgame.game.effect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Store the visual effect(not just images) such as explosion
 */
public class Visual {
    private static BufferedImage img;
    private ArrayList<int[]> locationAndTick;

    public Visual(String path) {
        img = Resource.readResourceImage(path);
        locationAndTick = new ArrayList<>();
    }

    public void addLocation(int x, int y) {
        int[] info = {0, x, y};
        locationAndTick.add(info);
    }

    public void play(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < locationAndTick.size(); i++) {
            int[] info = locationAndTick.get(i);
            if (info[0] < 100) {
                info[0]++;
                g2.drawImage(img, info[1], info[2], null);
            } else {
                locationAndTick.remove(info);
                i--;
            }
        }
    }
}
