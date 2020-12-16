package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;

@Path("booking/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Booking {
    @POST
    @Path("list")
    public String bookingList(@FormDataParam("city") String city, @FormDataParam("theme") String theme) { //initialises the function and passes in the city and the theme that was chosen by the user
        System.out.println("Invoked Booking.bookingList()" + city + theme); //invokes bookingList()
        JSONArray response = new JSONArray(); //initialises JSONArray
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Bookings.Date, Bookings.Time, Locations.City, Rooms.Theme, Rooms.Price, Bookings.BookingID" +
                    "                    FROM Bookings" +
                    "                    JOIN Rooms ON Bookings.RoomID=Rooms.RoomID" +
                    "                    JOIN Locations ON Bookings.LocationID = Locations.LocationID" +

                    "                    WHERE Bookings.EmailAddress IS NULL AND City = ? AND Theme = ? ");
            ps.setString(1, city); //sets the first ? as the City
            ps.setString(2, theme); //sets the second ? as the Theme
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject row = new JSONObject();
                row.put("Date", results.getString(1));
                row.put("Time", results.getString(2));
                row.put("City", results.getString(3));
                row.put("Theme", results.getString(4));
                row.put("Price", results.getString(5));
                row.put("ID", results.getString(6));
                response.add(row);
            }
            System.out.println(response.toString());
            return response.toString();
        } catch (Exception exception) { //if the JSONArray fails
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Err       }\nor\": \"Unable to list items.  Error code xx.\"}";
        }
    }

    @GET
    @Path("getBooking/{id}")

    public String getBooking(@PathParam("id") int id) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Bookings.Date, Bookings.Time, Locations.City, Rooms.Theme, Rooms.Price, Bookings.BookingID" +
                    "                    FROM Bookings" +
                    "                    JOIN Rooms ON Bookings.RoomID=Rooms.RoomID" +
                    "                    JOIN Locations ON Bookings.LocationID = Locations.LocationID" +
                    "                    WHERE Bookings.BookingID = ? ");

            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject row = new JSONObject();
                row.put("Date", results.getString(1));
                row.put("Time", results.getString(2));
                row.put("City", results.getString(3));
                row.put("Theme", results.getString(4));
                row.put("Price", results.getString(5));
                row.put("ID", results.getString(6));
                return row.toString();
            }
            return "{\"Error       }\nor\": \"Unable to list items.  Error code xx.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error       }\nor\": \"Unable to list items.  Error code xx.\"}";
        }
    }

    @POST
    @Path("update")
    public String updateBooking(@FormDataParam("EmailAddress") String EmailAddress, @FormDataParam("GroupSize") Integer GroupSize, @FormDataParam("BookingID") Integer BookingID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Bookings" +
                    "                                             SET EmailAddress = ?, GroupSize = ?" +
                    "                                             WHERE BookingID = ?");
            ps.setString(1, EmailAddress);
            ps.setInt(1, GroupSize);
            ps.setInt(1, BookingID);
            return "{\"OK\": \"Booking Confirmed\"}";
        } catch (Exception exception) {
            return "{\"Error\": \"Unable to confirm item, please see server console for more info.\"}";
        }

    }
}
