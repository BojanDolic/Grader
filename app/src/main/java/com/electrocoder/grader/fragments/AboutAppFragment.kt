package com.electrocoder.grader.fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.electrocoder.grader.databinding.FragmentAboutAppBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AboutAppFragment : Fragment() {

    private var _binding: FragmentAboutAppBinding? = null
    private val binding get() = _binding!!

    private var interAD: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAboutAppBinding.inflate(layoutInflater)

        //interAD = InterstitialAd(requireContext()) // ca-app-pub-3940256099942544/1033173712 //ca-app-pub-3485416724873570~1286194843
        //interAD?.adUnitId = "ca-app-pub-3485416724873570/1836982897" // Pravi AD unit
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
        //interAD.loadAd(AdRequest.Builder().build())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        interAD?.show(requireActivity())

        binding.aboutVersionCard.setOnClickListener {

            if(binding.aboutVersionChangelogText.isVisible) {

                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
                binding.aboutVersionChangelogText.visibility = View.GONE

            } else {
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
                binding.aboutVersionChangelogText.visibility = View.VISIBLE

            }


        }


        binding.aboutPlayStoreCard.setOnClickListener {
            navigateToPlayStore()
        }

    }

    fun navigateToPlayStore() {

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://dev?id=8670565893894460828"))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=8670565893894460828"))
            startActivity(intent)
        }


    }

}