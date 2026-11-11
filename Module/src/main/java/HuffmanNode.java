/**
 * A single node in a Huffman tree, implemented as a record.
 * <p>
 * A HuffmanTree.HuffmanNode represents either a leaf (an actual character together
 * with its occurrence count) or an internal node created by combining two
 * subtrees during tree construction.
 * <p>
 * Records are immutable and give you charAscii(), freq(), left(), and
 * right() accessor methods automatically, along with
 * equals()/hashCode()/toString(). Do not rename the components below.
 */
record HuffmanNode(int freq, int charAscii, HuffmanTree left, HuffmanTree right)
        implements HuffmanTree {
}