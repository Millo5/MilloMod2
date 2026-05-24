package millo.millomod2.client.util.math;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class ByteArray {

    private int head = 0;
    private byte[] array;
    private int[] chunks;

    public ByteArray() {
        array = new byte[0];
        chunks = new int[0];
    }

    private void add(boolean bit) {
        if (head % 8 == 0) expand();

        if (bit) array[head / 8] |= (byte) (1 << (7 - (head % 8)));
        head++;
    }

    public void add(byte b) {
        add(b, 8);
    }

    public void add(byte b, int length) {
        for (int i = 0; i < length; i++) {
            add((b & (1 << (7 - i))) != 0);
        }

        chunks = append(chunks, length);
    }

    public void add(byte[] bytes, int length) {
        while (length > 0) {
            int toAdd = Math.min(length, 8);
            add(bytes[0], toAdd);
            bytes = shift(bytes, toAdd);
            length -= toAdd;
        }
    }

    public void add(int value, int length) {
        for (int i = 0; i < length; i++) {
            add((value & (1 << (length - 1 - i))) != 0);
        }

        chunks = append(chunks, length);
    }

    public void add(long value, int length) {
        for (int i = 0; i < length; i++) {
            add((value & (1L << (length - 1 - i))) != 0);
        }

        chunks = append(chunks, length);
    }

    private int[] append(int[] arr, int value) {
        int[] newArr = new int[arr.length + 1];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        newArr[arr.length] = value;
        return newArr;
    }

    public void append(ByteArray other) {
        for (int i = 0; i < other.head; i++) {
            add((other.array[i / 8] & (1 << (7 - (i % 8)))) != 0);
        }
        for (int chunk : other.chunks) {
            chunks = append(chunks, chunk);
        }
    }

    private byte[] shift(byte[] bytes, int bits) {
        byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int shifted = (bytes[i] & 0xFF) << bits;
            newBytes[i] |= (byte) (shifted & 0xFF);
            if (i + 1 < bytes.length) {
                newBytes[i + 1] |= (byte) ((shifted >> 8) & 0xFF);
            }
        }
        return newBytes;
    }

    private void expand() {
        byte[] newArray = new byte[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int chunkIndex = 0;
        int bitCount = 0;
        for (int i = 0; i < head; i++) {
            if (bitCount > 0 && chunkIndex < chunks.length && bitCount == chunks[chunkIndex]) {
                sb.append(" ");
                chunkIndex++;
                bitCount = 0;
            }
            sb.append((array[i / 8] & (1 << (7 - (i % 8)))) != 0 ? "1" : "0");
            bitCount++;
        }
        return sb.toString();
    }

    public int length() {
        return head;
    }

    public String[] compress() {
        byte[][] chunks = new byte[(array.length + 9999) / 10000][];
        for (int i = 0; i < chunks.length; i++) {
            int start = i * 10000;
            int end = Math.min(start + 10000, array.length);
            chunks[i] = new byte[end - start];
            System.arraycopy(array, start, chunks[i], 0, end - start);
        }


        String[] compressed = new String[chunks.length];
        for (int i = 0; i < chunks.length; i++) {
            try {
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                GZIPOutputStream gzipOut = new GZIPOutputStream(byteOut);
                gzipOut.write(chunks[i]);
                gzipOut.close();
                byte[] compressedBytes = byteOut.toByteArray();

                String base64 = java.util.Base64.getEncoder().encodeToString(compressedBytes);
                compressed[i] = base64;
            } catch (IOException e) {
                e.printStackTrace();
                compressed[i] = "";
            }
        }

        return compressed;
    }
}
