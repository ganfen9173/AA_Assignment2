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

	int rowSize = 0;
	int colSize = 0;
	
	List<Guess> targetList = new ArrayList<Guess>();
	
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
	
    boolean isIn(Guess guess) {
      
            return guess.row >= 0 && guess.row < rowSize && guess.column >= 0 && guess.column < colSize;
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
    				System.out.println(ownShips.get(i).isdown);
    				
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
        // To be implemented.
 	
    	Guess thisGuess = new Guess();    	
    	//Hunting Mode  mode == 0
    	if (mode == 0) {
    		if (turn == 0) {
    			thisGuess.row = 0;
    			thisGuess.column = 0;
    			turn++;
    			return thisGuess;
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
    			    	System.out.println(lastGuess + " 1");
    				   	return thisGuess;   				   	
    				}
    				else {
    					if((i+1) % 2 == 0){
        					j = 0;
        					
        				}
        				if ((i+1) %2 == 1) {
        					j = 1;
        				}
        				thisGuess.row = i+1;
        				thisGuess.column =j;
        				if (!isguessed[thisGuess.row][thisGuess.column]) {
        					return thisGuess;
        				} else {
	        				while(this.isIn(thisGuess)&&isguessed[thisGuess.row][thisGuess.column]) {
	        					Random random = new Random();
	        					while (this.isguessed[i][j]){
	        				        i = random.nextInt(this.rowSize);
	        				        j = random.nextInt(this.colSize);
	        			    	}   	
	        				 	thisGuess.row = i;
	        				   	thisGuess.column = j;
	        			    	System.out.println(lastGuess + " 1");
	        				   	return thisGuess;   	
	        				}
        				}
    				}
    			
    			}else{
    				if(!isguessed[i][j+2]&&j+2<this.colSize&&i<this.rowSize) {
    					thisGuess.row = i;
    					thisGuess.column = j+2;
    					
    					return thisGuess;
    					}
    				else {
    					Random random = new Random();
    					do {
    				        i = random.nextInt(this.rowSize);
    				        j = random.nextInt(this.colSize);
    			    	} while (this.isguessed[i][j]);
    				 	thisGuess.row = i;
    				   	thisGuess.column = j;
    				 
    				   	return thisGuess;
    				   	
    				}
    			} 			
    		}
    		
    	}
    	
    	//Targeting Mode mode == 1
    	if (mode == 1) {
    		if (targetList.isEmpty()){
    			targetList.add(lastGuess);
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
    		}
    		for(int k=1;k<targetList.size();k++) {
    			if(this.isIn(targetList.get(k))) {
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
    	turn++;

    	return lastGuess;
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
    	isguessed[guess.row][guess.column] = true;
    	lastGuess = guess;
    	if (answer.isHit) {
    		mode = 1;
    		targetList.clear();
    		if (answer.shipSunk != null) {
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
    			if (isIn(guess1) && isguessed[targetList.get(targetList.size()-1).row][targetList.get(targetList.size()-1).column]) {
    				targetList.clear();
    				mode = 0;
    			} else if (!isIn(guess1) && isguessed[targetList.get(targetList.size()-2).row][targetList.get(targetList.size()-2).column]) {
    				targetList.clear();
    				mode = 0;
    			} else if (!isIn(guess1) && !isIn(guess1) && isguessed[targetList.get(targetList.size()-3).row][targetList.get(targetList.size()-3).column]) {
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


} // end of class GreedyGuessPlayer
