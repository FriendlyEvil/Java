package test;

import queue.LinkedQueue;

public class LinkedQueueTest {
    public static void main(String[] args) {
        LinkedQueue queue = new LinkedQueue();
        fill(queue);
        dump(queue);
        fill(queue);
        clear(queue);
        fill(queue);
        toArray(queue);
    }

    public static void fill(LinkedQueue queue) {
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
    }

    public static void dump(LinkedQueue queue) {
        while (!queue.isEmpty()) {
                System.out.println("Queue " + queue.size() + " " + queue.element() + " " + queue.dequeue());
        }
    }

    public static void clear(LinkedQueue queue) {
        queue.clear();
        System.out.println(queue.isEmpty());
    }

    public static void toArray(LinkedQueue queue) {
        for (Object o : queue.toArray()) {
            System.out.print(o + " ");
        }
    }
}
