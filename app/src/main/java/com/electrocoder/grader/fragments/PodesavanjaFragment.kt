package com.electrocoder.grader.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.electrocoder.grader.PredmetiViewModel

import com.electrocoder.grader.R
import com.electrocoder.grader.databinding.ActivityMainBinding
import com.electrocoder.grader.databinding.FragmentPodesavanjaBinding
import com.electrocoder.grader.entities.Predmet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 */
class PodesavanjaFragment : Fragment() {

    private var _binding: FragmentPodesavanjaBinding? = null
    lateinit private var mainView: View
    lateinit private var bottomNavigation: View
    lateinit var viewModel: PredmetiViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPodesavanjaBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(this).get(PredmetiViewModel::class.java)

        mainView = inflater.inflate(R.layout.activity_main, container, false)
        bottomNavigation = mainView.findViewById(R.id.bottomNavigationView)


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Samo privremeno kreiranje dialoga
         * Kasnije prebaciti u posebnu klasu
         */

        binding.podesavanjaObrisiPredmeteBtn.setOnClickListener {
            var dialogBuilder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Obrišite sve predmete")
                .setMessage("Da li ste sigurni da želite obrisati sve predmete ?\n" +
                        "Nakon brisanja predmete nije moguće vratiti.")
                .setCancelable(false)
                .setIcon(R.drawable.delete_icon)
                .setPositiveButton("Obriši") { dialog, which ->
                    viewModel.deleteAllPredmete()
                    var snackbar = Snackbar.make(view, "Svi predmeti obrisani", Snackbar.LENGTH_SHORT)
                    snackbar.anchorView = activity?.findViewById(R.id.bottomNavigationView)
                    snackbar.show()
                }
                .setNegativeButton("Odustani") { dialog, which ->
                    dialog.dismiss()
                }

            val dialog: androidx.appcompat.app.AlertDialog = dialogBuilder.create()
            dialog.show()
        }


        /**
         * Navigacija ka fragmentima
         */

        binding.podesavanjaOAplikacijiBtn.setOnClickListener {
            findNavController().navigate(R.id.aboutAppFragment)
        }

        binding.podesavanjaPrijaviGreskuBtn.setOnClickListener {
            findNavController().navigate(R.id.prijaviGreskuFragment)
        }

        binding.podesavanjaObrisiOcjeneBtn.setOnClickListener {

            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Brisanje ocjena")
                .setMessage("Da li ste sigurni da želite obrisati ocjene svih predmeta ?\n " +
                        "Napomena: obrisane ocjene je nemoguće vratiti!")
                .setPositiveButton("OBRIŠI") { dialog, which ->
                    dialog.dismiss()
                    obrisiOcjeneSvihPredmeta()
                }
                .setNegativeButton("ODUSTANI") { dialog, which ->
                    Toast.makeText(
                        requireContext(),
                        "Otkazali ste brisanje ocjena",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.cancel()
                }
                .show()
        }


    }

    /**
     * Funkcija poziva 'clearOcjenePredmeta' sql funkciju iz DAO
     */

    fun obrisiOcjeneSvihPredmeta() {

        val predmetiViewModel = ViewModelProvider(this).get(PredmetiViewModel::class.java)

        predmetiViewModel.clearOcjenePredmeta()

        Toast.makeText(
            requireContext(),
            "Ocjene obrisane",
            Toast.LENGTH_SHORT
        ).show()


        //return Snackbar.make(binding.podesavanjaOstaloSekcija, "Ocjene obrisane", Snackbar.LENGTH_SHORT)
        //snackbar.anchorView = binding.obrisiSvePredmeteBtn
        //snackbar.show()

        /*predmetiViewModel.getAllPredmete().observe(viewLifecycleOwner, Observer {

            for(predmet in it) {
                //if(predmet.ocjene.isNotEmpty()) {
                    clearOcjene(predmet.ocjene)
                    predmetiViewModel.updatePredmet(predmet)
                //}
            }
            Snackbar.make(binding.root, "Ocjene svih predmeta su uspješno obrisane", Snackbar.LENGTH_LONG).show()
        })
        predmetiViewModel.getAllPredmete().removeObservers(viewLifecycleOwner) */// Prekida posmatranje


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
