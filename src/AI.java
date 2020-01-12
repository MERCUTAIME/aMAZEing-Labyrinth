import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Scanner;

/* if ai has enemy, push/ block/ find treasure/ go to the edge/ random, then move
 * if it doesn't have a primary enemy, find treasure/ go to the edge/ push/ block/ random, then move
 * 
 * after trying to go to edge, if already tried to push, rand, if not, push
 * after trying to block, if already tried to find treasure, rand, if not, find
 */


public class AI extends Board{

	static int[] treasureX = new int[5]; //of enemy
	static int[] treasureY = new int[5];
	static boolean alreadyPushed = false;
	static boolean alreadyFound = false;
	static int[] tX = new int[5]; //of ai
	static int[] tY = new int[5];



	public static void ai(int turn) {

		System.out.println("ai");


		if (GUI.getTurn() == turn){

			int enemy = 100; //0-3 - enemy; 100 - no enemy
			boolean bigEnemy = false;
			alreadyPushed = false;
			alreadyFound = false;

			//when ai has more than 1 treasures to be found
			//check if a player finds 2 more treasures or only has 1 left to go
			if (found[turn] < 4){
				for (int i =0; i<3; i++){

					if (i!=turn){

						if (found[i]==4 ){

							if(enemy > 3 || (i>turn && enemy<turn) ){
								enemy = i;
								bigEnemy = true;
							}
						}

						else if (found[i] - found[turn] >=2 && bigEnemy == false){

							if(enemy > 3 || (i>turn && enemy<turn) )
								enemy = i;

						}
					}
				}

				//if no enemy is threatening, go find ai's own treasures
				if (enemy <4)
					push(enemy, turn);

				else{
					findTreasure(turn);

				}

			}
		}
	}

