package org.example.service;

import org.example.entity.Order;
import org.example.entity.User;

import java.util.Random;

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
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
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

    public User generateUser() {
        String username = generateRandomStringOfBetweenFiveEightCharacters();
        String countryCode = generateRandomCountryCode();

        User user = new User();

        user.setUsername(username);
        user.setCountryCode(countryCode);

        return user;
    }

    public Order generateOrder() {

    }
}
