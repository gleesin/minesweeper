package bbc.juniperus.games.minesweeper.gui.swing;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class Main {

	private static int WIDTH = 400;
	private static int HEIGHT = 500;
	private JMenuBar menuBar;
	
	
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
		
		menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("Game");
		JMenu menu2 = new JMenu("Help");
		
		menuBar.add(menu);
		menuBar.add(menu2);
		window = new JFrame();
		window.setJMenuBar(menuBar);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//window.setSize(WIDTH, HEIGHT);
		
		GameView gamePane = new GameView();
		GameController controller = new GameController(gamePane);
		
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
