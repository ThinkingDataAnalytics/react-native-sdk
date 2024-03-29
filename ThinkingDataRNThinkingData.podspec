
Pod::Spec.new do |s|
  s.name         = "RNThinkingData"
  s.version      = "2.2.0"
  s.summary      = "RNThinkingData"
  s.description  = <<-DESC
                  RNThinkingData
                   DESC
  s.homepage     = "https://www.thinkingdata.cn"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNThinkingData.git", :tag => "master" }
  s.source_files  = "ios/**/*.{h,m}"
  s.requires_arc = true
  s.dependency "React"
  s.dependency "ThinkingSDK", '2.8.0.1'
  #s.dependency "others"

end

