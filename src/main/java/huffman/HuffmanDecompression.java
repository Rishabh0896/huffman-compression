package huffman;

import io.BitInputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Decompresses a Huffman-encoded file.
 * The decompression process involves:
 * 1. Reading Huffman metadata
 * 2. Decoding bits to characters
 * 3. Writing decoded characters to an output file
 */
public class HuffmanDecompression {
    // Root node of the Huffman tree
    private static Node huffRoot = new Node();

    private final String inputFile;

    /**
     * Initializes a HuffmanDecompression instance with the input file to decompress.
     *
     * @param inputFile The path of the input compressed file.
     */
    public HuffmanDecompression(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Reads the compressed input file, decodes it, and writes the decompressed output to a file.
     */
    public void decompress() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("decoded_output.txt"));
            inflateHuffMetaData();
            // TODO : Add INFO Logging
//            huffmanCodes.forEach((key, value) -> System.out.println(key + " " + value));
            InputStream inputStream = Files.newInputStream(Paths.get(this.inputFile + ".hZip"));
            BitInputStream bitInputStream = new BitInputStream(inputStream);
            int bit;
            Node currentNode = huffRoot;
//            System.out.println(length);
            while ((bit = bitInputStream.readBit()) != -1) {
                if (bit == 0) {
                    currentNode = currentNode.getRight();
                } else {
                    currentNode = currentNode.getLeft();
                }

                // Check if we've reached a leaf node (character node)
                if (currentNode.getC() != Character.MIN_VALUE) {
                    // Write the decoded character to the output
                    writer.write(currentNode.getC());
                    // TODO : Add INFO Logging
                    // System.out.println("Character till bit : " + bit + " is : " + currentNode.getC());
                    // Reset to the root for the next code
                    currentNode = huffRoot;
                }
            }
            writer.close();

        } catch (IOException e) {
            // TODO: Add FATAL Logging
            e.printStackTrace();
        }
    }

    /**
     * Reads the Huffman metadata from a serialized file and inflates the Huffman tree.
     */
    private void inflateHuffMetaData() {
        try {
            FileInputStream fileInput = new FileInputStream(this.inputFile + "_huffman_data.ser");
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            huffRoot = (Node) objectInput.readObject();
            objectInput.close();
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
