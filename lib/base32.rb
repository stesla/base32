require 'openssl'

# Module for encoding and decoding in Base32 per RFC 3548
module Base32
  TABLE = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ234567'.freeze

  class Chunk
    def initialize(bytes)
      @bytes = bytes
    end

    def decode
      bytes = @bytes.take_while {|c| c != 61} # strip padding
      n = (bytes.length * 5.0 / 8.0).floor
      p = bytes.length < 8 ? 5 - (n * 8) % 5 : 0
      c = bytes.inject(0) {|m,o| (m << 5) + Base32.table.index(o.chr)} >> p
      (0..n-1).to_a.reverse.collect {|i| ((c >> i * 8) & 0xff).chr}
    end

    def encode
      n = (@bytes.length * 8.0 / 5.0).ceil
      p = n < 8 ? 5 - (@bytes.length * 8) % 5 : 0
      c = @bytes.inject(0) {|m,o| (m << 8) + o} << p
      [(0..n-1).to_a.reverse.collect {|i| Base32.table[(c >> i * 5) & 0x1f].chr},
       ("=" * (8-n))]
    end
  end

  def self.chunks(str, size)
    result = []
    bytes = str.bytes
    while bytes.any? do
      result << Chunk.new(bytes.take(size))
      bytes = bytes.drop(size)
    end
    result
  end

  def self.encode(str)
    chunks(str, 5).collect(&:encode).flatten.join
  end

  def self.decode(str)
    chunks(str, 8).collect(&:decode).flatten.join
  end

  def self.random_base32(length=16, padding=true)
    random = ''
    OpenSSL::Random.random_bytes(length).each_byte do |b|
      random << self.table[b % 32]
    end
    padding ? random.ljust((length / 8.0).ceil * 8, '=') : random
  end

  def self.table=(table)
    raise ArgumentError, "Table must have 32 unique characters" unless self.table_valid?(table)
    @table = table
  end

  def self.table
    @table || TABLE
  end

  def self.table_valid?(table)
    table.bytes.to_a.size == 32 && table.bytes.to_a.uniq.size == 32
  end
end
