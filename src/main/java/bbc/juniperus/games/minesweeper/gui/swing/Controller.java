package bbc.juniperus.games.minesweeper.gui.swing;

import java.util.List;
import javax.swing.JOptionPane;
import bbc.juniperus.games.minesweeper.core.CellInfo;
import bbc.juniperus.games.minesweeper.core.Coordinate;
import bbc.juniperus.games.minesweeper.core.MineField;

public class Controller implements CellGuiListener{

	private MineField field; 
	private GameView gamePane;

	public Controller(MineField field, GameView gamePane){
		this.field = field;
		this.gamePane = gamePane;
		gamePane.initialize(field, this);
	}
	
	private void gameOver(){
		JOptionPane.showConfirmDialog(null, "Game over piiico");
	}


	@Override
	public void leftMouseButtonClicked(Coordinate coordinate) {
		
		if (field.getCellInfo(coordinate.x, coordinate.y).hasFlag()) //Ignore if the cell has flag.
			return;
		
		List<Coordinate> newlyRevealedCells = field.revealCell(coordinate);
		gamePane.updateMineField(newlyRevealedCells);
		if (field.isGameOver()){
			//gameOver();
			return;
		}
	}


	@Override
	public void rightMouseButtonClicked(Coordinate coordinate){
		CellInfo info = field.getCellInfo(coordinate.x, coordinate.y);
		
		if (info.hasFlag()){ //If has flag, remove flag and add question mark.
			assert !info.hasQuestionMark();
			field.setFlag(coordinate, false);
			field.setQuestionMark(coordinate, true);
		}else if (info.hasQuestionMark()){ //Has question mark, remove question mark (flag shout not be there!).
			assert !info.hasFlag();
			field.setQuestionMark(coordinate, false);
		}else{ //info.hasFlag() == false && info.hasQuestionMark() == false
			assert !(info.hasQuestionMark() || info.hasFlag());
			field.setFlag(coordinate, true);
		}
		
		gamePane.updateMineField(coordinate);
		
	}

	@Override
	public void middleMouseButtonClicked(Coordinate coordinate) {
		
		
		
	}
}
