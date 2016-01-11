package rambleon.sprites;

import mini_game.Sprite;
import mini_game.SpriteType;

public class ImageSprite extends Sprite {
    private String imageLocation;
    private String imageName;
    
    public ImageSprite(
                        String initImageLocation,
                        SpriteType initSpriteType,
			float initX, 	float initY,
			float initVx, 	float initVy,
			String initState) {
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        imageLocation = initImageLocation;
        imageName = imageLocation.substring(0, imageLocation.lastIndexOf('.'));
    }
    
    public String getImageLocation() {
        return imageLocation; 
    }
    
    public void setImageLocation(String initImageLocation) {
        imageLocation = initImageLocation;
    }
    public String getImageName() {
        return imageName; 
    }
}