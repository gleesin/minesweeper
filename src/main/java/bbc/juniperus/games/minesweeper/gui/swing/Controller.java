package bbc.juniperus.games.minesweeper.gui.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

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
		if (evt.getPropertyName() == CellGui.REVELAED_PROPERTY)
			cellRevelaed((CellGui)evt.getSource());
		
	}
	
	
	private void cellRevelaed(CellGui cell){
		System.out.println(cell.getCoordinate());
		boolean hitMine = field.revealCell(cell.getCoordinate().x, cell.getCoordinate().y);
		
		if (hitMine){
			gameOver();
			return;
		}
		
		System.out.println(field.debugImg());
		pane.update();
		
	}
	
	private void cellFlagged(){
		
	}
	
	
	private void gameOver(){
		JOptionPane.showConfirmDialog(null, "Game over piiico");
	}
}
