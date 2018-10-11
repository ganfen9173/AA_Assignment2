package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import ship.Ship;
import world.World;

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
	
	private class OwnShip{
		Ship ship = null;
	    int[] rowCdns = { -1, -1, -1, -1, -1, -1, -1, -1 };
	    int[] colCdns = { -1, -1, -1, -1, -1, -1, -1, -1 };
	    boolean[] isdown = { true, true, true, true, true, true, true, true};
	    
	    private OwnShip() {}
	}
	
	OwnShip[] ownShips = new OwnShip[5];
	boolean[][] isguessed;
	
	
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
    	Guess previousGuess = new Guess();
    	if (guessList.size() != 0) {
    		previousGuess = guessList.get(guessList.size() - 1);
    		return previousGuess;
    	} 
    	
    	Guess thisGuess = new Guess();
  	
    	int turn = 0;
    	
    	//Hunting Mode  mode == 0
    	if (mode == 0) {
    		if (turn == 0) {
    			thisGuess.row = 0;
    			thisGuess.column = 0;
    		}
    		else {
    			if(previousGuess.column + 2 <= this.colSize) {
    				
    			}
    			
    		}
    		turn++;
    	}
    	
    	//Targeting Mode mode == 1
    	if (mode == 1) {
    		
    	}
    	guessList.add(thisGuess);
        // dummy return
        return thisGuess;
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
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
