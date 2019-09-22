import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;

public class RankingActor extends GenericActor
{ 
	static final int MAX_VISITED_STEPS = 20;
	
    public RankingActor(GenericMazeInputParser inputMaze) throws Exception
	{
		super(inputMaze);
		
		//prepare validations below
		//we assume that one start and one goal is the valid meze
		if (hasMoreThanOneGoals() || hasMoreThanOneStarts())
		{
			throw new Exception("Ranking actor cannot proceed with maze that has more than one Goals or Starts");
		}
		if (mazeToPlay.startAndEndCoincide())
		{
			throw new Exception("Ranking actor cannot play with Start and End that coincide");			
		}
		if (mazeToPlay.mazeWidth == 1 || mazeToPlay.mazeHeight == 1)
		{
			throw new Exception("Ranking actor cannot play in one dimmension maze");					
		}		
		
		//we did not initiliaze to the GenericActor class because it depends on the aglorithm when to initiliaze
		//in this algorithm this will be setted on the firs loop also with the same values (i.e. if the algorithm finds this values null the it might want to do something specific)
		previousPosX = mazeToPlay.startPosX;
		previousPosY = mazeToPlay.startPosY;
	}
	
	protected void rankingPathFinding()
	{
		while (true)
		{
			ArrayList<int[]> availiablePositions = new ArrayList<int[]>(); //list that will hold the valid steps for every move
			
			GenericLog.log(Level.INFO, "RankingActor.runRandomPathFinding", "*******************************************************************");
			GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "----------------------------------------------------------");				
			printActorCurrentMaze(); //print current state of actors maze		
			GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "----------------------------------------------------------");				
			
			//get all coincide cells even the invalid ones
			int[] north = getNorth();
			int[] south = getSouth();
			int[] east  = getEast();
			int[] west  = getWest();
			
			GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "Coincide cells: North=" + Arrays.toString(north) + ", South=" + Arrays.toString(south) +  ", East=" + Arrays.toString(east) +   ", West=" + Arrays.toString(west));	
			
			//Add to a list all availiable valid moves (not a wall and not our of boundries) excluding the previous step also
			//if it is valid then check if it is a goal
			if (mazeToPlay.isValidToMove(north[0],north[1]) && !(north[0] == previousPosX && north[1] == previousPosY))
			{
				GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "Valid North");
				if (mazeToPlay.isGoalCell(north[0],north[1]))
				{
					solutionFound = true;
					addStep(north[0], north[1]);
					return;
				}				
				availiablePositions.add(north);
			}
			if (mazeToPlay.isValidToMove(south[0],south[1]) && !(south[0] == previousPosX && south[1] == previousPosY))
			{
				GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "Valid South");
				if (mazeToPlay.isGoalCell(south[0],south[1]))
				{
					solutionFound = true;
					addStep(south[0], south[1]);
					return;
				}
				availiablePositions.add(south);
			}				
			if (mazeToPlay.isValidToMove(east[0],east[1]) && !(east[0] == previousPosX && east[1] == previousPosY))
			{
				GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "Valid East");
				if (mazeToPlay.isGoalCell(east[0],east[1]))
				{
					solutionFound = true;
					addStep(east[0], east[1]);
					return;
				}
				availiablePositions.add(east);
			}				
			if (mazeToPlay.isValidToMove(west[0],west[1]) && !(west[0] == previousPosX && west[1] == previousPosY))
			{
				GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "Valid West");
				if (mazeToPlay.isGoalCell(west[0],west[1]))
				{
					solutionFound = true;
					addStep(west[0], west[1]);
					return;
				}
				availiablePositions.add(west);
			}	
			
			//if there is no availiable moves go to the previous step
			if (availiablePositions.size() == 0)
			{
				//this condition covers the case that the actor start in a position that has nowhere to go
				if (previousPosX == currentPosX && previousPosY == currentPosY)
				{
					GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "I have no move to perform!");
					return;
				}
				
				//use temporary variable to exchange values between curent and previous position
				GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "No valid moves, i will just go back!");
				int tmpCurrentPosX = currentPosX;
				int tmpCurrentPosY = currentPosY;
				currentPosX = previousPosX;
				currentPosY = previousPosY;
				previousPosX = tmpCurrentPosX;
				previousPosY = tmpCurrentPosY;
				mazeToPlay.mazeMap[currentPosX][currentPosY]++; //increment the count of the visited times in the maze
				addStep(currentPosX, currentPosY);
				
				printPath();
				
				continue;
			}
			
			//parse all valid options and rank them
			//we move to the position with the highest rank
			double maxRank = 0; 
			int bestPostX  = 0;
			int bestPostY  = 0;
			
			//in this way, in case of new paths (never visited cells, will probably end up with same ranking), the priority of selecting a cell is north,south,east,west.
			//It would be nice to put here some random functionality
			if (availiablePositions.size()>1)//proceed to ranking in case there are more than one availiable steps
			{	
				for(int[] possiblePosition : availiablePositions) 
				{
					double currentRank = calculateRank(possiblePosition[0],possiblePosition[1]); 

					GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "Position:" + Arrays.toString(possiblePosition) + ", Rank:" + currentRank);
					
					//position with maximum rank
					if (currentRank > maxRank)
					{
						maxRank = currentRank;
						bestPostX = possiblePosition[0];
						bestPostY = possiblePosition[1];
					}
				}
			}
			else
			{
				//in case of one availiable we do not need to rank
				GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "One Possible decision:" + Arrays.toString(availiablePositions.get(0)));
				bestPostX = availiablePositions.get(0)[0];
				bestPostY = availiablePositions.get(0)[1];
			}
			
			//we decided where to move. lets set the positions (current and previous)
			previousPosX = currentPosX;
			previousPosY = currentPosY;
			currentPosX = bestPostX;
			currentPosY = bestPostY;
			mazeToPlay.mazeMap[currentPosX][currentPosY]++; //increment the count of the visited times in the maze
			addStep(currentPosX, currentPosY); //add the step to the "solution"

			if (mazeToPlay.mazeMap[currentPosX][currentPosY] > MAX_VISITED_STEPS)//getaway condition, in case a cell has been visited more than 20 times we stop searching
			{
				GenericLog.log(Level.INFO, "RankingActor.rankingPathFinding", "Ahhhhhh I am lost!!!!!I will never find the theater!!!!");
				return;
			}
			
			printPath();
		}
	}
	
	protected double calculateRank(int x, int y)
	{
		int stepsForGoal = calculateDistance (x, y);

		//my function of ranking. 
		//first:since the more steps to the goal(direct distance), the worse ranking, we try to create a metric compared to a constant(the constant is the maximum distance that the actor can have in the maze start position (0,0) and goal position (height-1,width-1). The weight of tihs metric to the outcome will be 5 out of 10.
		//second: we do the same think with visited cells, the more visisted cells, the worse may be the path. We compare it with the maximum visits in a cell (currently 20). The weight of tihs metric to the outcome will be 5 out of 10.
		return ((5*((double)(mazeToPlay.mazeWidth+mazeToPlay.mazeHeight-1)-stepsForGoal))/(double)(mazeToPlay.mazeWidth+mazeToPlay.mazeHeight-1)) + ((5*(double)(MAX_VISITED_STEPS - mazeToPlay.mazeMap[x][y]))/(double)MAX_VISITED_STEPS);		
	}
	
	//calculates distance from the given position to the goal
	protected int calculateDistance (int x, int y)
	{
		return Math.abs(x - mazeToPlay.goalPosX) + Math.abs(y - mazeToPlay.goalPosY) ;
	}
}