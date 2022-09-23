package cr.ac.ucr.ecci.proyecto_arce_mall.resources;

public class Product {
    // string productName for storing productName
    // and imgid for storing image id.
    private String productName;
    private String productPrice;
    private int imgid;

    public Product(String productName, String productPrice, int imgid) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.imgid = imgid;
    }

    public String getproductName() {
        return productName;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }

    public String getproductPrice() {
        return productPrice;
    }

    public void setproductPrice(String productPrice) {
        this.productPrice = productName;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
}
