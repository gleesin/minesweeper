package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import bbc.juniperus.games.minesweeper.core.CellInfo;
import bbc.juniperus.games.minesweeper.core.Coordinate;
import bbc.juniperus.games.minesweeper.gui.swing.ResourceLoader.ImageResource;
import bbc.juniperus.games.minesweeper.gui.swing.ResourceLoader.ImageSetResource;

@SuppressWarnings("serial")
public class CellView extends JPanel {

    private enum ButtonAction {LEFT, RIGHT};
    
    public static final int HEIGHT = 16;
    public static final int WIDTH = HEIGHT;
    private static final int BORDER_WIDTH = 2;
    public static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
    private static final Color colorBackground = GameView.MAIN_COLOR;
    private static final Color colorHitMine = Color.red;
    private static final Color colorBorder = GameView.DARK_COLOR;
    
    private static final Border border = new MineSweeperBorder(BORDER_WIDTH, GameView.LIGHT_COLOR, colorBorder);
    private static final Border borderRevealed = BorderFactory.createMatteBorder(1, 1, 0, 0, colorBorder);
    private static final Icon[] numberIcons = ResourceLoader.getInstance().createIconSet(ImageSetResource.MINEFIELD_NUMBERS, 10);
    private static final Icon mineIcon = ResourceLoader.getInstance().createIcon(ImageResource.MINE, 13);
    private static final Icon crossedMineIcon = ResourceLoader.getInstance().createIcon(ImageResource.CROSSED_MINE, 13);
    private static final Icon questionMarkIcon = ResourceLoader.getInstance().createIcon(ImageResource.QUESTION_MARK, 6);
    private static final Icon flagIcon = ResourceLoader.getInstance().createIcon(ImageResource.FLAG, 8);

    private static final Border labelPressedBorder = BorderFactory.createEmptyBorder(1, 1, 0, 0);
    
    private boolean isRevealed;
    private JLabel label;
    private CellInfo cellInfo;
    private Set<CellGuiObserver> listeners = new HashSet<CellGuiObserver>();
    private boolean isPressed;
    
    public CellView(final CellInfo  cellInfo){
        super(new BorderLayout());
        setBackground(colorBackground);
        setSize(WIDTH, HEIGHT);
        this.cellInfo = cellInfo;
    	
        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        setBorder(border);
        add(label);
    }
    
    

    void mousePressed(int button){
        if (isRevealed) 
            return;
    	
        if (button == MouseEvent.BUTTON3)
            fireButtonActivated(ButtonAction.RIGHT); //Activate flag/question mark right after right  button was pressed.
        else{
            if (!cellInfo.hasFlag()){
                setPressed(); //Just show pressed look. Revealing the cell is activated upon the release of the left mouse button.
        	}
        }
    }
    
    /**
     * {@link MouseEvent#BUTTON1} (left mouse button) has been released when the mouse cursor
     * was over this component (and it was pressed).
     */
    void mouseReleased(){
        if (isRevealed || cellInfo.hasFlag())
            return;
        assert isPressed;
    	
        fireButtonActivated(ButtonAction.LEFT);
        label.setBorder(null);
    }
    
    /**
     * Cancels the pressed state without the release of the mouse button.
     */
    void unpressMouse(){
        if (isRevealed)
            return;
        setUnpressed();
    }
    
    private void setPressed(){
        isPressed= true;
        setBorder(borderRevealed);
        label.setBorder(labelPressedBorder);
        repaint();
    }
    
    private void setUnpressed(){
        isPressed = false;
        setBorder(border);
        repaint(); // Paint back to normal.
    }
    
    public void addListener(CellGuiObserver listener){
        listeners.add(listener);
    }
    

    
    public void update(){
        if (cellInfo.isRevealed()){
            isRevealed = true;
            setRevealedLook();
            return;
        }
    	
        Icon icon = null;
    	
        if (cellInfo.hasQuestionMark())
            icon = questionMarkIcon;
        else if (cellInfo.hasFlag())
            icon = flagIcon;
    	
        label.setIcon(icon);
    }
    
    public Coordinate getCoordinate(){
        return cellInfo.getCoordinate();
    }

    private void setRevealedLook(){
        Icon icon = null;
        setBorder(borderRevealed);
        if (cellInfo.hasMine()){
            icon = mineIcon;
            if (cellInfo.wasMineHit())
                setBackground(colorHitMine);
            else if (cellInfo.hasFlag())
                icon = crossedMineIcon;
        }
        else{
            int count = cellInfo.getsetNearbyMinesCount();
            if (count > 0)
                icon = numberIcons[count-1];
        }
    	
        label.setIcon(icon);
    }
    
    
    private void fireButtonActivated(ButtonAction button){
        if (button == ButtonAction.LEFT)
            for (CellGuiObserver listener : listeners)
                listener.leftButtonActivated(cellInfo.getCoordinate());
        else
            for (CellGuiObserver listener : listeners)
                listener.rightButtonActivated(cellInfo.getCoordinate());
    }
    
    @Override
    public Dimension getPreferredSize(){
        return dimension;
    }	
}
