package com.yey.macflai.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yey.macflai.ui.theme.*

@Composable
fun HomeScreen(onNavigateToAxis: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBlue)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "MI RUTA PAES",
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = DarkBlue
                    )
                    Text(
                        text = "LENGUAJE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = DarkBlue
                    )
                    Text(
                        text = "THEME: SOFT CYAN AND COOL GRAY",
                        fontSize = 12.sp,
                        color = TextGray
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = {}, modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color.White)) {
                        Icon(Icons.Default.Create, contentDescription = null, tint = DarkBlue)
                    }
                    IconButton(onClick = {}, modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0xFFFFF9C4))) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300))
                    }
                    IconButton(onClick = {}, modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color.White)) {
                        Icon(Icons.Default.Face, contentDescription = null, tint = DarkBlue)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                    val p1 = Offset(size.width * 0.2f, size.height * 0.3f)
                    val p2 = Offset(size.width * 0.4f, size.height * 0.7f)
                    val p3 = Offset(size.width * 0.7f, size.height * 0.1f)
                    val p4 = Offset(size.width * 0.85f, size.height * 0.4f)
                    
                    drawPath(
                        path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(p1.x, p1.y)
                            quadraticBezierTo(size.width * 0.3f, size.height * 0.9f, p2.x, p2.y)
                            quadraticBezierTo(size.width * 0.5f, size.height * 0.2f, p3.x, p3.y)
                            lineTo(p4.x, p4.y)
                        },
                        color = Color.White,
                        style = Stroke(width = 15f, pathEffect = pathEffect)
                    )
                }
                
                NodeItem(
                    offset = Offset(0.2f, 0.3f),
                    label = "Palabras y Significado",
                    subLabel = "3 MAIN AXES",
                    progress = "45%",
                    color = Color(0xFF29B6F6),
                    icon = Icons.Default.Create,
                    onClick = onNavigateToAxis
                )

                NodeItem(
                    offset = Offset(0.4f, 0.7f),
                    label = "Comprensión Literal",
                    subLabel = "MALLO AXS 3",
                    progress = "45%",
                    color = Color(0xFF69F0AE),
                    icon = Icons.Default.Create,
                    onClick = onNavigateToAxis
                )

                NodeItem(
                    offset = Offset(0.7f, 0.1f),
                    label = "Comprensión Inferencial",
                    subLabel = "Current progress",
                    progress = "45%",
                    color = Color(0xFF00B0FF),
                    icon = Icons.Default.Star,
                    onClick = onNavigateToAxis
                )
            }
        }
        
        Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 90.dp)) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Face, contentDescription = "Face", tint = DarkBlue, modifier = Modifier.size(32.dp))
                    Row {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300))
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300))
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.LightGray)
                    }
                    Surface(
                        color = Color(0xFFFFE0B2),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("Streak: \uD83D\uDD25 4", color = Color(0xFFE65100), modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        
        Button(
            onClick = { onNavigateToAxis("Palabras y Significado") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).height(56.dp).fillMaxWidth(0.6f),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("START JOURNEY →", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
        }
    }
}

@Composable
fun BoxScope.NodeItem(
    offset: Offset,
    label: String,
    subLabel: String,
    progress: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(
                x = (LocalConfiguration.current.screenWidthDp * offset.x - 30).dp,
                y = (LocalConfiguration.current.screenHeightDp * offset.y - 120).dp
            )
            .clickable { onClick(label) }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(color.copy(alpha = 0.2f), CircleShape)
                    .padding(8.dp)
                    .background(color, CircleShape)
                    .padding(8.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 2.dp
            ) {
                Text(text = progress, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
            }
            Text(subLabel, color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}
