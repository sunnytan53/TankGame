package tankgame;

public class GameConstants {
    public static final int GAME_WORLD_WIDTH = 2048;
    public static final int GAME_WORLD_HEIGHT = 1024;

    public static final int GAME_SCREEN_WIDTH = 1024;
    public static final int GAME_SCREEN_HEIGHT = 768;

    public static final int GAME_INSTANCE_LENGTH = 32;

    public static final int GAME_PLAYER_SCREEN_LENGTH = 500;
    public static final int GAME_PLAYER_SCREEN_CENTER_OFFSET = GAME_PLAYER_SCREEN_LENGTH / 2 - GAME_INSTANCE_LENGTH; // 234
    public static final int GAME_PLAYER_SCREEN_RIGHT_X = GAME_SCREEN_WIDTH - GAME_PLAYER_SCREEN_LENGTH; // 524

    public static final double GAME_MINIMAP_SCALE = 0.2;
    public static final int GAME_MINIMAP_SCALED_X = 5 * 307; // (1 / scale) * exact position
    public static final int GAME_MINIMAP_SCALED_Y = 5 * 530; // (1 / scale) * exact position


    public static final int START_MENU_SCREEN_WIDTH = 500;
    public static final int START_MENU_SCREEN_HEIGHT = 550;

    public static final int END_MENU_SCREEN_WIDTH = 500;
    public static final int END_MENU_SCREEN_HEIGHT = 500;
}
