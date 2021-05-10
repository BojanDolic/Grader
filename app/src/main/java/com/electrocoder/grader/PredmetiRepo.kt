package com.electrocoder.grader

import android.app.Application
import androidx.lifecycle.LiveData
import com.electrocoder.grader.entities.Predmet
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PredmetiRepo(application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var predmetiDAO: PredmetDAO?
    private lateinit var predmeti: LiveData<List<Predmet>>


    init {
        val database = PredmetiDatabase.getInstance(application)
        predmetiDAO = database?.predmetDAO()
        predmeti = predmetiDAO?.getAllPredmete()!!
    }

    fun insert(predmet: Predmet) {
        launch { insertPredmetInBackground(predmet) }
    }

    fun deletePredmet(predmet: Predmet) {
        launch { deletePredmetInBackground(predmet) }
    }

    fun deleteAllPredmete() {
        launch { deleteAllPredmeteInBackground() }
    }

    fun updatePredmet(predmet: Predmet) {
        launch { updatePredmetInBackground(predmet) }
    }


    fun getPredmet(idPredmeta: Int): Predmet {
        lateinit var predmet: Predmet
        runBlocking { predmet = getPredmetFromDatabase(idPredmeta) }

        return predmet
    }

    fun getAllPredmete(): LiveData<List<Predmet>> {
        return predmeti
    }

    fun getAllPredmeteNoLiveData(): List<Predmet>? {
        var predmeti: List<Predmet>? = null
        runBlocking {  predmeti = getAllPredmeteNoLiveDataBackground()}

        return predmeti
    }

    fun getPredmetiCount(): Int {
        var brojPredmeta = 0
        runBlocking { brojPredmeta = getPredmetiCountBackground() }

        return brojPredmeta
    }

    fun getPredmetiProsjek(): Float {
        var sumaPredmeta = 0.0f
        runBlocking { sumaPredmeta = getPredmetiProsjekBackground() }


        return sumaPredmeta
    }

    fun clearOcjenePredmeta() {
        launch { clearOcjenePredmetaInBackground() }
    }

    private suspend fun getPredmetiProsjekBackground(): Float {
        var brojPredmeta = 0.0f
        withContext(Dispatchers.IO) {
            brojPredmeta = predmetiDAO?.getPredmetiProsjek()!!
        }
        return brojPredmeta
    }

    private suspend fun getPredmetiCountBackground(): Int {
        var brojPredmeta = 0
        withContext(Dispatchers.IO) {
            brojPredmeta = predmetiDAO?.getPredmetiCount()!!
        }
        return brojPredmeta
    }

    private suspend fun getAllPredmeteNoLiveDataBackground(): List<Predmet>? {
        var predmeti: List<Predmet>? = null
        withContext(Dispatchers.IO) {
            predmeti = predmetiDAO?.getAllPredmeteNoLiveData()
        }
        return predmeti
    }

    private suspend fun deletePredmetInBackground(predmet: Predmet) {
        withContext(Dispatchers.IO) {
            predmetiDAO?.deletePredmet(predmet)
        }
    }

   private suspend fun getPredmetFromDatabase(idPredmeta: Int): Predmet {
        lateinit var predmet: Predmet
        withContext(Dispatchers.IO) {
            predmet = predmetiDAO?.getPredmet(idPredmeta)!!
        }
        return predmet
    }

    private suspend fun deleteAllPredmeteInBackground() {
        withContext(Dispatchers.IO) {
            predmetiDAO?.deleteAllPredmete()
        }
    }

    private suspend fun updatePredmetInBackground(predmet: Predmet) {
        withContext(Dispatchers.IO) {
            predmetiDAO?.updatePredmet(predmet)
        }
    }

    private suspend fun insertPredmetInBackground(predmet: Predmet) {
        withContext(Dispatchers.IO) {
            predmetiDAO?.insertPredmet(predmet)
        }
    }

    private suspend fun clearOcjenePredmetaInBackground() {

        withContext(Dispatchers.IO) {
            predmetiDAO?.clearOcjenePredmeta()
        }

    }

}