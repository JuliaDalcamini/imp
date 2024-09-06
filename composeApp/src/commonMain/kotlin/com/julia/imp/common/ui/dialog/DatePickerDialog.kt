package com.julia.imp.common.ui.dialog

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.julia.imp.common.datetime.SelectableDatesRange
import com.julia.imp.common.datetime.selectedDate
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.cancel_label
import imp.composeapp.generated.resources.confirm_label
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    initialDate: LocalDate,
    onDismissRequest: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    minDate: LocalDate? = null
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds(),
        selectableDates = minDate?.let { SelectableDatesRange(minDate = it) }
            ?: DatePickerDefaults.AllDates
    )

    val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = confirmEnabled,
                onClick = {
                    datePickerState.selectedDate?.let {
                        onConfirm(it)
                        onDismissRequest()
                    }
                }
            ) {
                Text(stringResource(Res.string.confirm_label))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.cancel_label))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}