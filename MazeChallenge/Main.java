import java.io.*; 
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

public class Main 
{
    public static void main(String[] args)
	{  
	    //initiliazing variables/objects
		GenericFileOpener bufferReaderObj;
		BufferedReader mazesFileBuffer;
		String currnetLine;
		int  lineCounter = 0;
		ArrayList<String> currProcMaze = new ArrayList<String>();
		String mazeFileSeperator;
		Scanner filenameScanner = new Scanner(System.in);
		
		//read file name from user input
		System.out.print("Please enter the full path of the file you want to parse:");
		String filename = filenameScanner.nextLine();
		while (filename.equals(""))
		{
			System.out.print("Please enter a non empty input file path:"); //in case of empty input, loop infinitely demanding for "valid" input
			filename = filenameScanner.nextLine();
		}
		
		try
		{
			//try to open the input file
			bufferReaderObj = new GenericFileOpener();
			mazesFileBuffer =  bufferReaderObj.BufferedReaderFunc(filename);
		}
		catch(FileNotFoundException e)
		{
			//in case of file open error we cannot proceed;
			return;
		}
		
		try 
		{
			// we will support multiple mazes within the file
			//the seperator of the mazes will be ;(configurable) at the end of every maze
			//there is no need for ; at the last maze
			GenericLog.log(Level.INFO, "Main.main", "Start Processing Maze file");
			mazeFileSeperator = GenericConfigParser.confGetProperty("app.mazefileseperator");//only one maze file seperator can be supported
			while ((currnetLine = mazesFileBuffer.readLine()) != null) //loop within the lines of the file in order to seperate the mazes
			{
				if (currnetLine.indexOf(mazeFileSeperator) > -1) //read every line until our configured seperator
				{
					callActor(currProcMaze); //we found the first maze. start calling actors
					lineCounter = 0;
					currProcMaze = new ArrayList<String>(); //re-instantiate the to be processed arraylist
					continue;	
				}
				else
				{
					currProcMaze.add(currnetLine); //append rows to the list in order to struct the to be processed maze
				}
				lineCounter ++;
			}
			callActor(currProcMaze); //we do not have maze seperator at the end of the file so call once more the callActor method to process the last maze
			
			mazesFileBuffer.close(); //close the buffer
			GenericLog.log(Level.INFO, "Main.main", "End Processing Maze file");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
    }
	
	public static void callActor(ArrayList<String> currentProcMaze)
	{
		//First validate if the input maze
		GenericLog.log(Level.INFO, "Main.callActor", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Current Maze Start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		GenericMazeInputParser currentProcMazeObj = new GenericMazeInputParser(currentProcMaze);
		//Perform Generic Maze validatations.
		//Unsupported patterns
		//Maze that is not rectangle
		if (!currentProcMazeObj.isValid)
		{
			GenericLog.log(Level.WARNING, "Main.callActor", "Maze found INVALID due to:");
			GenericLog.printStringlist(Level.WARNING, "Main.callActor", currentProcMazeObj.mazeInvalidMessages);//print the input maze
			GenericLog.log(Level.WARNING, "Main.callActor", "Continue to the next maze if exists!");
			return;
		}
		
		GenericLog.log(Level.INFO, "Main.callActor", "----------------------Start Calling Random Actor----------------------");
		
		//Initialize and run the RandomActor
		try
		{
			RandomActor randomActor =  new RandomActor(currentProcMazeObj);
			randomActor.runRandomPathFinding(); //create actor object
			
			if (randomActor.solutionFound)
			{
				GenericLog.log(Level.INFO, "Main.callActor", "--------------------------RANDOM ACTOR FOUND SOLUTION--------------------------");
				randomActor.printPath();
			}	
			else
			{
				GenericLog.log(Level.INFO, "Main.callActor", "-----------------------------NO VALID SOLUTION-----------------------------");
				randomActor.printPath();
			}			
		}
		catch(Exception e)
		{
			GenericLog.log(Level.WARNING, "Main.callActor", "Random actor execution ended up with error:" + (e.getMessage()==null? e.toString() : e.getMessage()));
			//no need to do something, just continue to the next actions
		}

		GenericLog.log(Level.INFO, "Main.callActor", "----------------------End Calling Random Actor----------------------");
		GenericLog.log(Level.INFO, "Main.callActor", "");
		GenericLog.log(Level.INFO, "Main.callActor", "----------------------Start Calling Ranking Actor----------------------");
		
		//Initialize and run the RankingActor
		try
		{
			RankingActor rankingActor =  new RankingActor(currentProcMazeObj); //create actor object
			rankingActor.rankingPathFinding(); //run ranking path find algorithm
			
			if (rankingActor.solutionFound)
			{
				GenericLog.log(Level.INFO, "Main.callActor", "--------------------------RANKING ACTOR FOUND SOLUTION--------------------------");
				rankingActor.printPath();
			}
			else
			{
				GenericLog.log(Level.INFO, "Main.callActor", "-----------------------------NO VALID SOLUTION-----------------------------");
				rankingActor.printPath();
			}
		}
		catch(Exception e)
		{	
			GenericLog.log(Level.WARNING, "Main.callActor", "Ranking actor execution ended up with error:" + (e.getMessage()==null? e.toString() : e.getMessage()));
			//no need to do something, just continue to the next actions
		}		
		GenericLog.log(Level.INFO, "Main.callActor", "----------------------End Calling Ranking Actor----------------------");
		GenericLog.log(Level.INFO, "Main.callActor", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Current Maze End!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		GenericLog.log(Level.INFO, "Main.callActor", "");
	}
}