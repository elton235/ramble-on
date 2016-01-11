// 

package rambleon;

import audio_manager.AudioManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import mini_game.*;
import rambleon.events.*;

public class RambleOn extends MiniGame {
    
    AudioManager audio;
    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 700;
    
    public static final String MAPS_PATH = "./data/maps/";
    public static final String GUI_PATH = "./data/gui/";
    public static final String BACKGROUND_FILE_PATH                 = GUI_PATH + "RambleOnBackground.jpg";
    public static final String WELCOME_FILE_PATH                    = GUI_PATH + "RambleOnWelcome.jpg";
    public static final String ACCOUNTS_SCREEN_FILE_PATH            = GUI_PATH + "RambleOnAccountsScreen.jpg";
    public static final String CREATE_ACCOUNT_BUTTON_FILE_PATH      = GUI_PATH + "RambleOnCreateAccountButton.png";
    public static final String ACCOUNT_CREATION_SCREEN_FILE_PATH    = GUI_PATH + "RambleOnAccountCreationScreen.jpg";
    public static final String OK_BUTTON_FILE_PATH                  = GUI_PATH + "RambleOnOKButton.png";
    public static final String CANCEL_BUTTON_FILE_PATH              = GUI_PATH + "RambleOnCancelButton.png";
    public static final String CURRENT_ACCOUNT_SCREEN_FILE_PATH     = GUI_PATH + "RambleOnCurrentAccountScreen.jpg";
    public static final String LOG_OFF_BUTTON_FILE_PATH             = GUI_PATH + "RambleOnLogOffButton.png";
    public static final String PLAY_BUTTON_FILE_PATH                = GUI_PATH + "RambleOnPlayButton.png";
    public static final String SUBREGION_BUTTON_FILE_PATH           = GUI_PATH + "RambleOnSubRegionModeButton.png";
    public static final String FLAG_BUTTON_FILE_PATH                = GUI_PATH + "RambleOnFlagModeButton.png";
    public static final String CAPITAL_BUTTON_FILE_PATH             = GUI_PATH + "RambleOnCapitalModeButton.png";
    public static final String LEADER_BUTTON_FILE_PATH              = GUI_PATH + "RambleOnLeaderModeButton.png";
    public static final String WORLD_MAP_FILE_PATH                  = MAPS_PATH + "The World Map.png";
    public static final String SUB_REGION_FILE_PATH                 = GUI_PATH + "RambleOnSubRegion.png";
    public static final String FLAG_FILE_PATH                       = GUI_PATH + "RambleOnFlag.png";
    public static final String WIN_DISPLAY_FILE_PATH                = GUI_PATH + "RambleOnWinDisplay.png";
    
    public static final int FRAME_RATE = 30;
    public static final String APP_TITLE = "Ramble On";
    public static final Color COLOR_KEY = new Color(220, 110, 0);
    
    public static final String BACKGROUND_TYPE              = "BACKGROUND_TYPE";
    public static final String WELCOME_TYPE                 = "WELCOME_TYPE";
    public static final String ACCOUNTS_SCREEN_TYPE         = "ACCOUNTS_SCREEN_TYPE";
    public static final String CREATE_ACCOUNT_TYPE          = "CREATE_ACCOUNT_TYPE";
    public static final String ACCOUNT_CREATION_TYPE        = "ACCOUNT CREATION_TYPE";
    public static final String OK_BUTTON_TYPE               = "OK_BUTTON_TYPE";
    public static final String CANCEL_BUTTON_TYPE           = "CANCEL_BUTTON_TYPE";
    public static final String CURRENT_ACCOUNT_TYPE         = "CURRENT_ACCOUNT_TYPE";
    public static final String LOG_OFF_BUTTON_TYPE          = "LOG_OFF_BUTTON_TYPE";
    public static final String PLAY_BUTTON_TYPE             = "PLAY_BUTTON_TYPE";
    public static final String SUBREGION_MODE_BUTTON_TYPE   = "SUBREGION_MODE_BUTTON_TYPE";
    public static final String FLAG_MODE_BUTTON_TYPE        = "FLAG_MODE_BUTTON_TYPE";
    public static final String CAPITAL_MODE_BUTTON_TYPE     = "CAPITAL_MODE_BUTTON_TYPE";
    public static final String LEADER_MODE_BUTTON_TYPE      = "LEADER_MODE_BUTTON_TYPE";
    public static final String WORLD_MAP_TYPE               = "WORLD_MAP_TYPE";
    public static final String MAP_TYPE                     = "MAP_TYPE";
    public static final String SUB_REGION_TYPE              = "SUB_REGION_TYPE";
    public static final String FLAG_TYPE                    = "FLAG_TYPE";
    public static final String WIN_DISPLAY_TYPE             = "WIN_DISPLAY";
    
