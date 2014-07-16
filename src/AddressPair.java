import java.util.HashMap;

/**
 * A city, state pair extracted from a list of tokens.
 * 
 * @author Tao Qian
 */
public class AddressPair implements Comparable<AddressPair> {

	// Parameters are supposed to be implemented as private with get/set
	// but will skip that for now.

	EntityCandidate city;
	EntityCandidate state;

	private static EntityCandidate getDefaultState() {
		return new EntityCandidate(USAddressParser.NO_STATE,
				EntityCandidate.DEFAULT_START, EntityCandidate.DEFAULT_END,
				null);
	}

	private static EntityCandidate getDefaultCity() {
		return new EntityCandidate(USAddressParser.NO_CITY,
				EntityCandidate.DEFAULT_START, EntityCandidate.DEFAULT_END,
				null);
	}

	public AddressPair() {
		city = getDefaultCity();
		state = getDefaultState();
	}

	public AddressPair(EntityCandidate city, EntityCandidate state) {
		if (city != null) {
			this.city = city;
		} else {
			this.city = getDefaultCity();
		}

		if (state != null) {
			this.state = state;
		} else {
			this.state = getDefaultState();
		}
	}

	public AddressPair(String city, String state) {
		this();
		this.city.name = city;
		this.state.name = state;
	}

	public AddressPair(String city, String state, int cityStart, int cityEnd,
			int stateStart, int stateEnd) {
		this.city = new EntityCandidate(city, cityStart, cityEnd, null);
		this.state = new EntityCandidate(state, stateStart, stateEnd, null);
	}

	public String toString() {
		return city.name + "," + state.name;
	}

	/**
	 * Check whether two AddressPairs intersect with each other.
	 * 
	 * @param p
	 * @return
	 */
	public boolean intersect(AddressPair p) {
		if (this.city.intersect(p.city) || this.city.intersect(p.state)
				|| this.state.intersect(p.city)
				|| this.state.intersect(p.state))
			return true;
		return false;
	}

	/**
	 * Convert the pair to the normalized address string.
	 * 
	 * @param stateShortToLong
	 * @return the normalized string
	 */
	public String toResultString(HashMap<String, String> stateShortToLong) {
		// In the case that there is no state
		if (state.name.equals(USAddressParser.NO_STATE)) {
			return Utilities.capitializeFirstLetterOfWord(city.name);
		}

		// In the case that there is no city
		if (city.name.equals(USAddressParser.NO_CITY)) {
			return Utilities.capitializeFirstLetterOfWord(stateShortToLong
					.get(state.name));
		}

		// In all other cases
		return Utilities.capitializeFirstLetterOfWord(city.name) + ", "
				+ state.name.toUpperCase();
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

		int cityDiff = (o.city.endIndex - o.city.startIndex)
				- (city.endIndex - city.startIndex);
		if (cityDiff != 0) {
			return cityDiff;
		}
		return (o.state.endIndex - o.state.startIndex)
				- (o.state.endIndex - o.state.startIndex);
	}
}
