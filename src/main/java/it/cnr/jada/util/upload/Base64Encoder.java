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

public class Base64Encoder extends FilterOutputStream {

    private static final char[] chars = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', '+', '/'
    };
    private int charCount;
    private int carryOver;

    public Base64Encoder(OutputStream out) {
        super(out);
    }

    public static String encode(String unencoded) {
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) ((double) unencoded.length() * 1.3700000000000001D));
        Base64Encoder encodedOut = new Base64Encoder(out);
        byte[] bytes = null;
        try {
            bytes = unencoded.getBytes("8859_1");
        } catch (UnsupportedEncodingException _ex) {
        }
        try {
            encodedOut.write(bytes);
            encodedOut.close();
            return out.toString("8859_1");
        } catch (IOException _ex) {
            return null;
        }
    }

    public static void main(String args[])
            throws Exception {
        if (args.length != 1)
            System.err.println("Usage: Base64Encoder fileToEncode");
        Base64Encoder encoder = null;
        BufferedInputStream in = null;
        try {
            encoder = new Base64Encoder(System.out);
            in = new BufferedInputStream(new FileInputStream(args[0]));
            byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1)
                encoder.write(buf, 0, bytesRead);
        } finally {
            if (in != null)
                in.close();
            if (encoder != null)
                encoder.close();
        }
    }

    public void close()
            throws IOException {
        if (charCount % 3 == 1) {
            int lookup = carryOver << 4 & 0x3f;
            super.out.write(chars[lookup]);
            super.out.write(61);
            super.out.write(61);
        } else if (charCount % 3 == 2) {
            int lookup = carryOver << 2 & 0x3f;
            super.out.write(chars[lookup]);
            super.out.write(61);
        }
        super.close();
    }

    public void write(int b)
            throws IOException {
        if (b < 0)
            b += 256;
        if (charCount % 3 == 0) {
            int lookup = b >> 2;
            carryOver = b & 3;
            super.out.write(chars[lookup]);
        } else if (charCount % 3 == 1) {
            int lookup = (carryOver << 4) + (b >> 4) & 0x3f;
            carryOver = b & 0xf;
            super.out.write(chars[lookup]);
        } else if (charCount % 3 == 2) {
            int lookup = (carryOver << 2) + (b >> 6) & 0x3f;
            super.out.write(chars[lookup]);
            lookup = b & 0x3f;
            super.out.write(chars[lookup]);
            carryOver = 0;
        }
        charCount++;
        if (charCount % 57 == 0)
            super.out.write(10);
    }

    public void write(byte b[], int off, int len)
            throws IOException {
        for (int i = 0; i < len; i++)
            write(b[off + i]);

    }

}