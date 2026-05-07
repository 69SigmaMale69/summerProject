

class Snowball extends Piece {


    // to differentiate beween the small and large snowballs
    boolean isLarge;


    Snowball(int row, int col, boolean isLarge) {


        // pick the right image depending on if its a large or small snowball
        String imagePath;
        if (isLarge) {
            imagePath = "images/snowball_large.png";
        } else {
            imagePath = "images/snowball_small.png";
        }


        // pass everything up to Piece to handle position and image loading
        super(row, col, imagePath);

        this.isLarge = isLarge;
    }



    // is large or small for the stacking rules
    boolean isLarge() { return isLarge; }
}
