package org.example.menu;

import org.example.entity.Order;
import org.example.entity.User;
import org.example.service.GeneratorService;
import org.example.service.OrderService;
import org.example.service.UserService;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private final UserService userService;
    private final OrderService orderService;
    private final GeneratorService generatorService;
    private final Scanner scanner = new Scanner(System.in);

    public Menu(UserService userService, OrderService orderService, GeneratorService generatorService) {
        this.userService = userService;
        this.orderService = orderService;
        this.generatorService = generatorService;
    }

    public void menu() {
        while (true) {
            System.out.println("\n========== MENIU ==========");
            System.out.println("1. Genereaza N useri pe care sa ii introduci in baza de date (inserare paralela)");
            System.out.println("2. Genereaza N comenzi pe care sa le introduci in baza de date (inserare paralela)");
            System.out.println("3. Masoara durata de inserare a 100 de useri (inserare paralela)");
            System.out.println("4. Masoara durata de inserare a 100 de orders (inserare paralela)");
            System.out.println("5. Goleste baza de date (stergere Users si Orders)");
            System.out.print("\nSelectati una din optiuni (scrieti numarul, ex: 1): ");
            int option = scanner.nextInt();

            if (option == 1) {
                System.out.print("Dati numarul de useri pe care doriti sa ii inserati in baza de date: ");
                int numberOfGeneratedUsers = scanner.nextInt();
                System.out.println("\nSe incepe inserarea...");

                List<User> users = generatorService.generateUsers(numberOfGeneratedUsers);

                long start = System.nanoTime();
                userService.parallelInsertUsers(users, 16, 10000);
                long end = System.nanoTime();

                long durationMillis = (end - start) / 1_000_000;

                System.out.println("Inserarea celor " + numberOfGeneratedUsers + " useri s-a incheiat cu succes!");
                System.out.println("Durata: " + durationMillis + " milisecunde");
            }

            if (option == 2) {
                int actualNumberOfUsers = userService.count();
                if (actualNumberOfUsers != 0) {
                    System.out.print("Dati numarul de orders pe care doriti sa le inserati in baza de date: ");
                    int numberOfGeneratedOrders = scanner.nextInt();

                    System.out.println("\nSe incepe inserarea...");

                    List<Order> orders = generatorService.generateOrders(numberOfGeneratedOrders, actualNumberOfUsers);

                    long start = System.nanoTime();
                    orderService.parallelInsertOrders(orders, 8, 10000);
                    long end = System.nanoTime();

                    long durationMillis = (end - start) / 1_000_000;

                    System.out.println("Inserarea celor " + numberOfGeneratedOrders + " orders s-a incheiat cu succes!");
                    System.out.println("Durata: " + durationMillis + " milisecunde");
                } else {
                    throw new IllegalStateException("\nTrebuie sa generezi useri inainte de a popula baza de date cu orders!");
                }
            }

            if (option == 3) {
                System.out.println("Se incepe inserarea celor 100 de noi useri (paralel)...");

                List<User> users = generatorService.generateUsers(100);

                long start = System.nanoTime();
                userService.parallelInsertUsers(users, 8, 14);
                long end = System.nanoTime();

                long durationMillis = (end - start) / 1_000_000;

                System.out.println("Inserarea s-a incheiat cu succes!");
                System.out.println("Timp scurs pentru inserarea a 100 de useri: " + durationMillis + " milisecunde");
            }

            if (option == 4) {
                int actualNumberOfUsers = userService.count();

                System.out.println("Se incepe inserarea celor 100 de noi orders (paralel)...");

                List<Order> orders = generatorService.generateOrders(100, actualNumberOfUsers);

                long start = System.nanoTime();
                orderService.parallelInsertOrders(orders, 8, 14);
                long end = System.nanoTime();

                long durationMillis = (end - start) / 1_000_000;

                System.out.println("Inserarea s-a incheiat cu succes!");
                System.out.println("Timp scurs pentru inserarea a 100 de orders: " + durationMillis + " milisecunde");
            }

            if (option == 5) {
                System.out.println("Se incepe stergerea tuturor entitatilor din baza de date...");
                orderService.deleteAllOrders();
                userService.deleteAllUsers();
                System.out.println("Entitatile au fost sterse!");
            }
        }
    }
}
