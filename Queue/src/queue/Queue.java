package queue;

public interface Queue {

    /*
     * Pre: element != null element 
     * Post: queue immutable && size' = size + 1
     * && last element queue = element
     */  
    void enqueue(Object element);

    /*
     * Pre: size > 0
     * Post: R = first element queue && queue immutable
     */
    Object element();

    /*
     * Pre: size > 0
     * Post: R = first element queue && size' = size - 1
     * && other elements immutable
     */
    Object dequeue();

    /*
     * Pre: true
     * Post: R = size && queue immutable
     */
    int size();

    /*
     * Pre: true
     * Post: R = size == 0 && queue immutable
     */
    boolean isEmpty();

    /*
     * Pre: true
     * Post: size' = 0
     */
    void clear();

    /*
     * Pre: true
     * Post: R = queue[0..size - 1] && queue immutable
     */
    Object[] toArray();
}
