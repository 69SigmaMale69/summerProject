


import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {


    // how big each grid cell is in pixels
    int tileSize = 100;


    GameBoard board;


    // takes the board in so the panel knows what to draw
    GamePanel(GameBoard board) {
        this.board = board;

        // work out the total size of the panel from the grid dimensions
        int panelWidth = board.cols * tileSize;
        int panelHeight = board.rows * tileSize;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.WHITE);
    }


    // java calls this whenever the screen needs redrawing
// I split it into two methods to keep things clean
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawPieces(g);
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
}