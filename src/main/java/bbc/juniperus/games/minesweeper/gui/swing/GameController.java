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
	//private SoundPlayer player = new SoundPlayer();

	public GameController(GameOptions options, GameView gamePane){
		this.field = new MineField(options.getColumCount(),options.getRowCount(), options.getMineCount());
		this.gamePane = gamePane;
		this.options = options;
		
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
		gamePane.setTimeDisplayNumber(0);
		//player.playSound();
	}
	
	private void gameOver(){
		//JOptionPane.showConfirmDialog(null, "Game over piiico");
		gamePane.gameOver(false);
		stopTimer();
	}




	
	private void startTimer(){
			 
		ActionListener taskPerformer = new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				gamePane.setTimeDisplayNumber(++secondsPassed);
			}
		};
		timer = new Timer(1000, taskPerformer);
		timer.start();
		gamePane.setTimeDisplayNumber(++secondsPassed);
	}
	
	private void stopTimer(){
		if (timer != null)
			timer.stop();
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
		
		List<Coordinate> newlyRevealedCells = field.revealCell(coordinate);
		gamePane.updateMineField(newlyRevealedCells);
		if (field.isGameOver()){
			gameOver();
			return;
		}
		
		//System.out.println(field.debugImg());
		
		if (!timerOn){
			startTimer();
			timerOn = true;
		}
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
