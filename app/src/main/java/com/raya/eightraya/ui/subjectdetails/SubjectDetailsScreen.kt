package com.raya.eightraya.ui.subjectdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raya.eightraya.ui.theme.RayaBackground
import com.raya.eightraya.ui.theme.RayaBlue
import com.raya.eightraya.ui.theme.RayaInk
import com.raya.eightraya.ui.theme.RayaMuted
import com.raya.eightraya.ui.theme.RayaPurple
import kotlinx.coroutines.launch

@Composable
fun SubjectDetailsRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SubjectDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val summarySheet by viewModel.summarySheet.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    SubjectDetailsScreen(
        state = uiState,
        summarySheet = summarySheet,
        onBack = onBack,
        onUploadClick = {
            scope.launch {
                snackbarHostState.showSnackbar("File upload will be available in a future update.")
            }
        },
        onSummarizeDocument = { item ->
            viewModel.requestDocumentSummary(
                documentTitle = item.title,
                extractedText = item.extractedText,
            )
        },
        onDismissSummarySheet = { viewModel.clearSummarySheet() },
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailsScreen(
    state: SubjectDetailsUiState,
    summarySheet: SummarySheetUiState?,
    onBack: () -> Unit,
    onUploadClick: () -> Unit,
    onSummarizeDocument: (DocumentListItemUiState) -> Unit,
    onDismissSummarySheet: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = RayaBackground,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when (state) {
                                is SubjectDetailsUiState.Content -> state.subjectName
                                SubjectDetailsUiState.Loading -> "Subject"
                                SubjectDetailsUiState.SubjectNotFound -> "Subject"
                                is SubjectDetailsUiState.Error -> "Subject"
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = RayaInk,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = RayaInk,
                    ),
                )
            },
        ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (state) {
                SubjectDetailsUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = RayaBlue)
                    }
                }

                SubjectDetailsUiState.SubjectNotFound -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "This subject could not be found.",
                            color = RayaMuted,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                is SubjectDetailsUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = state.message,
                            color = RayaPurple,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                is SubjectDetailsUiState.Content -> {
                    SubjectDetailsBody(
                        documents = state.documents,
                        onUploadClick = onUploadClick,
                        onSummarizeDocument = onSummarizeDocument,
                    )
                }
            }
        }
        }

        if (summarySheet != null) {
            ModalBottomSheet(
                onDismissRequest = onDismissSummarySheet,
                sheetState = bottomSheetState,
                containerColor = Color.White,
            ) {
                SummarySheetContent(
                    state = summarySheet,
                    modifier = Modifier.padding(bottom = 28.dp),
                )
            }
        }
    }
}

@Composable
private fun SummarySheetContent(
    state: SummarySheetUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
    ) {
        Text(
            text = "AI summary",
            style = MaterialTheme.typography.labelLarge,
            color = RayaPurple,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = state.documentTitle,
            style = MaterialTheme.typography.titleLarge,
            color = RayaInk,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (val phase = state.phase) {
            SummarySheetPhase.Loading -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        strokeWidth = 3.dp,
                        color = RayaBlue,
                    )
                    Text(
                        text = "Summarizing with Gemini…",
                        style = MaterialTheme.typography.bodyLarge,
                        color = RayaMuted,
                    )
                }
            }

            is SummarySheetPhase.Success -> {
                Text(
                    text = phase.summaryText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = RayaInk,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(maxHeight = 360.dp)
                        .verticalScroll(rememberScrollState()),
                )
            }

            is SummarySheetPhase.Error -> {
                Text(
                    text = phase.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun SubjectDetailsBody(
    documents: List<DocumentListItemUiState>,
    onUploadClick: () -> Unit,
    onSummarizeDocument: (DocumentListItemUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        UploadButton(onClick = onUploadClick)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Documents",
            color = RayaInk,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (documents.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No documents yet. Upload study materials to get started.",
                    color = RayaMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                items(
                    items = documents,
                    key = { it.id },
                ) { item ->
                    DocumentRow(
                        item = item,
                        onSummarize = { onSummarizeDocument(item) },
                    )
                }
            }
        }
    }
}

@Composable
private fun UploadButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(listOf(RayaBlue, RayaPurple)),
                shape = RoundedCornerShape(10.dp),
            ),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            disabledElevation = 0.dp,
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Upload,
            contentDescription = null,
            tint = Color.White,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Upload",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )
    }
}

@Composable
private fun DocumentRow(
    item: DocumentListItemUiState,
    onSummarize: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val canSummarize = item.extractedText?.isNotBlank() == true
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.title,
                color = RayaInk,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.typeLabel,
                color = RayaMuted,
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.statusLabel,
                color = RayaPurple,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onSummarize,
                enabled = canSummarize,
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = if (canSummarize) RayaPurple else RayaMuted,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Summarize",
                    color = if (canSummarize) RayaPurple else RayaMuted,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
