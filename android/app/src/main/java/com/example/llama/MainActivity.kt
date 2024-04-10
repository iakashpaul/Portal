package com.example.llama

import android.app.ActivityManager
import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.text.format.Formatter
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.ElevatedButton
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.example.llama.ui.theme.LlamaAndroidTheme
import java.io.File
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.size

class MainActivity(
    activityManager: ActivityManager? = null,
    downloadManager: DownloadManager? = null,
    clipboardManager: ClipboardManager? = null,
): ComponentActivity() {
    private val tag: String? = this::class.simpleName

    private val activityManager by lazy { activityManager ?: getSystemService<ActivityManager>()!! }
    private val downloadManager by lazy { downloadManager ?: getSystemService<DownloadManager>()!! }
    private val clipboardManager by lazy { clipboardManager ?: getSystemService<ClipboardManager>()!! }

    private val viewModel: MainViewModel by viewModels()

    // Get a MemoryInfo object for the device's current memory status.
    private fun availableMemory(): ActivityManager.MemoryInfo {
        return ActivityManager.MemoryInfo().also { memoryInfo ->
            activityManager.getMemoryInfo(memoryInfo)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StrictMode.setVmPolicy(
            VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectLeakedClosableObjects()
                .build()
        )

        val free = Formatter.formatFileSize(this, availableMemory().availMem)
        val total = Formatter.formatFileSize(this, availableMemory().totalMem)

        viewModel.log("Memory Free/Total: $free / $total")
//        viewModel.log("Downloads directory: ${getExternalFilesDir(null)}")

        val extFilesDir = getExternalFilesDir(null)

        val models = listOf(
//            Downloadable(
//                "Gemma-2b-it-Q8",
//                Uri.parse("https://huggingface.co/iAkashPaul/gemma-2b-it-gguf/resolve/main/gemma-2b-it-Q8_0.gguf?download=true"),
//                File(extFilesDir, "gemma-2b-it-Q8_0.gguf"),
//            ),
//            Downloadable(
//                "Gemma-2b-it-Q4",
//                Uri.parse("https://huggingface.co/iAkashPaul/gemma-2b-it-gguf/resolve/main/gemma-2b-it-Q4_0.gguf?download=true"),
//                File(extFilesDir, "gemma-2b-it-Q4_0.gguf"),
//            ),
            Downloadable(
                "StableCode-3B",
                Uri.parse("https://huggingface.co/bartowski/stable-code-instruct-3b-GGUF/resolve/main/stable-code-instruct-3b-Q4_0.gguf?download=true"),
                File(extFilesDir, "stablecode-instruct-Q4_0.gguf"),
            ),
//            Downloadable(
//                "StableLM-2-zephyr-1_6b",
//                Uri.parse("https://huggingface.co/second-state/stablelm-2-zephyr-1.6b-GGUF/resolve/main/stablelm-2-zephyr-1_6b-Q8_0.gguf?download=true"),
//                File(extFilesDir, "stablelm-2-zephyr-1_6b.gguf"),
//            ),
            Downloadable(
                "Navarasa-2B",
                Uri.parse("https://huggingface.co/Telugu-LLM-Labs/Indic-gemma-2b-finetuned-sft-Navarasa-2.0-gguf/resolve/main/Q5_K_M.gguf?download=true"),
                File(extFilesDir, "navarasa-2-q5.gguf")
            ),
            Downloadable(
                "Mistral-7B-IT",
                Uri.parse("https://huggingface.co/TheBloke/Mistral-7B-Instruct-v0.2-GGUF/resolve/main/mistral-7b-instruct-v0.2.Q2_K.gguf?download=true"),
                File(extFilesDir, "mistral-7b-instruct-v0.2.Q2_K.gguf"),
            ),
            Downloadable(
                "StableLM-1.6B-Zephyr",
                Uri.parse("https://huggingface.co/stabilityai/stablelm-2-zephyr-1_6b/resolve/main/stablelm-2-zephyr-1_6b-Q4_0.gguf?download=true"),
                File(extFilesDir, "stablelm-2-zephyr-1_6b-Q4_0.gguf")
        )
        )

        setContent {
            LlamaAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainCompose(
                        viewModel,
                        clipboardManager,
                        downloadManager,
                        models,
                    )
                }

            }
        }
    }
}

@Composable
fun MainCompose(
    viewModel: MainViewModel,
    clipboard: ClipboardManager,
    dm: DownloadManager,
    models: List<Downloadable>
) {

    Column {
        val scrollState = rememberLazyListState()
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            modifier = Modifier
                .size(width = 400.dp, height = 200.dp)
        ) {
            Text(
                text = "Portal by @iAkashPaul",
                modifier = Modifier
                    .padding(30.dp),
                textAlign = TextAlign.Center, fontSize = 30.sp, fontWeight = FontWeight.Thin
            )

            val rowScrollState = rememberLazyListState()
            Row{
                Column{
                    Text("Select your LLM: ", fontSize = 17.sp,fontWeight = FontWeight.ExtraLight,
                        modifier = Modifier
                            .padding(15.dp), textAlign = TextAlign.Center,)
                }

                LazyRow(state = rowScrollState) {

                    items(models) {
                        Downloadable.ElevatedButton(viewModel, dm, it)
                    }
                }
            }
        }

//        Text("Portal by @iAkashPaul", fontSize = 33.sp, fontWeight = FontWeight.Thin)



        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(state = scrollState) {
                items(viewModel.messages) {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Row {
            Column{

                Button({ viewModel.send() }) { Text("Send") }
//            Button({ viewModel.bench(8, 4, 1) }) { Text("Bench") }
                Button({ viewModel.clear() }) { Text("Clear") }
//            Button({
//                viewModel.messages.joinToString("\n").let {
//                    clipboard.setPrimaryClip(ClipData.newPlainText("", it))
//                }
//            }) { Text("Copy") }

            }
            Column{
            OutlinedTextField(
                value = viewModel.message,
                onValueChange = { viewModel.updateMessage(it) },
                label = { Text("Prompt here!") },
            )
            }

        }
    }
}
