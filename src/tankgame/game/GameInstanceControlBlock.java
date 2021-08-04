package tankgame.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The template of a control block for instances
 */
public abstract class GameInstanceControlBlock {

    // stores all images together and the real control block only stores an index
    private static ArrayList<BufferedImage> imgList = new ArrayList<>();
    private int imgIndex;

    // stores the instances into arraylist for easy access
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

    // a generally used drawImage function for all instances
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

    // update all instances, leave empty for stationary instances
    public void updateInstances() {
        for (GameInstance gi : instances) {
            gi.update();
        }
    }

    // reset the instances when reloading the map
    public void resetInstances() {
        instances = new ArrayList<>();
    }

    public abstract void addInstance(int x, int y, float angle);
}
