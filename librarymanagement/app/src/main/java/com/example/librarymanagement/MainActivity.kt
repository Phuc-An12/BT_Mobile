package com.example.librarymanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Book(
    val id: Int,
    val name: String,
    var isBorrowed: Boolean = false
)

data class Student(
    val name: String,
    val borrowedBooks: MutableList<Book> = mutableListOf()
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LibraryManagementApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryManagementApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var students by remember {
        mutableStateOf(
            listOf(
                Student("Nguyen Van A"),
                Student("Nguyen Thi B"),
                Student("Nguyen Van C")
            )
        )
    }
    var allBooks by remember {
        mutableStateOf(
            listOf(
                Book(1, "Sách 01"),
                Book(2, "Sách 02"),
                Book(3, "Sách 03"),
                Book(4, "Sách 04"),
                Book(5, "Sách 05")
            )
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Quản lý") },
                    label = { Text("Quản lý") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Book, contentDescription = "DS Sách") },
                    label = { Text("DS Sách") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Sinh viên") },
                    label = { Text("Sinh viên") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> ManagementScreen(
                students = students,
                allBooks = allBooks,
                onUpdateStudent = { index, updatedStudent ->
                    students = students.toMutableList().apply {
                        this[index] = updatedStudent
                    }
                },
                modifier = Modifier.padding(paddingValues)
            )
            1 -> BookListScreen(
                books = allBooks,
                modifier = Modifier.padding(paddingValues)
            )
            2 -> StudentListScreen(
                students = students,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementScreen(
    students: List<Student>,
    allBooks: List<Book>,
    onUpdateStudent: (Int, Student) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStudentIndex by remember { mutableIntStateOf(0) }
    var studentName by remember { mutableStateOf(students[0].name) }
    var showAddDialog by remember { mutableStateOf(false) }

    val currentStudent = students[currentStudentIndex]
    val availableBooks = allBooks.filter { book ->
        students.all { student -> student.borrowedBooks.none { it.id == book.id } }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Hệ thống\nQuản lý Thư viện",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text("Sinh viên", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = studentName,
                onValueChange = { studentName = it },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                val nextIndex = (currentStudentIndex + 1) % students.size
                currentStudentIndex = nextIndex
                studentName = students[nextIndex].name
            }) {
                Text("Thay đổi")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Danh sách sách",
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            if (currentStudent.borrowedBooks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Bạn chưa mượn quyển sách nào\nNhấn 'Thêm' để bắt đầu hành trình đọc sách!",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(currentStudent.borrowedBooks) { book ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.Book,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                                Text(book.name)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = availableBooks.isNotEmpty()
        ) {
            Text("Thêm")
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Chọn sách để mượn") },
            text = {
                LazyColumn {
                    items(availableBooks) { book ->
                        TextButton(
                            onClick = {
                                val updatedStudent = currentStudent.copy(
                                    borrowedBooks = (currentStudent.borrowedBooks + book).toMutableList()
                                )
                                onUpdateStudent(currentStudentIndex, updatedStudent)
                                showAddDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(book.name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }
}

@Composable
fun BookListScreen(books: List<Book>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
        Text(
            "Danh sách sách",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(books) { book ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Book,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(book.name)
                    }
                }
            }
        }
    }
}

@Composable
fun StudentListScreen(students: List<Student>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
        Text(
            "Danh sách sinh viên",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(students) { student ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Text(student.name, fontWeight = FontWeight.Medium)
                        }
                        if (student.borrowedBooks.isNotEmpty()) {
                            Text(
                                "Đã mượn: ${student.borrowedBooks.joinToString(", ") { it.name }}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
