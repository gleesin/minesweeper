package bbc.juniperus.games.minesweeper.gui;

public class GameOptions {
    
    public enum Difficulty {
        EASY(9, 9, 10), MEDIUM(16, 16, 40), HARD(30, 16 , 99);
    	
        private final int rowCount, columnCount, mineCount;
    	
        Difficulty(int columnCount, int rowCount,int mineCount){
            this.rowCount = rowCount;
            this.columnCount = columnCount;
            this.mineCount = mineCount;
        }

        public int getRowCount() {
            return rowCount;
        }

        public int getColumnCount() {
            return columnCount;
        }

        public int getMineCount() {
            return mineCount;
        }
    	
    };
    
    private boolean questionMarks = true;
    private int rowCount;
    private int columnCount;
    private int mineCount;
    private Difficulty difficulty;
    private boolean sound;
    
    
    public GameOptions() {
        setDifficulty(Difficulty.HARD);
    }
    
    public void setDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
        setColumnCount(difficulty.getColumnCount());
        setRowCount(difficulty.getRowCount());
        setMineCount(difficulty.getMineCount());
    }
    
    public Difficulty getDifficulty(){
        return difficulty;
    }
    
    public boolean hasQuestionMarks() {
        return questionMarks;
    }
    public void setQuestionMarks(boolean questionMarks) {
        this.questionMarks = questionMarks;
    }
    public int getRowCount() {
        return rowCount;
    }
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    public int getColumCount() {
        return columnCount;
    }
    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }
    public int getMineCount() {
        return mineCount;
    }
    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }
    
}
