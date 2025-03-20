/**
 * Class to create a priority queue
 * @author Danry Yuan
 */
public class ArrayUniquePriorityQueue<T> implements UniquePriorityQueueADT<T>{
    private T[] queue;
    private double[] priority;
    private int count;

    /**
     * Private helper method to check whether the queue is full
     * @return boolean on whether the queue is full or not 
     */
    private boolean isFull() {
        if (count == queue.length) return true;
        return false;
    }

    /**
     * Private helper method to expand the queue
     */
    private void expand() {
        T[] tempQ = (T[]) new Object[queue.length+5];
        double[] tempP = new double[queue.length+5];

        // first copy queue to tempQ
        for (int i = 0; i < queue.length; i++) {
            tempQ[i] = queue[i];
        }
        queue = tempQ;

        // now copy priority to tempP
        for (int i = 0; i < priority.length; i++) {
            tempP[i] = priority[i];
        }
        priority = tempP;
    }

    /**
     * shuffle forward the queue from a certain index
     * @param index the index to end shuffling from
     */
    private void shuffleForward (int index) {
        for (int j = queue.length-1; j > index; j--) {
            priority[j] = priority[j-1];
        }
        for (int j = queue.length-1; j > index; j--) {
            queue[j] = queue[j-1];
        }
    }

    /**
     * shuffle the queue backwards from a certain index
     * @param index the index to start shuffling from
     */
    private void shuffleBack (int index) {
        for (int j = index; j < count-1; j++) {
            priority[j] = priority[j+1];
        }
        for (int j = index; j < count-1; j++) {
            queue[j] = queue[j+1];
        }
    }

    /**
     * Constructor
     */
    public ArrayUniquePriorityQueue (){
        queue = (T[])new Object[10];
        priority = new double[10];
        count = 0;
    }

    /**
     * add a generic item to the queue along with its priority
     * @param data the data item to put in queue
     * @param prio the priority of the data
     */
    public void add (T data, double prio) {
        if (!contains(data)) {

            if (isFull()) expand();

            for (int i = 0; i <= count; i++) {
                if (priority[i] > prio) {
                    shuffleForward(i);
                    priority[i] = prio;
                    queue[i] = data;
                    count++;
                    break;
                } else if (i == count) {
                    priority[i] = prio;
                    queue[i] = data;
                    count++;
                    break;
                }
            }
        }
    }

    /**
     * check if the queue contains a certain data item
     * @param data generic data to check if in the queue
     * @return boolean on whether the item exists in the queue or not 
     */
    public boolean contains (T data) {
        for (int i=0; i<count; i++) {
            if (queue[i].equals(data)) return true;
        }
        return false;
    }

    /**
     * check the front of the queue
     * @return generic item stored at the front of the queue
     */
    public T peek () throws CollectionException {
        if (count ==0) {
            throw new CollectionException("PQ is empty");
        }
        return queue[0];
    }

    /**
     * remove the item with the minimum priority/front of queue and return it
     * @return generic data item at the front of the queue/minimum priority
     */
    public T removeMin () throws CollectionException {
        if (count==0) throw new CollectionException("PQ is empty");
        T temp = queue[0];
        shuffleBack(0);
        count--;
        return temp;
    }

    /**
     * update the priority of a data item
     * @param data data item to switch priority for
     * @param newPrio new priority to change the priority of the data item for
     */
    public void updatePriority (T data, double newPrio) throws CollectionException {
        T temp;
        boolean exists = false;
        for (int i = 0; i<count; i++) {
            if (queue[i].equals(data)){
                temp = data;
                shuffleBack(i);
                count--;
                add(temp, newPrio);
                exists = true;
            }
        }
        if (!exists) throw new CollectionException("Item not found in PQ");
    }

    /**
     * check if the queue is empty
     * @return boolean on whether the queue is empty or not
     */
    public boolean isEmpty () {
        if (count==0) return true;
        else return false;
    }

    /**
     * check size of the queue
     * @return int of the size of the queue
     */
    public int size () {
        return count;
    }

    /**
     * check length of the queue
     * @return size
     */
    public int getLength () {
        int size = 0;
        double test;
        while (true) {
            try{
                test = priority[size];
                size++;
            } catch (ArrayIndexOutOfBoundsException e) {
                return size;
            }

        }
    }

    public String toString () {
        if (isEmpty()) return "The PQ is empty";
        String msg = "";
        for (int i = 0; i < size(); i++) {
            msg += queue[i].toString() + " [";
            msg += priority[i] + "]";
            if (i != size()-1) msg += ", ";
        }
        return msg;
    }
}
