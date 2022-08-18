# reads each line of the file and checks the line, then rewinds the file
def service_and_protocol service, protocol, file
  flag = false
  file.each do |line|
    flag = flag || checkline(line, service, protocol)
  end

  puts "#{service}/#{protocol}: No such service" unless flag

  file.rewind
end

# Handles the service arguments
def handle_service_args file
  if ARGV.length > 2
    (2...ARGV.length).each do |x|
      service = ARGV[x]

      if service =~ /(\w+)\/(\w+)/
        service_and_protocol($1, $2, file)
        puts ""
      else
        service_and_protocol(service, "tcp", file)
        puts ""
        service_and_protocol(service, "udp", file)
        puts ""
      end
    end
  end
end

# Print the contents of the line in the required format
def print_contents service, srvc, port, proto, aliases, comment
  puts "#{service}/#{proto}: #{port}"
  if aliases.include? service
    puts "#{service}/#{proto}: Alias for #{srvc}"
  end
  if aliases.length > 0
    print "#{service}/#{proto}: "
    if aliases.include? service
      print "Other "
    end
    print "Aliases "
    aliases.each { |a| print "#{a} " unless a == service }
    print "\n"
  end
  if comment != ""
    puts "#{service}/#{proto}: #{comment}"
  end
end

# checks the line for the service and protocol, and extracts the necessary info
def checkline(line, service, protocol)
  if line =~ /^(\w+)\s+(\d+)\/(\w{3})\s+(.+?)\s+(# .*)?$/
    srvc = $1
    port = $2
    proto = $3
    aliases = $4.split(" ")
    comment = $5

    if (srvc == service or aliases.include? service) and proto == protocol
      print_contents(service, srvc, port, proto, aliases, comment)
      return true
    end
    return false
  end
end

# Main function of the script
def main()
  if ARGV.length == 0
    puts "No arguments provided..."
    puts "Usage: ruby services.rb -s file_name [service/protocol] ..."

    exit(0)
  elsif ARGV[0] == "-h"
    puts """Usage: ruby services.rb -s file_name [service list]\n
Options:
  -h Prints this message
  -s file_name\tThe name of the service file to extract info from

Arguments:
  service list\tA list of the services and their optional protocol
              \tin the form service/protocol where the protocol can
              \tbe either udp or tcp.
"""
    exit(0)
  elsif ARGV[0] == "-s" and ARGV.length > 1
    file_name = ARGV[1]
    file = File.open(file_name)

    handle_service_args(file)

    file.close
  end
end

if __FILE__==$0
	main()
end
