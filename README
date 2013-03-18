= URLcrypt

Ever wanted to securely transmit (not too long) pieces of arbitrary binary data
in a URL? *urlencrypt* makes it easy!

This gem is based on the base32 gem from Samuel Tesla.

URLcrypt uses a 256-bit AES symmetric encryption to securely encrypt data, and
and encodes and decodes Base 62 strings that can be used directly in URLs.

For example, this can be used to securely store user ids and the like when you 
access a web application from a place that doesn't have other authentication
mechanisms, like when you load an image in an email.

URLcrypt uses a modified Base 32 algorithm that doesn't use padding characters,
and doesn't use vowels to avoid bad words in the generated string.

The main reason why Base 32 is useful is that Ruby's built-in Base 64 support
is, IMO, looking ugly in URLs and requires several characters that need to be 
URL-escaped.

Unfortunately, some other gems out there that in theory could handle this 
(like the radix gem) fail with strings that start with a "\0" byte.

*WORD OF WARNING* THERE IS NO GUARANTEE WHATSOEVER THAT THIS GEM IS ACTUALLY
SECURE AND WORKS. USE AT YOUR OWN RISK.

Note: this is version 0.0.1 which doesn't actually come with the encryption part
just yet. It will only work on Ruby 1.8.7 for now.

Patches are welcome, please include tests.

== Installation

Add the `urlcrypt` gem to your Gemfile.

=== Running the Test Suite

If you want to run the automated tests for URLcrypt, issue this command from the
distribution directory.

  % rake test:all

== References

* Base 32: RFC 3548: http://www.faqs.org/rfcs/rfc3548.html

== Simple Example

  encoded = URLcrypt.encode("chunky bacon!")  # => "MNUHK3TLPEQGEYLDN5XCC==="
  decoded = URLcrypt.decode(encoded)          # => "chunky bacon!"