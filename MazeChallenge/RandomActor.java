import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;

public class RandomActor extends GenericActor
{ 
	static final int MAX_VISITED_STEPS = 20;
	
    public RandomActor(GenericMazeInputParser inputMaze) throws Exception
	{
		super(inputMaze);
		//the following validations are actor specific
		if (hasMoreThanOneGoals() || hasMoreThanOneStarts())
		{
			throw new Exception("Random actor cannot proceed with maze that has more than one Goals or Starts");
		}
		if (mazeToPlay.startAndEndCoincide())
		{
			throw new Exception("Random actor cannot play with Start and End that coincide");			
		}
		if (mazeToPlay.getMazeWidth() == 1 || mazeToPlay.getMazeHeight() == 1)
		{
			throw new Exception("Random actor cannot play in one dimmension maze");					
		}
	}
	
	public void runRandomPathFinding()
	{
		while (true)
		{
			ArrayList<int[]> availiablePositions = new ArrayList<int[]>(); //list that will hold the valid steps for every move
			
			//assign to local variables in order not to call getters many times
			int currPosX = getCurrentPosX();			
			int currPosY = getCurrentPosY();
			int prevPosX = getPreviousPosX();
			int prevPosY = getPreviousPosY();
			
			//get all coincide cells event the invalid ones
			int[] north = getNorth();
			int[] south = getSouth();
			int[] east  = getEast();
			int[] west  = getWest();			
			
			GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "*******************************************************************");
			GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "----------------------------------------------------------");				
			printActorCurrentMaze(); //print current state of actors maze		
			GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "----------------------------------------------------------");							
			GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "Coincide cells: North=" + Arrays.toString(north) + ", South=" + Arrays.toString(south) +  ", East=" + Arrays.toString(east) +   ", West=" + Arrays.toString(west));				
			
			//Add to a list all availiable valid moves (not a wall and not our of boundries) excluding the previous step also
			//if it is valid then check if it is a goal
			if (mazeToPlay.isValidToMove(north[0],north[1]) && !(north[0] == prevPosX && north[1] == prevPosY))
			{
				GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "Valid North");
				if (mazeToPlay.isGoalCell(north[0],north[1]))
				{
					setSolutionFound(true);
					addStep(north[0], north[1]);
					return;
				}				
				availiablePositions.add(north);
			}
			if (mazeToPlay.isValidToMove(south[0],south[1]) && !(south[0] == prevPosX && south[1] == prevPosY))
			{
				GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "Valid South");
				if (mazeToPlay.isGoalCell(south[0],south[1]))
				{
					setSolutionFound(true);
					addStep(south[0], south[1]);
					return;
				}
				availiablePositions.add(south);
			}				
			if (mazeToPlay.isValidToMove(east[0],east[1]) && !(east[0] == prevPosX && east[1] == prevPosY))
			{
				GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "Valid East");
				if (mazeToPlay.isGoalCell(east[0],east[1]))
				{
					setSolutionFound(true);
					addStep(east[0], east[1]);
					return;
				}
				availiablePositions.add(east);
			}				
			if (mazeToPlay.isValidToMove(west[0],west[1]) && !(west[0] == prevPosX && west[1] == prevPosY))
			{
				GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "Valid West");
				if (mazeToPlay.isGoalCell(west[0],west[1]))
				{
					setSolutionFound(true);
					addStep(west[0], west[1]);
					return;
				}
				availiablePositions.add(west);
			}	
			
			//if there is no availiable moves go to the previous step
			if (availiablePositions.size() == 0)
			{
				//this condition covers the case that the actor start in a position that has nowhere to go
				if (prevPosX == currPosX && prevPosY == currPosY)
				{
					GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "I have no move to perform!");
					return;
				}
				
				GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "No valid moves, i will just go back!");
				//exchange the values previous to current and current to previous
				setCurrentPos(prevPosX, prevPosY);
			    setPreviousPos(currPosX, currPosY);
				mazeToPlay.incrementCellValue(getCurrentPosX(), getCurrentPosY());//increment the count of the visited times in the maze
				addStep(getCurrentPosX(),  getCurrentPosY());//add to the solution the new current pos
				
				if (mazeToPlay.getMazeCellValue(getCurrentPosX(), getCurrentPosY()) > MAX_VISITED_STEPS)//check also if the previous cell has been visited more than MAX_VISISTED_STEPS
				{
					GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "Ahhhhhh I am lost!!!!!I will never find the theater!!!!");
					return;
				}				
				
				printPath();
				
				continue;
			}
			
			//produce random number only in cases the availiable moves are more than one, otherwise go straight to the ONE!
			int[] choiceStep;
			if (availiablePositions.size() > 1)
			{
				//get a random step out of the list of availiable moves
				int randomChoiceNum = new Random().nextInt(availiablePositions.size());
				choiceStep = availiablePositions.get(randomChoiceNum);
				GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "Number of availiable steps:" + availiablePositions.size() + ", Random Number:" + randomChoiceNum + ", Selected:" + Arrays.toString(choiceStep));
			}
			else
			{
				choiceStep = availiablePositions.get(0);
				GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "One Possible decision:" + Arrays.toString(choiceStep));
			}
			
			//we decided where to move. lets set the positions (current and previous)
			setPreviousPos(currPosX, currPosY);
			setCurrentPos(choiceStep[0], choiceStep[1]);
			mazeToPlay.incrementCellValue(choiceStep[0], choiceStep[1]); //increment the count of the visited times in the maze
			addStep(choiceStep[0], choiceStep[1]); //add to the solution the step that was selected to move

			if (mazeToPlay.getMazeCellValue(choiceStep[0], choiceStep[1]) > MAX_VISITED_STEPS)//getaway condition. In case a cell is being visited more than 20 time we stop searching
			{
				GenericLog.log(Level.INFO, "RandomActor.runRandomPathFinding", "Ahhhhhh I am lost!!!!!I will never find the theater!!!!");
				return;
			}
			
			printPath();
		}
	}	
}