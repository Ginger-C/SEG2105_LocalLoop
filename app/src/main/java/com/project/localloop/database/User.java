package com.project.localloop.database;

import java.util.HashMap;
import java.util.Map;

/**
 * User.java
 * Abstract base class for storing user info in Firebase.
 * <p>
 * FIELD PERMISSIONS:
 * - userId: only set once after creation, no overwrite allowed
 * - email: immutable after creation, no setter
 * - accountType: immutable after creation, no setter
 *
 * @author Ginger-C
 * @since 2025-05-24
 */
public abstract class User {

    // ===========================
    // Account core information
    // ===========================
    public static final int TYPE_ADMIN = 0;
    public static final int TYPE_HOST = 1;
    public static final int TYPE_PARTICIPANT = 2;
    public static final int TYPE_ERROR = -1;
    /**
     * Firebase UID. Set once from FirebaseAuth.
     * No override allowed once set.
     */
    protected String userId;

    /**
     * Once userId is set, this flag prevents future changes.
     */
    protected boolean userIdLocked = false;

    /**
     * Display name of the user. Editable.
     */
    protected String userName;

    /**
     * Immutable after registration. Unique for each account.
     */
    protected String email;

    /**
     * User password. Can be updated.
     */
    protected String password;

    /**
     * Immutable. Assigned once during registration:
     * 0 = Admin, 1 = Host, 2 = Participant, -1 = Error.
     */
    protected int accountType;

    /**
     * Suspension status. Default = false.
     */
    protected boolean isSuspended = false;

    // ===========================
    // Constructors
    // ===========================

    /**
     * Required empty constructor for Firebase deserialization.
     */
    protected User() {}

    /**
     * Constructor for user creation (without Firebase ID yet).
     */
    protected User(String userName, String email, String password, int accountType) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.isSuspended = false;
    }

    // ===========================
    // Getters
    // ===========================

    /**
     * @return Firebase user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @return user's display name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return account type: 0=Admin, 1=Participant, 2=Host
     */
    public int getAccountType() {
        return accountType;
    }

    /**
     * @return suspension status
     */
    public boolean isSuspended() {
        return isSuspended;
    }

    // ===========================
    //  Setters (restricted)
    // ===========================

    /**
     * Sets user ID only once. Cannot be modified after the first set.
     * @param userId Firebase UID
     * @throws IllegalStateException if already set
     */
    public void setUserId(String userId) {
        if (!userIdLocked) {
            this.userId = userId;
            this.userIdLocked = true;
        } else {
            throw new IllegalStateException("userId can only be set once.");
        }
    }

    /**
     * Updates display name.
     * @param userName new display name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Updates password.
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets suspension status (admin only).
     * @param suspended true to suspend, false to release
     */
    protected abstract void setSuspended(boolean suspended);

    // ===========================
    //  Firebase Mapper
    // ===========================

    /**
     * Converts user object to key-value map for Firebase.
     * @return map representing user fields
     */
    protected Map<String, Object> toMapInternal() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userName", userName);
        map.put("email", email);
        map.put("password", password); // maybe unsafe
        map.put("accountType", accountType);
        map.put("isSuspended", isSuspended);
        return map;
    }

    public Map<String, Object> exportToFirebase() {
        Map<String, Object> safeMap = toMapInternal();
        safeMap.remove("password"); // remove sensitive info
        return safeMap;
    }

}
