package com.DB;

import com.Entity.User;
import redis.clients.jedis.Jedis;

import java.sql.*;

public class DatabaseManager {

    public static Connection getDataBaseConnection() {
        String url = "jdbc:postgresql://localhost:5432/userinfo";
        String user = "myuser";
        String password = "PP@123";

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                System.out.println("Connection successful!");
            }
            return connection;

        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertUser(Connection conn, User user) throws SQLException {
        String insertSQL = "INSERT INTO userinfo (id, name, email, location, fatherName, motherName) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getLocation());
            preparedStatement.setString(5, user.getFatherName());
            preparedStatement.setString(6, user.getMotherName());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static User getUserByIdFromDB(Connection conn, int id) {
        User user = null;
        String query = "SELECT * FROM userinfo WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("location"),
                            rs.getString("fatherName"),
                            rs.getString("motherName")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUserById(Connection conn, Jedis jedis, int id) {
        String key = "userId-" + String.valueOf(id);
        if (jedis.hexists(key, "name") && jedis.hexists(key, "email")) {
            String name = jedis.hget(key, "name");
            String email = jedis.hget(key, "email");
            System.out.println("From Redis Cache " + name);
            return new User(id, name, email, "", "", "");
        } else {
            User user = getUserByIdFromDB(conn, id);
            jedis.hset(key, "name", user.getName());
            jedis.hset(key, "email", user.getEmail());
            return user;
        }
    }

    public static Jedis getRedisConnection() {
        Jedis jedis = new Jedis("http://127.0.0.1:6379/");
        return jedis;
    }
}

