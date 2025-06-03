



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.morhenn.ar_navigation.model.Report
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import com.google.firebase.database.FirebaseDatabase

//for admin email verification


class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()

    val authState: LiveData<AuthState> = _authState

    val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser
    val currentUserEmail: String?
        get() = FirebaseAuth.getInstance().currentUser?.email


    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    fun setUserEmail(email: String) {
        _userEmail.value = email
    }



    init {
        CheckAuthStatus()
    }

    fun CheckAuthStatus(){
        if(auth.currentUser != null){
            _authState.value = AuthState.Authenticated
        }else{
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun login(email : String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and password cannot be empty.")
            return
        }

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
            if(task.isSuccessful){
                _authState.value = AuthState.Authenticated
            }else{
                _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong.")
            }

        }


    }


//    fun signup(email : String, password: String){
//
//        if(email.isEmpty() || password.isEmpty()){
//            _authState.value = AuthState.Error("Email and password cannot be empty.")
//            return
//        }
//
//        _authState.value = AuthState.Loading
//
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->
//            if(task.isSuccessful){
//                _authState.value = AuthState.Authenticated
//            }else{
//                _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong.")
//            }
//
//        }
//
//
//    }


//    fun signup(email: String, password: String) {
//        _authState.value = AuthState.Loading
//
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val user = auth.currentUser
//                    user?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
//                        if (verifyTask.isSuccessful) {
//                            _authState.value = AuthState.EmailVerificationSent
//                        } else {
//                            _authState.value = AuthState.Error("Failed to send verification email.")
//                        }
//                    }
//                } else {
//                    _authState.value = AuthState.Error(task.exception?.message ?: "Signup failed")
//                }
//            }
//    }

    fun signup(email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                        if (verifyTask.isSuccessful) {
                            _authState.value = AuthState.EmailVerificationSent
                        } else {
                            _authState.value = AuthState.Error("Failed to send verification email")
                        }
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Signup failed")
                }
            }
    }



    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }


    fun changePassword(
        oldPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        if (user != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, oldPassword)
            user.reauthenticate(credential)
                .addOnSuccessListener {
                    user.updatePassword(newPassword)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { exception -> onFailure(exception) }
                }
                .addOnFailureListener { exception -> onFailure(exception) }
        } else {
            onFailure(Exception("User not authenticated"))
        }
    }

    fun resetPassword(email: String, callback: (Boolean, String?) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun getCurrentUsers(): FirebaseUser? {
        return auth.currentUser
    }

//    fun logout() {
//        auth.signOut()
//        _authState.value = AuthState.Unauthenticated
//    }



}


//sealed class AuthState {
//    object Unauthenticated : AuthState()
//    object Authenticated : AuthState()
//    object Loading : AuthState()
//
//    data class Error(val message: String) : AuthState()
//
//}


//sealed class AuthState {
//    object Loading : AuthState()
//    object Authenticated : AuthState()
//    object Unauthenticated : AuthState()
//    object EmailVerificationSent : AuthState()
//    data class Error(val message: String) : AuthState()
//}


sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object EmailVerificationSent : AuthState()
    data class Error(val message: String) : AuthState()
    object Unauthenticated : AuthState()
}


fun submitReport(report: Report, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val reportsRef = database.getReference("reports")

    val newReportRef = reportsRef.push() // generates a unique key
    newReportRef.setValue(report)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
}







