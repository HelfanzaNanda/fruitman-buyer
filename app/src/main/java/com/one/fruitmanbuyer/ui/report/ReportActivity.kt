package com.one.fruitmanbuyer.ui.report

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.models.Report
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.extensions.gone
import com.one.fruitmanbuyer.utils.extensions.showToast
import com.one.fruitmanbuyer.utils.extensions.visible
import kotlinx.android.synthetic.main.content_report.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class ReportActivity : AppCompatActivity() {

    private val reportViewModel : ReportViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chart"
        observe()
    }

    private fun observe() {
        observeState()
        observeReports()
    }

    private fun observeState() = reportViewModel.listenToState().observer(this, Observer {
        handleUiState(
            it
        )
    })
    private fun observeReports() = reportViewModel.listenToReports().observe(this, Observer {
        handleReports(
            it
        )
    })

    private fun handleReports(list: List<Report>?) {
        list?.let { reports ->
            val lineDataSetCharts = mutableListOf<LineDataSet>()
            reports.map {
                val charts = ArrayList<Entry>()
                it.items.map { reportItems ->
                    charts.add(
                        Entry(
                            (reportItems.month + "F").toFloat(),
                            (reportItems.value + "F").toFloat()
                        )
                    )
                }
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                val lineDataSet = LineDataSet(charts, it.fruit)
                lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                lineDataSet.color = color
                lineDataSet.circleRadius = 5f
                lineDataSet.setCircleColor(color)
                lineDataSetCharts.add(lineDataSet)
            }

            val legend = chart.legend
            legend.isEnabled = true
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.setDrawInside(false)

            chart.description.isEnabled = false
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.data = LineData(lineDataSetCharts as List<ILineDataSet>?)
            chart.animateXY(100, 500)

            val months = ArrayList<String>()
            months.add("Januari")
            months.add("Februari")
            months.add("Maret")
            months.add("April")
            months.add("Mei")
            months.add("Juni")
            months.add("Juli")
            months.add("Agustus")
            months.add("September")
            months.add("Oktober")
            months.add("November")
            months.add("Desember")
            val month = AxisDateFormatter(months.toArray(arrayOfNulls<String>(months.size)))
            chart.xAxis?.setValueFormatter(month);

        }
    }

    private fun handleUiState(reportState: ReportState?) {
        reportState?.let {
            when(it){
                is ReportState.Loading -> handleLoading(it.state)
                is ReportState.ShowToast -> showToast(it.message)
            }
        }
    }

    private fun handleLoading(b: Boolean) {
        if (b) loading.visible() else loading.gone()
    }

    private fun getReport() = reportViewModel.report(Constants.getToken(this@ReportActivity))

    override fun onResume() {
        super.onResume()
        getReport()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}