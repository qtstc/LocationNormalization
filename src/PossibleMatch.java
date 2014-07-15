import java.util.ArrayList;

public class PossibleMatch implements Comparable<PossibleMatch>
	{
		String key;
		int startIndex;
		int endIndex;
		ArrayList<String> matches;
		
		PossibleMatch(String key, int startIndex, int endIndex, ArrayList<String> possibleMatches)
		{
			this.key = key;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.matches = possibleMatches;
		}
		
		boolean intersect(PossibleMatch match)
		{
			if(endIndex < match.startIndex)
			{
				return false;
			}
			if(startIndex > match.endIndex)
			{
				return false;
			}
			return true;
		}

		@Override
		public int compareTo(PossibleMatch o) {
			return (o.endIndex - o.startIndex)-(endIndex-startIndex);
		}
}