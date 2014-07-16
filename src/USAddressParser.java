import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Normalizes addresses.
 * 
 * @author Tao Qian
 * 
 */
public abstract class USAddressParser {
	
	public static final String NO_STATE = "no_state";
	public static final String NO_CITY = "no_city";
	
	/**
	 * Break a string into individual tokens
	 * 
	 * @param s
	 *            the string
	 * @return the tokens in the string stored in an ArrayList
	 */
	protected abstract ArrayList<String> getTokens(String s);

	/**
	 * Identify the tokens which are possible candidates for city and state
	 * names
	 * 
	 * @param tokens
	 *            the tokens to be identified
	 * @param possibleCities
	 *            a list of possible cities
	 * @param possibleStates
	 *            a list of possible states
	 */
	protected abstract void identifyEntities(ArrayList<String> tokens,
			ArrayList<EntityCandidate> possibleCities,
			ArrayList<EntityCandidate> possibleStates);

	/**
	 * Match possible city candidates with state candidates
	 * 
	 * @param possibleCities
	 *            the tokens which are possibly cities
	 * @param possibleStates
	 *            the tokens which are possibly states
	 * @return matched city, state pairs
	 */
	protected abstract ArrayList<AddressPair> matchAddressPairs(
			ArrayList<EntityCandidate> possibleCities,
			ArrayList<EntityCandidate> possibleStates);

	// Hard coded because the state names do not change very often.
	private static final String[] SHORT_STATE_NAMES = new String[] { "ak",
			"ky", "ny", "al", "la", "oh", "ar", "ma", "ok", "az", "md", "or",
			"ca", "me", "pa", "co", "mi", "ri", "ct", "mn", "sc", "dc", "mo",
			"sd", "de", "ms", "tn", "fl", "mt", "tx", "ga", "nc", "ut", "hi",
			"nd", "va", "ia", "ne", "vt", "id", "nh", "wa", "il", "nj", "wi",
			"in", "nm", "wv", "ks", "nv", "wy" };
	private static final String[] LONG_STATE_NAMES = new String[] { "alaska",
			"kentucky", "new york", "alabama", "louisiana", "ohio", "arkansas",
			"massachusetts", "oklahoma", "arizona", "maryland", "oregon",
			"california", "maine", "pennsylvania", "colorado", "michigan",
			"rhode island", "connecticut", "minnesota", "south carolina",
			"district of columbia", "missouri", "south dakota", "delaware",
			"mississippi", "tennessee", "florida", "montana", "texas",
			"georgia", "north carolina", "utah", "hawaii", "north dakota",
			"virginia", "iowa", "nebraska", "vermont", "idaho",
			"new hampshire", "washington", "illinois", "new jersey",
			"wisconsin", "indiana", "new mexico", "west virginia", "kansas",
			"nevada", "wyoming" };

	// HashMap which allows us to get the map between state names and their
	// abbreviations.
	protected static final HashMap<String, String> stateShortToLong;
	protected static final HashMap<String, String> stateLongToShort;

	// Load the HashMaps.
	static {
		stateShortToLong = new HashMap<String, String>();
		stateLongToShort = new HashMap<String, String>();
		for (int i = 0; i < SHORT_STATE_NAMES.length; i++) {
			String shortState = SHORT_STATE_NAMES[i];
			String longState = LONG_STATE_NAMES[i];
			stateLongToShort.put(longState, shortState);
			stateShortToLong.put(shortState, longState);
		}
	}

	/**
	 * Get the normalized addresses
	 * 
	 * @param line
	 *            the address string to be normalized
	 * @return the normalized address if the input can be normalized, otherwise,
	 *         the original line.
	 */
	public String getResult(String line) {
		ArrayList<EntityCandidate> possibleCities = new ArrayList<EntityCandidate>();
		ArrayList<EntityCandidate> possibleStates = new ArrayList<EntityCandidate>();
		//Add a NO_STATE to the possible states because some cities (e.g. alaska government)
		//do not have a state and we want to match those.
		possibleStates.add(new EntityCandidate(NO_STATE,
				EntityCandidate.DEFAULT_START, EntityCandidate.DEFAULT_END, null));

		ArrayList<String> tokens = getTokens(line);
		identifyEntities(tokens, possibleCities, possibleStates);
		ArrayList<AddressPair> pairs = matchAddressPairs(possibleCities,
				possibleStates);
		if (pairs.size() == 0) {
			// Return the original line if we cannot parse it.
			return line;
		}
		
		//Sort the result pairs for normalization purpose.
		Collections.sort(pairs);
		
		//Generate the result string
		StringBuilder sb = new StringBuilder(pairs.get(0).toResultString(
				stateShortToLong));
		for (int i = 1; i < pairs.size(); i++) {
			sb.append(" - " + pairs.get(i).toResultString(stateShortToLong));
		}
		return sb.toString();
	}
}
