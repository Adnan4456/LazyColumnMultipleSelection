package com.example.timerjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.timerjetpackcompose.ui.theme.TimerJetpackComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimerJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    color = MaterialTheme.colors.background
                ) {
                    singleItem()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun singleItem(){

    val list = listOf<String>("A","B","C","D","E","F")

    Box {
        // Remember our own LazyListState
        val listState = rememberLazyListState()
        // Remember a CoroutineScope to be able to launch
        val coroutineScope = rememberCoroutineScope()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState

        )
        {

            list.forEach{section ->
                run {

                    stickyHeader {

                        Text(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray)
                                .padding(5.dp),
                            text = "Section $section"
                        )
                    }
                    items(10) {
                        Text(text = "Item $it from the section $section")
                    }
                }
            }
        }

        // Show the button if the first visible item is past
        // the first item. We use a remembered derived state to
        // minimize unnecessary compositions
        val showButton by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }

        AnimatedVisibility(visible = showButton ,
        enter = fadeIn(),
            exit = fadeOut()
        ) {
            ScrollToTopButton(onClick = {
                coroutineScope.launch { listState.animateScrollToItem(5) }
            })
        }
    }
}



@Composable
private fun ScrollToTopButton(onClick: () -> Unit = {}){

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(50.dp),
        Alignment.BottomCenter){

        Button(onClick = { onClick() },
            modifier = Modifier.shadow(5.dp, shape = CircleShape)
            .clip(shape = CircleShape).size(65.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Green
            )
        ) {
            Icon(Icons.Filled.KeyboardArrowUp, "arrow up")

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TimerJetpackComposeTheme {
//        Greeting("Android")
        singleItem()
    }
}