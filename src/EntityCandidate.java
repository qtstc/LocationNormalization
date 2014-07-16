import java.util.ArrayList;

/**
 * Data structure to store the possible entities in a list of tokens.
 * @author Tao Qian
 *
 */
public class EntityCandidate implements Comparable<EntityCandidate> {
	
	// Parameters are supposed to be implemented as private with get/set
	// but will skip that for now.
	
	// Name of the entity candidate
	String name;
	
	// The starting index in the tokens list (inclusive)
	int startIndex;
	
	// The ending index in the tokens list (inclusive)
	int endIndex;
	
	// Possible matches for this entity
	// In our case, if the entity is a state, the matches will be cities
	// if the entity is a city, the matches will be states.
	ArrayList<String> matches;

	// The difference between default start and default end
	// is -1 (which is not a valid length)
	public static final int DEFAULT_START = -1;
	public static final int DEFAULT_END = -2;

	public EntityCandidate(String name, int startIndex, int endIndex,
			ArrayList<String> possibleMatches) {
		this.name = name;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.matches = possibleMatches;
	}

	/**
	 * Check whether this entity intersects with
	 * another entity from the same list tokens.
	 * By intersect, it means they share some tokens
	 * in the list
	 * 
	 * @param otherEntity 
	 * @return true if they intersects
	 */
	boolean intersect(EntityCandidate otherEntity) {
		if (endIndex < otherEntity.startIndex) {
			return false;
		}
		if (startIndex > otherEntity.endIndex) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(EntityCandidate o) {
		// Simple comparison based on the length of the tokens actually.
		// It is a very naive way to evaluate how good a candidate is.
		// The idea is that if a entity used more information from
		// the list of tokens, it better represents the list of tokens.
		return (o.endIndex - o.startIndex) - (endIndex - startIndex);
	}
}