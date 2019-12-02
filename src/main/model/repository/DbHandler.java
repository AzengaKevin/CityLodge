/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.model.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.model.Hire;
import main.model.Room;
import main.utils.AlertHelper;

/**
 * Class for handling Database operations
 *
 * @author guru
 */
public class DbHandler {

    public static final String CONNECTION_URL = "jdbc:sqlite:database/lodge.db";

    private static DbHandler dbh = null;
    private Connection conn = null;
    private Statement stmt = null;

    /**
     * Private constructor to enforce the singleton design pattern
     *
     * It also calls a method to initialize Connection conn
     */
    private DbHandler() {
        getConnection();
    }

    /**
     * Gets the database connection and assigns it to a global Connection
     * instance @conn
     */
    private void getConnection() {
        try {

            this.conn = DriverManager.getConnection(CONNECTION_URL);

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getLocalizedMessage());
            AlertHelper.error("Database Cnnnection", "Failed to create connection to the Database");

        }
    }

    /**
     * The method to instantiate this class
     *
     * @return DbHandler dbh, the only instance of the class
     */
    public static DbHandler getInstance() {
        if (dbh == null) {
            dbh = new DbHandler();
        }

        return dbh;
    }

    private void createHiresTable() {

        String createHireTblQuery = "CREATE TABLE hires(\n"
                + "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "room_id INTEGER NOT NULL,\n"
                + "customer_name TEXT NOT NULL,\n"
                + "num_of_guests INTEGER NOT NULL,\n"
                + "num_of_kids INTEGER NOT NULL,\n"
                + "checked_in_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                + "checked_out_at TIMESTAMP,\n"
                + "amount_paid REAL NOT NULL DEFAULT 0.00\n"
                + ")";

        try {
            stmt = conn.createStatement();

            if (stmt.execute(createHireTblQuery)) {
                AlertHelper.success("Creating Room Table", "Table Created/Exists");
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getLocalizedMessage());
            AlertHelper.error("Creating Hire Table", "Table creation failed");
        } finally {

            try {
                stmt.close();
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getLocalizedMessage());
            }
        }
    }

    private void createRoomsTable() {

        String createRoomsTblQuery = "CREATE TABLE rooms(\n"
                + "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "name TEXT NOT NULL,\n"
                + "filename TEXT NOT NULL DEFAULT 'file:/home/azenga/NetBeansProjects/CityLodge/images/noimage-large.png',\n"
                + "max_guests INTEGER NOT NULL,\n"
                + "max_kids INTEGER NOT NULL,\n"
                + "cost_per_night REAL NOT NULL,\n"
                + "under_maintenance BOOLEAN DEFAULT false,\n"
                + "description TEXT,\n"
                + "available BOOLEAN DEFAULT true,\n"
                + "type TEXT NOT NULL,\n"
                + "hires INTEGER NOT NULL DEFAULT 0\n"
                + ")";

        try {
            stmt = conn.createStatement();

            if (stmt.execute(createRoomsTblQuery)) {
                AlertHelper.success("Creating Room Table", "Table Created/Exists");
            }

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getLocalizedMessage());
            AlertHelper.error("Creating Room Table", "Table creation failed");
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getLocalizedMessage());
            }
        }

    }

    /**
     * Drops all the two tables i.e. rooms and hire
     */
    public void deleteRoomsTable() {
        String query = "DROP TABLE IF EXISTS rooms;";

        try {
            stmt = conn.createStatement();
            stmt.execute(query);
            createRoomsTable();

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getLocalizedMessage());
            AlertHelper.error("Dropping Room Table", "Error Dropping Rooms Table");
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getLocalizedMessage());
            }
        }
    }

    public void deleteHiresTable() {
        String query = "DROP TABLE IF EXISTS hires;";

        try {
            stmt = conn.createStatement();
            stmt.execute(query);
            createHiresTable();

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getLocalizedMessage());
            AlertHelper.error("Dropping Rooms Table", "Error Dropping Rooms Table");
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getLocalizedMessage());
            }
        }
    }

    public int addRoom(Room room) {
        String query = "INSERT INTO rooms(name, filename, max_guests, max_kids, cost_per_night, under_maintenance, description, available, type)\n"
                + "VALUES("
                + "'" + room.getName() + "',"
                + "'" + room.getFilename() + "',"
                + "" + room.getMaxGuests() + ","
                + "" + room.getMaxKids() + ","
                + "" + room.getCostPerNight() + ","
                + "" + room.isUnderMaintenance() + ","
                + "'" + room.getDescription() + "',"
                + "" + room.isAvailable() + ","
                + "'" + room.getType() + "'"
                + ");";

        try {
            stmt = conn.createStatement();

            return stmt.executeUpdate(query);

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getLocalizedMessage());
            AlertHelper.error("Adding a Room", "Failed to add the Room");

        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getLocalizedMessage());
            }
        }

        return -1;
    }

    public ObservableList<Room> getAllRooms() {
        ObservableList<Room> rooms = FXCollections.observableArrayList();

        try {
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM rooms ORDER BY hires DESC");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String filename = rs.getString("filename");
                int maxGuests = rs.getInt("max_guests");
                int maxKids = rs.getInt("max_kids");
                double costPerNight = rs.getDouble("cost_per_night");
                boolean underMintenance = rs.getBoolean("under_maintenance");
                String description = rs.getString("description");
                boolean available = rs.getBoolean("available");
                String type = rs.getString("type");

                Room room = new Room(name, filename, maxGuests, maxKids, costPerNight, underMintenance, description, available, type);
                room.setId(id);

                rooms.add(room);
            }

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getLocalizedMessage());
            AlertHelper.error("Getting Rooms", "Failed to retrieve all rooms");
        } finally {

            try {
                stmt.close();
            } catch (SQLException ex) {

                System.err.println("SQLException: " + ex.getLocalizedMessage());
            }
        }

        return rooms;
    }

    public boolean addNewHire(Hire hire) {

        String query = "INSERT INTO hires(room_id, customer_name, num_of_guests, num_of_kids, amount_paid)\n"
                + "VALUES("
                + "" + hire.getRoomId() + ","
                + "'" + hire.getCusName() + "',"
                + "" + hire.getNumOfGuests() + ","
                + "" + hire.getNumOfKids() + ","
                + "" + hire.getAmountPaid() + ""
                + ");";

        try {

            stmt = conn.createStatement();

            return stmt.executeUpdate(query) != -1;

        } catch (SQLException ex) {

            AlertHelper.error("Hiring", "Operation failed");
            System.err.println("SQLException: " + ex.getLocalizedMessage());

        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getLocalizedMessage());
            }
        }

        return false;

    }

    public ObservableList<Hire> getAllHires() {
        ObservableList<Hire> hires = FXCollections.observableArrayList();

        try {
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM hires");

            while (rs.next()) {
                int id = rs.getInt("id");
                int roomId = rs.getInt("room_id");
                String cusName = rs.getString("customer_name");
                int numOfGuests = rs.getInt("num_of_guests");
                int numOfKids = rs.getInt("num_of_kids");
                String startDateTime = rs.getString("checked_in_at");
                String endDatetime = rs.getString("checked_out_at");
                double amountPaid = rs.getDouble("amount_paid");

                hires.add(new Hire(id, roomId, cusName, amountPaid, startDateTime, endDatetime, numOfGuests, numOfKids));
            }

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getLocalizedMessage());
            AlertHelper.error("Getting hires", "Failed to retrieve all rooms");
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getLocalizedMessage());
            }
        }

        return hires;
    }

    public boolean executeAction(String action) {
        try {
            stmt = conn.createStatement();

            return stmt.executeUpdate(action) != -1;

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getLocalizedMessage());
            AlertHelper.error("SQLException Error", "Query Error, check in the console");
        }

        return false;
    }

    public ObservableList<Hire> getAllHiresByRoom(int roomid) {
        ObservableList<Hire> hires = FXCollections.observableArrayList();

        try {
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM hires WHERE room_id = " + roomid);

            while (rs.next()) {
                int id = rs.getInt("id");
                int roomId = rs.getInt("room_id");
                String cusName = rs.getString("customer_name");
                int numOfGuests = rs.getInt("num_of_guests");
                int numOfKids = rs.getInt("num_of_kids");
                String startDateTime = rs.getString("checked_in_at");
                String endDatetime = rs.getString("checked_out_at");
                double amountPaid = rs.getDouble("amount_paid");

                hires.add(new Hire(id, roomId, cusName, amountPaid, startDateTime, endDatetime, numOfGuests, numOfKids));
            }

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getLocalizedMessage());
            AlertHelper.error("Getting hires", "Failed to retrieve all rooms");
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getLocalizedMessage());
            }
        }

        return hires;
    }
}
