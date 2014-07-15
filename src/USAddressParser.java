import java.util.ArrayList;
import java.util.HashMap;


public abstract class USAddressParser {
	
	protected abstract ArrayList<String> getTokens(String s);
	protected abstract void getPossibleMatches(ArrayList<String> tokens, ArrayList<PossibleMatch> possibleCities, ArrayList<PossibleMatch> possibleStates);
	protected abstract ArrayList<AddressPair> matchAddressPairs(ArrayList<PossibleMatch> possibleCities, ArrayList<PossibleMatch> possibleStates);
	
	protected static final HashMap<String,String> stateShortToLong;
	protected static final HashMap<String,String> stateLongToShort;
	
	private static final String[] SHORT_STATE_NAMES = new String[] {"ak","ky","ny","al","la","oh","ar","ma","ok","az","md","or","ca","me","pa","co","mi","ri","ct","mn","sc","dc","mo","sd","de","ms","tn","fl","mt","tx","ga","nc","ut","hi","nd","va","ia","ne","vt","id","nh","wa","il","nj","wi","in","nm","wv","ks","nv","wy"};
	private static final String[] LONG_STATE_NAMES = new String[] {"alaska","kentucky","new york","alabama","louisiana","ohio","arkansas","massachusetts","oklahoma","arizona","maryland","oregon","california","maine","pennsylvania","colorado","michigan","rhode island","connecticut","minnesota","south carolina","district of columbia","missouri","south dakota","delaware","mississippi","tennessee","florida","montana","texas","georgia","north carolina","utah","hawaii","north dakota","virginia","iowa","nebraska","vermont","idaho","new hampshire","washington","illinois","new jersey","wisconsin","indiana","new mexico","west virginia","kansas","nevada","wyoming"};
	
	static
	{
		stateShortToLong = new HashMap<String, String>();
		stateLongToShort = new HashMap<String, String>();
		for(int i = 0;i< SHORT_STATE_NAMES.length;i++)
		{
			String shortState = SHORT_STATE_NAMES[i];
			String longState = LONG_STATE_NAMES[i];
			stateLongToShort.put(longState,shortState);
			stateShortToLong.put(shortState, longState);
		}
	}
	
	public String getResult(String line)
	{
		ArrayList<PossibleMatch> possibleCities = new ArrayList<PossibleMatch>();
		ArrayList<PossibleMatch> possibleStates = new ArrayList<PossibleMatch>();
		possibleStates.add(new PossibleMatch(LocationNormalization.NO_STATE,-1,-2,null));
		
		ArrayList<String> tokens = getTokens(line);
		getPossibleMatches(tokens, possibleCities, possibleStates);
		ArrayList<AddressPair> pairs = matchAddressPairs(possibleCities, possibleStates);
		if(pairs.size() == 0)
		{
			//Return the original line if we cannot parse it.
			return line;
		}
		StringBuilder sb = new StringBuilder(pairs.get(0).toResultString(stateShortToLong));
		for(int i = 1;i<pairs.size();i++)
		{
			sb.append(" - "+pairs.get(i).toResultString(stateShortToLong));
		}
		return sb.toString();
	}
}
