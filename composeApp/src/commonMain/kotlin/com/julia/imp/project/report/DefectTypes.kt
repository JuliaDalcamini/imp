package com.julia.imp.project.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.julia.imp.common.datetime.sumOfDuration
import com.julia.imp.common.report.LabeledPieChart
import com.julia.imp.common.report.Table
import com.julia.imp.common.report.TableCell
import com.julia.imp.common.report.TableHeader
import com.julia.imp.common.report.TableRow
import com.julia.imp.common.text.formatAsCurrency
import com.julia.imp.project.dashboard.data.DashboardData
import com.julia.imp.project.dashboard.data.DefectTypeSummary
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.average_cost_label
import imp.composeapp.generated.resources.average_effort_label
import imp.composeapp.generated.resources.defect_type_label
import imp.composeapp.generated.resources.defect_types_title
import imp.composeapp.generated.resources.no_data_available
import imp.composeapp.generated.resources.number_of_defects_label
import imp.composeapp.generated.resources.quantity_with_percentage_format
import imp.composeapp.generated.resources.total_label
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

private const val EMPTY_CELL_TEXT = "-"

@Composable
fun DefectTypesPage(
    data: DashboardData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ProjectReportHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            pageName = stringResource(Res.string.defect_types_title)
        )

        if (data.defectTypes.isNotEmpty()) {
            DefectTypesTable(
                modifier = Modifier.fillMaxWidth(),
                data = data
            )

            DefectTypesCharts(
                modifier = Modifier.fillMaxWidth().height(220.dp),
                data = data.defectTypes
            )
        } else {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.no_data_available),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DefectTypesTable(
    data: DashboardData,
    modifier: Modifier = Modifier
) {
    Table(modifier) {
        TableRow {
            TableHeader(stringResource(Res.string.defect_type_label))

            TableHeader(
                modifier = Modifier.weight(.5f),
                text = stringResource(Res.string.number_of_defects_label)
            )

            TableHeader(
                modifier = Modifier.weight(.5f),
                text = stringResource(Res.string.average_effort_label)
            )

            TableHeader(
                modifier = Modifier.weight(.5f),
                text = stringResource(Res.string.average_cost_label)
            )
        }

        data.defectTypes.forEach { summary ->
            val percentage = (summary.percentage * 100).toInt()

            TableRow {
                TableCell(summary.defectType.name)

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = stringResource(
                        Res.string.quantity_with_percentage_format,
                        summary.count,
                        percentage
                    )
                )

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = summary.averageEffort.inWholeSeconds.seconds.toString()
                )

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = summary.averageCost.formatAsCurrency()
                )
            }
        }

        TableRow {
            TableCell(
                text = stringResource(Res.string.total_label),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TableCell(
                modifier = Modifier.weight(.5f),
                textAlign = TextAlign.End,
                text = data.defectsProgress.total.toString(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TableCell(
                modifier = Modifier.weight(.5f),
                textAlign = TextAlign.End,
                text = EMPTY_CELL_TEXT,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TableCell(
                modifier = Modifier.weight(.5f),
                textAlign = TextAlign.End,
                text = EMPTY_CELL_TEXT,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun DefectTypesCharts(
    data: List<DefectTypeSummary>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val defectsData = data.filter { it.count > 0 }
        val totalDefects = defectsData.sumOf { it.count }
        val defectPercentages = defectsData.map { it.count.toFloat() / totalDefects }

        if (totalDefects > 0) {
            LabeledPieChart(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.number_of_defects_label),
                values = defectPercentages,
                label = { index ->
                    val summary = defectsData[index]

                    Text(summary.defectType.name)
                    Text(summary.count.toString())
                }
            )
        }

        val effortData = data.filter { it.averageEffort.isPositive() }
        val totalEffort = effortData.sumOfDuration { it.averageEffort }
        val effortPercentages = effortData.map { (it.averageEffort / totalEffort).toFloat() }

        if (totalEffort.isPositive()) {
            LabeledPieChart(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.average_effort_label),
                values = effortPercentages,
                label = { index ->
                    val summary = effortData[index]

                    Text(summary.defectType.name)
                    Text(summary.averageEffort.inWholeSeconds.seconds.toString())
                }
            )
        }

        val costData = data.filter { it.averageCost > 0 }
        val totalCost = costData.sumOf { it.averageCost }
        val costPercentages = costData.map { (it.averageCost / totalCost).toFloat() }

        if (totalCost > 0) {
            LabeledPieChart(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.average_cost_label),
                values = costPercentages,
                label = { index ->
                    val summary = costData[index]

                    Text(summary.defectType.name)
                    Text(summary.averageCost.formatAsCurrency())
                }
            )
        }
    }
}