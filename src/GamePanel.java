import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {

    // how big each grid cell is in pixels
    int tileSize = 100;

    GameBoard board;

    // tracks which piece is selected
    Piece selectedPiece = null;
    int selectedRow = 0;
    int selectedCol = 0;
    boolean gameOver = false;

    // task 3 added a separate flag for winning
    boolean levelWon = false;


    // takes the board in so the panel knows what to draw
    GamePanel(GameBoard board) {
        this.board = board;

        // work out the total size of the panel from the grid dimensions
        int panelWidth = board.cols * tileSize;
        int panelHeight = board.rows * tileSize;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.WHITE);

        // This listens for mouse clicks so we know when the player
        // wants to select or move a snowball.
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                // I added levelWon to this check too so the player cant
                // keep clicking pieces after winning. I had missed this
                // before which let the gam keep running after a win.
                if (gameOver || levelWon) return;

                // I divided the click location by the tile size to
                // figure out which square on the grid was actually clicked.
                int col = e.getX() / tileSize;
                int row = e.getY() / tileSize;

                if (selectedPiece == null) {
                    selectPiece(row, col);
                } else {
                    handleSecondClick(row, col);
                }
            }
        });
    }


    // task 3 the buttons in GameFrame call these so the player can
    // start over or move on once theyve finished a level
    void restartLevel() {
        board.loadLevel(board.getCurrentLevel());
        // I put resetState back in here because without it, the Game Over
        // message would stay stuck on the screen even after the level reloaded.
        resetState();
    }

    void nextLevel() {
        int next = board.getCurrentLevel() + 1;
        // I made sure to keep this check so the game doesnt try to load

        if (next > 3) next = 1;
        board.loadLevel(next);
        resetState();
    }



    void resetState() {
        selectedPiece = null;
        gameOver = false;
        levelWon = false;
        repaint();
    }


    // java calls this whenever the screen needs redrawing
    // I split it into two methods to keep things clean
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawPieces(g);
        drawSelected(g);
        drawGameOver(g);
        drawLevelWon(g);
    }

    // draws the grid lines so the player can see the cells
    void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);

        // horizontal lines - one at the top of every row plus one at the bottom
        for (int row = 0; row <= board.rows; row++) {
            int y = row * tileSize;
            g.drawLine(0, y, board.cols * tileSize, y);
        }

        // vertical lines - one at the left of every column plus one at the right
        for (int col = 0; col <= board.cols; col++) {
            int x = col * tileSize;
            g.drawLine(x, 0, x, board.rows * tileSize);
        }
    }

    // loops through every cell and draws the piece if there is one
    void drawPieces(Graphics g) {
        for (int row = 0; row < board.rows; row++) {
            for (int col = 0; col < board.cols; col++) {

                Piece piece = board.getPiece(row, col);

                // only draw if theres actually a piece here
                if (piece != null) {
                    int x = col * tileSize;
                    int y = row * tileSize;
                    g.drawImage(piece.getImage(), x, y, tileSize, tileSize, this);
                }
            }
        }
    }

    // first click select a piece at the clicked cell
    // task 3 update - I changed this so heads can be selected too,
    // because to place a head onto a stack you have to click the
    // head first then click the stack
    void selectPiece(int row, int col) {
        Piece piece = board.getPiece(row, col);

        // snowballs and heads are the only pieces that can be picked up
        // I made sure SnowmanHead is included here, otherwise the player
        // can't pick them up to finish the level!
        if (piece instanceof Snowball || piece instanceof SnowmanHead) {
            selectedPiece = piece;
            selectedRow = row;
            selectedCol = col;
            repaint();
        }
    }


    // task 3 second click is more complicated now because the player
    // might be trying to slide, stack or place a head. I had to work out
    // which one based on what they clicked on
    void handleSecondClick(int clickedRow, int clickedCol) {

        // I added this check after testing because clicking the same cell
        // was triggering game over. the direction was zero so the snowball
        // looped forever and ended up out of bounds.
        if (clickedRow == selectedRow && clickedCol == selectedCol) {
            selectedPiece = null;
            repaint();
            return;
        }

        Piece target = board.getPiece(clickedRow, clickedCol);

        // I check stacking and head placement first because they only
        // happen if the click landed on the right kind of piece in an
        // adjacent cell. anything else falls through to a normal slide
        if (target != null && board.isAdjacent(selectedRow, selectedCol, clickedRow, clickedCol)) {

            // small snowball clicked onto a large one - stack them
            if (selectedPiece instanceof Snowball && target instanceof Snowball) {
                Snowball selSnow = (Snowball) selectedPiece;
                Snowball tarSnow = (Snowball) target;

                // I fixed the logic here to ensure small goes on large.
                // Before it was inverted and tried to put big ones on small ones
                if (!selSnow.isLarge() && tarSnow.isLarge()) {
                    board.stackSmallOnLarge(selectedRow, selectedCol, clickedRow, clickedCol);
                    finishMove();
                    return;
                }
            }

            // head clicked onto a stack - finish the snowman
            if (selectedPiece instanceof SnowmanHead && target instanceof SnowballStack) {
                board.placeHeadOnStack(selectedRow, selectedCol, clickedRow, clickedCol);
                finishMove();
                return;
            }
        }

        // not a stack or place, must be a slide
        // heads cant slide so I just deselect if a head was picked
        if (selectedPiece instanceof SnowmanHead) {
            selectedPiece = null;
            repaint();
            return;
        }

        slideToward(clickedRow, clickedCol);
    }


    // pulled this out from the old movePiece. its still the same logic
    // of working out which way to slide and then doing it, just renamed
    // because slide is more accurate now that stacking exists too
    void slideToward(int clickedRow, int clickedCol) {
        int rowDiff = clickedRow - selectedRow;
        int colDiff = clickedCol - selectedCol;

        int moveRow = 0;
        int moveCol = 0;

        // I compare the distances to see which way the player wants to move.
        if (Math.abs(rowDiff) >= Math.abs(colDiff)) {
            moveRow = (rowDiff > 0) ? 1 : -1;
        } else {
            moveCol = (colDiff > 0) ? 1 : -1;
        }

        slideSnowball(selectedRow, selectedCol, moveRow, moveCol);
        finishMove();
    }


    // task 3 after any move I check if the level was won. before there
    // was nothing to win so this didnt exist
    void finishMove() {

        // doesn't stay on the screen after the piece has already moved.
        selectedPiece = null;

        if (board.isLevelComplete()) {
            levelWon = true;
        }

        repaint();
    }


    // slide the snowball one step at a time until something stops it
    void slideSnowball(int row, int col, int moveRow, int moveCol) {
        int currentRow = row;
        int currentCol = col;

        while (true) {
            int nextRow = currentRow + moveRow;
            int nextCol = currentCol + moveCol;

            // If the snowball goes off the edge, the player loses.
            if (!board.inBounds(nextRow, nextCol)) {
                board.removePiece(currentRow, currentCol);
                gameOver = true;
                repaint();
                return;
            }

            // If the next square isn't empty, the snowball stops.
            if (!board.isEmpty(nextRow, nextCol)) {
                break;
            }

            currentRow = nextRow;
            currentCol = nextCol;
        }

        // I added this check after testing because snowballs were
        // disappearing when they slid into another piece. movePiece was
        // being called even when the snowball hadnt moved, which made it
        // overwrite the blocking piece.
        if (currentRow != row || currentCol != col) {
            board.movePiece(row, col, currentRow, currentCol);
        }
    }

    void drawSelected(Graphics g) {
        // I draw a blue box around the selected piece so the player
        // knows exactly what they are about to move.
        if (selectedPiece != null) {
            g.setColor(Color.BLUE);
            int x = selectedCol * tileSize;
            int y = selectedRow * tileSize;
            g.drawRect(x, y, tileSize, tileSize);
        }
    }

    void drawGameOver(Graphics g) {
        // If the player loses, Game Over! in big red letters.
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", 130, 200);
        }
    }


    //  same idea as drawGameOver but for winning.
    void drawLevelWon(Graphics g) {
        if (levelWon) {
            // I changed  to Green I had it set to White before
            //  which made the text invisible
            g.setColor(new Color(0, 150, 0));
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Level Complete!", 90, 200);
        }
    }
}