package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bbc.juniperus.games.minesweeper.gui.swing.ResourceLoader.ImageSetResource;

@SuppressWarnings("serial")
public class Display extends JPanel{

	private static final int DIGIT_COUNT = 3;
	private JLabel[] labels = new JLabel[DIGIT_COUNT];
	private Icon[] numbers = ResourceLoader.getInstance().createIconSet(ImageSetResource.DISPLAY_NUMBERS,13);
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
		
		int max  = (int)Math.pow(10, DIGIT_COUNT)-1;
		if (number < 0 && number > max)
			throw new IllegalArgumentException("The number needs to be in range <0, " + max + ">");
		
		int[] digits = getDigitArray(number);
		
		removeAll();
		for (int i = 0; i < digits.length; i++) {
			//labels[i].setIcon(numbers[digits[i]]);
			//Instead seting an icon just create & add new JLabel. As setting
			//an icon invokes repaint and the digits in display might not change at once but one after another.
			//TODO look at better solutions (other than custom painting).
			add(new JLabel(numbers[digits[i]]));
		}
		
		revalidate();
		repaint();
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
