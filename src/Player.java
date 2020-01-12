import java.awt.event.*;
import javax.swing.*;

/**
 * @author Jenny Ho
 */

public class Player extends Board {



	/*
	 * Players
	 * 0 - bottom left
	 * 1 - top left
	 * 2 - top right
	 * 3 - bottom right
	 */


	private static String playerName;

	public Player(int i) {


	



	}

	public static String getPlayerName() {
		return ("Player " + (GUI.getTurn() + 1));
	}

	public static void setPlayerName(int i) {

	}

	
}







