import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
* Maze represents a maze that can be navigated. The maze
* should indicate its start and end squares, and where the
* walls are. 
*
* Eventually, this class will be able to load a maze from a
* file, and solve the maze.
* The starter code has part of the implementation of load, but
* it does not read and store the information about where the walls of the maze are.
*
*/
public class Maze { 
    //Number of rows in the maze.
    private int numRows;
    
    //Number of columns in the maze.
    private int numColumns;
    
    //Grid coordinates for the starting maze square
    private int startRow;
    private int startColumn;
    
    //Grid coordinates for the final maze square
    private int finishRow;
    private int finishColumn;
    

    private List<ArrayList<MazeSquare>> mazeSquares;
    
    /**
     * Creates an empty maze with no squares.
     */
    public Maze() {
      numRows = 0;
      numColumns = 0;
      startRow = 0;
      startColumn = 0;
      finishRow = 0;
      finishColumn = 0;
      mazeSquares = new ArrayList<ArrayList<MazeSquare>>();
    } 
    
    /**
     * Loads the maze that is written in the given fileName.
     * Returns true if the file in fileName is formatted correctly
     * (meaning the maze could be loaded) and false if it was formatted
     * incorrectly (meaning the maze could not be loaded). The correct format
     * for a maze file is given in the assignment description. Ways 
     * that you should account for a maze file being incorrectly
     * formatted are: one or more squares has a descriptor that doesn't
     * match  *, 7, _, or | as a descriptor; the number of rows doesn't match
     * what is specified at the beginning of the file; or the number of
     * columns in any row doesn't match what's specified at the beginning
     * of the file; or the start square or the finish square is outside of
     * the maze. You can assume that the file does start with the number of
     * rows and columns.
     * 
     */
    public boolean load(String fileName) { 
      File file = new File(fileName);
      Scanner scanner = null; 
      String[] rowDescripArray = new String[] {"*", "7", "_", "|"};
      List<String> rowDescripList = Arrays.asList(rowDescripArray);

      try{
      scanner = new Scanner(file);
      } catch (FileNotFoundException e){
      System.err.println(e);
      System.exit(1);
    }
    
    // This counter variable keeps track of each row, which is indicated by the line.
    int counter  = 0;

    while (scanner.hasNextLine()){

      /** I have two different variables that represent
      * the data on the line, line and lineArray.
      * lineArray is used to get the values of the
      * columns and rows on the first 3 rows of the
      * file. line is used to get the value of each
      * descriptor. */

      String line = scanner.nextLine();
      String[] lineArray = line.split(" "); // These values are assumed to be split according to spaces.
      ArrayList<MazeSquare> mazeSquareRows = new ArrayList<MazeSquare>();

      for(int i = 0; i < line.length(); i++){
        // Finds values of colums and rows and sets them to variable cols and rows.
        if(counter == 0){ 
          if(i == 0){
            numColumns = Integer.valueOf(lineArray[i]);
          }
          else if (i == 2){
            numRows = Integer.valueOf(lineArray[lineArray.length - 1]);
          }
        }

        // If the start square is outside of the maze.
        if(counter == 1){
          if(i == 0){
            startColumn = Integer.valueOf(lineArray[i]);
          }
          else if(i == 2){
            startRow = Integer.valueOf(lineArray[lineArray.length - 1]);
          }
          if(isInRange(startColumn, 0, numColumns) == false || isInRange(startRow, 0, numRows) == false){
            return false;
          }
          
        }

        // If the finish square is outside of the maze. 
        if(counter == 2){
          if(i == 0){
            finishColumn = Integer.valueOf(lineArray[i]);
          }
          else if(i == 2){
            finishRow = Integer.valueOf(lineArray[lineArray.length - 1]);
          }
          if(isInRange(finishColumn, 0, numColumns) == false || isInRange(finishRow, 0, numRows) == false){
            return false;
          }
        }

        // Checks to see if the row descriptions only contain 7,*,_,|. Also, if number of columns specified at any row doesn't match what's specified at the beginning. 
        if (counter >= 3){
          if (!rowDescripList.contains(line.substring(i, i+1)) || line.length() != numColumns){
            System.out.println(line.length() + " " + counter);
            return false;
          }

          int mazeRow = counter - 3;
          int mazeColumn = i;
          boolean hasRightWall;
          boolean hasTopWall;
          if (line.substring(i, i+1).equals("7")){
            hasRightWall = true;
            hasTopWall = true;
          }
          else if (line.substring(i, i+1).equals("|")){
            hasRightWall = true;
            hasTopWall = false;
          }
          else if (line.substring(i, i+1).equals("_")){
            hasRightWall = false;
            hasTopWall = true;
          }
          else{
            hasRightWall = false;
            hasTopWall = false;
          }

          // ArrayList that represents each row
          MazeSquare ms = new MazeSquare(mazeRow, mazeColumn, hasTopWall, hasRightWall);
          mazeSquareRows.add(i, ms);
        } 
      }
      // Add to larger ArrayList
      if(counter >= 3){
        mazeSquares.add(mazeSquareRows);
      }
      counter++;
    }
    // Checks to see if there are the same number of rows as specified.
    if (counter - 3 != numRows){
      return false;
    }
    return true;
  } 
    
