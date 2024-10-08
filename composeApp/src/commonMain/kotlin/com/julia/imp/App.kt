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
import com.julia.imp.artifact.create.CreateArtifactRoute
import com.julia.imp.artifact.create.CreateArtifactScreen
import com.julia.imp.artifact.details.ArtifactDetailsRoute
import com.julia.imp.artifact.details.ArtifactDetailsScreen
import com.julia.imp.artifact.edit.EditArtifactRoute
import com.julia.imp.artifact.edit.EditArtifactScreen
import com.julia.imp.artifact.list.ArtifactsRoute
import com.julia.imp.artifact.list.ArtifactsScreen
import com.julia.imp.artifact.prioritize.PrioritizeArtifactRoute
import com.julia.imp.artifact.prioritize.PrioritizeArtifactScreen
import com.julia.imp.common.session.SessionManager
import com.julia.imp.common.ui.theme.ImpTheme
import com.julia.imp.defect.DefectRoute
import com.julia.imp.defect.DefectsScreen
import com.julia.imp.inspection.create.CreateInspectionRoute
import com.julia.imp.inspection.create.CreateInspectionScreen
import com.julia.imp.inspection.details.InspectionDetailsRoute
import com.julia.imp.inspection.details.InspectionDetailsScreen
import com.julia.imp.login.LoginRoute
import com.julia.imp.login.LoginScreen
import com.julia.imp.logout.LogoutRoute
import com.julia.imp.logout.LogoutScreen
import com.julia.imp.project.ProjectsRoute
import com.julia.imp.project.create.CreateProjectRoute
import com.julia.imp.project.create.CreateProjectScreen
import com.julia.imp.project.dashboard.DashboardRoute
import com.julia.imp.project.dashboard.DashboardScreen
import com.julia.imp.project.list.ProjectsScreen
import com.julia.imp.project.manage.ManageProjectRoute
import com.julia.imp.project.manage.ManageProjectScreen
import com.julia.imp.register.RegisterRoute
import com.julia.imp.register.RegisterScreen
import com.julia.imp.team.NoTeamsRoute
import com.julia.imp.team.NoTeamsScreen
import com.julia.imp.team.create.CreateTeamRoute
import com.julia.imp.team.create.CreateTeamScreen
import com.julia.imp.team.manage.ManageTeamRoute
import com.julia.imp.team.manage.ManageTeamScreen
import com.julia.imp.team.member.list.TeamMembersRoute
import com.julia.imp.team.member.list.TeamMembersScreen

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
                        onLoginClick = { navController.popBackStack() },
                        onRegistrationComplete = { credentials ->
                            navController.popUpToAndNavigate(LoginRoute.of(credentials))
                        }
                    )
                }

                composable<LogoutRoute> {
                    LogoutScreen(onLogout = { SessionManager.activeSession = null })
                }

                composable<NoTeamsRoute> {
                    NoTeamsScreen(onCreateTeamClick = { navController.navigate(CreateTeamRoute) })
                }

                composable<CreateTeamRoute> {
                    CreateTeamScreen(
                        onBackClick = { navController.popBackStack() },
                        onTeamCreated = { SessionManager.activeSession = it }
                    )
                }

                composable<ManageTeamRoute> {
                    ManageTeamScreen(
                        onBackClick = { navController.popBackStack() },
                        onManageMembersClick = { navController.navigate(TeamMembersRoute) },
                        onTeamUpdate = { SessionManager.activeSession = session?.copy(team = it) }
                    )
                }

                composable<TeamMembersRoute> {
                    TeamMembersScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable<ProjectsRoute> {
                    ProjectsScreen(
                        onProjectClick = { navController.navigate(ArtifactsRoute(it)) },
                        onNewProjectClick = { navController.navigate(CreateProjectRoute) },
                        onViewProjectStatsClick = { navController.navigate(DashboardRoute(it)) },
                        onManageProjectClick = { navController.navigate(ManageProjectRoute(it.id)) },
                        onTeamSwitch = { SessionManager.activeSession = it },
                        onManageTeamClick = { navController.navigate(ManageTeamRoute) },
                        onCreateTeamClick = { navController.navigate(CreateTeamRoute) },
                        onLogoutClick = { navController.navigate(LogoutRoute) }
                    )
                }

                composable<CreateProjectRoute> {
                    CreateProjectScreen(
                        onBackClick = { navController.popBackStack() },
                        onProjectCreated = { navController.popBackStack() }
                    )
                }

                composable<DashboardRoute>(DashboardRoute.typeMap) { entry ->
                    val route = entry.toRoute<DashboardRoute>()

                    DashboardScreen(
                        project = route.project,
                        onBackClick = { navController.popBackStack() },
                        onShowReportRequest = onShowReportRequest
                    )
                }

                composable<ManageProjectRoute> { entry ->
                    val route = entry.toRoute<ManageProjectRoute>()

                    ManageProjectScreen(
                        projectId = route.projectId,
                        onBackClick = { navController.popBackStack() },
                        onProjectFinished = { navController.popBackStack() },
                        onProjectDeleted = { navController.popBackStack() }
                    )
                }

                composable<ArtifactsRoute>(ArtifactsRoute.typeMap) { entry ->
                    val route = entry.toRoute<ArtifactsRoute>()

                    ArtifactsScreen(
                        project = route.project,
                        onBackClick = { navController.popBackStack() },
                        onArtifactClick = { navController.navigate(ArtifactDetailsRoute.of(it)) },
                        onNewArtifactClick = { navController.navigate(CreateArtifactRoute(route.project)) },
                        onEditArtifactClick = { navController.navigate(EditArtifactRoute(it)) },
                        onShowDefectsClick = { navController.navigate(DefectRoute(it, route.project)) },
                        onPrioritizeArtifactClick = {
                            navController.navigate(
                                PrioritizeArtifactRoute(
                                    it,
                                    route.project
                                )
                            )
                        }
                    )
                }

                composable<CreateArtifactRoute>(CreateArtifactRoute.typeMap) { entry ->
                    val route = entry.toRoute<CreateArtifactRoute>()

                    CreateArtifactScreen(
                        projectId = route.project.id,
                        onBackClick = { navController.popBackStack() },
                        onArtifactCreated = { artifact ->
                            navController.run {
                                popBackStack()
                                popUpToAndNavigate(ArtifactDetailsRoute.of(artifact))
                            }
                        }
                    )
                }

                composable<EditArtifactRoute>(EditArtifactRoute.typeMap) { entry ->
                    val route = entry.toRoute<EditArtifactRoute>()

                    EditArtifactScreen(
                        artifact = route.artifact,
                        onBackClick = { navController.popBackStack() },
                        onArtifactSaved = { artifact ->
                            navController.run {
                                popBackStack()
                                popUpToAndNavigate(ArtifactDetailsRoute.of(artifact))
                            }
                        }
                    )
                }

                composable<PrioritizeArtifactRoute>(PrioritizeArtifactRoute.typeMap) { entry ->
                    val route = entry.toRoute<PrioritizeArtifactRoute>()

                    PrioritizeArtifactScreen(
                        artifact = route.artifact,
                        prioritizer = route.project.prioritizer,
                        onBackClick = { navController.popBackStack() },
                        onArtifactSaved = { artifact ->
                            navController.run {
                                popBackStack()
                                popUpToAndNavigate(ArtifactDetailsRoute.of(artifact))
                            }
                        }
                    )
                }

                composable<ArtifactDetailsRoute> { entry ->
                    val route = entry.toRoute<ArtifactDetailsRoute>()

                    ArtifactDetailsScreen(
                        artifactId = route.artifactId,
                        projectId = route.projectId,
                        onBackClick = { navController.popBackStack() },
                        onEditClick = { navController.navigate(EditArtifactRoute(it)) },
                        onInspectClick = { navController.navigate(CreateInspectionRoute(it)) },
                        onInspectionClick = { artifact, inspection ->
                            navController.navigate(
                                InspectionDetailsRoute(
                                    artifact,
                                    route.projectId,
                                    inspection.id
                                )
                            )
                        }
                    )
                }

                composable<CreateInspectionRoute>(CreateInspectionRoute.typeMap) { entry ->
                    val route = entry.toRoute<CreateInspectionRoute>()

                    CreateInspectionScreen(
                        artifact = route.artifact,
                        onBackClick = { navController.popBackStack() },
                        onInspectionCreated = {
                            navController.run {
                                popBackStack()
                                popUpToAndNavigate(ArtifactDetailsRoute.of(route.artifact))
                            }
                        }
                    )
                }

                composable<InspectionDetailsRoute>(InspectionDetailsRoute.typeMap) { entry ->
                    val route = entry.toRoute<InspectionDetailsRoute>()

                    InspectionDetailsScreen(
                        inspectionId = route.inspectionId,
                        artifact = route.artifact,
                        projectId = route.projectId,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable<DefectRoute>(DefectRoute.typeMap) { entry ->
                    val route = entry.toRoute<DefectRoute>()

                    DefectsScreen(
                        artifact = route.artifact,
                        project = route.project,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

private inline fun <reified T : Any> NavController.popUpToAndNavigate(route: T) =
    this.navigate(route) {
        popUpTo<T> { inclusive = true }
    }