package tankgame.game.moveable;

import tankgame.GameConstants;
import tankgame.game.GameInstance;

import java.awt.image.BufferedImage;

public abstract class Moveable extends GameInstance {
    protected int vx;
    protected int vy;
    protected final int R;

    public Moveable(int x, int y, int vx, int vy, float angle, int R, BufferedImage img) {
        super(x, y, angle, img);
        this.vx = vx;
        this.vy = vy;
        this.R = R;
    }

    protected void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
    }

    public abstract void update();
}
