package com.example.songsofthekingdom.ui.theme

import android.widget.Toast
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SongView(
    song: Song,
    modifier : Modifier = Modifier,
    text_color: Color = Color.Black,
    line_color: Color = Color.Black,
    size:Int = 15,
    text_size: TextUnit = size.sp,
) {
    Row {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(2.dp)
                .border(BorderStroke(2.dp, line_color))
                .clickable{},
        ) {

            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = "${song.text}",
                fontSize = text_size,
                fontWeight = FontWeight.SemiBold,
                color = text_color
            )

            Text(
                modifier = Modifier.fillMaxSize().align(Alignment.CenterEnd),
                text = "${song.number}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = text_color

            )

        }
    }
}

@Composable
fun Song(
    song:view){
    Text(text = "${song.chorus}")
    Text(text = "${song.verse}")

}

