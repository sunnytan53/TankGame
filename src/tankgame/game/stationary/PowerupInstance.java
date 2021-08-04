package tankgame.game.stationary;

import tankgame.game.Tank;

import java.awt.image.BufferedImage;

public abstract class PowerupInstance extends StationaryInstance{
    public PowerupInstance(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    public abstract void updateTank(Tank.TankInstance t);
}
