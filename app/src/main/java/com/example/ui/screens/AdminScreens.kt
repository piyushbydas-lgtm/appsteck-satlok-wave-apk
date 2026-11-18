package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.AppConfig
import com.example.data.model.ContentItem
import com.example.data.model.KnowledgeCategory
import com.example.data.model.PhotoAlbum
import com.example.data.repository.SatlokRepository
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SpiritualOrange
import com.example.util.MediaUploadManager
import com.example.util.MediaUploadResult
import com.example.util.UploadKind
import com.example.util.YouTubeHelper
import kotlinx.coroutines.launch
import java.io.File

private fun coilModelFor(source: String): Any =
    if (source.startsWith("/")) File(source) else source

// ----------------------------------------------------
// ADMIN LOGIN & FIRST-TIME SECURE SETUP SCREEN
// ----------------------------------------------------
@Composable
fun AdminLoginScreen(
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    var isConfigured by remember { mutableStateOf(SatlokRepository.isAdminAccountConfigured(context)) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_login_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("login_back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = if (isConfigured) "सुरक्षित एडमिन लॉगिन" else "एडमिन खाता सुरक्षा सेटअप",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SATLOK WAVE Admin Portal",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Text(
                text = if (isConfigured) {
                    "केवल अधिकृत एडमिन लॉगिन — पासवर्ड पूर्णतः गोपनीय एवं एन्क्रिप्टेड है"
                } else {
                    "प्रथम बार सेटअप: अपना व्यक्तिगत एडमिन ईमेल व पासवर्ड पंजीकृत करें"
                },
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_email_input"),
                label = { Text("एडमिन ईमेल (उदा. owner@satlokwave.com)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_password_input"),
                label = { Text(if (isConfigured) "एडमिन पासवर्ड" else "नया सुरक्षित पासवर्ड (कम से कम 6 अक्षर)") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle Password"
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )

            if (!isConfigured) {
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_confirm_password_input"),
                    label = { Text("पासवर्ड की पुष्टि करें (Confirm Password)") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = SkyBluePrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "सुरक्षा नियम: एडमिन पासवर्ड PBKDF2-SHA256 से एन्क्रिप्टेड रहता है और कभी भी स्क्रीन या कोड में प्रदर्शित नहीं होता।",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = {
                    val result = if (isConfigured) {
                        SatlokRepository.loginAdmin(context, email, password)
                    } else {
                        SatlokRepository.setupInitialAdmin(context, email, password, confirmPassword)
                    }
                    if (result.success) {
                        isConfigured = true
                        password = ""
                        confirmPassword = ""
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        onLoginSuccess()
                    } else {
                        errorMessage = result.message
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("admin_login_submit"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SpiritualOrange)
            ) {
                Text(
                    text = if (isConfigured) "सुरक्षित लॉगिन करें (Login)" else "सुरक्षित एडमिन खाता बनाएं",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

// ----------------------------------------------------
// ADMIN DASHBOARD & CONTENT MANAGEMENT
// ----------------------------------------------------
@Composable
fun AdminDashboardScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val isLoggedIn by SatlokRepository.isAdminLoggedIn.collectAsState()
    val adminEmail by SatlokRepository.adminEmail.collectAsState()
    val videos by SatlokRepository.videos.collectAsState()
    val audios by SatlokRepository.audios.collectAsState()
    val documents by SatlokRepository.documents.collectAsState()
    val albums by SatlokRepository.photoAlbums.collectAsState()
    val appConfig by SatlokRepository.appConfig.collectAsState()
    val context = LocalContext.current

    // Gate dashboard behind verified admin login
    if (!isLoggedIn) {
        AdminLoginScreen(onBack = onBack, onLoginSuccess = {})
        return
    }

    var selectedAdminTab by remember { mutableIntStateOf(0) }
    var showApkDownloadDialog by remember { mutableStateOf(false) }
    val tabTitles = listOf(
        "डैशबोर्ड",
        "वीडियो अपलोड",
        "दस्तावेज़/PDF",
        "ऑडियो अपलोड",
        "फोटो एलबम",
        "सेक्शन व लोगो",
        "सुरक्षा व क्लाउड",
        "APK बिल्ड"
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_dashboard_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Admin Top Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Text(
                            text = "एडमिन नियंत्रण केंद्र",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        if (adminEmail.isNotBlank()) {
                            Text(
                                text = adminEmail,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        SatlokRepository.logoutAdmin()
                        Toast.makeText(context, "लॉगआउट हो गया", Toast.LENGTH_SHORT).show()
                        onLogout()
                    },
                    modifier = Modifier.testTag("admin_logout_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            ScrollableTabRow(
                selectedTabIndex = selectedAdminTab,
                containerColor = MaterialTheme.colorScheme.surface,
                edgePadding = 8.dp
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedAdminTab == index,
                        onClick = { selectedAdminTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (selectedAdminTab == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            )
                        },
                        modifier = Modifier.testTag("admin_tab_$index")
                    )
                }
            }

            when (selectedAdminTab) {
                0 -> AdminOverviewTab(
                    videos = videos,
                    audios = audios,
                    documents = documents,
                    albums = albums,
                    onDeleteVideo = { SatlokRepository.deleteVideo(it) },
                    onToggleFeatured = { SatlokRepository.toggleFeatured(it) },
                    onTogglePublish = { SatlokRepository.toggleVideoPublishStatus(it) }
                )
                1 -> AdminUploadVideoTab()
                2 -> AdminUploadDocumentTab(documents = documents)
                3 -> AdminUploadAudioTab(audios = audios)
                4 -> AdminPhotoAlbumTab(albums = albums)
                5 -> AdminSectionsAndLogoTab(appConfig = appConfig)
                6 -> AdminSecurityAndCloudTab()
                7 -> AdminBuildAreaTab(onOpenApkDownload = { showApkDownloadDialog = true })
            }
        }
    }

    if (showApkDownloadDialog) {
        ApkDownloadDialog(onDismiss = { showApkDownloadDialog = false })
    }
}

@Composable
private fun AdminOverviewTab(
    videos: List<ContentItem>,
    audios: List<ContentItem>,
    documents: List<ContentItem>,
    albums: List<PhotoAlbum>,
    onDeleteVideo: (String) -> Unit,
    onToggleFeatured: (String) -> Unit,
    onTogglePublish: (String) -> Unit
) {
    val totalPhotos = albums.sumOf { it.photos.size }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    text = "सिस्टम आंकड़े (System Statistics)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(title = "वीडियो", value = "${videos.size}", color = SpiritualOrange, modifier = Modifier.weight(1f))
                    StatCard(title = "ऑडियो", value = "${audios.size}", color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                    StatCard(title = "PDF", value = "${documents.size}", color = Color(0xFF0277BD), modifier = Modifier.weight(1f))
                    StatCard(title = "फोटो", value = "$totalPhotos", color = Color(0xFF00897B), modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        }

        item {
            Text(
                text = "वीडियो प्रबंधन (Manage Videos)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        items(videos, key = { it.id }) { video ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${video.playlist} • ${video.storageType} • ${if (video.isDraft) "ड्राफ्ट" else "सार्वजनिक"}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }

                    TextButton(onClick = { onTogglePublish(video.id) }) {
                        Text(
                            text = if (video.isDraft) "प्रकाशित करें" else "ड्राफ्ट",
                            fontSize = 11.sp
                        )
                    }

                    IconButton(onClick = { onToggleFeatured(video.id) }) {
                        Icon(
                            imageVector = if (video.isFeatured) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Toggle Featured",
                            tint = SpiritualOrange
                        )
                    }

                    IconButton(onClick = { onDeleteVideo(video.id) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Video",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.titleLarge.copy(color = color, fontWeight = FontWeight.Bold))
            Text(text = title, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Medium))
        }
    }
}

// ----------------------------------------------------
// REUSABLE UPLOAD PROGRESS + RETRY BANNER
// ----------------------------------------------------
@Composable
private fun UploadStatusSection(
    isUploading: Boolean,
    progress: Float,
    progressText: String,
    errorMessage: String?,
    successMessage: String?,
    onRetry: (() -> Unit)? = null
) {
    if (isUploading) {
        Card(
            modifier = Modifier.fillMaxWidth().testTag("upload_progress_card"),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = progressText.ifBlank { "अपलोड जारी है..." },
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF01579B)
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = SkyBluePrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(progress.coerceIn(0f, 1f) * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF01579B))
                )
            }
        }
    }

    if (!errorMessage.isNullOrBlank()) {
        Card(
            modifier = Modifier.fillMaxWidth().testTag("upload_error_card"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (onRetry != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = onRetry) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("पुनः प्रयास करें (Retry)")
                    }
                }
            }
        }
    }

    if (!successMessage.isNullOrBlank()) {
        Card(
            modifier = Modifier.fillMaxWidth().testTag("upload_success_card"),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = successMessage,
                    color = Color(0xFF1B5E20),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

// ----------------------------------------------------
// TAB 1: UPLOAD VIDEO (Gallery + YouTube / Shorts + Custom Thumbnail)
// ----------------------------------------------------
@Composable
private fun AdminUploadVideoTab() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var mediaUrl by remember { mutableStateOf("") }
    var customThumbUrl by remember { mutableStateOf("") }
    var mimeType by remember { mutableStateOf("") }
    var fileSizeBytes by remember { mutableStateOf(0L) }
    var storageType by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(KnowledgeCategory.SATSANG) }
    var playlist by remember { mutableStateOf("Sant Rampal Ji Maharaj Satsang") }
    var duration by remember { mutableStateOf("") }
    var isDraft by remember { mutableStateOf(false) }

    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableFloatStateOf(0f) }
    var uploadProgressMsg by remember { mutableStateOf("") }
    var uploadError by remember { mutableStateOf<String?>(null) }
    var uploadSuccess by remember { mutableStateOf<String?>(null) }
    var lastFailedVideoUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val extractedYtId = remember(mediaUrl) { YouTubeHelper.extractVideoId(mediaUrl) }
    val previewThumbnail = remember(customThumbUrl, extractedYtId) {
        when {
            customThumbUrl.isNotBlank() -> customThumbUrl
            extractedYtId.isNotEmpty() -> YouTubeHelper.getThumbnailUrl(extractedYtId)
            else -> ""
        }
    }

    fun startVideoUpload(uri: Uri) {
        lastFailedVideoUri = uri
        isUploading = true
        uploadProgress = 0f
        uploadError = null
        uploadSuccess = null
        scope.launch {
            when (val res = MediaUploadManager.uploadPickedFile(context, uri, UploadKind.VIDEO) { pct, msg ->
                uploadProgress = pct
                uploadProgressMsg = msg
            }) {
                is MediaUploadResult.Success -> {
                    isUploading = false
                    mediaUrl = res.mediaUrl
                    if (customThumbUrl.isBlank() && res.thumbnailUrl.isNotBlank()) {
                        customThumbUrl = res.thumbnailUrl
                    }
                    mimeType = res.mimeType
                    fileSizeBytes = res.sizeBytes
                    storageType = res.storageType
                    if (duration.isBlank() && res.detectedDuration.isNotBlank()) {
                        duration = res.detectedDuration
                    }
                    uploadSuccess = res.statusMessage
                }
                is MediaUploadResult.Failure -> {
                    isUploading = false
                    uploadError = res.errorMessage
                }
            }
        }
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) startVideoUpload(uri)
    }

    val videoDocLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) startVideoUpload(uri)
    }

    val thumbPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            isUploading = true
            uploadError = null
            uploadSuccess = null
            scope.launch {
                when (val res = MediaUploadManager.uploadPickedFile(context, uri, UploadKind.THUMBNAIL) { pct, msg ->
                    uploadProgress = pct
                    uploadProgressMsg = msg
                }) {
                    is MediaUploadResult.Success -> {
                        isUploading = false
                        customThumbUrl = res.mediaUrl
                        uploadSuccess = "थंबनेल इमेज लोड हो गई!"
                    }
                    is MediaUploadResult.Failure -> {
                        isUploading = false
                        uploadError = res.errorMessage
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_upload_video_tab"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "नया वीडियो / YouTube / Shorts जोड़ें",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            UploadStatusSection(
                isUploading = isUploading,
                progress = uploadProgress,
                progressText = uploadProgressMsg,
                errorMessage = uploadError,
                successMessage = uploadSuccess,
                onRetry = lastFailedVideoUri?.let { failed -> { startVideoUpload(failed) } }
            )
        }

        item {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("वीडियो शीर्षक (Title)*") },
                modifier = Modifier.fillMaxWidth().testTag("video_title_input"),
                singleLine = true
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        videoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                        )
                    },
                    enabled = !isUploading,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Videocam, contentDescription = null, tint = SpiritualOrange, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("गैलरी वीडियो", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { videoDocLauncher.launch(arrayOf("video/*")) },
                    enabled = !isUploading,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = SpiritualOrange, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("फाइल मैनेजर", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = mediaUrl,
                onValueChange = {
                    mediaUrl = it
                    storageType = ""
                },
                label = { Text("YouTube लिंक / Shorts लिंक / वीडियो पथ*") },
                placeholder = { Text("https://youtube.com/watch?v=... या /shorts/...") },
                modifier = Modifier.fillMaxWidth().testTag("video_url_input"),
                singleLine = true
            )

            if (extractedYtId.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "YouTube Video ID पहचाना गया: $extractedYtId" +
                        if (YouTubeHelper.isShortsUrl(mediaUrl)) " (YouTube Shorts)" else "",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 104.dp, height = 62.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE1F5FE)),
                    contentAlignment = Alignment.Center
                ) {
                    if (previewThumbnail.isNotBlank()) {
                        AsyncImage(
                            model = coilModelFor(previewThumbnail),
                            contentDescription = "Thumbnail Preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.Image, contentDescription = null, tint = SkyBluePrimary)
                    }
                }

                OutlinedButton(
                    onClick = {
                        thumbPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    enabled = !isUploading,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("थंबनेल फोटो चुनें (वैकल्पिक)", fontSize = 12.sp)
                }
            }
        }

        item {
            Text(text = "श्रेणी चुनें (Category):", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    KnowledgeCategory.SATSANG to "सत्संग",
                    KnowledgeCategory.SHASTRARTH to "ज्ञान चर्चा",
                    KnowledgeCategory.BHAKTI_BODH to "भक्ति बोध",
                    KnowledgeCategory.SRISHTI_RACHNA to "सृष्टि रचना"
                ).forEach { (cat, label) ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(label, fontSize = 11.sp) }
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = playlist,
                onValueChange = { playlist = it },
                label = { Text("प्लेलिस्ट (Playlist)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("अवधि (उदा. 15:30 — गैलरी वीडियो में स्वतः भर जाती है)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("विस्तृत विवरण (Description)") },
                modifier = Modifier.fillMaxWidth().height(96.dp),
                maxLines = 4
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "ड्राफ्ट के रूप में सहेजें (पब्लिक को न दिखाएं)")
                Switch(checked = isDraft, onCheckedChange = { isDraft = it })
            }
        }

        item {
            Button(
                onClick = {
                    if (isUploading) {
                        Toast.makeText(context, "कृपया अपलोड पूरा होने की प्रतीक्षा करें", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (title.isBlank() || mediaUrl.isBlank()) {
                        uploadError = "कृपया वीडियो शीर्षक और मीडिया लिंक/फ़ाइल भरें।"
                        return@Button
                    }
                    val added = SatlokRepository.addVideo(
                        title = title,
                        description = description,
                        mediaUrl = mediaUrl,
                        category = selectedCategory,
                        playlist = playlist,
                        thumbnailUrl = customThumbUrl,
                        duration = duration.ifBlank { "10:00" },
                        isDraft = isDraft,
                        mimeType = mimeType,
                        fileSizeBytes = fileSizeBytes,
                        storageType = storageType
                    )
                    if (added) {
                        uploadError = null
                        uploadSuccess = "वीडियो डेटाबेस में सफलतापूर्वक प्रकाशित हो गया!"
                        Toast.makeText(context, "वीडियो सफलतापूर्वक सहेजा गया!", Toast.LENGTH_SHORT).show()
                        title = ""
                        description = ""
                        mediaUrl = ""
                        customThumbUrl = ""
                        duration = ""
                        mimeType = ""
                        fileSizeBytes = 0L
                        storageType = ""
                        lastFailedVideoUri = null
                    } else {
                        uploadError = "वीडियो सहेजने में असफल रहा।"
                    }
                },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("publish_video_button"),
                colors = ButtonDefaults.buttonColors(containerColor = SpiritualOrange),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("वीडियो प्रकाशित करें (Publish Video)", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

// ----------------------------------------------------
// TAB 2: UPLOAD PDF / DOCUMENTS
// ----------------------------------------------------
@Composable
private fun AdminUploadDocumentTab(documents: List<ContentItem>) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var docUrl by remember { mutableStateOf("") }
    var mimeType by remember { mutableStateOf("application/pdf") }
    var fileSizeBytes by remember { mutableStateOf(0L) }
    var storageType by remember { mutableStateOf("LOCAL") }

    var isUploading by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var progressMsg by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var lastFailedUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun startDocUpload(uri: Uri) {
        lastFailedUri = uri
        isUploading = true
        progress = 0f
        errorMessage = null
        successMessage = null
        scope.launch {
            when (val res = MediaUploadManager.uploadPickedFile(context, uri, UploadKind.DOCUMENT) { pct, msg ->
                progress = pct
                progressMsg = msg
            }) {
                is MediaUploadResult.Success -> {
                    isUploading = false
                    docUrl = res.mediaUrl
                    mimeType = res.mimeType.ifBlank { "application/pdf" }
                    fileSizeBytes = res.sizeBytes
                    storageType = res.storageType
                    successMessage = res.statusMessage
                }
                is MediaUploadResult.Failure -> {
                    isUploading = false
                    errorMessage = res.errorMessage
                }
            }
        }
    }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) startDocUpload(uri)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "PDF पुस्तकें एवं दस्तावेज़ अपलोड करें",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            UploadStatusSection(
                isUploading = isUploading,
                progress = progress,
                progressText = progressMsg,
                errorMessage = errorMessage,
                successMessage = successMessage,
                onRetry = lastFailedUri?.let { uri -> { startDocUpload(uri) } }
            )
        }

        item {
            OutlinedButton(
                onClick = { pdfPickerLauncher.launch(arrayOf("application/pdf")) },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color(0xFFD32F2F))
                Spacer(modifier = Modifier.width(8.dp))
                Text("फ़ोन से PDF फ़ाइल चुनें (Select PDF)")
            }
        }

        item {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("दस्तावेज़ / पुस्तक का नाम*") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = docUrl,
                onValueChange = { docUrl = it },
                label = { Text("PDF पथ या HTTPS लिंक*") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("संक्षिप्त विवरण") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
        }

        item {
            Button(
                onClick = {
                    if (title.isBlank() || docUrl.isBlank()) {
                        errorMessage = "कृपया दस्तावेज़ का नाम और PDF फ़ाइल/लिंक भरें।"
                        return@Button
                    }
                    val ok = SatlokRepository.addDocument(
                        title = title,
                        description = description,
                        mediaUrl = docUrl,
                        mimeType = mimeType,
                        fileSizeBytes = fileSizeBytes,
                        storageType = storageType
                    )
                    if (ok) {
                        errorMessage = null
                        successMessage = "PDF दस्तावेज़ पब्लिक मीडिया सेक्शन में प्रकाशित हो गया!"
                        title = ""
                        description = ""
                        docUrl = ""
                    } else {
                        errorMessage = "दस्तावेज़ सहेजने में समस्या हुई।"
                    }
                },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SpiritualOrange)
            ) {
                Text("PDF प्रकाशित करें (Publish PDF)", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        if (documents.isNotEmpty()) {
            item {
                Text(
                    text = "प्रकाशित दस्तावेज़ (${documents.size}):",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            items(documents, key = { it.id }) { doc ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = SkyBluePrimary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(doc.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(doc.storageType, style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { SatlokRepository.deleteDocument(doc.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// TAB 3: UPLOAD AUDIO (Device Audio Picker + Stream URL)
// ----------------------------------------------------
@Composable
private fun AdminUploadAudioTab(audios: List<ContentItem>) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var audioUrl by remember { mutableStateOf("") }
    var playlist by remember { mutableStateOf("भक्ति बोध ऑडियो") }
    var duration by remember { mutableStateOf("15:00") }
    var storageType by remember { mutableStateOf("LINK") }
    var fileSizeBytes by remember { mutableStateOf(0L) }

    var isUploading by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var progressMsg by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            isUploading = true
            progress = 0f
            errorMessage = null
            successMessage = null
            scope.launch {
                when (val res = MediaUploadManager.uploadPickedFile(context, uri, UploadKind.AUDIO) { pct, msg ->
                    progress = pct
                    progressMsg = msg
                }) {
                    is MediaUploadResult.Success -> {
                        isUploading = false
                        audioUrl = res.mediaUrl
                        storageType = res.storageType
                        fileSizeBytes = res.sizeBytes
                        successMessage = res.statusMessage
                    }
                    is MediaUploadResult.Failure -> {
                        isUploading = false
                        errorMessage = res.errorMessage
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "नया ऑडियो / अमृतवाणी जोड़ें", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }

        item {
            UploadStatusSection(
                isUploading = isUploading,
                progress = progress,
                progressText = progressMsg,
                errorMessage = errorMessage,
                successMessage = successMessage
            )
        }

        item {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("ऑडियो शीर्षक (Title)*") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedButton(
                onClick = { audioPickerLauncher.launch(arrayOf("audio/*")) },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Audiotrack, contentDescription = null, tint = SpiritualOrange)
                Spacer(modifier = Modifier.width(8.dp))
                Text("फ़ोन से ऑडियो / MP3 चुनें")
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = audioUrl,
                onValueChange = { audioUrl = it },
                label = { Text("ऑडियो पथ या MP3 URL*") },
                placeholder = { Text("https://example.com/audio.mp3") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = playlist,
                onValueChange = { playlist = it },
                label = { Text("प्लेलिस्ट (Playlist)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("अवधि (उदा. 15:00)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("विवरण (Description)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Button(
                onClick = {
                    if (title.isBlank() || audioUrl.isBlank()) {
                        errorMessage = "कृपया ऑडियो शीर्षक और फ़ाइल/URL दोनों भरें।"
                        return@Button
                    }
                    val ok = SatlokRepository.addAudio(
                        title = title,
                        description = description,
                        mediaUrl = audioUrl,
                        category = KnowledgeCategory.SPIRITUAL_MUSIC,
                        playlist = playlist,
                        duration = duration,
                        fileSizeBytes = fileSizeBytes,
                        storageType = storageType
                    )
                    if (ok) {
                        errorMessage = null
                        successMessage = "ऑडियो सफलतापूर्वक प्रकाशित किया गया!"
                        title = ""
                        audioUrl = ""
                        description = ""
                    }
                },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("ऑडियो सहेजें (Save Audio)", fontWeight = FontWeight.Bold)
            }
        }

        if (audios.isNotEmpty()) {
            item {
                Text(text = "कुल ऑडियो (${audios.size}):", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            }
            items(audios, key = { it.id }) { audio ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(audio.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("${audio.playlist} • ${audio.duration}", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { SatlokRepository.deleteAudio(audio.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Audio", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// TAB 4: PHOTO ALBUMS (With real upload progress)
// ----------------------------------------------------
@Composable
private fun AdminPhotoAlbumTab(albums: List<PhotoAlbum>) {
    var newAlbumTitle by remember { mutableStateOf("") }
    var newAlbumDesc by remember { mutableStateOf("") }
    var newPhotoTitle by remember { mutableStateOf("") }
    var newPhotoUrl by remember { mutableStateOf("") }
    var selectedAlbumId by remember { mutableStateOf(albums.firstOrNull()?.id ?: "") }

    var isUploading by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var progressMsg by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val albumPhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            isUploading = true
            progress = 0f
            errorMessage = null
            successMessage = null
            scope.launch {
                when (val res = MediaUploadManager.uploadPickedFile(context, uri, UploadKind.IMAGE) { pct, msg ->
                    progress = pct
                    progressMsg = msg
                }) {
                    is MediaUploadResult.Success -> {
                        isUploading = false
                        newPhotoUrl = res.mediaUrl
                        successMessage = res.statusMessage
                    }
                    is MediaUploadResult.Failure -> {
                        isUploading = false
                        errorMessage = res.errorMessage
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            UploadStatusSection(
                isUploading = isUploading,
                progress = progress,
                progressText = progressMsg,
                errorMessage = errorMessage,
                successMessage = successMessage
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "नया फोटो एलबम बनाएं", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newAlbumTitle,
                        onValueChange = { newAlbumTitle = it },
                        label = { Text("एलबम शीर्षक") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newAlbumDesc,
                        onValueChange = { newAlbumDesc = it },
                        label = { Text("एलबम विवरण") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (newAlbumTitle.isNotBlank()) {
                                SatlokRepository.createPhotoAlbum(
                                    title = newAlbumTitle,
                                    description = newAlbumDesc,
                                    coverUrl = "https://images.unsplash.com/photo-1545239351-ef35f43d514b?auto=format&fit=crop&w=600&q=80"
                                )
                                Toast.makeText(context, "एलबम तैयार!", Toast.LENGTH_SHORT).show()
                                newAlbumTitle = ""
                                newAlbumDesc = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SpiritualOrange)
                    ) {
                        Text("एलबम बनाएं", color = Color.White)
                    }
                }
            }
        }

        if (albums.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "एलबम में नई फोटो जोड़ें (Add Photo to Album)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "एलबम चुनें:", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            albums.take(4).forEach { album ->
                                FilterChip(
                                    selected = (selectedAlbumId.ifEmpty { albums.first().id }) == album.id,
                                    onClick = { selectedAlbumId = album.id },
                                    label = { Text(album.title.take(12), fontSize = 11.sp) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {
                                albumPhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            enabled = !isUploading,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = SpiritualOrange)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "मोबाइल गैलरी से फोटो चुनें")
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = newPhotoTitle,
                            onValueChange = { newPhotoTitle = it },
                            label = { Text("फोटो शीर्षक (उदा. सत्संग पांडाल)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = newPhotoUrl,
                            onValueChange = { newPhotoUrl = it },
                            label = { Text("फोटो पथ या इमेज URL") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                val targetId = selectedAlbumId.ifEmpty { albums.first().id }
                                if (newPhotoTitle.isNotBlank() && newPhotoUrl.isNotBlank()) {
                                    SatlokRepository.addPhotoToAlbum(
                                        albumId = targetId,
                                        title = newPhotoTitle,
                                        imageUrl = newPhotoUrl,
                                        caption = "सत्य ज्ञान दर्शन"
                                    )
                                    successMessage = "फोटो एलबम में सफलतापूर्वक सहेज दी गई!"
                                    newPhotoTitle = ""
                                    newPhotoUrl = ""
                                } else {
                                    errorMessage = "कृपया शीर्षक और फोटो चुनें।"
                                }
                            },
                            enabled = !isUploading,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("फोटो एलबम में जोड़ें", color = Color.White)
                        }
                    }
                }
            }
        }

        item {
            Text(text = "विद्यमान एलबम (${albums.size}):", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        }

        items(albums, key = { it.id }) { album ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = album.title, fontWeight = FontWeight.Bold)
                        Text(text = "${album.photos.size} फोटो • ${album.description}", style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = { SatlokRepository.deleteAlbum(album.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Album", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// TAB 5: SECTION PHOTOS & LOGO MANAGEMENT
// ----------------------------------------------------
@Composable
private fun AdminSectionsAndLogoTab(appConfig: AppConfig) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "सेक्शन फोटो एवं मुख्य लोगो प्रबंधन",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            )
            Text(
                text = "गैलरी से चुनी गई फोटो स्थायी डेटाबेस में सेव होकर होम स्क्रीन पर सभी उपयोगकर्ताओं को दिखाई देती हैं।",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        item {
            SectionPhotoEditorCard(
                title = "मुख्य ऐप लोगो (In-App Logo)",
                currentValue = appConfig.customLogoUrl,
                defaultDrawableRes = R.drawable.satlok_wave_logo,
                onSave = { SatlokRepository.updateInAppLogo(it) }
            )
        }
        item {
            SectionPhotoEditorCard(
                title = "सेक्शन 1: हमारे बारे में फोटो",
                currentValue = appConfig.aboutSectionPhoto,
                defaultDrawableRes = R.drawable.satlok_wave_logo,
                onSave = { SatlokRepository.updateSectionPhoto("about", it) }
            )
        }
        item {
            SectionPhotoEditorCard(
                title = "सेक्शन 2: सृष्टि रचना फोटो",
                currentValue = appConfig.srishtiRachnaPhoto,
                defaultDrawableRes = R.drawable.img_srishti_rachna,
                onSave = { SatlokRepository.updateSectionPhoto("srishti", it) }
            )
        }
        item {
            SectionPhotoEditorCard(
                title = "सेक्शन 3: सर्व धर्मों में कबीर परमात्मा फोटो",
                currentValue = appConfig.allReligionsPhoto,
                defaultDrawableRes = R.drawable.img_all_religions,
                onSave = { SatlokRepository.updateSectionPhoto("religions", it) }
            )
        }
        item {
            SectionPhotoEditorCard(
                title = "सेक्शन 4: पवित्र ग्रंथों का सार फोटो",
                currentValue = appConfig.granthSaarPhoto,
                defaultDrawableRes = R.drawable.img_holy_books,
                onSave = { SatlokRepository.updateSectionPhoto("granth", it) }
            )
        }
        item {
            SectionPhotoEditorCard(
                title = "सेक्शन 5: आध्यात्मिक ज्ञान चर्चा फोटो",
                currentValue = appConfig.shastrarthPhoto,
                defaultDrawableRes = R.drawable.satlok_wave_logo,
                onSave = { SatlokRepository.updateSectionPhoto("shastrarth", it) }
            )
        }
    }
}

@Composable
private fun SectionPhotoEditorCard(
    title: String,
    currentValue: String?,
    defaultDrawableRes: Int,
    onSave: (String) -> Unit
) {
    var photoInput by remember(currentValue) { mutableStateOf(currentValue ?: "") }
    var isUploading by remember { mutableStateOf(false) }
    var statusError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            isUploading = true
            statusError = null
            scope.launch {
                when (val res = MediaUploadManager.uploadPickedFile(context, uri, UploadKind.IMAGE) { _, _ -> }) {
                    is MediaUploadResult.Success -> {
                        isUploading = false
                        photoInput = res.mediaUrl
                        onSave(res.mediaUrl)
                        Toast.makeText(context, "$title फोटो अपडेट हो गई!", Toast.LENGTH_SHORT).show()
                    }
                    is MediaUploadResult.Failure -> {
                        isUploading = false
                        statusError = res.errorMessage
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE1F5FE)),
                    contentAlignment = Alignment.Center
                ) {
                    if (photoInput.isNotBlank()) {
                        AsyncImage(
                            model = coilModelFor(photoInput),
                            contentDescription = title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = defaultDrawableRes),
                            contentDescription = title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        enabled = !isUploading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isUploading) "अपलोड हो रहा है..." else "गैलरी से फोटो चुनें",
                            fontSize = 12.5.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (photoInput.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(
                            onClick = {
                                photoInput = ""
                                onSave("")
                                Toast.makeText(context, "डिफ़ॉल्ट फोटो रीसेट!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("डिफ़ॉल्ट रीसेट करें", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            if (!statusError.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = statusError.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// ----------------------------------------------------
// TAB 6: SECURITY (CHANGE PASSWORD) & CLOUDINARY CONFIG
// ----------------------------------------------------
@Composable
private fun AdminSecurityAndCloudTab() {
    val context = LocalContext.current
    val initialCloud = remember { MediaUploadManager.getCloudConfig(context) }

    var currentPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var passStatus by remember { mutableStateOf<String?>(null) }

    var cloudName by remember { mutableStateOf(initialCloud.cloudName) }
    var uploadPreset by remember { mutableStateOf(initialCloud.uploadPreset) }
    var cloudStatus by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("admin_security_tab"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "एडमिन पासवर्ड बदलें (Change Password)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SkyBluePrimary)
                    )
                    OutlinedTextField(
                        value = currentPass,
                        onValueChange = { currentPass = it },
                        label = { Text("वर्तमान पासवर्ड") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = { Text("नया पासवर्ड (न्यूनतम 6 अक्षर)") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmPass,
                        onValueChange = { confirmPass = it },
                        label = { Text("नए पासवर्ड की पुष्टि करें") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (!passStatus.isNullOrBlank()) {
                        Text(passStatus.orEmpty(), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                    }
                    Button(
                        onClick = {
                            val res = SatlokRepository.changeAdminPassword(context, currentPass, newPass, confirmPass)
                            passStatus = res.message
                            if (res.success) {
                                currentPass = ""
                                newPass = ""
                                confirmPass = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                    ) {
                        Text("पासवर्ड अपडेट करें", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "क्लाउड स्टोरेज कनेक्शन (Cloudinary HTTPS)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SkyBluePrimary)
                    )
                    Text(
                        text = "वैकल्पिक: यदि आप वीडियो, फोटो व PDF को अपने Cloudinary क्लाउड पर भी अपलोड करना चाहते हैं तो अपना Cloud Name व Unsigned Upload Preset दर्ज करें। खाली रखने पर सभी फ़ाइलें ऐप के स्थायी Room डेटाबेस में सेव रहेंगी।",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    OutlinedTextField(
                        value = cloudName,
                        onValueChange = { cloudName = it },
                        label = { Text("Cloudinary Cloud Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uploadPreset,
                        onValueChange = { uploadPreset = it },
                        label = { Text("Unsigned Upload Preset") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (!cloudStatus.isNullOrBlank()) {
                        Text(cloudStatus.orEmpty(), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32)))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                MediaUploadManager.saveCloudConfig(context, cloudName, uploadPreset)
                                cloudStatus = if (cloudName.isNotBlank() && uploadPreset.isNotBlank()) {
                                    "Cloudinary क्लाउड कॉन्फ़िगरेशन सेव हो गया!"
                                } else {
                                    "स्थानीय स्थायी डेटाबेस मोड सक्रिय।"
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SpiritualOrange)
                        ) {
                            Text("सेटिंग सहेजें", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        if (cloudName.isNotBlank() || uploadPreset.isNotBlank()) {
                            OutlinedButton(
                                onClick = {
                                    cloudName = ""
                                    uploadPreset = ""
                                    MediaUploadManager.saveCloudConfig(context, "", "")
                                    cloudStatus = "क्लाउड सेटिंग हटा दी गई (स्थानीय स्थायी स्टोरेज सक्रिय)।"
                                }
                            ) {
                                Text("हटाएं")
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// TAB 7: ADMIN ANDROID BUILD & PACKAGING AREA
// ----------------------------------------------------
@Composable
private fun AdminBuildAreaTab(onOpenApkDownload: () -> Unit = {}) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_build_area_tab"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Android APK / AAB बिल्ड एवं डाउनलोड सेंटर",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "आधिकारिक रिलीज पैकेज स्थिति एवं डाउनलोड विनिर्देश",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Android, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "SATLOK WAVE", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Text(text = "com.satlokwave.app", style = MaterialTheme.typography.bodySmall.copy(color = SpiritualOrange))
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF2E7D32).copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "READY", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(14.dp))

                    BuildDetailRow(label = "ऐप संस्करण (Version):", value = "1.0.0 (Build 1)")
                    BuildDetailRow(label = "टारगेट SDK (Target SDK):", value = "Android 15 (API 36)")
                    BuildDetailRow(label = "न्यूनतम SDK (Min SDK):", value = "Android 7.0 (API 24)")
                    BuildDetailRow(label = "डेटाबेस इंजन:", value = "Room SQLite Persistent")
                    BuildDetailRow(label = "वीडियो प्लेयर इंजन:", value = "Media3 ExoPlayer + YouTube")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onOpenApkDownload,
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("download_apk_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = SpiritualOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "APK डाउनलोड करें (Download APK)", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(
                                context,
                                "AAB Google Play Store अपलोड के लिए तैयार है!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Build, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Google Play AAB बंडल जानकारी")
                    }
                }
            }
        }
    }
}

@Composable
private fun BuildDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        Text(text = value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
    }
}
