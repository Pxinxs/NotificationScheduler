package com.test.scheduler;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.test.scheduler.db.AppDatabase;
import com.test.scheduler.db.NotificationDao;
import com.test.scheduler.entity.room.Notification;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private AppDatabase database;
    private NotificationDao notificationDao;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                // allowing main thread queries, for testing only
                .allowMainThreadQueries()
                .build();
        notificationDao = database.notificationDao();
    }

    @Test
    public void writeNotificationAndRead() throws Exception {
        Notification notification = new Notification("2018-02-22", "12:00", "test note", 1);
        notificationDao.insert(notification);

        notificationDao.getNotifications()
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertValue(notifications -> notifications.get(0).equals(notification));
    }

    @Test
    public void clearNotifications() throws Exception {
        notificationDao.deleteTable();

        notificationDao.getNotifications()
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertValue(notifications -> notifications.size() == 0);
    }

    @After
    public void closeDB() throws IOException {
        database.close();
    }
}
