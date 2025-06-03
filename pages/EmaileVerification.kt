



import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


import com.google.firebase.auth.FirebaseAuth
import de.morhenn.ar_navigation.AuthViewModel

import kotlinx.coroutines.delay

@Composable
fun EmailVerificationPage(

    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel

)
{
    val user = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    var isSending by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    // Send the first verification email on screen load
    LaunchedEffect(Unit) {
        if (user != null && !user.isEmailVerified) {
            user.sendEmailVerification()
            message = "Verification email sent to ${user.email}"
        }
    }

    // Auto-check verification every few seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000) // check every 5 seconds
            user?.reload()?.addOnSuccessListener {
                if (user.isEmailVerified) {
                    Toast.makeText(context, "Email verified! Logging in...", Toast.LENGTH_SHORT).show()
                    navController.navigate("login") {
                        popUpTo("email_verification") { inclusive = true }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Email Verification", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(message)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                isSending = true
                user?.sendEmailVerification()?.addOnCompleteListener {
                    isSending = false
                    message = if (it.isSuccessful) {
                        "Verification email resent."
                    } else {
                        "Failed to resend verification email."
                    }
                }
            },
            enabled = !isSending
        ) {
            Text(if (isSending) "Sending..." else "Resend Verification Email")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("You will be redirected once your email is verified.", fontSize = 14.sp, color = Color.Gray)
    }
}
