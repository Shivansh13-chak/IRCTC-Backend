package ticket.booking.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ticket.booking.entities.Ticket;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ticketbookinghandler implements HttpHandler {

    private static final String TICKET_DB = "src/main/java/ticket/booking/localDb/ticket.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(204, 0);
            exchange.getResponseBody().close();
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            String response = "Method Not Allowed";
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        // Read JSON ticket from body
        InputStream requestBody = exchange.getRequestBody();
        Ticket ticket = mapper.readValue(requestBody, Ticket.class);
        requestBody.close(); // Always close input stream


        String generatedTicketId = "TICKET-" + UUID.randomUUID().toString();
        ticket.setTicketId(generatedTicketId);


        // Load existing tickets
        List<Ticket> allTickets = loadTickets();

        // Add the new ticket
        allTickets.add(ticket);

        // Save updated list back to file
        saveTickets(allTickets);

        // Send success response
        String success = "Ticket booked successfully!";
        exchange.sendResponseHeaders(200, success.length());
        OutputStream os = exchange.getResponseBody();
        os.write(success.getBytes());
        os.close();
    }

    // Load all tickets from JSON file
    private List<Ticket> loadTickets() {
        try {
            File file = new File(TICKET_DB);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            Ticket[] ticketArray = mapper.readValue(file, Ticket[].class);
            return new ArrayList<>(Arrays.asList(ticketArray));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Save all tickets to JSON file
    private void saveTickets(List<Ticket> tickets) {
        try {
            File file = new File(TICKET_DB);
            mapper.writeValue(file, tickets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
