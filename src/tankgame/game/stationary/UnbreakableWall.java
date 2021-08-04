package tankgame.game.stationary;

import tankgame.game.GameInstanceControlBlock;

import java.awt.image.BufferedImage;

public class UnbreakableWall extends GameInstanceControlBlock {
    public UnbreakableWall(BufferedImage img) {
        super(img);
    }

    public void addInstance(int x, int y, float unused) {
        this.instances.add(new UnbreakableWallInstance(x, y));
    }

    private class UnbreakableWallInstance extends StationaryInstance {
        public UnbreakableWallInstance(int x, int y) {
            super(x, y, getImage());
        }
    }
}
