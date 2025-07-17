package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {

    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private Date dateOfTravel;
    private String trainId;
    private int seatsBooked;

    // Default constructor for Jackson
    public Ticket() {}

    // Constructor using @JsonCreator and @JsonProperty for deserialization
    @JsonCreator
    public Ticket(
            @JsonProperty("ticketId") String ticketId,
            @JsonProperty("userId") String userId,
            @JsonProperty("source") String source,
            @JsonProperty("destination") String destination,
            @JsonProperty("dateOfTravel") Date dateOfTravel,
            @JsonProperty("trainId") String trainId,
            @JsonProperty("seatsBooked") int seatsBooked
    ) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.trainId = trainId;
        this.seatsBooked = seatsBooked;
    }

    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setseatsBooked(int seatsBooked){this.seatsBooked=seatsBooked;}

  public int getSeatsBooked(){return seatsBooked; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(Date dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

   public String gettrainId(){return trainId;}
    public void setTrainId(String trainId){this.trainId=trainId;}

    // Method to get ticket info as a formatted string
    public String getTicketInfo() {
        return String.format("Ticket ID: %s belongs to User %s from %s to %s on %s",
                ticketId, userId, source, destination, dateOfTravel);
    }
}
