package org.example.service;

public class GeneratorService {
    private OrderService orderService;
    private UserService userService;

    public GeneratorService(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }
}