    public static final String INVISIBLE_STATE      = "INVISIBLE_STATE";
    public static final String VISIBLE_STATE        = "VISIBLE_STATE";
    public static final String MOUSE_OVER_STATE     = "MOUSE_OVER_STATE";
    
    public static final String WELCOME_SCREEN           = "WELCOME_SCREEN";
    public static final String ACCOUNTS_SCREEN          = "ACCOUNTS_SCREEN";
    public static final String CURRENT_ACCOUNT_SCREEN   = "CURRENT_ACCOUNT_SCREEN";
    public static final String ACCOUNT_CREATION_SCREEN  = "ACCOUNT_CREATION_SCREEN";
    public static final String GAMEPLAY_SCREEN          = "GAMEPLAY_SCREEN";
    public static final String SUBREGION_MODE           = "SUBREGION_MODE";
    public static final String FLAG_MODE                = "FLAG_MODE";
    public static final String CAPITAL_MODE             = "CAPITAL_MODE";
    public static final String LEADER_MODE              = "LEADER_MODE";
    
    public static final int SUB_STACK_VELOCITY = 2;
    public static final int FIRST_REGION_Y_IN_STACK = GAME_HEIGHT - 50;
    public static final int STACK_TRANSPARENCY = 70;
    
    public static final Color REGION_NAME_COLOR     = new Color(220, 220, 80);
    public static final Color SUBREGION_NAME_COLOR  = new Color(10, 10, 100);
    
    public static final String AUDIO_DIR = "./data/audio/";
    public static final String SUCCESS_FILE_NAME = AUDIO_DIR + "Success.wav";
    public static final String FAILURE_FILE_NAME = AUDIO_DIR + "Failure.wav";
    public static final String TRACKED_FILE_NAME = AUDIO_DIR + "RambleOn.mid";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
    public static final String TRACKED_SONG = "TRACKED_SONG";
    public static final String ANTHEM = "ANTHEM";
    
    public static final Font STATS_FONT = new Font("Serif", Font.BOLD, 24);
    public static final Color STATS_COLOR = new Color(200, 160, 80);
    public static final int STATS_X = 5;
    public static final int STATS_Y = 665;        
    
    public static final Font WIN_STATS_FONT = new Font("Sans Serif", Font.BOLD, 24);
    public static final Font WIN_EQUATION_FONT = new Font("Monospaced", Font.BOLD, 16);
    public static final Color WIN_STATS_COLOR = new Color(5, 5, 120);
    public static final int WIN_STATS_X = 400;
    public static final int WIN_STATS_START_Y = 330;
    public static final int WIN_STATS_Y_INC = 40;
    public static final int MAX_SCORE = 10000;
    
    private String currentScreen;
    ButtonHandler welcomeScreenHandler, createAccountHandler, okButtonHandler, 
            cancelButtonHandler, logOffButtonHandler, playButtonHandler, subRegionModeButtonHandler,
            flagModeButtonHandler, capitalModeButtonHandler, leaderModeButtonHandler;
    
    public AudioManager getAudio() { return audio; }
    public void initAudio() {
        audio = new AudioManager();
        try {
            audio.loadAudio(TRACKED_SONG, TRACKED_FILE_NAME);
            audio.play(TRACKED_SONG, true);
            audio.loadAudio(SUCCESS, SUCCESS_FILE_NAME);
            audio.loadAudio(FAILURE, FAILURE_FILE_NAME);
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(window, e.getStackTrace());
        }
    }

    public RambleOn() {
        super(APP_TITLE, FRAME_RATE);
        initAudio();
    }

    @Override
    public void initData() {
        data = new RambleOnDataModel();
        data.setGameDimensions(GAME_WIDTH, GAME_HEIGHT);
        initSpriteTypes();
        
        boundaryLeft 	= 0;
        boundaryRight 	= GAME_WIDTH;
        boundaryTop 	= 0;
        boundaryBottom 	= GAME_HEIGHT;
    }

    @Override
    public void initGUIControls() {
        canvas = new RambleOnPanel(this, (RambleOnDataModel)data);

        SpriteType sT;
        BufferedImage img;
        int x, y, vX, vY;
        Sprite s;
        
        sT = new SpriteType(BACKGROUND_TYPE);
        img = loadImage(BACKGROUND_FILE_PATH);
        x = 0;
        y = 0;
        vX = 0;
        vY = 0;
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, x, y, vX, vY, VISIBLE_STATE);
	guiDecor.put(BACKGROUND_TYPE, s);
        
