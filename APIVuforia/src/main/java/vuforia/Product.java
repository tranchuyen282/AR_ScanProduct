package vuforia;

public class Product {
    private String stt;
    private String master_code;
    private String name;
    private String retailer_key;
    private String Quantity;
    private String Price;
    private String Image;

    public Product(String stt, String master_code, String name, String retailer_key, String quantity, String price, String image) {
        this.stt = stt;
        this.master_code = master_code;
        this.name = name;
        this.retailer_key = retailer_key;
        Quantity = quantity;
        Price = price;
        Image = image;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getMaster_code() {
        return master_code;
    }

    public void setMaster_code(String master_code) {
        this.master_code = master_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRetailer_key() {
        return retailer_key;
    }

    public void setRetailer_key(String retailer_key) {
        this.retailer_key = retailer_key;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
