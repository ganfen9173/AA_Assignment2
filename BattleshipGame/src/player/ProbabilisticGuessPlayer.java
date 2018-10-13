package player;

import java.util.ArrayList;
import java.util.HashMap;
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
	int rowSize = 0;
	int colSize = 0;
	
	int mode = 0;
	int turn = 0;
	
	private class OwnShip{
		Ship ship = null;
		List<Integer> rowCdns = new ArrayList<Integer>();
		List<Integer> colCdns = new ArrayList<Integer>();
	    List<Boolean> isdown = new ArrayList<Boolean>();
	    
	    private OwnShip() {}
	}
	
	
	List<OwnShip> ownShips = new ArrayList<OwnShip>();
	boolean[][] isguessed;
	Guess lastGuess = new Guess();
	Guess firstTarget = new Guess();
	List<String> targetShips = new ArrayList<String>();
	List<Guess> targetList = new ArrayList<Guess>();
	HashMap<Guess,Boolean> targetMap = new HashMap<Guess,Boolean>();
	
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
		for (OwnShip i: ownShips) {
			if (i.ship.name()==shipName) {
				return i.ship;
			}
		}
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
    		OwnShip ship = new OwnShip();
    		ownShips.add(ship);
    	    ownShips.get(i).ship = localShipLocation.ship;
    	    for (int j = 0; j < ownShips.get(i).ship.len() *  ownShips.get(i).ship.width(); j++) {
    	    	ownShips.get(i).rowCdns.add(((World.Coordinate)localShipLocation.coordinates.get(j)).row) ;
    	    	ownShips.get(i).colCdns.add(((World.Coordinate)localShipLocation.coordinates.get(j)).column);
    	    	ownShips.get(i).isdown.add(false);
    	    }
    	    targetShips.add(ship.ship.name());
    	    i++;
    	}
    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
    	Answer answer = new Answer();
    	for (int i = 0; i < ownShips.size(); i++) {
    		for (int j = 0;j < ownShips.get(i).ship.len() * ownShips.get(i).ship.width(); j++) {
    			if ((guess.row == ownShips.get(i).rowCdns.get(j)) && (guess.column == ownShips.get(i).colCdns.get(j))) {
    				answer.isHit = true;
    				ownShips.get(i).isdown.set(j, true);

    				int x = 1;
    				for (int y = 0;y < ownShips.get(i).ship.len() * ownShips.get(i).ship.width(); y++) {
    					if (!ownShips.get(i).isdown.get(y)) {

    						x = 0;
    					}
    				}
    				
    				if (x != 0) {
    					answer.shipSunk = ownShips.get(i).ship;
    					System.out.println("Ship is sunk");
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
    		if(turn <this.rowSize) {
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
        		turn ++;
        		return thisGuess;
        	}
    		
    	}
    	if (mode == 1) {
    		int i = lastGuess.row;
			int j = lastGuess.column;
			//targeting cells in order of bottom, left, up, right
    		Guess targetGuess1 = new Guess();
    		Guess targetGuess2 = new Guess();
    		Guess targetGuess3 = new Guess();
    		Guess targetGuess4 = new Guess();
    		//insert to list
    		targetGuess1.row = i;
    		targetGuess1.column = j - 1;
    		targetList.add(targetGuess1);
    		targetGuess2.row = i - 1;
    		targetGuess2.column = j;
    		targetList.add(targetGuess2);
    		targetGuess3.row = i;
    		targetGuess3.column = j + 1;
    		targetList.add(targetGuess3);
    		targetGuess4.row = i + 1;
    		targetGuess4.column = j;
    		targetList.add(targetGuess4);
    		for(int k=1;k<targetList.size();k++) {
    			if(this.isIn(targetList.get(k),0)) {
    				if (!isguessed[targetList.get(k).row][targetList.get(k).column]){
    					thisGuess = targetList.get(k);
    					
    					return thisGuess;
    				}	
    			}
    			else{
					continue;
				}   			
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
    	if (answer.isHit) {

    		mode=1;
    		targetList.clear();
    		if(answer.shipSunk !=null) {
    			System.out.println("Change to Hunting Mode");
    			mode = 0;
    		}
    	}
    	else {
    		if (mode == 1) {
    			Guess guess1 = new Guess();
    			guess1.row = targetList.get(targetList.size()-1).row;
    			guess1.column = targetList.get(targetList.size()-1).column;
    			Guess guess2 = new Guess();
    			guess2.row = targetList.get(targetList.size()-2).row;
    			guess2.column = targetList.get(targetList.size()-2).column;
    			if (isIn(guess1,0) && isguessed[targetList.get(targetList.size()-1).row][targetList.get(targetList.size()-1).column]) {
    				targetList.clear();
    				mode = 0;
    			} else if (!isIn(guess1,0) && isguessed[targetList.get(targetList.size()-2).row][targetList.get(targetList.size()-2).column]) {
    				targetList.clear();
    				mode = 0;
    			} else if (!isIn(guess1,0) && !isIn(guess1,0) && isguessed[targetList.get(targetList.size()-3).row][targetList.get(targetList.size()-3).column]) {
    				targetList.clear();
    				mode = 0;
    			}
    			else {
    				lastGuess = targetList.get(0);

    			}
    		}
    	}
    } // end of update()


    @Override
    public boolean noRemainingShips() {
        // To be implemented.
    	for (int i = 0; i < ownShips.size(); i++) {
    		for (int j = 0; j < ownShips.get(i).ship.len() * ownShips.get(i).ship.width(); j++) {
    			if (!ownShips.get(i).isdown.get(j)) {
    				return false;
    	        }
    		}
    	}
    	return true;	
    } // end of noRemainingShips()

} // end of class ProbabilisticGuessPlayer


