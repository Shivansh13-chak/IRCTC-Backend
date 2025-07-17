package ticket.booking.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ticket.booking.entities.user;
import ticket.booking.util.userserviceutil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SignUpHandler implements HttpHandler {

    // ✅ Updated path to point to correct directory
    private static final String USER_DB = "src/main/java/ticket/booking/localDb/users.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            System.out.println("➡️ Received request at /api/user/signup");
            System.out.println("➡️ Method: " + exchange.getRequestMethod());

            // Set CORS headers
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            // Handle preflight OPTIONS request
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Content-Type", "text/plain");
                exchange.sendResponseHeaders(204, 0);
                exchange.getResponseBody().close();
                return;
            }

            // Reject non-POST methods
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                String response = "Method Not Allowed";
                exchange.getResponseHeaders().add("Content-Type", "text/plain");
                exchange.sendResponseHeaders(405, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            // Parse request body to user
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            user requestUser = mapper.readValue(isr, user.class);

            // Load existing users
            List<user> users = loadUsers();

            // Check for duplicate user
            for (user u : users) {
                if (u.getName().equals(requestUser.getName())) {
                    String response = "User already exists.";
                    sendResponse(exchange, response, 409); // Conflict
                    return;
                }
            }

            // Hash and set password, assign ID
            String hashed = userserviceutil.hashPassword(requestUser.getPassword());
            requestUser.setHashedPassword(hashed);
            requestUser.setPassword(null); // Don't store plain password
            requestUser.setUserId(UUID.randomUUID().toString());
            requestUser.setTicketsBooked(new ArrayList<>());

            // Save new user
            users.add(requestUser);
            saveUsers(users);

            String response = "Signup successful!";
            sendResponse(exchange, response, 200);

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
            // Ensure list is mutable to allow add()
            return new ArrayList<>(Arrays.asList(mapper.readValue(file, user[].class)));
        } catch (IOException e) {
            System.err.println("⚠️ Error loading users: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveUsers(List<user> users) {
        try {
            File file = new File(USER_DB);
            System.out.println("📁 Saving users to: " + file.getAbsolutePath());
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
        } catch (IOException e) {
            System.err.println("❌ Error saving users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendResponse(HttpExchange exchange, String message, int statusCode) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/plain");
        exchange.sendResponseHeaders(statusCode, message.length());
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes());
        os.close();
    }
}
