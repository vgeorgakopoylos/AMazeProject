import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.logging.Level;

public class GenericMazeInputParser 
{ 
	
	private String[][] parsedMaze; //two dimensional maze after parsed the initial input arraylist maze
	private ArrayList<String> mazeInvalidMessages = new ArrayList<String>(); //will contain the reasons why the maze might be invalid
	private boolean isValid = true; //is this maze valid?
	private boolean isRectangle = true;//is this maze rectangle?
	private boolean isValidPattern = true;//does this maze pattern is valid?
	private Map<String, Integer> mazePatternHash = new HashMap<>(); //we want to make a pattern of distinct characters exist in input map in order to support more than one input patterns in case need. Also we need to validate if the letters used are valid
	private String mazePattern; // maze pattern as shorted string whithout having the count of every character within the maze
	
	public static void main(String[] args) 
	{
		//testing
		ArrayList<String> testArray1 = new ArrayList<String>();
		testArray1.add("____G__X");
		testArray1.add("___XXX__");
		testArray1.add("X______X");
		testArray1.add("__XXXX__");
		testArray1.add("___XL___");
		testArray1.add("__S_XX__");	

		GenericMazeInputParser testObject1 = new GenericMazeInputParser(testArray1);
		GenericLog.printStringlist(Level.INFO, "GenericMazeInputParser.main", testObject1.mazeInvalidMessages);
		String[][] testArr2 = testObject1.transformMaze();
		GenericLog.printTwoDimArrString(Level.INFO, "GenericMazeInputParser.main", testArr2);
		Map<String, Integer>  hash1 = testObject1.transformMazePatternHash();
		GenericLog.printHashStrInt(Level.INFO, "GenericMazeInputParser.main", hash1);
		
		ArrayList<String> testArray2 = new ArrayList<String>();
		testArray2.add("OOOOEOOWW");
		testArray2.add("OOOWWWOOW");
		testArray2.add("WOOOOOOWW");
		testArray2.add("OOWWWWOOW");
		testArray2.add("OOOWOOOOW");
		testArray2.add("OOSOWWOOW");
		testArray2.add("OOOOWWOOW");
	
		GenericMazeInputParser testObject2 = new GenericMazeInputParser(testArray2);
		String[][] testArr1 = testObject2.transformMaze();
		GenericLog.printTwoDimArrString(Level.INFO, "GenericMazeInputParser.main", testArr1);
		Map<String, Integer>  hash2 = testObject2.transformMazePatternHash();
		GenericLog.printHashStrInt(Level.INFO, "GenericMazeInputParser.main", hash2);		
	}		

