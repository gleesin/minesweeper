package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bbc.juniperus.games.minesweeper.gui.resources.ResourceManager;

@SuppressWarnings("serial")
public class Display extends JPanel{

	private static final int DIGIT_COUNT = 3;
	private JLabel[] labels = new JLabel[DIGIT_COUNT];
	private Icon[] numbers = ResourceManager.getInstance().createDisplayNumberIcons(20);
	
	
	public Display(){
		GridLayout layout = new GridLayout(1,DIGIT_COUNT);
		setLayout(layout);
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel();
			labels[i].setIcon(numbers[0]);
			add(labels[i]);
		}
		
		setNumber(456);
		setNumber(987);
	}
	
	public void setNumber(int number){
		int max = 10 ^ DIGIT_COUNT -1;
		if (number < 0 && number > max)
			throw new IllegalArgumentException("The number needs to be in range <0, " + max + ">");
		
		int[] digits = getDigitArray(number);
		for (int i = 0; i < digits.length; i++) {
			labels[i].setIcon(numbers[digits[i]]);
		}
		
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
