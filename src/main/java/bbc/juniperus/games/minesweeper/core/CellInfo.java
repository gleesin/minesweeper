package bbc.juniperus.games.minesweeper.core;

/*
 * The interface for view object of {@link Cell}.
 * Provides getters to essential information while not exposing the
 * cell directly which is mean only for package internal use.
 */
public interface CellInfo {

	Coordinate getCoordinate();
	boolean isRevealed();
	boolean hasMine();
	boolean hasFlag();
	int getsetNearbyMinesCount();
	boolean wasMineHit();
}
