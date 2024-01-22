package com.example.alertpage

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.example.alertpage.models.Survey
import com.example.alertpage.ui.theme.AlertPageTheme

val TAG = MainActivity::class.java.simpleName

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    var surveys = ArrayList<Survey>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.initDisplayWithDummyValues()


        setContent {
            AlertPageTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//
//                }
                Column {
                    FilterButtons()
                    viewModel.getDisplayedList().observeAsState()?.value.let {
                        recyclerView(it)
                        if (it != null) {
                            if (it.size > 0) {
                                it[0]?.let { it1 -> Log.d(TAG, it1.actionName) }
                            }
                        } else {
                            Log.d(TAG, "it was null")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun listItemSurvey(survey: Survey) {
        var fontFamily = FontFamily(
            Font(R.font.montserrat_regular),
            Font(R.font.montserrat_bold)
        )
        val expanded = remember {
            mutableStateOf(false)
        }
        Surface(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp), onClick = {
            expanded.value = !expanded.value
        }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                elevation = CardDefaults.cardElevation()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    var displayText =
                        "${survey.surveyName} ${survey.actionName} for ${survey.userName}."
                    var fontWeight = FontWeight.Normal
                    if (survey.readFlag) {
                        fontWeight = FontWeight.Bold
                    }
                    Text(
                        text = "$displayText",
                        fontWeight = fontWeight,
                        fontFamily = fontFamily, modifier = Modifier.weight(.7f)
                    )
                    if (expanded.value) {
                        MinimalDialog(
                            onDismissRequest = { dialogDismissed() }, dialogText = displayText
                        )
                    }
                    if (survey.readFlag) {
                        Image(
                            modifier = Modifier
                                .weight(.3f)
                                .size(10.dp),
                            painter = painterResource(id = R.drawable.green_circle),
                            contentDescription = "Read Flag",
                            contentScale = ContentScale.Fit, alignment = Alignment.CenterEnd
                        )
                    }
                }
            }
        }
    }

    fun dialogDismissed() {
        Log.d(TAG, "dialog is dismissed, mark item as read.")
    }

    @Composable
    fun recyclerView(names: ArrayList<Survey>?) {
        if (!names.isNullOrEmpty()) {
            LazyColumn(Modifier.padding(vertical = 4.dp)) {
                items(items = names) { survey ->
                    listItemSurvey(survey)
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun recyclerViewPreview() {
        var survey = Survey("Some Survey Name", "started", "John Doe")

        Column {
            listItemSurvey(survey)
            listItemSurvey(survey)
        }
    }

    @Composable
    fun MinimalDialog(onDismissRequest: () -> Unit, dialogText: String) {
        // Dialog state Manager
        val dialogState: MutableState<Boolean> = remember {
            mutableStateOf(false)
        }
        var flagDialogClicked = true
        if (!dialogState.value) {
            Dialog(onDismissRequest = { onDismissRequest() }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = dialogText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, start = 16.dp, end = 16.dp),
                            textAlign = TextAlign.Center,
                        )
                        TextButton(
                            onClick = {
                                dialogState.value = flagDialogClicked
                            },
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.padding(8.dp),
                        ) {
                            OutlinedButton(colors = ButtonDefaults.buttonColors(
                                contentColor = Color.Blue, containerColor = Color(0xFFEF7D00)
                            ), shape = RoundedCornerShape(10.dp), onClick = {
                                dialogState.value = flagDialogClicked
                            }) {
                                Text(
                                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                                    text = "OK",
                                    style = TextStyle(color = Color.White, fontSize = 20.sp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun FilterButtons() {
        Row(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            OutlinedButton(onClick = {
                viewModel.filterList(3)//read
            }) {
                Text(modifier = Modifier.padding(start = 16.dp, end = 16.dp), text = "All")
            }

            OutlinedButton(onClick = {
                viewModel.filterList(2)//unread
            }) {
                Text(modifier = Modifier.padding(start = 16.dp, end = 16.dp), text = "Read")
            }

            OutlinedButton(onClick = {
                viewModel.filterList(1)//all
            }) {
//        Text(if (expanded.value) "Show less" else "Show more")
                Text(modifier = Modifier.padding(start = 16.dp, end = 16.dp), text = "Unread")
            }
        }
    }
}
