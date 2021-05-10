package com.electrocoder.grader.util

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.electrocoder.grader.PredmetiViewModel
import com.electrocoder.grader.R
import kotlinx.android.synthetic.main.fragment_predmet.view.*

class PodsjetnikTestWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    private lateinit var predmetiViewModel: PredmetiViewModel


    override fun doWork(): Result {

        // Prikazati notifikaciju
        showTestNotification(inputData.getString(Constants.IME_PREDMETA_TAG), inputData.getInt(Constants.PODSJETNIK_TESTA_TEXT, 0))




        return Result.success()
    }

    /**
     * Pokazuje notifikaciju u zavisnosti od dana preostalih do testa
     */

    fun showTestNotification(imePredmeta: String?, idNotifikacije: Int) {


        if(idNotifikacije == Constants.PRVI_PODSJETNIK_TESTA_ID) {

            val notificationBuilder = NotificationCompat.Builder(applicationContext, Constants.PODSJETNIK_TESTA_CHANNEL_ID)
                .setContentTitle("Grader | Podsjetnik za test")
                //.setContentText("Za 7 dana imaš test iz predmeta $imePredmeta\n. Bilo bi dobro da počneš učiti na vrijeme.\n\n Tvoj Grader :)")
                .setSmallIcon(R.drawable.grader_icon)
                .setStyle(NotificationCompat.BigTextStyle().bigText("Za 7 dana imaš test iz predmeta $imePredmeta.\nBilo bi dobro da počneš učiti na vrijeme.\n\nTvoj Grader :)"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .build()

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(20, notificationBuilder)
            }

        } else if(idNotifikacije == Constants.DRUGI_PODSJETNIK_TESTA_ID) {
            val notificationBuilder = NotificationCompat.Builder(applicationContext, Constants.PODSJETNIK_TESTA_CHANNEL_ID)
                .setContentTitle("Grader | Podsjetnik za test")
                //.setContentText("Sutra imaš test iz predmeta $imePredmeta\n. Bilo bi dobro da ponoviš gradivo pred test.\n\n Tvoj Grader :)")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Sutra imaš test iz predmeta $imePredmeta.\nBilo bi dobro da ponoviš gradivo pred test.\n\nTvoj Grader :)"))
                .setSmallIcon(R.drawable.grader_icon)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .build()

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(20, notificationBuilder)
            }

        }




    }
}