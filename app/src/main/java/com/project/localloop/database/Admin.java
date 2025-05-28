package com.project.localloop.database;

/**
 * Admin.java
 * Subclass representing administrator users.
 * Admins have elevated permissions such as user suspension / event moderation.
 * Admins can add or delete categories of events.
 *
 * @author Ginger-C
 * @since 2025-05-24
 */
public class Admin extends User {

    /**
     * Empty constructor required by Firebase.
     */
    public Admin() {
        super();
    }

    /**
     * Creates a new Admin user.
     * @param userName display name
     * @param email immutable email
     * @param password account password
     */
    public Admin(String userName, String email, String password) {
        super(userName, email, password, 0); // accountType = 0
    }

    protected void setSuspended(boolean suspended)
    {
        this.isSuspended = false; //regardless of param
    }

}
