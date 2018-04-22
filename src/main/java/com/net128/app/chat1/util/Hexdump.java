package com.net128.app.chat1.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Hexdump {
    private static final int ELEMENTS_PER_LINE = 16;
    private final static String hexAsciiDelimiter="    ";

    public static String toHex(final ByteBuffer buffer) {
        final StringBuilder out = new StringBuilder();
        hex(buffer, out);
        return out.toString();
    }

    public static void hex(final ByteBuffer buffer, final StringBuilder out) {
        final int start = buffer.position();
        final int end = buffer.limit();
        for (int pos = 0; pos < end; pos += ELEMENTS_PER_LINE) {
            out.append(String.format("%04X - ", pos));
            for (int i = 0; i < ELEMENTS_PER_LINE; ++i) {
                final int idx = start + pos + i;
                if (idx >= end) {
                    out.append("   ");
                } else {
                    final byte b = buffer.get(idx);
                    out.append(String.format("%02X ", b));
                }
            }
            out.append("\n");
        }
        buffer.rewind();
    }

    public static String toAscii(final byte... bytes) {
        return toAscii(ByteBuffer.wrap(bytes)); // byte order does not matter, only byte-wise access
    }

    public static String toAscii(final ByteBuffer buffer) {
        final StringBuilder out = new StringBuilder();
        ascii(buffer, out);
        return out.toString();
    }

    public static void ascii(final ByteBuffer buffer, final StringBuilder out) {
        final int start = buffer.position();
        final int end = buffer.limit();
        for (int pos = 0; pos < end; pos += ELEMENTS_PER_LINE) {
            out.append(String.format("%04X - ", pos));
            for (int i = 0; i < ELEMENTS_PER_LINE; ++i) {
                final int idx = start + pos + i;
                if (idx >= end) {
                    out.append(" ");
                } else {
                    out.append(escapeChars(buffer.get(idx)));
                }
            }
            out.append("\n");
        }
        buffer.rewind();
    }

    public static String toHexdump(final byte... bytes) {
        return toHexdump(ByteBuffer.wrap(bytes)); // byte order does not matter, only byte-wise access
    }

    public static String toHexdump(InputStream is) throws IOException {
        return toHexdump(ByteBuffer.wrap(IOUtils.toByteArray(is)));
    }

    public static void hexdump(InputStream is, final StringBuilder out) throws IOException {
        hexdump(ByteBuffer.wrap(IOUtils.toByteArray(is)), out);
    }

    public static void hexdump(byte [] data, final StringBuilder out) throws IOException {
        hexdump(ByteBuffer.wrap(data), out);
    }

    public static String hexdump(byte [] data) throws IOException {
        final StringBuilder out = new StringBuilder();
        hexdump(ByteBuffer.wrap(data), out);
        return out.toString();
    }

    public static String toHexdump(final ByteBuffer buffer) {
        final StringBuilder out = new StringBuilder();
        hexdump(buffer, out);
        return out.toString();
    }

    public static void hexdump(final ByteBuffer buffer, final StringBuilder out) {
        final int start = buffer.position();
        final int end = buffer.limit();
        for (int pos = 0; pos < end; pos += ELEMENTS_PER_LINE) {
            out.append(String.format("%08X  ", pos));
            for (int i = 0; i < ELEMENTS_PER_LINE; ++i) {
                final int idx = start + pos + i;
                if(i==ELEMENTS_PER_LINE/2) {
                    out.append(" ");
                }
                if (idx >= end) {
                    out.append("   ");
                } else {
                    final byte b = buffer.get(idx);
                    out.append(String.format("%02X ", b));
                }
            }
            out.append(hexAsciiDelimiter);
            for (int i = 0; i < ELEMENTS_PER_LINE; ++i) {
                final int idx = start + pos + i;
                if (idx >= end) {
                    out.append(" ");
                } else {
                    out.append(escapeChars(buffer.get(idx)));
                }
            }
            out.append("\n");
        }
        buffer.rewind();
    }

    public static String escapeChars(final byte... bytes) {
        final StringBuilder buf = new StringBuilder();
        for (final byte b : bytes) {
            if (b >= 32 && b < 127) {
                buf.append(Character.toChars(b));
            } else {
                buf.append('.');
            }
        }
        return buf.toString();
    }

    public static String escapeAsciiChars(final byte... bytes) {
        final StringBuilder buf = new StringBuilder();
        for (final byte b : bytes) {
            if ((b >= 32 && b < 127) || b==9 || b==13) {
                buf.append(Character.toChars(b));
            }
        }
        return buf.toString();
    }

    public static String toCompactHexdump(final ByteBuffer buffer) {
        String string = toHexdump(buffer);
        string = string.replaceAll(" +", " ");
        string = string.replaceAll("\r", "");
        string = string.trim();
        return string;
    }
}