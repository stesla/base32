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

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

/**
 * Static class to perform base32 encoding
 *
 * @author Chris Umbel
 */
public class Base32Decoder {
    public static final int[] byteTable = {
            0xff, 0xff, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
            0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
            0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16,
            0x17, 0x18, 0x19, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
            0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
            0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16,
            0x17, 0x18, 0x19, 0xff, 0xff, 0xff, 0xff, 0xff
    };

    public static byte[] decode(String encodedText) {
        int i;
        int shiftIndex = 0;
        int plainDigit;
        int plainPos = 0;
        int encodedByte;

        StringBuilder encodedBuffer = new StringBuilder(encodedText);

        if(encodedBuffer.length() > 0) {
            while(encodedBuffer.charAt(encodedBuffer.length() - 1) == '=')
                encodedBuffer.deleteCharAt(encodedBuffer.length() - 1);
        }

        byte[] buff = new byte[encodedBuffer.length() * 5 / 8];

        for(i = 0; i < encodedBuffer.length(); i++) {
            /* grab the encoded byte out of the input */
            encodedByte =  encodedBuffer.charAt(i) - 0x30;

            if(encodedByte >= 0 && encodedByte < byteTable.length) {
                /* get the raw plaintext value of the encoded byte */
                plainDigit = byteTable[encodedByte];

                if (plainDigit != 0xff) {
                    if(shiftIndex <= 3) {
                        shiftIndex = (shiftIndex + 5) % 8;

                        if(shiftIndex == 0) {
                            buff[plainPos] |= plainDigit;
                            plainPos++;
                        } else
                            buff[plainPos] |= plainDigit << (8 - shiftIndex);
                    } else {
                        shiftIndex = (shiftIndex + 5) % 8;
                        buff[plainPos] |= (plainDigit >>> shiftIndex);
                        plainPos++;

                        if (plainPos >= buff.length)
                            break;

                        buff[plainPos] |= plainDigit << (8 - shiftIndex);
                    }
                }
            }
        }
        
        return buff;
    }

    public static String decodeString(String encodedText) {
        return new String(decode(encodedText));
    }
}
