package org.mybatis.jpetstore.mapper;

import java.lang.String;
import java.util.List;
import org.mybatis.jpetstore.domain.Order;

public interface OrderMapper {


List<Order> getOrdersByUsername(String username);

Order getOrder(int orderId);

void insertOrder(Order order);

void insertOrderStatus(Order order);

}