// when a small snowball gets put on top of a large oone
// turn into one of these.

class SnowballStack extends Piece {


    SnowballStack(int row, int col) {
        super(row, col, "images/snowman_stack.png");
    }
}

// I made this its own class instead of leaving the two snowballs
// having a single SnowballStack object is way simpler