package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
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
import bbc.juniperus.games.minesweeper.gui.resources.ResourceManager;

@SuppressWarnings("serial")
public class CellGui extends JPanel {

	public static final int HEIGHT = 16;
	public static final int WIDTH = HEIGHT;
	private static final int BORDER_WIDTH = 2;
	public static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
	private static final Color colorBackground = GameView.MAIN_COLOR;
	private static final Color colorHitMine = Color.red;
	private static final Color colorBorder = GameView.DARK_COLOR;
	
	private static final Border border = new CellBorder(BORDER_WIDTH, GameView.LIGHT_COLOR, colorBorder);
	private static final Border borderRevealed = BorderFactory.createMatteBorder(1, 1, 0, 0, colorBorder);
	private static final Icon[] numberIcons = ResourceManager.getInstance().createFieldNumberIcons(10);
	private static final Icon mineIcon = ResourceManager.getInstance().createMineIcon(13);
	private static final Icon crossedMineIcon = ResourceManager.getInstance().createCrossedMineIcon(13);
	private static final Icon questionMarkIcon = ResourceManager.getInstance().createQuestionMarkIcon(6);
	private static final Icon flagIcon = ResourceManager.getInstance().createFlagIcon(8);

	private static final Border labelPressedBorder = BorderFactory.createEmptyBorder(1, 1, 0, 0);
	
	private boolean isRevealed;
	private JLabel label;
	private CellInfo cellInfo;
	private Set<CellGuiListener> listeners = new HashSet<CellGuiListener>();
	
	public CellGui(CellInfo  info){
		super(new BorderLayout());
		setBackground(colorBackground);
		setSize(WIDTH, HEIGHT);
		cellInfo = info;
		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		setBorder(border);
		add(label);
		
		addMouseListener(new MouseAdapter(){

			/*
			int pressedButton;
			
			@Override 
			public void mouseEntered(MouseEvent e){
				
				if (e.getButton() == MouseEvent.BUTTON2)
				System.out.println("Mouse entered");
			}
			*/
			
			
			/**
			 * Sets the pressed look if the cell has not been already revelaed
			 * and if the click comes from left mouse button.
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				//System.out.println("Mouse pressed" + e.getButton());
				
				if (isRevealed ||  //Ignore if this field is already revealed.
						e.getButton() != MouseEvent.BUTTON1 || //Ignore if this is not the left click.
						cellInfo.hasFlag() ) //Ignore if the underlying cell has flag
					return;
				
				setBorder(borderRevealed);
				label.setBorder(labelPressedBorder);
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (isRevealed) 
					return;
				
				label.setBorder(null);
				
				int x = e.getX();
				int y = e.getY();
				int w = CellGui.this.getWidth();
				int h = CellGui.this.getHeight();
				if ( x > 0 && y > 0 &&
						(x <= w) && ( y <=h))
					clicked(e.getButton());
				else{
					//Mouse was released with pointer being outside bounds of this component.
					setBorder(border);
					repaint(); // Paint back to normal.
				}
				
			}
			
		});
	}
	
	
	public void addListener(CellGuiListener listener){
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
	

	
	
	private void clicked(int button){
		
		
		if (button == MouseEvent.BUTTON1)
			for (CellGuiListener listener : listeners)
				listener.leftMouseButtonClicked(cellInfo.getCoordinate());
		else if (button == MouseEvent.BUTTON2)
			for (CellGuiListener listener : listeners)
				listener.middleMouseButtonClicked(cellInfo.getCoordinate());
		else if (button == MouseEvent.BUTTON3)
			for (CellGuiListener listener : listeners)
				listener.rightMouseButtonClicked(cellInfo.getCoordinate());
	}
	
	
	@Override
	public Dimension getPreferredSize(){
		return dimension;
	}
	
}
