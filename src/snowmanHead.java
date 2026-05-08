
class SnowmanHead extends Piece {

    // task 3 has 3 different heads now so I had to add a colour
    // before this it was just hardcoded to just blue
    String colour;


    SnowmanHead(int row, int col, String colour) {
        super(row, col, "images/head_" + colour + ".png");
        this.colour = colour;
    }


    // I kept the old constructor working so my level 1 setup still
    // works without me having to go and change it
    SnowmanHead(int row, int col) {
        this(row, col, "blue");
    }


    String getColour() { return colour; }
}


// task 3 update, red and yellow shoulld be added now