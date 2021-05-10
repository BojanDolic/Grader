package com.electrocoder.grader

import androidx.lifecycle.LiveData
import androidx.room.*
import com.electrocoder.grader.entities.Predmet
import com.electrocoder.grader.entities.PredmetWithOcjena

@Dao
interface PredmetDAO {

    @Insert
    fun insertPredmet(predmet: Predmet)

    @Update
    fun updatePredmet(predmet: Predmet)

    @Delete
    fun deletePredmet(predmet: Predmet)

    @Query("DELETE FROM tabela_predmeta")
    fun deleteAllPredmete()

    @Query("SELECT * FROM tabela_predmeta WHERE id = :idPredmeta")
    fun getPredmet(idPredmeta: Int): Predmet

    @Query("SELECT COUNT(id) FROM tabela_predmeta WHERE prosjek > 0.0")
    fun getPredmetiCount(): Int

    @Query("SELECT SUM(prosjek) FROM tabela_predmeta")
    fun getPredmetiProsjek(): Float

    @Transaction
    @Query("SELECT * FROM tabela_predmeta")
    fun getAllPredmete(): LiveData<List<PredmetWithOcjena>>

    @Query("SELECT * FROM tabela_predmeta")
    fun getAllPredmeteNoLiveData(): List<Predmet>


    @Query("UPDATE tabela_predmeta SET ocjene = '' ")
    fun clearOcjenePredmeta()


}