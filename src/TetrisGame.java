import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * CS 151 Project Fall2019
 * Jennifer Ni
 * Danh Nguyen
 * Wonyoung Kim
 * */

public class TetrisGame extends JFrame {
    private JLabel label;
    private JLabel score;
    private int highscore;

    public TetrisGame()
    {


        label = new JLabel(" 0");
        add(label,BorderLayout.EAST);

        TetrisFrame frame = new TetrisFrame(this);
        add(frame);
        frame.start();

        setTitle("TetrisDemo");
        setSize(200,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JMenuBar bar = new JMenuBar();
        bar.setBounds(0,0,45,24);

        JMenu file = new JMenu("File");
        file.setBounds(0,0,45,24);

        JMenuItem newGame = new JMenuItem("New game");
        newGame.addActionListener(e ->
        {
            //frame.isReset(true);
            frame.setRemovedLines(0);
            frame.getLabel().setText("" + frame.getRemovedLines());

            frame.start();
        });

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        JMenuItem highScore = new JMenuItem("HighScore");
        highScore.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {

                final JFrame alert = new JFrame("High Score");
                alert.setSize(200,150);
                alert.setLayout(null);
                alert.setLocationRelativeTo(null);

                if(frame.getRemovedLines() > highscore)
                {
                    highscore = frame.getRemovedLines();
                    score = new JLabel("You got new High Score : " + highscore);
                    score.setBounds(50,80,100,30);
                }

                score = new JLabel("The High Score is : " + highscore);
                score.setBounds(50,80,200,30);

                JButton okay = new JButton("Okay");
                okay.setBounds(0,0,200,50);
                okay.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        alert.dispose();
                    }
                });

                alert.add(score);
                alert.add(okay);
                alert.setResizable(true);
                alert.setVisible(true);



            }

        }

        );

        file.add(newGame);
        file.add(highScore);
        file.add(exit);
        bar.add(file);
        frame.add(bar);


    }

    public JLabel getLabel()
    {
        return label;
    }

    public static void main(String[] args) {
        TetrisGame game = new TetrisGame();
        game.setVisible(true);

    }
}
