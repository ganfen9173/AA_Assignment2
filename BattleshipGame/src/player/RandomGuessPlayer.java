package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import world.World;
import ship.Ship;

/**
 * Random guess player (task A).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class RandomGuessPlayer implements Player{

	int rowSize = 0;
	int colSize = 0;
	
	private class OwnShip{
		Ship ship = null;
	    ArrayList<Integer> rowCdns = new ArrayList<Integer>();
	    ArrayList<Integer> colCdns = new ArrayList<Integer>();
	    ArrayList<Boolean> isdown = new ArrayList<Boolean>();
	    
	    private OwnShip() {}
	}
	
	boolean[][] isguessed;
	ArrayList<OwnShip> ownShips = new ArrayList<OwnShip>();

	
    public void initialisePlayer(World world) {
        // To be implemented.
    	this.rowSize = world.numRow;
    	this.colSize = world.numColumn;
    	this.isguessed = new boolean[this.rowSize][this.colSize + (this.rowSize + 1) / 2];
    	
    	int i = 0;
    	
    	for (World.ShipLocation localShipLocation : world.shipLocations) {
    		OwnShip ship = new OwnShip();
    		ship.ship =  localShipLocation.ship;
    	    ownShips.add(ship);
    	    
    	    for (int j = 0; j < ownShips.get(i).ship.len() * ownShips.get(i).ship.width(); j++) {
    	    	ownShips.get(i).rowCdns.add(((World.Coordinate)localShipLocation.coordinates.get(j)).row);
    	    	ownShips.get(i).colCdns.add(((World.Coordinate)localShipLocation.coordinates.get(j)).column);
    	    	ownShips.get(i).isdown.add(false);
    	    }
    	      	i++;
    	}
    	
    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
        // To be implemented.
    	Answer answer = new Answer();
    	for (int i = 0; i < ownShips.size(); i++) {
    		for (int j = 0;j <ownShips.get(i).ship.len() * ownShips.get(i).ship.width(); j++) {
    			if ((guess.row == ownShips.get(i).rowCdns.get(j)) && (guess.column == ownShips.get(i).colCdns.get(j))) {
    				answer.isHit = true;
    				ownShips.get(i).isdown.get(j).equals(true) ;
    				int x = 1;
    				for (int y = 0;y < ownShips.get(i).ship.len() * ownShips.get(i).ship.width(); y++) {
    					if (ownShips.get(i).isdown.get(j) == false) {
    						x = 0;
    					}
    				}
    				
    				if (x != 0) {
    					answer.shipSunk = ownShips.get(i).ship;
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
    	Random random = new Random();
    	int i;
    	int j;
    	
    	do {
          i = random.nextInt(this.rowSize);
          j = random.nextInt(this.colSize);
        } while (this.isguessed[i][j]);
    	
    	Guess randomGuess = new Guess();
    	randomGuess.row = i;
    	randomGuess.column = j;
    	return randomGuess;
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
    	isguessed[guess.row][guess.column] = true;
    } // end of update()


    @Override
    public boolean noRemainingShips() {
        // To be implemented.
    	for (int i = 0; i < ownShips.size(); i++) {
    		for (int j = 0; j < ownShips.get(i).ship.len() * ownShips.get(i).ship.width(); j++) {
    			if (!(ownShips.get(i).isdown.get(j))) {
    				return false;
    	        }
    		}
    	}
    	return true;	
    }
    // end of noRemainingShips()

} // end of class RandomGuessPlayer

