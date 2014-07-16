import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class LocationNormalization {

	public static final String US_CITIES_DATA_FILE_PATH = "data/us_cities.txt";
	public static final String US_STATES_DATA_FILE_PATH = "data/us_states.txt";
	public static final String NORMALIZED_US_CITIES_DATA_FILE_PATH = "data/normalized_us_cities.txt";
	public static final String POSSIBLE_INPUTS_FILE_PATH = "data/possible_inputs.txt";
	
	public static final String SEPERATOR = "+";
	
	public static void main(String[] args) throws IOException{
		HashMap<String, ArrayList<String>> cityToState = new HashMap<String, ArrayList<String>>();
		readNormalizedUSCitiesDataFile(NORMALIZED_US_CITIES_DATA_FILE_PATH, cityToState);
		
		ArrayList<String> possibleInputs = Utilities.readLinesFromFile(POSSIBLE_INPUTS_FILE_PATH);
		
		SimpleAddressParser parser = new SimpleAddressParser(cityToState);
		
		ArrayList<String> possibleOutputs = new ArrayList<String>();
		for(int i = 0;i<possibleInputs.size();i++)
		{
			possibleOutputs.add(parser.getResult(possibleInputs.get(i)));
		}
		
		Utilities.print(possibleOutputs, "\n");
	}

	public static void readNormalizedUSCitiesDataFile(String filePath, HashMap<String, ArrayList<String>> cityToState)
	{
		ArrayList<String> lines = Utilities.readLinesFromFile(filePath);
		for(int i = 0;i<lines.size();i++)
		{
			StringTokenizer st = new StringTokenizer(lines.get(i));
			ArrayList<String> states = new ArrayList<String>();
			String city = st.nextToken(SEPERATOR);
			while(st.hasMoreTokens())
			{
				states.add(st.nextToken(SEPERATOR));
			}
			cityToState.put(city, states);
		}
	}
	
	
	
	public static void normalizeUSCitiesDataFile(String filePath, String newFilePath) {
		TreeMap<String, ArrayList<String>> cityToState = new TreeMap<String, ArrayList<String>>();

		ArrayList<String> lines = Utilities.readLinesFromFile(filePath);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String[] result = parseUSCitiesLine(line);
			String city = result[0].toLowerCase();
			String state = result[1].toLowerCase();

			if (cityToState.containsKey(city)) {
				ArrayList<String> array = cityToState.get(city);
				array.add(state);
			} else {
				ArrayList<String> array = new ArrayList<String>();
				array.add(state);
				cityToState.put(city, array);
			}
		}
		
		BufferedWriter w = null;
		
		try
		{
			w = new BufferedWriter(new FileWriter(newFilePath));
			for(Entry<String, ArrayList<String>> pair : cityToState.entrySet())
			{
				w.write(pair.getKey());
				ArrayList<String> states = pair.getValue();
				Collections.sort(states);
				for(int i = 0;i<states.size();i++)
				{
					w.write(SEPERATOR+states.get(i));
				}
				w.write("\n");
			}
			w.flush();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		finally
		{
			if(w != null)
			{
				try
				{
					w.close();
				}
				catch(Exception e)
				{
					System.out.println("Failed to close write:"+e.toString());
				}
			}
		}
	}

	public static String[] parseUSCitiesLine(String line) {
		String BALANCE = "(balance)";

		if (line.contains(BALANCE)) {
			line = line.replace(BALANCE, "");
		}

		String[] result = new String[2];
		int p1 = line.indexOf("(");
		// the line does not contain (
		// it is assumed to be in format city, state
		if (p1 < 0) {
			String[] tokens = line.split(",");
			result[0] = tokens[0].trim();
			result[1] = tokens.length == 2 ? tokens[1].trim() : USAddressParser.NO_STATE;
			return result;
		}

		// can be in various formats
		// city is assumed to be everything before the first (
		// state is assumed to be between the second and fourth character after
		// the comma
		result[0] = line.substring(0, p1);
		int comma = line.indexOf(",");
		result[1] = line.substring(comma + 2, comma + 4);
		return result;
	}
}
