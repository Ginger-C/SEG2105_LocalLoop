package com.project.localloop.repository;
//Import firebase related libraries
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncListUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
// For Live Data Updates
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.localloop.database.Category;
import com.project.localloop.database.Event;
import com.project.localloop.database.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataRepository.java
 * Repository layer encapsulating Firebase operations. Link UI to DB
 * 2025/6/1 Update: for data fetching, following logic of:
 *  - User: data fetch upon refresh => manually
 *  - Event: auto data fetching => auto Listener
 *  - Category: data fetch upon refresh/in event page => manually
 * @author Ginger-C
 * @since 2025-05-26
 */
public class DataRepository {
    private static DataRepository instance; // Singleton per app, shared by all
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;
    // Collection references
    private final CollectionReference userRef;
    private final CollectionReference eventRef;
    private final CollectionReference categoryRef;

    // Live Data from firebase
    private final MutableLiveData<User> userInstanceData = new MutableLiveData<>();
    private final MutableLiveData<Event> eventInstanceData = new MutableLiveData<>();
    // Swipe refresh manually, thus no need to use livedata
    private final List <User> allRegisteredUsers = new ArrayList<>();
    private final List <Category> allRegisteredCategories = new ArrayList<>();
    // Auto refresh, need to use livedata
    private final MutableLiveData<List<Event>> allRegisteredEvents = new MutableLiveData<>();
    // One listener per collection / instance
    private ListenerRegistration listenSelectedUser;
    private ListenerRegistration listenSelectedEvent;
    private ListenerRegistration eventsListener;
    private ListenerRegistration categoriesListener;


