package com.project.localloop.ui.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Host.java
 * Subclass representing event organizers.
 * Hosts can create and manage events they own,
 * accepts/rejects participation requests.
 *
 * @author Wen Bin
 */
public class Host extends User {

    /**
     * Events hosted by this user.
     * Can include ongoing and past events.
     */
    protected List<Event> hostedEvents = new ArrayList<>();

    /**
     * Required empty constructor for Firebase.
     */
    public Host() {
        super();
    }

    /**
     * Creates a new Host user.
     * @param userName display name
     * @param email immutable email
     * @param password account password
     */
    public Host(String userName, String email, String password) {
        super(userName, email, password, 1); // accountType = 1
    }

    /**
     * @return list of events hosted by this user
     */
    public List<Event> getHostedEvents() {
        return hostedEvents;
    }

    /**
     * Adds a new event to host's list.
     * @param event the event to add
     */
    public void addEvent(Event event) {
        hostedEvents.add(event);
    }
}
