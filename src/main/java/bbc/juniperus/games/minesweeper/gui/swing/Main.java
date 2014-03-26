package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bbc.juniperus.games.minesweeper.core.MineField;

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
		//window.setSize(WIDTH, HEIGHT);
		
		MineField mineField = new MineField(15,15);
		GameView gamePane = new GameView();
		GameController controller = new GameController(mineField, gamePane);
		
		window.add(gamePane);
		window.pack();
		/*
		JPanel pan = new JPanel();
		pan.setBackground(Color.yellow);
		pan.setPreferredSize(new Dimension(200,200));
		window.add(pan);*/
		
		
	}
	
	
	public static void main(String[] args) {
		new Main();
	}

}
