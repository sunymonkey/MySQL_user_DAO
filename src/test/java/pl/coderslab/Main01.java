package pl.coderslab;

import com.sun.security.jgss.GSSUtil;
import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

public class Main01 {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
//        User user = new User();
//        user.setUserName("Arek1");
//        user.setEmail("a1rkadiusz.jozwiak@coderslab.pl");
//        user.setPassword("Pass");
//        userDao.create(user);
//
//        System.out.println(user);

        User user1 = userDao.read(3);
        System.out.println(user1);


    }
}
