# CHANGELOG

## 0.3.3
- PR #7: Updated README formatting (contributer: codehs)
- PR #8: Include license in gemspec (contributer: flavio)
- PR #9: Detect invalid characters when decoding (contributer: voondo)
- PR #10: Properly initialize character table (contributer: pwnall)
- PR #11: Move version.rb into lib/base32 (contributer: pravi)
- PR #12: Fix deprecation warnings (contributer: kenchan)
- PR #13: Fix build errors (contributor: kenchan)

## 0.3.2

- PR #5: Make padding optional for `random_base32` (contributer: lukesteensen)

## 0.3.1

- PR #4: Fix compatibility with older JRubies (contributor: localshred)

## 0.3.0

- Add `Base32::random_base32` function
- Add test cases for hexadecimal conversion
- Upgrade tests to use Minitest (successor to Test::Unit)
- Add Travis and CodeClimate badges to README
- Add Gemfile and gemspec files, using `gem-release` gem to release
- Track version as a Ruby constant

## 0.2.0 and prior

- Support Base32::encode/decode methods