    /**
     * Returns true if number is greater than or equal to lower bound
     * and less than upper bound. 
     * @param number
     * @param lowerBound
     * @param upperBound
     * @return true if lowerBound ≤ number < upperBound
     */
    private static boolean isInRange(int number, int lowerBound, int upperBound) {
        return number < upperBound && number >= lowerBound;
    }
    
    /**
      * Prints the maze with the start and finish squares
      * marked. This method can print with or without a
      * solution. It takes an ArrayList parameter
      * solution that is representative of the stack
      * returned (it makes it easier to access all of
      * the elements). If "--solve" is not in the
      * command line argument, then the maze is
      * unmarked. 
     */
    public void print(ArrayList<MazeSquare> solution) {

        //We'll print off each row of squares in turn.
        for(int row = 0; row < numRows; row++) {
            
            //Print each of the lines of text in the row
            for(int charInRow = 0; charInRow < 4; charInRow++) {
                //Need to start with the initial left wall.
                if(charInRow == 0) {
                    System.out.print("+");
                } else {
                    System.out.print("|");
                }
                
                for(int col = 0; col < numColumns; col++) {
                    MazeSquare curSquare = this.getMazeSquare(row, col);

                    if(charInRow == 0) {
                        //We're in the first row of characters for this square - need to print
                        //top wall if necessary.
                        if(curSquare.hasTopWall()) {
                            System.out.print(getTopWallString());
                        } else {
                            System.out.print(getTopOpenString());
                        }
                    } else if(charInRow == 1 || charInRow == 3) {
                        //These are the interior of the square and are unaffected by
                        //the start/final state.
                        if(curSquare.hasRightWall()) {
                            System.out.print(getRightWallString());
                        } else {
                            System.out.print(getOpenWallString());
                        }
                    } else {
                        // We must be in the second row of characters.
                        // This is the row where start/finish should be displayed if relevant
                        
                        // Check if we're in the start or finish state
                        if(startRow == row && startColumn == col) {
                            System.out.print("  S  ");
                        } else if(finishRow == row && finishColumn == col) {
                            System.out.print("  F  ");
                        // This is the only line where the ArrayList solution is used. If the ArrayList containing each of the mazeSquares of the solution contains curSquare of the loop, then the "*" is printed. This way, if only 1 command line argument is given, it does not affect the rest of the code. Also, the order of the list passed in doesn't matter because I'm using contains().
                        } else if(solution.contains(curSquare)){
                          System.out.print("  *  ");
                        }
                        else {
                            System.out.print("     ");
                        }
                        if(curSquare.hasRightWall()) {
                            System.out.print("|");
                        } else {
                            System.out.print(" ");
                        }
                    } 
                }

                
                //Now end the line to start the next
                System.out.print("\n");
            }           
        }
        
        //Finally, we have to print off the bottom of the maze, since that's not explicitly represented
        //by the squares. Printing off the bottom separately means we can think of each row as
        //consisting of four lines of text.
        printFullHorizontalRow(numColumns);
    }
    
    /**
     * Prints the very bottom row of characters for the bottom row of maze squares (which is always walls).
     * numColumns is the number of columns of bottom wall to print.
     */
    private static void printFullHorizontalRow(int numColumns) {
        System.out.print("+");
        for(int row = 0; row < numColumns; row++) {
            //We use getTopWallString() since bottom and top walls are the same.
            System.out.print(getTopWallString());
        }
        System.out.print("\n");
    }
    
    /**
     * Returns a String representing the bottom of a horizontal wall.
     */
    private static String getTopWallString() {
        return "-----+";
    }
    
    /**
     * Returns a String representing the bottom of a square without a
     * horizontal wall.
     */
    private static String getTopOpenString() {
        return "     +";
    }
    
    /**
     * Returns a String representing a left wall (for the interior of the row).
     */
    private static String getRightWallString() {
        return "     |";
    }
    
    /**
     * Returns a String representing no left wall (for the interior of the row).
     */
    private static String getOpenWallString() {
        return "      ";
    }
    
    /**
     Returns a MazeSquare when given a row and a column. Throws an IndexOutOfBounds exception when the index is out of bounds. 
     */
    public MazeSquare getMazeSquare(int row, int col) {
        if(!isInRange(row, 0, numRows) || !isInRange(col, 0, numColumns)){
          throw new IndexOutOfBoundsException("Index Out of Bounds.");
        }
        return mazeSquares.get(row).get(col);
    }


  /**
  * Computes and returns a solution to this maze. If there are multiple
  * solutions, only one is returned, and getSolution() makes no guarantees about
  * which one. However, the returned solution will not include visits to dead
  * ends or any backtracks, even if backtracking occurs during the solution
  * process. 
  *
  * @return a stack of MazeSquare objects containing the sequence of squares
  * visited to go from the start square (bottom of the stack) to the finish
  * square (top of the stack). If there is no solution, an empty stack is
  * returned.
  */

