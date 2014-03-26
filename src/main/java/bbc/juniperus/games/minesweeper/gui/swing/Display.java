package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bbc.juniperus.games.minesweeper.gui.swing.ResourceManager.ImageSetResource;

@SuppressWarnings("serial")
public class Display extends JPanel{

	private static final int DIGIT_COUNT = 3;
	private JLabel[] labels = new JLabel[DIGIT_COUNT];
	private Icon[] numbers = ResourceManager.getInstance().createIconSet(ImageSetResource.DISPLAY_NUMBERS,13);
	private Image img;
	
	public Display(){
		GridLayout layout = new GridLayout(1,DIGIT_COUNT);
		setLayout(layout);
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel();
			labels[i].setIcon(numbers[0]);
			add(labels[i]);
		}

		setNumber(0);
	}
	
	public void setNumber(int number){
		int max = 10 ^ DIGIT_COUNT -1;
		if (number < 0 && number > max)
			throw new IllegalArgumentException("The number needs to be in range <0, " + max + ">");
		
		int[] digits = getDigitArray(number);
		
		//img = createImage();
		
		removeAll();
		for (int i = 0; i < digits.length; i++) {
			//labels[i].setIgnoreRepaint(true);
			//labels[i].setIcon(numbers[digits[i]]);
			add(new JLabel(numbers[digits[i]]));
		}
		
		revalidate();
		repaint();
	}
	
	
	/*
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(50,50);
	}*/
	
	private Image createImage(){
		
		Image iconImg = ((ImageIcon) numbers[2]).getImage();
		
		int height = iconImg.getHeight(null);
		int width = iconImg.getWidth(null) * DIGIT_COUNT;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Graphics g = img.getGraphics();
		
		for (int i = 0; i < DIGIT_COUNT;i++)
			g.drawImage(iconImg, i * 12, 0, null);
		
		return img;
	}
	
	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		/*
		System.out.println("Drawing img");
		//g.fillRect(2, 2, 20, 20);
		g.drawImage(img, 0, 0, null);
		*/
	}
	
	private int[] getDigitArray(int number){
		int[] digits = new int[DIGIT_COUNT];
		
		int leftover = number;
		for (int i = 0; i < DIGIT_COUNT; i++){
			int result = leftover % 10;
			leftover = (leftover - result)/10;
			digits[DIGIT_COUNT -1 -i] = result;
			
			if (leftover == 0)
				break;
		}
		assert leftover == 0;
		
		return digits;
	}
	
}
