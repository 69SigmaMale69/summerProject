

class Snowman extends Piece {

    String colour;


    Snowman(int row, int col, String colour) {
        super(row, col, "images/snowman_" + colour + ".png");
        this.colour = colour;
    }


    String getColour() { return colour; }
}

// the colour is just whatever colour the head was that got placed

