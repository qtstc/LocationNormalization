import java.util.*;
import java.io.*;

public class Utilities {
	
	
	public static void print(Iterable it, String seperator)
	{
		for(Object s : it)
		{
			System.out.print(s);
			System.out.print(seperator);
		}
	}
	
	
	public static ArrayList<String> getNormalizedLetterTokens(String s, HashSet<String> ignored)
	{
		ignored.add("");
		ArrayList<String> tokens = new ArrayList<String>(); 
		String currentToken = "";
		for(int i = 0;i<s.length();i++)
		{
			char c = s.charAt(i);
			if(isLetter(c))
			{
				currentToken += c;
			}
			else 
			{
				currentToken = currentToken.toLowerCase();
				if(!ignored.contains(currentToken))
				{
					tokens.add(currentToken);
					currentToken = "";
				}
			}
		}
		currentToken = currentToken.toLowerCase();
		if(!ignored.contains(currentToken))
		{
			tokens.add(currentToken);
		}
		return tokens;
	}
	
	public static boolean isLetter(char c)
	{
		if(c >= 'a' && c <= 'z')
		{
			return true;
		}
		if(c >= 'A' && c <= 'Z')
		{
			return true;
		}
		return false;
	}
	
	public static ArrayList<String> readLinesFromFile(String filePath)
	{
		BufferedReader r = null;
		String line = "";
		try
		{
			r = new BufferedReader(new FileReader(filePath));
			line = r.readLine();
			ArrayList<String> lines = new ArrayList<String>();
			while(line != null)
			{
				lines.add(line);
				line = r.readLine();
			}
			return lines;
		}
		catch (IOException e)
		{
			System.out.println("Line: "+line+" : "+e.toString());
			return null;
		}
		finally
		{
			if(r != null)
			{
				try
				{
					r.close();
				}
				catch(Exception e)
				{
					System.out.println("Failed to close bufferedReder: "+e.toString());
				}
			}
		}
	}
}
