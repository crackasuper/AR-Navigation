



import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import de.morhenn.ar_navigation.AuthViewModel
import de.morhenn.ar_navigation.MainActivity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordManagementScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar2(navController)
        },

    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = { Text("Old Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm New Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    when {
                        oldPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() -> {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                        newPassword != confirmPassword -> {
                            Toast.makeText(context, "New passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                        newPassword.length < 6 -> {
                            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            authViewModel.changePassword(
                                oldPassword = oldPassword,
                                newPassword = newPassword,
                                onSuccess = {
                                    Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                },
                                onFailure = {
                                    Toast.makeText(context, "something went wrong try again.", Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                    }
                }
            )  {
                Text("Change Password")
            }
        }
    }
}


@Composable
fun BottomNavigationBar2(navController: NavHostController) {

    val context = LocalContext.current


    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
    val adminEmails = listOf("admin@astu.edu.et", "sdmh725@gmail.com")
    val isAdmin = currentUserEmail in adminEmails

    val homeDestination = if (isAdmin) "admin" else "landing"
    val homeIconDescription = if (isAdmin) "Admin" else "Home"
    val homeIcon = Icons.Default.Home


    NavigationBar(
        containerColor = Color(0xFF2962FF),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(homeDestination) },
            icon = { Icon(homeIcon, contentDescription = homeIconDescription) }
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
            onClick = { navController.navigate("info") },
            icon = { Icon(Icons.Default.Info, contentDescription = "Info") }
        )
        NavigationBarItem(
            selected = true,
            onClick = { navController.navigate("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") }
        )
    }
}
