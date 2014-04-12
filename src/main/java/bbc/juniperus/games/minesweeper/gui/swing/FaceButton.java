package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;

import bbc.juniperus.games.minesweeper.gui.swing.ResourceLoader.ImageSetResource;


@SuppressWarnings("serial")
public class FaceButton extends JButton{

    private static final Border INSIDE_BORDER = new MineSweeperBorder(2,GameView.LIGHT_COLOR,GameView.DARK_COLOR);
    private static final Border INSIDE_BORDER_PRESSED = BorderFactory.createMatteBorder(1, 1, 0, 0, GameView.DARK_COLOR);
    private static final Border OUTSIDE_BORDER = new MineSweeperBorder(1,GameView.DARK_COLOR,GameView.DARK_COLOR);
    //private static final Border OUTSIDE_BORDER = BorderFactory.createLineBorder(Color.black);
    private static final Border BORDER = BorderFactory.createCompoundBorder(OUTSIDE_BORDER, INSIDE_BORDER);
    private static final Border BORDER_PRESSED = BorderFactory.createCompoundBorder(OUTSIDE_BORDER, INSIDE_BORDER_PRESSED);
    
    private static final Icon[] faceIcons = ResourceLoader.getInstance().createIconSet(ImageSetResource.FACES, 17);
    
    
    //Need to keep the enum order as it is! Determines icon from icon array. (Lazy, not ideal, solution).
    public enum Face {NORMAL, SUSPENDED, DEAD, VICTORIOUS}; 
    
    private Face face,previousFace;
    
    public FaceButton() {
        setBorder(BORDER);
        setBackground(GameView.MAIN_COLOR);
        setContentAreaFilled(false);
        setFocusable(false);
        //Mouse listener for changing the look accordingly,
        addMouseListener(new MouseAdapter(){
        	
        	
            @Override
            public void mousePressed(MouseEvent e){
                setBackground(GameView.MAIN_COLOR);
                setBorder(BORDER_PRESSED);
                previousFace = face;
                setFace(Face.NORMAL);
        	}
        	
            @Override
            public void mouseReleased(MouseEvent e){
                setBorder(BORDER);
                setFace(previousFace);
        	}
        	
        });
    	
    	
        setFace(Face.NORMAL);
    }
    
    public void reset(){
        setFace(Face.NORMAL);
        previousFace = Face.NORMAL;
    }
    
    public void setFace(Face face){
        setIcon(faceIcons[face.ordinal()]);
        this.face = face;
    }
    
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(26, 26);
    }
}
