package com.electrocoder.grader

import android.app.Application
import androidx.lifecycle.*
import com.electrocoder.grader.entities.Ocjena
import com.electrocoder.grader.entities.Predmet
import com.electrocoder.grader.entities.PredmetWithOcjena
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PredmetiViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var predmetiRepo: PredmetiRepo
    private lateinit var predmeti: LiveData<List<PredmetWithOcjena>>

    init {
        predmetiRepo = PredmetiRepo(application)
        predmeti = predmetiRepo.getAllPredmete()
    }

    fun insert(predmet: Predmet) {
        viewModelScope.launch {
            predmetiRepo.insert(predmet)
        }
    }

    fun insertPredmet(predmet: PredmetWithOcjena) {
        viewModelScope.launch {
            predmetiRepo.insertPredmetWithOcjene(predmet)
        }
    }

    fun insertOcjena(ocjena: Ocjena) {
        viewModelScope.launch {
            predmetiRepo.insertOcjenu(ocjena)
        }
    }

    fun updateOcjenePredmeta(predmet: PredmetWithOcjena) {
        viewModelScope.launch {
            predmetiRepo.updateOcjenePredmeta(predmet)
        }
    }

    suspend fun getProsjekPredmeta(): Float {
        return viewModelScope.async {
            predmetiRepo.getPredmetiProsjek()
        }.await()
    }

    fun updatePredmet(predmet: Predmet) {
        viewModelScope.launch {
            predmetiRepo.updatePredmet(predmet)
        }
    }

    fun getPredmet(idPredmeta: Long): LiveData<PredmetWithOcjena> {
        /*val predmet = MutableLiveData<PredmetWithOcjena?>()
        viewModelScope.launch {
            val returnedPredmet = predmetiRepo.getWholePredmet(idPredmeta)
            predmet.postValue(returnedPredmet)
        }*/
        val predmetLiveData = predmetiRepo.getWholePredmet(idPredmeta)
        return predmetLiveData
    }

    fun getAllPredmete(): LiveData<List<PredmetWithOcjena>> = predmetiRepo.getAllPredmete()

    suspend fun getPredmetiCount(): Int {
        val result = viewModelScope.async {
            predmetiRepo.getPredmetiCount()
        }
        return result.await()
    }

    fun deleteOcjenePredmeta(idPredmeta: Long) {
        viewModelScope.launch {
            predmetiRepo.clearOcjenePredmeta(idPredmeta)
        }
    }

    fun deleteOcjenu(ocjena: Ocjena) {
        viewModelScope.launch {
            predmetiRepo.deleteOcjenu(ocjena)
        }
    }

    fun getPredmetiProsjekSum(): Float = predmetiRepo.getPredmetiProsjek()

    fun deleteAllPredmete() {
        viewModelScope.launch {
            predmetiRepo.deleteAllPredmete()
        }
    }

    fun deletePredmet(predmet: Predmet) {
        viewModelScope.launch {
            predmetiRepo.deletePredmet(predmet)
        }
    }

    fun clearOcjenePredmeta() = predmetiRepo.clearOcjenePredmeta()

    fun getAllPredmeteNoLiveData(): List<Predmet>? = predmetiRepo.getAllPredmeteNoLiveData()
}