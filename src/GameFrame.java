
import javax.swing.*;

public class GameFrame extends JFrame {

    GameFrame() {

        // basic window setup first
        // how the window itself behaves before any content is added
        setTitle("Snow Problem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);


        // create the board and panel together
        // I made the board first because the panel needs it to know what to draw
        // so order had to be board first, then panel
        // my plan is to build GameBoard next so it can hold all the pieces
        // then GamePanel after that to actually draw them on screen
        GameBoard board = new GameBoard();
        GamePanel panel = new GamePanel(board);

        // I added the panel before sizing because pack() needs to know
        // what is inside the window before it can work out the size
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

// setVisible last so the window doesnt flicker on screen
    }
}