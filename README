# base32

![Travis CI][travis] ![code climate][code]
[travis]: https://travis-ci.org/stesla/base32.png "Travis CI"
[code]: https://codeclimate.com/github/stesla/base32.png "codeclimate"

For Version: 0.1.3

This package contains base32, a Ruby extension for encoding and decoding
in base32 per RFC 3548.

## Download

The latest version of base32 can be found at

http://rubyforge.org/frs/?group_id=3938

## Installation

### Normal Installation

You can install base32 with the following command from the distribution
directory.

  ` rake install`

### Gem Installation

Download and install base32 with the following command.

  ` gem install --remote base32`

### Running the Test Suite

If you want to run the automated tests for base32, issue this command from the
distribution directory.

  ` rake test:all`

## References

* RFC 3548: http://www.faqs.org/rfcs/rfc3548.html

## Simple Example

```rb
  require "base32"

  encoded = Base32.encode("chunky bacon!")  #==> "MNUHK3TLPEQGEYLDN5XCC==="
  decoded = Base32.decode(encoded)          #==> "chunky bacon!"

  puts %Q{"#{decoded}" is "#{encoded}" in base32}
```
