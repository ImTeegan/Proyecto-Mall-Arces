package cr.ac.ucr.ecci.proyecto_arce_mall.resources;

public class Product {
    private int id;
    private String title;
    private String description;
    private int price;
    private double discountPercentage;
    private int stock;
    private String brand;
    private String category;
    private String thumbnail;
    private String[] images;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return String.valueOf(price);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImgid() {
        return images[0];
    }

}
