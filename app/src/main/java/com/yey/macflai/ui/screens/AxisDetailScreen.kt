package com.yey.macflai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yey.macflai.ui.theme.*

data class AxisThemeConfig(
    val title: String,
    val subtitle: String,
    val bgColor: Color,
    val primaryColor: Color,
    val lightPrimaryColor: Color,
    val topic1Title: String,
    val topic2Title: String,
    val buttonText: String,
    val buttonColor: Color
)

fun getThemeConfig(axisId: String): AxisThemeConfig {
    return when {
        axisId.contains("Literal", ignoreCase = true) -> AxisThemeConfig(
            title = "EJE 2: LECTURA & CONTEXTO",
            subtitle = "THEME: FRESH GREENS AND LIME. FOCUS ON COMPRENSIÓN",
            bgColor = Color(0xFFE8F5E9),
            primaryColor = Color(0xFF2E7D32),
            lightPrimaryColor = Color(0xFFC8E6C9),
            topic1Title = "Identificar Ideas",
            topic2Title = "Análisis Crítico",
            buttonText = "IR A PRÁCTICA",
            buttonColor = Color(0xFF00E676)
        )
        axisId.contains("Inferencial", ignoreCase = true) -> AxisThemeConfig(
            title = "EJE 3: IDEAS & ESCRITURA",
            subtitle = "FOCUS: ESTRUCTURA Y ESTRUCTURA\ntheme: warm orango, puryota, and pink mix",
            bgColor = Color(0xFFFCE4EC),
            primaryColor = Color(0xFFC2185B),
            lightPrimaryColor = Color(0xFFF8BBD0),
            topic1Title = "Coherencia y Cohesión",
            topic2Title = "Argumentación",
            buttonText = "GENERAR ENSAYO",
            buttonColor = Color(0xFFE040FB)
        )
        else -> AxisThemeConfig( // Por defecto: Eje 1
            title = "EJE 1: PALABRAS & SIGNIFICADO",
            subtitle = "THEME: SUB-PATH EN LENGUAJE.",
            bgColor = SoftBlue,
            primaryColor = DarkBlue,
            lightPrimaryColor = Color(0xFFE3F2FD),
            topic1Title = "Etimología",
            topic2Title = "Sinónimos Contextuales",
            buttonText = "VER LECCIÓN",
            buttonColor = Color(0xFF536DFE)
        )
    }
}

@Composable
fun AxisDetailScreen(
    axisId: String,
    onStartJourney: () -> Unit,
    onBack: () -> Unit
) {
    val config = getThemeConfig(axisId)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(config.bgColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = config.title,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = config.primaryColor
                    )
                    Text(
                        text = config.subtitle,
                        fontSize = 12.sp,
                        color = TextGray
                    )
                }
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color.White)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = config.primaryColor)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            TopicCard(
                title = config.topic1Title, 
                icon = Icons.Default.Create, 
                primaryColor = config.primaryColor, 
                lightColor = config.lightPrimaryColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            TopicCard(
                title = config.topic2Title, 
                icon = Icons.Default.Search, 
                primaryColor = config.primaryColor, 
                lightColor = config.lightPrimaryColor
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Face, contentDescription = "Robot", tint = config.primaryColor, modifier = Modifier.size(64.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Score", color = TextGray, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("55%", color = config.primaryColor, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFE0E0E0))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Compleción", color = TextGray, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("30", color = config.primaryColor, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onStartJourney,
                        colors = ButtonDefaults.buttonColors(containerColor = config.buttonColor),
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(config.buttonText, color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TopicCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, primaryColor: Color, lightColor: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(lightColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = primaryColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, color = primaryColor, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Ir", tint = TextGray)
        }
    }
}
