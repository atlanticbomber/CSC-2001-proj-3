import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Starter test suite for Project 3 (Huffman Encoding/Decoding).
 * Write more tests!
 * These tests assume the test files are in the same directory as HuffmanTest.java
 */
public class HuffmanTest {

    @Test
    public void testCntFreq() throws IOException {
        int[] freqList = Huffman.cntFreq("file2.txt");
        int[] ansList = {2, 4, 8, 16, 0, 2, 0}; // counts for ascii 97..103
        for (int i = 0; i < ansList.length; i++) {
            assertEquals(ansList[i], freqList[97 + i], "mismatch at ascii " + (97 + i));
        }
    }

    @Test
    public void testComesBefore() {
        Huffman.HuffmanLeaf d = new Huffman.HuffmanLeaf(5, 'd');
        Huffman.HuffmanLeaf k = new Huffman.HuffmanLeaf(5, 'k');
        assertTrue(d.comesBefore(k));
        assertFalse(k.comesBefore(d));
    }

    @Test
    public void testCreateHeader() throws IOException {
        int[] freqList = Huffman.cntFreq("file2.txt");
        assertEquals("97 2 98 4 99 8 100 16 102 2 \n", Huffman.createHeader(freqList));
    }

    @Test
    public void test01Textfile() throws IOException {
        Huffman.huffmanEncode("file1.txt", "file1_out.txt");
        assertTrue(compareFiles("file1_out.txt", "file1_soln.txt"));
    }
    @Test
    public void test02Textfile() throws IOException {
        Huffman.huffmanEncode("file2.txt", "file2_out.txt");
        assertTrue(compareFiles("file2_out.txt", "file2_soln.txt"));
    }
    @Test
    public void test0mTextfile() throws IOException {
        Huffman.huffmanEncode("multiline.txt", "multiline_out.txt");
        assertTrue(compareFiles("multiline_out.txt", "multiline_soln.txt"));
    }
    @Test
    public void test01bTextfile() throws IOException {
        Huffman.huffmanDecode("file1_soln.txt", "file1_out.txt");
        assertTrue(compareFiles("file1_out.txt", "file1.txt"));
    }
    @Test
    public void test02bTextfile() throws IOException {
        Huffman.huffmanDecode("file2_soln.txt", "file2_out.txt");
        assertTrue(compareFiles("file2_out.txt", "file2.txt"));
    }
    @Test
    public void test0mbTextfile() throws IOException {
        Huffman.huffmanDecode("multiline_soln.txt", "multiline_out.txt");
        assertTrue(compareFiles("multiline_out.txt", "multiline.txt"));
    }
    @Test
    void oneCharTestEnc() throws IOException {
        Huffman.huffmanEncode("oneChar.txt", "oneChar_out.txt");
        assertTrue(compareFiles("oneChar_out.txt", "oneChar_soln.txt"));
    }
    @Test
    void oneCharTestDec() throws IOException {
        Huffman.huffmanDecode("oneChar_soln.txt", "oneChar_out.txt");
        assertTrue(compareFiles("oneChar_out.txt", "oneChar.txt"));
    }
    @Test
    void emptyTestEnc() throws IOException {
        Huffman.huffmanEncode("empty.txt", "empty_out.txt");
        assertTrue(compareFiles("empty_out.txt", "empty_soln.txt"));
    }
    @Test
    void emptyTestDec() throws IOException {
        Huffman.huffmanDecode("empty_soln.txt", "empty_out.txt");
        assertTrue(compareFiles("empty_out.txt", "empty.txt"));
    }

    @Test
    public void HuffmanNodeTest() {
        Huffman.HuffmanTree d = new Huffman.HuffmanLeaf(5, 'd');
        Huffman.HuffmanTree k = new Huffman.HuffmanLeaf(5, 'k');
        assertTrue(d.comesBefore(k));
        assertFalse(k.comesBefore(k));

        Huffman.HuffmanTree a = new Huffman.HuffmanLeaf(3, 'a');
        Huffman.HuffmanTree b = new Huffman.HuffmanLeaf(10, 'b');
        assertTrue(a.comesBefore(b));
    }

    @Test
    public void combineTest() {
        Huffman.HuffmanTree d = new Huffman.HuffmanLeaf(5, 'd');
        Huffman.HuffmanTree k = new Huffman.HuffmanLeaf(5, 'k');
        assertEquals(k.combine(d),d.combine(k));
        Huffman.HuffmanTree k2 = new Huffman.HuffmanLeaf(5, 'k');
        assertEquals(k.combine(k2),k2.combine(k));
    }
    @Test
    public void dumbHeapTest() {
        Random rand = new Random("CSC-2001-Data-Structures".hashCode());
        Huffman.HQueue testQueue = new Huffman.HQueue.HuffmanArrayHeap();
        for (int i = 0; i < 1000000; i++) {
            testQueue.enqueue(new Huffman.HuffmanLeaf(rand.nextInt(256), rand.nextInt(256)));
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

    /**
     * Compares two files line by line, trimming trailing CR/LF so the
     * comparison works the same on Windows and Unix-style line endings.
     */
    private static boolean compareFiles(String file1, String file2) throws IOException {
        try (BufferedReader r1 = new BufferedReader(new FileReader(file1));
             BufferedReader r2 = new BufferedReader(new FileReader(file2))) {
            String line1, line2;
            while (true) {
                line1 = r1.readLine();
                line2 = r2.readLine();
                if (line1 == null && line2 == null) {
                    return true;
                }
                if (line1 == null || line2 == null) {
                    return false;
                }
                if (!line1.trim().equals(line2.trim())) {
                    return false;
                }
            }
        }
    }
}
