data class ChatMessage(
//    val role: String,  // "user" or "assistant"
    val content: String =  "",
    val sender: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
