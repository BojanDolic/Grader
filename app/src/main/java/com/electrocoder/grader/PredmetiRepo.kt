package com.electrocoder.grader

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.electrocoder.grader.database.PredmetiDatabase
import com.electrocoder.grader.entities.Ocjena
import com.electrocoder.grader.entities.Predmet
import com.electrocoder.grader.entities.PredmetWithOcjena
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PredmetiRepo(application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var predmetiDAO: PredmetDAO?
    private lateinit var predmeti: LiveData<List<PredmetWithOcjena>>


    init {
        val database = PredmetiDatabase.getInstance(application)
        predmetiDAO = database?.predmetDAO()
        predmeti = predmetiDAO?.getAllPredmete()!!
    }

    suspend fun insert(predmet: Predmet) {
        insertPredmetInBackground(predmet)
    }

    suspend fun updateOcjenePredmeta(predmet: PredmetWithOcjena) {
        predmetiDAO?.insertOcjene(predmet)
    }

    suspend fun insertPredmetWithOcjene(predmet: PredmetWithOcjena) {
        predmetiDAO?.insertPredmetWithOcjene(predmet)
    }

    suspend fun insertOcjenu(ocjena: Ocjena) {
        predmetiDAO?.insertOcjena(ocjena)
    }

    suspend fun deletePredmet(predmet: Predmet) {
        predmetiDAO?.deletePredmet(predmet)
    }

    suspend fun deleteAllPredmete() {
        predmetiDAO?.deleteAllPredmete()
    }

    fun getWholePredmet(idPredmeta: Long): LiveData<PredmetWithOcjena> {
        return predmetiDAO?.getPredmetWithOcjene(idPredmeta)?.asLiveData()!!
    }

    fun updatePredmet(predmet: Predmet) {
        launch { updatePredmetInBackground(predmet) }
    }

    suspend fun getProsjekPredmeta(): Float {
        return predmetiDAO?.getPredmetiProsjek()!!
    }

    suspend fun clearOcjenePredmeta(idPredmeta: Long) {
        predmetiDAO?.clearOcjenePredmeta(idPredmeta)
    }

    /*suspend fun getPredmet(idPredmeta: Long): Predmet? {
        return predmetiDAO?.getPredmet(idPredmeta)
        *//*lateinit var predmet: Predmet
        runBlocking { predmet = getPredmetFromDatabase(idPredmeta) }

        return predmet*//*
    }*/

    fun getAllPredmete(): LiveData<List<PredmetWithOcjena>> {
        return predmeti
    }

    fun getAllPredmeteNoLiveData(): List<Predmet>? {
        var predmeti: List<Predmet>? = null
        runBlocking {  predmeti = getAllPredmeteNoLiveDataBackground()}

        return predmeti
    }

    suspend fun getPredmetiCount(): Int {
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

   /*private suspend fun getPredmetFromDatabase(idPredmeta: Int): Predmet {
        lateinit var predmet: Predmet
        withContext(Dispatchers.IO) {
            predmet = predmetiDAO?.getPredmet(idPredmeta)!!
        }
        return predmet
    }*/

    /*private suspend fun deleteAllPredmeteInBackground() {
        withContext(Dispatchers.IO) {
            predmet.deleteAllPredmete()
        }
    }*/

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
            predmetiDAO?.clearOcjeneAllPredmeta()
        }

    }
}