package rambleon;

import audio_manager.AudioManager;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import mini_game.*;
import org.w3c.dom.*;
import static rambleon.RambleOn.*;
import rambleon.sprites.TextualSprite;
import xml_utilities.InvalidXMLFileFormatException;
import xml_utilities.XMLUtilities;

public class RambleOnDataModel extends MiniGameDataModel {
    private String regionName;
    private String subRegionsType;
    private HashMap<Color, String> colorToSubRegionMappings;
    private HashMap<String, Color> subRegionToColorMappings;
    private HashMap<String, ArrayList<Point2D.Double>> pixels;
    private LinkedList<String> redSubRegions;
    private LinkedList<TextualSprite> subRegionStack;
    private XMLUtilities xmlUtil;
    private String currentPath;
    private String currentMap;
    private int magnificationLevel;
    public boolean gameInProgress;
    public String gameMode;
    private GregorianCalendar startTime;
    private GregorianCalendar winEndingTime;
    private int incorrectGuesses;
    private String anthem;
    
    public RambleOnDataModel() {
        colorToSubRegionMappings = new HashMap();
        subRegionToColorMappings = new HashMap();
        subRegionStack = new LinkedList();
        redSubRegions = new LinkedList();
        xmlUtil = new XMLUtilities();
        magnificationLevel = 0;
        currentPath = MAPS_PATH;
        currentMap = "The World";
        try {
            loadXMLFile();
        } catch (InvalidXMLFileFormatException ex) {
            // Deal with it
        }
        gameInProgress = false;
    }
    
    public int calculateScore() {
        int points = MAX_SCORE;
        long numMilliseconds = winEndingTime.getTimeInMillis() - startTime.getTimeInMillis();
        long numSeconds = numMilliseconds/1000L;
        points -= numSeconds;
        points -= (100 * incorrectGuesses);
        return points;
    }
    
    public String getGameWinDurationText() {
        long numMilliseconds = winEndingTime.getTimeInMillis() - startTime.getTimeInMillis();
        long numSeconds = numMilliseconds/1000L;
        return getSecondsAsTimeText(numSeconds);
    }
    
    // GETTERS
    public String getRegionName() { return regionName; }
    public String getSubRegionsType() { return subRegionsType; }
    public LinkedList<TextualSprite> getSubRegionStack() { return subRegionStack; }
    public int getNumberOfSubRegions() { return colorToSubRegionMappings.keySet().size(); }
    public String getCurrentPath() { return currentPath; }
    
    public void setRegionName(String initRegionName) { regionName = initRegionName; }
    public void setSubRegionsType(String initSubRegionsType) { subRegionsType = initSubRegionsType; }
    
    public int getRegionsFound() {
        return colorToSubRegionMappings.keySet().size() - subRegionStack.size();
    }
    
    public int getRegionsNotFound() {
        return subRegionStack.size();
    }
    
    public String getSecondsAsTimeText(long numSeconds) {
        long numHours = numSeconds/3600;
        numSeconds = numSeconds - (numHours * 3600);
        long numMinutes = numSeconds/60;
        numSeconds = numSeconds - (numMinutes * 60);
        
        String timeText = "";
        if (numHours > 0) { 
            timeText += numHours + ":"; 
        }
        timeText += numMinutes + ":";
        if (numSeconds < 10) {
            timeText += "0" + numSeconds;
        }
        else {
            timeText += numSeconds;
        }
        return timeText;
    }
    
    public String getGameTimeText() {
        if (startTime == null) {
            return "";
        }
        GregorianCalendar now = new GregorianCalendar();
        long diff = now.getTimeInMillis() - startTime.getTimeInMillis();
        long numSeconds = diff/1000L;

        return getSecondsAsTimeText(numSeconds);
    }
    
    // SETTERS
    public void addColorToSubRegionMappings(Color colorKey, String subRegionName) {
        colorToSubRegionMappings.put(colorKey, subRegionName);
    }
    
    public String getSubRegionMappedToColor(Color colorKey) {
        return colorToSubRegionMappings.get(colorKey);
    }
    
    public void addSubRegionToColorMappings(String subRegionName, Color colorKey) {
        subRegionToColorMappings.put(subRegionName, colorKey);
    }
    
