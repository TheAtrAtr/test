import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class Flights {
    private String origin;
    private String origin_name;
    private String destination;
    private String destination_name;
    @JsonFormat(pattern = "dd.MM.yy")
    private LocalDate departure_date;
    @JsonFormat(pattern = "H:m")
    private LocalTime departure_time;
    @JsonFormat(pattern = "dd.MM.yy")
    private LocalDate arrival_date;
    @JsonFormat(pattern = "H:m")
    private LocalTime arrival_time;
    private String carrier;
    private int stops;
    private int price;

    public Flights(String origin, String origin_name, String destination, String destination_name,
                   LocalDate departure_date, LocalTime departure_time, LocalDate arrival_date,
                   LocalTime arrival_time, String carrier, int stops, int price) {
        this.origin = origin;
        this.origin_name = origin_name;
        this.destination = destination;
        this.destination_name = destination_name;
        this.departure_date = departure_date;
        this.departure_time = departure_time;
        this.arrival_date = arrival_date;
        this.arrival_time = arrival_time;
        this.carrier = carrier;
        this.stops = stops;
        this.price = price;
    }
    public Flights() {
    }

    public String getOrigin() {
        return origin;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public String getDestination() {
        return destination;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public LocalDate getDeparture_date() {
        return departure_date;
    }

    public LocalTime getDeparture_time() {
        return departure_time;
    }

    public LocalDate getArrival_date() {
        return arrival_date;
    }

    public LocalTime getArrival_time() {
        return arrival_time;
    }

    public String getCarrier() {
        return carrier;
    }

    public int getStops() {
        return stops;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Flights{" +
                "origin='" + origin + '\'' +
                ", origin_name='" + origin_name + '\'' +
                ", destination='" + destination + '\'' +
                ", destination_name='" + destination_name + '\'' +
                ", departure_date='" + departure_date + '\'' +
                ", departure_time='" + departure_time + '\'' +
                ", arrival_date='" + arrival_date + '\'' +
                ", arrival_time='" + arrival_time + '\'' +
                ", carrier='" + carrier + '\'' +
                ", stops=" + stops +
                ", price=" + price +
                '}';
    }
}
