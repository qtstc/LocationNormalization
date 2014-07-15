import java.util.*;

public class SimpleAddressParser extends USAddressParser{
	private HashMap<String, ArrayList<String>> cityToState;
	
	public SimpleAddressParser(HashMap<String, ArrayList<String>> cityToState)
	{
		this.cityToState = cityToState;
	}
	
	protected ArrayList<AddressPair> matchAddressPairs(ArrayList<PossibleMatch> possibleCities, ArrayList<PossibleMatch> possibleStates)
	{
		ArrayList<AddressPair> results = new ArrayList<AddressPair>();
		for(int i = 0; i<possibleCities.size(); i++)
		{
			PossibleMatch possibleCity = possibleCities.get(i);
			for(int j = 0; j< possibleCity.matches.size(); j++)
			{
				String state = possibleCity.matches.get(j);
				for(int k = 0; k < possibleStates.size(); k ++)
				{
					if(state.equals(possibleStates.get(k).key) 
					&& !possibleStates.get(k).intersect(possibleCity))
					{
						results.add(new AddressPair(possibleCity, possibleStates.get(k)));
					}
				}
			}
		}
		
		ArrayList<AddressPair> newResult = new ArrayList<AddressPair>();
		
		//If there is no valid city, state combination,
		if(results.size() == 0)
		{
			//check if there is a valid city
			if(possibleCities.size() != 0)
			{
				Collections.sort(possibleCities);
				newResult.add(new AddressPair(possibleCities.get(0).key, possibleCities.get(0).matches.get(0)));
				return newResult;
			}
			
			//check if there is a valid state
			//the default size is 1 because no_state was added to the list.
			if(possibleStates.size() != 1)
			{
				Collections.sort(possibleStates);
				newResult.add(new AddressPair(null, possibleStates.get(0)));
				return newResult;
			}
			return newResult;
		}
		
		//If there is only one possible result, return it
		if(results.size() == 1)
		{
			return results;
		}
		
		//If there are more than one results,
		//return the two that do not intersect with each other.
		for(int i = 0;i<results.size();i++)
		{
			for(int j = i+1;j<results.size();j++)
			{
				if(!results.get(i).intersect(results.get(j)))
				{
					newResult.add(results.get(i));
					newResult.add(results.get(j));
					return newResult;
				}
			}
		}
		return newResult;
	}
	
	
	private void getPossibleMatchesForNTokens(int N, ArrayList<String> tokens, HashMap<String, ArrayList<String>> cityToState, ArrayList<PossibleMatch> possibleCities, ArrayList<PossibleMatch> possibleStates)
	{
		for(int i = 0; i < tokens.size() - N + 1;i++)
		{
			String combination = tokens.get(i);
			for(int j = 1; j < N ; j ++)
			{
				combination += " "+tokens.get(i + j);
			}
			
			ArrayList<String> a = cityToState.get(combination);
			if(a != null)
			{
				possibleCities.add(new PossibleMatch(combination,i, i+N-1, a));
			}
			
			String state = stateLongToShort.get(combination); 
		    if(state != null)
		    {
				possibleStates.add(new PossibleMatch(state,i, i + N - 1, null));
			}
		}
	}
	
	@Override
	protected void getPossibleMatches(ArrayList<String> tokens, ArrayList<PossibleMatch> possibleCities, ArrayList<PossibleMatch> possibleStates)
	{	
		getPossibleMatchesForNTokens(4, tokens, cityToState, possibleCities, possibleStates);
		getPossibleMatchesForNTokens(3, tokens, cityToState, possibleCities, possibleStates);
		getPossibleMatchesForNTokens(2, tokens, cityToState, possibleCities, possibleStates);
		getPossibleMatchesForNTokens(1, tokens, cityToState, possibleCities, possibleStates);
		for(int i = 0; i < tokens.size();i++)
		{
			String word = tokens.get(i);
			if(stateShortToLong.containsKey(word))
			{
				possibleStates.add(new PossibleMatch(word, i, i, null));
			}
		}
	}
	
	@Override
	protected ArrayList<String> getTokens(String s)
	{
		HashSet<String> ignored = new HashSet<String>();
		ignored.add("us");
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
	
	/**
	 * Check whether a character is letter.
	 * @param c the character to be checked
	 * @return true if the character is a letter in the English alphabet
	 */
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
	
}
