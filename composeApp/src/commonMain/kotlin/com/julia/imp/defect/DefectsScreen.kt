package com.julia.imp.defect

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.artifact.Artifact
import com.julia.imp.common.ui.dialog.ConfirmationDialog
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.topbar.TopBar
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.action_error_message
import imp.composeapp.generated.resources.action_error_title
import imp.composeapp.generated.resources.check_24px
import imp.composeapp.generated.resources.check_circle_20px
import imp.composeapp.generated.resources.defects_title
import imp.composeapp.generated.resources.description_format
import imp.composeapp.generated.resources.filter_all
import imp.composeapp.generated.resources.filter_fixed
import imp.composeapp.generated.resources.filter_not_fixed
import imp.composeapp.generated.resources.fix_defect_message
import imp.composeapp.generated.resources.fix_defect_title
import imp.composeapp.generated.resources.fixed_label
import imp.composeapp.generated.resources.load_error_message
import imp.composeapp.generated.resources.no_defects_artifact_message
import imp.composeapp.generated.resources.no_description
import imp.composeapp.generated.resources.question_format
import imp.composeapp.generated.resources.refresh_24px
import imp.composeapp.generated.resources.try_again_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun DefectsScreen(
    artifact: Artifact,
    onBackClick: () -> Unit,
    viewModel: DefectsViewModel = viewModel { DefectsViewModel() }
) {
    LaunchedEffect(artifact) {
        viewModel.initialize(artifact)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.defects_title),
                subtitle = artifact.name,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            if (viewModel.uiState.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(paddingValues)
                )
            }

            DefectList(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues),
                entries = viewModel.uiState.defects,
                selectedFilter = viewModel.uiState.filter,
                onFilterChange = { viewModel.setFilter(it) },
                showFixButton = viewModel.uiState.showFixButton,
                onFixClick = { viewModel.askToFix(it) },
                contentPadding = paddingValues
            )

            if (viewModel.uiState.defects?.isEmpty() == true) {
                Text(
                    modifier = Modifier.padding(24.dp).align(Alignment.Center),
                    text = stringResource(Res.string.no_defects_artifact_message)
                )
            }

            if (viewModel.uiState.error) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(paddingValues)
                        .padding(paddingValues)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.load_error_message),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )

                    TextButton(
                        modifier = Modifier.padding(top = 4.dp),
                        onClick = { viewModel.getDefects() }
                    ) {
                        Icon(vectorResource(Res.drawable.refresh_24px), null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(Res.string.try_again_label))
                    }
                }
            }
        }

        viewModel.uiState.defectToFix?.let { defect ->
            FixDefectDialog(
                onDismissRequest = { viewModel.dismissFixDialog() },
                onConfirm = { viewModel.fixDefect(defect) }
            )
        }

        if (viewModel.uiState.actionError) {
            ErrorDialog(
                title = stringResource(Res.string.action_error_title),
                message = stringResource(Res.string.action_error_message),
                onDismissRequest = { viewModel.dismissActionError() }
            )
        }
    }
}

@Composable
private fun FixDefectDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    ConfirmationDialog(
        title = stringResource(Res.string.fix_defect_title),
        message = stringResource(Res.string.fix_defect_message),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DefectList(
    entries: List<Defect>?,
    selectedFilter: DefectFilter,
    onFilterChange: (DefectFilter) -> Unit,
    showFixButton: Boolean,
    onFixClick: (Defect) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        stickyHeader("filters") {
            DefectListFilters(
                modifier = Modifier.fillMaxWidth(),
                selectedFilter = selectedFilter,
                onFilterChange = onFilterChange
            )
        }

        entries?.let {
            items(entries) { defect ->
                DefectListItem(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .animateItem(),
                    defect = defect,
                    showFixButton = showFixButton && !defect.fixed,
                    onFixClick = { onFixClick(defect) }
                )
            }
        }

        item {
            Spacer(Modifier.height(56.dp))
        }
    }
}

@Composable
private fun DefectListFilters(
    modifier: Modifier,
    selectedFilter: DefectFilter,
    onFilterChange: (DefectFilter) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(DefectFilter.entries) {
            FilterChip(
                selected = it == selectedFilter,
                onClick = { onFilterChange(it) },
                label = { Text(getFilterText(it)) }
            )
        }
    }
}

@Composable
private fun getFilterText(filter: DefectFilter): String =
    when (filter) {
        DefectFilter.NotFixed -> stringResource(Res.string.filter_not_fixed)
        DefectFilter.Fixed -> stringResource(Res.string.filter_fixed)
        DefectFilter.All -> stringResource(Res.string.filter_all)
    }

@Composable
private fun DefectListItem(
    defect: Defect,
    showFixButton: Boolean,
    onFixClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier) {
        BoxWithConstraints {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.weight(1f, fill = false),
                            text = defect.type.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (defect.fixed) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = vectorResource(Res.drawable.check_circle_20px),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = stringResource(
                            Res.string.question_format,
                            defect.question.text
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    val descriptionText = if (defect.description != null) {
                        stringResource(
                            Res.string.description_format,
                            defect.description
                        )
                    } else {
                        stringResource(Res.string.no_description)
                    }

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = descriptionText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (defect.fixed) {
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = stringResource(Res.string.fixed_label),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (showFixButton) {
                    IconButton(onClick = onFixClick) {
                        Icon(vectorResource(Res.drawable.check_24px), null)
                    }
                }
            }
        }
    }
}