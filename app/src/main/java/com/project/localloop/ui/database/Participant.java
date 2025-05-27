package com.project.localloop.ui.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Participant.java
 * Subclass representing event participants.
 * Participants can register and join events.
 *
 * @author Wen Bin
 */
public class Participant extends User {

    /**
     * Events the user is attending or has attended.
     */
    protected List<Event> registeredEvents = new ArrayList<>();

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
    public List<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    /**
     * Adds an event to participant's registered list.
     * @param event the event to register
     */
    public void registerEvent(Event event) {
        registeredEvents.add(event);
    }
}
