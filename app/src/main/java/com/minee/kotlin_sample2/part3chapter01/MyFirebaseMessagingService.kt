package com.minee.kotlin_sample2.part3chapter01

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.minee.kotlin_sample2.R

class MyFirebaseMessagingService() : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("minee new token", "" + p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("minee", "")

        createNotificationChannel()

        val type = remoteMessage.data["type"]
            ?.let { NotificationType.valueOf(it) }
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        type ?: return

        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)

        }
    }

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?
    ): Notification {
        val intent = Intent(this, P3C01MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "\uD83E\uDD29 \uD83E\uDD73 \uD83D\uDE0F \uD83D\uDE12 \uD83D\uDE1E " +
                                    "\uD83D\uDE14 \uD83D\uDE1F \uD83D\uDE15 \uD83D\uDE41 ☹️ \uD83D\uDE23 " +
                                    "\uD83D\uDE16 \uD83D\uDE2B \uD83D\uDE29 \uD83E\uDD7A \uD83D\uDE22 " +
                                    "\uD83D\uDE2D \uD83D\uDE24 \uD83D\uDE20 \uD83D\uDE21 \uD83E\uDD2C " +
                                    "\uD83E\uDD2F \uD83D\uDE33 \uD83E\uDD75 \uD83E\uDD76 \uD83D\uDE31 " +
                                    "\uD83D\uDE28 \uD83D\uDE30 \uD83D\uDE25 \uD83D\uDE13 \uD83E\uDD17 " +
                                    "\uD83E\uDD14 \uD83E\uDD2D \uD83E\uDD2B \uD83E\uDD25 \uD83D\uDE36 " +
                                    "\uD83D\uDE10 \uD83D\uDE11 \uD83D\uDE2C \uD83D\uDE44 \uD83D\uDE2F " +
                                    "\uD83D\uDE26 \uD83D\uDE27 \uD83D\uDE2E \uD83D\uDE32 \uD83E\uDD71 " +
                                    "\uD83D\uDE34 \uD83E\uDD24 \uD83D\uDE2A \uD83D\uDE35 \uD83E\uDD10 " +
                                    "\uD83E\uDD74 \uD83E\uDD22 \uD83E\uDD2E \uD83E\uDD27 \uD83D\uDE37 " +
                                    "\uD83E\uDD12 \uD83E\uDD15 \uD83E\uDD11 \uD83E\uDD20 \uD83D\uDE08 " +
                                    "\uD83D\uDC7F \uD83D\uDC79"
                        )
                )
            }
            NotificationType.CUSTOM -> {
                notificationBuilder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(packageName, R.layout.view_custom_notification)
                            .apply {
                                setTextViewText(R.id.titleTextView, title)
                                setTextViewText(R.id.messageTextView, message)
                            }
                    )
            }
        }

        return notificationBuilder.build()
    }

    companion object {
        private const val CHANNEL_NAME = "Emoji party"
        private const val CHANNEL_DESCRIPTION = "Emoji party 를 위한 채널"
        private const val CHANNEL_ID = "Channel id"
    }
}