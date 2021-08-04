package tankgame.game.stationary;

import tankgame.game.GameInstanceControlBlock;
import tankgame.game.Tank;

import java.awt.image.BufferedImage;

public class PowerupHeal extends GameInstanceControlBlock {
    public PowerupHeal(BufferedImage img) {
        super(img);
    }

    public void addInstance(int x, int y, float unused) {
        this.instances.add(new PowerupHeal.PowerupHealInstance(x, y));
    }

    private class PowerupHealInstance extends PowerupInstance {
        public PowerupHealInstance(int x, int y) {
            super(x, y, getImage());
        }

        @Override
        public void updateTank(Tank.TankInstance t) {
            t.increaseHealth();
        }
    }
}
