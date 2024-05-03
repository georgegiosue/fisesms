package xyz.ggeorge.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import xyz.ggeorge.core.domain.Fise

@Composable
fun FormFiseComponent(
    coroutine: CoroutineScope,
    onSubmit: (CoroutineScope, Fise.ToSend) -> Unit
) {

    val dni = remember {
        mutableStateOf("")
    }

    val vale = remember {
        mutableStateOf("")
    }

    Text(
        text = "Datos",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = dni.value,
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            if (dni.value.isNotEmpty()) {
                IconButton(
                    onClick = {
                        dni.value = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear",

                    )
                }
            }
        },
        onValueChange = { dni.value = it},
        label = { Text("DNI") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number)
    )

    Spacer(modifier = Modifier.padding(top = 4.dp))


    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = vale.value,
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            if (vale.value.isNotEmpty()) {
                IconButton(
                    onClick = {
                        vale.value = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear",

                        )
                }
            }
        },
        onValueChange = { vale.value = it },
        label = { Text("Vale") },
        supportingText = { Text("Formato: 05-04-23-123456-1") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number)
    )

    Spacer(modifier = Modifier.padding(top = 16.dp))

    Row(horizontalArrangement = Arrangement.SpaceAround) {

        FilledTonalButton(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            onClick = {
                val fise = Fise.ToSend(dni.value, vale.value)
                onSubmit(coroutine, fise)
            }
        ){
            Text(
                text = "Enviar",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}