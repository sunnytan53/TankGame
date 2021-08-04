package tankgame.game;

import tankgame.game.effect.Sound;
import tankgame.game.effect.Visual;
import tankgame.game.moveable.Bullet;
import tankgame.game.moveable.MoveableInstance;
import tankgame.game.stationary.BreakableWall;
import tankgame.game.stationary.PowerupInstance;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Tank extends GameInstanceControlBlock {
    // store health image for tank control block
    private Image healthBar[];

    public Tank(BufferedImage tank, BufferedImage bullet, BufferedImage[] health) {
        super(tank);
        int[][] arr = {{212, 212}, {1836, 812}};
        for (int i = 0; i < 2; i++) {
            addInstance(arr[i][0], arr[i][1], 0);
            ((TankInstance) this.instances.get(i)).setBulletCB(bullet);
        }
        this.healthBar = health;
    }

    // get the two tanks reference as an array
    public TankInstance[] getTankInstance() {
        TankInstance[] ti = {(TankInstance) this.instances.get(0), (TankInstance) this.instances.get(1)};
        return ti;
    }

    // reset the tanks
    public void reset() {
        TankInstance[] ti = getTankInstance();
        for (TankInstance t : ti) {
            t.reset();
        }
    }

    @Override
    public void addInstance(int x, int y, float unused3) {
        this.instances.add(new TankInstance(x, y));
    }

    // Tank's drawImage should trigger its Bullet's drawImage
    @Override
    public void drawImage(Graphics g) {
        super.drawImage(g);
        TankInstance[] ti = getTankInstance();
        for (int i = 0; i < ti.length; i++) {
            ti[i].bulletCB.drawImage(g);
            g.drawImage(healthBar[ti[i].getHealth()-1], ti[i].x - 40, ti[i].y - 60, null);
        }
    }

    // draw the tank information at fixed position on the screen
    private final int[] infoX = {50, 750};
    private final int[] infoY = {550, 600, 650, 700};

    public void drawInfo(Graphics2D g2) {
        TankInstance[] ti = getTankInstance();
        for (int i = 0; i < 2; i++) {
            // display health as a bar
            g2.setColor(Color.GREEN);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 40));
            g2.drawString("Health: ", infoX[i], infoY[0]);
            g2.drawImage(healthBar[ti[i].getHealth()-1], infoX[i] + 130, infoY[0] - 25, null);

            // display lives, show in red when not full, in green when full
            g2.setColor(Color.RED);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 40));
            g2.drawString("Live(s): " + ti[i].getLive(), infoX[i], infoY[1]);

            // display cooldown tick, show in blue when max, in pink for normal
            g2.setColor(ti[i].getCooldownTick() > 50 ? Color.PINK : Color.BLUE);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 40));
            g2.drawString("Cooldown: " + ti[i].getCooldownTick(), infoX[i], infoY[2]);

            // display speed, show in blue when max, in yellow for normal
            g2.setColor(ti[i].getSpeed() > 2 ? Color.BLUE : Color.ORANGE);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 40));
            g2.drawString("Speed: " + ti[i].getSpeed(), infoX[i], infoY[3]);
        }
    }

    // check collision and also border
    public void checkCollision(ArrayList<GameInstanceControlBlock> stationaryCB, Sound sound, Visual visual) {
        Tank.TankInstance[] ti = getTankInstance();

        // advanced way to check border using collision
        if (ti[0].hitbox.intersects(ti[1].hitbox)) {
            for (TankInstance t : ti) {
                t.restorePosition();
            }
        }

        for (int i = 0; i < ti.length; i++) {

            // check tanks with map instances
            for (GameInstanceControlBlock cb : stationaryCB) {
                for (int j = 0; j < cb.instances.size(); j++) {
                    GameInstance gi = cb.instances.get(j);
                    if (ti[i].hitbox.intersects(gi.hitbox)) {
                        // take up the power up and modify our tank
                        if (gi instanceof PowerupInstance pi) {
                            pi.updateTank(ti[i]);
                            cb.instances.remove(gi);
                        }
                        ti[i].restorePosition();
                    }
                }
            }

            // check bullets with map instances
            // must keep separately to detect multiple blocks
            for (int k = 0; k < ti[i].bulletCB.instances.size(); k++) {
                GameInstance b = ti[i].bulletCB.instances.get(k);
                boolean destroy = false;

                for (GameInstanceControlBlock cb : stationaryCB) {
                    for (int l = 0; l < cb.instances.size(); l++) {
                        GameInstance gi = cb.instances.get(l);
                        if (b.hitbox.intersects(gi.hitbox)) {
                            destroy = true;
                            if (cb instanceof BreakableWall) {
                                cb.instances.remove(gi);
                                l--;

                                // drop powerup when removing a breakable wall
                                int rand = new Random().nextInt(100);
                                int indexOfPowerup = 0;
                                if (rand < 3) {
                                    indexOfPowerup = 4;
                                } else if (rand < 9) {
                                    indexOfPowerup = 3;
                                } else if (rand < 21) {
                                    indexOfPowerup = 2;
                                }

                                if (indexOfPowerup > 1) {
                                    stationaryCB.get(indexOfPowerup).addInstance(gi.getX(), gi.getY(), 0);
                                }
                            }
                        }
                    }
                }

                // check tanks with bullets against each other
                int tankIndex = 0;
                if (i == 0) {
                    tankIndex = 1;
                }
                if (ti[tankIndex].hitbox.intersects(b.hitbox)) {
                    ti[tankIndex].reduceHealth();
                    destroy = true;
                }

                // play the explosion
                if (destroy) {
                    visual.addLocation(b.getX(), b.getY());
                    sound.play();
                    ti[i].bulletCB.instances.remove(b);
                    k--;
                }
            }
        }
    }

    // check if one of the tank is fatal
    public boolean checkEnd() {
        TankInstance[] ti = getTankInstance();
        return (ti[0].getLive() < 1 || ti[1].getLive() < 1);
    }

    /**
     * Tank Instance
     */
    public class TankInstance extends MoveableInstance {

        private final float ROTATIONSPEED = 3.0f;

        private boolean UpPressed, DownPressed, RightPressed, LeftPressed, ShootPressed;

        private Bullet bulletCB = null;

        private int cooldownTick = 100, shootTick = -1, oldX, oldY, health, live;

        private final int startX, startY;

        public TankInstance(int startX, int startY) {
            super(0, 0, 0, 0, 0, 2, getImage());
            this.startX = startX;
            this.startY = startY;
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

        private void rotateLeft() {
            this.angle -= this.ROTATIONSPEED;
        }

        private void rotateRight() {
            this.angle += this.ROTATIONSPEED;
        }

        private void moveBackwards() {
            vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            x -= speed * vx;
            y -= speed * vy;
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

        public void setBulletCB(BufferedImage img) {
            if (this.bulletCB == null) {
                this.bulletCB = new Bullet(img);
            }
        }

        // restore the position when can't move
        public void restorePosition() {
            x = oldX;
            y = oldY;
        }

        // reset the tank's status
        public void reset() {
            x = startX;
            y = startY;
            oldX = startX;
            oldY = startY;
            health = 4;
            live = 3;
            speed = 1;
            cooldownTick = 100;
        }

        /**
         * status change functions
         */
        public void reduceCooldown() {
            if (cooldownTick > 50) {
                cooldownTick -= 10;
            }
        }

        public void reduceHealth() {
            if (health > 1) {
                health--;
            } else {
                health = 4;
                live--;
                x = startX;
                y = startY;
                oldX = startX;
                oldY = startY;
            }
        }

        public void increaseHealth() {
            if (health < 3) {
                health++;
            }
        }

        public void increaseSpeed() {
            if (speed < 3) {
                speed++;
            }
        }

        /**
         * status getter for information
         */
        public int getHealth() {
            return health;
        }

        public int getLive() {
            return live;
        }

        public int getCooldownTick() {
            return cooldownTick;
        }

        public int getSpeed() {
            return speed;
        }
    }
}
