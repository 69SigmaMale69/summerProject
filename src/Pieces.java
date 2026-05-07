import javax.swing.ImageIcon;
import java.awt.Image;

abstract class Piece {


    // every piece has three aspects, x and y position and the actual image
    int row;
    int col;
    Image image;



    // sets up the position and loads the image
    Piece(int row, int col, String imagePath) {
        this.row = row;
        this.col = col;

        // load the image
        ImageIcon icon = new ImageIcon(imagePath);
        this.image = icon.getImage();
    }


    // simple ways to read and update the data
    // I added setRow and setCol now because I know task 2 will need them
    // when pieces start moving, easier to write
    int getRow() { return row; }
    int getCol() { return col; }
    void setRow(int row) { this.row = row; }
    void setCol(int col) { this.col = col; }
    Image getImage() { return image; }
}
// at first I thought the next step was GameBoard or GamePanel
// but both are relaint on peices
// both need Piece to already exist, GameBoard has to store pieces
// and GamePanel has to draw them.
// so the pieces first