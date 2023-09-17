package huffman;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import io.BitOutputStream;

/**
 * Compresses a file using Huffman coding.
 * 1. Counts character frequencies
 * 2. Builds Huffman tree from frequencies
 * 3. Encodes file bytes to Huffman codes
 * 4. Writes encoded bits and tree to compressed file
 */
public class HuffmanCompression {

    // Map of huffman codes for each character
    private static final HashMap<Character, String> huffmanCodes = new HashMap<>();

    private Node huffRoot = null;
    private final String inputFile;

    public HuffmanCompression(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Compresses the input file using Huffman coding.
     */
    public void compress() {
        HashMap<Character, Integer> map = new HashMap<>();
        countCharacterFrequencies(map);
        PriorityQueue<Node> minHeap = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        populatePriorityQueue(map, minHeap);
        buildHuffmanTree(minHeap);
        writeCompressedBits(map);
        // TODO: Add INFO logger
        // huffmanCodes.forEach((key, value) -> System.out.println((int)key + " " + value));
    }

    /**
     * Counts the frequencies of characters in the input file.
     *
     * @param map empty frequency map
     */
    private void countCharacterFrequencies(HashMap<Character, Integer> map) {
        File file = new File(inputFile);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for (String line; (line = br.readLine()) != null; ) {
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (map.containsKey(c)) {
                        int freq = map.get(c);
                        map.put(c, freq + 1);
                    } else {
                        map.put(c, 1);
                    }
                }
                if (map.containsKey('\n')) {
                    int freq = map.get('\n');
                    map.put('\n', freq + 1);
                } else {
                    map.put('\n', 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Builds the Huffman tree from character frequencies.
     *
     * @param minHeap priority queue containing the characters
     */
    private void buildHuffmanTree(PriorityQueue<Node> minHeap) {
        while (minHeap.size() >= 2) {
            Node left = minHeap.poll();
            Node right = minHeap.poll();
            assert right != null;
            Node node = new Node(Character.MIN_VALUE, left.getF() + right.getF(), left, right);
            minHeap.add(node);
        }
        Node root = minHeap.poll();
        this.huffRoot = root;
        generateHuffmanCodes(root, new StringBuilder());
    }

    /**
     * Generates Huffman codes for characters in the Huffman tree.
     *
     * @param node root node of the huffman tree
     * @param code huffman code corresponding to a character
     */
    private void generateHuffmanCodes(Node node, StringBuilder code) {
        if (node == null) {
            return;
        }
        if (node.getC() != Character.MIN_VALUE) {
            huffmanCodes.put(node.getC(), code.toString());
        } else {
            generateHuffmanCodes(node.getLeft(), code.append('1'));
            code.deleteCharAt(code.length() - 1);
            generateHuffmanCodes(node.getRight(), code.append('0'));
            code.deleteCharAt(code.length() - 1);
        }
    }

    /**
     * Writes compressed bits to the output file.
     *
     * @param map character frequency map
     */
    private void writeCompressedBits(HashMap<Character, Integer> map) {
        try {
            postProcess();
            char[] bitCount = countTotalBits(map);
            File in = new File(inputFile);
            OutputStream outputStream = Files.newOutputStream(Paths.get(inputFile + ".hzip"));
            BitOutputStream bitOutputStream = new BitOutputStream(outputStream);
            BufferedReader reader = new BufferedReader(new FileReader(in));
            String currentLine;
            // First 32 bits reserved for length of the file
            for (char c : bitCount) {
                bitOutputStream.writeBit(Character.getNumericValue(c));
            }
            while ((currentLine = reader.readLine()) != null) {
                for (int i = 0; i < currentLine.length(); i++) {
                    char c = currentLine.charAt(i);

                    // Look up the Huffman code for the current character
                    String huffmanCode = huffmanCodes.get(c);

                    if (huffmanCode != null) {
                        // Write each bit of the Huffman code to the BitOutputStream
                        for (char bit : huffmanCode.toCharArray()) {
                            int bitValue = Character.getNumericValue(bit);
                            bitOutputStream.writeBit(bitValue);
                        }
                    } else {
                        // Handle the case when the character is not in the HuffmanCodes HashMap
                        System.err.println("Character not found in HuffmanCodes: " + c);
                    }
                }
                String huffmanCode = huffmanCodes.get('\n');
                if (huffmanCode != null) {
                    // Write each bit of the Huffman code to the BitOutputStream
                    for (char bit : huffmanCode.toCharArray()) {
                        int bitValue = Character.getNumericValue(bit);
                        bitOutputStream.writeBit(bitValue);
                    }
                } else {
                    // Handle the case when the character is not in the HuffmanCodes HashMap
                    System.err.println("Character not found in HuffmanCodes: " + '\n');
                }
            }
            bitOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Counts the total number of bits needed to represent the compressed file.
     *
     * @param map frequency map
     * @return total length of the bits
     */
    private char[] countTotalBits(HashMap<Character, Integer> map) {
        int bitCount = 0;
        for (Map.Entry<Character, Integer> e : map.entrySet()) {
            bitCount = bitCount + huffmanCodes.get(e.getKey()).length() * e.getValue();
        }
        String binaryString = String.format("%32s", Integer.toBinaryString(bitCount)).replace(' ', '0');
        return binaryString.toCharArray();
    }

    /**
     * Post-processes the compression, saving the Huffman tree to a serialized file.
     */
    private void postProcess() {
        try {
            FileOutputStream fileOutputStream =
                    new FileOutputStream(inputFile + "_huffman_data.ser");
            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(huffRoot);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the priority queue with nodes from the character frequencies map.
     *
     * @param map     frequency map
     * @param minHeap priority queue to be populated
     */
    private void populatePriorityQueue(HashMap<Character, Integer> map,
                                       PriorityQueue<Node> minHeap) {
        map.forEach((key, value) -> minHeap.add(new Node(key, value)));
    }

}
