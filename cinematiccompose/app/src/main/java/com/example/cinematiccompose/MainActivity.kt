package com.example.cinematiccompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Dữ liệu phim
data class Movie(
    val title: String,
    val description: String,
    val imageRes: Int
)

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var showDetail by remember { mutableStateOf(false) }
            var selectedMovie by remember { mutableStateOf<Movie?>(null) }

            Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
                // ⚡ Animation chuyển cảnh có EaseOut
                AnimatedContent(
                    targetState = showDetail,
                    transitionSpec = {
                        slideInHorizontally(
                            animationSpec = tween(durationMillis = 400, easing = EaseOut)
                        ) togetherWith slideOutHorizontally(
                            animationSpec = tween(durationMillis = 400, easing = EaseOut)
                        )
                    },
                    label = ""
                ) { state ->
                    if (!state) {
                        MovieListScreen(onMovieClick = {
                            selectedMovie = it
                            showDetail = true
                        })
                    } else {
                        selectedMovie?.let {
                            MovieDetailScreen(movie = it, onBack = { showDetail = false })
                        }
                    }
                }
            }
        }
    }
}

// Màn hình danh sách phim
@Composable
fun MovieListScreen(onMovieClick: (Movie) -> Unit) {
    val movies = listOf(
        Movie("Interstellar", "A team of explorers travel through a wormhole in space.", R.drawable.interstellar),
        Movie("Dune", "A nobleman must travel to a dangerous desert planet.", R.drawable.dune),
        Movie("Inception", "A thief steals corporate secrets through dreams.", R.drawable.inception)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎬 CINEMATIC",
            color = Color(0xFF1DB954),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        movies.forEach { movie ->
            MovieCard(movie = movie, onClick = { onMovieClick(movie) })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Thẻ phim
@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit) {
    Surface(
        color = Color(0xFF1A1A1A),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = movie.imageRes),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(movie.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(movie.description, color = Color.Gray, fontSize = 13.sp, maxLines = 2)
            }
        }
    }
}

// Màn hình chi tiết phim
@Composable
fun MovieDetailScreen(movie: Movie, onBack: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
    ) {
        // Nút Back
        TextButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("← Back", color = Color(0xFF1DB954), fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = movie.imageRes),
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(movie.title, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(movie.description, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { Toast.makeText(context, "Playing ${movie.title}", Toast.LENGTH_SHORT).show() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
            modifier = Modifier.fillMaxWidth(0.9f).align(Alignment.CenterHorizontally)
        ) {
            Text("▶ Play", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { Toast.makeText(context, "Added to Watchlist", Toast.LENGTH_SHORT).show() },
            border = BorderStroke(1.dp, Color.White),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.9f).align(Alignment.CenterHorizontally)
        ) {
            Text("+ Add to Watchlist")
        }
    }
}







