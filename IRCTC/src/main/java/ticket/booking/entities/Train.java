package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {

    private String train_id;
    private int train_no;
    private List<List<Integer>> seats;
    private Map<String, String> station_times;
    private List<String> stations;

    public Train() {}

    @JsonCreator
    public Train(
            @JsonProperty("train_id") String train_id,
            @JsonProperty("train_no") int train_no,
            @JsonProperty("seats") List<List<Integer>> seats,
            @JsonProperty("station_times") Map<String, String> station_times,
            @JsonProperty("stations") List<String> stations
    ) {
        this.train_id = train_id;
        this.train_no = train_no;
        this.seats = seats;
        this.station_times = station_times;
        this.stations = stations;
    }

    public String getTrain_id() {
        return train_id;
    }

    public void setTrain_id(String train_id) {
        this.train_id = train_id;
    }

    public int getTrain_no() {
        return train_no;
    }

    public void setTrain_no(int train_no) {
        this.train_no = train_no;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public Map<String, String> getStation_times() {
        return station_times;
    }

    public void setStation_times(Map<String, String> station_times) {
        this.station_times = station_times;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    public String getTrainInfo() {
        return String.format("Train ID: %s Train No: %d", train_id, train_no);
    }
}
