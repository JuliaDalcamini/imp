import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.julia.imp.common.ui.theme.ImpTheme
import com.julia.imp.login.LoginScreen
import com.julia.imp.project.list.ProjectsScreen
import com.julia.imp.team.Team

@Composable
fun App() {
    ImpTheme {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) {
            val navController = rememberNavController()
            var currentTeam by remember { mutableStateOf<Team?>(null) }

            LaunchedEffect(currentTeam) {
                if (currentTeam != null) {
                    navController.navigate("/projects") {
                        popUpTo(navController.graph.findStartDestination().route ?: "/login") {
                            inclusive = true
                        }
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = "/login"
            ) {
                composable("/login") {
                    LoginScreen(
                        onLoggedIn = { currentTeam = it }
                    )
                }

                composable("/projects") {
                    ProjectsScreen(
                        currentTeam = currentTeam ?: throw IllegalStateException("Must have a team"),
                        onTeamChanged = { currentTeam = it }
                    )
                }
            }
        }
    }
}