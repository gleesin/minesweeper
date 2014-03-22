package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	
	public static final int HEIGHT = 22;
	public static final int WIDTH = 22;
	private static final int BORDER_WIDTH = 3;
	public static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
	private static final Color colorBackground = new Color(189, 189, 189);
	private static final Color colorHitMine = Color.red;
	
	private static final Color colorBorder =new Color(123, 123, 123);
	
	private static final Border borderRevealed = BorderFactory.createMatteBorder(1, 1, 0, 0, colorBorder);
	private static final Icon[] numberIcons = ResourceManager.getInstance().createFieldNumberIcons(10);
	private static final Icon mineIcon = ResourceManager.getInstance().createMineIcon(13);
	private static final Icon crossedMineIcon = ResourceManager.getInstance().createMineIcon(13);
	private static final Icon flagIcon = ResourceManager.getInstance().createFlagIcon(10);
	
	
	public static final String REVEALED_PROPERTY = "REVEALED_PROPERTY";
	public static final String FLAG_SET_PROPERTY = "FLAG_SET_PROPERTY";
	
	private boolean hasFlag;
	private State state;
	private JLabel label;
	private CellInfo cellInfo;
	private boolean initialized;
	
	public CellGui(CellInfo  info){
		setBackground(colorBackground);
		setBorder(borderRevealed);
		setSize(WIDTH, HEIGHT);
		cellInfo = info;
		label = new JLabel();
		add(label);
	
		addMouseListener(new MouseAdapter(){

			/**
			 * Sets the pressed look if the cell has not been already revelaed
			 * and if the click comes from left mouse button.
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				if (state == State.REVEALED ||  //Ignore if this field is already revealed.
						e.getButton() != MouseEvent.BUTTON1) //Ignore if this is not the left click.
					return;
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
				//setDefaultLook();
				/*
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				*/
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
		initialized = true;
		state = State.DEFAULT;
	}
	
	
	@Override
	public void paint(Graphics g){
		
		if (state == State.DEFAULT){
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
			label.paint(g);
		}else
			super.paint(g);
	}
	

	public void update(){
		if (cellInfo.isRevealed() && 
				(state != State.REVEALED) &&
				!cellInfo.hasFlag()){
			state = State.REVEALED;
			setRevealedLook();
			return;
		}
		
		setFlaggedLook(cellInfo.hasFlag());
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
		}
		else{
			int count = cellInfo.getsetNearbyMinesCount();
			if (count > 0)
				icon = numberIcons[count-1];
		}
		label.setIcon(icon);
	}
	
	private void setFlaggedLook(boolean hasFlag){
		if (hasFlag)
			label.setIcon(flagIcon);
		else
			label.setIcon(null);
	}
	
	
	
	private void clicked(int button){
		
		if (button == MouseEvent.BUTTON1)
			firePropertyChange(REVEALED_PROPERTY, false, true);
		else if (button == MouseEvent.BUTTON3){
			hasFlag = !hasFlag;
			firePropertyChange(FLAG_SET_PROPERTY, !hasFlag, hasFlag);
		}
	}
	
	
	@Override
	public Dimension getPreferredSize(){
		return dimension;
	}
	
}
