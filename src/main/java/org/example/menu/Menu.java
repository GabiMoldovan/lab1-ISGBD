package org.example.menu;

import org.example.entity.Order;
import org.example.entity.User;
import org.example.service.GeneratorService;
import org.example.service.OrderService;
import org.example.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private UserService userService;
    private OrderService orderService;
    private GeneratorService generatorService;
    private Scanner scanner = new Scanner(System.in);

    public Menu(UserService userService, OrderService orderService, GeneratorService generatorService) {
        this.userService = userService;
        this.orderService = orderService;
        this.generatorService = generatorService;
    }

    public void menu() {
        while (true) {
            System.out.println("\n========== MENIU ==========");
            System.out.println("1. Genereaza N useri pe care sa ii introduci in baza de date");
            System.out.println("2. Genereaza N comenzi pe care sa le introduci in baza de date");
            System.out.println("3. Masoara durata de inserare a 100 de useri");
            System.out.println("4. Masoara durata de inserare a 100 de orders");
            System.out.println("5. Goleste baza de date (stergere Users si Orders)");
            System.out.println("\nSelectati una din optiuni (scrieti numarul, ex: 1): ");
            int option = scanner.nextInt();

            if (option == 1) {
                System.out.print("Dati numarul de useri pe care doriti sa ii inserati in baza de date: ");
                int numberOfGeneratedUsers = scanner.nextInt();
                System.out.println("\nSe incepe inserarea...");
                generatorService.populateDatabaseWithUsers(numberOfGeneratedUsers);
                System.out.println("Inserarea celor " + numberOfGeneratedUsers + " s-a incheiat cu succes!");
            }
            if (option == 2) {
                if(userService.findAll().size() != 0){
                    System.out.print("Dati numarul de orders pe care doriti sa il inserati in baza de date: ");
                    int numberOfGeneratedOrders = scanner.nextInt();
                    System.out.println("\nSe incepe inserarea...");
                    generatorService.populateDatabaseWithOrders(numberOfGeneratedOrders);
                    System.out.println("Inserarea celor " + numberOfGeneratedOrders + " s-a incheiat cu succes!");
                }
                else throw new IllegalStateException("\nTrebuie sa generezi useri inainte de a popula baza de date cu orders!");
            }
            if (option == 3) {
                System.out.println("Se incepe inserarea celor 100 de noi useri...");
                List<User> users = new ArrayList<>();
                for(int i=0;i<100;i++)
                {
                    User user = generatorService.generateUser();
                    users.add(user);
                }
                LocalDateTime before = LocalDateTime.now();
                generatorService.populateDatabaseWithListOfUsers(users);
                LocalDateTime after = LocalDateTime.now();

                System.out.println("Inserarea s-a incheiat cu succes!");

                Duration duration = Duration.between(before, after);

                long seconds = duration.getSeconds();
                long millis = duration.toMillis();

                System.out.println("\nTimp scurs pentru inserarea a 100 de useri: " + millis + " milisecunde, adica" +
                        seconds + " secunde");
            }
            if (option == 4) {
                System.out.println("Se incepe inserarea celor 100 de noi orders...");
                List<Order> orders = new ArrayList<>();
                for(int i=0;i<100;i++)
                {
                    Order order = generatorService.generateOrder();
                    orders.add(order);
                }
                LocalDateTime before = LocalDateTime.now();
                generatorService.populateDatabaseWithListOfOrders(orders);
                LocalDateTime after = LocalDateTime.now();

                System.out.println("Inserarea s-a incheiat cu succes!");

                Duration duration = Duration.between(before, after);

                long seconds = duration.getSeconds();
                long millis = duration.toMillis();

                System.out.println("\nTimp scurs pentru inserarea a 100 de orders: " + millis + " milisecunde, adica" +
                        seconds + " secunde");
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
