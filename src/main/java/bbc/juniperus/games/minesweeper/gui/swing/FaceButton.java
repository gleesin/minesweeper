package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;


@SuppressWarnings("serial")
public class FaceButton extends JPanel{

	private static final Border INSIDE_BORDER = new CellBorder(2,GameView.LIGHT_COLOR,GameView.DARK_COLOR);
	private static final Border INSIDE_BORDER_PRESSED = BorderFactory.createMatteBorder(1, 1, 0, 0, GameView.DARK_COLOR);
	private static final Border OUTSIDE_BORDER = new CellBorder(1,GameView.DARK_COLOR,GameView.DARK_COLOR);
	//private static final Border OUTSIDE_BORDER = BorderFactory.createLineBorder(Color.black);
	private static final Border BORDER = BorderFactory.createCompoundBorder(OUTSIDE_BORDER, INSIDE_BORDER);
	private static final Border BORDER_PRESSED = BorderFactory.createCompoundBorder(OUTSIDE_BORDER, INSIDE_BORDER_PRESSED);
	
	public FaceButton() {
		setBorder(BORDER);
		setBackground(GameView.MAIN_COLOR);
		
		addMouseListener(new MouseAdapter(){
			
			
			@Override
			public void mousePressed(MouseEvent e){
				setBorder(BORDER_PRESSED);
			}
			
			@Override
			public void mouseReleased(MouseEvent e){
				setBorder(BORDER);
		;
			}
			
		});
		
		
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(26, 26);
	}
}
