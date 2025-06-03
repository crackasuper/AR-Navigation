



import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

import org.json.JSONObject

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

import java.io.IOException

class ChatBotViewModel : ViewModel() {

    val isBotTyping = mutableStateOf(false)

    var messages = mutableStateListOf<ChatMessage>()
        private set

    var inputText = mutableStateOf("")

    fun addMessage(sender: String, content: String) {
        val message = de.morhenn.ar_navigation.model.ChatMessage(sender, content, System.currentTimeMillis())
        messages.add(ChatMessage(sender, content))
        saveMessageToFirebase(message)
    }

    fun sendMessage(apiKey: String) {
        val question = inputText.value.trim()
        if (question.isEmpty()) return

        addMessage("You", question)
        inputText.value = ""

        isBotTyping.value = true // Start typing


        checkFirebaseForAnswer(question) { firebaseAnswer ->
            if (!firebaseAnswer.isNullOrEmpty() && firebaseAnswer != "null") {
                addMessage("Bot", firebaseAnswer)
            } else {
               callOpenAI(apiKey, question)
                //callOpenRouter(apiKey, question)
            }
        }
    }






    private fun checkFirebaseForAnswer(question: String, onResult: (String?) -> Unit) {
        val db = FirebaseDatabase.getInstance().getReference("faq")

        db.get().addOnSuccessListener { snapshot ->
            val questionLower = question.lowercase()

            for (entry in snapshot.children) {
                val key = entry.key?.lowercase() ?: continue
                if (questionLower.contains(key)) {
                    onResult(entry.getValue(String::class.java))
                    return@addOnSuccessListener
                }
            }

            onResult(null) // No match
        }.addOnFailureListener {
            onResult(null)
        }

    }



    private fun callOpenAI(apiKey: String, prompt: String) {
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaType()

        val requestBody = """
        {
            "model": "openai/gpt-3.5-turbo",
            "messages": [{"role": "user", "content": "$prompt"}]
        }
    """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://openrouter.ai/api/v1/chat/completions")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .addHeader("HTTP-Referer", "https://yourapp.com") // Optional but recommended
            .addHeader("X-Title", "CampusBot")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                addMessage("Bot", "Sorry, something went wrong.")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    try {
                        val json = JSONObject(it)
                        val reply = json.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content")
                        addMessage("Bot", reply.trim())
                    } catch (e: Exception) {
                        addMessage("Bot", "Sorry, I couldn't parse the response.")
                        Log.e("OpenRouterError", "${e.message}")
                    }
                }
                isBotTyping.value = false
            }
        })
    }

    fun saveMessageToFirebase(message: de.morhenn.ar_navigation.model.ChatMessage) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid/chatHistory")

        ref.push().setValue(message)
    }



}




@Composable
fun TypingAnimationBubble() {
    var dotCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            dotCount = (dotCount + 1) % 4
        }
    }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text("Bot is typing" + ".".repeat(dotCount), color = Color.Gray)
    }
}


data class ChatMessage(
    val sender: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

