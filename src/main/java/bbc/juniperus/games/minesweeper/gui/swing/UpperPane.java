package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class UpperPane extends JPanel{
	
	private static int HEIGHT = 50;
	private int width = 0;
	
	public UpperPane(){
		Color colorBackground = new Color(189, 189, 189);
		//Border bor = BorderFactory.createLineBorder(Color.black);
		Border bor = new CellBorder(3,colorBackground);
		setBorder(bor);
		
		
		setBackground(colorBackground);
	}
	
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(width, HEIGHT);
	}
}
