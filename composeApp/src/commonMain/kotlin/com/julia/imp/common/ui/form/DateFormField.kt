package com.julia.imp.common.ui.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import com.julia.imp.common.datetime.DateFormats
import com.julia.imp.common.ui.dialog.DatePickerDialog
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.calendar_month_24px
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import org.jetbrains.compose.resources.vectorResource

@Composable
fun DateFormField(
    date: LocalDate,
    label: String,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minDate: LocalDate? = null
) {
    var showPicker by remember { mutableStateOf(false) }

    Box(modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = date.format(DateFormats.DEFAULT),
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            enabled = enabled,
            label = { Text(label) },
            trailingIcon = {
                Icon(vectorResource(Res.drawable.calendar_month_24px), null)
            }
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable { showPicker = true }
                .pointerHoverIcon(PointerIcon.Text)
        )
    }

    if (showPicker) {
        DatePickerDialog(
            initialDate = date,
            onDismissRequest = { showPicker = false },
            onConfirm = { onDateSelected(it) },
            minDate = minDate
        )
    }
}