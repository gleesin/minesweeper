package bbc.juniperus.minesweeper.core;

public interface GameInfo {

	boolean isGameOver();
	int getRowCount();
	int getColumnCount();
	CellInfo getCellInfo(int x, int y);
}
