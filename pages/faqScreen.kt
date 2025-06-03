


import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import de.morhenn.ar_navigation.AuthViewModel
import de.morhenn.ar_navigation.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun faqScreen(
    modifier: Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAQ") },
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
            BottomNavigationBar3(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Frequently Asked Questions",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            FAQItem(
                question = "How do I start navigation?",
                answer = "Tap on the Navigation icon at the bottom and follow the AR camera guidance to your destination."
            )

            FAQItem(
                question = "Can I search for locations?",
                answer = "Yes, use the search bar on the home page to find key places around the university."
            )

            FAQItem(
                question = "How do I receive event notifications?",
                answer = "You will automatically receive notifications about upcoming events once you are logged in."
            )

            FAQItem(
                question = "How do I report a problem?",
                answer = "Go to the Profile settings and tap on 'Report' to submit any issue or feedback."
            )

            FAQItem(
                question = "How do I log out?",
                answer = "In the Profile settings, tap the 'Logout' button to safely sign out."
            )
        }
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize() // Smooth expand/collapse animation
        ) {
            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}


//
//@Composable
//fun BottomNavigationBar3(navController: NavHostController) {
//    val context = LocalContext.current
//    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
//    val adminEmails = listOf("admin@astu.edu.et", "sdmh725@gmail.com")
//    val isAdmin = currentUserEmail in adminEmails
//
//    NavigationBar(
//        containerColor = Color(0xFF2962FF),
//        tonalElevation = 8.dp
//    ) {
//        NavigationBarItem(
//            selected = false,
//
//             if (isAdmin) {
//                onClick = { navController.navigate("admin")}
//                icon = { Icon(Icons.Default.Home, contentDescription = "admin") }
//             } else {
//                  onClick = { navController.navigate("landing")}
//                   icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
//             }
//
//
////            onClick = { navController.navigate("landing") },
////            icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
//        )
//        NavigationBarItem(
//            selected = false,
//            onClick = {
//                context.startActivity(Intent(context, MainActivity::class.java))
//            },
//            icon = { Icon(Icons.Default.Place, contentDescription = "Navigate") }
//        )
//        NavigationBarItem(
//            selected = false,
//            onClick = { navController.navigate("info") },
//            icon = { Icon(Icons.Default.Info, contentDescription = "Info") }
//        )
//        NavigationBarItem(
//            selected = true,
//            onClick = { navController.navigate("profile") },
//            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") }
//        )
//    }
//}
//
//


@Composable
fun BottomNavigationBar3(navController: NavHostController) {
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
