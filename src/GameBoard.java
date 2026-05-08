public class GameBoard {

    //2d array chosen becasue it is just a flat grid basically
    // created just to store the pieces
    // the size of the grid
    int rows = 4;
    int cols = 5;


    // the actual grid
    Piece[][] grid = new Piece[rows][cols];



    // know what to load. starts at 1 so the game opens the same as before
    int currentLevel = 1;


    // making of the actual gamebaord and putting the pieces in from the image for task 1
    GameBoard() {
        loadLevel(1);
    }


    // task 3 needs more than one level so I pulled the level setup
    // out of the constructor and into its own method. now the panel
    // can ask the board to swap level whenever the player wants
    void loadLevel(int level) {

        // wipe the grid first so old pieces dont stick around
        // I had a bug at first where restart left the old snowmen on
        // the board and the new pieces just got drawn over the top
        clearGrid();

        currentLevel = level;

        if (level == 1) {
            setupLevel1();
        } else if (level == 2) {
            setupLevel2();
        } else if (level == 3) {
            setupLevel3();
        }
    }


    void clearGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid[row][col] = null;
            }
        }
    }


    // I worked out the positions by looking at the level 1 image from the brief

    void setupLevel1() {
        grid[0][1] = new Snowball(0, 1, false);
        grid[3][0] = new SnowmanHead(3, 0);
        grid[3][4] = new Snowball(3, 4, true);
    }


    // level 2 - I worked out the positions by looking at the top
    // image of the two outlines from the brief
    void setupLevel2() {
        grid[0][0] = new SnowmanHead(0, 0, "red");
        grid[0][2] = new Tree(0, 2);
        grid[0][4] = new Snowball(0, 4, false);

        grid[1][0] = new Snowball(1, 0, true);

        grid[2][3] = new Snowball(2, 3, false);

        grid[3][0] = new SnowmanHead(3, 0, "yellow");
        grid[3][1] = new Snowball(3, 1, true);
        grid[3][2] = new Tree(3, 2);
    }


    // level 3 - I worked out the positions from the bottom image
    // of the two outlines from the brief
    void setupLevel3() {
        grid[0][2] = new Snowball(0, 2, true);
        grid[0][4] = new Snowball(0, 4, false);

        grid[1][2] = new Tree(1, 2);
        grid[1][4] = new Snowball(1, 4, false);

        grid[2][0] = new SnowmanHead(2, 0, "red");
        grid[2][2] = new SnowmanHead(2, 2, "yellow");
        grid[2][4] = new SnowmanHead(2, 4, "blue");

        grid[3][0] = new Snowball(3, 0, false);
        grid[3][2] = new Snowball(3, 2, true);
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

        // I forgot to update the pieces own row and col at first and ended
        // up with snowballs that thought they were still in the old square.
        // adding these two lines fixed it
        piece.setRow(toRow);
        piece.setCol(toCol);
    }


    // task 3 stacking - small snowball goes on top of a large one
    // the small disappears and the large turns into a SnowballStack
    // which counts as an obstacle from then on
    void stackSmallOnLarge(int smallRow, int smallCol, int largeRow, int largeCol) {
        removePiece(smallRow, smallCol);
        removePiece(largeRow, largeCol);
        grid[largeRow][largeCol] = new SnowballStack(largeRow, largeCol);
    }


    // task 3 head placement  finishes the snowman
    // both the head and the stack get cleared and a Snowman takes
    // the stacks place
    void placeHeadOnStack(int headRow, int headCol, int stackRow, int stackCol) {

        //needed the colour
        // so the right snowman image gets used
        SnowmanHead head = (SnowmanHead) grid[headRow][headCol];
        String colour = head.getColour();

        removePiece(headRow, headCol);
        removePiece(stackRow, stackCol);
        grid[stackRow][stackCol] = new Snowman(stackRow, stackCol, colour);
    }


    // checks if two cells are right next to each other
    // I use this for both stacking and placing the head, both need
    // the two pieces to be touching
    boolean isAdjacent(int row1, int col1, int row2, int col2) {
        int rowDiff = Math.abs(row1 - row2);
        int colDiff = Math.abs(col1 - col2);


        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }


    // task 3 win check - level is finished when there are no loose
    // pieces left, only completed snowmen and trees on the board
    // I worked it out by going through every cell and looking for
    // anything that still needs to be dealt with
    boolean isLevelComplete() {
        boolean foundSnowman = false;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Piece piece = grid[row][col];

                if (piece == null) continue;

                // anything thats not a tree or a snowman means the player
                // still has work to do
                if (piece instanceof Snowball) return false;
                if (piece instanceof SnowmanHead) return false;
                if (piece instanceof SnowballStack) return false;

                if (piece instanceof Snowman) foundSnowman = true;
            }
        }

        // I added foundSnowman because otherwise an empty board with just
        // trees would count as a win, which doesnt make sense
        return foundSnowman;
    }


    int getCurrentLevel() {
        return currentLevel;
    }
}