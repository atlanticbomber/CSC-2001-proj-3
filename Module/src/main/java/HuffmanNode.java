/**
 * A single node in a Huffman tree, implemented as a record.
 *
 * A HuffmanNode represents either a leaf (an actual character together
 * with its occurrence count) or an internal node created by combining two
 * subtrees during tree construction.
 *
 * Records are immutable and give you charAscii(), freq(), left(), and
 * right() accessor methods automatically, along with
 * equals()/hashCode()/toString(). Do not rename the components below.
 */
public record HuffmanNode(int charAscii, int freq, HuffmanTree left, HuffmanTree right)
     implements HuffmanTree{
     public HuffmanNode genNew() {
         return new HuffmanNode(0, 0, null, null);
     }
     // returns true if this is less than other, otherwise false
     public boolean comesBefore(HuffmanTree other) {
         if (this.freq() < other.freq()) {
             return true;
         }
         else if (this.freq() == other.freq()) {
             return (this.charAscii() < other.charAscii());
         }
         return false;
    }
    // combines this node with passed in node, taking minimum of the char as the new char, smaller on left and larger on right
    public HuffmanNode combine(HuffmanTree other) {
         int newCount = this.freq() + other.freq();
         int min;
         HuffmanNode huss = genNew();
         if (this.charAscii() < other.charAscii()) {
             min = this.charAscii();
         }
         else {
             min = other.charAscii();
         }
         if (this.comesBefore(other)) {
             huss = new HuffmanNode(min, newCount, this, other);
         }
         else if (other.comesBefore(this)) {
             huss = new HuffmanNode(min, newCount, other, this);
         }
         return huss;
    }

    /**
     * Natural ordering consistent with comesBefore(), so other HuffmanNode
     * objects can be stored directly in a sorted structure (e.g. a
     * PriorityQueue or a sorted List) while building the tree.
     */
    @Override
    public int compareTo(HuffmanTree other) {
        if (this.comesBefore(other)) return -1;
        if (other.comesBefore(this)) return 1;
        return 0;
    }
}
