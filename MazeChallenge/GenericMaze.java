public class GenericMaze 
{ 
	//Constants Start
	static final int EMPTY_INT_INDICATOR = 0;
	static final int START_INT_INDICATOR = -1;
	static final int WALL_INT_INDICATOR = -2;
	static final int GOAL_INT_INDICATOR = -3;
	
	static final String EMPTY_STRING_INDICATOR = GenericConfigParser.confGetProperty("maze.empty");
	static final String START_STRING_INDICATOR = GenericConfigParser.confGetProperty("maze.start");
	static final String WALL_STRING_INDICATOR = GenericConfigParser.confGetProperty("maze.wall");
	static final String GOAL_STRING_INDICATOR = GenericConfigParser.confGetProperty("maze.end");
	//Constants End

	private int startPosX; //start position on X AXIS of the maze table
	private int startPosY; //start position on Y AXIS of the maze table
	private int goalPosX; //end position on X AXIS of the maze table
	private int goalPosY; //end position on Y AXIS of the maze table
	private int mazeWidth; //Contains maze width. used for validation or moving
	private int mazeHeight; //Contains maze height. used for validation or moving
	private static int[][] mazeMap; 
	
	public static void main(String[] args) 
	{
		String[][] testArray = {
						{"_","_","_","_","G","_","_","X"},
						{"_","_","_","X","X","X","_","_"},
						{"X","_","_","_","_","_","_","X"},
						{"_","_","X","X","X","X","_","_"},
						{"_","_","_","X","_","_","_","_"},
						{"_","_","S","_","_","X","_","_"}
		};
		GenericMaze mazeObj = new GenericMaze(testArray);
		mazeObj.printStringMaze();
		//mazeObj.printIntMaze();
		//isGoalCell method Start Testing
		System.out.print("isGoalCell (0,0):" + mazeObj.mazeMap[0][0] + "=" + mazeObj.isGoalCell(0,0) + ", expected false");
		System.out.println();
		System.out.print("isGoalCell (0,4):" + mazeObj.mazeMap[0][4] + "="+mazeObj.isGoalCell(0,4)  + ", expected true");
		System.out.println();
		System.out.print("isGoalCell (0,7):" + mazeObj.mazeMap[0][7] + "=" + mazeObj.isGoalCell(0,7) + ", expected false");
		System.out.println();
		System.out.print("isGoalCell (5,2):" + mazeObj.mazeMap[5][2] + "=" + mazeObj.isGoalCell (5,2) + ", expected false");
		System.out.println();
		//isGoalCell method End Testing
		
		//isWallCell method Start Testing
		System.out.print("isWallCell (0,0):" + mazeObj.mazeMap[0][0] + "=" + mazeObj.isWallCell(0,0) + ", expected false");
		System.out.println();
		System.out.print("isWallCell (0,4):" + mazeObj.mazeMap[0][4]+"=" + mazeObj.isWallCell(0,4) + ", expected false");
		System.out.println();
		System.out.print("isWallCell (0,7):" + mazeObj.mazeMap[0][7]+"=" + mazeObj.isWallCell(0,7) + ", expected true");
		System.out.println();
		System.out.print("isWallCell (5,2):" + mazeObj.mazeMap[5][2]+"=" + mazeObj.isWallCell(5,2) + ", expected false");
		System.out.println();
		//isWallCell method End Testing
		
		

		//isOutOfBounds method Start Testing
		System.out.print("isOutOfBounds (0,0):" + mazeObj.mazeMap[0][0] + "=" + mazeObj.isOutOfBounds(0,0) + ", expected false");
		System.out.println();
		System.out.print("isOutOfBounds (0,4):"+mazeObj.mazeMap[0][4] + "=" + mazeObj.isOutOfBounds(0,4) + ", expected false");
		System.out.println();
		System.out.print("isOutOfBounds (0,7):" + mazeObj.mazeMap[0][7] + "=" + mazeObj.isOutOfBounds(0,7) + ", expected false");
		System.out.println();
		System.out.print("isOutOfBounds (5,2):" + mazeObj.mazeMap[5][2] + "=" + mazeObj.isOutOfBounds(5,2) + ", expected false");
		System.out.println();
		System.out.print("isOutOfBounds (-1,0):" + mazeObj.isOutOfBounds(-1,0) + ", expected true");
		System.out.println();
		System.out.print("isOutOfBounds (0,10):" + mazeObj.isOutOfBounds(0,8) + ", expected true");
		System.out.println();
		System.out.print("isOutOfBounds (10,0):" + mazeObj.isOutOfBounds(6,0) + ", expected true");
		System.out.println();
		System.out.print("isOutOfBounds (10,10):" + mazeObj.isOutOfBounds(10,10) + ", expected true");
		System.out.println();	
		System.out.print("isOutOfBounds (-1,-1):" + mazeObj.isOutOfBounds(-1,-1) + ", expected true");
		System.out.println();	
		//isOutOfBounds method End Testing
		
		//isValidToMove method Start Testing 
		System.out.print("isValidToMove (0,0):" + mazeObj.mazeMap[0][0] + "=" + mazeObj.isValidToMove(0,0) + ", expected true");
		System.out.println();
		System.out.print("isValidToMove (0,4):" + mazeObj.mazeMap[0][4] + "=" + mazeObj.isValidToMove(0,4) + ", expected true");
		System.out.println();
		System.out.print("isValidToMove (0,7):" + mazeObj.mazeMap[0][7] + "=" + mazeObj.isValidToMove(0,7) + ", expected false");
		System.out.println();
		System.out.print("isValidToMove (5,2):" + mazeObj.mazeMap[5][2] + "=" + mazeObj.isValidToMove(5,2) + ", expected true");
		System.out.println();
		System.out.print("isValidToMove (-1,0):" + mazeObj.isValidToMove(-1,0) + ", expected false");
		System.out.println();
		System.out.print("isValidToMove (0,10):" + mazeObj.isValidToMove(0,10) + ", expected false");
		System.out.println();
		System.out.print("isValidToMove (10,0):" + mazeObj.isValidToMove(10,0) + ", expected false");
		System.out.println();
		System.out.print("isValidToMove (10,10):" + mazeObj.isValidToMove(10,10) + ", expected false");
		System.out.println();	
		System.out.print("isValidToMove (-1,-1):" + mazeObj.isValidToMove(-1,-1) + ", expected false");
		System.out.println();			
		//isValidToMove method End Testing 
		
	}
	
	//Constructor of the maze
	public GenericMaze (String[][] inputMazeMap)
	{ 
		this.mazeWidth = inputMazeMap[0].length;
		this.mazeHeight = inputMazeMap.length;
		this.mazeMap = new int [this.mazeHeight][this.mazeWidth];
		
		//start initiliaze maze array and start,end position 
		for (int i = 0; i < inputMazeMap.length; i++)
		{
			for (int j = 0; (inputMazeMap[i] != null && j < inputMazeMap[i].length); j++)
			{
				//we transform the map into a new one, represented with integers in order to support counter on visited cells
				if (EMPTY_STRING_INDICATOR.equals(inputMazeMap[i][j])) 
				{
					this.mazeMap[i][j] = EMPTY_INT_INDICATOR;  
				}
				else if (WALL_STRING_INDICATOR.equals(inputMazeMap[i][j]))
				{
					this.mazeMap[i][j] = WALL_INT_INDICATOR;
				}
				else if (START_STRING_INDICATOR.equals(inputMazeMap[i][j]))
				{
					this.mazeMap[i][j] = START_INT_INDICATOR;
					this.startPosX = i;
					this.startPosY = j;		
				}				
				else if (GOAL_STRING_INDICATOR.equals(inputMazeMap[i][j]))
				{
					this.mazeMap[i][j] = GOAL_INT_INDICATOR;
					this.goalPosX = i;
					this.goalPosY = j;
				}
			}
		}	
		//end initiliaze maze array and start,end position 	
	}

	//setter of startPosX
	private void setStartPosX(int xPos)
	{
		this.startPosX = xPos;
	}
	
	//getter of startPosX
	public int getStartPosX()
	{
		return this.startPosX;
	}	
	
	//setter of startPosY
	private void setStartPosY(int yPos)
	{
		this.startPosY = yPos;
	}
	
	//getter of startPosY
	public int getStartPosY()
	{
		return this.startPosY;
	}		
	
	//setter of goalPosX
	private void setGoalPosX(int xPos)
	{
		this.goalPosX = xPos;
	}
	
	//getter of goalPosX
	public int getGoalPosX()
	{
		return this.goalPosX;
	}		
	
	//setter of goalPosY
	private void setGoalPosY(int yPos)
	{
		this.goalPosY = yPos;
	}
	
	//getter of goalPosY
	public int getGoalPosY()
	{
		return this.goalPosY;
	}		

	//function for setting goal position coordinates
	private void setGoalPos(int xPos, int yPos)
	{
		this.goalPosX = xPos;
		this.goalPosY = yPos;
	}
	
	//getter of mazeWidth
	public int getMazeWidth()
	{
		return this.mazeWidth;
	}	

	//getter of mazeHeight
	public int getMazeHeight()
	{
		return this.mazeHeight;
	}	
	
	//sets the value of a scpecific cell of the maze
	public int getMazeCellValue(int x, int y)
	{
		return this.mazeMap[x][y];
	}	
	
	//sets the value of a scpecific cell of the maze
	public void setMazeCellValue(int x, int y, int val)
	{
		this.mazeMap[x][y] = val;
	}
	
	//increment a cell value by one
	public void incrementCellValue(int x, int y)
	{
		this.mazeMap[x][y]++;
	}	
	
	//sets the array dimmensions
	private void setArrayDimmensions(int width, int height)
	{
		this.mazeWidth = width;
		this.mazeHeight = height;
	}
	
	//determines if a position in the maze is the Goal Cell
	public boolean isGoalCell(int xPos, int yPos)
	{
		if (this.mazeMap[xPos][yPos] == GOAL_INT_INDICATOR)
		{
			return true;
		}
		
		return false;
	}
	
	//determines if a given position is wall. You cannot move in that cell
	private boolean isWallCell(int xPos, int yPos)
	{
		if (this.mazeMap[xPos][yPos] == WALL_INT_INDICATOR)
		{
			return true;
		}
		
		return false;
	}	
	
	//determines if a given position is out of array bounds . You cannot move in that cell
	private boolean isOutOfBounds(int xPos, int yPos)
	{
		if ( yPos > this.mazeWidth-1 || xPos > this.mazeHeight-1 || xPos < 0 || yPos < 0)
		{
			return true;
		}
		
		return false;
	}

	//combine more than one validations in order to define if a cell is eligible to move
	public boolean isValidToMove(int xPos, int yPos)
	{
		if (!isOutOfBounds(xPos, yPos) && !isWallCell(xPos, yPos)) //we need to put this in this order. Otherwise we get OutOfBoundsException
		{
			return true;
		}
		
		return false;
	}
	
	//check if starting and end position coincide
	public boolean startAndEndCoincide()
	{
		if ((this.startPosX == this.goalPosX+1 && this.startPosY == this.goalPosY) || (this.startPosX == this.goalPosX-1 && this.startPosY == this.goalPosY) || (this.startPosX == this.goalPosX && this.startPosY == this.goalPosY+1) || (this.startPosX == this.goalPosX && this.startPosY == this.goalPosY-1))
		{
			return true;
		}
		return false;
	}	
	
	//printing Integer maze, for class test purposes
	public void printIntMaze()
	{
		for (int i = 0; i < this.mazeMap.length; i++)
		{
			for (int j = 0; j < this.mazeMap[i].length; j++)
			{
				 //fixing a bit the layout of the output due to negative values.
				 //better layout is supported for number <1000
				if (this.mazeMap[i][j]>=0)
				{
					System.out.print((" "+this.mazeMap[i][j] + "   ").substring(0,5));
				}
				else
				{
					System.out.print((this.mazeMap[i][j] + "    ").substring(0,5));
				}
			}

			System.out.println();
		}		
	}
	
	//print maze in "user friendly" mode, for class test purposes
	public void printStringMaze()
	{
		for (int i = 0; i < this.mazeMap.length; i++)
		{
			for (int j = 0; j < this.mazeMap[i].length; j++)
			{
				if (this.mazeMap[i][j] == EMPTY_INT_INDICATOR) 
				{
					System.out.print(EMPTY_STRING_INDICATOR);
				}
				else if (this.mazeMap[i][j] == WALL_INT_INDICATOR)
				{
					System.out.print(WALL_STRING_INDICATOR);
				}
				else if (this.mazeMap[i][j] == START_INT_INDICATOR)
				{
					System.out.print(START_STRING_INDICATOR);
				}				
				else if (this.mazeMap[i][j] == GOAL_INT_INDICATOR)
				{
					System.out.print(GOAL_STRING_INDICATOR);
				}	
			}

			System.out.println();
		}		
	}	
}