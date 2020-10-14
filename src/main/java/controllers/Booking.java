package controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("booking/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Booking {
    @GET
    @Path("list")
    public String bookingList() {
        System.out.println("Invoked Booking.bookingList()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Bookings.Date, Bookings.Time, Locations.City, Rooms.Theme" +
                    "                    FROM Bookings" +
                    "                    JOIN Rooms ON Bookings.RoomID=Rooms.RoomID" +
                    "                    JOIN Locations ON Bookings.LocationID = Locations.LocationID" +
                    "                    WHERE Bookings.UserID IS NULL " );
            ResultSet results = ps.executeQuery();
            while (results.next()==true) {
                JSONObject row = new JSONObject();
                row.put("Date", results.getString(1));
                row.put("Time", results.getString(2));
                row.put("City", results.getString(3));
                row.put("Theme", results.getString(4));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }
}
