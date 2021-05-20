package com.electrocoder.grader.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.electrocoder.grader.PredmetiViewModel

import com.electrocoder.grader.R
import com.electrocoder.grader.databinding.FragmentProfilBinding
import com.electrocoder.grader.databinding.PredmetiFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null
    val binding get() = _binding!!

    ////////////////////////////////////////////////////////
    lateinit var viewModel: PredmetiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(PredmetiViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var sumaOcjena = 0
        var brojPredmeta = 0

        var prosjek = 0.0f

        viewModel.getAllPredmete().observe(viewLifecycleOwner, Observer {
            /*it.forEach {
                sumaOcjena += it.prosjek.roundToInt()
                if(it.prosjek > 0.0)
                    brojPredmeta++;
            }


            Log.d(TAG, "onViewCreated: $sumaOcjena \n $brojPredmeta")

            var prosjek = 0.0f
            if(brojPredmeta > 0) {
                prosjek = sumaOcjena / brojPredmeta.toFloat()
            }*/

            lifecycleScope.launch {

                prosjek = viewModel.getProsjekPredmeta()

                withContext(Dispatchers.Main) {
                    binding.profilProsjekOcjena.text = String.format("%.2f", prosjek)
                }

            }


        })




    }

}
