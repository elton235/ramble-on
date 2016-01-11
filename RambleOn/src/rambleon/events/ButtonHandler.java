package rambleon.events;
import java.awt.event.*;
import rambleon.RambleOn;
import static rambleon.RambleOn.*;

public class ButtonHandler implements ActionListener {
    private RambleOn game;
    private String type;
    
    public ButtonHandler(RambleOn initGame, String initType) {
        game = initGame;
        type = initType;
    }
    
    public void actionPerformed(ActionEvent ae) {
        switch (type) {
            case WELCOME_TYPE:
                game.changeScreenTo(GAMEPLAY_SCREEN);
                break;
            case SUBREGION_MODE_BUTTON_TYPE:
                game.startGame(SUBREGION_MODE);
                break;
        }
    }
}