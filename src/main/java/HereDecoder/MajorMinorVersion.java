package HereDecoder;

public class MajorMinorVersion {

    public String version;
    public boolean isValid;

    public MajorMinorVersion() {

    }

    public String getVersion() {
        return version;
    }

    public boolean isValid() {
        return isValid;
    }

    public int decode(byte[] buff) {
        int readBytes = 1;
        //IntUnTi one Byte
        int major = (buff[0] & 0xF0) >> 4;
        int minor = buff[0] & 0x0F;

        version = major + "." + minor;
        return readBytes;
    }

    public byte[] encodeVersion(String Version) {
        byte[] buff = new byte[1];
        String[] majorMinor = version.split(".");
        int major = Integer.parseInt(majorMinor[0]);
        int minor = Integer.parseInt(majorMinor[1]);
        buff[0] = (byte) ((major << 4) | minor);
        return buff;
    }
}