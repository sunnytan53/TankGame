package tankgame.game.stationary;

import tankgame.game.GameInstanceControlBlock;

import java.awt.image.BufferedImage;

public class BreakableWall extends GameInstanceControlBlock {
    public BreakableWall(BufferedImage img) {
        super(img);
    }

    public void addInstance(int x, int y, float unused) {
        this.instances.add(new BreakableWallInstance(x, y));
    }

    private class BreakableWallInstance extends StationaryInstance {
        public BreakableWallInstance(int x, int y) {
            super(x, y, getImage());
        }
    }
}