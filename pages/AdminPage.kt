



import android.content.Intent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items


import androidx.compose.runtime.*


import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.sp

import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


import android.widget.Toast

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.input.key.Key.Companion.Calendar
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import de.morhenn.ar_navigation.AuthViewModel
import de.morhenn.ar_navigation.MainActivity
import de.morhenn.ar_navigation.model.Event

import java.util.*
//
//
//@Composable
//fun AdminPage(
//    modifier: Modifier = Modifier,
//    navController: NavHostController,
//    authViewModel: AuthViewModel
//) {
//    val context = LocalContext.current
//
//    var title by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    var location by remember { mutableStateOf("") }
//    var date by remember { mutableStateOf("") }
//    var isUploading by remember { mutableStateOf(false) }
//
//    val database = FirebaseDatabase.getInstance().getReference("Events")
//    val eventsList = remember { mutableStateListOf<Pair<String, Event>>() }
//    var showDeleteDialog by remember { mutableStateOf(false) }
//    var deleteEventKey by remember { mutableStateOf("") }
//
//    // Fetch existing events
//    LaunchedEffect(Unit) {
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                eventsList.clear()
//                for (child in snapshot.children) {
//                    val event = child.getValue(Event::class.java)
//                    if (event != null) {
//                        eventsList.add(Pair(child.key ?: "", event))
//                    }
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {}
//        })
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Post an Event",
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        OutlinedTextField(
//            value = title,
//            onValueChange = { title = it },
//            label = { Text("Event Title") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 12.dp),
//            shape = RoundedCornerShape(12.dp)
//        )
//
//        OutlinedTextField(
//            value = description,
//            onValueChange = { description = it },
//            label = { Text("Event Description") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 12.dp),
//            shape = RoundedCornerShape(12.dp)
//        )
//
//        OutlinedTextField(
//            value = location,
//            onValueChange = { location = it },
//            label = { Text("Event Location") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 12.dp),
//            shape = RoundedCornerShape(12.dp)
//        )
//
//        OutlinedTextField(
//            value = date,
//            onValueChange = { date = it },
//            label = { Text("Event Date (e.g., 2025-05-01)") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 12.dp),
//            shape = RoundedCornerShape(12.dp)
//        )
//
//        if (isUploading) {
//            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))
//        }
//
//        Button(
//            onClick = {
//                if (title.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty() && date.isNotEmpty()) {
//                    isUploading = true
//                    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
//                    val event = Event(title, description, location, date, timestamp)
//
//                    database.push().setValue(event)
//                        .addOnSuccessListener {
//                            Toast.makeText(context, "Event posted successfully", Toast.LENGTH_SHORT).show()
//                            title = ""
//                            description = ""
//                            location = ""
//                            date = ""
//                        }
//                        .addOnFailureListener { e ->
//                            Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
//                        }
//                        .addOnCompleteListener {
//                            isUploading = false
//                        }
//                } else {
//                    Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
//                }
//            },
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Upload Event", color = Color.White)
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Divider(color = Color.Gray, thickness = 1.dp)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = "All Events",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(bottom = 12.dp)
//        )
//
//        LazyColumn {
//            items(eventsList) { (key, event) ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp)
////                    elevation = 8.dp,
////                    containerColor = Color(0xFFF5F5F5)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Text("Title: ${event.title}", fontWeight = FontWeight.Bold)
//                        Text("Description: ${event.description}")
//                        Text("Location: ${event.location}")
//                        Text("Date: ${event.date}")
//                        Text("Posted At: ${event.createdAt}")
//
//                        Row(
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
//                        ) {
//                            Button(
//                                onClick = {
//                                    deleteEventKey = key
//                                    showDeleteDialog = true
//                                },
//                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
//                            ) {
//                                Text("Delete", color = Color.White)
//                            }
//
//                            Button(
//                                onClick = {
//                                    val updatedEvent = Event(
//                                        title = event.title + " (Updated)",
//                                        description = event.description,
//                                        location = event.location,
//                                        date = event.date,
//                                        createdAt = event.createdAt
//                                    )
//                                    database.child(key).setValue(updatedEvent)
//                                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
//                                },
//                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF039BE5))
//                            ) {
//                                Text("Update", color = Color.White)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Divider(color = Color.Gray, thickness = 1.dp)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//
//        OutlinedButton({
//            context.startActivity(Intent(context, MainActivity::class.java))
//        }) {
//            Text("Continue to Navigation")
//        }
//
//
//        Button(
//            onClick = {
//                authViewModel.signout()
//                navController.navigate("login") {
//                    popUpTo("admin") { inclusive = true }
//                }
//            },
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Sign Out", color = Color.White)
//        }
//    }
//
//    if (showDeleteDialog) {
//        AlertDialog(
//            onDismissRequest = { showDeleteDialog = false },
//            title = { Text("Delete Event") },
//            text = { Text("Are you sure you want to delete this event?") },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        database.child(deleteEventKey).removeValue()
//                        showDeleteDialog = false
//                        Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show()
//                    },
//                    colors = ButtonDefaults.buttonColors(containerColor  = Color.Red)
//                ) {
//                    Text("Yes", color = Color.White)
//                }
//            },
//            dismissButton = {
//                Button(
//                    onClick = { showDeleteDialog = false },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
//                ) {
//                    Text("No", color = Color.White)
//                }
//            }
//        )
//    }
//    BottomNavigationBar6(navController)
//}
//
//
//
//@Composable
//fun BottomNavigationBar6(navController: NavHostController, modifier: Modifier = Modifier) {
//
//    val context = LocalContext.current
//
//    NavigationBar(
//        containerColor = Color(0xFF2962FF),
//        tonalElevation = 8.dp,
//        modifier = modifier
//    ) {
//        NavigationBarItem(
//            selected = true,
//            onClick = {
//                navController.navigate("landing")
//            },
//            icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
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
//            onClick = {
//                navController.navigate("info")
//            },
//            icon = { Icon(Icons.Default.Info, contentDescription = "Information for users") }
//        )
//        NavigationBarItem(
//            selected = false,
//            onClick = {
//                navController.navigate("profile")
//            },
//            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") }
//        )
//    }
//}
//
//@Composable
//fun AdminPage(
//    modifier: Modifier = Modifier,
//    navController: NavHostController,
//    authViewModel: AuthViewModel
//) {
//    val context = LocalContext.current
//
//    var title by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    var location by remember { mutableStateOf("") }
//    var date by remember { mutableStateOf("") }
//    var isUploading by remember { mutableStateOf(false) }
//
//    val database = FirebaseDatabase.getInstance().getReference("Events")
//    val eventsList = remember { mutableStateListOf<Pair<String, Event>>() }
//    var showDeleteDialog by remember { mutableStateOf(false) }
//    var deleteEventKey by remember { mutableStateOf("") }
//
//    // Fetch events
//    LaunchedEffect(Unit) {
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                eventsList.clear()
//                for (child in snapshot.children) {
//                    val event = child.getValue(Event::class.java)
//                    if (event != null) {
//                        eventsList.add(Pair(child.key ?: "", event))
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })
//    }
//
//    Scaffold(
//        bottomBar = {
//            BottomNavigationBar6(navController)
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = modifier
//                .padding(innerPadding)
//                .verticalScroll(rememberScrollState())
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            // Posting Form
//            Text("Post an Event", fontSize = 28.sp, fontWeight = FontWeight.Bold)
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Event Title") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
//            Spacer(modifier = Modifier.height(12.dp))
//            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Event Description") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
//            Spacer(modifier = Modifier.height(12.dp))
//            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Event Location") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
//            Spacer(modifier = Modifier.height(12.dp))
//            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Event Date (e.g., 2025-05-01)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
//            Spacer(modifier = Modifier.height(12.dp))
//
//            if (isUploading) {
//                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//                Spacer(modifier = Modifier.height(12.dp))
//            }
//
//            Button(
//                onClick = {
//                    if (title.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty() && date.isNotEmpty()) {
//                        isUploading = true
//                        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
//                        val event = Event(title, description, location, date, timestamp)
//
//                        database.push().setValue(event)
//                            .addOnSuccessListener {
//                                Toast.makeText(context, "Event posted successfully", Toast.LENGTH_SHORT).show()
//                                title = ""
//                                description = ""
//                                location = ""
//                                date = ""
//                            }
//                            .addOnFailureListener { e ->
//                                Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
//                            }
//                            .addOnCompleteListener {
//                                isUploading = false
//                            }
//                    } else {
//                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
//                    }
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Upload Event", color = Color.White)
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//            Divider(color = Color.Gray, thickness = 1.dp)
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//
//            Text("All Events", fontSize = 24.sp, fontWeight = FontWeight.Bold)
//
//            eventsList.forEach { (key, event) ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp)
//                        .clickable {
//                            val intent = Intent(context, MainActivity::class.java)
//                            // Optional: Pass location or event data to AR activity
//                            intent.putExtra("eventLocation", event.location)
//                            context.startActivity(intent)
//                        }
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Text("Title: ${event.title}", fontWeight = FontWeight.Bold)
//                        Text("Description: ${event.description}")
//                        Text("Location: ${event.location}")
//                        Text("Date: ${event.date}")
//                        Text("Posted At: ${event.createdAt}")
//
//                        Row(
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(top = 8.dp)
//                        ) {
//                            Button(
//                                onClick = {
//                                    deleteEventKey = key
//                                    showDeleteDialog = true
//                                },
//                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
//                            ) {
//                                Text("Delete", color = Color.White)
//                            }
//
//                            Button(
//                                onClick = {
//                                    val updatedEvent = event.copy(title = "${event.title} (Updated)")
//                                    database.child(key).setValue(updatedEvent)
//                                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
//                                },
//                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF039BE5))
//                            ) {
//                                Text("Update", color = Color.White)
//                            }
//                        }
//                    }
//                }
//            }
//
//
//            Spacer(modifier = Modifier.height(24.dp))
//            Divider(color = Color.Gray, thickness = 1.dp)
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            Spacer(modifier = Modifier.height(64.dp)) // leave space above bottom bar
//        }
//
//        // Delete dialog
//        if (showDeleteDialog) {
//            AlertDialog(
//                onDismissRequest = { showDeleteDialog = false },
//                title = { Text("Delete Event") },
//                text = { Text("Are you sure you want to delete this event?") },
//                confirmButton = {
//                    Button(
//                        onClick = {
//                            database.child(deleteEventKey).removeValue()
//                            showDeleteDialog = false
//                            Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show()
//                        },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
//                    ) {
//                        Text("Yes", color = Color.White)
//                    }
//                },
//                dismissButton = {
//                    Button(
//                        onClick = { showDeleteDialog = false },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
//                    ) {
//                        Text("No", color = Color.White)
//                    }
//                }
//            )
//        }
//    }
//}
//
//
//
@Composable
fun BottomNavigationBar6(navController: NavHostController, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    NavigationBar(
        containerColor = Color(0xFF2962FF),
        tonalElevation = 8.dp,
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {
                navController.navigate("admin")
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
            selected = false,
            onClick = {
                navController.navigate("profile")
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") }
        )
    }
}



@Composable
fun AdminPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance().getReference("Events")

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    val eventsList = remember { mutableStateListOf<Pair<String, Event>>() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteEventKey by remember { mutableStateOf("") }

    // Fetch events
    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventsList.clear()
                snapshot.children.forEach { child ->
                    child.getValue(Event::class.java)?.let { event ->
                        eventsList.add(Pair(child.key ?: "", event))
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Scaffold(
        bottomBar = { BottomNavigationBar6(navController) },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Title
            Text(
                "Admin Event Panel",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Event Form Section
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Post a New Event", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)

                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Event Title") }, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth())

                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth())

                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") }, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth())

                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (e.g. 2025-06-01)") }, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth())

                    if (isUploading) {
                        Spacer(Modifier.height(12.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (title.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty() && date.isNotEmpty()) {
                                isUploading = true
                                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                                val event = Event(title, description, location, date, timestamp)

                                database.push().setValue(event).addOnSuccessListener {
                                    Toast.makeText(context, "Event posted successfully", Toast.LENGTH_SHORT).show()
                                    title = ""; description = ""; location = ""; date = ""
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed: ${it.message}", Toast.LENGTH_LONG).show()
                                }.addOnCompleteListener {
                                    isUploading = false
                                }
                            } else {
                                Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Upload Event", color = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Divider()

            // Events List
            Text("All Events", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))

            eventsList.forEach { (key, event) ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.putExtra("eventLocation", event.location)
                            context.startActivity(intent)
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(event.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(event.description, fontSize = 14.sp)
                        Text("📍 ${event.location}", fontSize = 14.sp, color = Color.Gray)
                        Text("📅 ${event.date}", fontSize = 14.sp, color = Color.Gray)
                        Text("🕒 Posted: ${event.createdAt}", fontSize = 12.sp, color = Color.LightGray)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    deleteEventKey = key
                                    showDeleteDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                            ) {
                                Text("Delete", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    val updatedEvent = event.copy(title = "${event.title} (Updated)")
                                    database.child(key).setValue(updatedEvent)
                                    Toast.makeText(context, "Event updated", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                            ) {
                                Text("Update", color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(100.dp)) // Padding before bottom nav
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Event") },
                text = { Text("Are you sure you want to delete this event?") },
                confirmButton = {
                    Button(
                        onClick = {
                            database.child(deleteEventKey).removeValue()
                            showDeleteDialog = false
                            Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Yes", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDeleteDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("No", color = Color.White)
                    }
                }
            )
        }
    }
}
