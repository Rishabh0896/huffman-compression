package io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The BitOutputStream class provides a convenient way to write individual bits to an OutputStream.
 * It writes bits to the output stream and allows for efficient writing of binary data.
 */
public class BitOutputStream {
    private OutputStream out;        // The underlying output stream
    private int buffer;              // A buffer to accumulate bits before writing a byte
    private int bufferLength;        // The number of bits currently in the buffer

    /**
     * Constructs a BitOutputStream with an underlying OutputStream.
     *
     * @param out The OutputStream to write bits to.
     */
    public BitOutputStream(OutputStream out) {
        this.out = out;
        this.buffer = 0;
        this.bufferLength = 0;
    }

    /**
     * Writes a single bit (0 or 1) to the output stream.
     *
     * @param bit The bit to write (must be 0 or 1).
     * @throws IOException If an I/O error occurs while writing to the underlying stream.
     */
    public void writeBit(int bit) throws IOException {
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Bit must be 0 or 1");
        }

        buffer = (buffer << 1) | bit;
        bufferLength++;

        if (bufferLength == 8) {
            out.write(buffer);
            buffer = 0;
            bufferLength = 0;
        }
    }

    /**
     * Writes an end-of-file marker to the output stream.
     * This method writes -1 (11111111) as an end-of-file marker.
     *
     * @throws IOException If an I/O error occurs while writing to the underlying stream.
     */
    public void writeEndOfFileMarker() throws IOException {
        // Write -1 (11111111) as an end-of-file marker
        out.write(-1);
    }

    /**
     * Flushes any remaining bits in the buffer to the output stream.
     *
     * @throws IOException If an I/O error occurs while writing to the underlying stream.
     */
    public void flush() throws IOException {
        if (bufferLength > 0) {
            buffer <<= (8 - bufferLength);
            out.write(buffer);
            buffer = 0;
            bufferLength = 0;
        }
    }

    /**
     * Closes the BitOutputStream and releases any system resources associated with it.
     * This method also flushes any remaining bits in the buffer.
     *
     * @throws IOException If an I/O error occurs while closing the underlying stream.
     */
    public void close() throws IOException {
        flush();
        out.close();
    }
}
