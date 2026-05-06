package com.raya.eightraya.ui.subjects

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raya.eightraya.ui.theme.RayaBackground
import com.raya.eightraya.ui.theme.RayaBlue
import com.raya.eightraya.ui.theme.RayaInk
import com.raya.eightraya.ui.theme.RayaMuted
import com.raya.eightraya.ui.theme.RayaPurple
import com.raya.eightraya.ui.theme.RayaTrack

@Composable
fun SubjectsRoute(
    onSubjectClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SubjectsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var isCreateDialogVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is SubjectsUiEvent.LoadFailed -> snackbarHostState.showSnackbar(event.message)
                is SubjectsUiEvent.SaveFailed -> snackbarHostState.showSnackbar(event.message)
                is SubjectsUiEvent.SubjectCreated -> snackbarHostState.showSnackbar("${event.name} added")
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = RayaBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isCreateDialogVisible = true },
                containerColor = RayaPurple,
                contentColor = Color.White,
            ) {
                Icon(
                    imageVector = AddSubjectIcon,
                    contentDescription = "Add subject",
                )
            }
        },
    ) { innerPadding ->
        SubjectsScreen(
            state = uiState,
            onSubjectClick = onSubjectClick,
            modifier = Modifier.padding(innerPadding),
        )
    }

    if (isCreateDialogVisible) {
        CreateSubjectDialog(
            onDismiss = { isCreateDialogVisible = false },
            onCreate = { name ->
                viewModel.createSubject(name)
                isCreateDialogVisible = false
            },
        )
    }
}

@Composable
fun SubjectsScreen(
    state: SubjectsUiState,
    onSubjectClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(RayaBackground),
    ) {
        when (state) {
            SubjectsUiState.Loading -> LoadingSubjects()
            is SubjectsUiState.Error -> ErrorSubjects(message = state.message)
            is SubjectsUiState.Content -> SubjectsContent(
                state = state,
                onSubjectClick = onSubjectClick,
            )
        }
    }
}

@Composable
private fun SubjectsContent(
    state: SubjectsUiState.Content,
    onSubjectClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        SubjectsHeader(
            count = state.subjects.size,
            overallProgress = state.overallProgress,
        )

        if (state.subjects.isEmpty()) {
            EmptySubjects()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = state.subjects,
                    key = { it.id },
                ) { subject ->
                    SubjectCard(
                        subject = subject,
                        onClick = { onSubjectClick(subject.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SubjectsHeader(
    count: Int,
    overallProgress: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 22.dp),
    ) {
        Text(
            text = "University Subjects",
            color = RayaInk,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "$count subjects",
            color = RayaMuted,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(18.dp))
        ProgressBar(progress = overallProgress)
    }
}

@Composable
private fun SubjectCard(
    subject: SubjectCardUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SubjectInitial(name = subject.name)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subject.name,
                        color = RayaInk,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "${subject.completedSheets}/${subject.totalSheets} sheets",
                        color = RayaMuted,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Text(
                    text = subject.progressLabel,
                    color = RayaPurple,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            ProgressBar(progress = subject.progress)
        }
    }
}

@Composable
private fun SubjectInitial(
    name: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(42.dp)
            .background(
                brush = Brush.linearGradient(listOf(RayaBlue, RayaPurple)),
                shape = RoundedCornerShape(8.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name.firstOrNull()?.uppercase() ?: "S",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val safeProgress = progress.coerceIn(0f, 1f)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp),
    ) {
        val radius = size.height / 2f
        drawRoundRect(
            color = RayaTrack,
            cornerRadius = CornerRadius(radius, radius),
        )
        drawRoundRect(
            brush = Brush.horizontalGradient(listOf(RayaBlue, RayaPurple)),
            size = Size(width = size.width * safeProgress, height = size.height),
            cornerRadius = CornerRadius(radius, radius),
        )
    }
}

@Composable
private fun LoadingSubjects(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = RayaBlue)
    }
}

@Composable
private fun EmptySubjects(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "No subjects yet",
            color = RayaMuted,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun ErrorSubjects(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            color = RayaPurple,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun CreateSubjectDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var subjectName by rememberSaveable { mutableStateOf("") }
    val trimmedName = subjectName.trim()

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "New subject",
                color = RayaInk,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
        },
        text = {
            OutlinedTextField(
                value = subjectName,
                onValueChange = { subjectName = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Subject name") },
            )
        },
        confirmButton = {
            TextButton(
                enabled = trimmedName.isNotBlank(),
                onClick = { onCreate(trimmedName) },
            ) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        containerColor = Color.White,
    )
}

private val AddSubjectIcon: ImageVector = ImageVector.Builder(
        name = "AddSubject",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(19f, 13f)
            horizontalLineTo(13f)
            verticalLineTo(19f)
            horizontalLineTo(11f)
            verticalLineTo(13f)
            horizontalLineTo(5f)
            verticalLineTo(11f)
            horizontalLineTo(11f)
            verticalLineTo(5f)
            horizontalLineTo(13f)
            verticalLineTo(11f)
            horizontalLineTo(19f)
            verticalLineTo(13f)
            close()
        }
}.build()
