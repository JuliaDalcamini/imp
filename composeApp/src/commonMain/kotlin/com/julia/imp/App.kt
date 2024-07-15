package com.julia.imp

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.julia.imp.artifact.list.ArtifactsRoute
import com.julia.imp.artifact.list.ArtifactsScreen
import com.julia.imp.common.auth.UserCredentials
import com.julia.imp.common.session.SessionManager
import com.julia.imp.common.ui.theme.ImpTheme
import com.julia.imp.login.LoginRoute
import com.julia.imp.login.LoginScreen
import com.julia.imp.project.ProjectsRoute
import com.julia.imp.project.create.CreateProjectRoute
import com.julia.imp.project.create.CreateProjectScreen
import com.julia.imp.project.list.ProjectsScreen
import com.julia.imp.register.RegisterRoute
import com.julia.imp.register.RegisterScreen
import com.julia.imp.team.NoTeamsRoute
import com.julia.imp.team.NoTeamsScreen
import com.julia.imp.team.create.CreateTeamRoute
import com.julia.imp.team.create.CreateTeamScreen

@Composable
fun App(onShowReportRequest: (List<ImageBitmap>) -> Unit) {
    ImpTheme {
        Box(Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()) {
            val navController = rememberNavController()
            val session = SessionManager.activeSession
            var startDestination by remember { mutableStateOf<Any>(LoginRoute()) }

            LaunchedEffect(session) {
                val loggedIn = session != null
                val hasTeam = session?.team != null

                startDestination = when {
                    loggedIn && hasTeam -> ProjectsRoute
                    loggedIn && !hasTeam -> NoTeamsRoute
                    else -> LoginRoute()
                }

                navController.popBackStack(
                    route = startDestination,
                    inclusive = false
                )
            }

            NavHost(
                navController = navController,
                startDestination = startDestination,
                enterTransition = { slideIntoContainer(SlideDirection.Start) },
                exitTransition = { slideOutOfContainer(SlideDirection.Start) },
                popEnterTransition = { slideIntoContainer(SlideDirection.End) },
                popExitTransition = { slideOutOfContainer(SlideDirection.End) }
            ) {
                composable<LoginRoute> { entry ->
                    val route = entry.toRoute<LoginRoute>()

                    LoginScreen(
                        presetEmail = route.email,
                        presetPassword = route.password,
                        onSignIn = { SessionManager.activeSession = it },
                        onRegisterClick = { navController.navigate(RegisterRoute) }
                    )
                }

                composable<RegisterRoute> {
                    RegisterScreen(
                        onLoginClick = { navController.popBackStackToLogin() },
                        onRegistrationComplete = { navController.navigateToLogin(it) }
                    )
                }

                composable<NoTeamsRoute> {
                    NoTeamsScreen(onCreateTeamClick = { navController.navigate(CreateTeamRoute) })
                }

                composable<CreateTeamRoute> {
                    CreateTeamScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable<ProjectsRoute> {
                    ProjectsScreen(
                        onNewProjectClick = { navController.navigate(CreateProjectRoute) },
                        onProjectClick = { navController.navigate(ArtifactsRoute(it.id)) },
                        onTeamSwitch = { SessionManager.activeSession = it },
                        onCreateTeamClick = { navController.navigate(CreateTeamRoute) },
                        onShowReportRequest = onShowReportRequest
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

                composable<ArtifactsRoute> { entry ->
                    val route = entry.toRoute<ArtifactsRoute>()

                    ArtifactsScreen(
                        projectId = route.projectId,
                        onBackClick = { navController.popBackStack() },
                        onNewArtifactClick = {},
                        onEditArtifactClick = {}
                    )
                }
            }
        }
    }
}

private fun NavController.popBackStackToLogin() =
    this.popBackStack<LoginRoute>(
        inclusive = false,
        saveState = false
    )

private fun NavController.navigateToLogin(credentials: UserCredentials) =
    this.navigate(LoginRoute.of(credentials)) {
        popUpTo<LoginRoute> { inclusive = true }
    }

private val NavController.isAtLogin: Boolean
    get() =  this.currentBackStackEntry?.destination?.route == LoginRoute::class.qualifiedName