/* Copyright (c) 2007 Samuel Tesla
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

#include "ruby.h"
#include "decoder.h"
#include "encoder.h"

static VALUE
base32_decoder_initialize (VALUE self, VALUE value)
{
  rb_iv_set (self, "@value", rb_str_dup (value));
  return self;
}

static VALUE
base32_decoder_decode (VALUE self)
{
  VALUE value = rb_iv_get (self, "@value");
  value = StringValue (value);
  if (RSTRING (value)->len == 0)
    return value;

  VALUE result = rb_str_new (0, base32_decode_buffer_size (RSTRING (value)->len));
  size_t length = base32_decode ((uint8_t *) RSTRING (result)->ptr, RSTRING (result)->len,
                                 (uint8_t *) RSTRING (value)->ptr, RSTRING (value)->len);
  if (length == 0)
    rb_raise(rb_eRuntimeError, "Value provided not base32 encoded");

  RSTRING (result)->len = length;
  return result;
}

static VALUE
base32_encoder_initialize (VALUE self, VALUE value)
{
  rb_iv_set (self, "@value", rb_str_dup (value));
  return self;
}

static VALUE
base32_encoder_encode (VALUE self)
{
  VALUE value = rb_iv_get (self, "@value");
  value = StringValue(value);

  VALUE result = rb_str_new (0, base32_encoder_buffer_size (RSTRING (value)->len));
  base32_encode ((uint8_t *) RSTRING (result)->ptr, RSTRING (result)->len,
                 (uint8_t *) RSTRING (value)->ptr, RSTRING (value)->len);

  return result;
}

VALUE mBase32;
VALUE cDecoder;
VALUE cEncoder;

void Init_base32_ext ()
{
  mBase32 = rb_define_module ("Base32");

  cDecoder = rb_define_class_under (mBase32, "Decoder", rb_cObject);
  rb_define_method (cDecoder, "initialize", base32_decoder_initialize, 1);
  rb_define_method (cDecoder, "decode", base32_decoder_decode, 0);

  cEncoder = rb_define_class_under (mBase32, "Encoder", rb_cObject);
  rb_define_method (cEncoder, "initialize", base32_encoder_initialize, 1);
  rb_define_method (cEncoder, "encode", base32_encoder_encode, 0);
}
