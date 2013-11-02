$:.push File.expand_path('../lib', __FILE__)
require 'version'

Gem::Specification.new do |s|
  s.name    = 'base32'
  s.version = Base32::VERSION
  s.authors = ['Samuel Tesla']
  s.email   = 'samuel.tesla@gmail.com'
  s.summary = 'Ruby extension for base32 encoding and decoding'

  s.files         = `git ls-files`.split($/)
  s.executables   = s.files.grep(%r{^bin/}) { |f| File.basename(f) }
  s.test_files    = s.files.grep(%r{^(test|spec|features)/})
  s.require_paths = ['lib']

  s.has_rdoc = true
  s.extra_rdoc_files = ['README']

  s.post_install_message = File.read('UPGRADING') if File.exists?('UPGRADING')

  s.add_development_dependency 'rake'
  s.add_development_dependency 'minitest'
  s.add_development_dependency 'gem-release'
end
