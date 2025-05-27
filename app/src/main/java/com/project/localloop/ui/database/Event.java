package com.project.localloop.ui.database;

import java.util.List;

public class Event {
    String eventId;
    String eventName;
    String eventDate;
    String eventTime;
    String eventDuration;
    String eventCategory;
    String eventDescription;
    String participationFee;
    boolean participationStatus;

    List<String> participantIDs;
    List<String> hostIDs;
}
