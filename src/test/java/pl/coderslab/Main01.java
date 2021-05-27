package pl.coderslab;

public class Main01 {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        User user = new User();
        user.setUserName("Arek1");
        user.setEmail("a1rkadiusz.jozwiak@coderslab.pl");
        user.setPassword("Pass");
        userDao.create(user);

        UserDao userDao2 = new UserDao();
        User user2 = new User();
        user2.setUserName("M1ateusz");
        user2.setEmail("Ma1teusz.lewandowski@coderslab.pl");
        user2.setPassword("012ls123");
        userDao.create(user2);
    }
}