	private static void push(int enemy, int turn)  {

		System.out.println("push");

		alreadyPushed = true;

		int arrow;
		int block;
		int far = 0;
		int near = 0;
		boolean canReach = false;

		//if the enemy can be pushed away from the his treasures to the other side
		//first/last row
		if ((locationR[enemy] == 1 || locationR[enemy] == 7) && locationC[enemy]%2==0){

			for(int i=0; i<5; i++){


				for(int x=1;x<8;x++){

					for(int y=1; y<8; y++){

						if(array[x][y] == treasure[enemy][i]){

							treasureX[i] = x;
							treasureY[i] = y;

							if (Math.abs(x-locationR[enemy])<4)
								near++;
							else
								far++;						

						}						
					}
				}

			}

			//push
			if(near >= far){

				if(locationR[enemy] == 1){

					if (2 + locationC[enemy]/2 == GUI.getArrowPressed())
						canReach = true;

					else {


						GUI.insertTile(8 + locationC[enemy]/2);

						//check if the enemy can directly reach his treasures
						for(int i=0;i<5;i++){

							if(treasure[enemy][i] != null){
								
								for (int x = 0; x< 9; x++)
									for(int y=0; y<9; y++)
										Board.maze[x][y] = '.';
								
								Board.move = 0;

								Board.X_START = Board.locationR[enemy];
								Board.Y_START = Board.locationC[enemy];


								if(arrayTraversal(treasureX[i],treasureY[i], locationR[enemy], locationC[enemy]))
									canReach = true;				

							}

						}

						//recover the original position
						GUI.insertTile(2 + locationC[enemy]/2 );
						GUI.setInserted(false);

					}

					if(canReach == false){

						System.out.println("arrow "+8 + locationC[enemy]/2);
						GUI.arrow[8 + locationC[enemy]/2].doClick();

						move(turn);
					}
					else{

						//try every arrows to block the enemy's way
						block(enemy, turn);
						canReach = false;

					}

				}

				if(locationR[enemy] == 7){

					if (8 + locationC[enemy]/2 == GUI.getArrowPressed())
						canReach = true;

					else {
						GUI.insertTile(2 + locationC[enemy]/2);

						//check if the enemy can directly reach his treasures
						for(int i=0;i<5;i++){

							if(treasure[enemy][i] != null){
								
								Board.move = 0;

								Board.X_START = Board.locationR[enemy];
								Board.Y_START = Board.locationC[enemy];


								for (int x = 0; x< 9; x++)
									for(int y=0; y<9; y++)
										Board.maze[x][y] = '.';
								
								if(arrayTraversal(treasureX[i],treasureY[i], locationR[enemy], locationC[enemy]))
									canReach = true;				

							}

						}

						//recover the original position
						GUI.insertTile(8 + locationC[enemy]/2 );
						GUI.setInserted(false);

					}

					if(canReach == false){
						System.out.println("arrow " + 2 + locationC[enemy]/2);
						GUI.arrow[2 + locationC[enemy]/2].doClick();

						move(turn);
					}

					else{

						//try every arrows to block the enemy's way
						block(enemy, turn);
						canReach = false;

					}

				}
			}

			//if most of his treasures are on the other side
			else{

				//try every arrows to block the enemy's way
				block(enemy, turn);

			}
		}

		//first/last column

		else if ((locationC[enemy] == 1 || locationC[enemy] == 7) && locationR[enemy]%2==0){

			for(int i=0; i<5; i++){


				for(int x=0;x<9;x++){

					for(int y=0; y<9; y++){

						if(array[x][y] == treasure[enemy][i]){

							treasureX[i] = x;
							treasureY[i] = y;

							if (Math.abs(y-locationC[enemy])<4)
								near++;
							else
								far++;						

						}						
					}
				}

			}

			//push
			if(near >= far){

				if(locationC[enemy] == 1){

					if (5 + locationR[enemy]/2 == GUI.getArrowPressed())
						canReach = true;

					else {
						GUI.insertTile(-1 + locationR[enemy]/2);

						//check if the enemy can directly reach his treasures
						for(int i=0;i<5;i++){

							if(treasure[enemy][i] != null){
								
								Board.move = 0;

								Board.X_START = Board.locationR[enemy];
								Board.Y_START = Board.locationC[enemy];

								for (int x = 0; x< 9; x++)
									for(int y=0; y<9; y++)
										Board.maze[x][y] = '.';

								if(arrayTraversal(treasureX[i],treasureY[i], locationR[enemy], locationC[enemy]))
									canReach = true;				

							}

						}

						//recover the original position
						GUI.insertTile(5 + locationR[enemy]/2 );
						GUI.setInserted(false);

					}

					if(canReach == false){
						System.out.println("arrow " + -1 + locationR[enemy]/2);
						GUI.arrow[-1 + locationR[enemy]/2].doClick();
						move(turn);

					}
					else{

						//try every arrows to block the enemy's way
						block(enemy, turn);
						canReach = false;

					}

				}

				if(locationC[enemy] == 7){

					if (-1 + locationR[enemy]/2 == GUI.getArrowPressed())
						canReach = true;

					else {
						GUI.insertTile(5 + locationR[enemy]/2);

						//check if the enemy can directly reach his treasures
						for(int i=0;i<5;i++){

							if(treasure[enemy][i] != null){
								
								Board.move = 0;

								Board.X_START = Board.locationR[enemy];
								Board.Y_START = Board.locationC[enemy];

								for (int x = 0; x< 9; x++)
									for(int y=0; y<9; y++)
										Board.maze[x][y] = '.';
								
								if(arrayTraversal(treasureX[i],treasureY[i], locationR[enemy], locationC[enemy]))
									canReach = true;				

							}

						}

						//recover the original position
						GUI.insertTile(-1 + locationR[enemy]/2 );
						GUI.setInserted(false);

					}

					if(canReach == false){
						System.out.println("arrow " + 5 + locationR[enemy]/2);
						GUI.arrow[5 + locationR[enemy]/2].doClick();
						move(turn);
					}
					else{

						//try every arrows to block the enemy's way
						block(enemy, turn);
						canReach = false;

					}

				}
			}

			//if most of his treasures are on the other side
			else{

				//try every arrows to block the enemy's way
				block(enemy, turn);

			}
		}

		else{

			//try every arrows to block the enemy's way
			block(enemy, turn);

		}

	}



