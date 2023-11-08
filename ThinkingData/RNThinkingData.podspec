
Pod::Spec.new do |s|
  s.name         = "RNThinkingData"
  s.version      = "3.0.0"
  s.summary      = "RNThinkingData"
  s.description  = <<-DESC
                  RNThinkingData
                   DESC
  s.homepage     = "https://www.thinkingdata.cn"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/author/RNThinkingData.git", :tag => "master" }
  s.source_files  = "ios/**/*.{h,m}"
  s.requires_arc = true
  s.dependency "React"
  s.dependency "TAGameEngine", '0.4.0'

end

