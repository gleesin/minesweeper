package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import bbc.juniperus.games.minesweeper.gui.swing.GameOptions.Difficulty;

public class Main {

	
	enum MenuAction {
		NEW_GAME("New game"), BEGINNER("Beginner"), 
		INTERMEDIATE("Intermediate"), EXPERT("Expert"), 
		QUESTION_MARKS("Marks (?)"), SOUND ("Sound"),
		EXIT("Exit");
		
		private final String name;
		
		private MenuAction(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
	};
	
	private final String TITLE = "Minesweeper";
	private JMenuBar menuBar;
	private GameController gameController;
	private GameOptions options;
	private JFrame window;
	private Map<MenuAction, Action> actions = new HashMap<>();
	
	public Main(){
		
		System.out.println(UIManager.getSystemLookAndFeelClassName());
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
		
		System.out.println("constructing");
		try {
			ResourceLoader.getInstance().initialize();
		} catch (ResourceLoadingException e) {
			e.printStackTrace();
		}
		
	
		
		createGame();
		window.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Main();
	}
	

	
	private void createGame(){
		
		options = new GameOptions();
		GameView gamePane = new GameView();
		gameController = new GameController(options,gamePane);
		
		actions = createActions();
		menuBar = createMenuBar();
		
		window = new JFrame();
		window.setTitle(TITLE);
		window.setIconImage(ResourceLoader.getInstance().getApplicationImage());
		window.setResizable(false);
		window.setJMenuBar(menuBar);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//window.setSize(WIDTH, HEIGHT);
		
		window.add(gamePane);
		window.pack();
	}
	
	
	private Map<MenuAction, Action> createActions(){
		Map<MenuAction, Action> result = new HashMap<>();
		
		NewGameAction newGame = new NewGameAction(MenuAction.NEW_GAME.getName());
		newGame.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		
		result.put(MenuAction.NEW_GAME, newGame);
		result.put(MenuAction.BEGINNER, new NewGameAction(MenuAction.BEGINNER.getName(), Difficulty.EASY));
		result.put(MenuAction.INTERMEDIATE, new NewGameAction(MenuAction.INTERMEDIATE.getName(), Difficulty.MEDIUM));
		result.put(MenuAction.EXPERT, new NewGameAction(MenuAction.EXPERT.getName(), Difficulty.HARD));
		result.put(MenuAction.QUESTION_MARKS, new SetQuestionMarksAction(MenuAction.QUESTION_MARKS.getName()));
		result.put(MenuAction.SOUND, new SetQuestionMarksAction(MenuAction.SOUND.getName()));
		result.put(MenuAction.EXIT, new ExitGameAction(MenuAction.EXIT.getName()));
		
		
		return result;
	}
	
	private JMenuBar createMenuBar(){
		JMenuBar resultMenuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		
		gameMenu.add(actions.get(MenuAction.NEW_GAME));
		gameMenu.addSeparator();
		
		ButtonGroup group = new ButtonGroup();
		
		JRadioButtonMenuItem beginner = new JRadioButtonMenuItem(actions.get(MenuAction.BEGINNER));
		group.add(beginner);
		gameMenu.add(beginner);
		
		JRadioButtonMenuItem intermediate = new JRadioButtonMenuItem(actions.get(MenuAction.INTERMEDIATE));
		group.add(intermediate);
		gameMenu.add(intermediate);
		
		JRadioButtonMenuItem expert = new JRadioButtonMenuItem(actions.get(MenuAction.EXPERT));
		group.add(expert);
		gameMenu.add(expert);
		
		//Based on the set difficulty, set the selected item in menu.
		if (options.getDifficulty() ==  Difficulty.EASY)
			beginner.setSelected(true);
		else if (options.getDifficulty() == Difficulty.MEDIUM)
			intermediate.setSelected(true);
		else if (options.getDifficulty() == Difficulty.HARD)
			expert.setSelected(true);
		
		gameMenu.addSeparator();
		
		JCheckBoxMenuItem marks = new JCheckBoxMenuItem(actions.get(MenuAction.QUESTION_MARKS));
		marks.setSelected(options.hasQuestionMarks());
		gameMenu.add(marks);
		
		JCheckBoxMenuItem sound = new JCheckBoxMenuItem(actions.get(MenuAction.SOUND));
		marks.setSelected(options.isSound());
		gameMenu.add(sound);
		
		gameMenu.addSeparator();
		
		gameMenu.add(actions.get(MenuAction.EXIT));
		
		resultMenuBar.add(gameMenu);
		JMenu helpMenu = new JMenu("Help");
		
		
		resultMenuBar.add(helpMenu);
		
		return resultMenuBar;
	}
	
	
	@SuppressWarnings("serial")
	private class NewGameAction extends AbstractAction{

		Difficulty difficulty;
		
		public NewGameAction(String name) {
			super(name);
		}
		
		public NewGameAction(String name, Difficulty difficulty) {
			this(name);
			this.difficulty = difficulty;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (difficulty != null){
				options.setDifficulty(difficulty);
			}
			
			gameController.startNewGame();
			window.pack();
		}
		
	}
	
	@SuppressWarnings("serial")
	private class SetQuestionMarksAction extends AbstractAction{

		public SetQuestionMarksAction(String name) {
			super(name);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			options.setQuestionMarks(!options.hasQuestionMarks());
		}
	}
	
	
	@SuppressWarnings("serial")
	private class SetSoundAction extends AbstractAction{

		public SetSoundAction(String name) {
			super(name);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
	}
	
	
	
	@SuppressWarnings("serial")
	private class ExitGameAction extends AbstractAction{

		public ExitGameAction(String name) {
			super(name);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Bye bye!");
			System.exit(0);
		}
	}
}
