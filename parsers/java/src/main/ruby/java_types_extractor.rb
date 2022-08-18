#!/usr/bin/env ruby
#
# The MIT License (MIT)
#
# MSUSEL Java Parser
# Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
# Software Engineering Laboratory and Idaho State University, Informatics and
# Computer Science, Empirical Software Engineering Laboratory
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#


require 'uri'
require 'nokogiri'
require 'open-uri'
require 'mechanize'

class JavaTypesExtractor
end

def process_package(name, url)
  file = File.open("data/#{name}.types", "w")

  begin
    file.puts "package #{name}"
    file.puts ""

    agent = Mechanize.new { |agent| agent.user_agent_alias = 'Mac Safari' }

    page = agent.get("#{$base_url}#{url}")
    page.search("//table[@class='typeSummary']//td[@class='colFirst']/a").each do |node|
      type = node['title'].split(" ")[0]
      name = node.text.split("<")[0]
      file.puts "#{type} #{name}"
    end
  rescue StandardError => error
    puts "Error: #{error}"
  ensure
    file.close
  end
end

$base_url = "https://docs.oracle.com/javase/8/docs/api/"

def main()
  retries = 0
  begin
    agent = Mechanize.new { |agent| agent.user_agent_alias = 'Mac Safari' }

    agent.agent.http.verify_mode = OpenSSL::SSL::VERIFY_NONE

    puts "Opening Page: #{$base_url}overview-summary.html"
    page = agent.get("#{$base_url}overview-summary.html")

    packages = {}

    page.search("//td[@class='colFirst']/a").each do |node|
      key = node.text
      val = node['href']
      puts "found package: #{key}@#{val}"
      packages[key] = val
    end

    packages.each do |key, val|
      process_package key, val
    end

  rescue StandardError => error
    puts "Error: #{error}"
    if (retries < 5)
      Kernel.sleep(1)
      retries += 1
      retry
    end
  end
end

if __FILE__ == $0
  main()
end