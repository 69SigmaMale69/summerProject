import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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


        // task 3 - I needed buttons so the player can restart if they
        // mess up or move on once theyve solved a level. I put them in
        // their own panel at the top so they dont overlap the game
        JPanel buttons = makeButtonRow(panel);


        // I switched to BorderLayout because I now have two things to
        // place. buttons go at the top, game goes in the middle
        // I added the panel before sizing because pack needs to know
        // what is inside the window before it can work out the size
        setLayout(new BorderLayout());
        add(buttons, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

// setVisible last so the window doesnt flicker on screen
    }


    // I pulled this out into its own method because the constructor
    // was getting long. its just the row of buttons across the top
    JPanel makeButtonRow(GamePanel panel) {
        JPanel row = new JPanel();

        JButton restart = new JButton("Restart");
        JButton next = new JButton("Next Level");

        // task 3 - the listeners just call back into the panel which is
        // where the actual level switching logic lives
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.restartLevel();
            }
        });

        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.nextLevel();
            }
        });

        row.add(restart);
        row.add(next);
        return row;
    }
}