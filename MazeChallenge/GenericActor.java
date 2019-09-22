import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class GenericActor 
{ 
	protected int currentPosX;
	protected int currentPosY;
	protected int previousPosX;
	protected int previousPosY;	
	protected boolean solutionFound;
	protected ArrayList<int[]> path = new ArrayList<int[]>();
	protected GenericMaze mazeToPlay;
	protected Map<String, Integer> inputMazeCellDistrib;
		
	protected GenericActor(GenericMazeInputParser inputMaze)
	{
		this.mazeToPlay = new GenericMaze(inputMaze.transformMaze()); 
		this.inputMazeCellDistrib = inputMaze.transformMazePatternHash();//we need to keep this object in order to perform validations about how many starts or goals exists in the maze.We wont validate in this constructor because any actor might have its own rules to play i.e. two goals might be valid or two start and picking one random might be too.Currently i am not aware about a generic validation

		//initialize starting position
		this.currentPosX = mazeToPlay.startPosX; 
		this.currentPosY = mazeToPlay.startPosY;
		addStep(currentPosX, currentPosY); //starting position is part of the path
		this.mazeToPlay.mazeMap[currentPosX][currentPosY] = 1;//start point sets automatically 1 since start is represented by -1	
		this.solutionFound = false;
	}
		
	//I might should have declared cells as an object too, but i thought that it would be an overkill.
	//get north coordinates
	protected int[] getNorth()
	{
		return new int[] {this.currentPosX-1, this.currentPosY};
	}
	
	//get south coordinates
	protected int[] getSouth()
	{
		return new int[] {this.currentPosX+1,this.currentPosY};
	}	
	
	//get east coordinates
	protected int[] getEast()
	{
		return new int[] {this.currentPosX, this.currentPosY+1};
	}	
	
	//get west coordinates
	protected int[] getWest()
	{
		return new int[] {this.currentPosX, this.currentPosY-1};
	}		
	
	//add step to variable that keeps the moves the actor have done
	protected void addStep(int x, int y)
	{
		int[] step = {x,y};
		path.add(step);
	}

	//validates if the maze has more than one goals
	protected boolean hasMoreThanOneGoals()
	{
		String defaultEnd = GenericConfigParser.confGetProperty("maze.end");
		int numberOfGoals = inputMazeCellDistrib.get(defaultEnd);
		if (numberOfGoals > 1)
		{
			return true;
		}
		
		return false;	
	}
	
	//validates if the maze has more than one starts
	protected boolean hasMoreThanOneStarts()
	{
		String defaultStart = GenericConfigParser.confGetProperty("maze.start");
		int numberOfStarts = inputMazeCellDistrib.get(defaultStart);
		if (numberOfStarts > 1)
		{
			return true;
		}
		
		return false;	
	}	
	
	//print IntegerMaze with the current position, in a more "user friendly" view
	public void printActorCurrentMaze()
	{
		for (int i = 0; i < this.mazeToPlay.mazeMap.length; i++)
		{
			String tmpStr = "";
			for (int j = 0; j < this.mazeToPlay.mazeMap[i].length; j++)
			{
				 //fixing a bit the layout of the output due to negative values.
				 
				if (currentPosX == i && currentPosY == j)
				{
					tmpStr =  tmpStr + " $   ";
					continue;
				}
				else if (previousPosX == i && previousPosY == j)
				{
					tmpStr =  tmpStr + " P   ";
					continue;
				}			
				else if (this.mazeToPlay.mazeMap[i][j] == this.mazeToPlay.WALL_INT_INDICATOR)
				{
					tmpStr =  tmpStr + " " + this.mazeToPlay.WALL_STRING_INDICATOR +"   ";
				}
				else if (this.mazeToPlay.mazeMap[i][j] == this.mazeToPlay.GOAL_INT_INDICATOR)
				{
					tmpStr =  tmpStr + " " + this.mazeToPlay.GOAL_STRING_INDICATOR +"   ";
				}				
				else if (this.mazeToPlay.mazeMap[i][j]>=0)
				{
					tmpStr =  tmpStr + (" " + this.mazeToPlay.mazeMap[i][j] + "   ").substring(0,5);
				}
				else
				{
					tmpStr =  tmpStr + (this.mazeToPlay.mazeMap[i][j] + "    ").substring(0,5);
				}
			}

			GenericLog.log(Level.INFO, "GenericActor.printActorCurrentMaze", tmpStr);
		}		
	}	
	
	//prints the path the actor done at the moment called
	public void printPath()
	{
		int pathSize = path.size();
		int stepCounter = 0;
		String tmpStr = "";
		
		for(int[] step: path) 
		{
			tmpStr = tmpStr + Arrays.toString(step);
			if(stepCounter < pathSize-1)
			{
				tmpStr = tmpStr + "-->";
			}
			stepCounter++;
        }

		GenericLog.log(Level.INFO, "GenericActor.printPath", tmpStr);
	}	
	
}
