package huffman;

import java.io.Serializable;

/**
 * The Node class represents a node used in Huffman coding.
 * Each node contains a character (c), its frequency (f), references to its left and right children,
 * and a StringBuilder to store the Huffman code associated with the character.
 */
public class Node implements Serializable {
    private char c;                 // The character stored in this node
    private int f;                  // The frequency of the character
    private Node left;              // Reference to the left child node
    private Node right;             // Reference to the right child node
    // Stores the Huffman code associated with the character

    /**
     * Constructs a Node with a character and its frequency.
     *
     * @param c The character to be stored in the node.
     * @param f The frequency of the character.
     */
    Node(char c, int f) {
        this(c, f, null, null);
    }

    /**
     * Constructs a Node with a character, its frequency, and references to its left and right children.
     *
     * @param c     The character to be stored in the node.
     * @param f     The frequency of the character.
     * @param left  The left child node.
     * @param right The right child node.
     */
    Node(char c, int f, Node left, Node right) {
        this.c = c;
        this.f = f;
        this.left = left;
        this.right = right;
    }

    public Node() {

    }

    // Getter and Setter methods for private fields

    public char getC() {
        return c;
    }

    public int getF() {
        return f;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    // Override equals and hashCode methods for object comparison

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (c != node.c) return false;
        return f == node.f;
    }

    @Override
    public int hashCode() {
        int result = c;
        result = 31 * result + f;
        return result;
    }
}
