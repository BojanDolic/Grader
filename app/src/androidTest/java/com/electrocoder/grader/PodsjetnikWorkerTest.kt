package com.electrocoder.grader

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.testing.TestDriver
import androidx.work.testing.TestWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import com.electrocoder.grader.util.Constants
import com.electrocoder.grader.util.PodsjetnikTestWorker
import com.electrocoder.grader.util.PodsjetnikWorkerUtil
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class PodsjetnikWorkerTest {

    private lateinit var context: Context
    private lateinit var executor: Executor
    private lateinit var testDriver: TestDriver


    @Before
    fun init() {
        context = ApplicationProvider.getApplicationContext()
        executor = Executors.newSingleThreadExecutor()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        testDriver = WorkManagerTestInitHelper.getTestDriver(context)!!
    }

    @Test
    fun testPodsjetnikWorker_first() {

        val worker = TestWorkerBuilder<PodsjetnikTestWorker>(
            context,
            executor,
            inputData = workDataOf(Constants.PODSJETNIK_TESTA_TEXT to Constants.PRVI_PODSJETNIK_TESTA_ID)
        ).build()

        val result = worker.doWork()
        assertThat(result, `is`(ListenableWorker.Result.success()))

    }

    @Test
    fun testPodsjetnikWorker_second() {

        val workManager = WorkManager.getInstance(context)

        val worker = TestWorkerBuilder<PodsjetnikTestWorker>(
            context,
            executor,
            inputData = workDataOf(
                Constants.PODSJETNIK_TESTA_TEXT to Constants.DRUGI_PODSJETNIK_TESTA_ID,
            Constants.IME_PREDMETA_TAG to "TestPredmet")
        ).build()

        val result = worker.doWork()


        assertThat(result, `is`(ListenableWorker.Result.success()))

    }

    @Test
    fun checkWorkerCondition_delay_shouldBeRunning() {

        val request = OneTimeWorkRequestBuilder<PodsjetnikTestWorker>()
            .addTag("TestPredmetaTag")
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(workDataOf(Constants.PODSJETNIK_TESTA_TEXT to Constants.DRUGI_PODSJETNIK_TESTA_ID,
                Constants.IME_PREDMETA_TAG to "TestPredmet"))
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(request).result.get()

        // Provjerava da li je work manager u running ili pokrenutom stanju
        assertThat(PodsjetnikWorkerUtil.checkIsWorkScheduled(context, "TestPredmetaTag"), `is`(true))
    }

    @Test
    fun checkWorkerCondition_delay_shouldBeStopped() {

        val request = OneTimeWorkRequestBuilder<PodsjetnikTestWorker>()
            .addTag("TestPredmetaTag")
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(workDataOf(Constants.PODSJETNIK_TESTA_TEXT to Constants.DRUGI_PODSJETNIK_TESTA_ID,
                Constants.IME_PREDMETA_TAG to "TestPredmet"))
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(request)

        // Pomoću test drivera automatski pokrećemo worker nakon nekog vremena (delay) kako ne bismo čekali tokom testa
        testDriver.setInitialDelayMet(request.id)
        assertThat(PodsjetnikWorkerUtil.checkIsWorkScheduled(context, "TestPredmetaTag"), `is`(false))


    }

}