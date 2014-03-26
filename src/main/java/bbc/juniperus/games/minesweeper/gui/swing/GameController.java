package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import bbc.juniperus.games.minesweeper.core.CellInfo;
import bbc.juniperus.games.minesweeper.core.Coordinate;
import bbc.juniperus.games.minesweeper.core.GameInfo;
import bbc.juniperus.games.minesweeper.core.MineField;
import bbc.juniperus.games.minesweeper.gui.swing.FaceButton.Face;

public class GameController implements CellGuiListener{

	private MineField field; 
	private GameView gamePane;
	private boolean timerOn;
	private int secondsPassed;
	private Timer timer;

	public GameController(MineField field, GameView gamePane){
		this.field = field;
		this.gamePane = gamePane;
		gamePane.initialize(field.getGameInfo(), this);
		
		gamePane.addFaceButtonListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button was pressed");
			}
			
		});
		
		gamePane.setFlagDisplayNumber(field.getLeftFlagsCount());
	}
	
	
	private void newGame(){
		secondsPassed = 0;
	}
	
	private void gameOver(){
		//JOptionPane.showConfirmDialog(null, "Game over piiico");
		gamePane.gameOver(false);
		stopTimer();
	}


	@Override
	public void leftMouseButtonClicked(Coordinate coordinate) {
		
		if (field.getCellInfo(coordinate.x, coordinate.y).hasFlag()) //Ignore if the cell has flag.
			return;
		
		List<Coordinate> newlyRevealedCells = field.revealCell(coordinate);
		gamePane.updateMineField(newlyRevealedCells);
		if (field.isGameOver()){
			gameOver();
			return;
		}
		
		System.out.println(field.debugImg());
		
		if (!timerOn){
			startTimer();
			timerOn = true;
		}
		
		
		
	}

	
	private void startTimer(){
		 int delay = 1000; 
		 
		 final Thread t = Thread.currentThread();
		 
		 secondsPassed++;
		 
		 ActionListener taskPerformer = new ActionListener() {
			  
		      public void actionPerformed(ActionEvent evt) {
		         gamePane.setTimeDisplayNumber(++secondsPassed);
		      }
		  };
		  timer  = new Timer(delay, taskPerformer);
		  timer.start();
		  gamePane.setTimeDisplayNumber(secondsPassed);
	}
	
	private void stopTimer(){
		if (timer != null)
			timer.stop();
	}

	@Override
	public void rightMouseButtonClicked(Coordinate coordinate){
		CellInfo info = field.getCellInfo(coordinate.x, coordinate.y);
		
		if (info.hasFlag()){ //If has flag, remove flag and add question mark.
			assert !info.hasQuestionMark();
			removeFlag(coordinate);
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

	private void setFlag(Coordinate coordinate){
		int flagCount = field.setFlag(coordinate, true);
		gamePane.setFlagDisplayNumber(flagCount);
	}
	
	private void removeFlag(Coordinate coordinate){
		int flagCount = field.setFlag(coordinate, false);
		gamePane.setFlagDisplayNumber(flagCount);
	}
	
	@Override
	public void middleMouseButtonClicked(Coordinate coordinate) {
		
		
		
	}


	@Override
	public void leftMouseButtonPressed(Coordinate coordinate) {
		gamePane.setFace(Face.SUSPENDED);
	}


	@Override
	public void leftMouseButtonReleased(Coordinate coordinate) {
		gamePane.setFace(Face.NORMAL);
	}
	
}
