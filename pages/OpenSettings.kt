



import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.morhenn.ar_navigation.AuthViewModel
import de.morhenn.ar_navigation.MainActivity


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ProfileSettings(
    modifier: Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("landing") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar2(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            SettingItem("Password Management") {
                // Handle password
                navController.navigate("password")

            }

            SettingItem("FAQ") {
                navController.navigate("faq")

            }

            SettingItem("About") {
                // Handle About
                navController.navigate("about")

            }

            SettingItem("Report") {
                // Handle Report
                navController.navigate("report")

            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    authViewModel.signout()
                    navController.navigate("login") {
                        popUpTo("landing") { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text(text = "Logout", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun SettingItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 18.sp)
            Icon(Icons.Default.ArrowForward, contentDescription = "Go")
        }
    }
}



@Composable
fun BottomNavigationBar2(navController: NavHostController, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    NavigationBar(
        containerColor = Color(0xFF2962FF),
        tonalElevation = 8.dp,
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("landing")
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                context.startActivity(Intent(context, MainActivity::class.java))
            },
            icon = { Icon(Icons.Default.Place, contentDescription = "Navigate") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("info")
            },
            icon = { Icon(Icons.Default.Info, contentDescription = "Information for users") }
        )
        NavigationBarItem(
            selected = true,
            onClick = {
//                navController.navigate("profile")
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") }
        )
    }
}
