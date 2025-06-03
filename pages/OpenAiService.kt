



import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

object OpenAIService {
    private val client = OkHttpClient()
    private const val API_KEY = "sk-xxxxxxxxxxxxxxxxxx"
    private const val URL = "https://api.openai.com/v1/chat/completions"

    suspend fun getChatResponse(prompt: String): String? = withContext(Dispatchers.IO) {
        val jsonBody = """
            {
              "model": "gpt-3.5-turbo",
              "messages": [{"role": "user", "content": "$prompt"}]
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(URL)
            .addHeader("Authorization", "Bearer $API_KEY")
            .addHeader("Content-Type", "application/json")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody))
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val body = response.body?.string()
            val json = JSONObject(body)
            return@withContext json.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
        } else null
    }
}
