package com.example.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import com.example.data.model.ContentItem
import com.example.ui.theme.DarkNavyHeading
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.SkyBluePrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

@Composable
fun PdfViewerDialog(
    document: ContentItem,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var localPdfFile by remember(document.mediaUrl) { mutableStateOf<File?>(null) }
    var isLoading by remember(document.mediaUrl) { mutableStateOf(true) }
    var errorMessage by remember(document.mediaUrl) { mutableStateOf<String?>(null) }
    var currentPage by remember(document.mediaUrl) { mutableIntStateOf(0) }
    var pageCount by remember(document.mediaUrl) { mutableIntStateOf(0) }
    var renderedBitmap by remember(document.mediaUrl) { mutableStateOf<Bitmap?>(null) }
    var reloadTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(document.mediaUrl, reloadTrigger) {
        isLoading = true
        errorMessage = null
        val resolved = withContext(Dispatchers.IO) {
            runCatching {
                val url = document.mediaUrl.trim()
                if (url.startsWith("/")) {
                    val file = File(url)
                    if (!file.exists() || file.length() <= 0L) {
                        throw IllegalStateException("PDF फ़ाइल डिवाइस स्टोरेज में उपलब्ध नहीं है।")
                    }
                    file
                } else {
                    val cacheFile = File(context.cacheDir, "pdf_${document.id}.pdf")
                    if (!cacheFile.exists() || cacheFile.length() == 0L) {
                        val req = Request.Builder().url(url).build()
                        OkHttpClient().newCall(req).execute().use { resp ->
                            if (!resp.isSuccessful) {
                                throw IllegalStateException("PDF डाउनलोड नहीं हो सका (HTTP ${resp.code})")
                            }
                            resp.body?.byteStream()?.use { input ->
                                FileOutputStream(cacheFile).use { output ->
                                    input.copyTo(output)
                                }
                            } ?: throw IllegalStateException("PDF खाली है।")
                        }
                    }
                    cacheFile
                }
            }
        }
        resolved.onSuccess { file ->
            localPdfFile = file
            isLoading = false
        }.onFailure { err ->
            errorMessage = err.localizedMessage ?: "PDF खोलने में त्रुटि।"
            isLoading = false
        }
    }

    var rendererHolder by remember { mutableStateOf<Pair<ParcelFileDescriptor, PdfRenderer>?>(null) }

    DisposableEffect(localPdfFile) {
        val file = localPdfFile
        if (file != null && file.exists()) {
            try {
                val fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                val renderer = PdfRenderer(fd)
                pageCount = renderer.pageCount
                currentPage = 0
                rendererHolder = Pair(fd, renderer)
            } catch (e: Exception) {
                errorMessage = "यह फ़ाइल मानक PDF रेंडरर में नहीं खुल सकी: ${e.localizedMessage ?: ""}"
            }
        }
        onDispose {
            rendererHolder?.let { (fd, renderer) ->
                try {
                    renderer.close()
                    fd.close()
                } catch (_: Exception) {}
            }
            rendererHolder = null
        }
    }

    LaunchedEffect(rendererHolder, currentPage) {
        val holder = rendererHolder ?: return@LaunchedEffect
        val (_, renderer) = holder
        if (currentPage in 0 until renderer.pageCount) {
            val bmp = withContext(Dispatchers.IO) {
                renderer.openPage(currentPage).use { page ->
                    val width = (page.width * 2).coerceAtLeast(800)
                    val height = ((page.height.toFloat() / page.width.toFloat()) * width).toInt().coerceAtLeast(800)
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    bitmap.eraseColor(android.graphics.Color.WHITE)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    bitmap
                }
            }
            renderedBitmap = bmp
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("pdf_viewer_dialog"),
            color = Color(0xFFF0F7FC)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SkyBluePrimary)
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                    Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                        Text(
                            text = document.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (pageCount > 0) {
                            Text(
                                text = "पृष्ठ ${currentPage + 1} / $pageCount",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFE1F5FE))
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            try {
                                val file = localPdfFile
                                val intent = if (file != null && file.exists()) {
                                    val uri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        file
                                    )
                                    Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(uri, document.mimeType.ifBlank { "application/pdf" })
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                } else {
                                    Intent(Intent.ACTION_VIEW, Uri.parse(document.mediaUrl))
                                }
                                context.startActivity(Intent.createChooser(intent, "दस्तावेज़ खोलें"))
                            } catch (_: Exception) {
                                Toast.makeText(context, "बाहरी PDF व्यूअर उपलब्ध नहीं है", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Icon(Icons.Default.OpenInBrowser, contentDescription = "Open Externally", tint = Color.White)
                    }
                }

                // Content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = SkyBluePrimary)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("PDF दस्तावेज़ लोड हो रहा है...", color = DarkNavyHeading)
                        }
                    } else if (errorMessage != null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage.orEmpty(),
                                textAlign = TextAlign.Center,
                                color = DarkNavyHeading
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { reloadTrigger++ },
                                colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("पुनः प्रयास")
                            }
                        }
                    } else if (renderedBitmap != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                bitmap = renderedBitmap!!.asImageBitmap(),
                                contentDescription = "PDF Page ${currentPage + 1}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }

                // Footer Pagination
                if (pageCount > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { if (currentPage > 0) currentPage-- },
                            enabled = currentPage > 0
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("पिछला पृष्ठ", color = OceanBlue)
                        }

                        Text(
                            text = "${currentPage + 1} / $pageCount",
                            fontWeight = FontWeight.Bold,
                            color = DarkNavyHeading
                        )

                        OutlinedButton(
                            onClick = { if (currentPage < pageCount - 1) currentPage++ },
                            enabled = currentPage < pageCount - 1
                        ) {
                            Text("अगला पृष्ठ", color = OceanBlue)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
