package pl.coderslab;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.entity.ConsoleColors;
import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main02 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            menuPrint();
            String menu = scanner.nextLine().trim();
            switch (menu) {
                case "add" -> addUser();
                case "update" -> updateUser();
                case "remove" -> removeUser();
                case "print users" -> printUsers();
                case "list all" -> readAll();
                case "find" -> findUser();
                case "exit" -> {
                    System.out.println(ConsoleColors.RED + "Bye, bye !");
                    return;
                }default -> System.out.println(ConsoleColors.RED_BOLD + "Wrong command" + ConsoleColors.RESET);
            }
        }
    }

    private static void updateUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj id użytkownika którego chcesz zaktualizować:");
        int id = idRead();
        UserDao userDao = new UserDao();
        User user = userDao.read(id);
        if (user != null) {
            update(scanner, id, userDao, user);
        } else {
            System.out.println("Id nie istnieje");
        }
    }

    private static void update(Scanner scanner, int id, UserDao userDao, User user) {
        printUser(user);
        System.out.println("Który parametr chcesz zaktualizować ? Wpisz: email, name, password");
        String menu = scanner.nextLine().trim();
        int temp = 0;
        switch (menu) {
            case "email" -> {
                System.out.println("Wpisz nowy email:");
                String email = scanner.nextLine().trim();
                user.setEmail(email);
                temp = 1;
            } case "name" -> {
                System.out.println("Wpisz nowy name:");
                String name = scanner.nextLine().trim();
                user.setUserName(name);
                temp = 1;
            } case "password" -> {
                System.out.println("Wpisz stare hasło:");
                String password = scanner.nextLine().trim();
                if (BCrypt.checkpw(password, user.getPassword())) {
                    System.out.println("Wprowadz nowe haslo: ");
                    password = scanner.nextLine().trim();
                    user.setPassword(password);
                    temp = 1;
                } else {
                    System.out.println("Błędne hasło, aktualizacja niemożliwa");
                }
            } default -> System.out.println(ConsoleColors.RED_BOLD + "Wrong command" + ConsoleColors.RESET);
        }
        if (userDao.update(user) && temp == 1) {
            user = userDao.read(id);
            System.out.println("Dane po aktualizacji");
            printUser(user);
        }
    }

    private static void printUser(User user) {
        headUsersTable();
        System.out.println(user);
    }

    private static void printUsers() {
        UserDao userDao = new UserDao();
        System.out.println("Podaj id użytkownika: ");
        int id = idRead();
        User users = userDao.read(id);
        if (users != null) {
            printUser(users);
        } else {
            System.out.println("Brak wpisu w bazie");
        }
    }

    private static void readAll() {
        UserDao userDao = new UserDao();
        User[] users = userDao.findAll();
        printDatabase(users);
    }

    private static void findUser() {
        System.out.println("Po czym chcesz szukać użytkowników ? Wpisz email lub name");
        Scanner scanner = new Scanner(System.in);
        String menu = scanner.nextLine().trim();
        switch (menu) {
            case "email" -> emailFind();
            case "name" -> nameFind();
            default -> System.out.println(ConsoleColors.RED_BOLD + "Wrong command" + ConsoleColors.RESET);
        }
    }

    private static void nameFind() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Wpisz szukana nazwę użytkownika:");
        String name = scanner.nextLine().trim();
        UserDao userDao = new UserDao();
        User[] users = userDao.findName(name);
        printDatabase(users);
    }

    private static void emailFind() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Wpisz szukana nazwę użytkownika:");
        String name = scanner.nextLine().trim();
        UserDao userDao = new UserDao();
        User[] users = userDao.findEmail(name);
        printDatabase(users);
    }

    private static void removeUser() {
        System.out.println("Wpisz którego użytkownika chcesz usunąć:");
        int id = idRead();
        UserDao userDao = new UserDao();
        if (userDao.delete(id)){
            System.out.println("Wpis usunięty");
        } else {
            System.out.println(ConsoleColors.RED_BOLD + "Wpis nie usuniety" + ConsoleColors.RESET);
        }
    }

    private static int idRead() {
        Scanner scanner = new Scanner(System.in);
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println(ConsoleColors.RED_BOLD + "Wprowadzono błędny format danych !" + ConsoleColors.RESET);
        }
        return 0;
    }

    private static void addUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Wpisz użytkownika");
        String username = scanner.nextLine().trim();

        System.out.println("Wpisz email");
        String email = scanner.nextLine().trim();

        System.out.println("Wpisz hasło");
        String password = scanner.nextLine().trim();

        UserDao userDao = new UserDao();
        userDao.create(new User(username, email, password));
    }

    private static void menuPrint() {
        System.out.println("-".repeat(10));
        System.out.println(ConsoleColors.BLUE_BOLD + "Please select an option: " + ConsoleColors.RESET);
        System.out.println("add");
        System.out.println("update");
        System.out.println("remove");
        System.out.println("find");
        System.out.println("print users");
        System.out.println("list all");
        System.out.println("exit");
    }

    private static void printDatabase(User[] users) {
        if (users.length != 0) {
            headUsersTable();
            for (User element:users) {
                System.out.println(element);
            }
        } else {
            System.out.println("Brak danych w bazie");
        }
    }

    private static void headUsersTable() {
        System.out.printf("%3s. %-15s %-35s%n", "id", "user name", "email");
    }
}
