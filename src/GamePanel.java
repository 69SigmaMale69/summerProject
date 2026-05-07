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

                if (gameOver) return;

                // I divided the click location by the tile size to
                // figure out which square on the grid was actually clicked.
                int col = e.getX() / tileSize;
                int row = e.getY() / tileSize;

                if (selectedPiece == null) {
                    selectPiece(row, col);
                } else {
                    movePiece(row, col);
                }
            }
        });
    }

    // java calls this whenever the screen needs redrawing
    // I split it into two methods to keep things clean
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawPieces(g);
        drawSelected(g);
        drawGameOver(g);
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

    // first click select a snowball at the clicked cell
    void selectPiece(int row, int col) {
        Piece piece = board.getPiece(row, col);

        // I check if the piece is actually a Snowball here.
        if (piece instanceof Snowball) {
            selectedPiece = piece;
            selectedRow = row;
            selectedCol = col;
            repaint();
        }
    }

    // second click work out direction and move the snowball
    void movePiece(int clickedRow, int clickedCol) {
        int rowDiff = clickedRow - selectedRow;
        int colDiff = clickedCol - selectedCol;

        // I added this check after testing because clicking the same cell
        // was triggering game over. the direction was zero so the snowball
        // looped forever and ended up out of bounds.
        if (rowDiff == 0 && colDiff == 0) {
            selectedPiece = null;
            repaint();
            return;
        }

        int moveRow = 0;
        int moveCol = 0;

        // I compare the distances to see which way the player wants to move.
        if (Math.abs(rowDiff) >= Math.abs(colDiff)) {
            moveRow = (rowDiff > 0) ? 1 : -1;
        } else {
            moveCol = (colDiff > 0) ? 1 : -1;
        }

        slideSnowball(selectedRow, selectedCol, moveRow, moveCol);

        selectedPiece = null;
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
        // If the player loses, I just draw "Game Over!" in big red letters.
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", 130, 200);
        }
    }
}