	//Constructor of GenericMazeInputParser
	public GenericMazeInputParser(ArrayList<String> inputMaze)
	{ 
		GenericLog.log(Level.INFO, "GenericMazeInputParser.Constructor", "Printing input maze");
		GenericLog.printStringlist(Level.INFO, "GenericMazeInputParser.Constructor", inputMaze);//print the input maze "straight" from the file
		
		int parsedMazeCounter = 0;
		int previousLength = -1;
		ArrayList<String> availPatterns;
		
		parsedMaze = new String[inputMaze.size()][inputMaze.get(0).length()]; //Initializing array that will transform the input arraylist maze to array
		
		//Parsing input maze arraylist in order to identify if the input is a valid maze.
		//Also determines the pattern of the input maze in order to validate if it is supported
		for (int i = 0; i < inputMaze.size() ; i++)
		{
			String[] tempArray = inputMaze.get(i).split("");// we use temp array in case the length is not equal with the other lines
			
			//we dont want to continue executing the following(within the if (this.isRectangle)), if the map has been identified as non rectangle.
			//also we want to prevent putting the same message in mazeInvalidMessages
			if (this.isRectangle) 
			{
				if(previousLength == -1 || (previousLength == tempArray.length && previousLength > 0))
				{
					previousLength = tempArray.length;
					parsedMaze[parsedMazeCounter] = tempArray;
				}
				else
				{
					this.setInvalidRectagnle();
					this.setInvalidMessage("-The input maze is not rectangle.");
				}
			}
			
			//parse every line and set distinct letters to hash map. Also count the number of occurances of the letters
			for (int j = 0; j < tempArray.length; j++)
			{
				setMazePatternHash(tempArray[j]);
			}
			parsedMazeCounter++;
		}
		
		setMazePattern();//setting the string maze pattern that will determine if the maze is valid. This function sets a string pattern that works as an identification string
		availPatterns = GenericConfigParser.GetAvailiablePatterns(); //get supported patterns from the configuration file
		
		GenericLog.log(Level.INFO, "GenericMazeInputParser.Constructor", "Printing supported patterns");
		GenericLog.printStringlist(Level.INFO, "GenericMazeInputParser.Constructor", availPatterns);
		GenericLog.log(Level.INFO, "GenericMazeInputParser.Constructor", "Current maze pattern:" + this.mazePattern);
		
		if (!availPatterns.contains(this.mazePattern))
		{
			setInvalidPattern();
			this.setInvalidMessage("-The input maze does not contain valid pattern:'"+this.mazePattern+"'.Availiable Patterns:"+availPatterns);
		}
	}
	
	//adds a reason, for the input array , of not being valid
	private void setInvalidMessage(String message)
	{
		this.mazeInvalidMessages.add(message);
	}
	
	//adds a reason, for the input array , of not being valid
	public ArrayList<String> getInvalidMessage()
	{
		return this.mazeInvalidMessages;
	}	
	
	//set the boolean attribute indicates if the input array is rectangle
	private void setisRectangle(boolean inputVal)
	{
		this.isRectangle = inputVal;
	}
	
	//set the boolean attribute indicates if the input array is valid overall
	private void setisValid(boolean inputVal)
	{
		this.isValid = inputVal;
	}
	
	//geter of isValid
	public boolean getisValid()
	{
		return this.isValid;
	}	
	
	//appends distinct letter (contained in the input maze) and stores their count
	private void setMazePatternHash(String inputVal)
	{
		if (this.mazePatternHash.containsKey(inputVal))
		{
			mazePatternHash.put(inputVal, mazePatternHash.get(inputVal)+1);
		}
		else
		{
			mazePatternHash.put(inputVal, 1);
		}
	}
	
	//if the input array is not rectangle then it is not valid overall
	private void setInvalidRectagnle()
	{
		this.isRectangle = false;
		this.isValid = false;
	}	
	
	//if the input array does nto have a valid pattern then it is not valid overall
	private void setInvalidPattern()
	{
		this.isValidPattern = false;
		this.isValid = false;
	}	
	
	//transforms the maze pattern hash (that contains the distinct letter and their counts) to a sorted string that it is actualy the pattern identification
	private void setMazePattern()
	{
		String [] tmpArray = mazePatternHash.keySet().toArray(new String[mazePatternHash.size()]);
		Arrays.sort(tmpArray);
		this.mazePattern = String.join("", tmpArray); 
	}	
	
