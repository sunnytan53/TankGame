package tankgame.game.stationary;

import tankgame.game.GameInstance;

import java.awt.image.BufferedImage;

public abstract class Stationary extends GameInstance {
    public Stationary(int x, int y, BufferedImage img) {
        super(x, y, 0, img);
    }

    @Override
    public void update() {
        // do nothing since it should be stationary
    }
}
