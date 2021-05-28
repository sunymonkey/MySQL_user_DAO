package pl.coderslab;

import com.sun.security.jgss.GSSUtil;
import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

public class Main01 {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        User user = new User();
        user.setUserName("Arek1");
        user.setEmail("a1rkadiusz.jozwiak@coderslab.pl");
        user.setPassword("Pass");
        userDao.create(user);
//
//        System.out.println(user);

        User user1 = userDao.read(3);
        System.out.println(user1);

        User user2 = userDao.read(1);
        System.out.println(user2);

        System.out.println();
        System.out.println();

        User userToUpdate = userDao.read(1);
        System.out.println(userToUpdate);
        userToUpdate.setUserName("Arek");
        userToUpdate.setEmail("arkadiusz.jozwiak@coderslab.pl");
        userToUpdate.setPassword("Nowe");
        userDao.update(userToUpdate);
        User user3= userDao.read(1);
        System.out.println(user3);



    }
}
