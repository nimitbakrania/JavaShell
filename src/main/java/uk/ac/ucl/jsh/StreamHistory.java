package uk.ac.ucl.jsh;

import java.util.LinkedList;

public class StreamHistory<T> {

    private final int numberOfElementsToRemember;
    private LinkedList<T> queue = new LinkedList<T>(); // queue will store at most numberOfElementsToRemember

    public StreamHistory(int numberOfElementsToRemember) {
        this.numberOfElementsToRemember = numberOfElementsToRemember;
    }

    public StreamHistory save(T curElem) {

        if (queue.size() == numberOfElementsToRemember) {
            queue.pollLast(); // remove last to keep only requested number of elements
        }

        queue.offerFirst(curElem);

        return this;
    }


    public LinkedList<T> getLastElements() {
        return queue; // or return immutable copy or immutable view on the queue. Depends on what you want.
    }
}