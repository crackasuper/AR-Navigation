



import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import de.morhenn.ar_navigation.AuthViewModel
import de.morhenn.ar_navigation.MainActivity
import de.morhenn.ar_navigation.model.Report
import de.morhenn.ar_navigation.submitReport
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    modifier: Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    var reportMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Issue") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar5(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Report Icon",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Report a Problem",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = reportMessage,
                onValueChange = { reportMessage = it },
                label = { Text("Describe the issue...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (reportMessage.isNotBlank()) {
                        val userId = authViewModel.currentUser?.uid ?: "anonymous"
                        val userEmail = authViewModel.currentUserEmail ?: "no-email"
                        val report = Report(userId = userId, userEmail = userEmail, message = reportMessage)

                        val databaseRef = FirebaseDatabase.getInstance().getReference("reports")
                        val reportId = databaseRef.push().key ?: UUID.randomUUID().toString()

                        databaseRef.child(reportId).setValue(report)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Report submitted. Thank you!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to submit report: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Please enter a message.", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Submit", fontSize = 16.sp)
            }
        }
    }
}



@Composable
fun BottomNavigationBar5(navController: NavHostController) {
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
