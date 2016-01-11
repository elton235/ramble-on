package rambleon.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import rambleon.RambleOn;
import rambleon.RambleOnDataModel;

public class MapClickHandler implements MouseListener {
    private RambleOn game;
    
    public MapClickHandler(RambleOn initGame) {
        game = initGame;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if(((RambleOnDataModel)(game.getDataModel())).gameInProgress == false) {
            if(me.getButton() == MouseEvent.BUTTON1)
            ((RambleOnDataModel)(game.getDataModel())).zoomIn(game);
        else if(me.getButton() == MouseEvent.BUTTON3)
            ((RambleOnDataModel)(game.getDataModel())).zoomOut(game);
        } else {
            ((RambleOnDataModel)(game.getDataModel())).respondToMapSelection(game);
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }
    
    
}