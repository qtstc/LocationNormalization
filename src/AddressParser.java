import java.util.*;

public class AddressParser {
	
	public static final HashSet<String> IGNORED_TOKEN;
	private HashMap<String, ArrayList<String>> cityToState;
	private HashMap<String,String> stateShortToLong;
	private HashMap<String,String> stateLongToShort;
	
	static
	{
		IGNORED_TOKEN = new HashSet<String>();
		IGNORED_TOKEN.add("us");
	}
	
	public AddressParser(HashMap<String, ArrayList<String>> cityToState, HashMap<String,String> stateShortToLong, HashMap<String,String> stateLongToShort)
	{
		this.cityToState = cityToState;
		this.stateShortToLong = stateShortToLong;
		this.stateLongToShort = stateLongToShort;
	}
	
	public String getResult(String line)
	{
		ArrayList<String> tokens = Utilities.getNormalizedLetterTokens(line, IGNORED_TOKEN);
		
		return null;
	}
	
}
