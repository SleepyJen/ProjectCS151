import java.awt.*;
import javax.swing.*;

public class Tetris extends JFrame{
	private JLabel label;
	
	public Tetris() {
		label = new JLabel("0");
		add(label, BorderLayout.SOUTH);
		GameFrame frame = new GameFrame(this);
		add(frame);
		frame.start();
		setSize(200,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	public static void main(String[] args) {
		Tetris game = new Tetris();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}

}
