package bbc.juniperus.games.minesweeper.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MineField {
		
	private int columnsCount;
	private int rowsCount;
	private int minesNo;
	private int cellsCount;
	private float minesPortion = 0.15f;
	private Map<Coordinate,Cell> cells;
	
	public MineField(int colsNo, int rowsNo){
		this.columnsCount =colsNo;
		this.rowsCount = rowsNo;
		cellsCount = colsNo * rowsNo;
		cells = new HashMap<Coordinate,Cell>(cellsCount);
		minesNo = (int) (cellsCount * minesPortion);

		Set<Integer> mineIndexes = getMineIndexes();
		
		//Create mine field cells.
		int index =0;
		for (int row = 0; row < rowsNo; row++){
			for (int col = 0; col < colsNo; col++){
				Cell cell = new Cell(col,row);
				//Put mine.
				if (mineIndexes.contains(index))
					cell.setHasMine(true);
				index++;
				cells.put(cell.getCoordinate(), cell);
			}
		}
		
		//Count the number of mine-carrying neighbours for each cell
		for (Cell cell : cells.values())
			countNeighbouringMines(cell);
		
	}
	
	private Cell getCell(int x, int y){
		Coordinate coor = new Coordinate(x,y);
		return cells.get(coor);
	}
	
	public CellInfo getCellInfo(int x, int y){
		return getCell(x,y).getCellInfo();
	}
	
	public int getWidth(){
		return columnsCount;
	}
	
	public int getHeight(){
		return rowsCount;
	}
	
	public boolean revealCell(int x, int y){
		
		if (x < 0 || x >= columnsCount)
			throw new IllegalArgumentException("X is not within bounds 0 - " + (columnsCount-1));
		if (y < 0 || y >= rowsCount)
			throw new IllegalArgumentException("Y is not within bounds 0 - " + (rowsCount-1));
		
		Coordinate coo = new Coordinate(x,y);
		Cell cell = cells.get(coo);
		
		if (cell.hasMine())
			return true;
		
		uncoverCell(cell);
		
		return false;
	}
	
	private Set<Integer> getMineIndexes(){
		Set<Integer> mineCells = new HashSet<Integer>(minesNo);
		Random random = new Random();
		for (int i = 0; i < minesNo;i++){
			int index = random.nextInt(cellsCount);
			//If the number has been already picked add
			//one more iteration.
			if (!mineCells.add(index))
				i--;
		}
		return mineCells;
	}
	
	
	public String debugImg(){
		StringBuilder sb = new StringBuilder();
		
		for (int h = 0; h < getHeight(); h++){
			for (int w = 0; w < getWidth(); w++){
				Cell c = getCell(w, h);
				
//				System.out.print(c.getCoordinate() + " ");
				
				String s;
				if (c.hasMine())
					s = "X";
				else{
					if (!c.isRevealed())
						s = ".";
					else
						s = c.getNearbyMinesCount() +"";
				}
				
				sb.append(s);
				sb.append(" ");
			}
			sb.append("\n");
//			System.out.println();
		}
				
		
		return sb.toString();
	}
	
	/**
	 * Counts and set the number of mines in the nearby (neighbouring) cells for a given cell.
	 * @param cell cell to count the mine-carrying neighbours for
	 */
	private void countNeighbouringMines(Cell cell){
		if (cell.hasMine()) //Makes no sense to count it for a mine cell.
			return;
		
		int mineCount = 0;
		for (int y = -1; y < 2; y++)
			for (int x = -1 ; x < 2; x++){
				if (y == 0 && x == 0) //These are the 'cell' coordinates.
					continue;
				Cell neighbour = getCellNeighbour(cell, x, y);
				if (neighbour != null && neighbour.hasMine())
					mineCount++;
			}
		
		cell.setNearbyMinesCount(mineCount);
	}
	
	
	
	
	
	/**
	 * Reveals the cell and also its neighbours if the cell has no mine in neighbourhood.
	 * Cell is revealed only if condition <b>{@link Cell#hasMine()} == <code>true</code> </b> holds true. 
	 * If the cell does not have any mine-carrying neighbours the uncover operation is spread recursively to
	 * all of its neighbours eventually stopping at the cells near the mine or at the side of the minefield.
	 * @param cell cell to be uncovered
	 */
	private void uncoverCell(Cell cell){
		
		if (cell.isRevealed()) //Ignore if it has been already uncovered (also prevents endless loop).
			return;
		
		cell.reveal();
		
		if (cell.getNearbyMinesCount() > 0) //Contains mine in neighbourhood - do not uncover.
			return;
		
		//Uncover all neighbours as the cell has no mine in the neighbourhood
		for (int y = -1; y < 2; y++)
			for (int x = -1 ; x < 2; x++){
				if (y == 0 && x == 0) //These are the 'cell' coordinates - no need to check yourself.
					continue;
				Cell neighbour = getCellNeighbour(cell, x, y);
				if (neighbour == null) //No neighbour - must be the margin position.
					continue;
				uncoverCell(neighbour);
			}
	}
	
	private Cell getCellNeighbour(Cell cell,int offsetX, int offsetY ){
		Coordinate homeCoor = cell.getCoordinate();
		Coordinate neighbourCoor = new Coordinate(homeCoor.x + offsetX,
										homeCoor.y +  offsetY);
		return cells.get(neighbourCoor);
	}
	
	/*
	public static void main(String[] args) throws IOException{
		MineField field = new MineField(10,10);
		System.out.println(debugImg(field));
		boolean end = false;
		
		Scanner scanIn = new Scanner(System.in);
		while (!end){
			int x = scanIn.nextInt();
			int y = scanIn.nextInt();
			
			if ( x < 0)
				return;
			
			field.revealCell(x, y);
			System.out.println(debugImg(field));
		}
	}
	*/
}
