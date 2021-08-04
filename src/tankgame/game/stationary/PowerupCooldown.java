package tankgame.game.stationary;

import tankgame.game.GameInstanceControlBlock;
import tankgame.game.Tank;

import java.awt.image.BufferedImage;

public class PowerupCooldown extends GameInstanceControlBlock{
    public PowerupCooldown(BufferedImage img) {
        super(img);
    }

    public void addInstance(int x, int y, float unused) {
        this.instances.add(new PowerupCooldownInstance(x, y));
    }

    private class PowerupCooldownInstance extends PowerupInstance {
        public PowerupCooldownInstance(int x, int y) {
            super(x, y, getImage());
        }

        @Override
        public void updateTank(Tank.TankInstance t) {
            t.reduceCooldown();
        }
    }
}