package ticket.booking.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrainListHandler implements HttpHandler {

    public static final String train_db = "src/main/java/ticket/booking/localDb/train.json";
    private static final ObjectMapper mapper = new ObjectMapper();

@Override
public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        if("OPTION".equalsIgnoreCase(exchange.getRequestMethod())){
            exchange.getResponseHeaders().add("Content-Type","text/plain");
            exchange.sendResponseHeaders(204,0);
            exchange.getResponseBody().close();
            return;
        }
        if(!exchange.getRequestMethod().equalsIgnoreCase("GET"))
        {
            String response = "METHOD NOT ALLOWED";
            exchange.getResponseHeaders().add("Content-Type","text/plain");
            exchange.sendResponseHeaders(405,response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        try {
            List<Train> trains = loadTrains();

            String jsonResponse = mapper.writeValueAsString(trains);

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            String errorResponse = "Internal Server Error";
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(500, errorResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(errorResponse.getBytes());
            os.close();
        }
    }

    private List<Train> loadTrains() {
        try {
            File file = new File(train_db);
            if (!file.exists()) return new ArrayList<>();
            return new ArrayList<>(Arrays.asList(mapper.readValue(file, Train[].class)));
        } catch (IOException e) {
            System.err.println("⚠️ Error loading train data: " + e.getMessage());
            return new ArrayList<>();
        }
    }





    }


