import java.awt.*;
import java.util.*;
enum ShapesOfTetris {
	//from top to bottom
	NOTHING(new int[][] {
		{0,0}, {0,0}, {0,0}, {0,0}
	}, new Color(0,0,0)),
	L(new int[][] {
		{0,1}, {0,0}, {0,-1}, {1, -1}  
	}, new Color(6, 14, 252)), //BLUE
	BACKWARDSL(new int[][] { 
		{0,1}, {0,0}, {0,-1}, {-1,-1}
	}, new Color(255, 67, 212)), //PINK
	Z(new int[][] {
		{-1, 0}, {0,0}, {0,-1}, {1, -1}
	}, new Color(226, 0, 23)), //NICE RED
	S(new int[][] {
		{1,0}, {0,0}, {0,-1},{-1,-1}
	}, new Color(129, 249, 181)), //teal-ish 
	T(new int[][] {
		{-1, 0}, {0, 0}, {1, 0}, {0, 1}
	}, new Color(180, 45, 238)), // PURPLE
	LINE(new int[][] {
		{0,2}, {0,1}, {0,0}, {0,-1}
	}, new Color(45, 218, 238)),//LIGHT BLUE
	SQUARE(new int [][] {
		{0,1}, {1,1}, {0,0}, {1,0}
	}, new Color(251, 255, 92))//YELLOW
	;
	
	private ShapesOfTetris(int [][] location, Color c) {
		this.location = location;
		this.color = c;
	}
	
	public int[][] location;
	public Color color;
}
public class TetrisShapes {
	
	private ShapesOfTetris piece;
	private int[][] location;
	
	public TetrisShapes() {
		location = new int[4][2];
		setShape(ShapesOfTetris.NOTHING);
	}
	
	public void setShape(ShapesOfTetris piece) {
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; ++j) {
				location[i][j] = piece.location[i][j];
			}
		}
		this.piece = piece;
	}
	
	public int getX(int i) {
		return location[i][0];
	}
	
	public int getY(int i) {
		return location[i][1];
	}
	
	private void setX(int init, int x) {
		location[init][0] = x;
	}
	
	private void setY(int init, int y) {
		location[init][1] = y;
	}
	
	public void setRandom() {
		Random rand = new Random();
		int choice = Math.abs((rand.nextInt())%7 + 1); //hope this works not including nothing
		ShapesOfTetris[] shapes = ShapesOfTetris.values();
		setShape(shapes[choice]); //chooses a random shape
	}
	
	public ShapesOfTetris getShape() {
		return piece;
	}
	
	public int minX() { //looks for the min at location x
		int min = location[0][0];
		for(int i = 0; i < 4; i++) {
			if(min > location[i][0]) {
				min = location[i][0];
			}
		}
		return min;
	}
	
	public int minY() { //looks for the min at location y
		int min = location[0][1];
		for(int i = 0; i < 4; i++) {
			if(min > location[i][1]) {
				min = location[i][1];
			}
		}
		return min;
	}
	
	public TetrisShapes rotateRight() {
		if(this.piece == ShapesOfTetris.SQUARE) {
			return this;
		}
		TetrisShapes rotated = new TetrisShapes(); 
		rotated.piece = this.piece;
		
		for(int i=0; i <4; i++) {
			rotated.setX(i, -1*getY(i));
			rotated.setY(i, getX(i));
		}
		
		return rotated;
	}
	
	public TetrisShapes rotateLeft() {
		if(this.piece == ShapesOfTetris.SQUARE) {
			return this;
		}
		TetrisShapes rotated = new TetrisShapes(); 
		rotated.piece = this.piece;
		
		for(int i=0; i <4; i++) {
			rotated.setX(i, getY(i));
			rotated.setY(i, -1*getX(i));
		}
		
		return rotated;
	}
	
}


