package main.model;

import main.model.exception.InvalidObjectStringFormatException;

/**
 *
 * @author guru
 */
public class Hire {

    private int id;
    private int roomId;
    private String cusName;
    private double amountPaid;
    private String startDatetime;
    private String endDatetime;
    private int numOfGuests;
    private int numOfKids;

    public Hire(int id, int roomId, String cusName, double amountPaid, String startDatetime, String endDatetime, int numOfGuests, int numOfKids) {
        this.id = id;
        this.roomId = roomId;
        this.cusName = cusName;
        this.amountPaid = amountPaid;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.numOfGuests = numOfGuests;
        this.numOfKids = numOfKids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public int getNumOfGuests() {
        return numOfGuests;
    }

    public void setNumOfGuests(int numOfGuests) {
        this.numOfGuests = numOfGuests;
    }

    public int getNumOfKids() {
        return numOfKids;
    }

    public void setNumOfKids(int numOfKids) {
        this.numOfKids = numOfKids;
    }

    @Override
    public String toString() {
        return "Hire "
                + cusName + "; "
                + amountPaid + "; "
                + startDatetime + "; "
                + endDatetime + "; "
                + numOfGuests + "; "
                + numOfKids;
    }

    public static Hire fromString(String hireString) throws InvalidObjectStringFormatException {

        String[] roomParts = hireString.split("; ");

        if (roomParts.length != 6) {
            throw new InvalidObjectStringFormatException();
        } else {
            String cusName = roomParts[0].trim().split(" ")[1].trim();
            double amountPaid = Double.parseDouble(roomParts[1].trim());
            String startDatetime = roomParts[2].trim();
            String endDatetime = roomParts[3].trim();
            int numOfGuests = Integer.parseInt(roomParts[4].trim());
            int numOfKids = Integer.parseInt(roomParts[5].trim());

            return new Hire(0, 0, cusName, amountPaid, startDatetime, endDatetime, numOfGuests, numOfKids);
        }
    }

}
