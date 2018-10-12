package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import ship.Ship;
import world.World;
import world.World.Coordinate;

/**
 * Greedy guess player (task B).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class GreedyGuessPlayer  implements Player{
	static final int numShips = 5;
//	static final int maxLen = 4;
//	static final int maxWid = 2;
//	static final int[] rowDeltas = { 1, 0, -1, 0, 1, 0, -1, 0 };
//	static final int[] clnDeltas = { 0, -1, 0, 1, 1, 0, -1, 0 };
	int rowSize = 0;
	int colSize = 0;
	boolean isHex = false;
	
	List<Guess> guessList = new ArrayList<Guess>();
	
	int mode = 0;
	int turn = 0;
	
	private class OwnShip{
		Ship ship = null;
	    int[] rowCdns = { -1, -1, -1, -1, -1, -1, -1, -1 };
	    int[] colCdns = { -1, -1, -1, -1, -1, -1, -1, -1 };
	    boolean[] isdown = { true, true, true, true, true, true, true, true};
	    
	    private OwnShip() {}
	}
	
	OwnShip[] ownShips = new OwnShip[5];
	boolean[][] isguessed;
	Guess lastGuess = new Guess();
	
/*    boolean isIn(Guess guess) {
        if (isHex)
            return guess.row >= 0 && guess.row < numRow && guess.column >= (guess.row + 1) / 2 && guess.column < numColumn + (guess.row + 1) / 2;
        else
            return guess.row >= 0 && guess.row < numRow && guess.column >= 0 && guess.column < numColumn;
    }*/

	
    @Override
    public void initialisePlayer(World world) {
        // To be implemented.
    	this.rowSize = world.numRow;
    	this.colSize = world.numColumn;
    	this.isHex = world.isHex;
    	this.isguessed = new boolean[this.rowSize][this.colSize + (this.rowSize + 1) / 2];
    	
    	int i = 0;
    	
    	for (World.ShipLocation localShipLocation : world.shipLocations) {
    		this.ownShips[i] = new OwnShip();
    	    this.ownShips[i].ship = localShipLocation.ship;
    	    for (int j = 0; j < this.ownShips[i].ship.len() * this.ownShips[i].ship.width(); j++) {
    	    	this.ownShips[i].rowCdns[j] = ((World.Coordinate)localShipLocation.coordinates.get(j)).row;
    	    	this.ownShips[i].colCdns[j] = ((World.Coordinate)localShipLocation.coordinates.get(j)).column;
    	    	this.ownShips[i].isdown[j] = false;
    	    }
    	      	i++;
    	}
    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
    	Answer answer = new Answer();
    	for (int i = 0; i < 5; i++) {
    		for (int j = 0;j < this.ownShips[i].ship.len() * this.ownShips[i].ship.width(); j++) {
    			if ((guess.row == this.ownShips[i].rowCdns[j]) && (guess.column == this.ownShips[i].colCdns[j])) {
    				answer.isHit = true;
    				this.ownShips[i].isdown[j] = true;
    				int x = 1;
    				for (int y = 0;y < this.ownShips[i].ship.len() * this.ownShips[i].ship.width(); y++) {
    					if (this.ownShips[i].isdown[y] == false) {
    						x = 0;
    					}
    				}
    				
    				if (x != 0) {
    					answer.shipSunk = this.ownShips[i].ship;
    				}
    				return answer;
    			}
    		}
    	}
       return answer;
    } // end of getAnswer()


    @Override
    public Guess makeGuess() {
        // To be implemented.
 	
    	Guess thisGuess = new Guess();    	
    	//Hunting Mode  mode == 0
    	if (mode == 0) {
    		if (turn == 0) {
    			thisGuess.row = 0;
    			thisGuess.column = 0;
    		}
    		else {
    			int i = lastGuess.row;
    	    	int j = lastGuess.column;
    			if(j + 2 > this.colSize-1) {
    				if(i + 1 > this.rowSize-1) {
    					Random random = new Random();
    					while (this.isguessed[i][j]){
    				        i = random.nextInt(this.rowSize);
    				        j = random.nextInt(this.colSize);
    			    	}   	
    				 	thisGuess.row = i;
    				   	thisGuess.column = j;
    				   	System.out.println("1"+thisGuess);
    				   	return thisGuess;   				   	
    				}
    				else {
    					if((i+1) % 2 == 0){
        					j = 0;
        				}
        				if ((i+1) %2 == 1) {
        					j = 1; 					
        				}
        				while (isguessed[i+1][j]){
        					j = j+ 1;
        					if(j==this.colSize) {
        						Random random = new Random();
        						while (this.isguessed[i][j]){
            				        i = random.nextInt(this.rowSize);
            				        j = random.nextInt(this.colSize);
            			    	}        				    	
            				 	thisGuess.row = i;
            				   	thisGuess.column = j;
            				   	System.out.println("2"+thisGuess);
            				   	return thisGuess;
        					}
        				}
    				}
    				thisGuess.row = i+1;
    				thisGuess.column =j;
    			}else{
    				if(!isguessed[i][j+2]) {
    					thisGuess.row = i;
    					thisGuess.column = j+2;
    					}
    				else {
    					Random random = new Random();
    					while (this.isguessed[i][j]){
    				        i = random.nextInt(this.rowSize);
    				        j = random.nextInt(this.colSize);
    			    	} 
    				    	
    				 	thisGuess.row = i;
    				   	thisGuess.column = j;
    				   	System.out.println("1"+thisGuess);
    				}
    			} 			
    		}
    		
    	}
    	
    	//Targeting Mode mode == 1
    	if (mode == 1) {    		
    	}
    	turn++;
    	lastGuess = thisGuess;
    	//System.out.println(thisGuess);
        return thisGuess;
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
    	isguessed[guess.row][guess.column] = true;
    	lastGuess = guess;
    } // end of update()


    @Override
    public boolean noRemainingShips() {
        // To be implemented.
    	for (int i = 0; i < 5; i++) {
    		for (int j = 0; j < this.ownShips[i].ship.len() * this.ownShips[i].ship.width(); j++) {
    			if (!this.ownShips[i].isdown[j]) {
    				return false;
    	        }
    		}
    	}
    	return true;	
    } // end of noRemainingShips()

} // end of class GreedyGuessPlayer
