package com.project.localloop.database;

import java.util.Date;
import java.util.List;

/**
 * Event.java
 * Data structure representing a community event.
 * Designed for Firebase storing usage.
 * Supports: join requests, image display, categorization, fees, limits, etc.
 *
 * @author Ginger-C
 * @since 2025-05-27
 */
public class Event {

    // ========== Basic Information ==========
    private String eventUID; // Firebase document ID
    private boolean eventIdLocked = false; // after assigning eventID, no overwrite.
    private String eventName;
    private String eventDescription;
    private Date eventDateTime;   // combined date and time
    private int eventDurationMinutes;
    private List<String> categoryIds;

    // ========== Creator Info ==========
    private String createdBy;  // host UID.
                                //Unique for the moment,may add feature of multiple host later!


    // ========== Participation Info ==========
    private double participationFee;
    private int maxParticipants;

    // ========== State Control ==========
    private boolean isActive;
    private boolean isPublic;

    // ========== User Associations ==========
    private List<String> participantIDs;        // UIDs of accepted participants
    private List<JoinRequest> joinRequests;     // pending/joined/rejected states

    // ========== Image Display ==========
    private List<String> imageUrls;             // image URLs from Firebase Storage

    // ========== Required no-arg constructor for firebase ==========
    public Event() {}

    // Optional constructor
    public Event(String eventUID, String eventName, String eventDescription,
                 Date eventDateTime, int eventDuration, double participationFee, int maxParticipants,
                 String createdBy, boolean isActive, boolean isPublic,
                 List<String> participantIDs, List<JoinRequest> joinRequests, List<String> imageUrls) {
        this.eventUID = eventUID;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDateTime = eventDateTime;
        this.eventDurationMinutes = eventDuration;
        this.participationFee = participationFee;
        this.maxParticipants = maxParticipants;
        this.createdBy = createdBy;
        this.isActive = isActive;
        this.isPublic = isPublic;
        this.participantIDs = participantIDs;
        this.joinRequests = joinRequests;
        this.imageUrls = imageUrls;
    }

    // ========== Getters and Setters ==========
    public String geteventUID() { return eventUID; }
    public void setEventUID(String eventUID) {
        if (!eventIdLocked) {
            this.eventUID = eventUID;
            this.eventIdLocked = true;
        } else {
            throw new IllegalStateException("eventUID can only be set once.");
        }
    }
    public List<String> getCategoryIds() {
        return categoryIds;
    }
    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventDescription() { return eventDescription; }
    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }

    public Date getEventDateTime() { return eventDateTime; }
    public void setEventDateTime(Date eventDateTime) { this.eventDateTime = eventDateTime; }

    public int getEventDurationMinutes() {
        return eventDurationMinutes;
    }

    public void setEventDurationMinutes(int durationMinutes) {
        this.eventDurationMinutes = durationMinutes;
    }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public double getParticipationFee() { return participationFee; }
    public void setParticipationFee(double participationFee) { this.participationFee = participationFee; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean aPublic) { isPublic = aPublic; }

    public List<String> getParticipantIDs() { return participantIDs; }
    public void setParticipantIDs(List<String> participantIDs) { this.participantIDs = participantIDs; }

    public List<JoinRequest> getJoinRequests() { return joinRequests; }
    public void setJoinRequests(List<JoinRequest> joinRequests) { this.joinRequests = joinRequests; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }


}
