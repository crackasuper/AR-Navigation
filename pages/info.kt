



import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import de.morhenn.ar_navigation.AuthViewModel
import de.morhenn.ar_navigation.MainActivity

@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun info(modifier: Modifier = Modifier, navController: NavHostController, authViewModel: AuthViewModel) {
    var visible by remember { mutableStateOf(false) }

    // Start animation after small delay
    LaunchedEffect(Unit) {
        visible = true
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // TOP BAR
            TopAppBar(
                title = {
                    Text(text = "Information", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )

            // CONTENT with Animation
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(durationMillis = 800)) // Smooth fade-in
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "How to Use the Application",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    InfoSection(
                        title = "1. Search for Places:",
                        description = "Use the search bar at the top of the home page to find key locations inside the university."
                    )
                    InfoSection(
                        title = "2. Explore Events:",
                        description = "Check the Upcoming Events section to stay updated with campus events."
                    )
                    InfoSection(
                        title = "3. Navigate Using AR:",
                        description = "Click the Navigation icon in the bottom bar to start AR Navigation. Follow the camera guidance to reach your destination."
                    )
                    InfoSection(
                        title = "4. Notifications:",
                        description = "Stay updated with notifications regarding important updates and announcements."
                    )
                    InfoSection(
                        title = "5. Manage Your Profile:",
                        description = "Tap the Profile icon to view and manage your user settings."
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Enjoy your experience!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Bottom Bar
        BottomNavigationBar1(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun InfoSection(title: String, description: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = description,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun BottomNavigationBar1(navController: NavHostController, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
    val adminEmails = listOf("admin@astu.edu.et", "sdmh725@gmail.com")
    val isAdmin = currentUserEmail in adminEmails

    val homeDestination = if (isAdmin) "admin" else "landing"
    val homeIconDescription = if (isAdmin) "Admin" else "Home"
    val homeIcon = Icons.Default.Home

    NavigationBar(
        containerColor = Color(0xFF2962FF),
        tonalElevation = 8.dp,
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(homeDestination)
            },
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
            selected = true,
            onClick = {
                navController.navigate("info")
            },
            icon = { Icon(Icons.Default.Info, contentDescription = "Information for users") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("profile")
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") }
        )
    }
}
