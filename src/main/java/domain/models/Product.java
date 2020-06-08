package domain.models;


public class Product {
    private long id;
    private String name;
    private int price;
    private String structure;
    private String photoUrl;


    public Product(long id, String name, int price, String structure, String photoUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.structure = structure;
        this.photoUrl = photoUrl;
    }

    public Product(String name, int price, String structure, String photoUrl) {
        this.name = name;
        this.price = price;
        this.structure = structure;
        this.photoUrl = photoUrl;
    }

    public Product(String name, int price, String photoUrl) {
        this.name = name;
        this.price = price;
        this.photoUrl = photoUrl;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getStructure() {
        return structure;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
