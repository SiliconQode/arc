package proxy;

class RealImage implements Image {
    private String filename;
    public RealImage(String filename) { 
        this.filename = filename;
        loadImageFromDisk();
    }
 
    private void loadImageFromDisk() {
        // Potentially expensive operation
        // ...
        System.out.println("Loading   "+filename);
    }
 
    public void displayImage() { System.out.println("Displaying "+filename); }
}