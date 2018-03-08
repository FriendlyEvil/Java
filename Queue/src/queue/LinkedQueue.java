package queue;

public class LinkedQueue extends AbstractQueue {
    private Node tail;
    private Node head;

    protected void enqueueRealisation(Object element) {
        tail = new Node(element, null, tail);
        if (head == null) {
            head = tail;
        } else {
            tail.next.prev = tail;
        }
    }

    protected Object elementRealisation() {
        return head.value;
    }

    protected void dequeueRealisation() {
        if (head.prev != null) {
            head.prev.next = null;
        }
        head = head.prev;

    }

    protected void clearRealisation() {
        tail = null;
        head = null;
    }

    private class Node {
        Object value;
        Node prev;
        Node next;

        Node(Object element, Node prev, Node next) {
            value = element;
            this.prev = prev;
            this.next = next;
        }
    }
}
