import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class GenericActor 
{ 
	private int currentPosX;
	private int currentPosY;
	private int previousPosX;
	private int previousPosY;	
	private boolean solutionFound;
	private ArrayList<int[]> path = new ArrayList<int[]>();
	protected GenericMaze mazeToPlay;
	private Map<String, Integer> inputMazeCellDistrib;
		
	protected GenericActor(GenericMazeInputParser inputMaze)
	{
		this.mazeToPlay = new GenericMaze(inputMaze.transformMaze()); 
		this.inputMazeCellDistrib = inputMaze.transformMazePatternHash();//we need to keep this object in order to perform validations about how many starts or goals exists in the maze.We wont validate in this constructor because any actor might have its own rules to play i.e. two goals might be valid or two start and picking one random might be too.Currently i am not aware about a generic validation

		//initialize starting position
		this.currentPosX = mazeToPlay.getStartPosX(); 
		this.currentPosY = mazeToPlay.getStartPosY();
		//at star current position and previous position will be the same
		this.previousPosX = mazeToPlay.getStartPosX(); 
		this.previousPosY = mazeToPlay.getStartPosY();
		
		this.addStep(currentPosX, currentPosY); //starting position is part of the path
		this.mazeToPlay.setMazeCellValue(currentPosX, currentPosY, 1); //start point sets automatically 1 since start is represented by -1	
		this.solutionFound = false;
	}
	
	//setter of currentPosX
	private void setCurrentPosX(int xPos)
	{	
		this.currentPosX = xPos;
	}
	
	//getter of currentPosX
	protected int getCurrentPosX()
	{	
		return this.currentPosX;
	}
	
	//setter of currentPosY
	private void setCurrentPosY(int yPos)
	{	
		this.currentPosY = yPos;
	}
	
	//getter of currentPosY
	protected int getCurrentPosY()
	{	
		return this.currentPosY;
	}	
	
	//setter of the current position
	protected void setCurrentPos(int xPos, int yPos)
	{
		this.setCurrentPosX(xPos);
		this.setCurrentPosY(yPos);
	}
	
	//getter of previousPosX
	protected int getPreviousPosX()
	{
		return this.previousPosX;
	}	

	//setter of previousPosX
	private void setPreviousPosX(int xPos)
	{
		this.previousPosX = xPos;
	}		
	
	//getter of previousPosY
	protected int getPreviousPosY()
	{
		return this.previousPosY;
	}	
	
	//setter of previousPosY
	private void setPreviousPosY(int yPos)
	{
		this.previousPosY = yPos;
	}		
	
	//setter of the current position
	protected void setPreviousPos(int xPos, int yPos)
	{
		this.setPreviousPosX(xPos);
		this.setPreviousPosY(yPos);
	}			
	
	//setter of the solutionFound
	protected void setSolutionFound(boolean solutionFoundInput)
	{
		this.solutionFound = solutionFoundInput;
	}
	
	//setter of the solutionFound
	protected boolean getSolutionFound()
	{
		return this.solutionFound;
	}	
		
	//I might should have declared cells as  object, but i thought that it would be an overkill.
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
	protected void printActorCurrentMaze(String caller)
	{
		for (int i = 0; i < this.mazeToPlay.getMazeHeight(); i++)
		{
			String tmpStr = "";
			for (int j = 0; j < this.mazeToPlay.getMazeWidth(); j++)
			{
				 //fixing a bit the layout of the output due to negative values.
				 
				if (currentPosX == i && currentPosY == j)
				{
					tmpStr =  tmpStr + " $   ";
					continue;
				}
				else if (previousPosX == i && previousPosY == j)
				{
					tmpStr =  tmpStr + (" " + this.mazeToPlay.getMazeCellValue(i, j) + "P  ").substring(0,5);
					continue;
				}			
				else if (this.mazeToPlay.getMazeCellValue(i, j) == this.mazeToPlay.WALL_INT_INDICATOR)
				{
					tmpStr =  tmpStr + " " + this.mazeToPlay.WALL_STRING_INDICATOR +"   ";
				}
				else if (this.mazeToPlay.getMazeCellValue(i, j) == this.mazeToPlay.GOAL_INT_INDICATOR)
				{
					tmpStr =  tmpStr + " " + this.mazeToPlay.GOAL_STRING_INDICATOR +"   ";
				}				
				else if (this.mazeToPlay.getMazeCellValue(i, j)>=0)
				{
					tmpStr =  tmpStr + (" " + this.mazeToPlay.getMazeCellValue(i, j) + "   ").substring(0,5);
				}
				else
				{
					tmpStr =  tmpStr + (this.mazeToPlay.getMazeCellValue(i, j) + "    ").substring(0,5);
				}
			}

			GenericLog.log(Level.INFO, caller, tmpStr);
		}		
	}	
	
	//prints the path the actor done at the moment called
	protected void printPath(String caller)
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

		GenericLog.log(Level.INFO, caller, tmpStr);
	}	
}
