package com.electrocoder.grader.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.electrocoder.grader.databinding.FragmentPrijaviGreskuBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class PrijaviGreskuFragment : Fragment() {

    private var _binding: FragmentPrijaviGreskuBinding? = null
    private val binding get() = _binding!!

    private var interAD: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPrijaviGreskuBinding.inflate(inflater)

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

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bugReportSendBtn.setOnClickListener {

            interAD?.show(requireActivity())

            interAD?.fullScreenContentCallback = object : FullScreenContentCallback() {

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    sendBugReport()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    sendBugReport()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }
            }


        }

        binding.bugReportInput.doOnTextChanged { text, start, before, count ->

            binding.bugReportSendBtn.isEnabled =
                text != null && text.length > 20 && text.isNotEmpty()

        }


    }

    fun sendBugReport() {
        val subject = "Grader | Bugovi"

        val poruka = binding.bugReportInput.text.toString() + "\n\n " +
                "Uređaj: ${Build.DEVICE}\n " +
                "Proizvođač: ${Build.MANUFACTURER}\n " +
                "Model: ${Build.MODEL}\n " +
                "Android: ${Build.VERSION.RELEASE}"


        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf("dolicbojan2@gmail.com")
        ) // Kome se šalje email
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, poruka)

        intent.setType("message/rfc822")

        startActivity(Intent.createChooser(intent, "Izaberite aplikaciju"))
    }

}