/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.game;


import tankgame.GameConstants;
import tankgame.Launcher;
import tankgame.game.effect.GIF;
import tankgame.game.effect.Resource;
import tankgame.game.effect.Sound;
import tankgame.game.stationary.BreakableWall;
import tankgame.game.stationary.UnbreakableWall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class TRE extends JPanel implements Runnable {

    private BufferedImage worldBuffer;
    private BufferedImage world;

    private Tank tankCB;
    private ArrayList<GameInstanceControlBlock> stationaryCB = new ArrayList<>();

    private Sound explosion;
    private GIF explosionAnimation;

    private Launcher lf;

    public TRE(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            this.resetGame();
            while (true) {
                this.tankCB.updateInstances(); // update tank
                this.repaint();   // redraw game
                checkCollision();
                Thread.sleep(10);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    // check all collisions to do something
    private void checkCollision() {
        Tank.TankInstance[] ti = tankCB.getTankInstance();

        // check tank with tank
        if (ti[0].hitbox.intersects(ti[1].hitbox)) {
            for (Tank.TankInstance t : ti) {
                t.restorePosition();
            }
        }

        for (int i = 0; i < ti.length; i++) {
            // check tanks with map instances
            for (GameInstanceControlBlock cb : stationaryCB) {
                for (int j = 0; j < cb.instances.size(); j++) {
                    GameInstance gi = cb.instances.get(j);
                    if (ti[i].hitbox.intersects(gi.hitbox)) {
                        ti[i].restorePosition();
                    }
                }
            }

            // check bullets with map instances
            // must keep separately to detect multiple blocks
            for (int k = 0; k < ti[i].getBulletCB().instances.size(); k++) {
                GameInstance b = ti[i].getBulletCB().instances.get(k);
                boolean destroy = false;

                for (GameInstanceControlBlock cb : stationaryCB) {
                    for (int l = 0; l < cb.instances.size(); l++) {
                        GameInstance gi = cb.instances.get(l);
                        if (b.hitbox.intersects(gi.hitbox)) {
                            destroy = true;
                            if (cb instanceof BreakableWall) {
                                cb.instances.remove(gi);
                                l--;
                            }
                        }
                    }
                }

                // check tanks with bullets
                // must be reverse because this is a war!!!
                int tankIndex = 0;
                if (i == 0) {
                    tankIndex = 1;
                }
                if (ti[tankIndex].hitbox.intersects(b.hitbox)) {
                    // todo heath --
                    destroy = true;
                }

                if (destroy) {
                    explosionAnimation.setLocation(b.getX(), b.getY());
                    explosion.play();
                    ti[i].getBulletCB().instances.remove(b);
                    k--;
                }
            }
        }
    }

    /**
     * Reset game to its initial state
     */
    public void resetGame() {
        this.tankCB.reset();
    }


    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() {
        this.worldBuffer = new BufferedImage(GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);

        // read the image from resource folder
        this.world = Resource.readResourceImage("background.png");
        tankCB = new Tank(Resource.readResourceImage("tank.png"), Resource.readResourceImage("bullet.png"));

        // read effect resources
        explosion = new Sound("explosion.wav");
        explosionAnimation = new GIF("explosion.gif");

        // add all stationary related control blocks
        stationaryCB.add(new UnbreakableWall(Resource.readResourceImage("unbreakable.png")));
        stationaryCB.add(new BreakableWall(Resource.readResourceImage("breakable.png")));

        Tank.TankInstance[] ti = tankCB.getTankInstance();
        TankControl tc0 = new TankControl(ti[0], KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc0);

        TankControl tc1 = new TankControl(ti[1], KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);

        // load up the instances on the map
        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(TRE.class.getClassLoader().getResourceAsStream("maps/map.xlsx")));
        BufferedReader mapReader = new BufferedReader(isr);
        try {
            String line = null;
            line = mapReader.readLine();
            String item[] = line.split("\t");
            int rowCount = Integer.parseInt(item[0]);
            int colCount = Integer.parseInt(item[1]);

            // create the instances based on the type
            for (int row = 0; row < rowCount; row++) {
                line = mapReader.readLine();
                item = line.split("\t");
                for (int col = 0; col < colCount; col++) {
                    int cbIndex = -1;
                    switch (item[col]) {
                        case "1":
                        case "2":
                            cbIndex = 0;
                            break;
                        case "4":
                            cbIndex = 1;
                            break;
                        default:
                            break;
                    }
                    if (cbIndex > -1) {
                        stationaryCB.get(cbIndex).addInstance(col * GameConstants.GAME_INSTANCE_LENGTH,
                                row * GameConstants.GAME_INSTANCE_LENGTH, 0);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBackground(Color.black);
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // draw the instances onto the map first
        Graphics2D buffer = worldBuffer.createGraphics();
        buffer.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        buffer.drawImage(this.world, 0, 0, null);
        this.tankCB.drawImage(buffer);
        stationaryCB.forEach(cb -> cb.drawImage(buffer));

        // draw explosion if needed
        explosionAnimation.play(buffer);

        // draw two screens using tanks as the centers
        Tank.TankInstance[] ti = tankCB.getTankInstance();
        BufferedImage leftHalf = worldBuffer.getSubimage(getScreenOnX(ti[0]), getScreenOnY(ti[0]),
                GameConstants.GAME_PLAYER_SCREEN_LENGTH, GameConstants.GAME_PLAYER_SCREEN_LENGTH);
        BufferedImage rightHalf = worldBuffer.getSubimage(getScreenOnX(ti[1]), getScreenOnY(ti[1]),
                GameConstants.GAME_PLAYER_SCREEN_LENGTH, GameConstants.GAME_PLAYER_SCREEN_LENGTH);
        g2.drawImage(leftHalf, 0, 0, null);
        g2.drawImage(rightHalf, GameConstants.GAME_PLAYER_SCREEN_RIGHT_X, 0, null);

        // draw the minimap of the WHOLE world
//        BufferedImage minimap = worldBuffer.getSubimage(0, 0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
//        g2.scale(GameConstants.GAME_MINIMAP_SCALE, GameConstants.GAME_MINIMAP_SCALE);
//        g2.drawImage(minimap, GameConstants.GAME_MINIMAP_SCALED_X, 0, null);
    }

    private static int getScreenOnX(Tank.TankInstance t) {
        if (t.getX() > GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_PLAYER_SCREEN_CENTER_OFFSET - 32) {
            return GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_PLAYER_SCREEN_LENGTH;
        } else if (t.getX() > GameConstants.GAME_PLAYER_SCREEN_CENTER_OFFSET) {
            return t.getX() - GameConstants.GAME_PLAYER_SCREEN_CENTER_OFFSET;
        } else {
            return 0;
        }
    }

    private static int getScreenOnY(Tank.TankInstance t) {
        if (t.getY() > GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_PLAYER_SCREEN_CENTER_OFFSET - 32) { // 758
            return GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_PLAYER_SCREEN_LENGTH; // 524
        } else if (t.getY() > GameConstants.GAME_PLAYER_SCREEN_CENTER_OFFSET) {
            return t.getY() - GameConstants.GAME_PLAYER_SCREEN_CENTER_OFFSET;
        } else {
            return 0;
        }
    }
}
