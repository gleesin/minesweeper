package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.Border;

import bbc.juniperus.games.minesweeper.core.CellInfo;
import bbc.juniperus.games.minesweeper.core.Coordinate;
import bbc.juniperus.games.minesweeper.core.GameInfo;
import bbc.juniperus.games.minesweeper.gui.swing.FaceButton.Face;

public class MineFieldGrid extends JPanel{

	private static final long serialVersionUID = 1L;

	private Map<Coordinate,CellView> cells = new HashMap<Coordinate,CellView>();
	private static final Border BORDER = new MineSweeperBorder(3,GameView.DARK_COLOR, GameView.LIGHT_COLOR); 
	private boolean ignoreMouseEvent;
	private GameView gameView;
	
	
	MineFieldGrid(GameView gameView){
		MouseAdapter listener = new TheMouseListener();
		this.gameView = gameView;
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}
	
	public void newGame(GameInfo gameInfo, GameController controller){
		setLayout(new GridLayout(gameInfo.getRowCount(),gameInfo.getColumnCount()));
		setBorder(BORDER);
		ignoreMouseEvent = false;
		
		
		for (int y = 0; y < gameInfo.getRowCount(); y++)
			for (int x = 0; x < gameInfo.getColumnCount(); x++){
				CellInfo cellInfo = gameInfo.getCellInfo(x, y);
				CellView cell = new CellView(cellInfo);
				cell.addListener(controller);
				add(cell);
				cells.put(cellInfo.getCoordinate(),cell);
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
	
	public void gameOver(boolean won){
		ignoreMouseEvent = true;
	}
	
	
	private class TheMouseListener extends MouseAdapter{
		
		private CellView pressedCell;
		private List<CellView> pressedCells = new ArrayList<>();
		private int buttonPressed;
		
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (ignoreMouseEvent)
				return;
			if (buttonPressed == MouseEvent.BUTTON3) //No dragging for right mouse button.
				return;
			
			CellView c = getCell(e);
			
			if (buttonPressed == MouseEvent.BUTTON1){
				assert pressedCells.size() == 0; //No group press is active
				
				if ( c == pressedCell) //The drag event is for the same cell as is pressed = we are draging within the pressed cell.
					return; 
				
				//We are dragging from pressed cell outside...
				pressedCell.unpressMouse(); //Unpress the previously pressed cell
				
				if (c == null ) //We dragged out of the area contaning cells.
					pressedCell = null;
				else{ //We dragged over to another cell.
					c.mousePressed(MouseEvent.BUTTON1);
					pressedCell = c;
				}
			}else if (buttonPressed== MouseEvent.BUTTON2){
				assert pressedCell == null; //No left-button pressed cell.
				unpressGroup(); // Unpresse the previous group.
				if (c != null) //If we dragged to a new cell, press the new group. Ignore if we dragged outside the cell area.
					pressGroup(c.getCoordinate());
			}
		}

		/**
		 * Sets the pressed look if the cell has not been already revelaed
		 * and if the click comes from left mouse button.
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			if (ignoreMouseEvent)
				return;
			
			if (e.getButton() == MouseEvent.BUTTON1)
				gameView.setFace(Face.SUSPENDED);
			
			CellView c = getCell(e);
			buttonPressed = e.getButton(); //Save the type of button pressed.
			
			if (c == null) //If not pressed over the cell, nothing to do.
				return;
			
			if (buttonPressed == MouseEvent.BUTTON3){
				c.mousePressed(e.getButton());
			}else if (buttonPressed == MouseEvent.BUTTON1){
				c.mousePressed(buttonPressed);
				pressedCell = c;
			}else if (buttonPressed == MouseEvent.BUTTON2){
				pressGroup(c.getCoordinate());
			}else
				throw new AssertionError();
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (ignoreMouseEvent)
				return;
			gameView.setFace(Face.NORMAL);
			buttonPressed = -1;
			
			if (e.getButton() == MouseEvent.BUTTON2)
				unpressGroup();
			else if (e.getButton() == MouseEvent.BUTTON1){
				CellView c = getCell(e);
				if (c != null){ //If button was released over the cell
					
					if (pressedCell != c){ 
						//For rare cases when the mouse somehow manages to be released on an unpressed cell 
						//Press is on another cell and release is on another one (adjacent)  without drag event which
						//would set the cell as pressed.
						
						if (pressedCell == null) //When we had no pressed cell (happens when clicking on field when menu is displayed).
							return;
						
						pressedCell.unpressMouse();
						c.mousePressed(MouseEvent.BUTTON1);
					}
					c.mouseReleased();
					pressedCell = null;
				}else{ //Was released not over the cell - because of drag, there cannot be pressed cell in this situation.
					assert pressedCell == null;
				}
			}
		}
		
		
		private void pressGroup(Coordinate co){
			assert pressedCells.size() == 0;
			
			for (int y = co.y - 1; y < co.y + 2;y++)
				for (int x = co.x - 1; x < co.x + 2; x++){
					CellView cell = cells.get(new Coordinate(x,y));
					if (cell != null)
						pressedCells.add(cell);
				}
			
			for (CellView c: pressedCells)
				c.mousePressed(MouseEvent.BUTTON2);
		}
		
		private void unpressGroup(){
			for (CellView c : pressedCells)
				c.unpressMouse();
			pressedCells.clear();
		}
		
		private CellView getCell(MouseEvent e){
			Component c = getComponentAt(e.getPoint());
			
			//If its other component - container itself.
			if (c instanceof CellView == false)
				return null;
			else
				return (CellView) c;
		}
		
	}
}
