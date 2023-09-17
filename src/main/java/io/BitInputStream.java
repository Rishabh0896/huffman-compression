package io;

import java.io.IOException;
import java.io.InputStream;

/**
 * The BitInputStream class provides a convenient way to read individual bits from an InputStream.
 * It reads bits from the input stream and allows for efficient reading of binary data.
 */
public class BitInputStream {
    private InputStream in;         // The underlying input stream
    private int buffer;             // A buffer to store a byte from the input stream
    private int bufferLength;       // The number of bits available in the buffer

    /**
     * Constructs a BitInputStream with an underlying InputStream.
     *
     * @param in The InputStream to read bits from.
     */
    public BitInputStream(InputStream in) {
        this.in = in;
        this.buffer = 0;
        this.bufferLength = 0;
    }

    /**
     * Reads a single bit from the input stream.
     *
     * @return The next bit from the input stream as an integer (0 or 1), or -1 if the end of the stream is reached.
     * @throws IOException If an I/O error occurs while reading from the underlying stream.
     */
    public int readBit() throws IOException {
        if (bufferLength == 0) {
            int nextByte = in.read();
            if (nextByte == -1) {
                return -1; // End of stream
            }
            buffer = nextByte;
            bufferLength = 8; // Number of bits in a byte
        }

        int bit = (buffer >> (bufferLength - 1)) & 1;
        bufferLength--;
        return bit;
    }

    /**
     * Closes the BitInputStream and releases any system resources associated with it.
     *
     * @throws IOException If an I/O error occurs while closing the underlying stream.
     */
    public void close() throws IOException {
        in.close();
    }
}
