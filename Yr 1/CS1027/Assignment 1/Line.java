/**
This class represents a line
@author Danry Yuan*/
public class Line {
	private int[] start=new int[2], end=new int[2];
	/**
	 * 
	 * @param row row the line starts on
	 * @param col column the line starts on
	 * @param horizontal true or false on whether the line is horizontal
	 * @param length how long is the line
	 */
	public Line (int row, int col, boolean horizontal, int length) {
		if (horizontal) { 
			start[0] = row;
			start[1] = col;
			end[0] = row; //note how row ends the same as where it starts
			end[1] = col + length-1; //only column of a horizontal line changes with length
		}
		else {
			start[0] = row;
			start[1] = col;
			end[0] = row + length-1; //only row of a vertical line changes with length
			end[1] = col;
		}
	}
	/**
	 * 
	 * @return array of starting [0]row and [1]column
	 */
	public int[] getStart() {
		return start;
	}
	/**
	 * 
	 * @return length of line
	 */
	public int length() {
		int length;
		if (start[0]==end[0]) { //check if horizontal
			length = end[1]-start[1];
		}
		else {
			length = end[0]-start[0];
		}
		return length+1; // just algebra how I set up ends, shift start[0/1] in place of row/col and -1 to left 
	}
	/**
	 * 
	 * @return true or false on whether the line is horizontal
	 */
	public boolean isHorizontal() {
		boolean isHorizontal;
		if (start[0]==end[0]) {
			isHorizontal = true;
		}
		else isHorizontal = false;
		return isHorizontal;
	}
	/**
	 * Check if a point is on the line
	 * @param row input row to test for in the line
	 * @param col input column to test for in the line
	 * @return whether the input is on the line [true] or not [false]
	 */
	public boolean inLine(int row, int col) {
		if (isHorizontal()) {
			if( (start[1] <= col)&&(col <= end[1])) {//horizontal means only column changes so check if input column is between start and end of column
				if (start[0] == row) {//then just check if input is in same row as line
					return true;
				}
			}
		}
		else { // check vertical lines
			if( (start[0] <= row)&&(row <= end[0])) {
				if (start[1] == col) {
					return true;
				}
			}
		}
		return false;
}
	/**
	 * @return the line in string form with the format: "Line: [start row, start column]->[end row, end column]"
	 */
	public String toString() {
		String message = "Line:[" + start[0] + "," + start[1] + "]->["+end[0]+","+end[1]+"]";
		return message;
	}
	
}
