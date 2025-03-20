/**
 * Class which creates double linked lists of numbers in a base 2-16 objects with built-in methods to convert bases 
 * @author Danry Yuan 
 */
public class LinkedNumber {
	private int base;
	private DLNode<Digit> front;
	private DLNode<Digit> rear;

	/**
	 * Private helper method to create a list out of input number String. 
	 * @param num String of number to be made into list
	 */
	private void constructorHelper (String num) {
		if (num.isEmpty()) {
			throw new LinkedNumberException("no digits given");
		}
		
		char[] characters = num.toUpperCase().toCharArray();
		Digit tempDigit = new Digit(characters[0]); // start from first digit
		DLNode<Digit> temp = new DLNode<Digit>(); 
		front = new DLNode<Digit>(tempDigit); // first front
		rear = front; // only one node so front and rear point to this object
		front.setPrev(null); // no prev so set null and the node constructor by default sets next as null
		
		if (characters.length > 1) { // if more than one digit

			tempDigit = new Digit(characters[1]); // second digit
			temp.setElement(tempDigit); // make temp element second digit
			front.setNext(temp); 
			temp.setPrev(front); // link the temp and front
			rear = temp; // change rear pointer

			for (int digit=2; digit<characters.length;digit++) { // start from third digit
				tempDigit = new Digit(characters[digit]); // have a pointer to current digit
				temp.setNext(new DLNode<>(tempDigit)); // create new node next of temp
				temp = temp.getNext(); // have temp point to this new node
				temp.setPrev(rear); // link new node temp to the previous temp/rear
				rear = temp; // have rear point to this new node
				// repeat
			}
		} 
	}

	/**
	 * get the value of a number in the old base in decimal
	 * @param num
	 * @param oldBase
	 * @return
	 */
	private int value (char [] num, int oldBase) {
		int value = 0, counter = 0;
		for (int i = num.length-1; i >= 0; i--) {
			value += Character.getNumericValue(num[i]) * (Math.pow(oldBase, counter));
			counter++;
		}
		return value;
	}

	/**
	 * convert from decimal to other base
	 * @param newBase
	 * @param num 
	 * @return
	 */
	private String decimalToNonDecimal (int newBase, char[] num) {
		int current, value;
		char digit = ' ';
		String number = "";
		value = value(num, 10);

		while (value > 0) {
			current = value%newBase;
			
			if (current > 9) { // nondecimal digit
				switch (current) {
					case 10:
						digit = 'A';
						break;
					case 11:
						digit = 'B';
						break;
					case 12:
						digit = 'C';
						break;
					case 13:
						digit = 'D';
						break;
					case 14:
						digit = 'E';
						break;
					case 15:
						digit = 'F';
						break;
				}

			} else digit = (char)(current + '0'); // char is a stinky data type, to convert int to char not only do I have to typecast I even have to add the value of 0 to get a proper number as a char

			number = String.valueOf(digit) + number;
			value /= newBase;
		}

		return number;
	}
	
	/**
	 * convert nondecimal to decimal
	 * @return the decimal number as a string
	 */
	private String nonDecimalToDecimal () {
		char[] num = toString().toCharArray();

		return String.valueOf(value(num, base));
	}
	
	/**
	 * convert nondecimal to nondecimal
	 * @param newBase int which is the new base the number should be converted to
	 * @return string form of the new number in the new base
	 */
	private String nonDecimalToNonDecimal (int newBase) {
		String convert1, convert2;
		char [] newNum;

		convert1 = nonDecimalToDecimal();
		newNum = convert1.toCharArray();
		convert2 = decimalToNonDecimal(newBase, newNum);

		return convert2;
	}
	/**
	 * non-decimal number list constructor
	 * @param num number to be turned into list
	 * @param baseNum non-decimal base
	 */
	public LinkedNumber (String num, int baseNum) {
		base = baseNum;
		constructorHelper(num);
	}

	/**
	 * decimal number list constructor
	 * @param num number to be turned into list
	 */
	public LinkedNumber(int num) {
		this(String.valueOf(num), 10);
	}

	/**
	 * check whether the number in the list is valid for the base
	 * @return boolean whether the number in the list is valid for the base or not
	 */
	public boolean isValidNumber(){
		DLNode<Digit> current;
		int temp;
		current = rear;
		if (current == null) return false; // rear being null means there is nothing in the number
		while (current != null) { 
			temp = current.getElement().getValue();
			if (temp < 0) return false;
			if (temp < base) { // a valid digit for the base will always be less than the base itself since it starts count from 0. i.e. binary which is base 2 only has 1 and 0, both < 2
				current = current.getPrev(); // start from rear so go backwards (order honestly does not matter)
			} else return false;
		}
		return true;
	}

