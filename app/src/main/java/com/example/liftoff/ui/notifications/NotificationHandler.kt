package com.example.liftoff.ui.notifications
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import kotlin.random.Random
import android.content.Intent
import android.content.BroadcastReceiver
import java.util.*
import androidx.work.*
import java.util.concurrent.TimeUnit
import android.util.*

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        // Your code to trigger the notification
        val notificationHandler = NotificationHandler(applicationContext)
        notificationHandler.showSimpleNotification()
        return Result.success()
    }
}

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "workout_reminder"

    // SIMPLE NOTIFICATION
    fun showSimpleNotification() {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle("Workout Reminder")
            .setSmallIcon(android.R.drawable.ic_menu_my_calendar)  // Specify a valid small icon here
            .setContentText("It is time for your workout!")
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()  // finalizes the creation

        notificationManager.notify(Random.nextInt(), notification)
    }

    fun scheduleWorkoutNotification(context: Context, hour: Int, minute: Int) {
        val now = System.currentTimeMillis()

        // Calculate the delay
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.timeInMillis < now) {
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Schedule for the next day if time has passed
        }
        val delay = calendar.timeInMillis - now

        // Schedule the WorkManager
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Trigger the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, "workout_reminder_channel")
            .setContentTitle("Workout Reminder")
            .setContentText("It's time for your workout!")
            .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}