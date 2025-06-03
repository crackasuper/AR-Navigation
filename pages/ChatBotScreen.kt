




import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel




import androidx.compose.foundation.Image

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.*
import androidx.compose.ui.draw.clip

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.morhenn.ar_navigation.R
import java.text.SimpleDateFormat
import java.util.*



//
//@Composable
//fun ChatBotScreen(viewModel: ChatBotViewModel = viewModel()) {
//    val messages = viewModel.messages
//    val inputText by viewModel.inputText
//    val scrollState = rememberLazyListState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//    ) {
//
//        //displaying helper message for user
//        Text(
//            text = "🤖 Welcome! What can I help you with?",
//            modifier = Modifier
//                .padding(start = 16.dp, top = 8.dp, end = 16.dp),
//            style = MaterialTheme.typography.titleMedium,
//            color = Color.DarkGray
//        )
//
//        Column(modifier = Modifier.padding(start = 24.dp, end = 16.dp, top = 4.dp, bottom = 8.dp)) {
//            Text("• Navigation over app", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
//            Text("• Event information", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
//            Text("• Key places location in ASTU", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
//        }
//
//
//
//
//        LazyColumn(
//            state = scrollState,
//            modifier = Modifier
//                .weight(1f)
//                .padding(8.dp),
//            verticalArrangement = Arrangement.Bottom
//        ) {
//            items(messages) { msg ->
//                val isUser = msg.sender == "You"
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .background(
//                                if (isUser) Color(0xFFDCF8C6) else Color.White,
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                            .padding(12.dp)
//                            .widthIn(max = 250.dp)
//                    ) {
//                        Text(msg.content, color = Color.Black)
//                    }
//                }
//            }
//
//            // Show typing animation if bot is typing
//            if (viewModel.isBotTyping.value) {
//                item {
//                    TypingAnimationBubble()
//                }
//            }
//        }
//
//
//
//        Divider()
//
//        Row(
//            modifier = Modifier
//                .padding(8.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            TextField(
//                value = inputText,
//                onValueChange = { viewModel.inputText.value = it },
//                modifier = Modifier.weight(1f),
//                placeholder = { Text("Ask me anything...") }
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Button(onClick = {
//                viewModel.sendMessage("openAI key here")
//               //            }) {
//                Text("Send")
//            }
//        }
//    }
//}
//
//


//
//@Composable
//fun ChatBotScreen(viewModel: ChatBotViewModel = viewModel()) {
//    val messages = viewModel.messages
//    val inputText by viewModel.inputText
//    val scrollState = rememberLazyListState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//    ) {
//        // Chat messages (scrollable)
//        LazyColumn(
//            state = scrollState,
//            modifier = Modifier
//                .weight(1f)
//                .padding(8.dp),
//            verticalArrangement = Arrangement.Bottom
//        ) {
//            items(messages) { msg ->
//                val isUser = msg.sender == "You"
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .background(
//                                if (isUser) Color(0xFFDCF8C6) else Color.White,
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                            .padding(12.dp)
//                            .widthIn(max = 250.dp)
//                            .shadow(1.dp)
//                    ) {
//                        Text(msg.content, color = Color.Black)
//                    }
//                }
//            }
//        }
//
//        Divider()
//
//        // Help suggestions section
//        Column(
//            modifier = Modifier
//                .padding(horizontal = 16.dp, vertical = 8.dp)
//        ) {
//            Text(
//                text = "🤖 What can I help you with?",
//                style = MaterialTheme.typography.titleMedium,
//                color = Color.DarkGray
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Column(modifier = Modifier.padding(start = 8.dp)) {
//                Text("• Navigation over app", color = Color.Gray)
//                Text("• Event information", color = Color.Gray)
//                Text("• Key places location in ASTU", color = Color.Gray)
//            }
//        }
//
//        // Input area
//        Row(
//            modifier = Modifier
//                .padding(8.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            TextField(
//                value = inputText,
//                onValueChange = { viewModel.inputText.value = it },
//                modifier = Modifier.weight(1f),
//                placeholder = { Text("Ask me anything...") },
////                colors = TextFieldDefaults.textFieldColors(
////                    containerColor = Color.White
////                )
//
//                colors = TextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White
//                )
//
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Button(
//                onClick = {
//                    viewModel.sendMessage("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")
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
//            ) {
//                Text("Send", color = Color.White)
//            }
//        }
//    }
//}


//
//
//@Composable
//fun ChatBotScreen(viewModel: ChatBotViewModel = viewModel()) {
//    val messages = viewModel.messages
//    val inputText by viewModel.inputText
//    val scrollState = rememberLazyListState()
//    val dateFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
//
//
//
//    val suggestions = listOf(
//        "Navigation over app",
//        "Event information",
//        "Key places location in ASTU",
//        "AR navigation usage",
//        "Anchor finding and usage"
//    )
//
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//    ) {
//
//        LazyColumn(
//            state = scrollState,
//            modifier = Modifier
//                .weight(1f)
//                .padding(8.dp),
//            verticalArrangement = Arrangement.Bottom
//        ) {
//            items(messages) { msg ->
//                val isUser = msg.sender == "You"
//                val timestamp = dateFormat.format(Date())
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
//                ) {
//                    if (!isUser) {
//                        Image(
//                            painter = painterResource(R.drawable.bot_icon), // Add bot_icon.png to res/drawable
//                            contentDescription = "Bot Avatar",
//                            modifier = Modifier
//                                .size(32.dp)
//                                .clip(CircleShape)
//                                .padding(end = 4.dp)
//                        )
//
//                    }
//
//                    Column(
//                        modifier = Modifier
//                            .background(
//                                if (isUser) Color(0xFFDCF8C6) else Color.White,
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                            .padding(12.dp)
//                            .widthIn(max = 250.dp)
//                    ) {
//                        Text(
//                            msg.content,
//                            color = Color.Black
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Text(
//                            timestamp,
//                            fontSize = 10.sp,
//                            color = Color.Gray
//                        )
//                    }
//
//                    if (isUser) {
//                        Image(
//                            painter = painterResource(R.drawable.user_icon), // Add user_icon.png to res/drawable
//                            contentDescription = "User Avatar",
//                            modifier = Modifier
//                                .size(32.dp)
//                                .clip(CircleShape)
//                                .padding(start = 4.dp)
//                        )
//                    }
//                }
//            }
//        }
//
//        Divider()
//
//        // Suggestions section
//        Column(
//            modifier = Modifier
//                .padding(horizontal = 12.dp, vertical = 6.dp)
//        ) {
//            Text(
//                text = "🤖 What can I help you with?",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.DarkGray
//            )
//            Spacer(modifier = Modifier.height(6.dp))
//
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                suggestions.forEach { suggestion ->
//                    AssistChip(
//                        onClick = { viewModel.inputText.value = suggestion },
//                        label = { Text(suggestion, fontSize = 12.sp) }
//                    )
//                }
//            }
//        }
//
//        // Input Row
//        Row(
//            modifier = Modifier
//                .padding(8.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            TextField(
//                value = inputText,
//                onValueChange = { viewModel.inputText.value = it },
//                modifier = Modifier.weight(1f),
//                placeholder = { Text("Ask me anything...") },
//                colors = TextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White
//                )
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Button(
//                onClick = {
//                    viewModel.sendMessage("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")
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
//            ) {
//                Text("Send", color = Color.White)
//            }
//        }
//    }
//}
//
//



@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatBotScreen(viewModel: ChatBotViewModel = viewModel()) {
    val messages = viewModel.messages
    val inputText by viewModel.inputText
    //val isTyping by viewModel.isTyping
    val scrollState = rememberLazyListState()
    val dateFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    val suggestions = listOf(
        "Navigation over app",
        "Event information",
        "Key places location in ASTU",
        "AR navigation usage",
        "Anchor finding and usage"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            items(messages) { msg ->
                val isUser = msg.sender == "You"
                val timestamp = dateFormat.format(Date(msg.timestamp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    if (!isUser) {
                        Image(
                            painter = painterResource(R.drawable.bot_icon),
                            contentDescription = "Bot Avatar",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .padding(end = 4.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .background(
                                if (isUser) Color(0xFFDCF8C6) else Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                            .widthIn(max = 250.dp)
                    ) {
                        Text(msg.content, color = Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(timestamp, fontSize = 10.sp, color = Color.Gray)
                    }

                    if (isUser) {
                        Image(
                            painter = painterResource(R.drawable.user_icon),
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .padding(start = 4.dp)
                        )
                    }
                }
            }

//            // Typing animation
//            if (isTyping) {
//                item {
//                    Row(modifier = Modifier.padding(12.dp)) {
//                        CircularProgressIndicator(
//                            strokeWidth = 2.dp,
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text("Bot is typing...", fontSize = 14.sp, color = Color.Gray)
//                    }
//                }
//            }
        }

        Divider()

        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "🤖 What can I help you with?",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                suggestions.forEach { suggestion ->
                    AssistChip(
                        onClick = { viewModel.inputText.value = suggestion },
                        label = { Text(suggestion, fontSize = 12.sp) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { viewModel.inputText.value = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask me anything...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    viewModel.sendMessage("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")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Send", color = Color.White)
            }
        }
    }
}
