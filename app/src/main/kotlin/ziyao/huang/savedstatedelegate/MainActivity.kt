package ziyao.huang.savedstatedelegate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ziyao.huang.savedstatedelegate.ui.theme.SavedStateDelegateTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SavedStateDelegateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = innerPadding)
                            .verticalScroll(state = rememberScrollState())
                    ) {
                        Text(text = viewModel.name)
                        Text(text = viewModel.age.toString())
                        Text(text = viewModel.contents.toString())
                        Text(text = viewModel.ids.toString())
                        Text(text = viewModel.testSerializable.collectAsStateWithLifecycle().value.toString())
                        Text(text = viewModel.testParcelable.collectAsStateWithLifecycle().value.toString())
                        Text(text = viewModel.testKSerialization.toString())
                        Text(text = viewModel.testMoshi.toString())
                        Text(text = viewModel.testGson.toString())
                    }
                }
            }
        }
    }
}