	//this function transforms an input maze to a maze that contains only the symbols GSX_
	//we call this function after we have validated that the pattern is valid
	public String[][] transformMaze()
	{
		String defaultPattern = GenericConfigParser.confGetProperty("app.defaultMazePattern");

		if (mazePattern.equals(defaultPattern))// in case the input maze is identified as of the default pattern then return the initialize maze array
		{
			return this.parsedMaze;
		}
		else
		{
			String[][] transformedMaze = new String[parsedMaze.length][parsedMaze[0].length];
			
			//we first take the generic maze pattern symbols and their mappings
			String defaultStart = GenericConfigParser.confGetProperty("maze.start");
			String defaultEnd   = GenericConfigParser.confGetProperty("maze.end");
			String defaultWall  = GenericConfigParser.confGetProperty("maze.wall");
			String defaultEmpty = GenericConfigParser.confGetProperty("maze.empty");		
			String currentStart = GenericConfigParser.confGetProperty(this.mazePattern + "." + defaultStart);
			String currentEnd   = GenericConfigParser.confGetProperty(this.mazePattern + "." + defaultEnd); 
			String currentWall  = GenericConfigParser.confGetProperty(this.mazePattern + "." + defaultWall);
			String currentEmpty = GenericConfigParser.confGetProperty(this.mazePattern + "." + defaultEmpty); 
			
			//we parse the maze and we replace every symbol with the respective default one
			for (int i = 0; i < this.parsedMaze.length; i++)
			{
				for (int j = 0; j < this.parsedMaze[i].length; j++)
				{
					if (currentStart.equals(this.parsedMaze[i][j]))
					{
						transformedMaze[i][j] = defaultStart;
					}
					else if (currentEnd.equals(this.parsedMaze[i][j]))
					{
						transformedMaze[i][j] = defaultEnd;
					}					
					else if (currentWall.equals(this.parsedMaze[i][j]))
					{
						transformedMaze[i][j] = defaultWall;
					}					
					else if (currentEmpty.equals(this.parsedMaze[i][j]))
					{
						transformedMaze[i][j] = defaultEmpty;
					}					
				}
			}
			GenericLog.log(Level.INFO, "GenericMazeInputParser.transformMaze", "Maze was transformed to generic pattern.");
			GenericLog.printTwoDimArrString(Level.INFO, "GenericMazeInputParser.transformMaze", transformedMaze);
			
			return transformedMaze;
		}
	}
	
	//apart the table we should transform also the pattern hash in order to use it for validations
	//we call this function after we have validated that the pattern is valid
	public Map<String, Integer> transformMazePatternHash()
	{
		String defaultPattern = GenericConfigParser.confGetProperty("app.defaultMazePattern");

		if (mazePattern.equals(defaultPattern))// in case the input maze has default pattern return the initialize maze hash pattern
		{
			return this.mazePatternHash;
		}
		else
		{
			Map<String, Integer> transMazePatternHash = new HashMap<>();
			//we first take the default maze pattern values (we make this configurable because one day we might want to change it)
			String defaultStart = GenericConfigParser.confGetProperty("maze.start");
			String defaultEnd   = GenericConfigParser.confGetProperty("maze.end");
			String defaultWall  = GenericConfigParser.confGetProperty("maze.wall");
			String defaultEmpty = GenericConfigParser.confGetProperty("maze.empty");		
			
			//get the configured mappings
			String currentStart = GenericConfigParser.confGetProperty(this.mazePattern + "." + defaultStart);
			String currentEnd   = GenericConfigParser.confGetProperty(this.mazePattern + "." + defaultEnd); 
			String currentWall  = GenericConfigParser.confGetProperty(this.mazePattern + "." + defaultWall);
			String currentEmpty = GenericConfigParser.confGetProperty(this.mazePattern + "." + defaultEmpty); 			
			
			//parse the current hash pattern and  and create a new one replacing the input patern symbols with the respective default symbols
			for (Map.Entry<String, Integer> entry : mazePatternHash.entrySet()) 
			{
				if (currentStart.equals(entry.getKey()))
				{
					transMazePatternHash.put(defaultStart, entry.getValue());
				}
				else if (currentEnd.equals(entry.getKey()))
				{
					transMazePatternHash.put(defaultEnd, entry.getValue());
				}					
				else if (currentWall.equals(entry.getKey()))
				{
					transMazePatternHash.put(defaultWall, entry.getValue());
				}					
				else if (currentEmpty.equals(entry.getKey()))
				{
					transMazePatternHash.put(defaultEmpty, entry.getValue());
				}					
			}
			
			return transMazePatternHash;
		}
	}	
}