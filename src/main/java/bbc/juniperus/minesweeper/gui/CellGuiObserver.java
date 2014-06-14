package bbc.juniperus.minesweeper.gui;

import bbc.juniperus.minesweeper.core.Coordinate;


/**
 * Interface for observers of a cell gui object.
 *
 */
public interface CellGuiObserver {
    
    void leftButtonActivated(Coordinate coordinate);
    void rightButtonActivated(Coordinate coordinate);

}
