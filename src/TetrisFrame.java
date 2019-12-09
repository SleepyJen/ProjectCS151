import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TetrisFrame extends JPanel {
    private final int FRAME_WIDTH = 10;
    private final int FRAME_HEIGHT = 22;
    private final int SPEED = 300;

    private Timer timer;

    private boolean isCompletedFalling = false;
    private boolean isPause = false;
    private int removedLines = 0;
    private int currentX = 0;
    private int currentY = 0;
    private JLabel label;
    private TetrisPiece currentTetris;
    private TetrisPiece.TetrisShape[] tetrisFrame;
    //private boolean reset = false;
    private String message;

    public TetrisFrame(TetrisGame tetris)
    {
        setFocusable(true);
        label = tetris.getLabel();
        addKeyListener(new Press());
    }


    private int FrameWidth()
    {
        return (int)getSize().getWidth() / FRAME_WIDTH;
    }

    private int FrameHeight()
    {
        return (int)getSize().getHeight() / FRAME_HEIGHT;
    }

    private TetrisPiece.TetrisShape shapeAtLocation(int x, int y)
    {
        return tetrisFrame[(y * FRAME_WIDTH) + x];
    }

    public void start()
    {

            currentTetris = new TetrisPiece();
            tetrisFrame = new TetrisPiece.TetrisShape[FRAME_WIDTH * FRAME_HEIGHT];

            clearFrame();
            newTetris();

            timer = new Timer(SPEED, new RunGame());
            timer.start();
    }

    private void pause()
    {
        isPause = !isPause;
        if(isPause)
            label.setText("paused");

        else
            label.setText(String.valueOf(removedLines));

        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Dimension size = getSize();
        int frameTop = (int)size.getHeight() - FRAME_HEIGHT * FrameHeight();

        for(int i = 0; i < FRAME_HEIGHT; i++)
        {
            for(int j = 0; j < FRAME_WIDTH; j++)
            {
                TetrisPiece.TetrisShape shape = shapeAtLocation(j, FRAME_HEIGHT - i - 1);

                if(shape != TetrisPiece.TetrisShape.EmptyShape)
                {
                    drawSquare(g, j * FrameWidth(), frameTop + i * FrameHeight(), shape);
                }
            }
        }

        if(currentTetris.getShape() != TetrisPiece.TetrisShape.EmptyShape)
        {
            for(int i = 0; i < 4; i++)
            {
                int x = currentX + currentTetris.x(i);
                int y = currentY - currentTetris.y(i);

                drawSquare(g, x * FrameWidth(), frameTop + (FRAME_HEIGHT - y - 1) * FrameHeight(), currentTetris.getShape());
            }
        }
    }



    private void dropDown()
    {
        int newY = currentY;

        while(newY > 0)
        {
            if(!Moveable(currentTetris, currentX, newY - 1))
            {
                break;
            }
            newY--;
        }
        tetrisDropped();
    }

    private void oneLineDown()
    {
        if(!Moveable(currentTetris, currentX, currentY - 1))
            tetrisDropped();
    }

    private void clearFrame()
    {
        for(int i = 0; i < FRAME_HEIGHT * FRAME_WIDTH; i++)
            tetrisFrame[i] = TetrisPiece.TetrisShape.EmptyShape;
    }

    private void tetrisDropped()
    {
        for(int i = 0; i < 4; i++)
        {
            int x = currentX + currentTetris.x(i);
            int y = currentY - currentTetris.y(i);
            tetrisFrame[(y * FRAME_WIDTH) + x] = currentTetris.getShape();
        }
        removeWholeLines();

        if(!isCompletedFalling)
        {
            newTetris();
        }
    }

    private void newTetris()
    {
        currentTetris.setRandomShape();
        currentX = FRAME_WIDTH / 2 + 1;
        currentY = FRAME_HEIGHT - 1 + currentTetris.minY();

        if(!Moveable(currentTetris, currentX, currentY))
        {

            currentTetris.setShape(TetrisPiece.TetrisShape.EmptyShape);
            timer.stop();


            message = String.format("Game over. \n Score: %d", removedLines);

            label.setText(message);



        }
    }

    private boolean Moveable(TetrisPiece newTetris, int newX, int newY)
    {
        for(int i = 0; i < 4; i++)
        {
            int x = newX + newTetris.x(i);
            int y = newY - newTetris.y(i);

            if(x < 0 || x >= FRAME_WIDTH || y < 0 || y > FRAME_HEIGHT)
                return false;

            if(shapeAtLocation(x,y) != TetrisPiece.TetrisShape.EmptyShape)
                return false;
        }

        currentTetris = newTetris;
        currentX = newX;
        currentY = newY;

        repaint();

        return true;


    }

    private void removeWholeLines()
    {
        int fullLines = 0;
        for(int i = FRAME_HEIGHT - 1; i >= 0; i--)
        {
            boolean isLineFull = true;

            for(int j = 0; j < FRAME_WIDTH; j++)
            {
                if(shapeAtLocation(j,i) == TetrisPiece.TetrisShape.EmptyShape)
                {
                    isLineFull = false;
                    break;
                }
            }
            if(isLineFull)
            {
                fullLines++;
                for(int k = i; k < FRAME_HEIGHT - 1; k++)
                {
                    for(int j = 0; j < FRAME_WIDTH; j++)
                    {
                        tetrisFrame[(k * FRAME_WIDTH) + j] = shapeAtLocation(j, k + 1);
                    }
                }
            }
        }

        if(fullLines > 0)
        {
            removedLines += fullLines;

            label.setText(String.valueOf(removedLines));
            isCompletedFalling = true;
            currentTetris.setShape(TetrisPiece.TetrisShape.EmptyShape);
        }
    }

    private void drawSquare(Graphics g, int x, int y, TetrisPiece.TetrisShape shape)
    {
        Color color = shape.color;
        g.fillRect(x + 1, y + 1, FrameWidth() - 2, FrameHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x , y + FrameHeight() - 1 , x, y );
        g.drawLine(x, y,x + FrameWidth() - 1, y );
        g.setColor(color.darker());
        g.drawLine(x + 1, y + FrameHeight() - 1 ,
                x + FrameWidth() - 1, y + FrameHeight() - 1);
        g.drawLine(x + FrameWidth() - 1, y + FrameHeight() - 1, x + FrameWidth() - 1, y + 1);

    }

    private class RunGame implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(isPause)
                return;

            if(isCompletedFalling)
            {
                isCompletedFalling = false;
                newTetris();
            }
            else
                oneLineDown();
        }
    }

    public void setRemovedLines(int score)
    {
        this.removedLines = score;
    }

    public int getRemovedLines()
    {
        return removedLines;
    }

    public JLabel getLabel()
    {
        return label;
    }


//    public void isReset(boolean reset)
//    {
//        this.reset = reset;
//    }






    class Press extends KeyAdapter{
        public void keyPressed(KeyEvent e)
        {
            if(currentTetris.getShape() == TetrisPiece.TetrisShape.EmptyShape)
                return;

            int keycode = e.getKeyCode();
            switch(keycode)
            {
                case KeyEvent.VK_P:
                	pause();
                	break;
                case KeyEvent.VK_LEFT:
                	Moveable(currentTetris, currentX - 1, currentY - 1);
                	break;
                case KeyEvent.VK_RIGHT:
                	Moveable(currentTetris, currentX + 1, currentY);
                	break;
                case KeyEvent.VK_DOWN:
                	Moveable(currentTetris.rotateRight(), currentX, currentY);
                	break;
                case KeyEvent.VK_UP:
                	Moveable(currentTetris.rotateLeft(), currentX, currentY);
                	break;
                case KeyEvent.VK_SPACE:
                	dropDown();
                	break;
                case KeyEvent.VK_D:
                	oneLineDown();
                	break;
            }
        }

    }



}