        sT = new SpriteType(WELCOME_TYPE);
        img = loadImage(WELCOME_FILE_PATH);
        x = 0;
        y = 0;
        vX = 0;
        vY = 0;
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, x, y, vX, vY, VISIBLE_STATE);
	guiButtons.put(WELCOME_TYPE, s);
        currentScreen = WELCOME_SCREEN;
        
        // LOAD THE SUBREGION MODE BUTTON FOR THE GAMEPLAY SCREEN
        sT = new SpriteType(SUBREGION_MODE_BUTTON_TYPE);
        img = loadImage(SUBREGION_BUTTON_FILE_PATH);
        x = 950;
        y = 100;
        vX = 0;
        vY = 0;
        sT.addState(VISIBLE_STATE, img);
        sT.addState(INVISIBLE_STATE, img);
        s = new Sprite(sT, x, y, vX, vY, INVISIBLE_STATE);
	guiButtons.put(SUBREGION_MODE_BUTTON_TYPE, s);
        
        // AND THE WIN CONDITION DISPLAY
        sT = new SpriteType(WIN_DISPLAY_TYPE);
        img = loadImage(WIN_DISPLAY_FILE_PATH);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth()/2) - (img.getWidth(null)/2);
        y = (data.getGameHeight()/2) - (img.getHeight(null)/2);
        img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        sT.addState(INVISIBLE_STATE, img);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(WIN_DISPLAY_TYPE, s);
    }

    @Override
    public void initGUIHandlers() {
        welcomeScreenHandler = new ButtonHandler(this, WELCOME_TYPE);
        guiButtons.get(WELCOME_TYPE).setActionListener(welcomeScreenHandler);
        subRegionModeButtonHandler = new ButtonHandler(this, SUBREGION_MODE_BUTTON_TYPE);
    }

    @Override
    public void reset() {
        data.reset(this);
    }
    
    @Override
    public void updateGUI() {
        if (data.won()) {
            guiDecor.get(WIN_DISPLAY_TYPE).setState(VISIBLE_STATE);
        }	
    }
    
    private void initSpriteTypes() {
        BufferedImage imageToLoad;     
        SpriteType subRegionSpriteType = new SpriteType(SUB_REGION_TYPE);
        imageToLoad = loadImageWithColorKey(SUB_REGION_FILE_PATH, COLOR_KEY);
        subRegionSpriteType.addState(VISIBLE_STATE, imageToLoad);
        data.addSpriteType(subRegionSpriteType);
    }
    
    public void changeMap(String newMapPath) {
        SpriteType mapSpriteType = new SpriteType(MAP_TYPE);
        BufferedImage imageToLoad = loadImageWithColorKey(newMapPath, COLOR_KEY);
        mapSpriteType.addState(VISIBLE_STATE, imageToLoad);
        data.addSpriteType(mapSpriteType);
        Sprite mapSprite = new Sprite(mapSpriteType, 0, 0, 0, 0, VISIBLE_STATE);
        guiButtons.put(MAP_TYPE, mapSprite);
        MapClickHandler mch = new MapClickHandler(this);
        mapSprite.setMouseListener(mch);
    }
    
    public void loadWorldMap() {
        SpriteType mapSpriteType = new SpriteType(MAP_TYPE);
        BufferedImage imageToLoad = loadImageWithColorKey(WORLD_MAP_FILE_PATH, COLOR_KEY);
        mapSpriteType.addState(VISIBLE_STATE, imageToLoad);
        data.addSpriteType(mapSpriteType);
        Sprite mapSprite = new Sprite(mapSpriteType, 0, 0, 0, 0, VISIBLE_STATE);
        guiButtons.put(MAP_TYPE, mapSprite);
        MapClickHandler mch = new MapClickHandler(this);
        mapSprite.setMouseListener(mch);
    }
    
    // ENABLE/DISABLE GUI ELEMENTS AS APPROPRIATE
    public void changeScreenTo(String screen) {
        switch (screen) {
            case GAMEPLAY_SCREEN:
                // DISABLE
                guiButtons.get(WELCOME_TYPE).setActionListener(null);
                guiButtons.get(WELCOME_TYPE).setState(INVISIBLE_STATE);
                // ENABLE
                guiButtons.get(SUBREGION_MODE_BUTTON_TYPE).setState(VISIBLE_STATE);
                guiButtons.get(SUBREGION_MODE_BUTTON_TYPE).setActionListener(subRegionModeButtonHandler);
                loadWorldMap();
                break;
        }
    }
    
    public void disableGameModeButtons() {
        guiButtons.get(SUBREGION_MODE_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SUBREGION_MODE_BUTTON_TYPE).setActionListener(null);
    }
    
    public void startGame(String gameMode) {
        if(gameMode.equals(SUBREGION_MODE)) {
            disableGameModeButtons();
            ((RambleOnDataModel)data).initSubRegionModeData(this);
        } 
        
    }
    
    public void reload() {
        int numSubRegions = ((RambleOnDataModel)data).getRegionsFound() + ((RambleOnDataModel)data).getRegionsNotFound();
        this.boundaryTop = - (numSubRegions * 50);
    }
    
    public static void main(String[] args) {
        RambleOn game = new RambleOn();
        game.startGame();
    }
}