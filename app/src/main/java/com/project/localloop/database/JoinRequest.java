package com.project.localloop.database;

/**
 * JoinRequest.java
 *
 * This class represents a join request submitted by a Participant
 * who wants to join an Event in the LocalLoop application.
 *
 * Each JoinRequest contains:
 * - the user ID of the participant making the request
 * - the current status of the request (e.g., "pending", "accepted", "rejected")
 *
 * The purpose of this class is to enable an approval-based event registration system:
 * - Participants submit a JoinRequest when they want to join an event
 * - Organizers review and update the status of each JoinRequest
 * - The application uses the status to control access to events
 *
 * This structure allows the system to:
 * - track which users have applied to which events
 * - support real-time updates and notifications
 * - display request status to both organizers and participants
 *  * @author Ginger-C
 *  * @since 2025-05-27
 */
public class JoinRequest {
    public static final int STATUS_REJECTED = 0;
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_ACCEPTED = 2;
    public static final int STATUS_ERROR = -1;

    private String userId;
    private int status;

    public JoinRequest() {}

    public JoinRequest(String userId, int status) {
        this.userId = userId;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}