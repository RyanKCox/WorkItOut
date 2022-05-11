package com.revature.workitout.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutListVMTest //: TestCase()
{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    fun <T> LiveData<T>.observeOnce(onChangeHandler: (T)->Unit){
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer,observer)
    }

    @Test
    @Throws(Exception::class)
    fun loadingTest(){

        val viewModel = WorkoutListVM()
        Assert.assertEquals(true,viewModel.bLoading.value)
//        val list = viewModel.getExerciseList()
//
//        list.observeOnce {
//            Assert.assertTrue(it.isNotEmpty())
//        }


    }
}
class OneTimeObserver<T>(private val handler:(T)->Unit): Observer<T>, LifecycleOwner {
    private val lifecycle = LifecycleRegistry(this)
    init{
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun getLifecycle(): Lifecycle = lifecycle
    override fun onChanged(t: T) {
        handler(t)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}