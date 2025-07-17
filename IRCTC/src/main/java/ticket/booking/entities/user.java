package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class user {  // Changed to 'User' (capitalized)

    private String name;
    private String password;
    private String hashPassword;
    private List<Ticket> ticketBooked;
    private String userId;

    // Constructor using @JsonCreator for proper deserialization
    @JsonCreator
    public user(
            @JsonProperty("name") String name,
            @JsonProperty("password") String password,
            @JsonProperty("hashPassword") String hashPassword,
            @JsonProperty("ticketBooked") List<Ticket> ticketBooked,
            @JsonProperty("userId") String userId) {
        this.name = name;
        this.password = password;
        this.hashPassword = hashPassword;
        this.ticketBooked = ticketBooked;
        this.userId = userId;
    }

    // Default constructor
    public user() {}

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getHashedPassword() {
        return hashPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketBooked;
    }

    public String getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHashedPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketBooked = ticketsBooked;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Method to print tickets (for debugging purposes)
    public void printTickets() {
        for (Ticket ticket : ticketBooked) {
            System.out.println(ticket.getTicketInfo());
        }
    }
}
