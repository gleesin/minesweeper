package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
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

	public GameController(GameView gamePane){
		this.field = new MineField(15,15);
		this.gamePane = gamePane;
		gamePane.newGame(field.getGameInfo(), this);
		
		gamePane.addFaceButtonListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				newGame();
			}
			
		});
	}
	
	
	private void newGame(){
		secondsPassed = 0;
		timerOn = false;
		field = new MineField(field.getWidth(), field.getHeight());
		gamePane.newGame(field.getGameInfo(), this);
		gamePane.setFlagDisplayNumber(field.getLeftFlagsCount());
		gamePane.setTimeDisplayNumber(0);
	}
	
	private void gameOver(){
		//JOptionPane.showConfirmDialog(null, "Game over piiico");
		gamePane.gameOver(false);
		stopTimer();
	}




	
	private void startTimer(){
		secondsPassed++;
			 
		ActionListener taskPerformer = new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				gamePane.setTimeDisplayNumber(++secondsPassed);
			}
		};
		timer = new Timer(1000, taskPerformer);
		timer.start();
		gamePane.setTimeDisplayNumber(secondsPassed);
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
