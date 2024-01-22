package com.example.alertpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alertpage.models.Survey

class MainViewModel : ViewModel() {
    var TAG = MainViewModel::class.java.simpleName
    private var mDisplayedList = MutableLiveData<ArrayList<Survey>>()
    private var mCompleteList = MutableLiveData<ArrayList<Survey>>()

    private fun getUserName(): String {
        val list = listOf("Goofy", "Daisy", "Mickey", "Minnie")
        val randomIndex = (list.indices).random()
        return list[randomIndex]
    }

    private fun getActionName(): String {
        val list = listOf("completed", "started", "abandoned", "cancelled")
        val randomIndex = (list.indices).random()
        return list[randomIndex]
    }

    private fun getSurveyName(): String {
        val list = listOf("FC Survey", "CD Survey", "RB Survey", "India Survey")
        val randomIndex = (list.indices).random()
        return list[randomIndex]
    }

    fun initDisplayWithDummyValues() {
        var localList = ArrayList<Survey>()
        var survey = Survey(getSurveyName(), getActionName(), getUserName(), true)
        localList.add(survey)
        for (i in 1..5) {
            survey = Survey(getSurveyName(), getActionName(), getUserName())
            // mDisplayedList.value?.add(survey)
            localList.add(survey)
        }
        survey = Survey(getSurveyName(), getActionName(), getUserName(), true)
        localList.add(survey)
        Log.d(TAG, "initDisplayWithDummy")
        mDisplayedList.value = localList
        mCompleteList.value = localList
        Log.d(TAG, "${mDisplayedList.value!!.size} mDisp size isInitialised")
    }

    fun getDisplayedList(): MutableLiveData<ArrayList<Survey>> {
        return mDisplayedList
    }

    fun filterList(type: Int): LiveData<ArrayList<Survey>> {
        Log.d(TAG, "filterList")

        if (!mCompleteList.value.isNullOrEmpty()) {
            var list: ArrayList<Survey> = mCompleteList.value!!
            var localList = ArrayList<Survey>()
            when (type) {
                1 -> {//read
                    localList = ArrayList<Survey>()
                    for (i in list) {
                        if (i.readFlag) {
                            localList.add(i)
                        }
                    }
                }

                2 -> {//unread
                    localList = ArrayList<Survey>()
                    for (i in list) {
                        if (!i.readFlag) {
                            localList.add(i)
                        }
                    }
                }

                else -> {
                    localList = list
                }
            }
            mDisplayedList.value = localList
            Log.d(TAG, " ${mDisplayedList.value!!.size} size of filtered lost")
            Log.d(TAG, " ${localList.size} size of filtered lost")
            return MutableLiveData(localList)
        }
        return mDisplayedList
    }

}