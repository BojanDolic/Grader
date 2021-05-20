package com.electrocoder.grader.fragments

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.AutoTransition
import androidx.work.*
import com.electrocoder.grader.PredmetiViewModel
import com.electrocoder.grader.R
import com.electrocoder.grader.adapters.OcjenePredmetaRecyclerAdapter
import com.electrocoder.grader.databinding.FragmentPredmetBinding
import com.electrocoder.grader.databinding.PredmetiFragmentBinding
import com.electrocoder.grader.entities.Ocjena
import com.electrocoder.grader.entities.Predmet
import com.electrocoder.grader.entities.PredmetWithOcjena
import com.electrocoder.grader.util.Constants
import com.electrocoder.grader.util.PodsjetnikTestWorker
import com.electrocoder.grader.util.PredmetUtilFunctions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_predmet.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "PredmetFragment"

class PredmetFragment : Fragment(), DatePickerDialog.OnDateSetListener {


    private var _binding: FragmentPredmetBinding? = null
    private val binding get() = _binding!!

    var predmet: PredmetWithOcjena? = null
    var idPredmeta: Long = 0

    private lateinit var predmetiViewModel: PredmetiViewModel
    private lateinit var ocjeneAdapter: OcjenePredmetaRecyclerAdapter

    private  var interAD: InterstitialAd? = null

    private val args: PredmetFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        predmet = args.izabranPredmet
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        Log.d("ID", "onCreate: $idPredmeta")
        //sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPredmetBinding.inflate(inflater, container, false)

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

        //idPredmeta = PredmetFragmentArgs.fromBundle(requireArguments()).izabranPredmet

        idPredmeta = args.idPredmeta

        val pozicijaPredmeta = PredmetFragmentArgs.fromBundle(requireArguments()).pozicija
        binding.predmetProsjekOcjena.transitionName = "prosjekText_$pozicijaPredmeta"
        binding.speedDialFab.transitionName = "addPredmetAndOcjenuFab"

        predmetiViewModel = ViewModelProvider(this).get(PredmetiViewModel::class.java)

        /** Podešavanje adaptera i layout managera i prikaz ocjena preko recyclerview-a **/
        ocjeneAdapter = OcjenePredmetaRecyclerAdapter()
        binding.predmetOcjeneRecycler.layoutManager = GridLayoutManager(context, 3)
        binding.predmetOcjeneRecycler.adapter = ocjeneAdapter

        /*predmet?.let { predmet ->

            ocjeneAdapter.updateList(predmet.ocjene)

            binding.predmetProsjekOcjena.text = String.format("%.2f", predmet.prosjek)

            predmet?.predmet.zakazanTest.observe(viewLifecycleOwner, Observer {

                if(it) {
                    TransitionManager.beginDelayedTransition(binding.root, android.transition.AutoTransition())
                    binding.izbrisiPodsjetnikBtn.visibility = View.VISIBLE
                } else {
                    TransitionManager.beginDelayedTransition(binding.root, android.transition.AutoTransition())
                    binding.izbrisiPodsjetnikBtn.visibility = View.GONE
                }

            })

            *//**
             * Ako je aplikacija aktivna gledamo stanje workmanagera
             * Ako u tom trenutku stigne zadnja notifikacija, automatski setujemo zakazan test predmeta na false
             *//*

            WorkManager.getInstance(requireContext()).getWorkInfosByTagLiveData(predmet.predmet.imePredmeta + "1").observe(viewLifecycleOwner, Observer {

                if(it.size > 0)
                    if(it.get(0).state.isFinished) {
                        predmet.predmet.zakazanTest.value = false
                        predmetiViewModel.updatePredmet(predmet.predmet)
                    }

            })





        }*/



        binding.izbrisiPodsjetnikBtn.setOnClickListener { cancelPodsjetnikTesta() }

