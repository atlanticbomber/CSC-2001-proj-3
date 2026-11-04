public record HuffmanLeaf (int freq, int charAscii) implements HuffmanTree{

    @Override
    public boolean comesBefore(HuffmanTree other) {
        // unimplemented...
        return false;
    }

    @Override
    public HuffmanTree combine(HuffmanTree other) {
        // unimplemented...
        return this;
    }

    @Override
    public int compareTo(HuffmanTree other) {
        // unimplemented ...
        return 0;
    }
}
