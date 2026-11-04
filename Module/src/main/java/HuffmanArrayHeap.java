import java.util.NoSuchElementException;

public class HuffmanArrayHeap implements HQueue {

    private HuffmanTree[] elements;
    private int size;

    public HuffmanArrayHeap() {
        elements = new HuffmanTree[10];
        size = 0;
    }

    /**
     * return true if the queue is empty
     */
    @Override
    public boolean isEmpty() {
        return size==0;
    }

    /**
     * return a count of the number of elements in the queue
     */
    @Override
    public int count() {
        return size;
    }

    /**
     * enqueue a tree
     */
    @Override
    public void enqueue(HuffmanTree tree) {
        ensureCapacity();
        elements[size++] = tree;
        siftUp(size);
    }

    /**
     * dequeue and return the highest-priority tree
     */
    @Override
    public HuffmanTree dequeue() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        HuffmanTree result = elements[0];
        elements[0] = elements[--size];
        siftDown(1);
        return result;
    }

    /**
     * ensures that there is space left in the backing array and doubles its size if there isn't
     */
    private void ensureCapacity() {
        if (size==elements.length) {
            HuffmanTree[] newElements = new HuffmanTree[elements.length*2];
            for (int i = 0; i < elements.length; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    /**
     * sifts an element up the heap until its parent is smaller
     */
    private void siftUp(int index) {
        int parentIdx = index/2;
        if(parentIdx > 0 && elements[parentIdx-1].compareTo(elements[index-1]) > 0) {
            HuffmanTree temp = elements[index-1];
            elements[index-1] = elements[parentIdx-1];
            elements[parentIdx-1] = temp;
            siftUp(parentIdx);
        }

    }

    /**
     * sifts an element down the heap until both of its children are larger
     */
    private void siftDown(int index) {
        int leftIdx = index*2;
        int rightIdx = leftIdx+1;

        if((leftIdx > size || elements[index-1].compareTo(elements[leftIdx-1]) < 0) &&
            (rightIdx > size || elements[index-1].compareTo(elements[rightIdx-1]) < 0)) {
            return;
        }
        if(elements[leftIdx-1].compareTo(elements[rightIdx-1]) <= 0) {
            HuffmanTree temp = elements[leftIdx-1];
            elements[leftIdx-1] = elements[index-1];
            elements[index-1] = temp;
            siftDown(leftIdx);
        }
        else {
            HuffmanTree temp = elements[rightIdx-1];
            elements[rightIdx-1] = elements[index-1];
            elements[index-1] = temp;
            siftDown(rightIdx);
        }
    }

    /**
     * returns but does not remove the smallest element in the heap
     */
    public HuffmanTree peek() {
        if(isEmpty()) throw new NoSuchElementException();
        return elements[0];
    }
}