        predmetiViewModel.getPredmet(idPredmeta).observe(viewLifecycleOwner, { predmet ->

            

            predmet?.let {

                binding.predmetProsjekOcjena.text = String.format("%.2f", PredmetUtilFunctions.calculateProsjek(predmet.ocjene))

                ocjeneAdapter.updateList(predmet.ocjene)


                addFabButtons(predmet)



            } ?: run {
                // If predmet is null we show message to the user and exit the fragment
                Toast.makeText(requireContext(), "Problem prilikom učitavanja predmeta!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        })

        /*predmetiViewModel.getPredmet(idPredmeta).observe(viewLifecycleOwner, { predmetWithOcjene ->

            predmetWithOcjene?.let {
                predmet = it
                ocjeneAdapter.updateList(it.ocjene)



                predmet?.predmet.zakazanTest.observe(viewLifecycleOwner, Observer {

                    if(it) {
                        TransitionManager.beginDelayedTransition(binding.root, android.transition.AutoTransition())
                        binding.izbrisiPodsjetnikBtn.visibility = View.VISIBLE
                    } else {
                        TransitionManager.beginDelayedTransition(binding.root, android.transition.AutoTransition())
                        binding.izbrisiPodsjetnikBtn.visibility = View.GONE
                    }

                })
            }

        })*/

        /*predmet = predmetiViewModel.getPredmet(idPredmeta)
        ocjeneAdapter.updateList(predmet.ocjene)*/




        binding.expandOcjeneContainerBtn.setOnClickListener {

            if(!binding.predmetOcjeneRecycler.isVisible) {

                TransitionManager.beginDelayedTransition(binding.root, android.transition.AutoTransition())

                binding.predmetOcjeneRecycler.visibility = View.VISIBLE
                binding.expandOcjeneContainerBtn.rotationX = 180f
            } else {
                TransitionManager.beginDelayedTransition(binding.root, android.transition.AutoTransition())
                binding.predmetOcjeneRecycler.visibility = View.GONE
                binding.expandOcjeneContainerBtn.rotationX = 0f
            }



        }





        /*binding.dodajOcjenuPredmetaBtn.setOnClickListener({
            openAddOcjenuDialog()
        })*/

    }

    private fun addFabButtons(predmet: PredmetWithOcjena) {
        binding.speedDialFab.addActionItem(
            SpeedDialActionItem.Builder(R.id.dodajOcjenuFab, R.drawable.icon_add)
                .setFabBackgroundColor(resources.getColor(R.color.green))
                .setLabel("Dodaj ocjenu")
                .setFabImageTintColor(Color.parseColor("#ffffff"))
                .create()
        )
        binding.speedDialFab.addActionItem(
            SpeedDialActionItem.Builder(R.id.dodajPodsjetnikFab, R.drawable.alert_icon)
                .setFabBackgroundColor(resources.getColor(R.color.purple))
                .setLabel("Dodaj podsjetnik")
                .setFabImageTintColor(Color.parseColor("#ffffff"))
                .create()
        )

        binding.speedDialFab.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener {
            when (it.id) {
                R.id.dodajOcjenuFab -> {
                    openAddOcjenuDialog()
                    speedDialFab.close()
                    return@OnActionSelectedListener true
                }
                R.id.dodajPodsjetnikFab -> {
                    speedDialFab.close(true)
                    if(!predmet.predmet.zakazanTest.value!!)
                        openDatePickerDialog()
                    return@OnActionSelectedListener true
                }

            }
            false
        })
    }

    fun openDatePickerDialog() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), this, year, month, day)
        datePicker.show()

    }

    fun cancelPodsjetnikTesta() {

        WorkManager.getInstance(requireContext()).cancelAllWorkByTag(predmet?.predmet?.imePredmeta + "1")
        WorkManager.getInstance(requireContext()).cancelAllWorkByTag(predmet?.predmet?.imePredmeta + "7")

        predmet?.predmet?.zakazanTest?.value = false
        predmetiViewModel.updatePredmet(predmet?.predmet!!)

        val snackbar2 = Snackbar.make(binding.speedDialFab, "Test otkazan", Snackbar.LENGTH_SHORT)
        snackbar2.anchorView = binding.speedDialFab
        snackbar2.show()

    }

    fun openAddOcjenuDialog() {

        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.input_dialog_layout, null)
        var ocjenaInput = customView.findViewById(R.id.dialogOcjenaInput) as TextInputEditText
        var ocjenaInputLayout = customView.findViewById(R.id.dialogOcjenaInputLayout) as TextInputLayout

        MaterialAlertDialogBuilder(requireContext())
            .setView(customView)
            .setTitle("Dodajte ocjenu")
            .setPositiveButton("Dodaj", DialogInterface.OnClickListener { dialog, which ->

                val text = ocjenaInput.text.toString().trim()

                if(!text.isBlank() && !text.isEmpty()) {
                    val ocjenaValue = ocjenaInput.text.toString().toInt()

                    interAD?.show(requireActivity())

                    val ocjena = Ocjena(ocjena = ocjenaValue, datumOcjene = Date(), predmetId = predmet?.predmet?.id!!)

                    Log.d("TAG", "openAddOcjenuDialog: DEBUG OCJENA $ocjena")

                    predmetiViewModel.insertOcjena(ocjena)

                    /*predmet?.ocjene?.add(ocjena)

                    predmetiViewModel.updateOcjenePredmeta(predmet!!)
                    ocjeneAdapter.updateList(predmet?.ocjene!!)*/
                    //binding.predmetProsjekOcjena.text = String.format("%.2f", predmet.calculateProsjekPredmeta(predmet.ocjene))

                    val snackbar = Snackbar.make(binding.speedDialFab, "Dodali ste ocjenu $ocjena", Snackbar.LENGTH_SHORT)
                    snackbar.anchorView = binding.speedDialFab
                    snackbar.show()
                } else {
                    ocjenaInputLayout.error = "Morate unijeti ocjenu"
                }

            })
            .setNegativeButton("Zatvori", DialogInterface.OnClickListener { dialog, which ->  })
            .show()

    }

    /**
     * Funkcija koja se poziva nakon biranja datuma
     * Nalazi se sva logika vezana za provjeru datuma, kalkulacije i postavljanje WorkManagera
     */

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        predmet?.let { predmet ->


        if(!predmet?.predmet?.zakazanTest?.value!!) {

            val calendar = Calendar.getInstance()
            val currentTime = calendar.timeInMillis
            val selectedTime = calendar.set(year, month, dayOfMonth)
            val time2 = calendar.timeInMillis


            /**
             * Kalkulacija datuma
             * Prva notifikacija treba da se pojavi 7 dana prije testa
             * Druga notifikacija 1 dan prije testa
             */

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val danasnjiDatum = Date().time
            val izabraniDatum = calendar.time.time


            val danaDoTesta = (izabraniDatum - danasnjiDatum) / (1000 * 60 * 60 * 24)

            val prviPodsjetnikDana = danaDoTesta - 7
            val drugiPodsjetnikDana = danaDoTesta - 1


            /**
             * Izabran datum
             */
            if(danasnjiDatum < izabraniDatum) {

                if(danaDoTesta >= 7) {

                    val inputData: Data = Data.Builder()
                        .putString(Constants.IME_PREDMETA_TAG, predmet?.predmet?.imePredmeta)
                        .putInt(Constants.PODSJETNIK_TESTA_TEXT, Constants.PRVI_PODSJETNIK_TESTA_ID)
                        .build()

                    val workRequest: WorkRequest = OneTimeWorkRequestBuilder<PodsjetnikTestWorker>()
                        .addTag(predmet?.predmet?.imePredmeta + "7")
                        .setInputData(inputData)
                        .setInitialDelay(prviPodsjetnikDana, TimeUnit.DAYS)
                        .build()

                    WorkManager.getInstance(requireContext()).enqueue(workRequest)


                }

                val inputData: Data = Data.Builder()
                    .putString(Constants.IME_PREDMETA_TAG, predmet?.predmet?.imePredmeta)
                    .putInt(Constants.PODSJETNIK_TESTA_TEXT, Constants.DRUGI_PODSJETNIK_TESTA_ID)
                    .build()

                val workRequest: WorkRequest = OneTimeWorkRequestBuilder<PodsjetnikTestWorker>()
                    .addTag(predmet?.predmet?.imePredmeta + "1")
                    .setInputData(inputData)
                    .setInitialDelay(drugiPodsjetnikDana, TimeUnit.DAYS)
                    .build()

                WorkManager.getInstance(requireContext()).enqueue(workRequest)


                predmet?.predmet?.zakazanTest?.value = true

                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)

                val dateFormat2 = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.GERMANY)

                val snackbar = Snackbar.make(binding.root, "Test zakazan za ${dateFormat2.format(Date(year - 1900, month, dayOfMonth))}", Snackbar.LENGTH_LONG)
                snackbar.anchorView = binding.speedDialFab
                snackbar.setActionTextColor(resources.getColor(R.color.colorSecondary, requireContext().theme))
                snackbar.setAction("OTKAŽI", View.OnClickListener {

                    WorkManager.getInstance(requireContext()).cancelAllWorkByTag(predmet?.predmet?.imePredmeta + "1")
                    WorkManager.getInstance(requireContext()).cancelAllWorkByTag(predmet?.predmet?.imePredmeta + "7")
                    predmet?.predmet?.zakazanTest?.value = false

                    val snackbar2 = Snackbar.make(binding.speedDialFab, "Test otkazan", Snackbar.LENGTH_SHORT)
                    snackbar2.anchorView = binding.speedDialFab
                    snackbar2.show()

                })
                snackbar.show()

                predmetiViewModel.updatePredmet(predmet.predmet)


            } else return Toast.makeText(requireContext(), "Pogrešan datum", Toast.LENGTH_SHORT).show()


        } else return Toast.makeText(requireContext(), "Već ste zakazali test", Toast.LENGTH_SHORT).show()

        }

    }

}