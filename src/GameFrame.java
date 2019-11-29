import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameFrame extends JPanel implements ActionListener{

	private static final int WIDTH = 10;
	private static final int HEIGHT = 22;
	private static final int AREA = WIDTH*HEIGHT;
	private int removedLines =0;
	private int thisX = 0;
	private int thisY = 0;
	private Timer timer;
	private boolean done = false;
	private boolean start = false;
	private boolean pause = false;
	private JLabel label;
	private TetrisShapes currentPiece;
	private ShapesOfTetris[] tetrisShapes;
	
	public GameFrame(Tetris board) {
		setFocusable(true);
		currentPiece = new TetrisShapes();
		timer = new Timer(400, this);
		label = board.getLabel();
		tetrisShapes = new ShapesOfTetris[AREA];
		clearGameFrame();
		addKeyListener(new Press());
	}
	private void clearGameFrame() {
		for (int i=0; i < AREA; i++ ) {
			tetrisShapes[i] = ShapesOfTetris.NOTHING;
		}
	}	
	
	public ShapesOfTetris shapeAtLocation(int x, int y) {
		return tetrisShapes[y*WIDTH + x];
	}
	
	public int getBoardWidth() {
		int frameWidth = (int) (getSize().getWidth()/WIDTH);
		return frameWidth;
	}
	
	public int getBoardHeight() {
		int frameHeight = (int)(getSize().getHeight()/HEIGHT);
		return frameHeight;
	}
	
	public void piecesDropped() {
		for(int i=0;i<4;i++) {
			int x = thisX + currentPiece.getX(i);
			int y = thisY + currentPiece.getY(i);
			tetrisShapes[y*WIDTH + x ] = currentPiece.getShape();
		}
		
		removeLines();
		
		if(!done) {
			newPiece();
		}
	}
	
	public void removeLines() {
		int filledLines = 0;
		
		for(int i = HEIGHT -1; i>=0; --i) {
			boolean fullLine = true;
			for(int j = 0; i<WIDTH; ++j) {
				if(shapeAtLocation(j,i) == ShapesOfTetris.NOTHING){
					fullLine = false;
					j = WIDTH; 
				}
			}
			if(fullLine) {
				filledLines++;
				for(int k=i; k <WIDTH; ++k) {
					for(int j=0; j<WIDTH;++j) {
						tetrisShapes[k*WIDTH+j] = shapeAtLocation(j, k+1);
					}
				}
			}
			if(filledLines > 0) {
				removedLines += filledLines;
				label.setText("" + removedLines);
				done = true;
				currentPiece.setShape(ShapesOfTetris.NOTHING);
				repaint();
			}
			
		}
	}
	
	public void newPiece() {
		currentPiece.setRandom();
		thisX = WIDTH/2 +1;
		thisY = HEIGHT/2 + 1 + currentPiece.minY();
		
		if(!moveablePiece(currentPiece, thisX, thisY-1)) {
			currentPiece.setShape(ShapesOfTetris.NOTHING);
			timer.stop();
			start = false;
			label.setText("Game Over");
		}
	}
	
	public boolean moveablePiece(TetrisShapes newShape, int newX, int newY) {
		for(int i = 0; i<4; ++i) {
			int x = newX + newShape.getX(i);
			int y = newY + newShape.getY(i);
			
			if(x <0 || y <0 ||x>WIDTH || y >=HEIGHT) {
				return false;
			}
			if(shapeAtLocation(x,y) != ShapesOfTetris.NOTHING) {
				return false;
			}
		}
		this.currentPiece = newShape;
		this.thisX = newX;
		this.thisY = newY;
		return true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(done) {
			done = false;
			newPiece();
		}else{
			downOneLine();
		}		
	}
	
	public void downOneLine() {
		if(!moveablePiece(currentPiece, thisX, thisY-1)) {
			piecesDropped();
		}
	}
	
	public void paint(Graphics g2) {
		super.paint(g2);
		Dimension size = getSize();
		int top = (int) (size.getHeight() - HEIGHT*getBoardHeight());
		
		for(int i=0; i<HEIGHT; i++) {
			for(int j=0; j<WIDTH; ++j) {
				ShapesOfTetris shape = shapeAtLocation(j, HEIGHT - i - 1);
				if(shape != ShapesOfTetris.NOTHING) {
					drawShapes(g2, j*getBoardWidth(), top+i*getBoardHeight(), shape);
				}
			}
		}
		if(currentPiece.getShape() != ShapesOfTetris.NOTHING) {
			for(int i=0; i<4; ++i) {
				int x = thisX + currentPiece.getX(i);
				int y = thisY - currentPiece.getY(i);
				drawShapes(g2, x*getBoardWidth(), top + (HEIGHT - y -1)* getBoardHeight(), currentPiece.getShape());
			}
		}
		
	}
	
	private void drawShapes(Graphics g2, int x, int y, ShapesOfTetris shape) { //this may not work, check when run again.
		Color c = shape.color;
		g2.setColor(c);
		g2.fillRect(x + 1, y +1, getBoardWidth(), getBoardHeight());
		g2.setColor(c.brighter()); //not sure this will work....
		g2.drawLine(x, y +getBoardHeight()-1, x, y);
		g2.drawLine(x, y, x +getBoardHeight()-1, y);
		g2.setColor(c.darker()); //also not sure if this would work...
		g2.drawLine(x+1, y+getBoardHeight()-1, x+getBoardWidth()-1, y+getBoardHeight()-1);
		g2.drawLine(x+getBoardWidth()-1, y+getBoardHeight()-1, x+getBoardWidth()-1, y+1);
	}
	
	private void trickleDown() {
		int y = thisY;
		while(y>0) {
			if(!moveablePiece(currentPiece, thisX, y-1)) {
				break;
			}
			--y;
		}
		piecesDropped();
	}
	
	public void start() {
		if(pause) {
			return;
		}else {
			start = true;
			done = false;
			removedLines = 0;
			clearGameFrame();
			newPiece();
			timer.start();
		}
	}
	
	public void pause() {
		if(!start) {
			return;
		}else {
			pause = !pause;
			if(pause) {
				timer.stop();
				label.setText("Paused");
			}else {
				timer.start();
				label.setText("" + removedLines);
			}
		}
		repaint();
	}
	
class Press extends KeyAdapter {
	public void keyPressed(KeyEvent k) {
		if(!start || currentPiece.getShape() == ShapesOfTetris.NOTHING) {
			int keyCode = k.getKeyCode();
			if(keyCode == 'p' || keyCode == 'P') {
				pause();
			}
			if(pause) {
				return;
			}
			if(keyCode == KeyEvent.VK_RIGHT) {
				moveablePiece(currentPiece, thisX + 1 , thisY);
			}else if (keyCode == KeyEvent.VK_LEFT) {
				moveablePiece(currentPiece, thisX -1, thisY);
			}else if(keyCode == KeyEvent.VK_UP) {
				moveablePiece(currentPiece.rotateLeft(), thisX, thisY);
			}else if(keyCode == KeyEvent.VK_DOWN) {
				moveablePiece(currentPiece.rotateRight(), thisX, thisY);
			}else if(keyCode == KeyEvent.VK_SPACE) {
				trickleDown();
			} //should i add a down one space faster?
		}
	}
}
}
