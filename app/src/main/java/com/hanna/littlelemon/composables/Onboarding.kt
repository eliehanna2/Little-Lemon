package com.hanna.littlelemon.composables

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hanna.littlelemon.ui.theme.PrimaryGreen
import com.hanna.littlelemon.ui.theme.PrimaryYellow
import com.hanna.littlelemon.R

@Composable
fun Onboarding(navController: NavHostController){
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",

                modifier = Modifier
                    .size(100.dp)
            )
        }
        item{
            Row(
                modifier = Modifier
                    .background(color = PrimaryGreen)
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Let's get to know you",
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
            }
        }
        item{
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Personal Information",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
        item{
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
                                    showToast(
                                        context,
                                        "Registration unsuccessful. Please enter all data."
                                    )
                                } else {
                                    saveUserData(context, firstName, lastName, email)
                                    showToast(context, "Registration successful!")

                                    // Navigate to Home screen
                                    navController.navigate(com.hanna.littlelemon.Home.route)
                                }
                            },
                            border = BorderStroke(2.dp, PrimaryGreen),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = PrimaryYellow,
                                contentColor = PrimaryGreen
                            ),


                            ) {
                            Text(text = "Register", style = TextStyle(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview(){
    val navController = rememberNavController()
    Onboarding(navController = navController)
}
private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun saveUserData(context: Context, firstName: String, lastName: String, email: String) {
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    sharedPref.edit {
        putString("firstName", firstName)
        putString("lastName", lastName)
        putString("email", email)
        putBoolean("userRegistered", true)
    }
}