package com.redisEndpoint;


import com.Servise.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/userDetails")
public class UserAPIController extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        PrintWriter out = httpServletResponse.getWriter();
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = httpServletRequest.getReader().readLine()) != null) {
            sb.append(line);
        }

        JSONObject requestBody = new JSONObject(sb.toString());

        try {
            JSONObject insertResponse = userService.insertUser(requestBody);
            out.write(insertResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.flush();
        }
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        PrintWriter out = httpServletResponse.getWriter();
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");

        int userId = Integer.parseInt(httpServletRequest.getParameter("id").trim());

        try {
            JSONObject userJson = userService.getUserDetails(userId);
            if (userJson != null) {
                out.write(userJson.toString());
            } else {
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.flush();
        }
    }
}
