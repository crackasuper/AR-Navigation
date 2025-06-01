

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import de.morhenn.ar_navigation.AuthState
import de.morhenn.ar_navigation.AuthViewModel
import de.morhenn.ar_navigation.MainActivity
import de.morhenn.ar_navigation.R
import de.morhenn.ar_navigation.model.Event
import de.morhenn.ar_navigation.viewmodel.EventViewModel

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import de.morhenn.ar_navigation.persistance.Place
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.CompositionLocalProvider




@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun LandingPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val viewModel: EventViewModel = viewModel()
    val events by viewModel.events.collectAsState()
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    val images = listOf(
        R.drawable.middle,  // your current image
        R.drawable.borcamu,  // add second image
        R.drawable.cse,
        R.drawable.gada,
        R.drawable.homepage// add third image
        // Add more if you want
    )

    var currentImageIndex by remember { mutableStateOf(0) }

    // This will change image every 3 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // 3000 milliseconds = 3 seconds
            currentImageIndex = (currentImageIndex + 1) % images.size
        }
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }


    CompositionLocalProvider(LocalContentColor provides Color.Black) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                background = Color.White,
                surface = Color.White,
                primary = Color(0xFF2962FF),
                onPrimary = Color.White,
                onBackground = Color.Black,
                onSurface = Color.Black
            )
        ) {

            Scaffold(
                modifier = Modifier.background(Color.White),
                containerColor = Color.White,

                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Dashboard",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        },
                      //  backgroundColor = Color(0xFF2962FF),
                        actions = {
                                Button(onClick = { navController.navigate("chatbot") }) {
                                 Text("Ask Chatbot")
                                }

                        }
                    )
                },

                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {


                    item {
                        Text(
                            text = "Dashboard",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.padding(16.dp))


                    }


                    item {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Crossfade(
                                targetState = images[currentImageIndex],
                                label = "Image Crossfade"
                            ) { imageRes ->
                                Image(
                                    painter = painterResource(id = imageRes),
                                    contentDescription = "Banner Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .height(220.dp)
                                        .clip(RoundedCornerShape(24.dp))

                                )
                            }
                        }

                    }



                    // Dots indicator
                    item {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            images.forEachIndexed { index, _ ->
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(if (index == currentImageIndex) 12.dp else 8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (index == currentImageIndex) Color(0xFF2962FF)
                                            else Color.LightGray
                                        )
                                )
                            }
                        }
                    }

                    // Key Places title
                    item {
                        Text(
                            text = "Key Places",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    // Key Places Cards
                    item {
                        val keyPlaces = listOf(
                            Triple(
                                "Library",
                                "Main university library with vast academic resources",
                                R.drawable.library
                            ),
                            Triple(
                                "CSE Building",
                                "Computer Science & Engineering department",
                                R.drawable.cse
                            ),
                            Triple(
                                "Gada Hall",
                                "Main hall used for university events",
                                R.drawable.maingate
                            ),
                            Triple(
                                "Admin Building",
                                "Main admin building in the university that contains different services and offices",
                                R.drawable.admin
                            )
                        )

                        var selectedPlace by remember {
                            mutableStateOf<Triple<String, String, Int>?>(
                                null
                            )
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(keyPlaces) { place ->
                                Card(
                                    modifier = Modifier
                                        .width(180.dp)
                                        .clickable { selectedPlace = place },
                                    elevation = CardDefaults.cardElevation(6.dp),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Column {
                                        Image(
                                            painter = painterResource(id = place.third),
                                            contentDescription = place.first,
                                            modifier = Modifier
                                                .height(100.dp)
                                                .fillMaxWidth(),
                                            contentScale = ContentScale.Crop
                                        )
                                        Text(
                                            text = place.first,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Dialog popup
                        selectedPlace?.let { place ->

                            val locationUrl = when (place.first) {
                                "Library" -> "https://maps.app.goo.gl/BVkrTX4e6bxG2kCV8"
                                "CSE Building" -> "https://www.google.com/maps?q=Your+CSE+Building+Location"
                                "Gada Hall" -> "https://www.google.com/maps?q=Your+Gada+Hall+Location"
                                "Admin Building" -> "https://maps.app.goo.gl/N4CQpb8pV182xX1s6"
                                else -> ""
                            }
                            AlertDialog(
                                onDismissRequest = { selectedPlace = null },
                                title = { Text(place.first) },
                                text = {
                                    Column {
                                        Image(
                                            painter = painterResource(id = place.third),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(180.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(place.second)
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "📍 View on Google Maps",
                                            color = Color(0xFF2962FF),
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clickable {
                                                    val intent = Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(locationUrl)
                                                    )
                                                    context.startActivity(intent)
                                                }
                                                .padding(top = 8.dp)
                                        )
                                    }
                                },
                                confirmButton = {
                                    TextButton(onClick = { selectedPlace = null }) {
                                        Text("Close")
                                    }
                                }
                            )

                        }
                    }


                    items(events) { event ->

                        EventCard(event) {
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))


                        Spacer(modifier = Modifier.height(12.dp))


                    }



                }
            }



        }
    }
}

@Composable
fun EventCard(event: Event, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = event.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "📍 ${event.location}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = event.description, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "🗓️ ${event.date}", fontSize = 14.sp, color = Color.Gray)
            }
        }

    }
    @Composable
    fun BottomNavigationBar(navController: NavHostController) {

        val context = LocalContext.current

        NavigationBar(
            containerColor = Color(0xFF2962FF),
            tonalElevation = 8.dp
        ) {
            NavigationBarItem(
                selected = true,
                onClick = { /* TODO */ },
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
                selected = false,
                onClick = {
                    navController.navigate("profile")
                },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") }
            )
        }

}