	/**
	 * getter for base
	 * @return base
	 */
	public int getBase() {
		return base;
	}

	/** 
	 * getter for front
	 * @return front frontmost DLNode in list
	 */
	public DLNode<Digit> getFront() {
		return front;
	}

	/**
	 * getter for rear
	 * @return rear rearmost DLNode in list
	 */
	public DLNode<Digit> getRear() {
		return rear;
	}
	
	/**
	 * getter for number of digits in list
	 * @return counter counts digits
	 */
	public int getNumDigits() {
		int counter = 0;
		DLNode<Digit> current = front;

		while (current != null){
			current = current.getNext();
			counter++;
		}
		return counter;
	}

	/**
	 * convert the linked number to string
	 * @return string form of the linked list
	 */
	public String toString() {
		DLNode<Digit> current = front;
		String message = "";

		while (current != null) {
			message += current.getElement().toString();
			current = current.getNext();
		}
		return message;
	}

	/**
	 * check if two linked numbers are equal
	 * @param other other linkednumber object
	 * @return boolean of whether the two linked numbers are equal
	 */
	public boolean equals (LinkedNumber other){
		DLNode<Digit> currentThis = front;
		DLNode<Digit> currentOther = other.getFront();

		if (base == other.getBase()) { // check bases

			if (rear.getElement().getValue() == other.getRear().getElement().getValue()){ // check rears

			while (currentThis != rear||currentOther!=other.getRear()){ // loop until reach the rear
				if (currentThis.getElement().getValue() == currentOther.getElement().getValue()) { // if the elements of both lists in this position are equal
					currentThis = currentThis.getNext(); // move forward on this list
					currentOther = currentOther.getNext(); // move forward on other list

				} else return false; // element of both lists in this position are not equal therefore false
			}

		} else return false; // rears aren't equal, false

		} else return false; // base aren't equal, false

		return true; // no return false statement was triggered until now, must be true
	}
	/**
	 * create another linked number list which is this linked list converted to another base
	 * @param newBase int of the base of the new linked number list
	 * @return newList linked number list with the new base.
	 */
	public LinkedNumber convert (int newBase) {   
		LinkedNumber newList = null;
		char [] num = toString().toCharArray();

		if (isValidNumber()) {

			if (base == 10) 
				newList = new LinkedNumber(decimalToNonDecimal(newBase, num), newBase);
			if (newBase == 10)
				newList = new LinkedNumber(nonDecimalToDecimal(), newBase);
			if (newBase != 10&&base != 10) 
				newList = new LinkedNumber(nonDecimalToNonDecimal(newBase), newBase);
			
			} else throw new LinkedNumberException("cannot convert invalid number");

		return newList;
	}

	/**
	 * add a digit to a specified position
	 * @param digit Digit to be input to list
	 * @param position int position from rear=0 to add a number, the number in that position is pushed back
	 */
	public void addDigit (Digit digit, int position){
		DLNode<Digit> add;
		int length = getNumDigits();

		if (position >= 0&&position <= length) {

			DLNode<Digit> current=rear;

			if (current == null){ // empty list
				constructorHelper(digit.toString());
			}

			else { 
				add = new DLNode<Digit>(digit);

				if (position==0) { // new rear
					rear.setNext(add);
					add.setPrev(rear);
					rear = add;
				} else {
					if (position==length){ // new front
						front.setPrev(add);
						add.setNext(front);
						add.setPrev(null);
						front = add;
					} else { // middle of list
						for (int i = position-1; i>0; i--){
							current = current.getPrev(); 
						}
						add.setPrev(current.getPrev());
						current.getPrev().setNext(add);
						current.setPrev(add);
						add.setNext(current);
					}
				}
		}
	} else throw new LinkedNumberException("invalid position");
	}

	/**
	 * remove a digit from the specified position
	 * @param position int position from rear=0 to remove a number
	 * @return the removed Digit
	 */
	public int removeDigit(int position) {
		int value, length = getNumDigits();
		DLNode<Digit> current=rear;

		if (position >= 0&&position < length) { // valid position
			if (length == 1){ // empty
				rear = null;
				front = null;
			}
			else if (position==0) { // rear
				current = rear;
				rear = rear.getPrev();
				rear.setNext(null);
			} else {
				if (position==length-1){ // front
					current = front;
					front = front.getNext();
					front.setPrev(null);
				} else { // middle of list
					for (int i = position-1; i>=0; i--){
						current = current.getPrev(); 
					}
					current.getPrev().setNext(current.getNext());
					current.getNext().setPrev(current.getPrev());
				}
			}
				
	} else throw new LinkedNumberException("invalid position");

	value = current.getElement().getValue() * (int)Math.pow(base, position);
	return value;
}
}
