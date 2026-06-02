package ci.nsu.moble.main.ui

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val nameRegex = Regex("^[A-Za-zА-Яа-яЁё]{2,30}$")
    val loginRegex = Regex("^[a-zA-Z0-9_]{4,20}$")
    val passwordRegex =
        Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,50}$")

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var middleName by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }

    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }

    var selectedGender by rememberSaveable { mutableStateOf("") }

    var genderExpanded by remember { mutableStateOf(false) }
    var groupExpanded by remember { mutableStateOf(false) }

    var selectedGroupId by rememberSaveable { mutableStateOf<Int?>(null) }
    var selectedGroupName by rememberSaveable { mutableStateOf("") }

    val genders = listOf("Мужской", "Женский")

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    val isFormValid =
        nameRegex.matches(firstName) &&
                nameRegex.matches(lastName) &&
                loginRegex.matches(login) &&
                passwordRegex.matches(password) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                phoneNumber.length >= 10 &&
                selectedGroupId != null &&
                selectedGender.isNotEmpty()

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {

            Text(
                text = "Регистрация",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = {
                    firstName = it.filter { c -> c.isLetter() }
                },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = it.filter { c -> c.isLetter() }
                },
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = middleName,
                onValueChange = {
                    middleName = it.filter { c -> c.isLetter() }
                },
                label = { Text("Отчество") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = birthDate,
                onValueChange = {
                    birthDate = it
                },
                label = { Text("Дата рождения (2000-01-31)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = {
                    genderExpanded = !genderExpanded
                }
            ) {

                OutlinedTextField(
                    value = selectedGender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Пол") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = genderExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = {
                        genderExpanded = false
                    }
                ) {

                    genders.forEach { gender ->

                        DropdownMenuItem(
                            text = {
                                Text(gender)
                            },
                            onClick = {
                                selectedGender = gender
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = groupExpanded,
                onExpandedChange = {
                    groupExpanded = !groupExpanded
                }
            ) {

                OutlinedTextField(
                    value = selectedGroupName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Группа") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = groupExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = groupExpanded,
                    onDismissRequest = {
                        groupExpanded = false
                    }
                ) {

                    state.groups.forEach { group ->

                        DropdownMenuItem(
                            text = {
                                Text(group.name)
                            },
                            onClick = {
                                selectedGroupId = group.id
                                selectedGroupName = group.name
                                groupExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = login,
                onValueChange = {
                    login = it.filter { c ->
                        c.isLetterOrDigit() || c == '_'
                    }
                },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it.replace(" ", "")
                },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it.replace(" ", "")
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it.filter { c ->
                        c.isDigit()
                    }
                },
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                enabled = isFormValid,
                onClick = {

                    viewModel.register(
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName,
                        birthDate = birthDate,
                        gender = selectedGender,
                        groupId = selectedGroupId,
                        login = login,
                        password = password,
                        email = email,
                        phoneNumber = phoneNumber,
                        onSuccess = onBackToLogin
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зарегистрироваться")
            }

            TextButton(
                onClick = onBackToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад ко входу")
            }

            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}