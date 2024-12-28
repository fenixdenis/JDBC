package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl extends Util implements UserDao {
    Connection connection = getConnection();
    //конструктор
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
       //запрос создание таблицы
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255) NOT NULL, " +
                "last_name VARCHAR(255) NOT NULL, " +
                "age TINYINT NOT NULL" +
                ");";
        try (Statement statement = connection.createStatement()) {
            //выполнение запроса
            statement.execute(sql);
            System.out.println("Создана таблица");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        //запрос в базу данных
        String sql = "DROP TABLE IF EXISTS users;";
        //создание стайтемент для обработки запроса в БД
        try (Statement statement = connection.createStatement())
        {   //выполнение запроса
            statement.execute(sql);
            System.out.println("Таблица удалена.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        //запрос в базу данных
        String sql = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?);";
        //создание объекта, который используется для выполнения параметризованных SQL-запросов к базе данных.
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            // Выполнение запроса
            preparedStatement.executeUpdate();
            System.out.println("User saved successfully: " + name + " " + lastName);
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        //запрос в базу данных
        String sql = "DELETE FROM users WHERE id = ?;";
        //создание объекта, который используется для выполнения параметризованных SQL-запросов к базе данных.
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Установка параметра id для подготовленного запроса
            preparedStatement.setLong(1, id);
            // Выполнение запроса
            //executeUpdate() — это метод, который выполняет запрос
            // и возвращает количество строк, которые были затронуты.
            int rowsAffected = preparedStatement.executeUpdate();
            //проверяет, была ли затронута хотя бы
            // одна строка. Если да, то выводит сообщение об успешном удалении пользователя.
            if (rowsAffected > 0) {
                System.out.println("User с ID " + id + " удалён");
            } else {
                System.out.println("User с ID" + id + "не найден");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка удаления user with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users1 = new ArrayList<>();
        // запрос для получения всех данных из таблицы users
        String query = "SELECT id, name, last_name, age FROM users";
        try (// Создаем Statement для выполнения запроса
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             // выполняет запрос и возвращает объект ResultSet, содержащий результаты запроса.
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            // Обрабатываем результаты запроса
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("last_name");
                int age = resultSet.getInt("age");
                // Создаем объект User и добавляем его в список
                User userObj = new User(name, lastName, (byte) age);
                users1.add(userObj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users1;
    }

    public void cleanUsersTable() {
        //запрос
        String query = "TRUNCATE TABLE users";
        // Создаем Statement для выполнения запроса
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Выполняем запрос
            preparedStatement.executeUpdate();
            System.out.println("Таблица успешно очищена.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

