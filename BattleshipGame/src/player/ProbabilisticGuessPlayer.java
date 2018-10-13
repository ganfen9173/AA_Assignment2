package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import ship.Ship;
import world.World;

/**
 * Probabilistic guess player (task C).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class ProbabilisticGuessPlayer  implements Player{
	static final int numShips = 5;

	int rowSize = 0;
	int colSize = 0;
	
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
	List<String> targetShips = new ArrayList<String>();
	
	boolean isIn(Guess guess, int outter) {
    	return guess.row >= outter && guess.row < rowSize-outter && guess.column >= outter && guess.column < colSize-outter;
    }
	
	Guess randomGuess(int outter) {
    	int i = 0,j = 0;
    	Guess randomGuess = new Guess();
    	Random random = new Random();
		do{
	        i = random.nextInt(this.rowSize-outter*2)+outter;
	        j = random.nextInt(this.colSize-outter*2)+outter;
    	}while (this.isguessed[i][j]);
		randomGuess.row = i;
		randomGuess.column = j;
	   	return randomGuess;
    }
	
	public Ship getTargetShip(String shipName) {
		if (ownShips[1].ship.name() == "AircraftCarrier")
			return ownShips[1].ship;
		if (ownShips[2].ship.name() == "Cruiser")
			return ownShips[2].ship;
		if (ownShips[3].ship.name() == "Frigate")
			return ownShips[3].ship;
		if (ownShips[4].ship.name() == "PatrolCraft")
			return ownShips[4].ship;
		if (ownShips[5].ship.name() == "Submarine")
			return ownShips[5].ship;
		return null;
	}
		
    @Override
    public void initialisePlayer(World world) {
        // To be implemented.
    	this.rowSize = world.numRow;
    	this.colSize = world.numColumn;
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
    	    targetShips.add(this.ownShips[i].ship.name());
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
        
    	Guess thisGuess = new Guess();
    	// Hunting mode
    	if (mode == 0) {
    		if(turn <this.rowSize*2) {
    			String shipName="";
        		Random randomShip = new Random();
        		do {
        			shipName = targetShips.get(randomShip.nextInt(targetShips.size()));
        		}while (!this.targetShips.contains(shipName));
        		thisGuess = randomGuess(getTargetShip(shipName).len());
        		turn++;
        		return thisGuess;
        	}
    		else {
        		thisGuess =  randomGuess(0);
        	}
    		
    	}
    		
    	
        // dummy return
        return null;
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

} // end of class ProbabilisticGuessPlayer


