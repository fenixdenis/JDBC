package jm.task.core.jdbc;


import jm.task.core.jdbc.service.UserServiceImpl;


public class Main {
    public static void main(String[] args) {

        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("IVAN", "IVANOV", (byte) 33);
        userService.saveUser("EVA", "IVANOVICH", (byte) 20);
        userService.saveUser("DEN", "DOBRINOV", (byte) 45);
        userService.saveUser("MARI", "PETROVICH", (byte) 22);
        userService.getAllUsers().forEach(System.out::println);
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
