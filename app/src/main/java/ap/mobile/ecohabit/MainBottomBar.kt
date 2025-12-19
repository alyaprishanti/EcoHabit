package ap.mobile.ecohabit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MainBottomBar(
    current: BottomTab,
    onSelect: (BottomTab) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = current == BottomTab.CALCULATOR,
            onClick = { onSelect(BottomTab.CALCULATOR) },
            icon = { Icon(Icons.Default.Calculate, null) },
            label = { Text("Carbon") }
        )

        NavigationBarItem(
            selected = current == BottomTab.QUIZ,
            onClick = { onSelect(BottomTab.QUIZ) },
            icon = { Icon(Icons.Default.Quiz, null) },
            label = { Text("Quiz") }
        )

        NavigationBarItem(
            selected = current == BottomTab.ECOTARGET,
            onClick = { onSelect(BottomTab.ECOTARGET) },
            icon = { Icon(Icons.Default.BarChart, null) },
            label = { Text("Target") }
        )
    }
}

enum class BottomTab {
    CALCULATOR, QUIZ, ECOTARGET
}
