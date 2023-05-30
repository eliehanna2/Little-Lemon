package com.hanna.littlelemon.composables

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hanna.littlelemon.ui.theme.PrimaryGreen
import com.hanna.littlelemon.ui.theme.PrimaryYellow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.hanna.littlelemon.*
import com.hanna.littlelemon.R
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.hanna.littlelemon.MenuViewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import coil.compose.rememberImagePainter
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage

@Composable
fun Home(navController: NavController){
    HomePage(navController = navController)
}

@Composable
fun HomePage(navController: NavController) {

    val context = LocalContext.current
    val viewModel: MenuViewModel = viewModel()
    val databaseMenuItems = viewModel.getAllDatabaseMenuItems().observeAsState(emptyList()).value
    val searchPhrase = remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        viewModel.fetchMenuDataIfNeeded()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(50.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate(com.hanna.littlelemon.Profile.route)
                        }
                )
            }
        }

        item{ Spacer(modifier = Modifier.height(16.dp)) }
        //HeroSection

        item{
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = PrimaryGreen)
                        .padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text(
                            text = "Little Lemon", style = MaterialTheme.typography.h4,
                            color = PrimaryYellow, fontWeight = FontWeight.Bold
                        )
                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                            Text(
                                text = "Chicago", style = MaterialTheme.typography.h5,
                                color = Color.White, fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text =
                                "We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                                style = MaterialTheme.typography.body1,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }

                    Column(verticalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(R.drawable.hero_image),
                            contentDescription = "Hero Image",
                            modifier = Modifier.fillMaxWidth(0.6f)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                }
            }
        }
        // Search TextField
        item{
            TextField(
                value = searchPhrase.value,
                onValueChange = { searchPhrase.value = it },
                placeholder = { Text("Enter search phrase") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = ""
                    )
                }
            )
        }
        item{ Spacer(modifier = Modifier.width(16.dp)) }
        // Filtered MenuItems
        val filteredMenuItems = if (searchPhrase.value.isBlank()) {
            databaseMenuItems
        } else {
            databaseMenuItems.filter { menuItem ->
                menuItem.title.contains(searchPhrase.value, ignoreCase = true)
            }
        }
        item{ MenuItems(menuItems = filteredMenuItems, context) }

    }

}


@Composable
fun MenuItems(menuItems: List<MenuItemRoom>, context: Context) {
    Spacer(modifier = Modifier
        .width(20.dp)
        .padding(top = 10.dp, bottom = 10.dp))
    Column {
        for (menuItem in menuItems) {
            MenuItem(item = menuItem, context)
        }
    }
}

@Composable
fun MenuItem(item: MenuItemRoom, context: Context) {
    Spacer(modifier = Modifier.width(10.dp))
    Divider(color = Color.Gray, thickness = 1.dp)
    Spacer(modifier = Modifier.width(10.dp))
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(modifier = Modifier.fillMaxWidth(0.5f)) {
            Text(text = item.title, fontWeight = FontWeight.Bold)
            Text(text = "$  ${item.price}")
            Text(text = item.description)
        }
        Column {
            Spacer(modifier = Modifier.width(10.dp))
            AsyncImage(
                model = item.image,
                contentDescription = null,
                modifier = Modifier.size(100.dp, 100.dp),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(10.dp))
        }

    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview(){
    Home(rememberNavController())
}
