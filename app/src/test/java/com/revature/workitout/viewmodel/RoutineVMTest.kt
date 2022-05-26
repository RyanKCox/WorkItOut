package com.revature.workitout.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.revature.workitout.model.data.Routine
import com.revature.workitout.model.repo.fakeRoutineRepo
import com.revature.workitout.model.room.entity.RoutineEntity
import com.revature.workitout.viewmodel.providers.CoroutineProvider
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
//@RunWith(AndroidJUnit4::class)
class RoutineVMTest : TestCase() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = StandardTestDispatcher()

    val fakeRepo = fakeRoutineRepo()
    val viewModel = RoutineVM(fakeRepo, CoroutineProvider(
        main = testCoroutineDispatcher,
        io = testCoroutineDispatcher
    ))

    @Test
    fun testDeleteRoutine() {

        kotlinx.coroutines.test.runTest(testCoroutineDispatcher) {
            viewModel.selectedRoutine.value =
                Routine(
                    routineEntity = RoutineEntity(0, "test"),
                    exercises = mutableListOf()
                )

            fakeRepo.list.add(viewModel.selectedRoutine.value!!)

            viewModel.deleteRoutine()

        }
        assertTrue(fakeRepo.list.isEmpty())


    }

    @Test
    fun testCreateRoutine() {
        kotlinx.coroutines.test.runTest(testCoroutineDispatcher) {
            fakeRepo.list.clear()

            viewModel.createRoutine("CreateTest")

        }
        assertTrue(fakeRepo.list.isNotEmpty())
        assertEquals("CreateTest",fakeRepo.list.first().routineEntity.name)


    }
}