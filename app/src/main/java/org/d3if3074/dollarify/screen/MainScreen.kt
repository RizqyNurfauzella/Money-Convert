package org.d3if3074.dollarify.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3074.dollarify.R
import org.d3if3074.dollarify.navigation.Screen
import org.d3if3074.dollarify.ui.theme.DollarifyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null, modifier = Modifier.size(50.dp))
                        Image(
                            painter = painterResource(id = R.drawable.text),
                            contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFC6EBC5),
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.tentang_aplikasi),
                            tint = Color(0xFF3A4D39)
                        )
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

enum class Currency {
    DOLLAR,
    YEN,
    BAHT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent(modifier: Modifier) {
    var nominalRupiah by rememberSaveable { mutableStateOf("") }
    var nominalRupiahError by rememberSaveable { mutableStateOf(false) }

    var pajakPersen by rememberSaveable { mutableStateOf("") }
    var pajakPersenError by rememberSaveable { mutableStateOf(false) }

    var konversiUang by rememberSaveable { mutableFloatStateOf(0f) }

    var expanded by rememberSaveable { mutableStateOf(false) }

    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    var selectedOptionTextError by rememberSaveable { mutableStateOf(false) }

    var icon by rememberSaveable {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.label_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                label = { Text(stringResource(id = R.string.label_drop)) },
                isError = selectedOptionTextError,
                trailingIcon = { IconPickerDropdown(
                    isError = selectedOptionTextError,
                    unit = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)} ) },
                supportingText = { ErrorHint(selectedOptionTextError) },
                colors = TextFieldDefaults.colors(
                    cursorColor = Color(0xFF3A4D39),
                    focusedLabelColor = Color(0xFF3A4D39),
                    unfocusedLabelColor = Color(0xFF3A4D39),
                    focusedIndicatorColor = Color(0xFF627254),
                    unfocusedIndicatorColor =  Color(0xFF627254),
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                Currency.entries.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency.toString()) },
                        onClick = {
                            selectedOptionText = currency.toString()
                            expanded = false
                        },
                    )
                }
            }
        }
        Text(
            text = stringResource(id = R.string.konversi_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = nominalRupiah,
            onValueChange = { nominalRupiah = it },
            label = { Text(text = stringResource(id = R.string.nominal_rupiah)) },
            isError = nominalRupiahError,
            trailingIcon = { IconPicker(nominalRupiahError, unit = "Rp") },
            supportingText = { ErrorHint(nominalRupiahError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                cursorColor = Color(0xFF3A4D39),
                focusedLabelColor = Color(0xFF3A4D39),
                unfocusedLabelColor = Color(0xFF3A4D39),
                focusedIndicatorColor = Color(0xFF627254),
                unfocusedIndicatorColor =  Color(0xFF627254),
            )
        )
        Text(
            text = stringResource(id = R.string.pajak_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = pajakPersen,
            onValueChange = { pajakPersen = it },
            label = { Text(text = stringResource(id = R.string.pajak_persen)) },
            isError = pajakPersenError,
            trailingIcon = { IconPicker(pajakPersenError, unit = "%") },
            supportingText = { ErrorHint(pajakPersenError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                cursorColor = Color(0xFF3A4D39),
                focusedLabelColor = Color(0xFF3A4D39),
                unfocusedLabelColor = Color(0xFF3A4D39),
                focusedIndicatorColor = Color(0xFF627254),
                unfocusedIndicatorColor =  Color(0xFF627254),
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(
                onClick = {
                    nominalRupiahError = (nominalRupiah == "" || nominalRupiah == "0")
                    pajakPersenError = (pajakPersen == "" || pajakPersen.toFloatOrNull() == null || pajakPersen.toFloat() < 0)
                    selectedOptionTextError = (selectedOptionText.isBlank() || Currency.entries.none { it.name == selectedOptionText })

                    if (nominalRupiahError || pajakPersenError) return@Button

                    val pajak = pajakPersen.toFloat() / 100
                    val jumlahRupiah = nominalRupiah.toFloat()
                    val totalRupiah = jumlahRupiah + (jumlahRupiah * pajak)
                    konversiUang = konversiUang(totalRupiah,selectedOptionText)
                    icon = when (selectedOptionText){
                        Currency.DOLLAR.toString() -> "$"
                        Currency.YEN.toString() -> "¥"
                        Currency.BAHT.toString() -> "฿"
                        else -> ""
                    }
                },
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC6EBC5),
                    contentColor = Color(0xFF3A4D39)
                )
            ) {
                Text(text = stringResource(id = R.string.konversi))
            }
        }
        if (konversiUang != 0f) {
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 2.dp
            )
            Text(
                text = stringResource(id = R.string.hasil_konversi,icon, konversiUang),
                style = MaterialTheme.typography.titleLarge
            )
            Row {
                IconButton(onClick = { shareData(
                    context = context,
                    message = context.getString(R.string.share_template, icon, konversiUang))}) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = stringResource(id = R.string.share),
                        tint = Color(0xFF3A4D39),
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(modifier = Modifier.size(20.dp))
                IconButton(onClick = {
                    selectedOptionText = ""
                    nominalRupiah = ""
                    pajakPersen = ""
                    konversiUang = 0f
                    selectedOptionTextError = false
                    nominalRupiahError = false
                    pajakPersenError = false
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = stringResource(id = R.string.share),
                        tint = Color(0xFF3A4D39),
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun IconPickerDropdown(isError: Boolean, unit: @Composable (() -> Unit)) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        unit()
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(id = R.string.input_invalid))
    }
}

private fun konversiKeDollar(nominalRupiah: Float): Float {
    // 1 Dollar = Rp 15.000,00
    return nominalRupiah / 15000
}

private fun konversiKeYen(nominalRupiah: Float): Float {
    // 1 Yen = Rp 104,93
    return (nominalRupiah / 104.93).toFloat()
}

private fun konversiKeBaht(nominalRupiah: Float): Float {
    // 1 Baht = Rp 436,87
    return (nominalRupiah / 436.87).toFloat()
}

private fun konversiUang(nominalRupiah: Float, currency: String): Float {
    return when (currency) {
        "DOLLAR" -> konversiKeDollar(nominalRupiah)
        "YEN" -> konversiKeYen(nominalRupiah)
        "BAHT" -> konversiKeBaht(nominalRupiah)
        else -> 0f
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) !=null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun KonversiPreview() {
    DollarifyTheme {
        MainScreen(rememberNavController())
    }
}