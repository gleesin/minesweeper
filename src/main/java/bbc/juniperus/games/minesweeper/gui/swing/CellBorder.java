package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

public class CellBorder implements Border{

	private int borderWidth;
	private Color colorLight,colorDark;
	private static final int factor = 56;
	
	public CellBorder(int width, Color colorBackground){
		borderWidth = width;
		
		
		colorDark = new Color( colorBackground.getRed() - factor,
				colorBackground.getGreen() - factor,
				colorBackground.getBlue() - factor
				);
		
		colorLight = new Color( colorBackground.getRed() + factor,
				colorBackground.getGreen() + factor,
				colorBackground.getBlue() + factor
				);		
	}
	
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		
		g.setColor(colorLight);
		for (int i = 0; i < borderWidth;i ++){
			g.drawLine(0, i, width -1 -i ,i); //Horizontal line on top
			g.drawLine(i, 0, i,height - 1 -i); //Vertical line on the left
		}
		
		g.setColor(colorDark);
		for (int i = 0; i < borderWidth; i++){
			System.out.println(" i is " + i);
			g.drawLine(i+1,height-i ,width ,height - i); //Horizontal line at the bottom
			g.drawLine(width - i, i+1, width -i,height); //Vertical line on the right
		}
		
	}

	@Override
	public Insets getBorderInsets(Component c) {
		//Insets is obviously mutable :/. Better create a new object every-time.
		return new Insets(borderWidth,borderWidth,borderWidth,borderWidth);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

}
