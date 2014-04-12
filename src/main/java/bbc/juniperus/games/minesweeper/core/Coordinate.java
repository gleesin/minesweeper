package bbc.juniperus.games.minesweeper.core;


public class Coordinate {
    public final int x;
    public final int y;
    
    public Coordinate(int x, int y){
        this.x =x;
        this.y =y;
    }
    
    public boolean equals(Object o){
        if (o instanceof Coordinate == false)
            return false;
        Coordinate c = (Coordinate) o;
    	
        return (x == c.x && y == c.y);
    }
    
    @Override
    public int hashCode(){
        return y*11+y*7;
    }
    
    @Override
    public String toString(){
        return "[" + x + "," + y + "]";
    }
    
}
