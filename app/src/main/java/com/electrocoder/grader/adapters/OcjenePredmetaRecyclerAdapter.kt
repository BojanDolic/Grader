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

class OcjenePredmetaRecyclerAdapter : RecyclerView.Adapter<OcjenePredmetaRecyclerAdapter.OcjeneViewHolder>() {

    lateinit private var ocjene: ArrayList<Int>


    override fun getItemCount(): Int {
        return ocjene.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OcjeneViewHolder {
        return OcjeneViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ocjena_item_predmet_layout, parent, false))
    }

    override fun onBindViewHolder(holder: OcjeneViewHolder, position: Int) {
        holder.ocjenaText.text = ocjene.get(position).toString()


        holder.ocjenaContainer.setCardBackgroundColor(returnColorBasedOnOcjena(holder.ocjenaContainer.context, ocjene.get(position)))


    }

    inner class OcjeneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ocjenaText: TextView
        var ocjenaContainer: CardView

        init {
            ocjenaText = itemView.findViewById(R.id.ocjenaPredmetaItemText)
            ocjenaContainer = itemView.findViewById(R.id.ocjena_item_container)

        }
    }

    fun updateList(noveOcjene: ArrayList<Int>) {
        ocjene = noveOcjene
        notifyDataSetChanged()
    }

    fun getMinMaxValueRange(ocjena: Int, max: Float): Float = ocjena / max

    fun returnColorBasedOnOcjena(context: Context, ocjena: Int): Int {

        if(ocjena >= 1f && ocjena < 2f) {
            return context.resources.getColor(R.color.red, null)
        } else if(ocjena >= 2f && ocjena < 3f) {
            return context.resources.getColor(R.color.orange, null)
        } else if(ocjena >= 3f && ocjena < 4f) {
            return context.resources.getColor(R.color.yellow, null)
        } else if(ocjena >= 4f && ocjena <= 4.4f) {
            return context.resources.getColor(R.color.green, null)
        } else return context.resources.getColor(R.color.light_green, null)

    }

}