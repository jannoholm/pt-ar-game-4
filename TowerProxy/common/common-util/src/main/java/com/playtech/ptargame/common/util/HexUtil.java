package com.playtech.ptargame.common.util;

import java.nio.ByteBuffer;
import java.util.Collection;

public class HexUtil {
    private static final char[] HEX={'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F'};

    /**
     * Convert byte array to hex string.
     *
     * @param value  byte array to make hex of
     * @param start  inclusive
     * @param end    exclusive
     * @return       hex string of byte array part
     */
    public static String toHex(byte[] value, int start, int end){
        final char[] buf = new char[((end-start)<<1)];
        for (int i=0; start<end;){
            int b=value[start++]&0xFF;
            buf[i++]=HEX[b>>>4];
            buf[i++]=HEX[(b&0xF)];
        }
        return String.valueOf(buf);
    }

    /**
     * Parse hex string to byte array.
     * If hex length is 2n+1 (odd number), then last byte is ignored.
     *
     * @param hex   hex string to convert to byte array
     * @return      byte array converted from hex
     */
    public static byte[] parseHex( String hex ) {
        final int len = hex.length();
    	/*
    	if ((len&1) != 0) //complaint, ignore the last nibble
    		throw new NumberFormatException("Invalid hex: "+hex);
    	*/
        final byte[] result = new byte[len>>>1];
        for (int i=0, j=0;i<result.length;i++){
            int high = parseNibble(hex.charAt(j++));
            int low = parseNibble(hex.charAt(j++));

            result[i]=(byte)((high<<4) | low);
        }
        return result;
    }

    private static int parseNibble(char c){
        if (c>='0' && c<='9'){
            return (c-'0');
        }
        if (c>='A' && c<='F')
            return 0xA-'A'+c;//keeping the exact order of constants/literals result in better bytecode
        if (c>='a' && c<='f')
            return 0xA-'a'+c;

        throw new NumberFormatException("Invalid nibble "+c);
    }
    /**
     * Convert byte array to hex string.
     *
     * @param value     byte array to convert to hex string
     * @return          hex string
     */
    public static String toHex( byte[] value ) {
        return toHex( value, 0, value.length );
    }

    /**
     * Convert byte to hex string.
     *
     * @param value byte value to convert to hex
     * @return      hex string of byte
     */
    public static String toHex( byte value ) {
        String result = Integer.toHexString( value & 0xFF ).toUpperCase();
        return ( result.length() == 2 ? result : "0" + result );
    }

    /**
     * Convert long to hex string.
     *
     * @param value     long value to convert to hex
     * @return          hex representation of long value
     */
    public static String toHex( long value ) {
        byte[] b=new byte[8];
        ByteBuffer.wrap(b).putLong(value);
        return toHex(b);
    }

    public static String toHex(ByteBuffer buffer) {
        int size = buffer.remaining();
        byte[] b = new byte[size];
        buffer.get(b);
        return toHex(b);
    }

    public static String toHex(Collection<ByteBuffer> buffers) {
        StringBuilder s = new StringBuilder();
        for (ByteBuffer buffer : buffers) {
            s.append(toHex(buffer));
        }
        return s.toString();
    }
}
