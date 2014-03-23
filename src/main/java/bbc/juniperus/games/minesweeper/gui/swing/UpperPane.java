package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class UpperPane extends JPanel{
	
	private static int HEIGHT = 43;
	private static final Border INSIDE_BORDER = new CellBorder(2,GameView.DARK_COLOR,GameView.LIGHT_COLOR);
	private static final Border OUTSIDE_BORDER = BorderFactory.createMatteBorder(0, 0, 6, 0, GameView.MAIN_COLOR);
	private static final Border BORDER = BorderFactory.createCompoundBorder(OUTSIDE_BORDER, INSIDE_BORDER);
	
	public UpperPane(){
		setBackground(GameView.MAIN_COLOR);
		setBorder(BORDER);
		
		FaceButton b = new FaceButton();
		add(new Display());
		
		
	}
	
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(0, HEIGHT);
	}
}
