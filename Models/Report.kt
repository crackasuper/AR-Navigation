


data class Report(
    val message: String = "",
    val userEmail: String = "",
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
