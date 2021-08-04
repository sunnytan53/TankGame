package tankgame.game.stationary;

import tankgame.game.GameInstanceControlBlock;
import tankgame.game.Tank;

import java.awt.image.BufferedImage;

public class PowerupSpeedBoost extends GameInstanceControlBlock {
    public PowerupSpeedBoost(BufferedImage img) {
        super(img);
    }

    public void addInstance(int x, int y, float unused) {
        this.instances.add(new PowerupSpeedBoost.PowerupTankSpeedBoostInstance(x, y));
    }

    private class PowerupTankSpeedBoostInstance extends PowerupInstance {
        public PowerupTankSpeedBoostInstance(int x, int y) {
            super(x, y, getImage());
        }

        @Override
        public void updateTank(Tank.TankInstance t) {
            t.increaseSpeed();
        }
    }
}
