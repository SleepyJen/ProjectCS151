import java.awt.*;
import java.util.Random;

public class TetrisPiece {

    // enum type represents 7 Tetris shapes and EmptyShape
    protected enum TetrisShape
    {
        EmptyShape(new int[][]{ { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }, new Color(0, 0, 0))
        ,Z(new int[][] { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } }, new Color(226, 0, 23))
        , S(new int[][] { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } }, new Color(129, 249, 181))
        , Line(new int[][] { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } }, new Color(45, 218, 238))
        , T(new int[][] { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } }, new Color(180, 45, 238))
        , Square(new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } }, new Color(251, 255, 92))
        , L(new int[][] { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, new Color(6, 14, 252))
        , reversedL(new int[][] { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, new Color(255, 67, 212));

        public int[][] coordinate;
        public Color color;
        TetrisShape(int[][] coordinates, Color color) {
            this.coordinate = coordinates;
            this.color = color;
        }
    }

    private TetrisShape shape;
    private int[][] coordinates; //coordinates for tetrix piece

    public TetrisPiece()
    {
        coordinates = new int[4][2];
        setShape(TetrisShape.EmptyShape);
    }

    public void setShape(TetrisShape shape)
    {
        // 3D array store all possible coordinate values of the Tetris pieces
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 2; j++)
                coordinates[i][j] = shape.coordinate[i][j];
        }
        this.shape = shape;
    }

    private void setXcoordinate(int index, int x)
    {
        coordinates[index][0] = x;
    }

    private void setYcoordinate(int index, int y)
    {
        coordinates[index][1] = y;
    }

    public int x(int index) {
        return coordinates[index][0];
    }

    public int y(int index)
    {
        return coordinates[index][1];
    }

    public TetrisShape getShape()
    {
        return shape;
    }

    public void setRandomShape()
    {
        Random r = new Random();
        int num = Math.abs(r.nextInt()) % 7 + 1;

        TetrisShape[] values = TetrisShape.values();
        setShape(values[num]);
    }

    public int minX()
    {
        int min = coordinates[0][0];
        for(int i = 0; i < coordinates[0].length; i++)
            min = Math.min(min, coordinates[i][0]);

        return min;
    }

    public int minY()
    {
        int min = coordinates[0][1];
        for(int i = 0; i < coordinates[0].length; i++)
            min = Math.min(min, coordinates[i][0]);

        return min;
    }

    public TetrisPiece rotateLeft()
    {
        if(shape == TetrisShape.Square)
            return this;

        TetrisPiece rotatedShape = new TetrisPiece();
        rotatedShape.shape = shape;

        for(int i = 0; i < 4; i++)
        {
            rotatedShape.setXcoordinate(i, y(i));
            rotatedShape.setYcoordinate(i, -x(i));
        }

        return rotatedShape;
    }

    public TetrisPiece rotateRight()
    {
        if(shape == TetrisShape.Square)
            return this;

        TetrisPiece rotatedShape = new TetrisPiece();
        rotatedShape.shape = shape;

        for(int i = 0; i < 4; i++)
        {
            rotatedShape.setXcoordinate(i, -y(i));
            rotatedShape.setYcoordinate(i, x(i));
        }

        return rotatedShape;
    }



}
