package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
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
	
	public static final int HEIGHT = 42;
	public static final int WIDTH = 42;
	private static final int BORDER_WIDTH = 5;
	public static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
	private static final Color colorBackground = new Color(189, 189, 189);
	private static final Color colorBorder =new Color(123, 123, 123);
	//private static final Border borderNormal = BorderFactory.createRaisedBevelBorder();
	private static final Border borderRevealed = BorderFactory.createMatteBorder(1, 1, 0, 0, colorBorder);
	private static final ImageIcon[] numberIcons = ResourceManager.getInstance().getFieldNumberIcons(25);
	
	public static final String REVELAED_PROPERTY = "REVELAED_PROPERTY";
	
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
		Font font = label.getFont().deriveFont(Font.BOLD);
		label.setFont(font);
		//label.setText(info.getCoordinate().toString());
		add(label);
		
		addMouseListener(new MouseAdapter(){

			@Override
			public void mousePressed(MouseEvent e) {
				if (state == State.REVEALED) //Ignore if this field is already revealed.
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
					reveal();
				else{
					state = State.DEFAULT;
					repaint();
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
		}else
			super.paint(g);
	}
	
	@Override
	public void repaint(){
		if (initialized)
			setLook();
		super.repaint();
	}
	

	public void setLook(){
		//System.out.println("reloading look");
		if (cellInfo.isRevealed() && (state != State.REVEALED)){
			state = State.REVEALED;
			setRevealedLook();
		}
	}
	
	public Coordinate getCoordinate(){
		return cellInfo.getCoordinate();
	}
	
	private void setPressedLook(){
		//setBorder(borderRevealed);
		//setBackground(Color.white);
	}
	
	private void setRevealedLook(){
		//setBorder(borderRevealed);
		//setBackground(Color.white);
		int count = cellInfo.getsetNearbyMinesCount();
		ImageIcon icon = null;
		if (count > 0)
			icon = numberIcons[count-1];
		label.setIcon(icon);
	}
	
	private void setDefaultLook(){
		//setBorder(borderNormal);
		//setBackground(colorBackground);
	}
	
	private void reveal(){
		//revelead = true;
		//setBorder(null);
		firePropertyChange(REVELAED_PROPERTY, false, true);
		/*
		String text = cellInfo.getsetNearbyMinesCount() +"";
		label.setText(text);
		System.out.println("Clicked");
		*/
	}
	
	
	@Override
	public Dimension getPreferredSize(){
		return dimension;
	}
	
}
