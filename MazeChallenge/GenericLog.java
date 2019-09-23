import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.Date; 
import java.util.ArrayList;
import java.util.Map;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   

public class GenericLog 
{
    private static FileHandler logFile;
	private static ConsoleHandler logConsole;
	private static Logger logger;
	private static String datePartName = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()).toString(); //we put this on a static variable in order to create a new file in case the date changes
	private String configFileName;
	private SimpleFormatter formatterTxt;

	//create logging with sigleton pattern
	public static void main(String[] args) 
	{
		//tests
		datePartName = "20190912";//override date to test
		GenericLog.log(Level.INFO, "GenericLog.main", "Test1");
		datePartName = "20190913";
		GenericLog.log(Level.INFO, "GenericLog.main", "Test2");
		GenericLog.log(Level.INFO, "GenericLog.main", "Test3");
	}	
	
    private GenericLog() throws IOException
	{
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setUseParentHandlers(false); //we dont want parent handlers,messes up the output format for this app
		
		String fileSeparator = System.getProperty( "file.separator"); //get os file seperator in order to be os independent
	    String logFolder = GenericConfigParser.confGetProperty("app.logfolder"); //get the folder name of the logs
		configFileName = String.join(fileSeparator, "..", logFolder, datePartName+".log"); //create filename with path
       
		//create the handlers objects
		logFile = new FileHandler(configFileName, true); //append the logging in case the file exists
		logConsole = new ConsoleHandler(); // create the console handler
		
		//setting the lvl of the logs, taken from properties file
		logFile.setLevel(Level.parse(GenericConfigParser.confGetProperty("app.log.filelvl")));
		logConsole.setLevel(Level.parse(GenericConfigParser.confGetProperty("app.log.consolelvl")));

        //customizing the format of the logs
		//not sure if this the best practice
		formatterTxt = new SimpleFormatter() {
			  private static final String format = "[%1$tF %1$tT %1$tL] [%2$-7s] %3$s %n";

			@Override
			public synchronized String format(LogRecord lr) {
				  return String.format(format,
						  new Date(lr.getMillis()),
						  lr.getLevel().getLocalizedName(),
						  lr.getMessage());
			}
		};
		
		//set the formaters on both console and log file handlers
        logFile.setFormatter(formatterTxt);
		logConsole.setFormatter(formatterTxt);
		
		//adds the handlers
        logger.addHandler(logFile);
		logger.addHandler(logConsole);
    }
	
	private static Logger getLogger()
	{
		//check intializing object if null or if day has changed in order to create new file
		if(logger == null || !datePartName.equals(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()).toString()))
		{
			try 
			{
				//close handler before adding new because it will create additional files
				if (logger != null)
				{
					logFile.close();
					logConsole.close();
				}
				new GenericLog();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return logger;
	}
	
	//log the input to file. put extra spaces in order to beutify the output
	public static void log(Level level, String caller, String msg)
	{
		getLogger().log(level, ("["+ caller + "]                                        ").substring(0,40) + ":" + msg);
	}
	
	//print two dimensional arrays
	public static void printTwoDimArrString(Level level, String caller,String[][] inputArray)
	{
		log(level, caller, "-------------------------------");
		for (int i = 0; i < inputArray.length; i++)
		{
			log(level, caller, String.join("", inputArray[i]));
		}
		log(level, caller, "-------------------------------");
	}	

	//print String arraylists
	public static void printStringlist(Level level, String caller, ArrayList<String> inputArrayList)
	{
		log(level, caller, "-------------------------------");
		for(String inputArrayRow : inputArrayList) 
		{
			log(level, caller, inputArrayRow);
		}
		log(level, caller, "-------------------------------");
	}

	//print specific  time of hasmap
	public static void printHashStrInt(Level level, String caller, Map<String, Integer> map)
	{
		log(level, caller, "-------------------------------");
		map.forEach((key,value) -> log(level, caller, key + " = " + value));
		log(level, caller, "-------------------------------");
	}	
}