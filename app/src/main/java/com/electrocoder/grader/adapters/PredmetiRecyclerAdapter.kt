package com.electrocoder.grader.adapters

import android.content.Context
import android.view.*
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.electrocoder.grader.R
import com.electrocoder.grader.databinding.PredmetItemBinding
import com.electrocoder.grader.entities.Predmet
import com.electrocoder.grader.util.Constants
import com.electrocoder.grader.util.PredmetUtilFunctions
import com.google.android.material.card.MaterialCardView

//import kotlinx.android.synthetic.main.predmet_item.view.*

class PredmetiRecyclerAdapter(val context: Context) : ListAdapter<Predmet, PredmetiRecyclerAdapter.ViewHolder>(DiffCallback()) {

    lateinit var predmetClickListener: OnPredmetClickListener
    interface OnPredmetClickListener {
        fun onPredmetClicked(predmet: Predmet, prosjekText: TextView, backgroundCard: MaterialCardView, pozicija: Int)
        fun onPredmetLongClicked(view: View, predmet: Predmet)
        fun onContextMenuClicked(predmet: Predmet, opcija: Constants.CONTEXT_MENU_OPTIONS)
    }


    class DiffCallback : DiffUtil.ItemCallback<Predmet>() {

        override fun areItemsTheSame(oldItem: Predmet, newItem: Predmet): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Predmet, newItem: Predmet): Boolean {
            return oldItem.imePredmeta.equals(newItem.imePredmeta) &&
                    oldItem.ocjene.equals(newItem.ocjene) &&
                    oldItem.prosjek == newItem.prosjek
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PredmetItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

   /* override fun getItemCount(): Int {
        return predmeti.size
    } */

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var predmet: Predmet = getItem(position)

        holder.populateData(predmet)



        //Log.d("ADAPTER", predmet.imePredmeta)

    }


    inner class ViewHolder(val binding: PredmetItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var prosjek: TextView
        var imePredmeta: TextView
        var predmetCard: MaterialCardView
        var opcijePredmeta: ImageButton

        fun populateData(predmet: Predmet) {

            binding.imePredmetaText.text = predmet.imePredmeta
            binding.prosjekPredmetaText.text = String.format("%.2f", predmet.prosjek)
            binding.prosjekPredmetaText.transitionName = "prosjekText_$position"

            val bojaPredmeta = PredmetUtilFunctions.returnColorBasedOnOcjena(
                binding.root.context,
                predmet.prosjek.toInt()
            )

            binding.prosjekPredmetaText.setTextColor(bojaPredmeta)

            binding.prosjekPredmetaContainer.setBackgroundColor(
                PredmetUtilFunctions.returnAlphedColor(bojaPredmeta)
            )

        }

        init {
            imePredmeta = itemView.findViewById(R.id.imePredmetaText)
            prosjek = itemView.findViewById(R.id.prosjekPredmetaText)
            predmetCard = itemView.findViewById(R.id.predmetBackgroundCard)
            opcijePredmeta = itemView.findViewById(R.id.opcijePredmetaBtn)

            itemView.setOnClickListener {
                val position = adapterPosition
                predmetClickListener.onPredmetClicked(getItem(position), prosjek, predmetCard, position)
            }

            opcijePredmeta.setOnClickListener {

                val popupMenu = PopupMenu(context, opcijePredmeta, Gravity.START, R.style.RoundedPopupMenu, R.style.RoundedPopupMenu)
                popupMenu.inflate(R.menu.predmet_options_menu)

                popupMenu.setOnMenuItemClickListener {

                    when(it.itemId) {

                        R.id.predmet_context_menu_delete_predmet -> {
                            predmetClickListener.onContextMenuClicked(getItem(position), Constants.CONTEXT_MENU_OPTIONS.CONTEXT_MENU_DELETE_PREDMET)
                        }
                        R.id.predmet_context_menu_delete_ocjene -> {
                            predmetClickListener.onContextMenuClicked(getItem(position), Constants.CONTEXT_MENU_OPTIONS.CONTEXT_MENU_DELETE_OCJENE_PREDMETA)
                        }

                    }

                    true
                }
                popupMenu.show()
            }

           /*itemView.setOnLongClickListener {

                true
            }*/

            /**
             * Treba dodati context menu
             * Dugim klikom na jedan od predmeta, treba se otvoriti meni
             */

           /* itemView.setOnLongClickListener {
                val position = adapterPosition
                predmetClickListener.onPredmetLongClicked(itemView, getItem(position))
                true

            }*/



        }

    }



    fun getPredmetAt(position: Int): Predmet {
        return getItem(position)
    }

    /*fun loadPredmete(predmeti: List<Predmet>) {
        this.predmeti = predmeti
        notifyDataSetChanged()
    }*/

    fun setOnPredmetClickListener(predmetClickListener: OnPredmetClickListener) {
        this.predmetClickListener = predmetClickListener
    }




}