package com.Servise;

import com.DB.DatabaseManager;
import com.Entity.User;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.sql.Connection;

public class UserService {

    public JSONObject insertUser(JSONObject requestBody) {
        JSONObject response = new JSONObject();

        try {
            String name = requestBody.optString("name");
            String email = requestBody.optString("email");
            String location = requestBody.optString("location");
            String fatherName = requestBody.optString("fatherName");
            String motherName = requestBody.optString("motherName");
            int id = requestBody.optInt("id");

            User newUser = new User(id, name, email, location, fatherName, motherName);
            Connection conn = DatabaseManager.getDataBaseConnection();
            boolean isInserted = DatabaseManager.insertUser(conn, newUser);

            if (isInserted) {
                response.put("status", "200");
                response.put("message", "Inserted successfully");
            } else {
                response.put("status", "500");
                response.put("message", "Failed to insert user");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "500");
            response.put("message", "Internal server error");
        }

        return response;
    }

    public JSONObject getUserDetails(int userId) {
        JSONObject userJson = null;
        try {
            Connection conn = DatabaseManager.getDataBaseConnection();
            Jedis jedis = DatabaseManager.getRedisConnection();

            User user = DatabaseManager.getUserById(conn, jedis, userId);
            if (user != null) {
                userJson = new JSONObject();
                userJson.put("id", user.getId());
                userJson.put("name", user.getName());
                userJson.put("email", user.getEmail());
                userJson.put("location", user.getLocation());
                userJson.put("fatherName", user.getFatherName());
                userJson.put("motherName", user.getMotherName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userJson;
    }
}
