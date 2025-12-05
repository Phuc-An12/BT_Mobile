package com.example.smartuthtasks.uiscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartuthtasks.network.Task

@Composable
fun TaskItem(
    task: Task,
    onDelete: (Task) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(onClick = { onDelete(task) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Xóa công việc",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
