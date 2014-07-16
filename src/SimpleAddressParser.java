import java.util.*;

/**
 * A simple address parser that breaks up the input string into tokens based on
 * non-letter characters, identifies entities based on matches with state/city
 * list and matches city to state using brutal force.
 * 
 * @author Tao Qian
 * 
 */
public class SimpleAddressParser extends USAddressParser {

	// The HashMap that stores that uses a list of U.S. city
	// names as keys and their respective state(s) as the value.
	// We used HashMap because the dataset is small enough to fit
	// in the memory.
	private HashMap<String, ArrayList<String>> cityToState;

	public SimpleAddressParser(HashMap<String, ArrayList<String>> cityToState) {
		this.cityToState = cityToState;
	}

	protected ArrayList<AddressPair> matchAddressPairs(
			ArrayList<EntityCandidate> possibleCities,
			ArrayList<EntityCandidate> possibleStates) {
		// Try evey city, state combinations to see if there is a match.
		// Not the best solution but performance is still okay with small input
		// we have.
		ArrayList<AddressPair> results = new ArrayList<AddressPair>();
		for (int i = 0; i < possibleCities.size(); i++) {
			EntityCandidate possibleCity = possibleCities.get(i);
			for (int j = 0; j < possibleCity.matches.size(); j++) {
				String state = possibleCity.matches.get(j);
				for (int k = 0; k < possibleStates.size(); k++) {
					// A match is only valid if the two entities do not
					// intersect.
					if (state.equals(possibleStates.get(k).name)
							&& !possibleStates.get(k).intersect(possibleCity)) {
						results.add(new AddressPair(possibleCity,
								possibleStates.get(k)));
					}
				}
			}
		}

		ArrayList<AddressPair> newResult = new ArrayList<AddressPair>();

		// If there is no valid city, state combination,
		if (results.size() == 0) {
			// check if there is a valid city
			if (possibleCities.size() != 0) {
				Collections.sort(possibleCities);
				// For now, we just pick the first possible city, and its first
				// possible state
				newResult.add(new AddressPair(possibleCities.get(0).name,
						possibleCities.get(0).matches.get(0)));
				return newResult;
			}

			// check if there is a valid state
			// the default size is 1 because no_state was added to the list.
			if (possibleStates.size() != 1) {
				Collections.sort(possibleStates);
				newResult.add(new AddressPair(null, possibleStates.get(0)));
				return newResult;
			}
			return newResult;
		}

		// If there is only one possible result, return it
		if (results.size() == 1) {
			return results;
		}

		// If there are more than one results,
		// return the first two that do not intersect with each other.
		for (int i = 0; i < results.size(); i++) {
			for (int j = i + 1; j < results.size(); j++) {
				if (!results.get(i).intersect(results.get(j))) {
					newResult.add(results.get(i));
					newResult.add(results.get(j));
					return newResult;
				}
			}
		}
		return newResult;
	}

	/**
	 * Look at N consecutive tokens in the list of tokens and see if they are
	 * valid entities. A entity is valid if it is a city name, or state name
	 * (not abbreviated).
	 * 
	 * @param N
	 * @param tokens
	 * @param cityToState
	 * @param possibleCities
	 * @param possibleStates
	 */
	private void getPossibleMatchesForNTokens(int N, ArrayList<String> tokens,
			HashMap<String, ArrayList<String>> cityToState,
			ArrayList<EntityCandidate> possibleCities,
			ArrayList<EntityCandidate> possibleStates) {
		for (int i = 0; i < tokens.size() - N + 1; i++) {
			String combination = tokens.get(i);
			for (int j = 1; j < N; j++) {
				combination += " " + tokens.get(i + j);
			}

			ArrayList<String> a = cityToState.get(combination);
			if (a != null) {
				possibleCities.add(new EntityCandidate(combination, i, i + N
						- 1, a));
			}

			String state = stateLongToShort.get(combination);
			if (state != null) {
				possibleStates.add(new EntityCandidate(state, i, i + N - 1,
						null));
			}
		}
	}

	@Override
	protected void identifyEntities(ArrayList<String> tokens,
			ArrayList<EntityCandidate> possibleCities,
			ArrayList<EntityCandidate> possibleStates) {
		// Check for entities for up to 5 tokens
		getPossibleMatchesForNTokens(5, tokens, cityToState, possibleCities,
				possibleStates);
		getPossibleMatchesForNTokens(4, tokens, cityToState, possibleCities,
				possibleStates);
		getPossibleMatchesForNTokens(3, tokens, cityToState, possibleCities,
				possibleStates);
		getPossibleMatchesForNTokens(2, tokens, cityToState, possibleCities,
				possibleStates);
		getPossibleMatchesForNTokens(1, tokens, cityToState, possibleCities,
				possibleStates);

		// For single token, also need to check it against abbreviation of
		// states
		for (int i = 0; i < tokens.size(); i++) {
			String word = tokens.get(i);
			if (stateShortToLong.containsKey(word)) {
				possibleStates.add(new EntityCandidate(word, i, i, null));
			}
		}
	}

	@Override
	protected ArrayList<String> getTokens(String s) {
		// Break a string into tokens by only taking letters.
		HashSet<String> ignored = new HashSet<String>();
		ignored.add("us");
		ignored.add("");

		ArrayList<String> tokens = new ArrayList<String>();
		String currentToken = "";
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isLetter(c)) {
				currentToken += c;
			} else {
				currentToken = currentToken.toLowerCase();
				if (!ignored.contains(currentToken)) {
					tokens.add(currentToken);
					currentToken = "";
				}
			}
		}
		currentToken = currentToken.toLowerCase();
		if (!ignored.contains(currentToken)) {
			tokens.add(currentToken);
		}
		return tokens;
	}

	/**
	 * Check whether a character is letter.
	 * 
	 * @param c
	 *            the character to be checked
	 * @return true if the character is a letter in the English alphabet
	 */
	public static boolean isLetter(char c) {
		if (c >= 'a' && c <= 'z') {
			return true;
		}
		if (c >= 'A' && c <= 'Z') {
			return true;
		}
		return false;
	}

}
