package domain.models;

public class Order {
    private long order_id;
    private long chatID;
    private int product_id;
    private int count;

    public Order(){

    }
    public Order(long chatID, int product_id, int count) {
        setChatID(chatID);
        setProduct_id(product_id);
        setCount(count);
    }

    public Order(long order_id, long chatID, int product_id, int count) {
        setOrder_id(order_id);
        setChatID(chatID);
        setProduct_id(product_id);
        setCount(count);
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public long getChatID() {
        return chatID;
    }

    public void setChatID(long chatID) {
        this.chatID = chatID;
    }


    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}