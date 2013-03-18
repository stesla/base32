# URLcrypt

Ever wanted to securely transmit (not too long) pieces of arbitrary binary data
in a URL? **URLcrypt** makes it easy!

This gem is based on the [base32](https://github.com/stesla/base32) gem from Samuel Tesla.

URLcrypt uses **256-bit AES symmetric encryption** (not just yet, coming in v0.0.2)
to securely encrypt data, and encodes and decodes **Base 32 strings that can be used directly in URLs**.

This can be used to securely store user ids, download expiration dates and 
other arbitrary data like that when you access a web application from a place 
that doesn't have other authentication or persistence mechanisms (like cookies):
 
  * Loading a generated image from your web app in an email
  * Links that come with an expiration date (à la S3)
  * Mini-apps that don't persist data on the server

**Note:** this is version 0.0.1 which doesn't actually come with the encryption part
just yet. It will only work on Ruby 1.8.7 for now.

**Important**: As a general guideline, URL lengths shouldn't exceed about 2000 
characters in length, as URLs longer than that will not work in some browsers
and with some (proxy) servers. This limits the amount of data you can store
with URLcrypt.

**WORD OF WARNING: THERE IS NO GUARANTEE WHATSOEVER THAT THIS GEM IS ACTUALLY SECURE AND WORKS. USE AT YOUR OWN RISK.**

Patches are welcome; please include tests!

## Installation

Add the `urlcrypt` gem to your Gemfile.

## Simple Example

```ruby
URLcrypt.encode('chunky bacon!')          # => "mnAhk6tlp2qg2yldn8xcc"
URLcrypt.decode('mnAhk6tlp2qg2yldn8xcc')  # => "chunky bacon!"
```

## Running the Test Suite

If you want to run the automated tests for URLcrypt, issue this command from the
distribution directory.

```
% rake test:all
```

## Why not Base 64, or an other radix/base library?

URLcrypt uses a modified Base 32 algorithm that doesn't use padding characters,
and doesn't use vowels to avoid bad words in the generated string.

The main reason why Base 32 is useful is that Ruby's built-in Base 64 support
is, IMO, looking ugly in URLs and requires several characters that need to be 
URL-escaped.

Unfortunately, some other gems out there that in theory could handle this 
(like the radix gem) fail with strings that start with a "\0" byte.


## References

* Base 32: RFC 3548: http://www.faqs.org/rfcs/rfc3548.html