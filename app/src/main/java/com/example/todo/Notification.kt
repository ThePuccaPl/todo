package com.example.todo

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        val intentA = Intent(context, MainActivity::class.java)
        intentA.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intentA,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "111")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setContentIntent(pendingIntent)
            .build()

        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(123, notification)
    }


//    override fun onReceive(context: Context, intent: Intent?) {
//        Log.d("this", "notify")
//
//        val intent = Intent(context, MainActivity::class.java)
//
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val taskName = intent.getStringExtra(titleExtra)
//        val taskText = intent.getStringExtra(messageExtra)
//
//        val builder = NotificationCompat.Builder(context, "111")
//            .setSmallIcon(R.drawable.baseline_notifications_24)
//            .setContentTitle(taskName)
//            .setContentText(taskText)
//            .setAutoCancel(true)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//            .setDefaults(NotificationCompat.DEFAULT_SOUND)
//            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//
//        val notificationManagerCompat = NotificationManagerCompat.from(context)
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        notificationManagerCompat.notify(123, builder.build())
//
//    }

}