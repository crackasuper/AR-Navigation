


import de.morhenn.ar_navigation.model.ChatRequest
import de.morhenn.ar_navigation.model.ChatResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApiService {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    fun sendMessage(
        @Header("Authorization") auth: String,
        @Body request: ChatRequest
    ): Call<ChatResponse>
}
