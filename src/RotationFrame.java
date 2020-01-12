import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class RotationFrame extends JFrame implements KeyListener{

	Arrow arrow = new Arrow(new ImageIcon("src/graphics/tiles/1.png"));

	public RotationFrame() {

		setTitle("Rotation - using arrow keys");
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.RED);
		setLayout(null);

		arrow.setBounds(100,100,128,128); //arrow is wider than to handle the rotation - adjust the size to meet your needs
		add(arrow);

		addKeyListener(this);

		setVisible(true);

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode()==KeyEvent.VK_LEFT)
			arrow.setAngle(arrow.getAngle()-90);
		else if (e.getKeyCode()==KeyEvent.VK_RIGHT)
			arrow.setAngle(arrow.getAngle()+90);

		repaint();

	}

	@Override
	public void keyReleased(KeyEvent e) {

		System.out.println(arrow.getAngle());

	}

	@Override
	public void keyTyped(KeyEvent e) {

		// NOT USED

	}


}