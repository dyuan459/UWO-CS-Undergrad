/**
 * This class represents a grid of letters to be crushed.
 * @author danry
 */
public class LetterCrush {
	private char[][] grid;
	public static final char EMPTY=' ';
	/**
	 * private help to convert a grid to message
	 * @param messageGrid grid to be converted to message
	 * @return string form of grid
	 */
	private String stringMessage(char[][]messageGrid) {
		int i, s;
		String message = "";
		int end=messageGrid[0].length;
		for (i=0;i<messageGrid.length;i++) {
			if (i >= 1) {
				message+="|";
			}
			for (s=0;s<end;s++){
				message += messageGrid[i][s];
				}
			message+="|"+i+"\n";
		}
		message+="+";
		for (i=0;i<end;i++) {
			message+=i;
		}
		message+="+";
		return message;
	}
	/**
	 * Private helper method to compare an old character and a character at a specified place in grid to check if there is a line of the same characters.
	 * @param row specified row
	 * @param col specified column
	 * @param longest current longest row 
	 * @param length current length of line of the same characters
	 * @param compareOld last character to compare to
	 * @param end end of line of same characters
	 * @param isHorizontal boolean on whether comparing vertically or horizontally right now
	 * @return Object array of useful data
	 */
	private Object[] comparison(int row, int col, int longest, int length, char compareOld, int end, boolean isHorizontal) {
		char compareNew;
		Object[] compare = new Object[4];
		compareNew = grid[row][col];
		if (compareNew == compareOld&&compareOld!=EMPTY) {
			length++;
		} else {
			compareOld = compareNew;
			length = 1;
		}
		if (length >=3&&length>=longest) {
			if (longest <= length) {
				longest = length;
				if (isHorizontal) end = col;
				else end = row;
			}
		}
		compare[0] = longest;
		compare[1] = end;
		compare[2] = length;
		compare[3] = compareOld;
		return compare;
	}
	
	/**
	 * Constructor
	 * @param width width of character grid
	 * @param height height of character grid
	 * @param initial initial string to be input to grid
	 */
	public LetterCrush(int width, int height, String initial) {
		int row;
		int col;
		int charPosition=0;
		grid = new char[height][width];
			for (row=0; row<height;row++) {
				for (col=0;col<width;col++) { // comb through initial and enter in the characters at charPosition to the grid first left to right, top to bottom
					if (charPosition>=initial.length()) { 
						grid[row][col] = EMPTY;
					}
					else {
					grid[row][col] = initial.charAt(charPosition);
					charPosition++;}
				}
			}
		}
	
	/**
	 * make grid into String
	 * @return String form of grid
	 */
	public String toString() {
		String message = "LetterCrush\n|";
		message += stringMessage(grid);
		return message;
	}
	
	/**
	 * check if grid is stable
	 * @return boolean of whether the grid is stable
	 */
	public boolean isStable() {
		int row;
		int col;
			for (row=grid.length-1; row>0;row--) {
				for (col=0;col<grid[0].length;col++) {
					if ((grid[row-1][col]!=EMPTY)&&(grid[row][col]==EMPTY)) { // -1 means 1 row up
						return false;
					}
				}
			}
			return true;
	}
	
	/**
	 * simulate gravity, characters with EMPTY beneath should go down
	 */
	public void applyGravity() {
		int row, col;
		for (row=grid.length-1; row>0;row--) { // top to bottom
			for (col=0;col<grid[0].length;col++) {
				if (grid[row-1][col]!=EMPTY&&grid[row][col]==EMPTY) {
					grid[row][col] = grid[row-1][col];
					grid[row-1][col] = EMPTY;
				}
			}
		}
	}
	
	/**
	 * remove a Line of characters from grid
	 * @param theLine Line to remove from grid
	 * @return boolean on whether a line was valid and removed, true, or not, false
	 */
	public boolean remove(Line theLine) {
		int i;
		int rowOrCol = 0;
		int length = theLine.length();
		int[] start = theLine.getStart(); 
		
		if (theLine.isHorizontal()) {
			rowOrCol = 1; // 0 for vertical or columns
			if ((start[1]+length-1 > grid[0].length)||(start[0]>grid.length)) { // is the horizontal line longer or shorter than the grid? 
				return false; // if so invalid
			}
		}
		else {
			if ((start[0]+length-1 > grid.length)||(start[1]>grid[0].length)) { // same as above but for vertical
				return false;
		}
		}
		int end = start[rowOrCol] + length-1;
		for (i=start[rowOrCol]; i<=end;i++) {
			if (rowOrCol == 1) {
				grid[start[0]][i] = EMPTY;
			}
			else {
				grid[i][start[1]] = EMPTY;
				}
			}
		return true;
	}
	
	/**
	 * overload toString, change grid to String but with characters in a certain line lowered
	 * @param theLine line of characters to be lowered
	 * @return String form of grid
	 */
	public String toString(Line theLine) {
		int row, col;
		char temp;
		char[][] tempGrid = new char[grid.length][grid[0].length];
		String message = "CrushLine\n|";
		for (row=0; row<grid.length;row++) {
			for (col=0;col<grid[0].length;col++) {
				if (theLine.inLine(row, col)){
					temp = Character.toLowerCase(grid[row][col]);
				} else temp = grid[row][col];
				tempGrid[row][col] = temp;
			}
		}
		message += stringMessage(tempGrid);
		return message;
	}
	
	/**
	 * look for longest line
	 * @return either null if no valid line or a Line of 3+ same characters on the grid
	 */
	public Line longestLine() {
		int row, col;
		int end=0, lengthiest=0, length=1;
		Object[] comparison;
		char compareOld = ' ';
		Line longest=new Line(0,0,true,1);
		
		//vertical check
		for (col=0;col<grid[0].length-1;col++) {
			for (row=grid.length-1; row>=0;row--) {
				comparison = comparison(row, col, lengthiest, length, compareOld, end, false); //same for horizontal
				lengthiest = (int)comparison[0];
				end = (int)comparison[1];
				length = (int)comparison[2];
				compareOld = (char)comparison[3];
				if (length >= 3&&length >= lengthiest) {
					longest = new Line(end, col, false, length);
				}
			}
			compareOld = ' ';
		}
		//horizontal check
		for (row=0; row<grid.length; row++) {
			for (col=grid[0].length-1; col>=0; col--) {
				comparison = comparison(row, col, lengthiest, length, compareOld, end, true);
				lengthiest = (int)comparison[0];
				end = (int)comparison[1];
				length = (int)comparison[2];
				compareOld = (char)comparison[3];
				if ((length >= 3)&&(length >= lengthiest)) {
					longest = new Line(row, end, true, length);
				}
			}
			compareOld = ' ';
		}
		if (lengthiest == 0) { // 0 means initialized meaning no longest line
			return null;
		} else return longest;
	}
	
	/**
	 * delete Lines of 3+ of the same characters, then apply gravity until stable then look for next 3+ line until none is found and it is null
	 */
	public void cascade() {
		Line longest=longestLine();
		while (longest!=null) {
			
			remove(longest);
			while (isStable() == false) {
				applyGravity();
			}
			longest = longestLine();
		}
	}
}
