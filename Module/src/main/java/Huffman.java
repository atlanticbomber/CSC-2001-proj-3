import java.io.*;
import java.util.*;

/**
 * Project 3: Huffman Encoding / Decoding.
 *
 * Implement the methods below to compress and decompress text files using
 * Huffman coding. See the assignment write-up for the full specification.
 *
 *  - Treat characters as 8-bit values in the range [0, 255].
 *  - When writing output files, write the newline character explicitly as
 *    '\n' (do not rely on System.lineSeparator() / println()), so your
 *    output is identical across Windows/Mac/Linux. A BufferedWriter over
 *    a FileWriter, writing "\n" yourself, works well for this.
 *  - Encoded files store the code as literal '0'/'1' characters (one byte
 *    each), not as packed bits, so the "encoded" file will actually be
 *    larger than the original -- that's expected for this assignment.
 *  - createHuffTree() is built on top of addNode()/removeMin(), which
 *    operate generically over a java.util.Collection<HuffmanNode>.
 *  - HuffmanNode is a record, so its components are read with
 *    node.charAscii(), node.freq(), node.left(), and node.right() -- no
 *    "get" prefix.
 */
public class Huffman {

    /** Number of possible 8-bit character values. */
    public static final int NUM_CHARS = 256;

    /**
     * Opens the text file named filename and counts the frequency of
     * occurrence of every character in it.
     * Returns an int array of size NUM_CHARS, indexed by ASCII value, with
     * the count of each character (0 for characters that do not occur).
     */
    public static int[] cntFreq(String filename) throws IOException {
        int[] freq = new int[NUM_CHARS];
        File file = new File(filename);
        FileReader reader = new FileReader(file);
        while (reader.ready()) {
            freq[reader.read()]++;
        }
        return freq;
    }


