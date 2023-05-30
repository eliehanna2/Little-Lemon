package com.hanna.littlelemon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hanna.littlelemon.composables.*

@Composable
fun NavigationComposable(context: Context,navController: NavHostController) {
    NavHost(navController = navController,
        startDestination = determineStartDestination(context)
    ) {
        composable(Home.route) {
            Home(navController)
        }
        composable(Profile.route) {
            Profile(navController)
        }
        composable(Onboarding.route) {
            Onboarding(navController)
        }
    }
}

private fun determineStartDestination(context: Context): String {
    //check if user data is stored in shared preferences and return the appropriate start destination
    // For example, if user data exists, returns Onboarding
    // Otherwise, returns Home as the default start destination
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)


    if (sharedPreferences.getBoolean("userRegistered", false)) {
        return Home.route
    }
    else{
        return Onboarding.route
    }
}