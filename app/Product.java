public class Product {
    private String name;
    private String price;
    private String description;
    private String imageUrl;
    private float rating;

    public Product() {} // Required for Firestore

    public Product(String name, String description, String price, String imageUrl, float rating) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }

    public void setName(String name) { this.name = name; }
    public void setPrice(String price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setRating(float rating) { this.rating = rating; }
}
