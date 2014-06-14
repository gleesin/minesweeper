package bbc.juniperus.minesweeper;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import bbc.juniperus.minesweeper.gui.AboutDialog;
import bbc.juniperus.minesweeper.gui.GameController;
import bbc.juniperus.minesweeper.gui.GameOptions;
import bbc.juniperus.minesweeper.gui.GameOptions.Difficulty;
import bbc.juniperus.minesweeper.gui.GameView;
import bbc.juniperus.minesweeper.gui.ResourceLoader;
import bbc.juniperus.minesweeper.gui.ResourceLoadingException;
import bbc.juniperus.minesweeper.gui.SoundPlayer;

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
    private JFrame frame;
    private SoundPlayer soundPlayer;
    private Map<MenuAction, Action> actions = new HashMap<>();
    
    public Main(){
    	
        System.out.println(UIManager.getSystemLookAndFeelClassName());
    	
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
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
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        new Main();
    }
    

    
    private void createGame(){
    	
        options = new GameOptions();
        GameView gamePane = new GameView();
        soundPlayer = new SoundPlayer();
        try {
            soundPlayer.initialize();
        } catch (ResourceLoadingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        gameController = new GameController(options,gamePane, soundPlayer);
    	
        actions = createActions();
        menuBar = createMenuBar();
    	
        frame = new JFrame();
        frame.setTitle(TITLE);
        frame.setIconImage(ResourceLoader.getInstance().getApplicationImage());
        frame.setResizable(false);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window.setSize(WIDTH, HEIGHT);
    	
        frame.add(gamePane);
        frame.pack();
    	
        /*
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                AudioSystem.NOT_SPECIFIED,
                16, 2, 4,
                AudioSystem.NOT_SPECIFIED, true);
        */
    	
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
        result.put(MenuAction.SOUND, new SetSoundAction(MenuAction.SOUND.getName()));
        result.put(MenuAction.EXIT, new ExitGameAction(MenuAction.EXIT.getName()));
    	
    	
        return result;
    }
    
    @SuppressWarnings("serial")
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
        marks.setModel(new DefaultButtonModel(){
            @Override
            public boolean isSelected(){
                return options.hasQuestionMarks();
        	}
        });
        //marks.setSelected(options.hasQuestionMarks());
        gameMenu.add(marks);
    	
        JCheckBoxMenuItem sound = new JCheckBoxMenuItem(actions.get(MenuAction.SOUND));
        sound.setModel(new DefaultButtonModel(){
            @Override
            public boolean isSelected(){
                return options.isSound();
            }
        });
        gameMenu.add(sound);
    	
        gameMenu.addSeparator();
    	
        gameMenu.add(actions.get(MenuAction.EXIT));
    	
        resultMenuBar.add(gameMenu);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new ShowAboutAction());
    	
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
            frame.pack();
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
        	options.setSound(!options.isSound());
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
    
    @SuppressWarnings("serial")
    private class ShowAboutAction extends AbstractAction{

        public ShowAboutAction() {
            super("About");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new AboutDialog(frame);
            dialog.setVisible(true);
        }
    }
}
