package com.electrocoder.grader.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.electrocoder.grader.PinInputView
import com.electrocoder.grader.PredmetiViewModel

import com.electrocoder.grader.R
import com.electrocoder.grader.databinding.AddpredmetFragmentBinding
import com.electrocoder.grader.entities.Ocjena
import com.electrocoder.grader.entities.Predmet
import com.electrocoder.grader.entities.PredmetWithOcjena
import com.electrocoder.grader.util.PredmetUtilFunctions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.textfield.TextInputEditText
import org.jetbrains.annotations.TestOnly

/**
 * A simple [Fragment] subclass.
 */
class AddPredmetFragment : Fragment() {

    lateinit var _binding: AddpredmetFragmentBinding

    val binding get() = _binding

    lateinit var viewModel: PredmetiViewModel

    private var interAD: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = AddpredmetFragmentBinding.inflate(inflater, container, false)
        var view = binding.root

        viewModel = ViewModelProvider(this).get(PredmetiViewModel::class.java)

        InterstitialAd.load(
            requireContext(),
            "ca-app-pub-3485416724873570/1836982897",
            AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(p0: InterstitialAd) {
                    interAD = p0
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    interAD = null
                }
            })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.addPredmetFragment, true)
            .setExitAnim(R.anim.fade_out)
            .setEnterAnim(R.anim.fade_in)
            .build()


        binding.dodajPredmetBtn.setOnClickListener{

            var imePredmetaText = binding.imePredmetaTextInput.text.toString()
            if(imePredmetaText.length < 4 || imePredmetaText.isBlank() || imePredmetaText.length > 26) {
                Toast.makeText(context, "Morate unijeti ime predmeta (min 4 slova, max 26)", Toast.LENGTH_SHORT).show()

            } else {

                if(PredmetUtilFunctions.isPredmetNameAvailable(viewModel, imePredmetaText)) {
                    if(binding.ocjeneInput.text?.isBlank()!!) {
                        var predmet = Predmet()
                        predmet.imePredmeta = imePredmetaText
                        viewModel.insert(predmet)

                        interAD?.show(requireActivity())
                        findNavController().navigateUp()
                    } else {
                        var predmetWithOcjene = PredmetWithOcjena()
                        predmetWithOcjene.predmet.imePredmeta = imePredmetaText
                        predmetWithOcjene.ocjene = parseOcjeneFromInput(binding.ocjeneInput)
                        /*predmet.imePredmeta = imePredmetaText
                        predmet.ocjene = parseOcjeneFromInput(binding.ocjeneInput)*/
                        viewModel.insertPredmet(predmetWithOcjene)

                        interAD?.show(requireActivity())
                        findNavController().navigateUp()
                    }
                } else Toast.makeText(requireContext(), "Već postoji predmet sa tim imenom !", Toast.LENGTH_SHORT).show()

            }

        }

    }

    fun parseOcjeneFromInput(ocjeneInput: EditText): ArrayList<Ocjena> {

        val charArr = ocjeneInput.text.toString().toCharArray()
        val ocjeneArray = ArrayList<Ocjena>()

        charArr.forEachIndexed { index, c ->
            ocjeneArray.add(
                Ocjena(ocjena = c.toString().toInt()))
            Log.d("TAG", "parseOcjeneFromInput: OCJENA ${ocjeneArray[index].ocjena}")
        }


        return ocjeneArray
    }


}
