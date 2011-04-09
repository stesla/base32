/*
Copyright (c) 2011 Chris Umbel

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package base32;

/**
 * Static class to perform base32 encoding
 *
 * @author Chris Umbel
 */
public class Base32Encoder {
    private static final String charTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    public static int getDigit(byte[] buff, int i) {
        return buff[i] >= 0 ? buff[i] : buff[i] + 256;
    }

    public static String encode(String plainText) {
        return encode(plainText.getBytes());
    }

    public static int quintetCount(byte[] buff) {
	int quintets = buff.length / 5;

	return buff.length % 5 == 0 ? quintets: quintets + 1;
    }

    public static String encode(byte[] buff) {
        int next;
        int current;
        int shiftIndex = 0;
        int digit = 0;
        int i = 0;
	int outputLength = quintetCount(buff) * 8;

        StringBuilder builder = new StringBuilder(outputLength);

        while(i < buff.length) {
            current = getDigit(buff, i);

            if(shiftIndex > 3) {
                if(i + 1 < buff.length)
                    next = getDigit(buff, i + 1);
                else
                    next = 0;

                digit = current & (0xff >> shiftIndex);
                shiftIndex = (shiftIndex + 5) % 8;
                digit <<= shiftIndex;
                digit |= next >> (8 - shiftIndex);
                i++;
            } else {
                digit = (current >> (8 - (shiftIndex + 5))) & 0x1f;
                shiftIndex = (shiftIndex + 5) % 8;

                if(shiftIndex == 0)
                    i++;
            }

            builder.append(charTable.charAt(digit));
        }

	int padding = builder.capacity() - builder.length();

        for(i = 0; i < padding; i++)
            builder.append("=");

        return builder.toString();
    }
}
