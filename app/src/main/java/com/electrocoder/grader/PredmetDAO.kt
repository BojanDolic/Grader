package com.electrocoder.grader

import androidx.lifecycle.LiveData
import androidx.room.*
import com.electrocoder.grader.entities.Ocjena
import com.electrocoder.grader.entities.Predmet
import com.electrocoder.grader.entities.PredmetWithOcjena
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

@Dao
interface PredmetDAO {

    /**
     * Function returns long which is the id of the inserted object (predmet)
     * @see https://developer.android.com/training/data-storage/room/accessing-data#convenience-insert
     *
     */
    @Insert
    fun insertPredmet(predmet: Predmet): Long

    @Insert
    fun insertOcjene(ocjene: List<Ocjena>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOcjena(ocjena: Ocjena)


    @Transaction
    suspend fun insertPredmetWithOcjene(predmet: PredmetWithOcjena) {
        val id = insertPredmet(predmet.predmet)
        //for(ocjena in predmet.ocjene)
        predmet.ocjene.forEach {
            it.predmetId = id
        }
        insertOcjene(predmet.ocjene)
    }

    @Update
    suspend fun updateOcjena(ocjena: Ocjena)

    @Transaction
    suspend fun insertOcjene(predmet: PredmetWithOcjena) {
        for (ocjena in predmet.ocjene) {
            ocjena.predmetId = predmet.predmet.id
            insertOcjena(ocjena)
        }
    }

    @Update
    fun updatePredmet(predmet: Predmet)

    @Delete
    suspend fun deletePredmet(predmet: Predmet)

    @Delete
    suspend fun deleteOcjenu(ocjena: Ocjena)

    @Query("DELETE FROM tabela_predmeta")
    suspend fun deleteAllPredmete()

    @Query("SELECT * FROM tabela_predmeta WHERE predmet_id = :idPredmeta")
    fun getPredmet(idPredmeta: Long): Flow<Predmet>

    @Transaction // IN (SELECT DISTINCT(predmet_id) FROM tabela_ocjena)
    @Query("SELECT * FROM tabela_predmeta WHERE predmet_id = :idPredmeta")
    fun getPredmetWithOcjene(idPredmeta: Long): Flow<PredmetWithOcjena>

    @Query("SELECT * FROM tabela_ocjena WHERE predmet_id = :idPredmeta")
    fun getOcjene(idPredmeta: Long): Flow<List<Ocjena>>



    @Query("SELECT COUNT(predmet_id) FROM tabela_predmeta")
    fun getPredmetiCount(): Int

    // Calculates average for all subjects
    @Query("SELECT AVG(prosjek) FROM ( SELECT AVG(ocjena) AS prosjek FROM tabela_ocjena GROUP BY predmet_id )")
    fun getPredmetiProsjek(): Float

    @Transaction
    @Query("SELECT * FROM tabela_predmeta")
    fun getAllPredmete(): LiveData<List<PredmetWithOcjena>>

    @Query("SELECT * FROM tabela_predmeta")
    fun getAllPredmeteNoLiveData(): List<Predmet>

    // Clears all marks from all subjects
    @Query("DELETE FROM tabela_ocjena")
    fun clearOcjeneAllPredmeta()

    // Clears marks from particular subject
    @Query("DELETE FROM tabela_ocjena WHERE predmet_id = :idPredmeta")
    suspend fun clearOcjenePredmeta(idPredmeta: Long)


}