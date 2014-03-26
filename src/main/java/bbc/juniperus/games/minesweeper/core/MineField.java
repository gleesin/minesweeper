package bbc.juniperus.games.minesweeper.core;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	private List<Cell> mines = new ArrayList<Cell>();
	private boolean mineHit;
	private List<Coordinate> newlyRevealedCells = new ArrayList<Coordinate>();
	private GameInfo gameInfo;
	private int flagCount;
	
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
				if (mineIndexes.contains(index)){
					cell.setHasMine(true);
					mines.add(cell);
				}
				index++;
				cells.put(cell.getCoordinate(), cell);
			}
		}
		
		//Count the number of mine-carrying neighbours for each cell
		for (Cell cell : cells.values())
			countNeighbouringMines(cell);
		
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
	
	public boolean isGameOver(){
		return mineHit;
	}
	
	public List<Coordinate> revealCell(Coordinate coordinate){
	
		if (coordinate.x < 0 || coordinate.x >= columnsCount)
			throw new IllegalArgumentException("Coordinate.x is not within bounds 0 - " + (columnsCount-1));
		if (coordinate.y < 0 || coordinate.y >= rowsCount)
			throw new IllegalArgumentException("Coordinate.y is not within bounds 0 - " + (rowsCount-1));
		
		Cell cell = cells.get(coordinate);
		
		if (cell.hasFlag())
			throw new IllegalStateException("Cannot reveal flagged cell");

		if (cell.hasMine()){
			cell.mineWasHit();
			List<Coordinate> mineCoordinates = new ArrayList<Coordinate>();
			//Fill the coordinate list with mine cells and set all of them as revealed.
			for (Cell c : mines){
				mineCoordinates.add(c.getCoordinate());
				c.reveal();
			}
			mineHit = true; 
			//The cells to be revealed are the mine cells
			return Collections.unmodifiableList(mineCoordinates);
		}
		
		uncoverCell(cell);
		
		return Collections.unmodifiableList(newlyRevealedCells);
	}
	
	
	public int setFlag(Coordinate coordinate, boolean isFlagged){
		
		if (isFlagged){
			if (flagCount >= minesNo)
				throw new IllegalStateException("Number of flag cannot be higher than number of mines");
			flagCount++;
		}else{
			if (flagCount == 0)
				throw new IllegalStateException("There are no flags on minefield");
			flagCount--;
		}
		
		cells.get(coordinate).setHasFlag(isFlagged);
		
		return flagCount;
	}
	
	public void setQuestionMark(Coordinate coordinate, boolean hasQuestionmark){
		cells.get(coordinate).setHasQuestionMark(hasQuestionmark);
	}
	
	private Cell getCell(int x, int y){
		Coordinate coor = new Coordinate(x,y);
		return cells.get(coor);
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
	 * Reveals the cell and also recusrively its neighbors if the cell has no mine in neighborhood.
	 * Cell is revealed only if condition <b>{@link Cell#hasMine()} == <code>true</code> </b> holds true. 
	 * If the cell does not have any mine-carrying neighbors the uncover operation is spread recursively to
	 * all of its neighbors eventually stopping at the cells near the mine or at the side of the minefield.
	 * @param cell cell to be uncovered
	 */
	private void uncoverCell(Cell cell){
		
		if (cell.isRevealed()) //Ignore if it has been already uncovered (also prevents stackoverflow cycle).
			return;
		
		cell.reveal();
		cell.setHasQuestionMark(false); //Ensure the question mark is not present after being uncovered.
		newlyRevealedCells.add(cell.getCoordinate()); //Add to the list of revealed cells.
		
		
		if (cell.getNearbyMinesCount() > 0) //Contains mine in neighborhood - do not uncover the neighbors.
			return;
		
		//Uncover all neighbors as the cell has no mine in the neighborhood
		for (int y = -1; y < 2; y++)
			for (int x = -1 ; x < 2; x++){
				if (y == 0 && x == 0) //These are the 'cell' coordinates - no need to check yourself.
					continue;
				Cell neighbour = getCellNeighbour(cell, x, y);
				if (neighbour == null) //No neighbor - must be the margin position.
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
	
	public GameInfo getGameInfo(){
		if (gameInfo == null)
			gameInfo = new GameInfo(){

				@Override
				public boolean isGameOver() {
					return MineField.this.isGameOver();
				}

				@Override
				public int getRowCount() {
					return getHeight();
				}

				@Override
				public int getColumnCount() {
					return getWidth();
				}

				@Override
				public CellInfo getCellInfo(int x, int y) {
					return MineField.this.getCellInfo(x, y);
				}
		};

				return gameInfo;
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
