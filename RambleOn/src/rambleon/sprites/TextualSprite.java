package rambleon.sprites;

import mini_game.Sprite;
import mini_game.SpriteType;

public class TextualSprite extends Sprite {
    private String textToRender;
    
    public TextualSprite(
                        String initTextToRender,
                        SpriteType initSpriteType,
			float initX, 	float initY,
			float initVx, 	float initVy,
			String initState) {
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        textToRender = initTextToRender;
    }
    
    public String getTextToRender() {
        return textToRender; 
    }
    
    public void setTextToRender(String initTextToRender) {
        textToRender = initTextToRender;
    }
}