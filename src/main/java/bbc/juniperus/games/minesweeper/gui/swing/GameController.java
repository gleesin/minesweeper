package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Timer;

import bbc.juniperus.games.minesweeper.core.CellInfo;
import bbc.juniperus.games.minesweeper.core.Coordinate;
import bbc.juniperus.games.minesweeper.core.MineField;

public class GameController implements CellGuiObserver{

    private MineField field; 
    private GameView gamePane;
    private boolean timerOn;
    private int secondsPassed;
    private Timer timer;
    private GameOptions options;
    private SoundPlayer soundPlayer;
    private static final int TIMER_INTERVAL = 1000;

    public GameController(GameOptions options, GameView gamePane, SoundPlayer soundPlayer){
        this.field = new MineField(options.getColumCount(),options.getRowCount(), options.getMineCount());
        this.gamePane = gamePane;
        this.options = options;
        assert soundPlayer.isInitialized();
        this.soundPlayer = soundPlayer;
    	
        gamePane.newGame(field.getGameInfo(), this);
    	
        gamePane.addFaceButtonListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
        	}
        	
        });
    }
    
    
    public void startNewGame(){
        secondsPassed = 0;
        timerOn = false;
        field = new MineField(options.getColumCount(),options.getRowCount(), options.getMineCount());
        gamePane.newGame(field.getGameInfo(), this);
        gamePane.setFlagDisplayNumber(field.getLeftFlagsCount());
        stopTimer();
        gamePane.setTimeDisplayNumber(0);
    }
    
    private void gameOver(boolean won){
        
        stopTimer();
        
        if (options.isSound())
            if (won)
                soundPlayer.playWinSound();
            else
                soundPlayer.playExplosionSound();
        gamePane.gameOver(won);
    }

    private void startTimer(){
        	 
        ActionListener taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                gamePane.setTimeDisplayNumber(++secondsPassed);
                if (options.isSound())
                    soundPlayer.playTickSound();
        	}
        };
        timer = new Timer(TIMER_INTERVAL, taskPerformer);
        timer.start();
        gamePane.setTimeDisplayNumber(++secondsPassed); //Start from value 1.
    }
    
    private void stopTimer(){
        if (timer != null)
            timer.stop();
        timerOn = false;
    }



    private void setFlag(Coordinate coordinate){
        int flagCount = field.setFlag(coordinate, true);
        gamePane.setFlagDisplayNumber(flagCount);
    }
    
    private void removeFlag(Coordinate coordinate){
        int flagCount = field.setFlag(coordinate, false);
        gamePane.setFlagDisplayNumber(flagCount);
    }


    @Override
    public void leftButtonActivated(Coordinate coordinate) {
    	
        if (field.getCellInfo(coordinate.x, coordinate.y).hasFlag()) //Ignore if the cell has flag.
            return;
    	
        if (!timerOn){
            startTimer();
            timerOn = true;
        }
        
        List<Coordinate> newlyRevealedCells = field.revealCell(coordinate);
        gamePane.updateMineField(newlyRevealedCells);
        if (field.wasMineHit())
            gameOver(false);
        else if (field.isGameWon())
            gameOver(true);
    }
    
    @Override
    public void rightButtonActivated(Coordinate coordinate) {
            CellInfo info = field.getCellInfo(coordinate.x, coordinate.y);
        	
            if (info.hasFlag()){ //If has flag, remove flag and add question mark.
                assert !info.hasQuestionMark();
                removeFlag(coordinate);
                if (options.hasQuestionMarks())
                    field.setQuestionMark(coordinate, true);
            }else if (info.hasQuestionMark()){ //Has question mark, remove question mark (flag shout not be there!).
                assert !info.hasFlag();
                field.setQuestionMark(coordinate, false);
            }else{ //info.hasFlag() == false && info.hasQuestionMark() == false
                assert !(info.hasQuestionMark() || info.hasFlag());
        		
                if (field.getLeftFlagsCount()  == 0) //Ignore if we cannot set more flags.
                    return;
                setFlag(coordinate);
        	}
        	
            gamePane.updateMineField(coordinate);
    }
}
