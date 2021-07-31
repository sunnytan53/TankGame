package tankgame.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class GameInstanceControlBlock {
    private static ArrayList<BufferedImage> imgList = new ArrayList<>();

    private int imgIndex;
    protected ArrayList<GameInstance> instances = new ArrayList<>();

    public GameInstanceControlBlock(BufferedImage img) {
        if (!imgList.contains(img)) {
            imgList.add(img);
        }
        imgIndex = imgList.indexOf(img);
    }

    public BufferedImage getImage() {
        return imgList.get(imgIndex);
    }

    public void drawImage(Graphics g) {
        for (GameInstance gi : instances) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform rotation = AffineTransform.getTranslateInstance(gi.getX(), gi.getY());
            rotation.rotate(Math.toRadians(gi.getAngle()),
                    imgList.get(imgIndex).getWidth() / 2.0,
                    imgList.get(imgIndex).getHeight() / 2.0);
            g2d.drawImage(imgList.get(imgIndex), rotation, null);
        }
    }

    public void updateInstances() {
        for (GameInstance gi : instances) {
            gi.update();
        }
    }

    public abstract void addInstance(int x, int y, float angle);
}
