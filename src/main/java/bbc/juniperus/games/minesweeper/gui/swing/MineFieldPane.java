package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.Border;

import bbc.juniperus.games.minesweeper.core.CellInfo;
import bbc.juniperus.games.minesweeper.core.Coordinate;
import bbc.juniperus.games.minesweeper.core.GameInfo;

public class MineFieldPane extends JPanel{

	private static final long serialVersionUID = 1L;

	private Map<Coordinate,CellGui> cells = new HashMap<Coordinate,CellGui>();
	private static final Border BORDER = new CellBorder(3,GameView.DARK_COLOR, GameView.LIGHT_COLOR); 
	private GameController controller;
	
	
	MineFieldPane(){
	
		MouseAdapter listener = new TheMouseListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}
	
	
	public void fillWithCells(GameInfo gameInfo, GameController controller){
		setLayout(new GridLayout(gameInfo.getRowCount(),gameInfo.getColumnCount()));
		setBorder(BORDER);
		this.controller = controller;
		
		
		for (int y = 0; y < gameInfo.getRowCount(); y++)
			for (int x = 0; x < gameInfo.getColumnCount(); x++){
				CellInfo cellInfo = gameInfo.getCellInfo(x, y);
				CellGui cell = new CellGui(cellInfo);
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
		//Stop the cell field from processing mouse events.
		for (CellGui c : cells.values())
			c.setIgnoreMouseEvents(true);
	}
	
	
	private class TheMouseListener extends MouseAdapter{
		
		private CellGui pressedCell;
		private List<CellGui> pressedCells = new ArrayList<>();
		private int buttonPressed;
		
		
		@Override
		public void mouseDragged(MouseEvent e) {
			System.out.println(Thread.currentThread().getId());
			if (buttonPressed == MouseEvent.BUTTON3) //No dragging for right mouse button.
				return;
			
			CellGui c = getCell(e);
			
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
			
			System.out.println(Thread.currentThread().getId());
			CellGui c = getCell(e);
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
			buttonPressed = -1;
			System.out.println(Thread.currentThread().getId());
			System.out.println("released");
			
			if (e.getButton() == MouseEvent.BUTTON2)
				unpressGroup();
			else if (e.getButton() == MouseEvent.BUTTON1){
				CellGui c = getCell(e);
				if (c != null){ //If button was released over the cell
					System.out.println(pressedCell.getCoordinate() + " - " + c.getCoordinate());
					assert pressedCell == c;
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
					CellGui cell = cells.get(new Coordinate(x,y));
					if (cell != null)
						pressedCells.add(cell);
				}
			
			for (CellGui c: pressedCells)
				c.mousePressed(MouseEvent.BUTTON2);
		}
		
		private void unpressGroup(){
			for (CellGui c : pressedCells)
				c.unpressMouse();
			pressedCells.clear();
		}
		
		private CellGui getCell(MouseEvent e){
			Component c = getComponentAt(e.getPoint());
			
			//If its other component - container itself.
			if (c instanceof CellGui == false)
				return null;
			else
				return (CellGui) c;
		}
		
	}
}
