package queue;

public class ArrayQueue extends AbstractQueue {
    private Object elements[] = new Object[16];
    private int head = 0;
    private int tail = 0;

    private void ensureCapacity(int size) {
        if (size < elements.length && (size > elements.length / 4 || size < 4)) {
            return;
        }
        Object[] newElements;
        if (size >= elements.length) {
            newElements = new Object[elements.length * 2];
        } else {
            newElements = new Object[elements.length / 2];
        }
        if (head > tail) {
            System.arraycopy(elements, head, newElements, 0, elements.length - head);
            System.arraycopy(elements, 0, newElements, elements.length - head, tail);
        } else {
            System.arraycopy(elements, head, newElements, 0, size());
        }
        tail = size();
        head = 0;
        elements = newElements;
    }

    protected void enqueueRealisation(Object element) {
        ensureCapacity(size + 1);
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
    }

    public Object elementRealisation() {
        return elements[head];
    }

    protected void dequeueRealisation() {
        elements[head] = null;
        head = (head + 1) % elements.length;
    }

    protected void clearRealisation() {
        elements = new Object[16];
        head = 0;
        tail = 0;
    }
}
