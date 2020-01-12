import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Arrow extends JLabel {

	private double angle;

	public Arrow(ImageIcon imageIcon) {
		super(imageIcon);
	}

	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D gx = (Graphics2D) g;
		gx.rotate(angle * Math.PI / 180, getWidth()/2, getHeight()-64); //angle in radians, point (x,y) of rotation
		super.paintComponent(g);

	}

}