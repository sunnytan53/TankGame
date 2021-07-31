package tankgame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameInstance {
    protected int x, y;
    protected float angle;
    protected Rectangle hitbox;

    public GameInstance(int x, int y, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        hitbox = new Rectangle(this.x, this.y, img.getWidth(), img.getHeight());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    // abstract functions that child classes must include
    public abstract void update();
}
