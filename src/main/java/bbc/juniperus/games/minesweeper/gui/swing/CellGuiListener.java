package bbc.juniperus.games.minesweeper.gui.swing;

import bbc.juniperus.games.minesweeper.core.Coordinate;

public interface CellGuiListener {
	void leftMouseButtonClicked(Coordinate coordinate);
	void rightMouseButtonClicked(Coordinate coordinate);
	void middleMouseButtonClicked(Coordinate coordinate);
	void mouseButtonPressed(Coordinate coordinate, int mouseButton);
	void leftMouseButtonReleased(Coordinate coordinate);
}
