package com.computerlist.model;
import java.time.LocalDate;
import java.util.List;

public class Computer {

    private int id;
    private String title;
    private String description;
    private int gamingZoneId;
    private GamingZone gamingZone;
    private LocalDate startDate;
    private LocalDate endDate;
    private double hourlyRate;
    private int creator_id;
    private User creator;


    private List<RoomOption> roomOptions;
    public Computer() {}

    public Computer(int id, String title,String description, int gamingZoneId, GamingZone gamingZone,
                LocalDate startDate, LocalDate endDate, double hourlyRate, int creator_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.gamingZoneId = gamingZoneId;
        this.gamingZone = gamingZone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hourlyRate = hourlyRate;
        this.creator_id = creator_id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getGamingZoneId() {
        return gamingZoneId;
    }

    public GamingZone getGamingZone() {
        return gamingZone;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGamingZoneId(int gamingZoneId) {
        this.gamingZoneId = gamingZoneId;
    }

    public void setGamingZone(GamingZone gamingZone) {
        this.gamingZone = gamingZone;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public List<RoomOption> getRoomOptions() {
        return roomOptions;
    }

    public void setRoomOptions(List<RoomOption> roomOptions) {
        this.roomOptions = roomOptions;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", gamingZoneId=" + gamingZoneId +
                ", gamingZone=" + gamingZone +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", hourlyRate=" + hourlyRate +
                '}';
    }
}