	private static void block(int enemy, int turn)  {

		System.out.println("block");

		boolean canReach = false;

		outerloop:
			for(int count = 0; count<4; count++){

				// Simulate a key press
				GUI.looseTile.setAngle(Board.getRotationLT()* 90-90);

				if (getRotationLT() > 0)
					setRotationLT(Board.getRotationLT()-1);
				else
					setRotationLT(3);	

				GUI.looseTile.repaint();

				for(int i=0; i<6; i++){

					canReach = false;

					if (6 + i == GUI.getArrowPressed())
						canReach = true;

					else {
						GUI.insertTile(i);

						//check if the enemy can directly reach his treasures
						for(int x=0;x<5;x++){

							if(treasure[enemy][x] != null){
								
								Board.move = 0;

								Board.X_START = Board.locationR[enemy];
								Board.Y_START = Board.locationC[enemy];

								for (int z = 0; z< 9; z++)
									for(int y=0; y<9; y++)
										Board.maze[z][y] = '.';
								
								if(arrayTraversal(treasureX[x],treasureY[x], locationR[enemy], locationC[enemy]))
									canReach = true;				

							}

						}

						//recover the original position
						GUI.insertTile(i+6);
						GUI.setInserted(false);
					}

					if(canReach == false){
						System.out.println("arrow " + i);
						GUI.arrow[i].doClick();
						move(turn);
						break outerloop;
					}


				}


				if (canReach == true){

					for(int i=6; i<12; i++){

						canReach = false;

						if ( i-6 == GUI.getArrowPressed())
							canReach = true;

						else{

							GUI.insertTile(i);

							//check if the enemy can directly reach his treasures
							for(int x=0;x<5;x++){

								if(treasure[enemy][x] != null){
									
									Board.move = 0;

									Board.X_START = Board.locationR[enemy];
									Board.Y_START = Board.locationC[enemy];

									for (int z = 0; z< 9; z++)
										for(int y=0; y<9; y++)
											Board.maze[z][y] = '.';
									
									if(arrayTraversal(treasureX[x],treasureY[x], locationR[enemy], locationC[enemy]))
										canReach = true;				

								}

							}

							//recover the original position
							GUI.insertTile(i-6);
							GUI.setInserted(false);
						}

						if(canReach == false){
							System.out.println("arrow " + i);
							GUI.arrow[i].doClick();
							move(turn);
							break outerloop;
						}

					}
				}
			}

		//if can't block enemy, find ai's own treasures
		if(canReach == true){

			if (!alreadyFound)
				findTreasure(turn);
			else
				randInsert(turn);

		}

	}


	private static void randInsert(int turn) {

		System.out.println("rand");

		Random rand = new Random();
		int i;

		do{
			i = rand.nextInt(12);
		} while (i+6 == GUI.getArrowPressed() || i-6 == GUI.getArrowPressed());

		System.out.println("arrow " + i);
		GUI.arrow[i].doClick();
		move(turn);


	}


	private static void findTreasure(int turn)  {

		System.out.println("find");

		alreadyFound = true;
		boolean canGo = false;

		//check where are the treasures needed
		for(int i=0; i<5; i++){

			if (treasure[turn][i] != null){

				for(int x=1; x<8; x++){
					for(int y=1; y<8; y++){

						if (array[x][y] == treasure[turn][i] ){

							tX[i] = x;
							tY[i] = y;

						}
					}
				}
			}
		}

		outerloop:
			for(int count = 0; count<4; count++){


				// Simulate a key press
				GUI.looseTile.setAngle(Board.getRotationLT()* 90-90);

				if (getRotationLT() > 0)
					setRotationLT(Board.getRotationLT()-1);
				else
					setRotationLT(3);	

				GUI.looseTile.repaint();


				for(int i=0; i<6; i++){

					if (6 + i != GUI.getArrowPressed()){

						GUI.insertTile(i);

						//check if the ai can reach his treasures
						for(int x=0;x<5;x++){

							if(treasure[turn][x] != null){
								
								Board.move = 0;

								Board.X_START = Board.locationR[turn];
								Board.Y_START = Board.locationC[turn];

								for (int z = 0; z< 9; z++)
									for(int y=0; y<9; y++)
										Board.maze[z][y] = '.';
								
								if(arrayTraversal(tX[x],tY[x], locationR[turn], locationC[turn]))
									canGo = true;				

							}

						}

						//recover the original position
						GUI.insertTile(i+6);
						GUI.setInserted(false);
					}

					if(canGo == true){
						System.out.println("arrow " + i);
						GUI.arrow[i].doClick();
						move(turn);
						break outerloop;
					}

				}

				if (canGo == false){

					for(int i=6; i<12; i++){

						if ( i-6 != GUI.getArrowPressed()){

							GUI.insertTile(i);

							//check if the enemy can directly reach his treasures
							for(int x=0;x<5;x++){

								if(treasure[turn][x] != null){
									
									Board.move = 0;

									Board.X_START = Board.locationR[turn];
									Board.Y_START = Board.locationC[turn];

									for (int z = 0; z< 9; z++)
										for(int y=0; y<9; y++)
											Board.maze[z][y] = '.';
									
									if(arrayTraversal(tX[x],tY[x], locationR[turn], locationC[turn]))
										canGo = true;				

								}

							}

							//recover the original position
							GUI.insertTile(i-6);
							GUI.setInserted(false);
						}

						if(canGo == true){
							System.out.println("arrow " + i);
							GUI.arrow[i].doClick();
							move(turn);
							break outerloop;
						}

					}
				}
			}

		if (canGo == false){

			pushAI(turn);


		}

	}

