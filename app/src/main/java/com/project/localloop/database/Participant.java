package com.project.localloop.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Participant.java
 * Subclass representing event participants.
 * Participants can register and join events.
 *
 * @author Ginger-C
 * @since 2025-05-23
 */
public class Participant extends User {

    /**
     * Events the user is attending or has attended.
     */
    protected List<String> registeredEventIds = new ArrayList<>(); // use event UID to identify

    /**
     * Required empty constructor for Firebase.
     */
    public Participant() {
        super();
    }

    /**
     * Creates a new Participant user.
     * @param userName display name
     * @param email immutable email
     * @param password account password
     */
    public Participant(String userName, String email, String password) {
        super(userName, email, password, 2); // accountType = 2
    }

    /**
     * @return list of registered events
     */
    public List<String> getRegisteredEvents() {
        return registeredEventIds;
    }

    /**
     * Adds an event to participant's registered list.
     * @param event the event to register
     */
    public void registerEvent(String event) {
        registeredEventIds.add(event);
    }

    protected void setSuspended(boolean suspended)
    {
        this.isSuspended = suspended;
    }
}
