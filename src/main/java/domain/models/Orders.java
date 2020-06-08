package domain.models;

public class Orders {
    private int order_id;
    private String korzina;
    private String getType;
    private  String adress;
    private String phone;
    private String status;
    private String oplata;

    public Orders(){

    }

    public Orders(int order_id, String korzina, String getType, String adress, String phone, String status, String oplata) {
        this.order_id = order_id;
        this.korzina = korzina;
        this.getType = getType;
        this.adress = adress;
        this.phone = phone;
        this.status = status;
        this.oplata = oplata;
    }
    public Orders(String korzina, String getType, String adress, String phone, String status, String oplata) {

        this.korzina = korzina;
        this.getType = getType;
        this.adress = adress;
        this.phone = phone;
        this.status = status;
        this.oplata = oplata;
    }


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getKorzina() {
        return korzina;
    }

    public void setKorzina(String korzina) {
        this.korzina = korzina;
    }

    public String getGetType() {
        return getType;
    }

    public void setGetType(String getType) {
        this.getType = getType;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOplata() {
        return oplata;
    }

    public void setOplata(String oplata) {
        this.oplata = oplata;
    }
}