    /**
     * Builds a Huffman tree from an array of character frequencies (as
     * produced by cntFreq()) and returns the root HuffmanNode of the tree.
     *
     * Use an HQueue to manage the collection of
     * not-yet-combined nodes as you build the tree.
     *
     * Special cases:
     *  - If freq contains no non-zero counts (empty input file), return null
     *  - If freq contains exactly one non-zero count (input file has only
     *    one unique character), return a single leaf HuffmanNode for that
     *    character (no combine() call needed).
     */
    public static HuffmanTree createHuffTree(int[] freq) {
        HQueue q = new HuffmanArrayHeap();
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] > 0) {
                q.enqueue(new HuffmanLeaf(freq[i], i));
            }
        }
        if (q.isEmpty()) {
            return null;
        }
        while (q.count() > 1) {
            HuffmanTree t1 = q.dequeue();
            HuffmanTree t2 = q.dequeue();
            HuffmanTree t3 = t1.combine(t2);
            q.enqueue(t3);
        }
        return q.dequeue();
    }

    /**
     * Traverses the Huffman tree rooted at root and builds the Huffman
     * code for every character that appears in it. Going left appends
     * '0' to the code, going right appends '1'.
     *
     * Returns a String array of size NUM_CHARS, indexed by ASCII value.
     * Characters that do not appear in the tree have an empty string ""
     * at their index. The same is true of the lone character in a
     * single-node tree (the only-one-unique-character case) - it has no
     * left/right branch to build a code out of; huffmanEncode/huffmanDecode
     * handle that case entirely through the header.
     */
    public static String[] createCode(HuffmanTree root) {
        String[] codes = new String[NUM_CHARS];
        Arrays.fill(codes, "");
        codeCreationHelper(root, codes, "");
        return codes;
    }
    //recursive helper method to create codes
    public static void codeCreationHelper(HuffmanTree tree, String[] codes, String currentCode) {
        if(tree instanceof HuffmanLeaf leaf) {
            codes[leaf.charAscii()] = currentCode;
        }
        else if(tree instanceof HuffmanNode node) {
            codeCreationHelper(node.left(),codes,currentCode+"0");
            codeCreationHelper(node.right(),codes,currentCode+"1");
        }
    }

    /**
     * Given a frequency array (as produced by cntFreq()), returns a
     * header string listing the ASCII values and their associated
     * frequencies, separated by single spaces, in ascending order of
     * ASCII value. Only characters with a non-zero count are included.
     *
     * Example: for the text "aaabbbbcc" this returns "97 3 98 4 99 2".
     */
    public static String createHeader(int[] freq) {
        String result = "";
        for (int i = 0; i < freq.length; i++) {
            if(freq[i] > 0)
                result += i + " " + freq[i] + " ";
        }
        return result+"\n";
    }

    /**
     * Reads the input text file inFile and writes to outFile:
     *   1. A header line (see createHeader), followed by a newline
     *   2. The Huffman-encoded contents of inFile, using the codes from
     *      createCode() -- UNLESS inFile has only one unique character,
     *      see below.
     * If outFile already exists, its old contents are overwritten.
     *
     * Special cases:
     *  - Empty input file: outFile should end up empty too
     *  - Input file with only one unique character: should contain only the
     *    header line (e.g. "97 5") and nothing else - no second line of code bits.
     */
    public static void huffmanEncode(String inFile, String outFile) throws IOException {
        int[] freq = cntFreq(inFile);
        HuffmanTree root = createHuffTree(freq);

        if (root == null) {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(outFile))) {}
            return;
        }
        String[] codes = createCode(root);
        String header = createHeader(freq);

        try (BufferedWriter out = new BufferedWriter(new FileWriter(outFile))) {
            out.write(header);

            if (root instanceof HuffmanLeaf) {
                return;
            }
            try (BufferedReader in = new BufferedReader(new FileReader(inFile))) {

                int ch;
                while ((ch = in.read()) != -1) {
                    out.write(codes[ch]);
                }
            }
        }
    }

    /**
     * Parses a header string (in the format produced by createHeader) and
     * returns the corresponding frequency array of size NUM_CHARS, indexed
     * by ASCII value, i.e. the same format returned by cntFreq().
     *
     * Example: for the header "97 3 98 4 99 2", freq[97, 98, 99] == {3, 4, 2}.
     */
    public static int[] parseHeader(String headerString) {
        int[] freq = new int[NUM_CHARS];
        String[] parts = headerString.trim().split("\\s+");
        for (int i = 0; i < parts.length; i += 2) {
            int a = Integer.parseInt(parts[i]);
            int c = Integer.parseInt(parts[i + 1]);
            freq[a] = c;
        }
        return freq;
    }

    /**
     * Reads an encoded file (in the format written by huffmanEncode) and
     * writes the decoded text to decodeFile.
     *
     * Recreate the frequency array from the header with parseHeader(),
     * rebuild the Huffman tree with createHuffTree(), then walk the tree
     * for each bit of the second line: '0' -> left child, '1' -> right
     * child. When a leaf is reached, emit its character and restart at
     * the root.
     *
     * Special case: if the recreated tree is a single leaf (the
     * only-one-unique-character case), there is no second line to read
     * The header's count alone tells you how many times that character occurred,
     * so write it out that many times directly.
     */
    public static void huffmanDecode(String encodedFile, String decodeFile) throws IOException {
        try(FileWriter decoder = new FileWriter(decodeFile); FileReader input = new FileReader(encodedFile)) {
            String fullText = input.readAllAsString();
            if (!fullText.isEmpty())  { //not empty file case
                String header = fullText.substring(0, fullText.indexOf('\n'));
                HuffmanTree tree = createHuffTree(parseHeader(header));
                if (tree instanceof HuffmanLeaf) { //one unique char case
                    for (int i = 0; i < tree.freq(); i++) {
                        decoder.write(tree.charAscii());
                    }
                } else { //standard case
                    HuffmanTree root = tree;
                    String encodedText = fullText.substring(fullText.indexOf('\n') + 1);
                    for (char c : encodedText.toCharArray()) {
                        HuffmanNode node = (HuffmanNode) tree;
                        tree = (c == '0') ? node.left() : node.right();
                        if (tree instanceof HuffmanLeaf) {
                            decoder.write(tree.charAscii());
                            tree = root;
                        }
                    }
                }
            }
        }

    }
}
