import java.io.*;
import java.util.*;

public class LocationNormalization {

	public static final String NO_STATE = "no_state";
	public static final String US_CITIES_DATA_FILE_PATH = "data/us_cities.txt";
	public static final String US_STATES_DATA_FILE_PATH = "data/us_states.txt";

	public static void main(String[] args) throws IOException{
		parseUSCitiesDataFile(US_CITIES_DATA_FILE_PATH);
		HashMap<String, String> stateShortToLong = new HashMap<String, String>();
		HashMap<String, String> stateLongToShort = new HashMap<String, String>();
		readUSStatesDataFile(US_STATES_DATA_FILE_PATH, stateLongToShort, stateShortToLong);
	}
	
	public static void readUSStatesDataFile(String filePath, HashMap<String, String> stateLongToShort, HashMap<String, String> stateShortToLong)
	{
		ArrayList<String> lines = Utilities.readLinesFromFile(filePath);
		for(int i = 0 ; i < lines.size();i++)
		{
			String[] tokens = lines.get(i).split("-");
			stateLongToShort.put(tokens[1],tokens[0]);
			stateShortToLong.put(tokens[0],tokens[1]);
		}
	}

	
	public static void parseUSCitiesDataFile(String filePath) {
		HashMap<String, ArrayList<String>> cityToState = new HashMap<String, ArrayList<String>>();
		int duplicateCount = 0;
		int maxDuplicate = 0;

		ArrayList<String> lines = Utilities.readLinesFromFile(filePath);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String[] result = parseUSCitiesLine(line);
			String city = result[0];
			String state = result[1];

			if (cityToState.containsKey(city)) {
				ArrayList<String> array = cityToState.get(city);
				array.add(state);
				// System.out.println(tokens[0]+" is in multiple states");
				maxDuplicate = Math.max(maxDuplicate, array.size());
				duplicateCount++;
			} else {
				ArrayList<String> array = new ArrayList<String>();
				array.add(state);
				cityToState.put(city, array);
			}
		}
		System.out.println(cityToState.size());
		System.out.println(duplicateCount);
		System.out.println(maxDuplicate);
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
			result[1] = tokens.length == 2 ? tokens[1].trim() : NO_STATE;
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
