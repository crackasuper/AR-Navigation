

package de.morhenn.ar_navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.morhenn.ar_navigation.pages.AboutScreen
import de.morhenn.ar_navigation.pages.AdminPage
import de.morhenn.ar_navigation.pages.ChatBotScreen
import de.morhenn.ar_navigation.pages.EmailVerificationPage
import de.morhenn.ar_navigation.pages.LandingPage
import de.morhenn.ar_navigation.pages.LoginPage
import de.morhenn.ar_navigation.pages.PasswordManagementScreen
import de.morhenn.ar_navigation.pages.ProfileSettings
import de.morhenn.ar_navigation.pages.ReportScreen
import de.morhenn.ar_navigation.pages.StartPage
import de.morhenn.ar_navigation.pages.SignupPage
import de.morhenn.ar_navigation.pages.faqScreen
import de.morhenn.ar_navigation.pages.info


@Composable
fun MyApplicationNav(
    modifier: Modifier = Modifier, authViewModel: AuthViewModel
) {


    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start", builder = {
        composable("start"){
            StartPage(modifier, navController, authViewModel)
        }
        composable("login"){

            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup"){
            SignupPage(modifier, navController, authViewModel)
        }
        composable("home"){
            LandingPage(modifier, navController, authViewModel)
        }
        composable("admin"){
            AdminPage(modifier, navController, authViewModel)
        }
        composable("info") {
            info(modifier, navController, authViewModel)
        }

        composable("landing") {
            LandingPage(modifier, navController, authViewModel)
        }

        composable("profile") {
            ProfileSettings(modifier, navController, authViewModel)
        }

        composable("password") {
            PasswordManagementScreen(modifier, navController, authViewModel)
        }

        composable("faq") {
            faqScreen(modifier, navController, authViewModel)
        }

        composable("report") {
            ReportScreen(modifier,navController, authViewModel)
        }

        composable("about") {
            AboutScreen(modifier, navController, authViewModel)
        }

        composable("chatbot"){
            ChatBotScreen()

        }

        composable("email_verification") {
            EmailVerificationPage(modifier, navController, authViewModel)
        }


    })


}
