/**
 * Class for making a froge move
 * @author Danry Yuan
 */
public class FrogPath {
	private Pond pond;

    /**
     * helper method to determine hexagon's given score/points/priority and outputs if an unexpected error occurs
     * @param hex hexagon to check
     * @return double of the input hexagon's score/points/priority and -1 means skip this hexagon since it's a danger
     */
    private double hexScore (Hexagon hex){
        if (hex.isAlligator()||hex.isMudCell()) return -1;

        for (int i = 0; i < 6; i++) {
            if (hex.isReedsCell() && hex.getNeighbour(i) != null && hex.getNeighbour(i).isAlligator()) return 10;
            if (hex.getNeighbour(i) != null && hex.getNeighbour(i).isAlligator()) return -1;
        }
        if (hex.isLilyPadCell()) return 4;
        if (hex.isReedsCell()) return 5;
        if (hex.isEnd()) return 3;
        if (hex.isWaterCell()) return 6;
        try {
        return foodType(hex);
    }
    catch (Exception e) {
        System.out.println("for some reason a cell isn't alligator, reeds near alligator, near alligator, lilypad, reeds, franny, water, nor food");
        return 999;
    }
    }

    /**
     * helper method that searches the immediate vicinity for hexagons and updates a priority queue for them
     * @param currCell current cell to search around
     * @param prioQueue priority queue to update
     * @return updated priority queue
     */
    private UniquePriorityQueueADT <Hexagon> searchImmediate(Hexagon currCell, UniquePriorityQueueADT <Hexagon> prioQueue) {
        double tempPrio;
        for (int i = 0; i < 6; i++) {
            if (currCell.getNeighbour(i) != null&&!currCell.getNeighbour(i).isMarked()) { // make sure no nullpointers happen
            tempPrio = hexScore(currCell.getNeighbour(i));
            if (tempPrio == -1) continue; // skip iteration if hazard is in checked cell
            prioQueue.add(currCell.getNeighbour(i), tempPrio);}
        }
        return prioQueue;
    }

    /**
     * helper method searches far and near hexagons and updates a priority queue with them
     * @param currCell current cell to search around
     * @param prioQueue priority queue to update with searched hexagons
     * @return updated priority queue 
     */
    private UniquePriorityQueueADT <Hexagon> searchFar(Hexagon currCell, UniquePriorityQueueADT <Hexagon> prioQueue) {
        double tempPrio;
        prioQueue = searchImmediate(currCell, prioQueue);
        for (int i = 0; i < 6; i++) {
            if (currCell.getNeighbour(i) != null && currCell.getNeighbour(i).getNeighbour(i) != null && !currCell.getNeighbour(i).getNeighbour(i).isMarked()) { // can't get neighbour of null and can't do anything with null

                tempPrio = hexScore(currCell.getNeighbour(i).getNeighbour(i));
                if (tempPrio != -1)prioQueue.add(currCell.getNeighbour(i).getNeighbour(i), tempPrio+0.5);
            }
            
            if (currCell.getNeighbour(i) != null && currCell.getNeighbour(i).getNeighbour(Math.abs((i-1)%6)) != null && !currCell.getNeighbour(i).getNeighbour(Math.abs((i-1)%6)).isMarked()) { // same here
                tempPrio = hexScore(currCell.getNeighbour(i).getNeighbour(Math.abs((i-1)%6)));
                if (tempPrio != -1) prioQueue.add(currCell.getNeighbour(i).getNeighbour(Math.abs((i-1)%6)), tempPrio+1);
            }
        }
        return prioQueue;
    }

    /**
     * helper method to get score/priority of a fly/food hexagon
     * @param hex food hexagon
     * @return score/priority of the flies in the food hexagon
     * @throws Exception something went terribly wrong
     */
    private double foodType (Hexagon hex) throws Exception {
        switch (((FoodHexagon)hex).getNumFlies()) {
            case 1: return 2;
            case 2: return 1;
            case 3: return 0;
            default: throw new Exception("no food?");
        }
    }

    /**
     * constructor
     * @param filename filename of pond to construct
     */
    public FrogPath (String filename){
        try {
        pond = new Pond(filename);
    } catch (Exception e) {
        System.out.println("no files? Q.Q");
    }
    }

    /**
     * find best hexagon to move to
     * @param currCell current cell to look around
     * @return the best hexagon to move to or null because there are no hexagons to move to
     */
    public Hexagon findBest(Hexagon currCell) {
        UniquePriorityQueueADT <Hexagon> priority = new ArrayUniquePriorityQueue<Hexagon>();
        try {
        if (currCell.isLilyPadCell()) {
            priority = searchFar(currCell, priority);
        } else searchImmediate(currCell, priority);
        }
        
        catch (Exception e) {
        	System.out.println(e.getMessage()); // something went terribly wrong
        }

        if (priority.isEmpty()) return null;
        else return priority.removeMin();
    }
	
    /**
     * find path Frank should take
     * @return string of the path Frank took or no solution if it is impossible to navigate
     */
    public String findPath() {
        ArrayStack <Hexagon> stack = new ArrayStack<Hexagon>();
        Hexagon curr, next, start = pond.getStart();
        String msg = "";
        int fliesEaten = 0;

        stack.push(start);
        start.markInStack();

        while (!stack.isEmpty()){ // main loop
            curr = (Hexagon)stack.peek();
            msg += curr.getID() + " ";
            if (curr.isEnd()) break;
            if (curr instanceof FoodHexagon) {
                fliesEaten += ((FoodHexagon)curr).getNumFlies();
                ((FoodHexagon)curr).removeFlies();
            }
            next = findBest(curr);
        if (next == null) {
            stack.pop();
            curr.markOutStack();
        } else {
            stack.push(next);
            next.markInStack();
        }
        }
        if (stack.isEmpty()) msg = "No solution"; // already backtracked all the way without finding other hexagons to go to
        else msg += "ate " + fliesEaten + " flies";
        return msg;
    }
    
    /**
     * individual testing
     * @param args for input arguments
     */
    public static void main(String[] args) {
    	FrogPath test = new FrogPath("pond9.txt");
    	System.out.println(test.findPath());
    	
    }
}
