public interface HuffmanTree extends Comparable<HuffmanTree> {
    /**
     * Returns true if the tree rooted at node a comes before the tree
     * rooted at node b, false otherwise.
     *
     * A node "comes before" another if its frequency count is smaller.
     * Ties are broken using the ASCII value of the character: the node
     * with the smaller ASCII value comes first.
     */
    // returns true if this is less than other, otherwise false
     default boolean comesBefore(HuffmanTree other) {
        if (this.freq() < other.freq()) {
            return true;
        }
        else if (this.freq() == other.freq()) {
            return (this.charAscii() < other.charAscii());
        }
        return false;
    }

    /**
     * Creates and returns a new internal HuffmanNode with a and b as its
     * two children.
     *
     * The child for which comesBefore() is true goes on the left, the
     * other goes on the right. The new node's frequency is the sum of a's
     * and b's frequencies. The new node's charAscii value is the smaller
     * of a's and b's charAscii values (used to break ties later on).
     */
    // combines this node with passed in node, taking minimum of the char as the new char, smaller on left and larger on right
     default HuffmanTree combine(HuffmanTree other) {
        int newCount = this.freq() + other.freq();
        int min;
        HuffmanNode huss;
        min = Math.min(this.charAscii(), other.charAscii());
        if (this.comesBefore(other)) {
            huss = new HuffmanNode(min, newCount, this, other);
        }
        else {
            huss = new HuffmanNode(min, newCount, other, this);
        }
        return huss;
    }

    //abstract methods to allow external classes to access shared fields
     int freq();
     int charAscii();

    /**
     * Natural ordering consistent with comesBefore(), so that HuffmanNode
     * objects can be stored directly in a sorted structure (e.g. a
     * PriorityQueue or a sorted List) while building the tree.
     */
    @Override
     default int compareTo(HuffmanTree other) {
        if (this.comesBefore(other)) return -1;
        if (other.comesBefore(this)) return 1;
        return 0;
    }
}
