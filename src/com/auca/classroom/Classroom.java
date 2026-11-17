package com.auca.classroom;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.util.Checks;

public class Classroom implements Identifiable, Registrable {
    private String classroomId;
    private String buildingName;
    private String roomNumber;
    private int capacity;
    private int floorNumber;
    private boolean available;

    public Classroom(String classroomId, String buildingName, String roomNumber, int capacity, int floorNumber, boolean available) {
        this.classroomId = classroomId; this.buildingName = buildingName; this.roomNumber = roomNumber;
        this.capacity = capacity; this.floorNumber = floorNumber; this.available = available;
    }
    @Override public String getId() { return classroomId; }
    @Override public String getDisplayName() { return buildingName + " " + roomNumber; }
    @Override public boolean isActive() { return available; }
    @Override public void validate() throws ValidationException {
        Checks.text(classroomId, "Classroom ID", 4, 30); Checks.text(buildingName, "Building name", 2, 60);
        Checks.text(roomNumber, "Room number", 1, 30);
        if (capacity <= 0) throw new ValidationException("Classroom capacity must be greater than zero.");
        if (floorNumber < 0) throw new ValidationException("Floor number cannot be negative.");
    }
    @Override public void printDetails() { System.out.println(this); }
    @Override public String getRegistrationSummary() { return "Classroom " + classroomId + " - " + getDisplayName(); }
    public String getClassroomId() { return classroomId; } public void setClassroomId(String v) { classroomId = v; }
    public String getBuildingName() { return buildingName; } public void setBuildingName(String v) { buildingName = v; }
    public String getRoomNumber() { return roomNumber; } public void setRoomNumber(String v) { roomNumber = v; }
    public int getCapacity() { return capacity; } public void setCapacity(int v) { capacity = v; }
    public int getFloorNumber() { return floorNumber; } public void setFloorNumber(int v) { floorNumber = v; }
    public boolean isAvailable() { return available; } public void setAvailable(boolean v) { available = v; }
    @Override public String toString() { return "Classroom{id='" + classroomId + "', building='" + buildingName
            + "', room='" + roomNumber + "', capacity=" + capacity + ", floor=" + floorNumber + ", available=" + available + "}"; }
}
