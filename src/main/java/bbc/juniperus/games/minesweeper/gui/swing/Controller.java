package bbc.juniperus.games.minesweeper.gui.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JOptionPane;

import bbc.juniperus.games.minesweeper.core.Coordinate;
import bbc.juniperus.games.minesweeper.core.MineField;

public class Controller implements PropertyChangeListener {

	private MineField field; 
	private MineFieldPane pane;
	
	public Controller(MineField field, MineFieldPane pane){
		this.field = field;
		this.pane = pane;
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == CellGui.REVEALED_PROPERTY)
			cellRevealed((CellGui)evt.getSource());
		if (evt.getPropertyName() == CellGui.FLAG_SET_PROPERTY)
			cellFlagChanged((CellGui)evt.getSource(), (boolean)evt.getNewValue());
		
	}
	
	
	private void cellRevealed(CellGui cell){
		System.out.println(cell.getCoordinate());
		List<Coordinate> newlyRevealedCells = field.revealCell(cell.getCoordinate());
		
		pane.update(newlyRevealedCells);
		if (field.isGameOver()){
			//gameOver();
			return;
		}
		System.out.println(field.debugImg());
	}
	
	private void cellFlagChanged(CellGui cell, boolean flagIsSet){
		System.out.println("cell flag changed " + flagIsSet);
		field.setFlagged(cell.getCoordinate(), flagIsSet);
		pane.update(cell.getCoordinate());
	}
	
	
	private void gameOver(){
		JOptionPane.showConfirmDialog(null, "Game over piiico");
	}
}
