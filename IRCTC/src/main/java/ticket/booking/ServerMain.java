package ticket.booking;

import com.sun.net.httpserver.HttpServer;
import ticket.booking.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerMain {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            server.createContext("/api/user/signup", new SignUpHandler());
            server.createContext("/api/user/login", new LoginHandler());
            server.createContext("/api/train/", new TrainListHandler());
            //server.createContext("/api/user/bookings", new BookingViewHandler());
            //server.createContext("/api/user/cancel", new BookingCancelHandler());

            server.setExecutor(null); // creates a default executor
            server.start();

            System.out.println("🚀 Server started at http://localhost:8080");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

