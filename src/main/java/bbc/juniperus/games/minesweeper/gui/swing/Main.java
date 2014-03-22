package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import bbc.juniperus.games.minesweeper.core.MineField;
import bbc.juniperus.games.minesweeper.gui.resources.ResourceLoadingException;
import bbc.juniperus.games.minesweeper.gui.resources.ResourceManager;

public class Main {

	private static int WIDTH = 400;
	private static int HEIGHT = 500;
	
	private JFrame window;
	
	public Main(){
		
		try {
			ResourceManager.getInstance().initialize();
		} catch (ResourceLoadingException e) {
			e.printStackTrace();
		}
		createGui();
		window.setVisible(true);
	}
	
	
	private void createGui(){
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(WIDTH, HEIGHT);
		window.setLayout(new FlowLayout());
		window.add(new MineFieldPane(new MineField(20, 20)));
	}
	
	
	public static void main(String[] args) {
		new Main();
	}

}
