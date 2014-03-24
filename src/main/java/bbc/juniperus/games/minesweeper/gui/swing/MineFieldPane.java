package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.Border;

import bbc.juniperus.games.minesweeper.core.CellInfo;
import bbc.juniperus.games.minesweeper.core.Coordinate;
import bbc.juniperus.games.minesweeper.core.MineField;

public class MineFieldPane extends JPanel{

	private static final long serialVersionUID = 1L;

	private Map<Coordinate,CellGui> cells = new HashMap<Coordinate,CellGui>();
	private static final Border BORDER = new CellBorder(3,GameView.DARK_COLOR, GameView.LIGHT_COLOR); 

	public void fillWithCells(MineField field, Controller controller){
		setLayout(new GridLayout(field.getHeight(),field.getWidth()));
		setBorder(BORDER);
		
		
		for (int y = 0; y < field.getHeight(); y++)
			for (int x = 0; x < field.getWidth(); x++){
				CellInfo info = field.getCellInfo(x, y);
				CellGui cell = new CellGui(info);
				cell.addListener(controller);
				add(cell);
				cells.put(info.getCoordinate(),cell);
			}
	
	}

	public void update(List<Coordinate> coordinates) {
		//Update relevant cell gui's according to the model.
		//This will also mark them for repainting by this container.
		for (Coordinate c : coordinates)
			cells.get(c).update(); 
			
		repaint();
	}
	
	public void update(Coordinate coordinate) {
		cells.get(coordinate).update(); 
		repaint();
	}
	
	
}