    public Color getColorMappedToSubRegion(String subRegion) {
        return subRegionToColorMappings.get(subRegion);
    }
    
    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        
    }
    
    public void respondToMapSelection(RambleOn game) {
        Sprite map = game.getGUIButtons().get(MAP_TYPE);
        SpriteType mapType = map.getSpriteType();
        BufferedImage img = mapType.getStateImage(map.getState());
        int rgb = img.getRGB(lastMouseX, lastMouseY);
        Color pixelColor = new Color(rgb);
        String clickedSubRegion = colorToSubRegionMappings.get(pixelColor);
        if ((clickedSubRegion == null) || (subRegionStack.isEmpty())) {
            return;
        }
        if (clickedSubRegion.equals(subRegionStack.get(0).getTextToRender())) {
            game.getAudio().play(SUCCESS, false);
            changeSubRegionColorOnMap(game, clickedSubRegion, Color.GREEN);
            subRegionStack.removeFirst();
            for (String s : redSubRegions) {
                Color subRegionColor = subRegionToColorMappings.get(s);
                changeSubRegionColorOnMap(game, s, subRegionColor);
            }
            redSubRegions.clear();
            startStackSpritesMovingDown();
            if (subRegionStack.isEmpty()) {
                winEndingTime = new GregorianCalendar();
                this.endGameAsWin();
                game.getAudio().stop(TRACKED_SONG);
                game.getAudio().play(ANTHEM, false);
            }
        } else {
            if (!redSubRegions.contains(clickedSubRegion)) {
                game.getAudio().play(FAILURE, false);
                incorrectGuesses++;
                changeSubRegionColorOnMap(game, clickedSubRegion, Color.RED);
                redSubRegions.add(clickedSubRegion);
            }
        }
    }
    
    public void startStackSpritesMovingDown() {
        for (Sprite s : subRegionStack) {
            s.setVy(SUB_STACK_VELOCITY);
        }
    }
    
    public void changeSubRegionColorOnMap(RambleOn game, String subRegion, Color color) {
        Sprite map = game.getGUIButtons().get(MAP_TYPE);
        SpriteType mapType = map.getSpriteType();
        BufferedImage img = mapType.getStateImage(map.getState());
        ArrayList<Point2D.Double> subRegionPixels = pixels.get(subRegion);
        for (Point2D.Double p : subRegionPixels) {
            int rgb= color.getRGB();
            img.setRGB((int)(p.x), (int)(p.y), rgb);
        }
    }
    
    public int getIncorrectGuesses() { return incorrectGuesses; }
    
    public void zoomIn(RambleOn game) {
        if(magnificationLevel < 2) {
            Sprite map = game.getGUIButtons().get(MAP_TYPE);
            SpriteType mapType = map.getSpriteType();
            BufferedImage img = mapType.getStateImage(map.getState());
            int rgb = img.getRGB(lastMouseX, lastMouseY);
            Color pixelColor = new Color(rgb);
            String clickedSubRegion = colorToSubRegionMappings.get(pixelColor);
            if(clickedSubRegion == null) {
                return;
            }
            currentPath = currentPath + clickedSubRegion + "/";
            currentMap = clickedSubRegion;
            String currentMapPath = currentPath + currentMap + " Map.png";
            game.changeMap(currentMapPath);
            magnificationLevel += 1;
            colorToSubRegionMappings.clear();
            subRegionToColorMappings.clear();
            try {
                loadXMLFile();
            } catch (InvalidXMLFileFormatException ex) {
                // Do later
            }
        }
    }
    
    public void zoomOut(RambleOn game) {
        if(magnificationLevel > 0) {
            String[] tokens = currentPath.split("/");
            currentPath = "";
            for(int i=0; i<tokens.length-1; i++) {
                currentPath = currentPath + tokens[i] + "/";
            }
            if(magnificationLevel == 1) {
                currentMap = "The World";
            } else {
                currentMap = tokens[tokens.length-2];
            }
            String currentMapPath = currentPath + currentMap + " Map.png";
            game.changeMap(currentMapPath);
            magnificationLevel -= 1;
            colorToSubRegionMappings.clear();
            subRegionToColorMappings.clear();
            try {
                loadXMLFile();
            } catch (InvalidXMLFileFormatException ex) {
                // Do later
            }
        }
    }

    public void initSubRegionModeData(RambleOn game) {
        startTime = new GregorianCalendar();
        regionName = currentMap;
        File f = new File(currentPath + regionName + " National Anthem.mid");
        if(f.exists() && !f.isDirectory()) { 
            try {
                game.getAudio().loadAudio(ANTHEM, currentPath + regionName + " National Anthem.mid");
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(RambleOnDataModel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RambleOnDataModel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(RambleOnDataModel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidMidiDataException ex) {
                Logger.getLogger(RambleOnDataModel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MidiUnavailableException ex) {
                Logger.getLogger(RambleOnDataModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        SpriteType sT = this.getSpriteType(SUB_REGION_TYPE);
        int x = 900;
        for (Color c : colorToSubRegionMappings.keySet()) {
            String subRegion = colorToSubRegionMappings.get(c);
            subRegionToColorMappings.put(subRegion, c);
            TextualSprite subRegionSprite = new TextualSprite(subRegion, sT, x, 0, 0, 0, VISIBLE_STATE);
            subRegionStack.add(subRegionSprite);
        }
        Collections.shuffle(subRegionStack);
        int y = 600;
        int yInc = 50;
        for (TextualSprite ts : subRegionStack) {
            int tY = y+yInc;            
            ts.setY(tY);
            yInc -= 50;
        }
        ((RambleOn)game).reload();
        incorrectGuesses = 0;
        pixels = new HashMap();
        for (TextualSprite tS : subRegionStack) {
            pixels.put(tS.getTextToRender(), new ArrayList());
        }
        Sprite map = game.getGUIButtons().get(MAP_TYPE);
        SpriteType mapType = map.getSpriteType();
        BufferedImage img = mapType.getStateImage(map.getState());
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int rgb = img.getRGB(i, j);
                Color c = new Color(rgb);
                if (colorToSubRegionMappings.containsKey(c)) {
                    String subRegion = colorToSubRegionMappings.get(c);
                    ArrayList<Point2D.Double> subRegionPixels = pixels.get(subRegion);
                    subRegionPixels.add(new Point2D.Double(i, j));
                }
            }
        }
        // RESET THE AUDIO
        AudioManager audio = ((RambleOn)game).getAudio();    
        
        if (!audio.isPlaying(TRACKED_SONG))
        {
            audio.play(TRACKED_SONG, true);
        }
        gameMode = "SUBREGION";
        gameInProgress = true;
        beginGame();
        winEndingTime = null;
    }

    @Override
    public void reset(MiniGame game) {
        
    }
    
    @Override
    public void updateAll(MiniGame game) {
        for (Sprite s : subRegionStack) {
            s.update(game);
        }
        if (!subRegionStack.isEmpty()) {
            Sprite bottomOfStack = subRegionStack.get(0);
            if (bottomOfStack.getY() == FIRST_REGION_Y_IN_STACK) {
                for (Sprite s : subRegionStack) {
                    s.setVy(0);
                }
            }
        }
    }

    @Override
    public void updateDebugText(MiniGame game) {
        debugText.clear();
    }
    
    public void loadXMLFile() throws InvalidXMLFileFormatException {
        String xmlFile = currentPath + currentMap + " Data.xml";
        String xsdFile = MAPS_PATH + "RegionData.xsd";
        org.w3c.dom.Document doc = xmlUtil.loadXMLDocument(xmlFile, xsdFile);
        Node subRegionsListNode = doc.getElementsByTagName("region").item(0);
        ArrayList<Node> subRegionsList = xmlUtil.getChildNodesWithName(subRegionsListNode, "sub_region");
        for (Node regionNode : subRegionsList) {
            NamedNodeMap regionAttributes = regionNode.getAttributes();
            int red = Integer.parseInt(regionAttributes.getNamedItem("red").getNodeValue());
            int green = Integer.parseInt(regionAttributes.getNamedItem("green").getNodeValue());
            int blue = Integer.parseInt(regionAttributes.getNamedItem("blue").getNodeValue());
            String name = regionAttributes.getNamedItem("name").getNodeValue();
            Color regionColor = new Color(red, green, blue);
            addColorToSubRegionMappings(regionColor, name);
        }
    }
}