  public Stack<MazeSquare> getSolution(){
    // Create a new Stack that represents the path from start to finish.
    // Also create an ArrayList of ArrayLists of Boolean values that represent whether or not a square has been visited. 

    Stack<MazeSquare> mazePath = new MysteryStackImplementation<MazeSquare>();
    ArrayList<ArrayList<Boolean>> isVisited = new ArrayList<ArrayList<Boolean>>();

    // This loop initializes all values inside isVisited to false.
    for(int row = 0; row < mazeSquares.size(); row++){
      ArrayList<Boolean> isVisitedRow = new ArrayList<Boolean>();
      for(int col = 0; col < mazeSquares.get(0).size(); col++){
        isVisitedRow.add(col, false);
      }
      isVisited.add(isVisitedRow);
    }
    
    // Push startSquare to the top of the stack, declare variables currentRow and currentColumn. 
    MazeSquare startSquare = getMazeSquare(startRow, startColumn);
    mazePath.push(startSquare);
    isVisited.get(startRow).set(startColumn, true);
    int currentRow;
    int currentColumn;

    // This is the main loop of getSolution(). It'll only run if the stack isn't empty and if the square at the top of the stack isn't the finish square. 
    while(mazePath.isEmpty() == false && mazePath.peek() != getMazeSquare(finishRow, finishColumn)){

      MazeSquare currentSquare = mazePath.peek();
      
      // hasValidNeighbor is used to track if there are any walls blocking a square from a neighbor OR if that neighboring square has been visited. Otherwise, squares will be popped off the Stack. 
      currentRow = currentSquare.getRow();
      currentColumn = currentSquare.getColumn();
      isVisited.get(currentRow).set(currentColumn, true);
      boolean hasValidNeighbor;

      // go above
      if(currentSquare.hasTopWall() == false && isVisited.get(currentRow - 1).get(currentColumn) == false){
        hasValidNeighbor = true;
        currentSquare = getMazeSquare(currentRow - 1, currentColumn);
      }

    // go right
      else if(currentSquare.hasRightWall() == false && isVisited.get(currentRow).get(currentColumn + 1) == false){
        hasValidNeighbor = true;
        currentSquare = getMazeSquare(currentRow, currentColumn + 1);
        
      }

      // go below
      else if(isInRange(currentRow + 1, 0 , numRows) == true && isVisited.get(currentRow + 1).get(currentColumn) == false && getMazeSquare(currentRow + 1, currentColumn).hasTopWall() == false){
        hasValidNeighbor = true;
        currentSquare = getMazeSquare(currentRow + 1, currentColumn);
        
      }

      // go left
      else if(isInRange(currentColumn - 1, 0, numColumns) == true && isVisited.get(currentRow).get(currentColumn - 1) == false && getMazeSquare(currentRow, currentColumn - 1).hasRightWall() == false){
        hasValidNeighbor = true;
        currentSquare = getMazeSquare(currentRow, currentColumn - 1);
        
      }
      
      else{
        hasValidNeighbor = false;
        mazePath.pop();
      }

      // Only if haveValidNeighbor == true do we push the new currentSquare.
      if(hasValidNeighbor == true){
        mazePath.push(currentSquare);
      }

    }
    return mazePath;
    
  }
    
    /**
     * Main can take 2 possible parameters, the .txt
     * file with the maze info and "--solve" if the maze
     * should be solved. 
     */ 
    public static void main(String[] args) { 
      // Declare mazeLoads, a boolean that is set to what is returned by load(). Only if load returns true will anything be printed or solved. Otherwise, a message will be displayed. 

      String file;
      boolean mazeLoads;
      Maze m = new Maze();
      file = args[0];
      mazeLoads = m.load(file);

      // Create a new ArrayList of mazeSquares that represents whatever's inside the stack returned by getSolution. if "--solve" is not indicated, solution is empty, and print() without solution won't be impacted. 
      ArrayList<MazeSquare> solution = new ArrayList<MazeSquare>();   

        if (args.length == 1){
          if (mazeLoads == true){
            m.print(solution);
          }
          else{
            System.out.println("Maze doesn't load.");
            System.exit(1);
          }
        }

        else if(args.length == 2 && args[1].equals("--solve")){
          if (mazeLoads == true){
            // Create a new stack that represents the contents of the returned stack in getSolution(). while loop will go through the stack and add each element to solution. If the returned stack is empty, then the Maze is unsolvable. 

            Stack<MazeSquare> printSolution = new MysteryStackImplementation<MazeSquare>();printSolution = m.getSolution();
            if(printSolution.isEmpty() == true){
              System.out.println("Maze is unsolvable.");
            }
            while(printSolution.isEmpty() == false){
              solution.add(printSolution.pop());
            }
            m.print(solution);
          }
          else{
            System.out.println("Maze doesn't load.");
            System.exit(1);
          }
        }
    } 
}