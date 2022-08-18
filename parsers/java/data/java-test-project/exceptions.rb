def first_line(filename)
  file = open(filename, "w")
  begin
    file.write("Hello World")
  rescue
    puts "An error occurred: #{$!}"
  ensure
    file.close
  end
end

first_line("test.txt")file = open(filename, "w")
