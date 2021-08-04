package tankgame.game.moveable;

import tankgame.GameConstants;
import tankgame.game.GameInstance;

import java.awt.image.BufferedImage;

/**
 * The template for all moveable instances, like tanks and bullets
 */
public abstract class MoveableInstance extends GameInstance {
    protected int vx;
    protected int vy;
    protected final int R;
    protected int speed;

    public MoveableInstance(int x, int y, int vx, int vy, float angle, int R, BufferedImage img) {
        super(x, y, angle, img);
        this.vx = vx;
        this.vy = vy;
        this.R = R;
        this.speed = 1;
    }

    protected void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += speed*vx;
        y += speed*vy;
    }

    public abstract void update();
}
