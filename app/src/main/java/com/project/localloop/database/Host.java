package com.project.localloop.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Host.java
 * Subclass representing event organizers.
 * Hosts can create and manage events they own,
 * accepts/rejects participation requests.
 *
 * @author Ginger-C
 * @since 2025-05-24
 */
public class Host extends User {

    /**
     * Events hosted by this user.
     * Can include ongoing and past events.
     */
    protected List<String> hostedEventIds = new ArrayList<>(); // use event UID to identify
    protected List<String> currentEventIds = new ArrayList<>(); // use event UID to identify
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
    public List<String> getHostedEvents() {
        return hostedEventIds;
    }

    /**
     * Adds a new event to host's list.
     * @param event the event to add(by event UID)
     */
    public void addEvent(String event) {
        hostedEventIds.add(event);
    }

}
