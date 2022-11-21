package com.example.songsofthekingdom.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SongView(
    song: Song,
) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .border(BorderStroke(2.dp, Color.Black))
                .clickable { TODO("I Haven't Built the Page For the Song Yet") },
        ) {

            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                text = "${song.text}",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopEnd)
                    .padding(15.dp),
                text = "${song.number}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black

            )

        }
    }

