# URLcrypt

[![Build Status](https://travis-ci.org/madrobby/URLcrypt.png?branch=master)](https://travis-ci.org/madrobby/URLcrypt)

Ever wanted to securely transmit (not too long) pieces of arbitrary binary data
in a URL? **URLcrypt** makes it easy!

This gem is based on the [base32](https://github.com/stesla/base32) gem from Samuel Tesla.

URLcrypt uses **256-bit AES symmetric encryption**
to securely encrypt data, and encodes and decodes 
**Base 32 strings that can be used directly in URLs**.

This can be used to securely store user ids, download expiration dates and 
other arbitrary data like that when you access a web application from a place 
that doesn't have other authentication or persistence mechanisms (like cookies):
 
  * Loading a generated image from your web app in an email
  * Links that come with an expiration date (à la S3)
  * Mini-apps that don't persist data on the server

Works with Ruby 1.8, 1.9 and 2.0.

**Important**: As a general guideline, URL lengths shouldn't exceed about 2000 
characters in length, as URLs longer than that will not work in some browsers
and with some (proxy) servers. This limits the amount of data you can store
with URLcrypt.

**WORD OF WARNING: THERE IS NO GUARANTEE WHATSOEVER THAT THIS GEM IS ACTUALLY SECURE AND WORKS. USE AT YOUR OWN RISK.**

Patches are welcome; please include tests!

## Installation

Add `urlcrypt` to your Gemfile.

## Example

```ruby
# encrypt and encode with 256-bit AES
# one-time setup, set this to a securely random key with at least 256 bits, see below
URLcrypt::key = '...' 

# now encrypt and decrypt!
URLcrypt::encrypt('chunky bacon!')        # => "sgmt40kbmnh1663nvwknxk5l0mZ6Av2ndhgw80rkypnp17xmmg5hy"
URLcrypt::decrypt('sgmt40kbmnh1663nvwknxk5l0mZ6Av2ndhgw80rkypnp17xmmg5hy')
  # => "chunky bacon!"

# encoding without encryption (don't use for anything sensitive!), doesn't need key set
URLcrypt.encode('chunky bacon!')          # => "mnAhk6tlp2qg2yldn8xcc"
URLcrypt.decode('mnAhk6tlp2qg2yldn8xcc')  # => "chunky bacon!"
```

### Generating keys

The easiest way to generate a secure key is to use `rake secret` in a Rails app:

```sh
$ rake secret
ba7f56f8f9873b1653d7f032cc474938fd749ee8fbbf731a7c41d698826aca3cebfffa832be7e6bc16eaddc3826602f35d3fd6b185f261ee8b0f01d33adfbe64
```

To use the key with URLcrypt, you'll need to convert that from a hex string into a real byte array:

```ruby
URLcrypt::key = ['longhexkeygoeshere'].pack('H*')
```

## Running the Test Suite

If you want to run the automated tests for URLcrypt, issue this command from the
distribution directory.

```sh
$ rake test:all
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