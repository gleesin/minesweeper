package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import bbc.juniperus.games.minesweeper.core.CellInfo;
import bbc.juniperus.games.minesweeper.core.Coordinate;
import bbc.juniperus.games.minesweeper.gui.resources.ResourceManager;

@SuppressWarnings("serial")
public class CellGui extends JPanel {

	private enum State {DEFAULT, PRESSED, REVEALED}
	
	public static final int HEIGHT = 20;
	public static final int WIDTH = HEIGHT;
	private static final int BORDER_WIDTH = 3;
	public static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
	private static final Color colorBackground = new Color(189, 189, 189);
	private static final Color colorHitMine = Color.red;
	
	private static final Color colorBorder =new Color(123, 123, 123);
	
	private static final Border borderRevealed = BorderFactory.createMatteBorder(1, 1, 0, 0, colorBorder);
	private static final Icon[] numberIcons = ResourceManager.getInstance().createFieldNumberIcons(10);
	private static final Icon mineIcon = ResourceManager.getInstance().createMineIcon(13);
	private static final Icon crossedMineIcon = ResourceManager.getInstance().createCrossedMineIcon(13);
	private static final Image flagImg = ResourceManager.getInstance().getFlagImage(9);
	private static final Image questionMarkImg = ResourceManager.getInstance().getQuestionMarkImage(7);
	private static final Icon questionMarkIcon = new ImageIcon(questionMarkImg);
	
	private State state;
	private JLabel label;
	private CellInfo cellInfo;
	private Set<CellGuiListener> listeners = new HashSet<CellGuiListener>();
	
	public CellGui(CellInfo  info){
		setBackground(colorBackground);
		setBorder(borderRevealed);
		setSize(WIDTH, HEIGHT);
		cellInfo = info;
		label = new JLabel();
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
				
				if (state == State.REVEALED ||  //Ignore if this field is already revealed.
						e.getButton() != MouseEvent.BUTTON1 || //Ignore if this is not the left click.
						cellInfo.hasFlag() ) //Ignore if the underlying cell has flag
					return;
				
				if (cellInfo.hasQuestionMark()) //If it has question mark show it also when the cell is pressed.
					label.setIcon(questionMarkIcon);
				else
					label.setIcon(null); //Null icon if there is no question mark (prevents label having this icon
										 //when cell is pressed after the question mark has been removed).
				
				state = State.PRESSED;
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (state == State.REVEALED) 
					return;
				
				int x = e.getX();
				int y = e.getY();
				int w = CellGui.this.getWidth();
				int h = CellGui.this.getHeight();
				if ( x > 0 && y > 0 &&
						(x <= w) && ( y <=h))
					clicked(e.getButton());
				else{
					//Mouse was released with pointer being outside bounds of this component.
					state = State.DEFAULT;
					repaint(); // Paint back to normal.
				}
				
			}
			
		});
		state = State.DEFAULT;
	}
	
	
	public void addListener(CellGuiListener listener){
		listeners.add(listener);
	}
	
	
	@Override
	public void paint(Graphics g){
		
	
		if (state == State.DEFAULT){
			setOpaque(true);
			
			g.setColor(colorBackground);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			g.setColor(Color.WHITE);
			
			for (int i = 0; i < BORDER_WIDTH;i ++){
				g.drawLine(0, i, WIDTH -1 -i ,i); //Horizontal line on top
				g.drawLine(i, 0, i,HEIGHT - 1 -i); //Vertical line on the left
			}
			
			g.setColor(colorBorder);
			for (int i = BORDER_WIDTH -1; i > 0; i--){
				g.drawLine(i,HEIGHT-i ,WIDTH ,HEIGHT - i); //Horizontal line at the bottom
				g.drawLine(WIDTH - i, i, WIDTH -i,HEIGHT); //Vertical line on the right
			}
			
			if (cellInfo.hasQuestionMark()){ //Paint flag image over it if model cell is flagged.
				int x = Math.round((WIDTH - questionMarkImg.getWidth(null))/2.0f);
				int y = Math.round((HEIGHT - questionMarkImg.getHeight(null))/2.0f);
				g.drawImage(questionMarkImg, x, y, null);;
			}
			
			if (cellInfo.hasFlag()){
				int x = Math.round((WIDTH - flagImg.getWidth(null))/2.0f);
				int y = Math.round((HEIGHT - flagImg.getHeight(null))/2.0f);
				g.drawImage(flagImg, x, y, null);;
			}
			
			
		}else
			super.paint(g);
	}
	

	public void update(){
		if (cellInfo.isRevealed()){
			state = State.REVEALED;
			setRevealedLook();
			return;
		}
	}
	
	public Coordinate getCoordinate(){
		return cellInfo.getCoordinate();
	}

	private void setRevealedLook(){
		Icon icon = null;
		System.out.println(cellInfo.hasMine());
		
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
