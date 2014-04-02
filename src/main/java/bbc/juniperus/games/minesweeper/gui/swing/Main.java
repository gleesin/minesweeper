package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class Main {

	private JMenuBar menuBar;
	private GameController gameController;
	private JFrame window;
	private Map<Class<? extends Action>, Action> actions = new HashMap<>();
	
	public Main(){
		System.out.println("constructing");
		try {
			ResourceManager.getInstance().initialize();
		} catch (ResourceLoadingException e) {
			e.printStackTrace();
		}
		createGui();
		window.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Main();
	}
	

	
	private void createGui(){
		
		actions = createActions();
		menuBar = createMenuBar();
		
	
		window = new JFrame();
		window.setJMenuBar(menuBar);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//window.setSize(WIDTH, HEIGHT);
		
		GameOptions options = new GameOptions();
		GameView gamePane = new GameView();
		gameController = new GameController(options,gamePane);
		
		window.add(gamePane);
		window.pack();
	}
	
	
	private Map<Class<? extends Action>, Action> createActions(){
		Map<Class<? extends Action>, Action> result = new HashMap<>();
		
		result.put(NewGameAction.class, new NewGameAction());
		
		return result;
	}
	
	private JMenuBar createMenuBar(){
		JMenuBar resultMenuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.add(actions.get(NewGameAction.class));
		
		
		resultMenuBar.add(gameMenu);
		JMenu helpMenu = new JMenu("Help");
		
		
		resultMenuBar.add(helpMenu);
		
		return resultMenuBar;
	}
	
	
	@SuppressWarnings("serial")
	private class NewGameAction extends AbstractAction{

		public NewGameAction() {
			super("New game");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("starting new game");
		}
		
	}
}
