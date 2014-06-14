package bbc.juniperus.minesweeper.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MineField {
    	
    private int columnCount;
    private int rowCount;
    private int mineCount;
    private int cellCount;
    private Map<Coordinate,Cell> cells;
    private List<Cell> mines = new ArrayList<Cell>();
    private boolean mineHit, gameWon;
    private List<Coordinate> newlyRevealedCells = new ArrayList<Coordinate>();
    private GameInfo gameInfo;
    private int flagsLeft;
    private int coveredCells;
    private List<Coordinate> mineCoordinates = new ArrayList<>();
    
    public MineField(int columnCount, int rowCount, int mineCount){
        this.columnCount =columnCount;
        this.rowCount = rowCount;
        this.mineCount = mineCount;
    	
        cellCount = columnCount * rowCount;
        cells = new HashMap<Coordinate,Cell>(cellCount);
    	
        flagsLeft = mineCount;
    	
        coveredCells = cellCount;
        
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                Cell cell = new Cell(col, row);
                cells.put(cell.getCoordinate(), cell);
            }
        }
    }
    
    
    public void putMines(Coordinate ignoreCoordinate){
        Set<Coordinate> coordinates = calculateMineCoordinates(ignoreCoordinate);
        mineCoordinates.addAll(coordinates);
        
        
        for (Coordinate coordinate : mineCoordinates){
            Cell cell = cells.get(coordinate);
            mines.add(cell);
            cell.setHasMine(true);
        }

        // Count the number of mine-carrying neighbours for each cell
        for (Cell cell : cells.values())
            countNeighbouringMines(cell);
        
    }

    public CellInfo getCellInfo(int x, int y){
        return getCell(x,y).getCellInfo();
    }
    
    public int getColumnCount(){
        return columnCount;
    }
    
    public int getRowCount(){
        return rowCount;
    }
    
    public boolean wasMineHit(){
        return mineHit;
    }
    
    public boolean isGameWon(){
        return gameWon;
    }
    
    public int getLeftFlagsCount(){
        return flagsLeft;
    }
    
    
    public List<Coordinate> revealCell(Coordinate coordinate){
    
        if (coordinate.x < 0 || coordinate.x >= columnCount)
            throw new IllegalArgumentException("Coordinate.x is not within bounds 0 - " + (columnCount-1));
        if (coordinate.y < 0 || coordinate.y >= rowCount)
            throw new IllegalArgumentException("Coordinate.y is not within bounds 0 - " + (rowCount-1));
    	
        Cell cell = cells.get(coordinate);
    	
        if (cell.hasFlag())
            throw new IllegalStateException("Cannot reveal flagged cell");

        if (cell.hasMine()){
            cell.mineWasHit();
            //Reveal all mines
            for (Cell c : mines)
                c.reveal();
        	
            mineHit = true; 
            return Collections.unmodifiableList(mineCoordinates);
        }
    	
        uncoverCell(cell);
        verifyIfWon();
        
        if(gameWon) //If game won, also update the mine cell as we set the flags there.
            newlyRevealedCells.addAll(mineCoordinates);
        
        return Collections.unmodifiableList(newlyRevealedCells);
    }
    
    
    public int setFlag(Coordinate coordinate, boolean isFlagged){
    	
        if (isFlagged){
            if (flagsLeft == 0)
                throw new IllegalStateException("There cannot be more flags then mines");
            flagsLeft--;
        }else{
            if (flagsLeft == mineCount)
                throw new IllegalStateException("Cannot remove flag. There should be no flag in the field.");
            flagsLeft++;
        }
    	
        cells.get(coordinate).setHasFlag(isFlagged);
    	
        return flagsLeft;
    }
    
    public void setQuestionMark(Coordinate coordinate, boolean hasQuestionmark){
        cells.get(coordinate).setHasQuestionMark(hasQuestionmark);
    }
    
    private Cell getCell(int x, int y){
        Coordinate coor = new Coordinate(x,y);
        return cells.get(coor);
    }
    
    
    private Set<Coordinate> calculateMineCoordinates(Coordinate ignoreCoordinate){
        
        
        
        Set<Coordinate> coordinates = new HashSet<Coordinate>(mineCount);
        Random random = new Random();
        for (int i = 0; i < mineCount;i++){
            int index = random.nextInt(cellCount);
            Coordinate co = indextToCoordinate(index);
            //If the coordinate has been already picked or should be ignored, add
            //one more iteration.
            if (co.equals(ignoreCoordinate) || 
                    !coordinates.add(co)){
                i--;
            }
        }
        
        return coordinates;
    }
    
    private Coordinate indextToCoordinate(int i){
        int x = i  % columnCount;
        int y = (i - x) / columnCount ;
        
        return new Coordinate(x,y);
    }
    
    
    public String debugImg(){
        StringBuilder sb = new StringBuilder();
    	
        for (int h = 0; h < getRowCount(); h++){
            for (int w = 0; w < getColumnCount(); w++){
                Cell c = getCell(w, h);
        		
                String s;
                if (c.hasFlag())
                    s ="F";
                else if (c.hasMine())
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
        }
        		
    	
        return sb.toString();
    }
    
    
    private void verifyIfWon(){
        if (coveredCells > mines.size())
            return;
        
        gameWon = true;
        //Make the flag set on all mines.
        for (Cell cell : mines)
            if (!cell.hasFlag())
                cell.setHasFlag(true);
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
    	
        //Ignore if it has been already uncovered (also prevents stackoverflow cycle) or
        //if has flag.
        if (cell.isRevealed() || cell.hasFlag()) 
            return;
    	
        cell.reveal();
        cell.setHasQuestionMark(false); //Ensure the question mark is not present after being uncovered.
        newlyRevealedCells.add(cell.getCoordinate()); //Add to the list of revealed cells.
    	coveredCells--; //Decrement the total number of covered cells
    	
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
        if (gameInfo == null) //Initiate lazily
            gameInfo = new GameInfo(){

                @Override
                public boolean isGameOver() {
                    return MineField.this.wasMineHit();
            	}

                @Override
                public int getRowCount() {
                    return MineField.this.getRowCount();
            	}

                @Override
                public int getColumnCount() {
                    return MineField.this.getColumnCount();
            	}

                @Override
                public CellInfo getCellInfo(int x, int y) {
                    return MineField.this.getCellInfo(x, y);
            	}
        };

                return gameInfo;
    }
    
}
