package com.one.fruitmanbuyer.ui.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Report
import com.one.fruitmanbuyer.repositories.ReportRepository
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.utils.SingleLiveEvent

class ReportViewModel(private val reportRepository: ReportRepository) : ViewModel() {

    private val state : SingleLiveEvent<ReportState> = SingleLiveEvent()
    private val reports = MutableLiveData<List<Report>>()

    private fun isLoading(b : Boolean){ state.value = ReportState.Loading(b) }
    private fun toast(m : String){ state.value = ReportState.ShowToast(m) }

    fun report(token : String){
        isLoading(true)
        reportRepository.report(token, object : ArrayResponse<Report>{
            override fun onSuccess(datas: List<Report>?) {
                isLoading(false)
                datas?.let { reports.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToReports() = reports

}

sealed class ReportState{
    data class Loading(var state : Boolean): ReportState()
    data class ShowToast(var message : String): ReportState()
}