package com.example.alertpage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alertpage.ui.theme.AlertPageTheme
import com.example.alertpage.models.Survey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var surveys = ArrayList<Survey>()
        var survey = Survey(getSurveyName(), getActionName(), getUserName())
        surveys.add(survey)
        for (i in 1..5) {
            survey = Survey(getSurveyName(), getActionName(), getUserName())
            surveys.add(survey)
        }


        setContent {
            AlertPageTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//
//                }
                recyclerView(surveys)
            }
        }
    }

    private fun getUserName(): String {
        val list = listOf("Goofy", "Daisy", "Mickey Mouse", "Minnie Mouse")
        val randomIndex = (list.indices).random()
        return list[randomIndex]
    }

    private fun getActionName(): String {
        val list = listOf("completed", "started", "abandoned", "cancelled")
        val randomIndex = (list.indices).random()
        return list[randomIndex]
    }

    private fun getSurveyName(): String {
        val list = listOf("FC Survey", "CD Survey", "RB Survey", "India Survey")
        val randomIndex = (list.indices).random()
        return list[randomIndex]
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlertPageTheme {
        Greeting("Android")
    }
}

@Composable
fun listItem(txtItem: String) {
    Surface(color = Color.Cyan, modifier = Modifier.padding(16.dp)) {
        Column {
            Text(text = "This is the list item of RV $txtItem")
        }
    }
}

@Composable
fun listItemSurvey(survey: Survey) {
    val expanded = remember {
        mutableStateOf(false)
    }
    Surface(color = Color.Cyan, modifier = Modifier.padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "${survey.surveyName} ${survey.actionName} for ${survey.userName}.")
            OutlinedButton(
                onClick = { expanded.value = !expanded.value },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(if (expanded.value) "Show less" else "Show more")
            }
            if (expanded.value) {
                Column {
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = "This is shown when clicked"
                    )
                }
            }
        }

    }
}

@Composable
fun recyclerView(names: List<Survey>) {
    LazyColumn(Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            listItemSurvey(name)
        }
    }
}

@Preview
@Composable
fun recyclerViewPreview() {
    var survey = Survey("Some Survey Name", "started", "John Doe")

    Column {
        listItemSurvey(survey)
        listItemSurvey(survey)
    }
}