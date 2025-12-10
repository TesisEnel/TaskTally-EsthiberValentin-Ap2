package edu.ucne.tasktally.presentation.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DrawableImage(
    val resourceId: Int,
    val name: String,
    val displayName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(
    onDismiss: () -> Unit,
    onImageSelected: (String) -> Unit,
    selectedImageName: String? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val availableImages = listOf(
        //maticas
        "img0_yellow_tree" to "🌳 Árbol amarillo",
        "img1_purple_vines" to "🌿 Vines moradas",
        "img2_little_bush" to "🌱 Arbusto",
        "img3_little_plant" to "🪴 Plantita",
        "img5_purple_flower" to "💐 Flor morada",
        "img6_purple_plant" to "🪻 Planta morada",
        "img7_green_tree" to "🌲 Árbol verde",
        "img8_green_leaves" to "🍃 Hojas verdes",
        "img9_color_leaves" to "🍂 Hojas colores",

        //objetos
        "img10_batteries" to "🔋 Baterías",
        "img11_boxes" to "📦 Cajas",
        "img12_calendar" to "📅 Calendario",
        "img13_chocolate" to "🍫 Chocolate",
        "img14_clock" to "⏰ Reloj",
        "img15_coffee_cup" to "☕ Café",
        "img16_coffee_machine" to "☕ Cafetera",
        "img16_dishes" to "🍽️ Platos",
        "img17_doughnut" to "🍩 Dona",
        "img18_doughnut" to "🍩 Dona 2",
        "img19_files" to "📁 Archivos",
        "img20_folder" to "📂 Carpeta",
        "img21_food" to "🍱 Comida",
        "img22_hamburguer" to "🍔 Hamburguesa",
        "img23_ice_cream" to "🍦 Helado",
        "img24_mobile_phone" to "📱 Teléfono",
        "img25_notebook" to "📓 Cuaderno",
        "img26_pancakes" to "🥞 Pancakes",
        "img27_pizza" to "🍕 Pizza",
        "img28_pizza_slice" to "🍕 Pizza slice",
        "img29_pudding" to "🍮 Pudín",
        "img30_recycle_bin" to "♻️ Reciclaje"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Selecciona una imagen",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(availableImages) { (imageName, displayName) ->
                    val isSelected = imageName == selectedImageName

                    OutlinedButton(
                        onClick = {
                            onImageSelected(imageName)
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = displayName,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}