/* author - Zhuolin Hou */

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GUI extends JFrame implements KeyListener, ActionListener{

	//screen size constants
	public static final int FRAME_WIDTH = 2048;
	public static final int FRAME_HEIGHT = 1536;



	//set up variables
	private static JLabel t = new JLabel();
	static Arrow[][] map = new Arrow[7][7];
	static JButton[][] board = new JButton[7][7];
	private static JLabel[][] tbf = new JLabel[4][5];
	private String[][] getBoard = Board.getArray();
	static Arrow looseTile = new Arrow(null);
	static JButton[] arrow = new JButton[12];
	private static int turn =0;
	private static int arrowPressed = 100;

	private static boolean inserted = false;


	//objects
	public static Board Board = new Board();
	public static Player[] player = new Player[4];

	public GUI() throws AWTException{

		//set up the frame
		setTitle("aMAZEing Labyrinth Game");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setSize(FRAME_WIDTH,FRAME_HEIGHT);

		//everything can happen on top of the background
		setContentPane(new JLabel(new ImageIcon (new ImageIcon ("src/graphics/background.png").getImage().getScaledInstance(FRAME_WIDTH, FRAME_HEIGHT, 0))));
		setLayout(null);

		//set up JLabels
		//set up turns
		t = new JLabel();
		t.setBounds(1630,460,450,100);
		t.setFont(new Font("Ravie",Font.BOLD, 48));
		t.setForeground(new Color(0,255,0));	//player1
		t.setText("a");

		add(t);


		Board = new Board();

		for (int r = 0; r < 7; r++)

			for(int c = 0; c < 7; c ++){
				map[r][c]= new Arrow(null);
				board[r][c] = new JButton();

			}



		//set up players
		for (int i =0; i<4; i++){

			player[i] = new Player(i);
			setPlayerImage(i);
			add(player[i]);

		}

		player[0].setBounds(340, 1068,128,128);
		player[1].setBounds(340, 300, 128, 128);
		player[2].setBounds(1108, 300, 128, 128);
		player[3].setBounds(1108, 1068, 128, 128);

		t.setText(player[0].getPlayerName());



		//set up board	

		for (int row = 0; row < 7; row++){

			for(int column = 0; column < 7; column ++){


				board[row][column].setBounds(340+128*column,300+128*row,128,128);
				board[row][column].setOpaque(false);
				board[row][column].setContentAreaFilled(false);
				board[row][column].setBorderPainted(false);
				add(board[row][column]);
				board[row][column].addActionListener(this);

				map[row][column].setBounds(340+128*column,300+128*row,128,128);
				setBoardImage(row,column);
				rotate(row,column);
				add(map[row][column]);




			}


		}

		//		Board.setUp();

		//set up treasures to be found
		for (int player = 0; player < 4; player++){

			for(int stuff = 0; stuff < 5; stuff ++){

				tbf[player][stuff]= new JLabel();

				//treasures needed by player 1
				if (player==0)
					tbf[player][stuff].setBounds(530+stuff*100,1362,100,100);
				//2
				else if (player == 1)
					tbf[player][stuff].setBounds(80,500+stuff*100,100,100);
				//3
				else if (player == 2)
					tbf[player][stuff].setBounds(530+stuff*100,40,100,100);
				//4
				else
					tbf[player][stuff].setBounds(1412,500+stuff*100,100,100);

				setTreasureImage(player, stuff);
				add(tbf[player][stuff]);
			}
		}	






		//set up JButtons
		for(int i=0; i<12; i++){
			arrow[i] = new JButton();

			if (i <3)
				arrow[i].setBounds(1276, 454+258*i, 70, 70);
			else if (i <6)
				arrow[i].setBounds(491 + 258*(i-3) ,1225, 70, 70);
			else if (i < 9)
				arrow[i].setBounds(240, 454+258*(i-6), 70, 70);
			else
				arrow[i].setBounds(491 + 258*(i-9), 197, 70, 70);

			arrow[i].setOpaque(false);
			arrow[i].setContentAreaFilled(false);
			arrow[i].setBorderPainted(false);
			add(arrow[i]);
			arrow[i].addActionListener(this);
		}

		//set up loose tile
		setLooseTileImage();
		looseTile.setBounds(1704, 800, 128, 128);
		add(looseTile);

		addKeyListener(this);
		setFocusable(true);

		//let them all show up
		setVisible(true);

		AI.ai(3);


	}

	public static void nextTurn() throws AWTException{

		//reset the position of the player JLabel
		player[turn].setBounds( 340+128*(Board.getLocationC()[turn]-1),300+128*(Board.getLocationR()[turn]-1) ,128,128);


		inserted = false;

		for(int i=0; i<5; i++)
			setTreasureImage(turn, i);

		//next player
		if (turn <3)
			turn ++;
		else
			turn = 0;

		//reset the name of the player and the text colour
		if (turn == 0){
			t.setForeground(new Color(0,255,0));	//player1



		}

		else if (turn==1){
			t.setForeground(new Color(255,0,0)); //player2

		}

		else if (turn==2){
			t.setForeground(new Color(255,241,0)); //player3


		}

		else{
			t.setForeground(new Color(0,0,255)); //player4



		}	

		t.setText(Player.getPlayerName());


		AI.ai(3); //can be any number between 0 and 3


	}




	@Override
	public void keyTyped(KeyEvent e) {


	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode()==KeyEvent.VK_LEFT ){
			looseTile.setAngle(Board.getRotationLT()* 90-90);

			if (Board.getRotationLT() > 0)
				Board.setRotationLT(Board.getRotationLT()-1);
			else
				Board.setRotationLT(3);			
		}
		else if (e.getKeyCode()==KeyEvent.VK_RIGHT){

			looseTile.setAngle(Board.getRotationLT()* 90+90);

			if (Board.getRotationLT() < 3)
				Board.setRotationLT(Board.getRotationLT()+1);
			else
				Board.setRotationLT(0);	

		}
		repaint();



	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	public static void setTreasureImage(int player, int stuff){

		ImageIcon imageIcon = new ImageIcon("src/graphics/treasures/" + Board.getTreasure()[player][stuff] + ".PNG"); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		imageIcon = new ImageIcon(newimg);  // transform it back
		tbf[player][stuff].setIcon(imageIcon);

	}

	public void setBoardImage(int row, int column){

		ImageIcon imageIcon = new ImageIcon("src/graphics/tiles/" + getBoard[row+1][column+1] +".png"); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(128, 128,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		imageIcon = new ImageIcon(newimg);  // transform it back
		map[row][column].setIcon(imageIcon);

	}

	public void setLooseTileImage(){

		ImageIcon imageIcon = new ImageIcon("src/graphics/tiles/" + Board.getLooseTile() + ".png"); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(128, 128,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		imageIcon = new ImageIcon(newimg);  // transform it back
		looseTile.setIcon(imageIcon);	
	}

	public void setPlayerImage(int i){

		ImageIcon imageIcon = new ImageIcon("src/graphics/player"+(i+1)+".png"); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(128, 128,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		imageIcon = new ImageIcon(newimg);  // transform it back
		player[i].setIcon(imageIcon);	



	}



	public void rotate(int row, int column){

		map[row][column].setAngle(90* Board.rotation[row][column]);
		setBoardImage(row,column );

		//regain focus
		looseTile.removeKeyListener(this);
		looseTile.addKeyListener(this);
		looseTile.setFocusable(true);

	}



	/* author - Zhuolin Hou & Jenny Ho */
	public void actionPerformed(ActionEvent event){

		if (event.getSource() == arrow[0] && arrowPressed != 6 && inserted == false){
			insertTile(0);
			arrowPressed = 0;

		}

		else if (event.getSource() == arrow[6] && arrowPressed != 0 && inserted == false){
			insertTile(6);
			arrowPressed = 6;
		}

		else if (event.getSource() == arrow[1] && arrowPressed != 7 && inserted == false){
			insertTile(1);
			arrowPressed = 1;
		}

		else if (event.getSource() == arrow[7] && arrowPressed != 1 && inserted == false){
			insertTile(7);
			arrowPressed = 7;
		}

		else if (event.getSource() == arrow[2] && arrowPressed != 8 && inserted == false){
			insertTile(2);
			arrowPressed = 2;
		}

		else if (event.getSource() == arrow[8] && arrowPressed != 2 && inserted == false){
			insertTile(8);
			arrowPressed = 8;
		}

		else if (event.getSource() == arrow[9] && arrowPressed != 3 && inserted == false){
			insertTile(9);
			arrowPressed = 9;
		}

		else if (event.getSource() == arrow[3] && arrowPressed != 9 && inserted == false){
			insertTile (3);
			arrowPressed = 3;
		}

		else if (event.getSource() == arrow[10] && arrowPressed != 4 && inserted == false){
			insertTile (10);
			arrowPressed = 10;
		}

		else if (event.getSource() == arrow[4] && arrowPressed != 10 && inserted == false){
			insertTile (4);
			arrowPressed = 4;
		}

		else if (event.getSource() == arrow[11] && arrowPressed!= 5 && inserted == false){
			insertTile (11);
			arrowPressed = 11;
		}

		else if (event.getSource() == arrow[5] && arrowPressed != 11 && inserted == false){
			insertTile (5);
			arrowPressed = 5;
		}

		else{
			if (GUI.isInserted() == true){


				for(int x = 0; x < 7; x++) 
					for(int y = 0; y < 7; y++) 
						if(event.getSource() == GUI.board[x][y]) {

							Board.clickedX = x;
							Board.clickedY = y;
						}

				for (int x = 0; x< 9; x++)
					for(int y=0; y<9; y++)
						Board.maze[x][y] = '.';

				Board.move = 0;

				Board.X_START = Board.locationR[GUI.getTurn()];
				Board.Y_START = Board.locationC[GUI.getTurn()];

				if(Board.arrayTraversal(Board.clickedX+1,Board.clickedY+1, Board.X_START, Board.Y_START)) {
					Board.locationR[GUI.getTurn()] = Board.clickedX+1;
					Board.locationC[GUI.getTurn()] = Board.clickedY+1;



					//make treasure found null
					for(int i = 0; i < 5; i++)	{
						if(Board.array[Board.locationR[GUI.getTurn()]][Board.locationC[GUI.getTurn()]] == Board.treasure[GUI.getTurn()][i] && Board.treasure[GUI.getTurn()][i] != null) {
							Board.treasure[GUI.getTurn()][i] = null;
							Board.found[GUI.getTurn()] ++;
						}
					}

					if(Board.found[GUI.getTurn()] == 5) {
						JOptionPane.showMessageDialog(new JFrame(), "Player " + (GUI.getTurn()+1) + " wins!");
						System.exit(0);
					}




					try {

						GUI.nextTurn();

					} catch (AWTException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

				else
					JOptionPane.showMessageDialog(new JFrame(), "Can't be reached.");

			}

			else

				JOptionPane.showMessageDialog(new JFrame(), "Insert the loose tile first.");

		}

		for(int x=0; x<7; x++){
			for (int y=0; y<7; y++){
				setBoardImage(x, y);
				rotate(x,y);
			}
		}
		setLooseTileImage();

		for(int i=0; i<4; i++)
			player[i].setBounds( 340+128*(Board.getLocationC()[i]-1),300+128*(Board.getLocationR()[i]-1) ,128,128);

		//regain focus
		for(int i = 0; i< 12; i++)
			arrow[i].setFocusable(false);
		
		for(int x=0; x<7; x++)
			for(int y=0; y<7; y++)
				board[x][y].setFocusable(false);

		
		looseTile.removeKeyListener(this);
		looseTile.addKeyListener(this);
		looseTile.setFocusable(true);
		looseTile.setAngle(0);


	}


	public static void insertTile(int x){ 

		//row 2 - right
		if (x==0){

			inserted = true;

			for(int i = 0; i<7; i++){
				Board.array[2][i] = Board.array[2][i+1];
				if (i>0)
					Board.rotation[1][i-1] = Board.rotation[1][i];
			}

			Board.array[2][7] = Board.looseTile;
			Board.rotation[1][6] = Board.rotationLT;
			Board.looseTile = Board.array[2][0];
			Board.rotationLT=0;

			for(int i=0; i<4; i++){

				if (Board.locationR[i] == 2){
					if (Board.locationC[i]>1)
						Board.locationC[i]--;
					else
						Board.locationC[i] = 7;


				}
			}


		}

		//row 2 - left

		else if ( x==6 ){

			inserted = true;


			for(int i = 8; i>1; i--){
				Board.array[2][i] = Board.array[2][i-1];
				if (i <8)
					Board.rotation[1][i-1] = Board.rotation[1][i-2];				
			}
			Board.array[2][1] = Board.looseTile;
			Board.rotation[1][0] = Board.rotationLT;
			Board.looseTile = Board.array[2][8];
			Board.rotationLT=0;


			for(int i=0; i<4; i++){

				if (Board.locationR[i] == 2){
					if (Board.locationC[i]<7)
						Board.locationC[i]++;
					else
						Board.locationC[i] = 1;



				}
			}

		}

		//row 4 - right 

		else if (x==1 ){

			inserted = true;

			for(int i = 0; i < 7; i++){
				Board.array[4][i] = Board.array[4][i+1];
				if (i>0)
					Board.rotation[3][i-1] = Board.rotation[3][i];
			}

			Board.array[4][7] = Board.looseTile;
			Board.rotation[3][6] = Board.rotationLT;
			Board.looseTile = Board.array[4][0];
			Board.rotationLT=0;


			for(int i=0; i<4; i++){

				if (Board.locationR[i] == 4){
					if (Board.locationC[i]>1)
						Board.locationC[i]--;
					else
						Board.locationC[i] = 7;



				}
			}


		}

		//row 4 - left 

		else if (x == 7){
			inserted = true;

			for(int i = 8; i > 1; i--){
				Board.array[4][i] = Board.array[4][i-1];

				if (i <8)
					Board.rotation[3][i-1] = Board.rotation[3][i-2];	
			}
			Board.array[4][1] = Board.looseTile;
			Board.rotation[3][0] = Board.rotationLT;
			Board.looseTile = Board.array[4][8];
			Board.rotationLT=0;


			for(int i=0; i<4; i++){

				if (Board.locationR[i] == 4){
					if (Board.locationC[i]<7)
						Board.locationC[i]++;
					else
						Board.locationC[i] = 1;



				}
			}

		}

		//row 6 - right

		else if (x == 2){

			inserted = true;


			for(int i = 0; i < 7; i++){
				Board.array[6][i] = Board.array[6][i+1];
				if (i>0)
					Board.rotation[5][i-1] = Board.rotation[5][i];
			}
			Board.array[6][7] = Board.looseTile;
			Board.rotation[5][6] = Board.rotationLT;
			Board.looseTile = Board.array[6][0];
			Board.rotationLT=0;

			for(int i=0; i<4; i++){

				if (Board.locationR[i] == 6){
					if (Board.locationC[i]>1)
						Board.locationC[i]--;
					else
						Board.locationC[i] = 7;



				}
			}

		}

		//row 6 - left

		else if (x==8){

			inserted = true;


			for(int i = 8; i > 1; i--){

				Board.array[6][i] = Board.array[6][i-1];
				if (i <8)
					Board.rotation[5][i-1] = Board.rotation[5][i-2];	

			}
			Board.array[6][1] = Board.looseTile;
			Board.rotation[5][0] = Board.rotationLT;
			Board.looseTile = Board.array[6][8];
			Board.rotationLT=0;

			for(int i=0; i<4; i++){

				if (Board.locationR[i] == 6){
					if (Board.locationC[i]<7)
						Board.locationC[i]++;
					else
						Board.locationC[i] = 1;



				}
			}

		}

		//column 2 - above 

		else if (x==9){

			inserted = true;


			for(int i = 8; i > 1; i--){
				Board.array[i][2] = Board.array[i-1][2];
				if (i <8)
					Board.rotation[i-1][1] = Board.rotation[i-2][1];	
			}
			Board.array[1][2] = Board.looseTile;
			Board.rotation[0][1] = Board.rotationLT;
			Board.looseTile = Board.array[8][2];
			Board.rotationLT=0;

			for(int i=0; i<4; i++){

				if (Board.locationC[i] == 2){
					if (Board.locationR[i]<7)
						Board.locationR[i]++;
					else
						Board.locationR[i] = 1;



				}
			}



		}

		//column 2 - below 

		else if (x==3){

			inserted = true;


			for(int i = 0; i < 7; i++){
				Board.array[i][2] = Board.array[i+1][2];
				if (i>0)
					Board.rotation[i-1][1] = Board.rotation[i][1];
			}
			Board.array[7][2] = Board.looseTile;
			Board.rotation[6][1] = Board.rotationLT;
			Board.looseTile = Board.array[0][2];
			Board.rotationLT=0;



			for(int i=0; i<4; i++){

				if (Board.locationC[i] == 2){
					if (Board.locationR[i]>1)
						Board.locationR[i]--;
					else
						Board.locationR[i] = 7;



				}
			}

		}

		//column 4 - above 

		else if (x==10){

			inserted = true;


			for(int i = 8; i > 1; i--){
				Board.array[i][4] = Board.array[i-1][4];
				if (i <8)
					Board.rotation[i-1][3] = Board.rotation[i-2][3];	
			}
			Board.array[1][4] = Board.looseTile;
			Board.rotation[0][3] = Board.rotationLT;
			Board.looseTile = Board.array[8][4];
			Board.rotationLT=0;


			for(int i=0; i<4; i++){

				if (Board.locationC[i] == 4){
					if (Board.locationR[i]<7)
						Board.locationR[i]++;
					else
						Board.locationR[i] = 1;



				}
			}


		}

		//column 4 - below 

		else if ( x==4){

			inserted = true;


			for(int i = 0; i < 7; i++){
				Board.array[i][4] = Board.array[i+1][4];
				if (i>0)
					Board.rotation[i-1][3] = Board.rotation[i][3];
			}
			Board.array[7][4] = Board.looseTile;
			Board.rotation[6][3] = Board.rotationLT;
			Board.looseTile = Board.array[0][4];
			Board.rotationLT=0;



			for(int i=0; i<4; i++){

				if (Board.locationC[i] == 4){
					if (Board.locationR[i]>1)
						Board.locationR[i]--;
					else
						Board.locationR[i] = 7;



				}
			}

		}

		//column 6 - above 

		else if (x==11){
			inserted = true;


			for(int i = 8; i > 1; i--){
				Board.array[i][6] = Board.array[i-1][6];
				if (i <8)
					Board.rotation[i-1][5] = Board.rotation[i-2][5];		
			}
			Board.array[1][6] = Board.looseTile;
			Board.rotation[0][5] = Board.rotationLT;
			Board.looseTile = Board.array[8][6];
			Board.rotationLT=0;



			for(int i=0; i<4; i++){

				if (Board.locationC[i] == 6){
					if (Board.locationR[i]<7)
						Board.locationR[i]++;
					else
						Board.locationR[i] = 1;



				}
			}

		}

		//column 6 - below 

		else if (x==5){

			inserted = true;


			for(int i = 0; i < 7; i++){
				Board.array[i][6] = Board.array[i+1][6];
				if (i>0)
					Board.rotation[i-1][5] = Board.rotation[i][5];
			}
			Board.array[7][6] = Board.looseTile;
			Board.rotation[6][5] = Board.rotationLT;
			Board.looseTile = Board.array[0][6];
			Board.rotationLT=0;


			for(int i=0; i<4; i++){

				if (Board.locationC[i] == 6){
					if (Board.locationR[i]>1)
						Board.locationR[i]--;
					else
						Board.locationR[i] = 7;



				}
			}

		}


	}


	public static int getTurn() {
		return turn;
	}

	public static void setTurn(int turn) {
		GUI.turn = turn;
	}

	public static boolean isInserted() {
		return inserted;
	}

	public static void setInserted(boolean inserted) {
		GUI.inserted = inserted;
	}

	public static int getArrowPressed() {
		return arrowPressed;
	}

	public static void setArrowPressed(int arrowPressed) {
		GUI.arrowPressed = arrowPressed;
	}





}
