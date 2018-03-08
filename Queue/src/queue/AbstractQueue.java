package queue;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    protected abstract void enqueueRealisation(Object element);

    protected abstract Object elementRealisation();

    protected abstract void dequeueRealisation();

    protected abstract void clearRealisation();

    public void enqueue(Object element) {
        assert element != null;
        enqueueRealisation(element);
        size++;
    }

    public Object element() {
        assert size > 0;
        return elementRealisation();
    }

    public Object dequeue() {
        assert size > 0;
        Object result = elementRealisation();
        dequeueRealisation();
        size--;
        return result;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        clearRealisation();
        size = 0;
    }

    public Object[] toArray() {
        Object[] res = new Object[size];
        for (int i = 0; i < size; i++) {
            res[i] = dequeue();
            enqueue(res[i]);
        }
        return res;
    }
}
