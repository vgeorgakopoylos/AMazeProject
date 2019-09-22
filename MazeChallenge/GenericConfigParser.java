import java.io.InputStream; 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

public class GenericConfigParser
{
	static final String CONFIG_FILE_FOLDER = "Config";
	static final String CONFIG_FILE_NAME = "app.properties";
	static final String PATTERN_PREFIX = "pattern";
	
	private static GenericConfigParser configFileInstance;
	private static Properties prop  = new Properties();
	
	//get configuration properties with sigleton pattern
	public static void main(String[] args) 
	{
		//tests
		System.out.println("app.name:"+confGetProperty("app.name"));
		GetAvailiablePatterns();
	}	
	
    private GenericConfigParser()
	{
		GenericFileOpener inputStreamLogFileObj = new GenericFileOpener();
		String fileSeparator = System.getProperty( "file.separator" );	// get the os file seperator	
		String configFileName = String.join(fileSeparator, ".." , CONFIG_FILE_FOLDER, CONFIG_FILE_NAME); //create the file name
		GenericLog.log(Level.INFO, "GenericConfigParser.Constructor", "Opening Property file:" + configFileName);
		
		try
		{
			InputStream inputStreamLogFile = inputStreamLogFileObj.InputStreamReaderFunc(configFileName); //open configuration file
			prop.load(inputStreamLogFile);
			GenericLog.log(Level.INFO, "GenericConfigParser.Constructor", "Property File:" + configFileName +" opened");
		}
		catch (IOException e) 
		{
			GenericLog.log(Level.SEVERE, "GenericConfigParser.Constructor", "Error opening file:"+configFileName);
            e.printStackTrace();
			System.exit(0);//there is no need to go on if config file does not exists
        }
    }
	
	private static GenericConfigParser getConfig()
	{
		//initialize object if does not exists.
		if(configFileInstance == null)
		{
			configFileInstance = new GenericConfigParser();
		}
		return configFileInstance;
	}
	
	//get property given the name
	public static String confGetProperty(String propertyName)
	{
		return getConfig().prop.getProperty(propertyName);
	}
	
	//for future reference: we can put this in static configuration object in order not to get this for every maze.
	//this function gets the supported/configured maze patterns. The pattern is consisted from maze distinct used character i.e. GSX_ G stand for Goal S for Start etc
	//The order of the letters in the pattern must derive from array sort
	public static ArrayList<String> GetAvailiablePatterns()
	{
		GenericConfigParser configObj = getConfig();
		ArrayList<String> availPatternList = new ArrayList<String>();
		int counter = 1;

		while (true)
		{
			if (configObj.prop.containsKey(PATTERN_PREFIX + counter)) 
			{
				availPatternList.add(prop.getProperty(PATTERN_PREFIX + counter));
				counter++;
			}
			else
			{
				break;
			}
		}

		return availPatternList;
	}	
}