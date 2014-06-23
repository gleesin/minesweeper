package bbc.juniperus.minesweeper.gui;

import bbc.juniperus.minesweeper.model.Coordinate;


/**
 * An observer for event related to {@link CellView}.
 */
public interface CellViewObserver {
   
	/**
	 * Invoked when a left mouse button action has been
	 * activated on a cell view with a given coordinate.
	 * @param coordinate coordinate of the cell view's cell
	 */
    void leftButtonActivated(Coordinate coordinate);
	/**
	 * Invoked when a left mouse button action has been
	 * activated on a cell view with a given coordinate.
	 * @param coordinate coordinate of the cell view's cell
	 */
    void rightButtonActivated(Coordinate coordinate);

}
