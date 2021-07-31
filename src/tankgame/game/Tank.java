package tankgame.game;

import tankgame.game.moveable.Bullet;
import tankgame.game.moveable.Moveable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tank extends GameInstanceControlBlock {
    public Tank(BufferedImage tank, BufferedImage bullet) {
        super(tank);
        for (int i = 0; i < 2; i++) {
            addInstance(0, 0, 0);
            ((TankInstance) this.instances.get(i)).setBulletCB(bullet);
        }
    }

    public void addInstance(int unused1, int unused2, float unused3) {
        this.instances.add(new TankInstance());
    }

    public TankInstance[] getTankInstance() {
        TankInstance[] ti = {(TankInstance) this.instances.get(0), (TankInstance) this.instances.get(1)};
        return ti;
    }

    public void reset() {
        TankInstance[] ti = getTankInstance();
        ti[0].x = 200;
        ti[0].y = 200;
        ti[0].oldX = 200;
        ti[0].oldY = 200;
        ti[1].x = 300;
        ti[1].y = 300;
        ti[1].oldX = 300;
        ti[1].oldY = 300;
    }

    @Override
    public void drawImage(Graphics g) {
        super.drawImage(g);
        TankInstance[] ti = getTankInstance();
        for (int i = 0; i < ti.length; i++) {
            ti[i].bulletCB.drawImage(g);
        }
    }

    protected class TankInstance extends Moveable {

        private final float ROTATIONSPEED = 3.0f;

        private boolean UpPressed;
        private boolean DownPressed;
        private boolean RightPressed;
        private boolean LeftPressed;
        private boolean ShootPressed;

        private Bullet bulletCB = null;

        private short cooldownTick = 100;
        private short shootTick = -1;

        private int oldX, oldY;

        public TankInstance() {
            super(0, 0, 0, 0, 0, 2, getImage());
        }

        public void setBulletCB(BufferedImage img) {
            if (this.bulletCB == null) {
                this.bulletCB = new Bullet(img);
            }
        }

        public Bullet getBulletCB() {
            return bulletCB;
        }

        protected void toggleUpPressed() {
            this.UpPressed = true;
        }

        void toggleDownPressed() {
            this.DownPressed = true;
        }

        void toggleRightPressed() {
            this.RightPressed = true;
        }

        void toggleLeftPressed() {
            this.LeftPressed = true;
        }

        void toggleShootPressed() {
            this.ShootPressed = true;
        }

        void unToggleUpPressed() {
            this.UpPressed = false;
        }

        void unToggleDownPressed() {
            this.DownPressed = false;
        }

        void unToggleRightPressed() {
            this.RightPressed = false;
        }

        void unToggleLeftPressed() {
            this.LeftPressed = false;
        }

        void unToggleShootPressed() {
            this.ShootPressed = false;
        }

        @Override
        public void update() {
            if (this.UpPressed) {
                oldX = x;
                oldY = y;
                this.moveForwards();
            }
            if (this.DownPressed) {
                oldX = x;
                oldY = y;
                this.moveBackwards();
            }
            if (this.LeftPressed) {
                this.rotateLeft();
            }
            if (this.RightPressed) {
                this.rotateRight();
            }
            this.hitbox.setLocation(x, y);

            //  set up a cooldown time to avoid too many shots
            if (this.ShootPressed && shootTick == -1) {
                bulletCB.addInstance(this.x, this.y, this.angle);
                shootTick = 0;
            }
            if (shootTick != -1) {
                shootTick++;
                if (shootTick >= cooldownTick) {
                    shootTick = -1;
                }
            }
            bulletCB.updateInstances();
        }

        private void rotateLeft() {
            this.angle -= this.ROTATIONSPEED;
        }

        private void rotateRight() {
            this.angle += this.ROTATIONSPEED;
        }

        private void moveBackwards() {
            vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            x -= vx;
            y -= vy;
        }

        public void restorePosition() {
            this.x = oldX;
            this.y = oldY;
        }
    }
}
