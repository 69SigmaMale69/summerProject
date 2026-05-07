

public class GameBoard {

//2d array chosen becasue it is just a flat grid basically
    // created just to store the pieces 
    // the size of the grid
    int rows = 4;
    int cols = 5;


    // the actual grid
    Piece[][] grid = new Piece[rows][cols];


    // making of the actual gamebaord and putting the pieces in from the image for task 1
    GameBoard() {
        setupLevel1();
    }


    // I worked out the positions by looking at the level 1 image from the brief

    void setupLevel1() {
        grid[0][1] = new Snowball(0, 1, false);
        grid[3][0] = new SnowmanHead(3, 0);
        grid[3][4] = new Snowball(3, 4, true);
    }


    // get the piece at a position
    Piece getPiece(int row, int col) {
        return grid[row][col];
    }

    // set a piece at a position
    void setPiece(int row, int col, Piece piece) {
        grid[row][col] = piece;
    }

    // remove a piece from a position
    void removePiece(int row, int col) {
        grid[row][col] = null;
    }


    // check if a position is actually on the grid
    // task 2 will use this when sliding snowballs to make sure
    // they dont try to move outside the grid
    boolean inBounds(int row, int col) {
        boolean rowOk = row >= 0 && row < rows;
        boolean colOk = col >= 0 && col < cols;
        return rowOk && colOk;
    }

    // check if a cell has nothing in it
    boolean isEmpty(int row, int col) {
        return grid[row][col] == null;
    }


    // move a piece from one cell to another
    // task 2 will use this when snowballs slide into a new position
    void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = grid[fromRow][fromCol];
        grid[toRow][toCol] = piece;
        grid[fromRow][fromCol] = null;
    }
}
