/* Copyright (c) 2007-2011 Samuel Tesla
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#include <stdlib.h>
#include "ruby.h"
#include "decoder.h"
#include "encoder.h"

/*
 * call-seq:
 *   Base32.decode(encoded_string) -> string
 *
 * Decodes a string that is encoded in base32.  Will throw an ArgumentError if
 * it cannot successfully decode it.
 */
static VALUE
b32_decode (VALUE self, VALUE value)
{
  value = StringValue (value);
  if (RSTRING_LEN (value) == 0)
    return value;

  size_t buflen = base32_decode_buffer_size (RSTRING_LEN (value));
  char *buffer = (char *) malloc (buflen);
#ifdef TEST
  memset(buffer, 0xff, buflen);
#else
  memset(buffer, 0x00, buflen);
#endif

  size_t length = base32_decode ((uint8_t *) buffer, buflen,
                                 (uint8_t *) RSTRING_PTR (value), RSTRING_LEN (value));

  if (length == 0) {
    free(buffer);
    rb_raise(rb_eRuntimeError, "Value provided not base32 encoded");
  }

  VALUE result = rb_str_new (0, length);
  memcpy(RSTRING_PTR (result), buffer, length);
  free(buffer);
  return result;
}

/*
 * call-seq:
 *   Base32.encode(string) -> encoded_string
 *
 * Encodes a string in base32.
 */
static VALUE
b32_encode (VALUE self, VALUE value)
{
  value = StringValue(value);

  VALUE result = rb_str_new (0, base32_encoder_buffer_size (RSTRING_LEN (value)));
#ifdef TEST
  memset(RSTRING_PTR (result), 0xff, RSTRING_LEN (result));
#endif
  base32_encode ((uint8_t *) RSTRING_PTR (result), RSTRING_LEN (result),
                 (uint8_t *) RSTRING_PTR (value), RSTRING_LEN (value));

  return result;
}

#ifdef TEST
static VALUE
b32_test_strlen (VALUE self, VALUE value)
{
  return UINT2NUM (strlen (RSTRING_PTR (value)));
}
#endif


VALUE mBase32;
#ifdef TEST
VALUE mBase32Test;
#endif

void Init_base32 ()
{
  mBase32 = rb_define_module ("Base32");
  rb_define_module_function(mBase32, "decode", b32_decode, 1);
  rb_define_module_function(mBase32, "encode", b32_encode, 1);
#ifdef TEST
  mBase32Test = rb_define_module ("Base32Test");
  rb_define_module_function(mBase32Test, "strlen", b32_test_strlen, 1);
#endif
}
