import java.util.HashMap;


public class AddressPair implements Comparable<AddressPair>{
	
		String state;
		String city;
		
		int cityStart;
		int cityEnd;
		int stateStart;
		int stateEnd;
		
		public AddressPair()
		{
			state = LocationNormalization.NO_STATE;
			city = LocationNormalization.NO_CITY;
			
			cityStart = -1;
			cityEnd = -2;
			stateStart = -1;
			stateEnd = -2;
		}
		
		public AddressPair(PossibleMatch city, PossibleMatch state)
		{
			this();
			if(city != null)
			{
				this.city = city.key;
				this.cityStart = city.startIndex;
				this.cityEnd = city.endIndex;
			}
			if(state != null)
			{
				this.state = state.key;
				this.stateStart = state.startIndex;
				this.stateEnd = state.endIndex;
			}
		}
		
		public AddressPair(String city, String state)
		{
			this();
			this.city = city;
			this.state = state;
		}
		
		public AddressPair(String city, String state, int cityStart, int cityEnd, int stateStart, int stateEnd)
		{
			this.city = city;
			this.state = state;
			this.cityStart = cityStart;
			this.cityEnd = cityEnd;
			this.stateStart = stateStart;
			this.stateEnd = stateEnd;
		}
		
		public String toString()
		{
			return city+","+state;
		}
		
		public boolean intersect(AddressPair p)
		{
			if(cityStart > p.stateEnd || stateEnd < p.cityStart)
			{
				return false;
			}
			return true;
		}
		
		public boolean subset(AddressPair p)
		{
			if(cityStart <= p.cityStart && cityEnd >= p.cityEnd
					&& stateStart <= p.stateStart && stateEnd >= p.stateEnd)
			{
				return true;
			}
			return false;
		}
		
		public String toResultString(HashMap<String, String> stateShortToLong)
		{
			// In the case that there is no state
			if(state.equals(LocationNormalization.NO_STATE))
			{
				return Utilities.capitializeFirstLetterOfWord(city);
			}
			
			// In the case that there is no city
			if(city.equals(LocationNormalization.NO_CITY))
			{
				return Utilities.capitializeFirstLetterOfWord(stateShortToLong.get(state));
			}
			
			//In all other cases
			return Utilities.capitializeFirstLetterOfWord(city) + ", "+ state.toUpperCase();
		}
		
		public int hashCode() {
	        return toString().hashCode();
	    }

	    public boolean equals(Object obj) {
	       if (!(obj instanceof AddressPair))
	            return false;
	        if (obj == this)
	            return true;

	        AddressPair rhs = (AddressPair) obj;
	        return rhs.hashCode() == hashCode();
	    }
	    
		@Override
		public int compareTo(AddressPair o) {
	    	
	    	int cityDiff =  (o.cityEnd - o.cityStart) - (cityEnd - cityStart);
	    	if(cityDiff != 0)
	    	{
	    		return cityDiff;
	    	}
	    	return (o.stateEnd - o.stateStart) - (stateEnd - stateStart);
		}
}
