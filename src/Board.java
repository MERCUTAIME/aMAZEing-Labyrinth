/* author - Zhuolin Hou */


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Board extends JLabel{

	static String[][] array = new String[9][9];
	static String looseTile;
	static int[] locationR = new int[4];
	static int[] locationC = new int[4];
	static String[][] treasure = new String[4][5];
	static int[][] rotation = new int[7][7];
	static int rotationLT;
	static int[] found = new int[4];

	int clickedX, clickedY;
	static int X_START;
	static int Y_START;

	static int move = 0;

	static char [][] maze = new char[9][9];

	Scanner input = new Scanner (System.in);


	//left = 0; right = 1; down = 2; up = 3
	static final int LEFT = 0;
	static final int RIGHT = 1;
	static final int DOWN = 2;
	static final int UP = 3;

	public Board(){



		//stationary tiles
		array[1][1] = "1";
		array[1][3] = "C";
		array[1][5] = "A";
		array[1][7] = "2";
		array[3][1] = "X";
		array[3][3] = "G";
		array[3][5] = "R";
		array[3][7] = "S";
		array[5][1] = "I";
		array[5][3] = "W";
		array[5][5] = "K";
		array[5][7] = "V";
		array[7][1] = "3";
		array[7][3] = "H";
		array[7][5] = "J";
		array[7][7] = "4";

		//add 33 of (12 movable tiles with treasure + 9 L tiles + 13 I tiles), leaving one extra
		Random rand = new Random();
		List<String> givenList =  new ArrayList<String>(Arrays.asList(
				"B","D","E","F","L","M","N","O","P","Q","T","U",
				"6","6","6","6","6","6","6","6","6",
				"5","5","5","5","5","5","5","5","5","5","5","5","5"));

		int numberOfElements = 34;
		String[] randomElement = new String[34];

		for (int i = 0; i < numberOfElements; i++) {
			int randomIndex = rand.nextInt(givenList.size());
			randomElement[i] = givenList.get(randomIndex);
			givenList.remove(randomIndex);
		}

		int q = 0;

		//set element and rotate randomly
		for (int y = 1; y < 8; y++){

			if (y % 2 == 1){

				for (int x = 2; x <= 6; x=x+2){
					array[x][y] = randomElement[q];
					rotation[x-1][y-1] = rand.nextInt(4);
					q++;
				}
			}

			else{

				for (int x = 1; x <=7; x++){
					array[x][y] = randomElement[q];
					rotation[x-1][y-1] = rand.nextInt(4);
					q++;
				}

			}

		}


		looseTile = randomElement[q];



		//assign treasures to be found to each player
		List<String> treasures =  new ArrayList<String>(Arrays.asList(
				"B","D","E","F","L","M","N","O","P","Q","T","U","C", "A", "X", "G", "R", "S", "I", "W", "K", "V","H", "J"));

		numberOfElements = 24;
		randomElement = new String[24];

		for (int i = 0; i < numberOfElements; i++) {
			int randomIndex = rand.nextInt(treasures.size());
			randomElement[i] = treasures.get(randomIndex);
			treasures.remove(randomIndex);
		}

		int count = 0;

		for (int x = 0; x<4; x++){

			for(int y=0; y<5; y++){


				treasure[x][y] = randomElement[count];
				count++;

			}

//			System.out.println();
		}



		//set start location for each player
		locationR[0] = 7;
		locationC[0] = 1;
		locationR[1] = 1;
		locationC[1] = 1;
		locationR[2] = 1;
		locationC[2] = 7;
		locationR[3] = 7;
		locationC[3] = 7;

	}



	public static String[][] getArray() {
		return array;
	}


	public static void setArray(String[][] array) {
		Board.array = array;
	}


	public static String getLooseTile() {
		return looseTile;
	}


	public static void setLooseTile(String looseTile) {
		Board.looseTile = looseTile;
	}


	public static int[] getLocationR() {
		return locationR;
	}


	public static void setLocationR(int[] locationR) {
		Board.locationR = locationR;
	}


	public static int[] getLocationC() {
		return locationC;
	}


	public static void setLocationC(int[] locationC) {
		Board.locationC = locationC;
	}



	public static String[][] getTreasure() {
		return treasure;
	}



	public static void setTreasure(String[][] treasure) {
		Board.treasure = treasure;
	}


	/*author - Zhuolin Hou, Jenny Ho */
	public static boolean arrayTraversal(int x, int y, int R, int C) {

		move ++;
//		System.out.println("row: "+R+ ", column: "+ C);

		if ((R == X_START) && (C == Y_START) && (move >1))

			return false;

		else if (R ==x && C == y)
			return true;

		else{

//			System.out.printf("Total moves: %d - Press 'Enter' to continue...", move);
//			input.nextLine();

			for(int key = 1; key <5; key++){

				switch (key){

				case 1: //down

					if (R>=7)
						break;
					//check current tile
					else if(isValidMove(array[R][C], DOWN, rotation[R-1][C-1], R, C) && isValidMove(array[R+1][C], UP, rotation[R][C-1], R+1, C)) {

						maze[R][C] = 'x';

						if( arrayTraversal(x, y, R+1, C))
							return true;
						else{
//							System.out.println("move: "+ move +". Can't move on");
							maze[R+1][C] = '0';
							maze[R][C] = '.';
						}
					}

					else
//						System.out.println("move: "+ move +". Can't go down");


					break;

				case 2: //right

					if(C>=7)
						break;

					//check current tile
					else if(isValidMove(array[R][C], RIGHT, rotation[R-1][C-1], R, C) && isValidMove(array[R][C+1], LEFT, rotation[R-1][C], R, C+1)) {

						maze[R][C] = 'x';
						
						if (arrayTraversal(x, y, R, C+1))
							return true;
						else{
//							System.out.println("move: "+ move +". Can't move on");
							maze[R][C+1] = '0';
							maze[R][C] = '.';
						}
					}
					else
//						System.out.println("move: "+ move +". Can't go right");

					break;

				case 3: //up

					if(R<=1)
						break;

					//check current tile
					else if(isValidMove(array[R][C], UP, rotation[R-1][C-1], R, C) && isValidMove(array[R-1][C], DOWN, rotation[R-2][C-1], R-1, C)) {


						maze[R][C] = 'x';
						
						if ( arrayTraversal(x, y, R-1, C))
							return true;
						else{
//							System.out.println("move: "+ move +". Can't move on");
							maze[R-1][C] = '0';
							maze[R][C] = '.';
						}

					}
					else
//						System.out.println("move: "+ move +". Can't go up");

					break;

				case 4: //left

					if (C<=1)
						break;

					else if(isValidMove(array[R][C], LEFT, rotation[R-1][C-1], R, C) && C >1 && isValidMove(array[R][C-1], RIGHT, rotation[R-1][C-2], R, C-1)) {

						maze[R][C] = 'x';

						if (arrayTraversal(x, y, R, C-1))
							return true;
						else{
//							System.out.println("move: "+ move +". Can't move on");
							maze[R][C-1] = '0';
							maze[R][C] = '.';
						}

					}
//					else
//						System.out.println("move: "+ move +". Can't go left");
				}		

			}

			// if no valid moves available mark spot
			maze[R][C] = '0';

			//			System.out.printf("Total moves: %d - Press 'Enter' to continue...", move);
			//			input.nextLine();

//			System.out.println("move: "+ move +". Can't move");
			return false;



		}
	}

//	public void setUp(){
//		for(int x = 0; x < 7; x++) {
//			for(int y = 0; y < 7; y++) {
//				GUI.board[x][y].addMouseListener(this);
//			}
//		}
//	}



	/*author - Zhuolin, Pooja */
	public static boolean isValidMove(String tileName, int key, int rotationPosition, int R, int C) {

		if (maze[R][C] != '.')
			return false;

		else if((tileName.equals("7") || tileName.equals("A")||tileName.equals("C")||tileName.equals("R") )) { // discuss with jenny which key represents up down left or right
			if (rotationPosition == 0) {
				if (key == 3) 
					return false;

			}


			else if(rotationPosition == 1) {
				if (key == 1 )
					return false;
			}

			else if(rotationPosition == 2) {
				if (key == 2 )
					return false;

			}
			else if(rotationPosition == 3) {
				if (key == 0)
					return false;


			}
		}

		else if((tileName .equals( "B")||tileName.equals( "D")||tileName.equals( "E")||tileName.equals( "F")||tileName.equals( "J")||
				tileName.equals( "H")|| tileName .equals( "L" )|| tileName.equals( "T")||tileName.equals( "W" ) ) ) { // discuss with jenny which key represents up down left or right
			if (rotationPosition == 0) {
				//left = 0	right=1 down=2 up=3	
				if (key == 2) {
					return false;
				}


			}
			else if(rotationPosition == 1) {
				if (key == 0 )
					return false;

			}
			else if(rotationPosition == 2) {
				if (key == 3)
					return false;


			}
			else if(rotationPosition == 3) {
				if (key == 1 )
					return false;

			}
		}

		else if((tileName .equals( "1") || tileName .equals( "O")||tileName.equals( "Q"))) {

			if (rotationPosition == 0) {
				//left = 0	right=1 down=2 up=3	
				if (key == 3 || key == 0 ) {
					return false;
				}

			}


			else if(rotationPosition == 1) {
				if (key == 3 || key == 1)
					return false;

			}
			else if(rotationPosition == 2) {
				if (key == 1 || key == 2)
					return false;

			}
			else if(rotationPosition == 3) {
				if (key == 0 || key == 2)
					return false;

			}

		}



		else if((tileName .equals( "P")||tileName.equals( "2"))){
			//left = 0	right=1 down=2 up=3	
			if (rotationPosition == 0) {
				if (key == 3  || key == 1) 
					return false;		

			}

			else if(rotationPosition == 1) {
				if (key == 1 || key ==2 )
					return false;

			}
			else if(rotationPosition == 2) {
				if (key == 0 || key == 2)
					return false;

			}
			else if(rotationPosition == 3) {
				if (key == 0 || key == 3)
					return false;


			}
		}

		else if((tileName .equals( "3" )|| tileName .equals( "6") )) { // discuss with jenny which key represents up down left or right
			if (rotationPosition == 0) {
				//left = 0	right=1 down=2 up=3	
				if (key == 0 || key == 2 ) {
					return false;
				}


			}
			else if(rotationPosition == 1) {
				if (key == 0 || key == 3 )
					return false;

			}
			else if(rotationPosition == 2) {
				if (key == 1 || key == 3)
					return false;


			}
			else if(rotationPosition == 3) {
				if (key == 1 || key == 2 )
					return false;

			}

		}
		else if((tileName.equals( "4")||tileName.equals("M")||tileName.equals( "N")||tileName.equals( "U" ) )) { // discuss with jenny which key represents up down left or right
			if (rotationPosition == 0) {
				//left = 0	right=1 down=2 up=3	
				if (key == 1 || key == 2 ) {
					return false;
				}

			}


			else if(rotationPosition == 1) {
				if (key == 0 || key == 2 )
					return false;

			}
			else if(rotationPosition == 2) {
				if (key == 0 || key == 3)
					return false;


			}
			else if(rotationPosition == 3) {
				if (key == 1 || key == 3 )
					return false;

			}

		}

		else if((tileName.equals("K")||tileName .equals( "S" )|| tileName.equals( "V" ))) { // discuss with jenny which key represents up down left or right
			if (rotationPosition == 0) {
				//left = 0	right=1 down=2 up=3	
				if (key == 1 ) {
					return false;
				}

			}


			else if(rotationPosition == 1) {
				if (key == 2)
					return false;

			}
			else if(rotationPosition == 2) {
				if (key == 0)
					return false;


			}
			else if(rotationPosition == 3) {
				if (key == 3 )
					return false;

			}

		}
		else if((tileName.equals( "G")||tileName .equals( "I" )||tileName.equals( "X") ) ) { // discuss with jenny which key represents up down left or right
			if (rotationPosition == 0) {
				//left = 0	right=1 down=2 up=3	
				if (key == 0 ) {
					return false;
				}

			}


			else if(rotationPosition == 1) {
				if (key == 3 )
					return false;

			}
			else if(rotationPosition == 2) {
				if (key == 1)
					return false;


			}
			else if(rotationPosition == 3) {
				if (key == 2 )
					return false;

			}

		}

		else if(tileName .equals("5") ) { // discuss with jenny which key represents up down left or right
			if (rotationPosition == 0 || rotationPosition == 2) {
				//left = 0	right=1 down=2 up=3	
				if (key == 0 || key == 1 ) {
					return false;
				}

			}


			else if(rotationPosition == 1|| rotationPosition == 3) {
				if (key == 2 || key == 3 )
					return false;

			}

		}


		return true;


	}



	public static int[][] getRotation() {
		return rotation;
	}



	public static void setRotation(int[][] rotation) {
		Board.rotation = rotation;
	}



	public static int getRotationLT() {
		return rotationLT;
	}



	public static void setRotationLT(int rotationLT) {
		Board.rotationLT = rotationLT;
	}
	
	

}
