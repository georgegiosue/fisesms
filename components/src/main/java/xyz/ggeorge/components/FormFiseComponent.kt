package xyz.ggeorge.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.inputmethod.InputMethodManager
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
    onAIResponse: Boolean,
    aiCouponValue: String,
    aiDniValue: String,
    postAISetValues: () -> Unit,
    onSubmit: (CoroutineScope, Fise.ToSend) -> Unit
) {

    var dni = remember {
        mutableStateOf("")
    }

    var vale = remember {
        mutableStateOf("")
    }

    if (onAIResponse) {
        dni.value = aiDniValue
        vale.value = aiCouponValue
        postAISetValues()
    }

    val focusManager = LocalFocusManager.current

    val ctx = LocalContext.current

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
        onValueChange = {
            if (it.length <= 8) {
                dni.value = it
            }
        },
        label = { Text("DNI") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        )
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
        onValueChange = {
            if (it.length <= 13) {
                vale.value = it
            }
        },
        label = { Text("Vale") },
        supportingText = { Text("Formato: 05-04-23-123456-1") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        )
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

                hideKeyboard(ctx)
                focusManager.clearFocus()
            }
        ) {
            Text(
                text = "Enviar",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }

}

@SuppressLint("ServiceCast")
fun hideKeyboard(ctx: Context) {
    val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(null, 0)
}
