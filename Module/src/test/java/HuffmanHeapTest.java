import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class HuffmanHeapTest {
    @Test
    public void dumbHeapTest() {
        Random rand = new Random("Henry Mitchell".hashCode());
        HQueue testQueue = new HuffmanArrayHeap();
        for (int i = 0; i < 1000000; i++) {
            testQueue.enqueue(new HuffmanLeaf(rand.nextInt(256), rand.nextInt(256)));
        }
        assertFalse(testQueue.isEmpty());
        while (testQueue.count() > 1) {
            assertTrue(testQueue.dequeue().compareTo(testQueue.peek()) <= 0,Integer.toString(testQueue.count()));
        }
        testQueue.dequeue();
        assertTrue(testQueue.isEmpty());
        assertThrows(NoSuchElementException.class, testQueue::peek);
        assertThrows(NoSuchElementException.class, testQueue::dequeue);
    }
}
