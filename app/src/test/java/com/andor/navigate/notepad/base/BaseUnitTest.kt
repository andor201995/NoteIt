package com.andor.navigate.notepad.base

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import com.andor.navigate.notepad.core.NoteViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito


open class BaseUnitTest {

    lateinit var noteViewModel: NoteViewModel
    private val sampleUUID = "sample_uuid_for_user_data_operations"

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initiateDependency() {
        val applicationMock = Mockito.mock(Application::class.java)
        val fireStoreMock = Mockito.mock(FirebaseFirestore::class.java)
        noteViewModel = NoteViewModel(applicationMock, sampleUUID, fireStoreMock)
    }
}

class OneTimeObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {
    private val lifecycle = LifecycleRegistry(this)

    init {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun getLifecycle(): Lifecycle = lifecycle

    override fun onChanged(t: T) {
        handler(t)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}

fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = OneTimeObserver(handler = onChangeHandler)
    observe(observer, observer)
}