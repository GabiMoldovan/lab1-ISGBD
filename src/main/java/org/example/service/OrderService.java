package org.example.service;

import org.example.entity.Order;
import org.example.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    private static <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> parts = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            parts.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return parts;
    }

    private void shutdownAndAwait(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(1, TimeUnit.HOURS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void parallelInsertOrders(List<Order> orders, int threadCount, int batchSize) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (List<Order> batch : partition(orders, batchSize)) {
            executorService.submit(() -> saveListOfOrders(batch));
        }
        shutdownAndAwait(executorService);
    }
}
