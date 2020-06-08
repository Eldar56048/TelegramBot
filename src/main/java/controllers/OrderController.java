package controllers;


import com.sun.org.apache.xpath.internal.operations.Or;
import domain.models.Order;
import domain.models.Product;
import repositories.OrderRepository;
import repositories.ProductRepository;

import java.util.ArrayList;
import java.util.stream.Stream;

public class OrderController {
    ArrayList<Product>products=new ArrayList<>();
    Order order;

    public ArrayList<Order> NewOrder(long chatID,int product_id,int count,ArrayList <Order> orders){

       return NewOrder(chatID,product_id,count,orders.size(),orders);

    }

    private ArrayList<Order> NewOrder(long chatID,int product_id,int count,int orderrsize ,ArrayList <Order> orders){
        order=new Order(chatID,product_id,count);
        orders.add(order);
        return orders;
    }

    public boolean Filter(int product_id,long chat_id,ArrayList <Order> orders){
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).getProduct_id()==product_id){
                return true;
            }
        }
        return false;

    }


    public String seeKorzina(long chatID,ArrayList <Order> orders){
        return  seeKorzina(chatID,orders.size(),orders);

    }
    private String seeKorzina(long chatID,int ordersize ,ArrayList <Order> orders){
        ProductRepository  productRepository=new ProductRepository();
        products=productRepository.FillProduct();
        int sum=0;
        String korzina="";

        for (int i=0;i<ordersize;i++){
            if(orders.get(i).getChatID()==chatID){
                for(int j=0;j<products.size();j++){
                    if(orders.get(i).getProduct_id()==products.get(j).getId()){
                        korzina+="Имя товара: "+products.get(j).getName()+"\n";
                        korzina+="Количество: "+orders.get(i).getCount()+"\n";
                        korzina+="Цена зо один товар: "+products.get(j).getPrice()+"\n"+"\n"+"\n";
                        sum+=products.get(j).getPrice()*orders.get(i).getCount();
                    }
                }
            }
        }
        korzina+="Итого: "+sum;
        return korzina;
    }

    public int countOrders(long chatID,ArrayList<Order> orders,long productID){
        ProductRepository  productRepository=new ProductRepository();
        products=productRepository.FillProduct();
        int sum=0;
        for (int i=0;i<orders.size();i++){
            if((orders.get(i).getChatID()==chatID)&&(productID==orders.get(i).getProduct_id())){
                System.out.println(orders.get(i).getCount());
                       return orders.get(i).getCount();




        }

    }
        return 0;
    }

    public int getPrice(long productID){
        ProductRepository  productRepository=new ProductRepository();
        products=productRepository.FillProduct();
        for (int i=0;i<products.size();i++){
            if(products.get(i).getId()==productID){
                return products.get(i).getPrice();
            }
        }
        return 0;
    }




    public ArrayList<Order> addCount(long chatID,int product_id,ArrayList <Order> orders){
        addCount(chatID,orders.size(),product_id,orders);
        return orders;
    }

    private ArrayList<Order> addCount(long chatID,int size,int product_id,ArrayList <Order> orders){
        for(int i=0;i<size;i++){
            if((orders.get(i).getChatID()==chatID)&&(orders.get(i).getProduct_id()==product_id)){
                int count=orders.get(i).getCount();
                orders.get(i).setCount(++count);
            }
        }
        return orders;
    }

    public ArrayList<Order> minusCount(long chatID,ArrayList <Order> orders){
       return   minusCount(chatID,orders.size(),orders);
    }

    private ArrayList<Order> minusCount(long chatID,int size,ArrayList <Order> orders){
        for(int i=0;i<size;i++){
            if(orders.get(i).getChatID()==chatID){
                int count=orders.get(i).getCount();
                orders.get(i).setCount(count--);
            }
        }
        return orders;
    }



    public String removeOrder(long chatID, ArrayList<Order> orders){

        String korzina = "Итого: 0" ;
        return korzina;
    }



}