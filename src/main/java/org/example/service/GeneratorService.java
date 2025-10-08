package org.example.service;

import org.example.entity.Order;
import org.example.entity.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GeneratorService {
    private OrderService orderService;
    private UserService userService;

    public GeneratorService(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    public int generateRandomNumberBetween(int min, int max) {
        // range = [min, max]
        Random random = new Random();
        return random.nextInt(min, max+1);
    }

    public String generateRandomStringOfBetweenFiveEightCharacters() {
        Random random = new Random();
        int leftLimit = 97; // ASCII CODE for letter 'a'
        int rightLimit = 122; // ASCII CODE for letter 'z'
        int targetStringLength = generateRandomNumberBetween(5, 8);
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

    public String generateRandomCountryCode() {
        String[] countries = {"RO", "FR", "DE", "IT", "ES", "US", "JP", "BR"};

        Random random = new Random();
        int number = random.nextInt(0, 8);

        return countries[number];
    }

    public String generateRandomOrderStatus() {
        String[] status = {"PENDING", "COMPLETED", "CANCELLED", "RETURNED", "FAILED",
                            "CANCELLED", "REJECTED", "REFUNDED"};

        Random random = new Random();
        int number = random.nextInt(0, 8);

        return status[number];

    }

    public BigDecimal generateRandomBigDecimal(BigDecimal min, BigDecimal max) {
        double randomDouble = ThreadLocalRandom.current()
                .nextDouble(min.doubleValue(), max.doubleValue());

        return BigDecimal.valueOf(randomDouble)
                .setScale(2, RoundingMode.HALF_UP);
    }


    public User generateUser() {
        String username = generateRandomStringOfBetweenFiveEightCharacters();
        String countryCode = generateRandomCountryCode();

        User user = new User();

        user.setUsername(username);
        user.setCountryCode(countryCode);

        return user;
    }

    public Order generateOrder() {
        BigDecimal min = new BigDecimal("1");
        BigDecimal max = new BigDecimal("2500");

        BigDecimal amount = generateRandomBigDecimal(min, max);
        String status = generateRandomOrderStatus();

        int countUsers = userService.count();
        Long assignOrderToUser = (long) generateRandomNumberBetween(0, countUsers);

        Order order = new Order();

        order.setOrderTotal(amount);
        order.setStatus(status);
        order.setUser(userService.findById(assignOrderToUser));

        return order;
    }

    public void populateDatabaseWithUsers(int numberOfUsers) {
        for(int i = 0; i < numberOfUsers; i++) {
            User user = generateUser();
            userService.save(user);
        }
    }

    public void populateDatabaseWithOrders(int numberOfOrders) {
        for(int i = 0; i < numberOfOrders; i++) {
            Order order = generateOrder();
            orderService.save(order);
        }
    }
}
