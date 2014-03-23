package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import bbc.juniperus.games.minesweeper.core.Coordinate;
import bbc.juniperus.games.minesweeper.core.MineField;

@SuppressWarnings("serial")
public class GameView extends JPanel{

	UpperPane upperPane;
	MineFieldPane mineFieldPane;
	
	
	public GameView() {
		super(new BorderLayout());
		
		upperPane = new UpperPane();
		mineFieldPane = new MineFieldPane();
		
		add(upperPane, BorderLayout.NORTH);
		add(mineFieldPane);

	}
	
	public void initialize(MineField mineField, Controller controller){
		mineFieldPane.fillWithCells(mineField, controller);
	}
	
	public void updateMineField(List<Coordinate> coordinates){
		mineFieldPane.update(coordinates);
	}
	
	public void updateMineField(Coordinate coordinate){
		mineFieldPane.update(coordinate);
	}
	
}
