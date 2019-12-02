package main.model;

import main.model.exception.InvalidObjectStringFormatException;

/**
 *
 * @author guru
 */
public class Room {

    private int id;
    private String name;
    private String filename;
    private int maxGuests;
    private int maxKids;
    private double costPerNight;
    private boolean underMaintenance;
    private String description;
    private boolean available;
    private String type;

    public Room(String name, String filename, int maxGuests, int maxKids, double costPerNight, boolean underMaintenance, String description, boolean available, String type) {
        this.name = name;
        this.filename = filename;
        this.maxGuests = maxGuests;
        this.maxKids = maxKids;
        this.costPerNight = costPerNight;
        this.underMaintenance = underMaintenance;
        this.description = description;
        this.available = available;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public int getMaxKids() {
        return maxKids;
    }

    public void setMaxKids(int maxKids) {
        this.maxKids = maxKids;
    }

    public double getCostPerNight() {
        return costPerNight;
    }

    public void setCostPerNight(double costPerNight) {
        this.costPerNight = costPerNight;
    }

    public boolean isUnderMaintenance() {
        return underMaintenance;
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        this.underMaintenance = underMaintenance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Room "
                + name + "; "
                + maxGuests + "; "
                + maxKids + "; "
                + costPerNight + "; "
                + underMaintenance + "; "
                + description + "; "
                + available + "; "
                + type + "; "
                + filename;

    }

    public static Room fromString(String roomString) throws InvalidObjectStringFormatException {

        String[] roomParts = roomString.split("; ");

        if (roomParts.length != 9) {
            throw new InvalidObjectStringFormatException();
        } else {
            String name = roomParts[0].trim().split(" ")[1].trim();
            int maxGuests = Integer.parseInt(roomParts[1].trim());
            int maxKids = Integer.parseInt(roomParts[2].trim());
            double costPerNight = Double.parseDouble(roomParts[3].trim());
            boolean underMaintenance = Boolean.parseBoolean(roomParts[4].trim());
            String description = roomParts[5].trim();
            boolean available = Boolean.parseBoolean(roomParts[6].trim());
            String type = roomParts[7].trim();
            String filename = roomParts[8].trim();

            return new Room(name, filename, maxGuests, maxKids, costPerNight, underMaintenance, description, available, type);
        }

    }

}
