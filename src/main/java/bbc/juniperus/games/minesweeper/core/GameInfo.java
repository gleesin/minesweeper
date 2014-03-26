package bbc.juniperus.games.minesweeper.core;

public interface GameInfo {

	boolean isGameOver();
	int getRowCount();
	int getColumnCount();
	CellInfo getCellInfo(int x, int y);
}
