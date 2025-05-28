package com.project.localloop.repository;
//Import firebase related libraries
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * UserRepository.java
 * Repository layer encapsulating Firebase operations. Link UI to DB
 * @author Ginger-C
 * @since 2025-05-26
 */
public class UserRepository {
    private static UserRepository instance; // Singleton per app, shared by all
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;
    // Collection references
    private final CollectionReference userRef;
    private final CollectionReference eventRef;

    private UserRepository() { // Private constructor, only entrance
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize 初始化 CollectionReference
        userRef = db.collection("users");
        eventRef = db.collection("events");
    }

    /**
     * Singleton pattern.
     * @return instance of UserRepository,which contains unique firebase
     */
    public static synchronized UserRepository getInstance() { // use synchronized to avoid concurrent access
        if (instance == null) {
            instance = new UserRepository(); // only create upon first call
        }
        return instance;
    }
    public interface Callback {
        void onSuccess();
        void onError(String error);
    }

    public void registerUser(String email, String password, String userName, int accountType,Callback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    String uid = auth.getCurrentUser().getUid();
                    Map<String, Object> userDoc = new HashMap<>();
                    userDoc.put("userId", uid);
                    userDoc.put("userName", userName);
                    userDoc.put("email", email);
                    userDoc.put("accountType", accountType);
                    userDoc.put("isSuspended", false);

                    userRef.document(uid).set(userDoc)
                            .addOnSuccessListener(r -> callback.onSuccess())
                            .addOnFailureListener(e -> callback.onError(e.getMessage()));
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
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
        auth.signInWithEmailAndPassword(email, password)
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


}
