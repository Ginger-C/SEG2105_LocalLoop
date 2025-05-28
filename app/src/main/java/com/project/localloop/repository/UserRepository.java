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
 */
public class UserRepository {
    private static UserRepository instance; // Singleton per app, shared by all
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;
    // Collection references
    private final CollectionReference userRef = db.collection("users");
    private final CollectionReference eventRef = db.collection("events");

    private UserRepository() { // Private constructor, only entrance
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Singleton pattern.
     * @return
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

    public void registerUser(String email, String password, String name, Callback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    String uid = auth.getCurrentUser().getUid();
                    Map<String, Object> userDoc = new HashMap<>();
                    userDoc.put("userId", uid);
                    userDoc.put("userName", name);
                    userDoc.put("email", email);
                    userDoc.put("accountType", 1);
                    userDoc.put("isSuspended", false);

                    db.collection("users").document(uid).set(userDoc)
                            .addOnSuccessListener(r -> callback.onSuccess())
                            .addOnFailureListener(e -> callback.onError(e.getMessage()));
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}
