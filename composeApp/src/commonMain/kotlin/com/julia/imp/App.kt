import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.julia.imp.common.session.SessionManager
import com.julia.imp.common.session.UserSession
import com.julia.imp.common.ui.theme.ImpTheme
import com.julia.imp.login.LoginRoute
import com.julia.imp.login.LoginScreen
import com.julia.imp.project.ProjectsRoute
import com.julia.imp.project.create.CreateProjectRoute
import com.julia.imp.project.create.CreateProjectScreen
import com.julia.imp.project.list.ProjectsScreen

@Composable
fun App() {
    ImpTheme {
        Box(Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()) {
            val navController = rememberNavController()
            val session = SessionManager.activeSession

            LaunchedEffect(session) {
                handleSessionChange(session, navController)
            }

            NavHost(
                navController = navController,
                startDestination = LoginRoute
            ) {
                composable<LoginRoute> {
                    LoginScreen()
                }

                composable<ProjectsRoute> {
                    ProjectsScreen(
                        onNewProjectClick = { navController.navigate(CreateProjectRoute) }
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
            }
        }
    }
}

private fun handleSessionChange(session: UserSession?, navController: NavController) {
    if (session != null) {
        navController.navigate(ProjectsRoute) {
            popUpTo(LoginRoute)
        }
    } else {
        navController.navigate(LoginRoute) {
            popUpTo(LoginRoute)
            launchSingleTop = true
        }
    }
}