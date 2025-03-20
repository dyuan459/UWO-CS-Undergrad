/**
 * a tree which divides an input matrix of colors in int form into quadrants into quadrants into quadrants etc...
 * @author danry yuan
 */
class QuadrantTree {
	private QTreeNode root;
	
/**
 * private method to reduce matrix for each quadrant (deprecated)
 * @param reducee the matrix to reduce
 * @param x x coord to reduce from
 * @param y y coord to reduce from 
 * @return reduced matrix
 */
//	private int[][] reducer(int [][] reducee, int x, int y) {
//		int a = 0, b = 0, size = reducee.length;
//		int[][] reduced = new int[size/2][size/2];
//		try {
//		for (int i = x; i < size; i++) {
//			for (int j = y; j < size; j++) {
//				reduced[a][b] = reducee[i][j];
//				b++;
//			}
//			b = 0;
//			a++;
//		}
//		return reduced;
//		}
//		catch (IndexOutOfBoundsException e) { // if it hits array limit then it's done
//			return reduced;
//		}
//	}
	
	/**
	 * private helper recursive method to construct tree
	 * @param x x coordinate to construct the quadrant from
	 * @param y y coordinate to construct the quadrant from
	 * @param pixels matrix of pixels
	 * @param size size of quadrant
	 * @return root of tree
	 */
	private QTreeNode constructionWorker (int x, int y, int[][] pixels, int size) {
		
		if (size == 1) return new QTreeNode(null, x, y, 1, pixels[x][y]); // base case return node with size 1 and no children
		
		QTreeNode[] children = new QTreeNode[4];
		
		// recursive cases: recurse quadrants and store each in another part of an array of these matrix quadrants
		children[0] = constructionWorker (x, y, pixels, size/2);
		children[1] = constructionWorker (x, y+(size/2), pixels, size/2);
		children[2] = constructionWorker (x+(size/2), y, pixels, size/2);
		children[3] = constructionWorker (x+(size/2), y+(size/2), pixels, size/2);
		
		// finally after recursing return a big node with children from that array
		return new QTreeNode(children, x, y, size, Gui.averageColor(pixels, x, y, size));
	}
	
	/**
	 * constructor but all it really is for is to call construction worker 
	 * @param thePixels input matrix of colors in the form of int
	 */
	public QuadrantTree(int[][]thePixels) {
		
		root = constructionWorker(0, 0, thePixels, thePixels.length);
		
	}
	
	/**
	 * gets the root
	 * @return the root
	 */
	public QTreeNode getRoot() {
		return root;
	}
	
	/**
	 * gets all the pixels on one level
	 * @param r root to find pixels from
	 * @param theLevel indicate what level of recursion the function is on
	 * @return in base case return a single listnode with the root, in recursive case return a list of nodes
	 */
	public ListNode<QTreeNode> getPixels(QTreeNode r, int theLevel) {
		
		if (theLevel == 0||r.isLeaf()) return new ListNode<QTreeNode>(r); // base case just return a listnode with the treenode
		
		ListNode<QTreeNode> node, front;
		front = getPixels(r.getChild(0), theLevel-1); // origin recursive case, recurse with the first child of param r as param
		node = front;
		
		while (node.getNext() != null) node = node.getNext(); // the origin case may return a list so loop through it
		//until the end to know where to insert more nodes from
		
		for (int i = 1; i < 4; i++) { // main recursive cases, go through the rest of this root's children
			node.setNext(getPixels(r.getChild(i), theLevel-1)); // set next from node after getting a return
			
			while (node.getNext() != null) node = node.getNext(); // loop until end of this new list since a list may 
			// be returned
		}
		return front;
	}
	
	/**
	 * find pixels with matching color
	 * @param r root to find matching color pixels from
	 * @param theColor color to match
	 * @param theLevel recursion level
	 * @return base case: duple with the param root in a listnode with a count of 1 or null if not matching. recursive case: duple with list and however much for the count
	 */
	public Duple findMatching (QTreeNode r, int theColor, int theLevel) {
		
		if (r.isLeaf()||theLevel == 0) { // base case
			if (Gui.similarColor(theColor, r.getColor())) return new Duple(new ListNode<QTreeNode>(r), 1); // check if similar color before returning or else return null
			else return null;
		}
		
		Duple oldVal, values = null;
		ListNode<QTreeNode> front = null, next = null;
		int j=0, count = 0;
		
		for (int i = 0; i < 4; i++) { // first recursive loop until the first matching is found in children or return null
			oldVal = findMatching(r.getChild(i), theColor, theLevel-1);
			if (oldVal != null) { // if it exists then save the front, prepare next for finding endpoint to insert new nodes from, and save which child has the first matching node
				front = oldVal.getFront();
				next = front;
				j = i;
				if (i != 3) { // if the first matching is found and not all children have been checked then save the count and break out of loop
					count = oldVal.getCount();
					break; 
				}
				else return oldVal; // only one match this recursive case
			}
			if (i == 3) return null; // no match this recursive case
		}
		
		if (count > 1) { // if the saved count is more than 1 then loop until end of list to start inserting from
			while (next.getNext() != null) {
				next = next.getNext();
			}
		}
		
		for (int i = j+1; i < 4; i++) { // loop through and find matching in remaining children
			values = findMatching(r.getChild(i), theColor, theLevel-1);
			
			if (values != null) { // if there is a matching add it to list
				
				next.setNext(values.getFront());
				
				while (next.getNext() != null) { 
					
					next = next.getNext();
					count++; // loop until end of list while adding up count
					
				}
			}
		}
		
		return new Duple(front, count);
	}
	
	/**
	 * find a quadrant/node with the param coords
	 * @param r root to find from
	 * @param theLevel recursion level
	 * @param x x coord to look for
	 * @param y y coord to look for
	 * @return base case: either return null if level isn't correct or return the r param if the x and y params are part of the quadrant being searched
	 */
	public QTreeNode findNode(QTreeNode r, int theLevel, int x, int y) {
		
		if (r.isLeaf()&&theLevel != 0) return null; // incorrect level
		int size = r.getSize();
		
		if (theLevel == 0) { // base case
			
			if (size == 1) {
				if (x == r.getx() && y == r.gety()) return r; // is exactly the node 
			}
			
			else if (x >= r.getx() && x < r.getx()+size && y >= r.gety() && y < r.gety()+size) return r; // is part of this quadrant
			
			return null;
		}
		
		if (x < r.getx() + (size/2) && y < r.gety() + (size/2)) return findNode(r.getChild(0), theLevel-1, x, y); // is part of quadrant
		if (x >= r.getx() + (size/2) && y < r.gety() + (size/2)) return findNode(r.getChild(1), theLevel-1, x, y);
		if (x < r.getx() + (size/2) && y >= r.gety() + (size/2)) return findNode(r.getChild(2), theLevel-1, x, y);
		if (x > r.getx() + (size/2) && y > r.gety() + (size/2)) return findNode(r.getChild(3), theLevel-1, x, y);
		return findNode(r.getChild(3), theLevel-1, x, y); // if x == r.getx, y == r.gety
	}
	
	
}
