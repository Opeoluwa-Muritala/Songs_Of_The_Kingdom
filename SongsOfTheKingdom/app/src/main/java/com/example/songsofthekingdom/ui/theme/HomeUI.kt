package com.example.songsofthekingdom.ui.theme

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.songsofthekingdom.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp

@Composable
fun HomePage(){


    val LightBlue = Color(179,205,245)
    Scaffold(
        topBar = {TopAppBar(
            backgroundColor = LightBlue,
            title = { Text(
                "Songs Of The Kingdom",
                color = Color.Black
            ) },
            navigationIcon = {
                IconButton(
                    onClick = { TODO("working out how to use the search button")
                    },
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.search),
                            contentDescription = "Search Button",
                            tint = Color.Black
                        )
                    }
                )
            },
            actions = {
                IconButton(onClick = { TODO("working out what would be in the menu")},
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription= "Menu button",
                            tint = Color.Black)
                    })
            }
        )},
    ){
        val painter = painterResource(R.drawable.musicnotes)
        Box {
            Image(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                ,
                painter = painter ,
                alpha = 0.2F,
                contentDescription = "Music notes in background",
            contentScale = ContentScale.FillBounds)
                Box {
                    Songs()
                }

                }
            }
        }
@Composable
fun Songs(){
    val listofsongs = listOfSongs()
    val getALlData = listofsongs.getAllData()

    LazyColumn(
        contentPadding = PaddingValues(all= 5.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(items = getALlData){ song ->
             SongView(song = song)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview(){
    HomePage()
}