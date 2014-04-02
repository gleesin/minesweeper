package bbc.juniperus.games.minesweeper.gui.swing;

public class GameOptions {
	
	private boolean questionMarks = true;
	private int rowCount = 15;
	private int columCount = 15;
	private float minesRatio = 0.15f;
	
	
	
	
	public boolean isQuestionMarks() {
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
		return columCount;
	}
	public void setColumCount(int columCount) {
		this.columCount = columCount;
	}
	public float getMinesRatio() {
		return minesRatio;
	}
	public void setMinesRatio(float minesRatio) {
		this.minesRatio = minesRatio;
	}
	
	
}
