package com.julia.imp

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.julia.imp.artifact.list.ArtifactsRoute
import com.julia.imp.artifact.list.ArtifactsScreen
import com.julia.imp.common.session.SessionManager
import com.julia.imp.common.session.UserSession
import com.julia.imp.common.ui.theme.ImpTheme
import com.julia.imp.login.LoginRoute
import com.julia.imp.login.LoginScreen
import com.julia.imp.project.ProjectsRoute
import com.julia.imp.project.create.CreateProjectRoute
import com.julia.imp.project.create.CreateProjectScreen
import com.julia.imp.project.list.ProjectsScreen
import com.julia.imp.register.RegisterRoute
import com.julia.imp.register.RegisterScreen
import com.julia.imp.report.ReportRoute
import com.julia.imp.report.ReportScreen

@Composable
fun App(onShareReportRequest: (List<ImageBitmap>) -> Unit) {
    ImpTheme {
        Box(Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()) {
            val navController = rememberNavController()
            val session = SessionManager.activeSession

            LaunchedEffect(session) {
                handleSessionChange(session, navController)
            }

            NavHost(
                navController = navController,
                startDestination = LoginRoute,
                enterTransition = { slideIntoContainer(SlideDirection.Start) },
                exitTransition = { slideOutOfContainer(SlideDirection.Start) + fadeOut() + scaleOut(targetScale = .5f) },
                popEnterTransition = { slideIntoContainer(SlideDirection.End) },
                popExitTransition = { slideOutOfContainer(SlideDirection.End) + fadeOut() + scaleOut(targetScale = .5f) }
            ) {
                composable<LoginRoute> {
                    LoginScreen(
                        onRegisterClick = { navController.navigate(RegisterRoute) }
                    )
                }

                composable<RegisterRoute> {
                    RegisterScreen(
                        onLoginClick = { navController.popBackStackToLogin() },
                        onRegistrationComplete = { navController.popBackStackToLogin() }
                    )
                }

                composable<ProjectsRoute> {
                    ProjectsScreen(
                        onNewProjectClick = { navController.navigate(CreateProjectRoute) },
                        onProjectClick = { navController.navigate(ArtifactsRoute(projectId = it.id)) }
                    )
                }

                composable<CreateProjectRoute> {
                    CreateProjectScreen(
                        onBackClick = { navController.popBackStack() },
                        onProjectCreated = {
                            navController.navigate(ProjectsRoute) {
                                popUpTo(ProjectsRoute) { inclusive = true }
                            }
                        }
                    )
                }

                composable<ArtifactsRoute> {
                    val route = it.toRoute<ArtifactsRoute>()

                    ArtifactsScreen(
                        projectId = route.projectId,
                        onBackClick = { navController.popBackStack() },
                        onNewArtifactClick = {},
                        onEditArtifactClick = { navController.navigate(ReportRoute) }
                    )
                }

                composable<ReportRoute> {
                    ReportScreen(
                        onBackClick = { navController.popBackStack() },
                        onShareRequest = onShareReportRequest
                    )
                }
            }
        }
    }
}

private fun handleSessionChange(session: UserSession?, navController: NavController) {
    when {
        session != null && navController.isAtLogin -> {
            navController.navigate(ProjectsRoute) {
                popUpTo<LoginRoute> { inclusive = true }
            }
        }

        session != null -> {
            navController.navigate(ProjectsRoute) {
                popUpTo<ProjectsRoute> { inclusive = true }
            }
        }

        !navController.isAtLogin -> {
            navController.popBackStackToLogin()
        }
    }
}

private fun NavController.popBackStackToLogin() =
    this.popBackStack<LoginRoute>(
        inclusive = false,
        saveState = false
    )

private val NavController.isAtLogin: Boolean
    get() =  this.currentBackStackEntry?.destination?.route == LoginRoute::class.qualifiedName