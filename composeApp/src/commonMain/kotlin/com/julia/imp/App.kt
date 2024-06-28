import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.julia.imp.common.session.SessionManager
import com.julia.imp.common.session.UserSession
import com.julia.imp.common.ui.theme.ImpTheme
import com.julia.imp.login.LoginScreen
import com.julia.imp.project.form.NewProjectScreen
import com.julia.imp.project.list.ProjectsScreen

@Composable
fun App() {
    ImpTheme {
        Box(Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()) {
            val navController = rememberNavController()
            val session = SessionManager.activeSession

            LaunchedEffect(session) {
                if (session != null) {
                    navController.navigate("/projects") {
                        popUpTo("/login")
                    }
                } else {
                    navController.navigate("/login") {
                        launchSingleTop = true
                        popUpTo("/login")
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = "/login"
            ) {
                composable("/login") {
                    LoginScreen()
                }

                composable("/projects") {
                    ProjectsScreen()
                }
            }
        }
    }
}
