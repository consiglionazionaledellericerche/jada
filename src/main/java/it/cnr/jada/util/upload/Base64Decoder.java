/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.jada.util.upload;

import java.io.*;

public class Base64Decoder extends FilterInputStream {

    private static final char[] chars = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', '+', '/'
    };
    private static final int[] ints;

    static {
        ints = new int[128];
        for (int i = 0; i < 64; i++)
            ints[chars[i]] = i;

    }

    private int charCount;
    private int carryOver;

    public Base64Decoder(InputStream in) {
        super(in);
    }

    public static String decode(String encoded) {
        byte[] bytes = null;
        try {
            bytes = encoded.getBytes("8859_1");
        } catch (UnsupportedEncodingException _ex) {
        }
        Base64Decoder in = new Base64Decoder(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) ((double) bytes.length * 0.67000000000000004D));
        try {
            byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1)
                out.write(buf, 0, bytesRead);
            out.close();
            return out.toString("8859_1");
        } catch (IOException _ex) {
            return null;
        }
    }

    public static void main(String args[])
            throws Exception {
        if (args.length != 1)
            System.err.println("Usage: java Base64Decoder fileToDecode");
        Base64Decoder decoder = null;
        try {
            decoder = new Base64Decoder(new BufferedInputStream(new FileInputStream(args[0])));
            byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = decoder.read(buf)) != -1)
                System.out.write(buf, 0, bytesRead);
        } finally {
            if (decoder != null)
                decoder.close();
        }
    }

    public int read()
            throws IOException {
        int x;
        do {
            x = super.in.read();
            if (x == -1)
                return -1;
        } while (Character.isWhitespace((char) x));
        charCount++;
        if (x == 61)
            return -1;
        x = ints[x];
        int mode = (charCount - 1) % 4;
        if (mode == 0) {
            carryOver = x & 0x3f;
            return read();
        }
        if (mode == 1) {
            int decoded = (carryOver << 2) + (x >> 4) & 0xff;
            carryOver = x & 0xf;
            return decoded;
        }
        if (mode == 2) {
            int decoded = (carryOver << 4) + (x >> 2) & 0xff;
            carryOver = x & 3;
            return decoded;
        }
        if (mode == 3) {
            int decoded = (carryOver << 6) + x & 0xff;
            return decoded;
        } else {
            return -1;
        }
    }

    public int read(byte b[], int off, int len)
            throws IOException {
        int i;
        for (i = 0; i < len; i++) {
            int x = read();
            if (x == -1 && i == 0)
                return -1;
            if (x == -1)
                break;
            b[off + i] = (byte) x;
        }

        return i;
    }
}