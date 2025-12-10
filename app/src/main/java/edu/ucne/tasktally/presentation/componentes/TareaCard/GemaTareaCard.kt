package edu.ucne.tasktally.presentation.componentes.TareaCard

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.tasktally.R
import edu.ucne.tasktally.domain.models.TareaGema
import edu.ucne.tasktally.ui.theme.TaskTallyTheme

@Composable
fun GemaTareaCard(
    modifier: Modifier = Modifier,
    tarea: TareaGema,
    isProcessing: Boolean = false,
    onIniciarClick: (String) -> Unit = {},
    onCompletarClick: (String) -> Unit = {}
) {
    val imageResId = remember(tarea.nombreImgVector) {
        getDrawableResourceIdSafe(tarea.nombreImgVector)
    }

    val backgroundColor = when (tarea.estado.lowercase()) {
        "pendiente" -> MaterialTheme.colorScheme.primary
        "iniciada" -> MaterialTheme.colorScheme.secondary
        "completada" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
    ) {
        if (imageResId != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Imagen de tarea",
                    modifier = Modifier
                        .size(120.dp)
                        .offset(x = 20.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = tarea.estado.uppercase(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = tarea.titulo,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (tarea.descripcion.isNotBlank()) {
                    Text(
                        text = tarea.descripcion,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${tarea.puntos} puntos",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (tarea.estado.lowercase()) {
                    "pendiente" -> {
                        Button(
                            onClick = { onIniciarClick(tarea.tareaId) },
                            enabled = !isProcessing,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = backgroundColor
                            ),
                            modifier = Modifier.height(36.dp)
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = backgroundColor,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = if (isProcessing) "Iniciando..." else "Iniciar",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    "iniciada" -> {
                        Button(
                            onClick = { onCompletarClick(tarea.tareaId) },
                            enabled = !isProcessing,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = backgroundColor
                            ),
                            modifier = Modifier.height(36.dp)
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = backgroundColor,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            } else {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = if (isProcessing) "Completando..." else "Completar",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    "completada" -> {
                        Surface(
                            color = Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = backgroundColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "¡Completada!",
                                    color = backgroundColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getDrawableResourceIdSafe(imageName: String?): Int? {
//    if (imageName.isNullOrBlank()) return null
    return null
//
//    return try {
//        when (imageName) {
//            //maticas
//            "img0_yellow_tree" -> R.drawable.img0_yellow_tree
//            "img1_purple_vines" -> R.drawable.img1_purple_vines
//            "img2_little_bush" -> R.drawable.img2_little_bush
//            "img3_little_plant" -> R.drawable.img3_little_plant
//            "img5_purple_flower" -> R.drawable.img5_purple_flower
//            "img6_purple_plant" -> R.drawable.img6_purple_plant
//            "img7_green_tree" -> R.drawable.img7_green_tree
//            "img8_green_leaves" -> R.drawable.img8_green_leaves
//            "img9_color_leaves" -> R.drawable.img9_color_leaves
//
//            //objetos
//            "img10_batteries" -> R.drawable.img10_batteries
//            "img11_boxes" -> R.drawable.img11_boxes
//            "img12_calendar" -> R.drawable.img12_calendar
//            "img13_chocolate" -> R.drawable.img13_chocolate
//            "img14_clock" -> R.drawable.img14_clock
//            "img15_coffee_cup" -> R.drawable.img15_coffee_cup
//            "img16_coffee_machine" -> R.drawable.img16_coffee_machine
//            "img16_dishes" -> R.drawable.img16_dishes
//            "img17_doughnut" -> R.drawable.img17_doughnut
//            "img18_doughnut" -> R.drawable.img18_doughnut
//            "img19_files" -> R.drawable.img19_files
//            "img20_folder" -> R.drawable.img20_folder
//            "img21_food" -> R.drawable.img21_food
//            "img22_hamburguer" -> R.drawable.img22_hamburguer
//            "img23_ice_cream" -> R.drawable.img23_ice_cream
//            "img24_mobile_phone" -> R.drawable.img24_mobile_phone
//            "img25_notebook" -> R.drawable.img25_notebook
//            "img26_pancakes" -> R.drawable.img26_pancakes
//            "img27_pizza" -> R.drawable.img27_pizza
//            "img28_pizza_slice" -> R.drawable.img28_pizza_slice
//            "img29_pudding" -> R.drawable.img29_pudding
//            "img30_recycle_bin" -> R.drawable.img30_recycle_bin
//
//            else -> {
//                Log.w("GemaTareaCard", "Imagen no encontrada: $imageName")
//                null
//            }
//        }
//    } catch (e: Exception) {
//        Log.e("GemaTareaCard", "Error al cargar drawable: $imageName", e)
//        null
//    }
}

@Preview(showBackground = true)
@Composable
fun GemaTareaCardPreview() {
    TaskTallyTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GemaTareaCard(
                tarea = TareaGema(
                    tareaId = "1",
                    titulo = "Arreglar la habitación",
                    descripcion = "Organizar y limpiar toda la habitación",
                    estado = "pendiente",
                    puntos = 60,
                    nombreImgVector = "img0_yellow_tree"
                )
            )

            // Tarea iniciada
            GemaTareaCard(
                tarea = TareaGema(
                    tareaId = "2",
                    titulo = "Hacer la tarea de matemáticas",
                    descripcion = "Completar ejercicios del capítulo 5",
                    estado = "iniciada",
                    puntos = 45,
                    nombreImgVector = "img25_notebook"
                )
            )

            // Tarea completada
            GemaTareaCard(
                tarea = TareaGema(
                    tareaId = "3",
                    titulo = "Lavar los platos",
                    descripcion = "Lavar todos los platos del desayuno",
                    estado = "completada",
                    puntos = 30,
                    nombreImgVector = "img16_dishes"
                )
            )
        }
    }
}
