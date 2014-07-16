import java.util.HashMap;

/**
 * A city, state pair extracted from a list of tokens.
 * 
 * @author Tao Qian
 */
public class AddressPair implements Comparable<AddressPair> {

	// Parameters are supposed to be implemented as private with get/set
	// but will skip that for now.
	
	String state;
	String city;

	// Starting, ending (both inclusive) index for the city and state in the list of tokens.
	int cityStart;
	int cityEnd;
	int stateStart;
	int stateEnd;

	public AddressPair() {
		state = USAddressParser.NO_STATE;
		city = USAddressParser.NO_CITY;

		cityStart = EntityCandidate.DEFAULT_START;
		cityEnd = EntityCandidate.DEFAULT_END;
		stateStart = EntityCandidate.DEFAULT_START;
		stateEnd = EntityCandidate.DEFAULT_END;
	}

	public AddressPair(EntityCandidate city, EntityCandidate state) {
		this();
		if (city != null) {
			this.city = city.name;
			this.cityStart = city.startIndex;
			this.cityEnd = city.endIndex;
		}
		if (state != null) {
			this.state = state.name;
			this.stateStart = state.startIndex;
			this.stateEnd = state.endIndex;
		}
	}

	public AddressPair(String city, String state) {
		this();
		this.city = city;
		this.state = state;
	}

	public AddressPair(String city, String state, int cityStart, int cityEnd,
			int stateStart, int stateEnd) {
		this.city = city;
		this.state = state;
		this.cityStart = cityStart;
		this.cityEnd = cityEnd;
		this.stateStart = stateStart;
		this.stateEnd = stateEnd;
	}

	public String toString() {
		return city + "," + state;
	}

	/**
	 * Check whether two AddressPairs intersect with each other.
	 * @param p
	 * @return
	 */
	public boolean intersect(AddressPair p) {
		if (cityStart > p.stateEnd || stateEnd < p.cityStart) {
			return false;
		}
		return true;
	}

	public boolean subset(AddressPair p) {
		if (cityStart <= p.cityStart && cityEnd >= p.cityEnd
				&& stateStart <= p.stateStart && stateEnd >= p.stateEnd) {
			return true;
		}
		return false;
	}

	public String toResultString(HashMap<String, String> stateShortToLong) {
		// In the case that there is no state
		if (state.equals(USAddressParser.NO_STATE)) {
			return Utilities.capitializeFirstLetterOfWord(city);
		}

		// In the case that there is no city
		if (city.equals(USAddressParser.NO_CITY)) {
			return Utilities.capitializeFirstLetterOfWord(stateShortToLong
					.get(state));
		}

		// In all other cases
		return Utilities.capitializeFirstLetterOfWord(city) + ", "
				+ state.toUpperCase();
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

		int cityDiff = (o.cityEnd - o.cityStart) - (cityEnd - cityStart);
		if (cityDiff != 0) {
			return cityDiff;
		}
		return (o.stateEnd - o.stateStart) - (stateEnd - stateStart);
	}
}
