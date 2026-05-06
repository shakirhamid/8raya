package com.raya.eightraya.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raya.eightraya.ui.subjectdetails.SubjectDetailsRoute
import com.raya.eightraya.ui.subjects.SubjectsRoute

@Composable
fun EightRayaNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SubjectsDestination,
        modifier = modifier,
    ) {
        composable<SubjectsDestination> {
            SubjectsRoute(
                onSubjectClick = { subjectId ->
                    navController.navigate(SubjectDetailsDestination(subjectId = subjectId))
                },
            )
        }
        composable<SubjectDetailsDestination> {
            SubjectDetailsRoute(
                onBack = { navController.navigateUp() },
            )
        }
    }
}
