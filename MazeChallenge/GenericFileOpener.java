import java.io.*; 
import java.util.logging.*;

class GenericFileOpener
{ 
	//function for opening files with BufferedReader
	public BufferedReader BufferedReaderFunc(String fileFullPath) throws FileNotFoundException
	{ 
		GenericLog.log(Level.INFO, "GenericFileOpener.BufferedReaderFunc", "Trying Opening file:" + fileFullPath);
		BufferedReader fileBufferReader = null;
		try
		{
			fileBufferReader = new BufferedReader(new FileReader(fileFullPath));
			GenericLog.log(Level.INFO, "GenericFileOpener.BufferedReaderFunc", "File:" + fileFullPath +" opened");
			return fileBufferReader;			
		}
		catch(FileNotFoundException e)
		{
			//if the file does not open do not proceed
			GenericLog.log(Level.SEVERE, "GenericFileOpener.BufferedReaderFunc", "File not found:" + fileFullPath);
			e.printStackTrace();
			throw e;
		}
	} 
	
	//function for opening files with InputStream
	public InputStream InputStreamReaderFunc(String fileFullPath)  throws FileNotFoundException
	{ 
		GenericLog.log(Level.INFO, "GenericFileOpener.InputStreamReaderFunc", "Trying Opening file:" + fileFullPath);
		InputStream fileInputStreamReader = null;
		try
		{
			fileInputStreamReader = new FileInputStream(fileFullPath);
			GenericLog.log(Level.INFO, "GenericFileOpener.InputStreamReaderFunc", "File:" + fileFullPath + " opened");
			return fileInputStreamReader;			
		}
		catch(FileNotFoundException e)
		{
			//if the file does not open do not proceed
			GenericLog.log(Level.SEVERE, "GenericFileOpener.InputStreamReaderFunc", "File not found:" + fileFullPath);
			e.printStackTrace();
			throw e;
		}
	} 	
}