    //======================================================================
    // Constructor
    //======================================================================
    private DataRepository() { // Private constructor, only entrance
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Getting colleciton references
        userRef = db.collection("users");
        eventRef = db.collection("events");
        categoryRef = db.collection("categories");


        // Set listener to observe Event collection data change + initialize registered event
        eventsListener = eventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("DataRepository", "In getting all Events: Listen failed.", e);
                    e.printStackTrace();
                    return;
                }
                List<Event> currentEventList = new ArrayList<>();
                if (snapshots != null) {
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Event currentEvent = doc.toObject(Event.class);
                        currentEvent.setEventUID(doc.getId());//set UID
                        currentEventList.add(currentEvent);
                    }
                }
                allRegisteredEvents.postValue(currentEventList);
            }
        });
    }
    /**
     * Singleton pattern.
     * @return instance of DataRepository,which contains unique firebase
     */
    public static synchronized DataRepository getInstance() { // use synchronized to avoid concurrent access
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository();
                }
            }
        }
        return instance;
    }
    public interface Callback {
        void onSuccess();
        void onError(String error);
    }

    public void registerUser(String email, String password, String userName, long accountType,Callback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    // Do not record password in firestore
                    String uid = auth.getCurrentUser().getUid(); //access auth UID
                    Map<String, Object> userDoc = new HashMap<>();
                    userDoc.put("userId", uid); // assign auth UID to firestore document UID
                    userDoc.put("userName", userName);
                    userDoc.put("email", email);
                    userDoc.put("accountType", accountType);
                    userDoc.put("isSuspended", false);

                    userRef.document(uid).set(userDoc)
                            .addOnSuccessListener(r -> callback.onSuccess())
                            .addOnFailureListener(e -> callback.onError(e.getMessage()));
                })
              .addOnFailureListener(e -> {
                    // Failed to write in Firestore: delete authen
                    auth.getCurrentUser()
                            .delete()
                            .addOnCompleteListener(del -> {
                                callback.onError("Failed to save profile: " + e.getMessage());
                            });
                });
    }

    public void loginUser(String email, String password, Callback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void registerEvent(Map<String, Object> eventData, Callback callback) {
        eventRef.add(eventData)
                .addOnSuccessListener(docRef -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    // Grabbing user info
    public void loginAndLoadUserData(String email, String password, UserDataCallback callback) {
        auth.signInWithEmailAndPassword(email, password.trim()) // Ignore \n and \t
                .addOnSuccessListener(result -> {
                    String uid = auth.getCurrentUser().getUid();
                    userRef.document(uid).get()
                            .addOnSuccessListener(snapshot -> {
                                if (snapshot.exists()) {
                                    String userName = snapshot.getString("userName");
                                    Long temp = snapshot.getLong("accountType"); // dont use primitive, for convert
                                    int accountType = temp !=null ? temp.intValue():-1;
                                    callback.onSuccess(userName, accountType);
                                } else {
                                    callback.onError("User info invalid");
                                }
                            })
                            .addOnFailureListener(e -> callback.onError("Failed to load user info: " + e.getMessage()));
                })
                .addOnFailureListener(e -> callback.onError("Failed to log in: " + e.getMessage()));
    }

    // Data interface
    public interface UserDataCallback {
        void onSuccess(String userName, int accountType);
        void onError(String error);
    }

    // =============================================
    //              Fetching Data
    // =============================================
    public List<User> getAllUsers() {
        return allRegisteredUsers;
    }
    public LiveData<List<Event>> getAllEvents() {
        return allRegisteredEvents;
    }
    // Users
    // 1. Grabbing single info
    public MutableLiveData<User> getUserInstance(String uid) {
        listenSelectedUser = userRef.document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                if (doc != null && doc.exists()) {
                    User user = doc.toObject(User.class);
                    if (user != null) {
                        user.setUserId(doc.getId());
                        userInstanceData.postValue(user);
                    }
                }
            }
        });
        return userInstanceData;
    }

    // 2. Grabbing list of all registered users (MANUALLY)
    public interface RegUserListCallback {
        void onSuccess(List<User> registeredUserList);
        void onError(String errorMsg);
    }
    public void fetchAllRegisteredUsersOnce(RegUserListCallback callback) {
        userRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    //temporary: clear before refresh
                    allRegisteredUsers.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        User user = doc.toObject(User.class);
                        user.setUserId(doc.getId());
                        allRegisteredUsers.add(user);
                    }
                    Log.d("HomeAdminFragment", "user list size = " + allRegisteredUsers.size());
                    callback.onSuccess(allRegisteredUsers);
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    // Events
    // 1. Grabbing single event info
    public MutableLiveData<Event> getEventInstance(String uid) {
        listenSelectedEvent = eventRef.document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                if (doc != null && doc.exists()) {
                    Event selectedEvent = doc.toObject(Event.class);
                    if (selectedEvent != null) {
                        selectedEvent = doc.toObject(Event.class);
                        eventInstanceData.postValue(selectedEvent);
                    }
                }
            }
        });
        return eventInstanceData;
    }
    // Categories
    // 1. Grabbing single Category info
    // 2. Grabbing all categories once
    /*
       private void fetchAllCategories()
       {  // Set listen to observe data change.
        // Set listener to observe User collection data change + initialize registered users
        if (usersListener == null) {
            usersListener = userRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e("DataRepository", "In getting all users: Listen failed.", e);
                        e.printStackTrace();
                        return;
                    }
                    List<User> currentUserList = new ArrayList<>();
                    if (snapshots != null) {
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Log.d("DataRepository", "users snapshot received: " + snapshots.size());
                            User currentUser = doc.toObject(User.class);
                            currentUser.setUserId(doc.getId()); //set UID
                            currentUserList.add(currentUser);
                        }
                    }
                    allRegisteredUsers.postValue(currentUserList);
                }
            });
        }}

     */

    // Remove listeners
    public void removeAllListeners() {
        if (eventsListener != null) {
            eventsListener.remove();
            eventsListener = null;
        }
        if (categoriesListener != null) {
            categoriesListener.remove();
            categoriesListener = null;
        }
        if (listenSelectedUser != null) {
            listenSelectedUser.remove();
            listenSelectedUser = null;
        }
        if (listenSelectedEvent != null) {
            listenSelectedEvent.remove();
            listenSelectedEvent = null;
        }
    }
}