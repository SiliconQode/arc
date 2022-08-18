# define a class
class Box
  attr_reader :width, :height
  attr_writer :width, :height

  # class constants
  BOX_COMPANY = "TATA Inc"
  BOXWEIGHT = 10

  # class variables
  @@count = 0

  # constructor method
  def initialize(w, h)
    @width, @height = w, h

    @@count = 1
  end

  # accessor methods
  def width
    @width
  end

  def height
    @height
  end

  # mutator methods
  def width=width
    @width = width
  end

  def height=height
    @height = height
  end

  #private :getWidth, :getHeight

  # instance methods
  def getArea
    @width * @height
  end

  def self.printCount()
    puts "Box count is: #{@@count}"
  end

  # define to_s methods
  def to_s
    "(w:#{@width}, h:#{@height})" # string formatting of the object
  end
end

class BigBox < Box

  def getArea
    @area = @width * @height
    puts "Big box area is: #{@area}"
  end
end

# create an object
box = Box.new(10, 20)

# use mutator methods
box.width = 15
box.height = 25

# use accessor methods
x = box.width()
y = box.height()

puts "Width of the box is: #{x}"
puts "Height of the box is: #{y}"

# call instance methods
a = box.getArea()
puts "Area of the box is: #{a}"

box2 = Box.new(30, 100)

Box.printCount()

puts "String representation fo box is: #{box}"

box = BigBox.new(10, 20)
box.getArea()

puts Box::BOX_COMPANY
