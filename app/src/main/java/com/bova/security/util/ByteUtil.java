package com.bova.security.util;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteUtil {
    public static byte[] fromUnsignedInt(long value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putLong(value);

        return Arrays.copyOfRange(bytes, 4, 8);
    }

    public static long toUnsignedInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8).put(new byte[]{0, 0, 0, 0}).put(bytes);
        buffer.position(0);

        return buffer.getLong();
    }
}
