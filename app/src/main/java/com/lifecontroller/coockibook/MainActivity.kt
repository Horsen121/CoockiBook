package com.lifecontroller.coockibook

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lifecontroller.coockibook.ui.theme.CoockiBookTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CoockiBookTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BoxWithConstraints {
                        if (maxWidth < 600.dp) {
                            PhoneView(context = applicationContext)
                        } else {
                            TabletView(context = applicationContext)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhoneView(
    context: Context
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "posts"
    ) {
        composable(route = "posts") {
            Posts(navController, context)
        }
        composable(
            route = "post" + "?postId={postId}",
            arguments = listOf(
                navArgument(name = "postId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val id = it.arguments?.getLong("postId") ?: -1L
            if (id != -1L) {
                val post = DB.getInstance(context).dao.getPostById(id) ?: Post(title = "", description = "")
                PostView(navController = navController, post = post)
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TabletView(
    context: Context
) {
    val posts = mutableStateOf( DB.getInstance(context).dao.getPosts() )
    var post by remember { mutableStateOf("Default") }

    Row(
        Modifier.fillMaxSize()
    ) {
        val modifier = Modifier.fillMaxHeight()
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth(0.4f)
                .padding(16.dp)
        ) {
            items(posts.value) {
                Text(
                    text = it.title,
                    modifier = Modifier.clickable { post = it.description }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Text(
            text = post,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp)
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Posts(
    navController: NavController,
    context: Context
) {
    val posts = mutableStateOf( DB.getInstance(context).dao.getPosts() )

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(posts.value) {
            Text(
                text = it.title,
                modifier = Modifier.clickable { navController.navigate("post?postId=${it.id}") }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@Composable
fun PostView(
    navController: NavController,
    post: Post
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = post.description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Button(onClick = { navController.navigate("posts") }) {
            Text(text = "Back to MainScreen")
        }
    }
}