package com.electrocoder.grader.util

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.electrocoder.grader.entities.Predmet
import java.util.concurrent.ExecutionException

object PodsjetnikWorkerUtil {


    /**
     * Funkcija koja provjerava da li je work pokrenut ili je "stavljen" na pokretanje
     */
    fun checkIsWorkScheduled(context: Context, tag: String): Boolean {

        val workManager = WorkManager.getInstance(context)
        val workInfos = workManager.getWorkInfosByTag(tag)

        try {
            var pokrenut = false

            val workStates = workInfos.get()
            for(workInfo in workStates) {
                val state = workInfo.state
                pokrenut = state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED
            }
            return pokrenut
        } catch (e: ExecutionException) {
            e.printStackTrace()
            return false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Prekida
     */
    fun cancelPredmetPodsjetnikWorkers(context: Context, predmet: Predmet) {
        if(checkIsWorkScheduled(context, predmet.imePredmeta + "1"))
            WorkManager.getInstance(context).cancelAllWorkByTag(predmet.imePredmeta + "1")
        if(checkIsWorkScheduled(context, predmet.imePredmeta + "7"))
            WorkManager.getInstance(context).cancelAllWorkByTag(predmet.imePredmeta + "7")
    }


}