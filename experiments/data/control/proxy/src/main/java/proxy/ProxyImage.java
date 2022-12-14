package proxy;

class ProxyImage implements Image {
    private String filename;
    private RealImage image;
 
    public ProxyImage(String filename) { this.filename = filename; }
    public void displayImage() {
        if (image == null) {
            image = new RealImage(filename); // load only on demand
        }
        image.displayImage();
    }
}