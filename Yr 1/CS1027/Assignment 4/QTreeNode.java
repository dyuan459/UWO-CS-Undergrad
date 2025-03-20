/**
 * class to represent a node in a quadrant tree
 * @author danry yuan
 */
public class QTreeNode {
	private int x, y;
	private int size;
	private int color;
	private QTreeNode parent;
	private QTreeNode[] children;
	
	/**
	 * private helper method to quickly check if index is valid
	 * @param index index to check 
	 * @throws QTreeException uhoh it's wrong
	 */
	private void checkIndex (int index) throws QTreeException{
		if (children == null || index < 0 || index > 3) throw new QTreeException("");
	}
	
	/**
	 * Empty constructor
	 */
	public QTreeNode() {
		parent = null;
		children = new QTreeNode[4];
		x=0; y=0; size=0; color=0;
	}
	
	/**
	 * filled out constructor 
	 * @param theChildren children of this node
	 * @param xcoord left corner of quadrant
	 * @param ycoord top corner of quadrant
	 * @param theSize size of quadrant
	 * @param theColor color in quadrant
	 */
	public QTreeNode(QTreeNode[] theChildren, int xcoord, int ycoord, int theSize, int theColor) {
		children = theChildren;
		if (children != null) for (int i = 0; i < 4; i++) children[i].setParent(this);
		x = xcoord; y = ycoord; size = theSize; color = theColor;
		parent = null;
	}
	
	/**
	 * check if the quadrant contains this x or y coord
	 * @param xcoord x to check
	 * @param ycoord y to check
	 * @return boolean on whether the x and y are contained
	 */
	public boolean contains(int xcoord, int ycoord) {
		if (xcoord < 0 || ycoord < 0) return false;
		if (xcoord < x + size && ycoord < y + size) return true; 
		return false;
	}
	
	/**
	 * getters
	 * @return what the user wants to get
	 */
	public int getx() {
		return x;
	}
	public int gety() {
		return y;
	}
	public int getSize() {
		return size;
	}
	public int getColor() {
		return color;
	}
	public QTreeNode getParent() {
		return parent;
	}
	public QTreeNode getChild(int index) throws QTreeException {
		checkIndex(index);
		return children[index];
	}
	
	/**
	 * setters
	 * @param what is being set
	 */
	public void setx (int newx) {
		x = newx;
	}
	public void sety (int newy) {
		y = newy;
	}
	public void setSize (int newSize) {
		y = newSize;
	}
	public void setColor (int newColor) {
		y = newColor;
	}
	public void setParent(QTreeNode newParent) {
		parent = newParent;
	}
	public void setChild(QTreeNode newChild, int index) {
		checkIndex(index);
		children[index] = newChild;
	}
	
	/**
	 * check if this node is a leaf
	 * @return boolean on whether it is a leaf or not
	 */
	public boolean isLeaf() {
		if (children == null) return true;
		for (int i = 0; i < 4; i++) {
			if (children[i] != null) break;
			if (i == 3) return true;
		}
		return false;
	}
}
