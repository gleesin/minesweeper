package bbc.juniperus.games.minesweeper.gui.swing;

import bbc.juniperus.games.minesweeper.core.Coordinate;


/**
 * Interface for observers of a cell gui object.
 *
 */
public interface CellGuiObserver {
    
    void leftButtonActivated(Coordinate coordinate);
    void rightButtonActivated(Coordinate coordinate);

}
