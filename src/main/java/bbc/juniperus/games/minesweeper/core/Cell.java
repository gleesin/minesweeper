package bbc.juniperus.games.minesweeper.core;


class Cell {
	private Coordinate coordinate;
	private boolean mine;
	private boolean flagged;
	private boolean revealed;
	private boolean wasMineHit;
	private int nearbyMinesCount = -1;
	
	//Testing & debugging purposes.
	public final int debugId;
	private static int count;
	
	Cell(int x, int y){
		coordinate = new Coordinate(x,y);
		debugId = count++;
	}
	
	boolean hasMine(){
		return mine;
	}
	
	void setHasMine(boolean b){
		mine = b;
	}
	
	boolean isRevealed(){
		return revealed;
	}
	
	boolean reveal(){
		assert !revealed : "The cell has been already unconvered";
		revealed = true;
		return mine;
	}
	
	boolean hasFlag(){
		return flagged;
	}
	
	void setHasFlag(boolean b){
		flagged = b;
	}
	
	Coordinate getCoordinate(){
		return coordinate;
	}
	
	int getNearbyMinesCount() {
		return nearbyMinesCount;
	}

	void setNearbyMinesCount(int nearbyMinesCount) {
		this.nearbyMinesCount = nearbyMinesCount;
	}

	void mineWasHit(){
		wasMineHit = true;
	}
	
	@Override
	public String toString(){
		String mineStr = mine?"mine!":"";
		String flaggedStr = flagged?"flagged":"";
		return Cell.class.getSimpleName() +  " [" + 
				+ coordinate.x + ", " + coordinate.y +"] " + mineStr + ", " + flaggedStr +",  dbugId: " + debugId;
	}
	
	
	
	CellInfo getCellInfo(){
		return new CellInfo(){

			@Override
			public Coordinate getCoordinate() {
				return Cell.this.getCoordinate();
			}

			@Override
			public boolean isRevealed() {
				return Cell.this.isRevealed();
			}

			@Override
			public boolean hasMine() {
				return Cell.this.hasMine();
			}

			@Override
			public boolean hasFlag() {
				return Cell.this.hasFlag();
			}

			@Override
			public int getsetNearbyMinesCount() {
				return Cell.this.getNearbyMinesCount();
			}
			
			@Override
			public boolean wasMineHit() {
				return wasMineHit;
			}
		};
	}
	
	
}