	private static void pushAI(int turn)  {

		System.out.println("pushAI");

		int near = 0, far = 0;

		//if ai can be pushed to his treasures on the other side
		//first/last row
		if ((locationR[turn] == 1 || locationR[turn] == 7) && locationC[turn]%2==0){

			for(int i=0; i<5; i++){
				if (treasure[turn][i] != null){

					if (Math.abs( tX[i]-locationR[turn])<4)
						near++;
					else
						far++;						

				}						
			}

			//push
			if(near < far){

				if(locationR[turn] == 1){

					if (2 + locationC[turn]/2 != GUI.getArrowPressed()) {

						System.out.println("arrow " + 8 + locationC[turn]/2);
						GUI.arrow[8 + locationC[turn]/2].doClick();
						move(turn);

					}
					else{

						if (!alreadyPushed){

							if(turn ==3 )
								push(0, turn);
							else
								push(turn+1, turn);

						}
						else
							randInsert(turn);
					}

				}

				if(locationR[turn] == 7){

					if (8 + locationC[turn]/2 != GUI.getArrowPressed()){

						System.out.println("arrow " + 2 + locationC[turn]/2);
						GUI.arrow[2 + locationC[turn]/2].doClick();
						move(turn);

					}

					else{

						if (!alreadyPushed){

							if(turn ==3 )
								push(0, turn);
							else
								push(turn+1, turn);

						}
						else
							randInsert(turn);

					}

				}
			}

			//if most of his treasures are near
			else{

				if (!alreadyPushed){

					if(turn ==3 )
						push(0, turn);
					else
						push(turn+1, turn);

				}
				else
					randInsert(turn);

			}
		}

		//first/last column

		else if ((locationC[turn] == 1 || locationC[turn] == 7) && locationR[turn]%2==0){

			for(int i=0; i<5; i++){
				if (treasure[turn][i] != null){

					if (Math.abs(tY[i]-locationC[turn])<4)
						near++;
					else
						far++;						

				}
			}

			//push
			if(near < far){

				if(locationC[turn] == 1){

					if (-1 + locationR[turn]/2 != GUI.getArrowPressed()){

						System.out.println("arrow " + 5 + locationR[turn]/2);
						GUI.arrow[5 + locationR[turn]/2].doClick();					
						move(turn);


					}
					else{

						if (!alreadyPushed){

							if(turn ==3 )
								push(0, turn);
							else
								push(turn+1, turn);

						}
						else
							randInsert(turn);

					}

				}

				if(locationC[turn] == 7){

					if (5 + locationR[turn]/2 != GUI.getArrowPressed()){

						System.out.println("arrow " + -1 + locationR[turn]/2);
						GUI.arrow[-1 + locationR[turn]/2].doClick();
						move(turn);

					}
					else{

						if (!alreadyPushed){

							if(turn ==3 )
								push(0, turn);
							else
								push(turn+1, turn);

						}
						else
							randInsert(turn);

					}

				}
			}

			//if most of his treasures are near
			else{

				if (!alreadyPushed){

					if(turn ==3 )
						push(0, turn);
					else
						push(turn+1, turn);

				}
				else
					randInsert(turn);

			}
		}
		else{

			if (!alreadyPushed){

				if(turn ==3 )
					push(0, turn);
				else
					push(turn+1, turn);

			}
			else
				randInsert(turn);

		}

	}

	private static void move(int turn)  {

		System.out.println("move");

		boolean clicked = false;

		outerloop:
			for(int i=0; i<5; i++){

				for(int x=1; x<8; x++){
					for(int y=1; y<8; y++){

						if (array[x][y] == treasure[turn][i] ){
							
							Board.move = 0;

							Board.X_START = Board.locationR[turn];
							Board.Y_START = Board.locationC[turn];
							
							for (int z = 0; z< 9; z++)
								for(int n=0; n<9; n++)
									Board.maze[z][n] = '.';

							if(arrayTraversal(x,y, locationR[turn], locationC[turn])){

								System.out.println(x +", "+y);
								GUI.board[x-1][y-1].doClick();
								clicked = true;
								break outerloop;

							}

						}
					}
				}
			}


		if (!clicked){
			
			GUI.board[locationR[turn]-1][locationC[turn]-1].doClick();

		}





	}


}







