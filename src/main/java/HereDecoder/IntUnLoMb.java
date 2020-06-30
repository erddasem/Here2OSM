package HereDecoder;

public class IntUnLoMb {

    public static int max_size = 5;
    public static int min_size = 1;

    public int size;
    public int value;
    public boolean isValid;

    public static byte[] encode(int value) {
        long unsignedValue = (long) value;
        int size;

        if (unsignedValue < 1 << 7)
            size = 1; // 7 bits available
        else if (unsignedValue < 1 << 14)
            size = 2; // 14 bits available;
        else if (unsignedValue < 1 << 21)
            size = 3; // 21 bits available;
        else if (unsignedValue < 1 << 28)
            size = 4; // 28 bits available;
        else
            size = 5;

        byte[] buf = new byte[size];
        for (int i = 0; i < size; i++) {

            int shift = 7 * (size - i - 1);
            byte curByte = (byte) (0x7f & (unsignedValue >> shift));
            curByte |= 0x80; //Set continue bit to true;
            buf[i] = curByte;
        }
        buf[size - 1] &= 0x7f; //Set the continue bit on the last byte to 0
        return buf;

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public int decode(byte[] byteBuffer) {
        int buf_size = byteBuffer.length;
        if (buf_size < min_size) {
            isValid = false;
            return 0;
        }
        size = min_size;

        //Start with MSB
        value = Byte.toUnsignedInt(byteBuffer[0]) & 0x7f;

        // Processing 1---5 input bytes
        for (int i = 0; i < max_size; i++) {
            byte buf = byteBuffer[i];
            //No continuation?
            if ((buf & 0x80) == 0x0) {
                isValid = true;
                // Return number of bytes read
                return size;
            }
            size++;

            //Make space for the next 7 bits
            value <<= 7;

            // add 7 bite to value
            value |= (Byte.toUnsignedInt(byteBuffer[i + 1]) & 0x7f);
        }

        // If we reach here, we've read 5 bytes
        // all bytes have the continuation bit set to 1  < encoding is wrong

        //nothing res
        return 0;
    }
}
