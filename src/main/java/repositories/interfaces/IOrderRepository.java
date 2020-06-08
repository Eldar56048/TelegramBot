package repositories.interfaces;

import domain.models.Orders;

public interface IOrderRepository extends  IEntityRepository<Orders>{


    void add(String korzina, String getType, String adress, String phone, String status, String oplata);
}
