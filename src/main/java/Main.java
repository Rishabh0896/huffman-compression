import huffman.HuffmanCompression;
import huffman.HuffmanDecompression;

/**
 * Main driver class for Huffman coding application.
 * Provides command line interface to compress/decompress files using Huffman coding.
 */
public class Main {

    /**
     * Main method.
     *
     * @param args command line arguments
     *             args[0]: input file name
     *             args[1]: operation - "compress" or "decompress"
     */
    public static void main(String[] args) {

        // Validate command line arguments
        if (args.length < 2) {
            printUsage();
            return;
        }

        String inputFile = args[0];
        String operation = args[1].toLowerCase();

        // Call appropriate operation
        if ("compress".equals(operation)) {
            compressFile(inputFile);
        } else if ("decompress".equals(operation)) {
            decompressFile(inputFile);
        } else {
            printInvalidOperationError();
        }
    }

    /**
     * Compress input file using Huffman coding.
     *
     * @param inputFile input file name
     */
    public static void compressFile(String inputFile) {
        HuffmanCompression compressor = new HuffmanCompression(inputFile);
        compressor.compress();
    }

    /**
     * Decompress input file using Huffman coding.
     *
     * @param inputFile input file name
     */
    public static void decompressFile(String inputFile) {
        HuffmanDecompression decompressor = new HuffmanDecompression(inputFile);
        decompressor.decompress();
    }

    /**
     * Prints usage instructions.
     */
    private static void printUsage() {
        System.out.println("Usage: java Main <file_name> <operation>");
        System.out.println("   <file_name>: The name of the input file.");
        System.out.println("   <operation>: 'compress' or 'decompress'.");
    }

    /**
     * Prints error message for invalid operation.
     */
    private static void printInvalidOperationError() {
        System.out.println("Invalid operation. Please specify 'compress' or 'decompress'.");
    }

}