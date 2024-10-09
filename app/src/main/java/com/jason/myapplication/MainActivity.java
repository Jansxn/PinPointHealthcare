//package com.jason.myapplication;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.jason.myapplication.R; // Ensure this is correct
//
//public class MainActivity extends AppCompatActivity {
//
//    private FirebaseAuth mAuth;
//    private FirebaseUser currentUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        checkLoginStatus();
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.menu_home) {// Already in MainActivity, do nothing or handle as needed
//                return true;
//            } else if (itemId == R.id.menu_assessment) {
//                Intent assessmentIntent = new Intent(MainActivity.this, Assessment.class);
//                startActivity(assessmentIntent);
//                return true;
//            } else if (itemId == R.id.menu_profile) {
//                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
//                startActivity(profileIntent);
//                return true;
//            }
//            return false;
//        });
//    }
//
//    void checkLoginStatus() {
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//        if (currentUser == null) {
//            Intent intent = new Intent(MainActivity.this, AccountIntro.class);
//            startActivity(intent);
//            finish();
//        } else {
//            setContentView(R.layout.activity_main);
//        }
//    }
//
//}


//package com.jason.myapplication;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import java.util.Calendar;
//
//public class MainActivity extends AppCompatActivity {
//
//    private FirebaseAuth mAuth;
//    private FirebaseUser currentUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        checkLoginStatus();
//
//        // Initialize Bottom Navigation
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.menu_home) {
//                return true;
//            } else if (itemId == R.id.menu_assessment) {
//                Intent assessmentIntent = new Intent(MainActivity.this, Assessment.class);
//                startActivity(assessmentIntent);
//                return true;
//            } else if (itemId == R.id.menu_profile) {
//                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
//                startActivity(profileIntent);
//                return true;
//            }
//            return false;
//        });
//
//        // Set reminders on specific dates
//        setAssessmentReminders();
//    }
//
//    void checkLoginStatus() {
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//        if (currentUser == null) {
//            Intent intent = new Intent(MainActivity.this, AccountIntro.class);
//            startActivity(intent);
//            finish();
//        } else {
//            setContentView(R.layout.activity_main);
//        }
//    }
//
//    // Method to set up assessment reminders
//    private void setAssessmentReminders() {
//        Calendar[] reminderDates = {
//                getReminderDate(1, Calendar.JANUARY),
//                getReminderDate(1, Calendar.APRIL),
//                getReminderDate(1, Calendar.JULY),
//                getReminderDate(1, Calendar.OCTOBER)
//        };
//
//        for (Calendar reminderDate : reminderDates) {
//            setReminderAlarm(reminderDate);
//        }
//    }
//
//    private Calendar getReminderDate(int day, int month) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MONTH, month);
//        calendar.set(Calendar.DAY_OF_MONTH, day);
//        calendar.set(Calendar.HOUR_OF_DAY, 9);  // Time to send notification, 9 AM
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        if (calendar.before(Calendar.getInstance())) {
//            calendar.add(Calendar.YEAR, 1); // Set for the next year if date is past
//        }
//        return calendar;
//    }
//
//    private void setReminderAlarm(Calendar reminderDate) {
//        Intent intent = new Intent(this, AssessmentReminderReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                this, (int) reminderDate.getTimeInMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        if (alarmManager != null) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderDate.getTimeInMillis(), pendingIntent);
//        }
//    }
//}

package com.jason.myapplication;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private static final int POST_NOTIFICATIONS_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLoginStatus();

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        POST_NOTIFICATIONS_PERMISSION_CODE);
            }
        }

        // Initialize Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                return true;
            } else if (itemId == R.id.menu_assessment) {
                Intent assessmentIntent = new Intent(MainActivity.this, Assessment.class);
                startActivity(assessmentIntent);
                return true;
            } else if (itemId == R.id.menu_profile) {
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });

        // Set reminders on specific dates
        setAssessmentReminders();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == POST_NOTIFICATIONS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                // You can show a message or take other actions here
            }
        }
    }

    void checkLoginStatus() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, AccountIntro.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    // Method to set up assessment reminders
    private void setAssessmentReminders() {
        Calendar[] reminderDates = {
                getReminderDate(1, Calendar.JANUARY),
                getReminderDate(1, Calendar.APRIL),
                getReminderDate(1, Calendar.JULY),
                getReminderDate(1, Calendar.OCTOBER)
        };

        for (Calendar reminderDate : reminderDates) {
            setReminderAlarm(reminderDate);
        }
    }

    private Calendar getReminderDate(int day, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 9);  // Time to send notification, 9 AM
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.YEAR, 1); // Set for the next year if date is past
        }
        return calendar;
    }

    private void setReminderAlarm(Calendar reminderDate) {
        Intent intent = new Intent(this, AssessmentReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, (int) reminderDate.getTimeInMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderDate.getTimeInMillis(), pendingIntent);
        }
    }
}




