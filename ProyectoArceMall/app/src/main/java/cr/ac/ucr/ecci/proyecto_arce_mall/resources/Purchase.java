package cr.ac.ucr.ecci.proyecto_arce_mall.resources;

import java.util.List;

public class Purchase {
    private String userId;
    private String date;
    private List<Product> products;
    private int productCount;
    private int totalPrice;

    public Purchase() {
        this.userId = "";
        this.date = null;
        this.products = null;
        this.productCount = 0;
        this.totalPrice = 0;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
