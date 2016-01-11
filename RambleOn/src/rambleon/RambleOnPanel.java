package rambleon;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JPanel;
import mini_game.*;
import static rambleon.RambleOn.*;
import rambleon.sprites.TextualSprite;

public class RambleOnPanel extends JPanel {
    private MiniGame game;
    private RambleOnDataModel data;
    
    public RambleOnPanel(MiniGame initGame, RambleOnDataModel initData) {
        game = initGame;
        data = initData;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        renderToGraphicsContext(g);
    }
    
    public void renderToGraphicsContext(Graphics g) {
        renderBackground(g);
	renderGUIControls(g);
        renderMap(g);
        renderSubRegionStack(g);
        renderStats(g);
        renderWin(g);
    }
    
    public void renderWin(Graphics g) {
        if (data.won()) {
            Sprite s = game.getGUIDecor().get(WIN_DISPLAY_TYPE);
            renderSprite(g, s);

            g.setFont(WIN_STATS_FONT);
            g.setColor(WIN_STATS_COLOR);
            
            g.drawString("Region: " + data.getRegionName(),                     WIN_STATS_X, WIN_STATS_START_Y);
            g.drawString("Score: " + data.calculateScore(),                     WIN_STATS_X, WIN_STATS_START_Y + WIN_STATS_Y_INC);
            g.drawString("Game Duration: " + data.getGameWinDurationText(),     WIN_STATS_X, WIN_STATS_START_Y + (WIN_STATS_Y_INC*2));
            g.drawString("Sub Regions: " + data.getNumberOfSubRegions(),        WIN_STATS_X, WIN_STATS_START_Y + (WIN_STATS_Y_INC*3));
            g.drawString("Incorrect Guesses: " + data.getIncorrectGuesses(),    WIN_STATS_X, WIN_STATS_START_Y + (WIN_STATS_Y_INC*4));
        }
    }
    
    public void renderBackground(Graphics g) {
        Sprite background = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, background);
    }
    
    public void renderMap(Graphics g) {
        Sprite map = game.getGUIButtons().get(MAP_TYPE);
        if (map != null) {
            renderSprite(g, map);
        }
    }
    
    public void renderSubRegionStack(Graphics g) {
        String regionName = data.getRegionName();
        if (regionName != null) {
            LinkedList<TextualSprite> list = data.getSubRegionStack();
            Iterator<TextualSprite> it = list.iterator();
            String textToRender = "Regions Of " +regionName;
            Font font = getMaxFitFont(g, textToRender, 280, 40, "Serif", Font.BOLD, 40);
            int counter = 0;
            while (it.hasNext() && (counter < 10)) {
                TextualSprite ts = it.next();
                regionName = ts.getTextToRender();
                int x = (int)(ts.getX());
                int y = (int)(ts.getY());
                int width = (int)(ts.getAABBwidth());
                int height = (int)(ts.getAABBheight());
                Color fillColor = data.getColorMappedToSubRegion(regionName);
                Color textColor = SUBREGION_NAME_COLOR;
                if (counter == 0) {
                    fillColor = new Color(100, 220, 100);
                    textColor = new Color(255, 0, 0);
                }
                g.setColor(fillColor);
                g.fill3DRect(x, y, width, height, true);
                g.setColor(textColor);
                g.drawString(regionName, x, y + 40);                
                counter++;
            }
            g.setColor(Color.BLACK);
            g.fillRect(900, 150, 300, 50);
            g.setColor(REGION_NAME_COLOR);
            g.setFont(font);
            g.drawString(textToRender, 910, 190);
        }
    }
    
    public Font getMaxFitFont(Graphics g, String text, int maxWidth, int maxHeight, String fontFamily, int fontStyle, int desiredPointSize) {
        Font f = null; 
        boolean bestFontFound = false;
        while (!bestFontFound)
        {
            f = new Font(fontFamily, fontStyle, desiredPointSize);
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            Rectangle2D textBounds = fm.getStringBounds(text, g);
            double textWidth = textBounds.getWidth();
            double textHeight = textBounds.getHeight();
            if ((textWidth > maxWidth) || (textHeight > maxHeight))
            {
                desiredPointSize--;
                if (desiredPointSize < 1)
                    return f;
            }
            else
            {
                bestFontFound = true;
            }
        }
        return f;
    }
    
    public void renderGUIControls(Graphics g) {
        Sprite s = game.getGUIButtons().get(WELCOME_TYPE);
        renderSprite(g, s);
        
        s = game.getGUIButtons().get(SUBREGION_MODE_BUTTON_TYPE);
        renderSprite(g, s);
    }
    
    public void renderStats(Graphics g) {
        if (data.inProgress()) {
            String statsText = data.getGameTimeText() 
                + "    Regions Found: " + data.getRegionsFound()
                + "    Regions Left: " + data.getRegionsNotFound()
                + "    Incorrect Guesses: " + data.getIncorrectGuesses();
            g.setFont(STATS_FONT);
            g.setColor(STATS_COLOR);
            g.drawString(statsText, STATS_X, STATS_Y);
        }
    }
    
    public void renderSprite(Graphics g, Sprite s) {
        if (!s.getState().equals(INVISIBLE_STATE)) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null);
        }
    }
    
    public void renderDebuggingText(Graphics g) {
    }
}