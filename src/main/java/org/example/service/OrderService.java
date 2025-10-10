package org.example.service;

import org.example.entity.Order;
import org.example.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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

    public long saveListOfOrders(List<Order> orders) {
        return orderRepository.saveListOfOrders(orders);
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

    public long parallelInsertOrders(List<Order> orders, int threadCount, int batchSize) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<Long>> futures = new ArrayList<>();

        // Submit tasks in parallel
        for (List<Order> batch : partition(orders, batchSize)) {
            Callable<Long> task = () -> saveListOfOrders(batch);
            futures.add(executorService.submit(task));
        }

        long totalTime = 0L;

        // Collect results from each future
        for(Future<Long> future : futures) {
            try{
                totalTime += future.get(); // Waits for the task to finish and gets the result
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        shutdownAndAwait(executorService);
        return totalTime;
    }
}
