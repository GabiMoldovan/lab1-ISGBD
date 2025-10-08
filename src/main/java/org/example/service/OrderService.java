package org.example.service;

import org.example.entity.Order;
import org.example.repository.OrderRepository;

import java.util.List;

public class OrderService {
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public void delete(Order order) {
        orderRepository.delete(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public int count() {
        List<Order> orders = orderRepository.findAll();
        return orders.size();
    }

    public void deleteAllOrders() {
        orderRepository.deleteAllOrders();
    }

    public void saveListOfOrders(List<Order> orders) {
        orderRepository.saveListOfOrders(orders);
    }
}
