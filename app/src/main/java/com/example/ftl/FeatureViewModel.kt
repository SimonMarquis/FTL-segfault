package com.example.ftl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FeatureViewModel @Inject constructor(uuid: UUID) : ViewModel() {
    val state: LiveData<State> = MutableLiveData(State(uuid))
}
