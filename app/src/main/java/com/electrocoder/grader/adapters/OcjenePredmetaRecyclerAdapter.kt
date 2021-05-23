package com.electrocoder.grader.adapters

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.electrocoder.grader.R
import com.electrocoder.grader.databinding.OcjenaItemPredmetLayoutBinding
import com.electrocoder.grader.entities.Ocjena
import com.electrocoder.grader.util.PredmetUtilFunctions
import java.text.SimpleDateFormat
import java.util.*

class OcjenePredmetaRecyclerAdapter : RecyclerView.Adapter<OcjenePredmetaRecyclerAdapter.OcjeneViewHolder>() {

    public interface OnOcjenaClickListener {
        fun onOcjenaLongClicked(ocjena: Ocjena)
    }

    private lateinit var clickListener: OnOcjenaClickListener

    private var ocjene = emptyList<Ocjena>()

    public fun setOcjenaClickListener(listener: OnOcjenaClickListener) {
        this.clickListener = listener
    }

    override fun getItemCount(): Int {
        return ocjene.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OcjeneViewHolder {
        val binding = OcjenaItemPredmetLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return OcjeneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OcjeneViewHolder, position: Int) {

        holder.populateData(ocjene[position])
        //.get(position)
        /*holder.ocjenaText.text = ocjene.get(position).toString()


        holder.ocjenaContainer.setCardBackgroundColor(returnColorBasedOnOcjena(holder.ocjenaContainer.context, ocjene.get(position)))*/


    }

    inner class OcjeneViewHolder(val binding: OcjenaItemPredmetLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {

            fun populateData(ocjena: Ocjena) {

                binding.ocjenaItemOcjenaText.text = ocjena.ocjena.toString()
                binding.ocjenaItemDatumText.text = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(ocjena.datumOcjene)

                val color = PredmetUtilFunctions.returnColorBasedOnOcjena(
                    binding.ocjenaItemOcjenaContainer.context,
                    ocjena.ocjena)

                binding.ocjenaItemOcjenaText.setTextColor(color)
                binding.ocjenaItemOcjenaContainer.setCardBackgroundColor(
                    PredmetUtilFunctions.returnAlphedColor(color))

                binding.root.setOnLongClickListener {
                    clickListener.onOcjenaLongClicked(ocjena)
                    true
                }

            }

    }

    fun updateList(noveOcjene: List<Ocjena>) {
        ocjene = noveOcjene
        notifyDataSetChanged()
    }

    fun getMinMaxValueRange(ocjena: Int, max: Float): Float = ocjena / max

}