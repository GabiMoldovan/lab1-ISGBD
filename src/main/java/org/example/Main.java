package org.example;

import org.example.menu.Menu;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.example.service.GeneratorService;
import org.example.service.OrderService;
import org.example.service.UserService;

public class Main {
    public static void main(String[] args) {
        //TODO: sa rezolv problema cu deschisu-inchisu conexiunii la baza de date la fiecare insert
        //System.out.println("Goodbye, World!");
        OrderRepository orderRepository = new OrderRepository();
        UserRepository userRepository = new UserRepository();

        OrderService orderService = new OrderService(orderRepository);
        UserService userService = new UserService(userRepository);

        GeneratorService generatorService = new GeneratorService(orderService, userService);

        Menu menu = new Menu(userService, orderService, generatorService);

        menu.menu();
    }
}