package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class DisplayPane extends JPanel{
    
    private static int HEIGHT = 43;
    private static final Border INSIDE_BORDER = new MineSweeperBorder(2,GameView.DARK_COLOR,GameView.LIGHT_COLOR);
    private static final Border OUTSIDE_BORDER = BorderFactory.createMatteBorder(0, 0, 6, 0, GameView.MAIN_COLOR);
    private static final Border BORDER = BorderFactory.createCompoundBorder(OUTSIDE_BORDER, INSIDE_BORDER);
    
    private FaceButton button;
    private Display flagDisplay, timeDisplay;
    
    public DisplayPane(){
        super(new BorderLayout());
        setBackground(GameView.MAIN_COLOR);
        setBorder(BORDER);
    	
        flagDisplay = new Display();
        timeDisplay = new Display();
    	
        JPanel leftPane = new JPanel();
        leftPane.setBackground(getBackground());
        leftPane.add(flagDisplay);
    	
    	
        JPanel rightPane = new JPanel();
        rightPane.setBackground(getBackground());
        rightPane.add(timeDisplay);
    	
        JPanel middlePane = new JPanel();
        middlePane.setBackground(getBackground());
        button = new FaceButton();
        middlePane.add(button);
    	
        add(middlePane);
        add(leftPane, BorderLayout.WEST);
        add(rightPane, BorderLayout.EAST);
    }
    
    
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(0, HEIGHT);
    }

    
    public FaceButton getFaceButton(){
        return button;
    }
    
    public Display getFlagDisplay(){
        return flagDisplay;
    }
    
    public Display getTimeDisplay(){
        return timeDisplay;
    }

}
