package com.example.mybitcoinportolioapp.presentation.global_components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybitcoinportolioapp.presentation.ui.theme.FontFamilies


@Composable
fun CustomButton(
    buttonText: String,
    buttonColor: Color,
    painter: Painter,
    buttonOnClick: () -> Unit,
    modifier: Modifier
){
    Button(
        onClick = buttonOnClick,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        modifier = modifier,
        elevation = ButtonDefaults.elevatedButtonElevation(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Button Text
            Text(
                text = buttonText,
                fontSize = 20.sp,
                fontFamily = FontFamilies.fontFamily1,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text

            // Icon
            Image(
                painter = painter,
                contentDescription = "$buttonText Icon",
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(Color.Black)
            )
        }
    }
}