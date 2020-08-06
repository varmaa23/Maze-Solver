/**
* MazeSquare represents a single square within a Maze.
* @author Anna Rafferty
*/ 
public class MazeSquare {


    //Wall variables
    private boolean hasTopWall = false;
    private boolean hasRightWall = false;
		
    //Location of this square in a larger maze.
    private int row;
    private int col;
		

    // Constructor for MazeSquare takes 4 parameters
    public MazeSquare(int row, int col, boolean hasTopWall, boolean hasRightWall){
      this.row = row;
      this.col = col;
      this.hasTopWall = hasTopWall;
      this.hasRightWall = hasRightWall;
    }
		
    /**
     * Returns true if this square has a top wall.
     */
    public boolean hasTopWall() {
        return hasTopWall;
    }
		
    /**
     * Returns true if this square has a right wall.
     */
    public boolean hasRightWall() {
        return hasRightWall;
    }
		
    /**
     * Returns the row this square is in.
     */
    public int getRow() {
        return row;
    }
		
    /**
     * Returns the column this square is in.
     */
    public int getColumn() {
        return col;
    }

    // Just a tester method.
    public String toString(){
      return row + " row+" + col + " col+" + hasTopWall + " topWall+" + hasRightWall + " rightWall";
    }
    
} 