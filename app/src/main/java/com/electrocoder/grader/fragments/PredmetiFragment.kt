package com.electrocoder.grader.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.electrocoder.grader.PredmetiViewModel
import com.electrocoder.grader.R
import com.electrocoder.grader.adapters.PredmetiRecyclerAdapter
import com.electrocoder.grader.databinding.PredmetiFragmentBinding
import com.electrocoder.grader.entities.Predmet
import com.electrocoder.grader.util.Constants
import com.electrocoder.grader.util.PodsjetnikWorkerUtil
import com.electrocoder.grader.util.PredmetUtilFunctions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.snackbar.Snackbar

/**
 * Ovaj fragment sadrži čitav UI vezan za listu predmeta
 * Ujedno je i prvi screen koji se pojavljuje pri pokretanju aplikacije
 */


class PredmetiFragment : Fragment(), PredmetiRecyclerAdapter.OnPredmetClickListener {

    private lateinit var predmetiViewModel: PredmetiViewModel
    private lateinit var predmetiRecyclerAdapter: PredmetiRecyclerAdapter

    private var _binding: PredmetiFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var bannerAd: AdView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PredmetiFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        //bannerAd = AdView(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MobileAds.initialize(requireContext())
        binding.predmetiBannerAd.loadAd(AdRequest.Builder().build())


        binding.predmetiRecycler.layoutManager = LinearLayoutManager(context)
        predmetiRecyclerAdapter = PredmetiRecyclerAdapter(requireContext())
        predmetiRecyclerAdapter.setOnPredmetClickListener(this)
        binding.predmetiRecycler.adapter = predmetiRecyclerAdapter

        predmetiViewModel = ViewModelProvider(this).get(PredmetiViewModel::class.java)

        // Dobijanje predmeta iz baze i "posmatranje" na promjene
        predmetiViewModel.getAllPredmete().observe(viewLifecycleOwner, Observer { predmeti ->
            predmetiRecyclerAdapter.submitList(predmeti)

            for (predmet in predmeti) {
                if(WorkManager.getInstance(requireContext()).getWorkInfosByTag(predmet.imePredmeta + "1").isDone) {
                    predmet.zakazanTest.value = false
                    predmetiViewModel.updatePredmet(predmet)
                }
            }

        })



        // Brisanje predmeta sa swipe
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            /**
             * Ako korisnik prevuče predmet ka lijevo, predmet se briše
             * Prije brisanja moramo
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val predmet = predmetiRecyclerAdapter.getPredmetAt(viewHolder.adapterPosition)

                // Provjerava stanje workera i ujedno ih zaustavlja
                PodsjetnikWorkerUtil.cancelPredmetPodsjetnikWorkers(requireContext(), predmet)

                predmetiViewModel.deletePredmet(predmet)
                val snackbar = Snackbar.make(binding.root, "Obrisali ste predmet: ${predmet.imePredmeta}", Snackbar.LENGTH_SHORT)
                snackbar.anchorView = binding.addPredmetBtn
                snackbar.show()
            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.predmetiRecycler)


        binding.addPredmetBtn.setOnClickListener {

            var navOptions: NavOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .build()

            activity?.findNavController(R.id.navigation_host)?.navigate(R.id.addPredmetFragment, null, navOptions)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Za meni tokom dugog klika na predmet
     */
    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.predmet_options_menu, menu)
    }



    override fun onPredmetClicked(predmet: Predmet, prosjekText: TextView, backgroundCard: MaterialCardView, pozicija: Int) {

        val action = PredmetiFragmentDirections.actionPredmetiFragmentToPredmetFragment(predmet.id!!, pozicija, predmet.imePredmeta)

        val extras = FragmentNavigatorExtras(prosjekText to prosjekText.transitionName,
            binding.addPredmetBtn to "addPredmetAndOcjenuFab")

        findNavController().navigate(action, extras)

        //val bundle = bundleOf("izabranPredmet" to predmet.id, "imePredmeta" to predmet.imePredmeta)

        //findNavController().navigate(R.id.predmetFragment, bundle, null, extras)
    }

    /**
     * Implementacija interface metode iz recycleradaptera
     * Poziva se kada se klikne na jednu od popup menu opcija
     */

    override fun onContextMenuClicked(predmet: Predmet, opcija: Constants.CONTEXT_MENU_OPTIONS) {

        if(opcija == Constants.CONTEXT_MENU_OPTIONS.CONTEXT_MENU_DELETE_PREDMET) {
            MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
                .setTitle("Obrišite predmet")
                .setMessage("Želite obrisati predmet: ${predmet.imePredmeta} ?")
                .setCancelable(false)
                .setIcon(R.drawable.delete_icon)
                .setPositiveButton("Obriši") { dialog, which ->
                    predmetiViewModel.deletePredmet(predmet)
                    val snackbar = Snackbar.make(binding.addPredmetBtn, "Obrisali ste predmet: ${predmet.imePredmeta}", Snackbar.LENGTH_SHORT)
                    snackbar.anchorView = binding.addPredmetBtn
                    snackbar.show()
                }
                .setNegativeButton("Odustani") { dialog, which ->
                    dialog.dismiss()
                }.show()


        } else if(opcija == Constants.CONTEXT_MENU_OPTIONS.CONTEXT_MENU_DELETE_OCJENE_PREDMETA) {

            MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
                .setTitle("Obrišite ocjene predmet")
                .setMessage("Da li ste sigurni da želite obrisati ocjene predmeta: ${predmet.imePredmeta} ?")
                .setCancelable(false)
                .setIcon(R.drawable.delete_icon)
                .setPositiveButton("Obriši") { dialog, which ->

                    if(PredmetUtilFunctions.predmetHasOcjene(predmet))
                        PredmetUtilFunctions.deleteOcjenePredmetaAndSave(predmet, predmetiViewModel)

                    val snackbar = Snackbar.make(binding.addPredmetBtn, "Obrisali ste ocjene predmeta: ${predmet.imePredmeta}", Snackbar.LENGTH_SHORT)
                    snackbar.anchorView = binding.addPredmetBtn
                    snackbar.show()
                }
                .setNegativeButton("Odustani") { dialog, which ->
                    dialog.dismiss()
                }.show()


        }



    }

    override fun onPredmetLongClicked(view: View, predmet: Predmet){

        /*var dialogBuilder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
            .setTitle("Obrišite predmet")
            .setMessage("Želite obrisati predmet: ${predmet.imePredmeta} ?")
            .setCancelable(false)
            .setIcon(R.drawable.delete_icon)
            .setPositiveButton("Obriši") { dialog, which ->
                predmetiViewModel.deletePredmet(predmet)
                val snackbar = Snackbar.make(binding.addPredmetBtn, "Obrisali ste predmet: ${predmet.imePredmeta}", Snackbar.LENGTH_SHORT)
                snackbar.anchorView = binding.addPredmetBtn
                snackbar.show()
            }
            .setNegativeButton("Odustani") { dialog, which ->
                dialog.dismiss()
            }


        dialogBuilder.show()*/
    }


}