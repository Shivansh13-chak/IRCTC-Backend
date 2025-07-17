package ticket.booking.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ticket.booking.entities.user;
import ticket.booking.util.userserviceutil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LoginHandler implements HttpHandler {

    private static final String USER_DB = "src/main/java/ticket/booking/localDb/users.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("➡️ Received request at /api/user/login");
        System.out.println("➡️ Method: " + exchange.getRequestMethod());

        // CORS headers for frontend to communicate with backend
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        // Handle preflight request (OPTIONS)
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(204, -1);
            exchange.getResponseBody().close();
            return;
        }

        // Check if it's a POST request
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            String response = "Method Not Allowed";
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        try {
            // Read the request body
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            user loginRequest = mapper.readValue(isr, user.class);

            List<user> users = loadUsers();

            for (user u : users) {
                if (u.getName().equals(loginRequest.getName())) {
                    boolean passwordMatch = userserviceutil.checkPassword(
                            loginRequest.getPassword(), u.getHashedPassword());

                    if (passwordMatch) {
                        String response = "Login successful!";
                        sendResponse(exchange, response, 200);
                        return;
                    } else {
                        break; // found user, wrong password
                    }
                }
            }

            String response = "Invalid username or password.";
            sendResponse(exchange, response, 401);
        } catch (Exception e) {
            e.printStackTrace();
            String response = "Internal Server Error";
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(500, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private List<user> loadUsers() {
        try {
            File file = new File(USER_DB);
            if (!file.exists()) return new ArrayList<>();
            return new ArrayList<>(Arrays.asList(mapper.readValue(file, user[].class)));
        } catch (IOException e) {
            System.err.println("⚠️ Error loading users: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void sendResponse(HttpExchange exchange, String message, int statusCode) throws IOException {
        // Use JSON content type for consistency (can change back to 'text/plain' if needed)
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, message.length());
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes());
        os.close();
    }
}
