package com.yey.macflai.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yey.macflai.ui.theme.*
import com.yey.macflai.viewmodel.SinclairUiState
import com.yey.macflai.viewmodel.SinclairViewModel
import androidx.compose.foundation.text.selection.SelectionContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(viewModel: SinclairViewModel, axisId: String, onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState is SinclairUiState.Idle) {
            viewModel.startChallenge(axisId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Desafío: $axisId", color = DarkBlue, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.resetState(); onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = DarkBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SoftBlue)
            )
        },
        containerColor = SoftBlue
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = uiState) {
                is SinclairUiState.Idle -> {
                    // Esperando comando
                }
                is SinclairUiState.CustomLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = DarkBlue, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Sinclair está analizando textos de alta complejidad...",
                            color = TextGray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = { viewModel.startChallenge(axisId) },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
                is SinclairUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                            ) {
                                SelectionContainer {
                                    Text(
                                        text = state.desafio.texto,
                                        color = TextGray,
                                        fontSize = 16.sp,
                                        lineHeight = 26.sp,
                                        textAlign = TextAlign.Justify,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Título de la pregunta (Mapeo de pregunta)
                            Text(
                                text = state.desafio.pregunta,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = DarkBlue,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        // Mapeo de opciones (Map<String, String>)
                        val opcionesList = state.desafio.opciones.entries.toList()
                        items(opcionesList) { option ->
                            val isSelected = option.key == state.opcionSeleccionada
                            val isAnswered = state.opcionSeleccionada != null
                            
                            val isCorrectAnswer = option.key == state.desafio.respuestaCorrecta

                            val targetBackgroundColor = when {
                                isSelected && state.esCorrecta == true -> Color(0xFFC8E6C9)
                                isSelected && state.esCorrecta == false -> Color(0xFFFFCDD2)
                                isAnswered && isCorrectAnswer -> Color(0xFFC8E6C9)
                                else -> Color.White
                            }

                            val targetBorderColor = when {
                                isSelected && state.esCorrecta == true -> Color(0xFF4CAF50)
                                isSelected && state.esCorrecta == false -> Color(0xFFF44336)
                                isAnswered && isCorrectAnswer -> Color(0xFF4CAF50)
                                else -> Color.LightGray
                            }

                            val backgroundColor by animateColorAsState(targetValue = targetBackgroundColor, label = "bg_color")
                            val borderColor by animateColorAsState(targetValue = targetBorderColor, label = "border_color")

                            OutlinedCard(
                                onClick = { 
                                    if (!isAnswered) {
                                        viewModel.enviarRespuesta(option.key)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, borderColor),
                                colors = CardDefaults.outlinedCardColors(containerColor = backgroundColor)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isSelected) DarkBlue else SoftBlue,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = option.key,
                                                color = if (isSelected) Color.White else DarkBlue,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = option.value,
                                        color = if (isSelected) DarkBlue else TextGray,
                                        fontSize = 15.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    if (state.opcionSeleccionada != null) {
                        ModalBottomSheet(
                            onDismissRequest = { 
                                viewModel.resetState()
                                onBack() 
                            },
                            containerColor = Color.Transparent,
                            dragHandle = null
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.9f)
                                    .background(
                                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF81D4FA), // Blue top
                                                Color(0xFFFFF59D), // Yellow mid
                                                Color(0xFFFFCC80), // Orange lower
                                                Color(0xFFFFCCBC)  // Warm peach bottom
                                            )
                                        ),
                                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                                    )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "MÓDULO ACTIVE: RETROALIMENTACIÓN & EVALUACIÓN",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp,
                                        color = DarkBlue,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Theme: Combined, celebratory mix of all colors",
                                        fontSize = 12.sp,
                                        color = TextGray
                                    )
                                    
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text("Results", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkBlue)
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text("9.1", fontWeight = FontWeight.Black, fontSize = 64.sp, color = Color.White)
                                        Text("/10", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.White, modifier = Modifier.padding(bottom = 12.dp))
                                    }

                                    Spacer(modifier = Modifier.height(32.dp))

                                    // Feedback Card
                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4).copy(alpha = 0.9f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Face, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = if (state.esCorrecta == true) "Evaluación de Sinclair (Positiva)" else "Corrección de Sinclair",
                                                    fontWeight = FontWeight.Bold,
                                                    color = DarkBlue
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            if (state.respuesta == null) {
                                                CircularProgressIndicator(color = DarkBlue, modifier = Modifier.align(Alignment.CenterHorizontally))
                                                Text("Cargando análisis profundo...", modifier = Modifier.align(Alignment.CenterHorizontally), color = TextGray)
                                            } else {
                                                Text(
                                                    text = state.respuesta,
                                                    color = TextGray,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Detailed Breakdown Card
                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4).copy(alpha = 0.9f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text("Detailed breakdown by skill:", fontWeight = FontWeight.Bold, color = DarkBlue)
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text("Lectura", color = TextGray)
                                                Text("72%", fontWeight = FontWeight.Bold, color = DarkBlue)
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text("Comprensión", color = TextGray)
                                                Text("80%", fontWeight = FontWeight.Bold, color = DarkBlue)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    Button(
                                        onClick = { 
                                            viewModel.resetState()
                                            onBack()
                                        },
                                        modifier = Modifier.fillMaxWidth().height(56.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF448AFF)),
                                        shape = RoundedCornerShape(28.dp)
                                    ) {
                                        Text("SIGUIENTE DESAFÍO", fontSize = 16.sp, fontWeight = FontWeight.Black)
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
