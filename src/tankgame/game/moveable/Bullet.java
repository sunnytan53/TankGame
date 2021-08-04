package tankgame.game.moveable;

import tankgame.game.GameInstanceControlBlock;

import java.awt.image.BufferedImage;

public class Bullet extends GameInstanceControlBlock {
    public Bullet(BufferedImage img) {
        super(img);
    }

    public void addInstance(int x, int y, float angle) {
        this.instances.add(new BulletInstance(x, y, angle));
    }

    private class BulletInstance extends MoveableInstance {
        public BulletInstance(int x, int y, float angle) {
            super(x, y, 0, 0, angle, 5, getImage());
        }

        @Override
        public void update() {
            moveForwards();
            hitbox.setLocation(x, y);
        }
    }
}
