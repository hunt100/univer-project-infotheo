public class HuffmanNode implements Comparable<HuffmanNode> {
    int frequency;
    char data;
    HuffmanNode left, right;

    @Override
    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}