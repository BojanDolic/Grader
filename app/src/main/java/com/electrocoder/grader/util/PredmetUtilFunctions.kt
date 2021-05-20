package com.electrocoder.grader.util

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import androidx.core.graphics.ColorUtils
import com.electrocoder.grader.PredmetiViewModel
import com.electrocoder.grader.R
import com.electrocoder.grader.entities.Ocjena
import com.electrocoder.grader.entities.Predmet
import com.electrocoder.grader.entities.PredmetWithOcjena

object PredmetUtilFunctions {

    fun isPredmetNameAvailable(viewModel: PredmetiViewModel, imePredmeta: String): Boolean {

        val predmetName = imePredmeta.trim()
        var available = true

        val predmetList = viewModel.getAllPredmeteNoLiveData()

        if(predmetList != null && predmetList.isNotEmpty()) {

            for(predmet in predmetList) {
                if(TextUtils.equals(predmet.imePredmeta, predmetName))
                    available = false
            }
        }
        return available
    }

    fun predmetHasOcjene(predmet: PredmetWithOcjena): Boolean = predmet.ocjene.isNotEmpty()

    //fun deleteOcjenePredmeta(predmet: Predmet) = predmet.ocjene.clear()

    fun deleteOcjenePredmetaAndSave(predmetWithOcjena: PredmetWithOcjena, predmetiViewModel: PredmetiViewModel) {
        predmetiViewModel.deleteOcjenePredmeta(predmetWithOcjena.predmet.id)
    }

    /**
     * Funtion that returns color based on subject's marks
     *
     * @param context Context
     * @param ocjena mark from that subject
     *
     * @return returns color integer
     */
    fun returnColorBasedOnOcjena(context: Context, ocjena: Int): Int {

        if(ocjena < 1f) {
            return context.resources.getColor(R.color.colorSecondary, null)
        } else if(ocjena >= 1f && ocjena < 2f) {
            return context.resources.getColor(R.color.red, null)
        } else if(ocjena >= 2f && ocjena < 3f) {
            return context.resources.getColor(R.color.orange, null)
        } else if(ocjena >= 3f && ocjena < 4f) {
            return context.resources.getColor(R.color.yellow, null)
        } else if(ocjena >= 4f && ocjena <= 4.4f) {
            return context.resources.getColor(R.color.green, null)
        } else return context.resources.getColor(R.color.light_green, null)

    }

    /**
     * Function that returns color with alpha value of 15%
     *
     * @param color original color
     * @param alpha alpha parameter (default is 37 or about 15%)
     *
     * @return color with alpha value set
     */
    fun returnAlphedColor(color: Int, alpha: Int = 37): Int =
        ColorUtils.setAlphaComponent(color, alpha)


    fun calculateProsjek(ocjene: List<Ocjena>): Double {
        var prosjek: Double
        var zbir = 0;

        if(ocjene.isNotEmpty())
            for(ocjena in ocjene) {
                zbir += ocjena.ocjena
            }
        else return 0.0

        prosjek = zbir / ocjene.size.toDouble()

        return prosjek
    }
}