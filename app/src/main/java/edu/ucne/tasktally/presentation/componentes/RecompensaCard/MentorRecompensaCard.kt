package edu.ucne.tasktally.presentation.componentes.RecompensaCard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.tasktally.R
import edu.ucne.tasktally.ui.theme.TaskTallyTheme

@Composable
fun MentorRecompensaCard(
    modifier: Modifier = Modifier,
    numeroRecompensa: String = "Recompensa #1",
    titulo: String = "Cenar pizza",
    precio: Int = 750,
    imageName: String? = null,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                val imageResId = getDrawableResourceId(imageName)
                if (imageResId != null) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = titulo,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {

                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Valor: $precio pts",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onEditClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar recompensa",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                    onClick = onDeleteClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar recompensa",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun getDrawableResourceId(imageName: String?): Int? {
    if (imageName == null) return null

    return when (imageName) {
        //maticas
        "img0_yellow_tree" -> R.drawable.img0_yellow_tree
        "img1_purple_vines" -> R.drawable.img1_purple_vines
        "img2_little_bush" -> R.drawable.img2_little_bush
        "img3_little_plant" -> R.drawable.img3_little_plant
        "img4_pink_tree" -> R.drawable.img4_pink_tree
        "img5_purple_flower" -> R.drawable.img5_purple_flower
        "img6_purple_plant" -> R.drawable.img6_purple_plant
        "img7_green_tree" -> R.drawable.img7_green_tree
        "img8_green_leaves" -> R.drawable.img8_green_leaves
        "img9_color_leaves" -> R.drawable.img9_color_leaves

        //objetos
        "img10_batteries" -> R.drawable.img10_batteries
        "img11_boxes" -> R.drawable.img11_boxes
        "img12_calendar" -> R.drawable.img12_calendar
        "img13_chocolate" -> R.drawable.img13_chocolate
        "img14_clock" -> R.drawable.img14_clock
        "img15_coffee_cup" -> R.drawable.img15_coffee_cup
        "img16_coffee_machine" -> R.drawable.img16_coffee_machine
        "img16_dishes" -> R.drawable.img16_dishes
        "img17_doughnut" -> R.drawable.img17_doughnut
        "img18_doughnut" -> R.drawable.img18_doughnut
        "img19_files" -> R.drawable.img19_files
        "img20_folder" -> R.drawable.img20_folder
        "img21_food" -> R.drawable.img21_food
        "img22_hamburguer" -> R.drawable.img22_hamburguer
        "img23_ice_cream" -> R.drawable.img23_ice_cream
        "img24_mobile_phone" -> R.drawable.img24_mobile_phone
        "img25_notebook" -> R.drawable.img25_notebook
        "img26_pancakes" -> R.drawable.img26_pancakes
        "img27_pizza" -> R.drawable.img27_pizza
        "img28_pizza_slice" -> R.drawable.img28_pizza_slice
        "img29_pudding" -> R.drawable.img29_pudding
        "img30_recycle_bin" -> R.drawable.img30_recycle_bin
        else -> null
    }
}

@Preview(showBackground = true)
@Composable
fun MentorRecompensaCardPreview() {
    TaskTallyTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MentorRecompensaCard(
                modifier = Modifier.weight(1f),
                titulo = "Cenar pizza",
                precio = 750,
                imageName = "img27_pizza",
                onEditClick = { },
                onDeleteClick = { }
            )

            MentorRecompensaCard(
                modifier = Modifier.weight(1f),
                titulo = "Helado de chocolate",
                precio = 300,
                imageName = "img23_ice_cream",
                onEditClick = { },
                onDeleteClick = { }
            )
        }
    }
}