package com.electrocoder.grader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.electrocoder.grader.entities.Predmet

class PredmetiViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var predmetiRepo: PredmetiRepo
    private lateinit var predmeti: LiveData<List<Predmet>>

    init {
        predmetiRepo = PredmetiRepo(application)
        predmeti = predmetiRepo.getAllPredmete()
    }

    fun insert(predmet: Predmet) = predmetiRepo.insert(predmet)

    fun updatePredmet(predmet: Predmet) = predmetiRepo.updatePredmet(predmet)

    fun getPredmet(idPredmeta: Int): Predmet = predmetiRepo.getPredmet(idPredmeta)

    fun getAllPredmete(): LiveData<List<Predmet>> = predmetiRepo.getAllPredmete()

    fun getPredmetiCount(): Int = predmetiRepo.getPredmetiCount()

    fun getPredmetiProsjekSum(): Float = predmetiRepo.getPredmetiProsjek()

    fun deleteAllPredmete() = predmetiRepo.deleteAllPredmete()

    fun deletePredmet(predmet: Predmet) = predmetiRepo.deletePredmet(predmet)

    fun clearOcjenePredmeta() = predmetiRepo.clearOcjenePredmeta()

    fun getAllPredmeteNoLiveData(): List<Predmet>? = predmetiRepo.getAllPredmeteNoLiveData()
}