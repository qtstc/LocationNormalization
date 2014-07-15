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
	
	public static String capitializeFirstLetterOfWord(String s)
	{
		int interval = 'A' - 'a';
		char[] a = s.toCharArray();
		a[0] = (char)(a[0] + interval);
		for(int i = 0;i<a.length;i++)
		{
			if(a[i] == ' ')
			{
				a[i+1] = (char) (a[i+1] +interval);
			}
		}
		return new String(a);
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
