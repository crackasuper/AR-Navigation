

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment


import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.morhenn.ar_navigation.AuthViewModel
import de.morhenn.ar_navigation.R
//
@androidx.compose.runtime.Composable
fun StartPage(modifier: Modifier, navController: NavHostController, authViewModel: AuthViewModel) {



    Box(
        modifier = Modifier
             .fillMaxSize()
             .background(Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(40.dp))

                // Top logo: ASTU
                Image(
                    painter = painterResource(id = R.drawable.astu),
                    contentDescription = "ASTU Logo",
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Welcome text
                Text(
                    text = "Welcome to ASTU\n Navigating with AR !",
                    fontSize = 22.sp

                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mid image as background visual
                Image(
                    painter = painterResource(id = R.drawable.front),
                    contentDescription = "Navigation Illustration",
                    modifier = Modifier
                        .height(400.dp)
                        .background(Color.White)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = "   Discover new  way to navigate our\n" +
                            "university with our Augmented Reality \n" +
                            "             (AR) navigation system",
                    fontSize = 16.sp
                )
            }
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E55EF))
            ) {
                Text("Get Started", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }


}
