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

import org.jruby.Ruby;
import org.jruby.RubyModule;
import org.jruby.RubyString;
import org.jruby.RubyFixnum;
import org.jruby.runtime.Block;
import org.jruby.runtime.CallbackFactory;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * The actual module containing the methods jruby will use
 *
 * @author Chris Umbel
 */
public class Base32Module {
    public static IRubyObject encode(IRubyObject recv, IRubyObject plainText, Block unusedBlock) {
        return RubyString.newString(recv.getRuntime(), Base32Encoder.encode(plainText.asJavaString()));
    }

    public static IRubyObject decode(IRubyObject recv, IRubyObject encodedText, Block unusedBlock) {
        return RubyString.newString(recv.getRuntime(), Base32Decoder.decodeString(encodedText.asJavaString()));
    }

    public static IRubyObject strlen(IRubyObject recv, IRubyObject string, Block unusedBlock) {
        return RubyFixnum.newFixnum(recv.getRuntime(), string.asJavaString().length());
    }

    public static void init(Ruby runtime) {
        RubyModule base32Module = runtime.defineModule("Base32");
        CallbackFactory callbackFactory = runtime.callbackFactory(Base32Module.class);
        base32Module.defineModuleFunction("encode", callbackFactory.getSingletonMethod("encode", IRubyObject.class));
        base32Module.defineModuleFunction("decode", callbackFactory.getSingletonMethod("decode", IRubyObject.class));

        base32Module = runtime.defineModule("Base32Test");
        base32Module.defineModuleFunction("strlen", callbackFactory.getSingletonMethod("strlen", IRubyObject.class));
    }
}
