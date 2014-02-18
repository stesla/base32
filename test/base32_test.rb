gem 'minitest'
require 'minitest/autorun'
require File.dirname(__FILE__) + '/../lib/base32.rb'

class TestBase32 < Minitest::Test
  def assert_decoding(encoded, plain)
    decoded = Base32.decode(encoded)
    assert_equal(plain, decoded)
  end

  def assert_encoding(encoded, plain)
    actual = Base32.encode(plain)
    assert_equal(encoded, actual)
  end

  def assert_encode_and_decode(encoded, plain)
    assert_encoding(encoded, plain)
    assert_decoding(encoded, plain)
  end

  def assert_hex_encode_and_decode(encoded, hex)
    plain = [hex].pack('H*')
    assert_encode_and_decode(encoded, plain)
  end

  def test_empty_string
    assert_encode_and_decode('', '')
  end

  def test_a
    assert_encode_and_decode('ME======', 'a')
  end

  def test_12345
    assert_encode_and_decode('GEZDGNBV', '12345')
  end

  def test_abcde
    assert_encode_and_decode('MFRGGZDF', 'abcde')
  end

  def test_constitution_preamble
    plaintext =<<-EOT
      We the people of the United States, in order to form a more perfect union,
      establish justice, insure domestic tranquility, provide for the common
      defense, promote the general welfare, and secure the blessings of liberty
      to ourselves and our posterity, do ordain and establish this Constitution
      for the United States of America.
    EOT
    encoded = %W(
      EAQCAIBAEBLWKIDUNBSSA4DFN5YGYZJAN5TCA5DIMUQFK3TJORSWIICTORQXIZLTFQQGS3RA
      N5ZGIZLSEB2G6IDGN5ZG2IDBEBWW64TFEBYGK4TGMVRXIIDVNZUW63RMBIQCAIBAEAQGK43U
      MFRGY2LTNAQGU5LTORUWGZJMEBUW443VOJSSAZDPNVSXG5DJMMQHI4TBNZYXK2LMNF2HSLBA
      OBZG65TJMRSSAZTPOIQHI2DFEBRW63LNN5XAUIBAEAQCAIDEMVTGK3TTMUWCA4DSN5WW65DF
      EB2GQZJAM5SW4ZLSMFWCA53FNRTGC4TFFQQGC3TEEBZWKY3VOJSSA5DIMUQGE3DFONZWS3TH
      OMQG6ZRANRUWEZLSOR4QUIBAEAQCAIDUN4QG65LSONSWY5TFOMQGC3TEEBXXK4RAOBXXG5DF
      OJUXI6JMEBSG6IDPOJSGC2LOEBQW4ZBAMVZXIYLCNRUXG2BAORUGS4ZAINXW443UNF2HK5DJ
      N5XAUIBAEAQCAIDGN5ZCA5DIMUQFK3TJORSWIICTORQXIZLTEBXWMICBNVSXE2LDMEXAU===).join
    assert_encode_and_decode(encoded, plaintext)
  end

  def test_hex_byte_encoding
    assert_hex_encode_and_decode('FA======', '28')
    assert_hex_encode_and_decode('2Y======', 'd6')
    assert_hex_encode_and_decode('234A====', 'd6f8')
    assert_hex_encode_and_decode('234AA===', 'd6f800')
    assert_hex_encode_and_decode('234BA===', 'd6f810')
    assert_hex_encode_and_decode('234BCDA=', 'd6f8110c')
    assert_hex_encode_and_decode('234BCDEA', 'd6f8110c80')
    assert_hex_encode_and_decode('234BCDEFGA======', 'd6f8110c8530')
    assert_hex_encode_and_decode('234BCDEFG234BCDEFE======', 'd6f8110c8536b7c0886429')
  end

  def test_random_base32
    assert_equal(16, Base32.random_base32.length)
    assert_match(/^[A-Z2-7]+$/, Base32.random_base32)
  end

  def test_random_base32_length
    assert_equal(32, Base32.random_base32(32).length)
    assert_equal(40, Base32.random_base32(40).length)
    assert_equal(32, Base32.random_base32(29).length)
    assert_match(/^[A-Z2-7]{1}={7}$/, Base32.random_base32(1))
    assert_match(/^[A-Z2-7]{29}={3}$/, Base32.random_base32(29))
  end

  def test_random_base32_padding
    assert_equal(32, Base32.random_base32(32, false).length)
    assert_equal(40, Base32.random_base32(40, false).length)
    assert_equal(29, Base32.random_base32(29, false).length)
    assert_match(/^[A-Z2-7]{1}$/, Base32.random_base32(1, false))
    assert_match(/^[A-Z2-7]{29}$/, Base32.random_base32(29, false))
  end

  def test_assign_new_table
    new_table =  'abcdefghijklmnopqrstuvwxyz234567'
    Base32.table = new_table
    assert_equal(new_table, Base32.table)
    Base32.table = Base32::TABLE # so as not to ruin other tests
  end

  def test_check_table_length
    assert_raises(ArgumentError) { Base32.table = ('a' * 31) }
    assert_raises(ArgumentError) { Base32.table = ('a' * 32) }
    assert_raises(ArgumentError) { Base32.table = ('a' * 33) }
    assert_raises(ArgumentError) { Base32.table = ('abcdefghijklmnopqrstuvwxyz234567' * 2) }
    Base32.table = Base32::TABLE # so as not to ruin other tests
  end

  def test_encode_decode_with_alternate_table
    Base32.table = 'abcdefghijklmnopqrstuvwxyz234567'
    assert_hex_encode_and_decode('fa======', '28')
    assert_hex_encode_and_decode('2y======', 'd6')
    assert_hex_encode_and_decode('234a====', 'd6f8')
    assert_hex_encode_and_decode('234aa===', 'd6f800')
    assert_hex_encode_and_decode('234ba===', 'd6f810')
    assert_hex_encode_and_decode('234bcda=', 'd6f8110c')
    assert_hex_encode_and_decode('234bcdea', 'd6f8110c80')
    assert_hex_encode_and_decode('234bcdefga======', 'd6f8110c8530')
    assert_hex_encode_and_decode('234bcdefg234bcdefe======', 'd6f8110c8536b7c0886429')
    Base32.table = Base32::TABLE # so as not to ruin other tests
  end

end
