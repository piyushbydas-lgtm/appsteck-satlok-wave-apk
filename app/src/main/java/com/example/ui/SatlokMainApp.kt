package com.example.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.BhaktiBodhTopic
import com.example.data.model.ContentItem
import com.example.data.model.PhotoAlbum
import com.example.data.repository.SatlokRepository
import com.example.ui.components.AudioMiniPlayer
import com.example.ui.components.NavDestination
import com.example.ui.components.SatlokBottomNavBar
import com.example.ui.components.SatlokNavDrawerContent
import com.example.ui.components.TopHeader
import com.example.ui.components.VideoPlayerView
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdminLoginScreen
import com.example.ui.screens.AllReligionsScreen
import com.example.ui.screens.ApkDownloadDialog
import com.example.ui.screens.AudioBookScreen
import com.example.ui.screens.BhaktiBodhDetailScreen
import com.example.ui.screens.CampaignScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.FreeBookOrderDialog
import com.example.ui.screens.GranthSaarScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MalaCounterDialog
import com.example.ui.screens.MediaScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.NaamDikshaScreen
import com.example.ui.screens.PdfViewerDialog
import com.example.ui.screens.PhotoViewerDialog
import com.example.ui.screens.PolicyScreen
import com.example.ui.screens.ScriptureEvidenceScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.ShastrarthScreen
import com.example.ui.screens.ShortsScreen
import com.example.ui.screens.SrishtiRachnaScreen
import com.example.ui.theme.SatlokWaveTheme
import com.example.ui.theme.SpiritualOrange
import com.example.util.AudioPlaybackManager
import com.example.util.LanguageManager
import kotlinx.coroutines.launch

sealed interface AppScreen {
    data object Home : AppScreen
    data object Media : AppScreen
    data object NaamDiksha : AppScreen
    data object Shorts : AppScreen
    data object Messages : AppScreen
    data object Shastrarth : AppScreen
    data object AudioBook : AppScreen
    data object SrishtiRachna : AppScreen
    data object ScriptureEvidence : AppScreen
    data object GranthSaar : AppScreen
    data object AllReligions : AppScreen
    data object Campaigns : AppScreen
    data class BhaktiBodhDetail(val topic: BhaktiBodhTopic) : AppScreen
    data object Search : AppScreen
    data class VideoPlayer(val video: ContentItem) : AppScreen
    data object AdminLogin : AppScreen
    data object AdminDashboard : AppScreen
    data object About : AppScreen
    data object Contact : AppScreen
    data object Privacy : AppScreen
    data object Terms : AppScreen
}

@Composable
fun SatlokMainApp() {
    var isDarkTheme by remember { mutableStateOf(false) }
    val currentLanguage by LanguageManager.currentLanguage.collectAsState()
    val isAdminLoggedIn by SatlokRepository.isAdminLoggedIn.collectAsState()
    val appConfig by SatlokRepository.appConfig.collectAsState()
    val context = LocalContext.current

    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }
    var selectedPhotoViewerState by remember { mutableStateOf<Pair<PhotoAlbum, Int>?>(null) }
    var selectedDocument by remember { mutableStateOf<ContentItem?>(null) }
    var showFreeBookDialog by remember { mutableStateOf(false) }
    var showMalaCounterDialog by remember { mutableStateOf(false) }
    var showApkDownloadDialog by remember { mutableStateOf(false) }
    var infoNoticeDialog by remember { mutableStateOf<Pair<String, String>?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Android Hardware Back button handling
    BackHandler(
        enabled = drawerState.isOpen ||
                selectedDocument != null ||
                selectedPhotoViewerState != null ||
                showFreeBookDialog ||
                showMalaCounterDialog ||
                showApkDownloadDialog ||
                infoNoticeDialog != null ||
                currentScreen != AppScreen.Home
    ) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else if (selectedDocument != null) {
            selectedDocument = null
        } else if (infoNoticeDialog != null) {
            infoNoticeDialog = null
        } else if (showFreeBookDialog) {
            showFreeBookDialog = false
        } else if (showMalaCounterDialog) {
            showMalaCounterDialog = false
        } else if (showApkDownloadDialog) {
            showApkDownloadDialog = false
        } else if (selectedPhotoViewerState != null) {
            selectedPhotoViewerState = null
        } else {
            currentScreen = AppScreen.Home
        }
    }

    SatlokWaveTheme(darkTheme = isDarkTheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = currentScreen is AppScreen.Home || currentScreen is AppScreen.Media,
            drawerContent = {
                SatlokNavDrawerContent(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme },
                    currentLanguage = currentLanguage,
                    onToggleLanguage = { LanguageManager.toggleLanguage() },
                    isAdminLoggedIn = isAdminLoggedIn,
                    onNavigateHome = { currentScreen = AppScreen.Home },
                    onNavigateAbout = { currentScreen = AppScreen.About },
                    onNavigateCounter = { showMalaCounterDialog = true },
                    onNavigateContact = { currentScreen = AppScreen.Contact },
                    onNavigateCampaigns = { currentScreen = AppScreen.Campaigns },
                    onNavigateNaamDiksha = { currentScreen = AppScreen.NaamDiksha },
                    onNavigateFreeBooks = { showFreeBookDialog = true },
                    onNavigateBloodDonation = { currentScreen = AppScreen.Campaigns },
                    onNavigateBhaktiBodh = {
                        val firstTopic = SatlokRepository.bhaktiBodhTopics.value.firstOrNull()
                        if (firstTopic != null) {
                            currentScreen = AppScreen.BhaktiBodhDetail(firstTopic)
                        } else {
                            currentScreen = AppScreen.Home
                        }
                    },
                    onNavigateSettings = { currentScreen = AppScreen.Contact },
                    onNavigateAdminLogin = { currentScreen = AppScreen.AdminLogin },
                    onNavigateAdminDashboard = { currentScreen = AppScreen.AdminDashboard },
                    onDownloadApk = { showApkDownloadDialog = true },
                    onAdminLogout = {
                        SatlokRepository.logoutAdmin()
                        currentScreen = AppScreen.Home
                    },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        ) {
            val showTopHeader = currentScreen is AppScreen.Home ||
                    currentScreen is AppScreen.Media ||
                    currentScreen is AppScreen.Shorts ||
                    currentScreen is AppScreen.Messages ||
                    currentScreen is AppScreen.Shastrarth

            val showBottomNav = currentScreen is AppScreen.Home ||
                    currentScreen is AppScreen.Media ||
                    currentScreen is AppScreen.NaamDiksha ||
                    currentScreen is AppScreen.Shorts ||
                    currentScreen is AppScreen.Messages

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("satlok_app_scaffold"),
                topBar = {
                    if (showTopHeader) {
                        TopHeader(
                            appName = appConfig.appName,
                            customLogoUrl = appConfig.customLogoUrl,
                            isAdmin = isAdminLoggedIn,
                            onSearchClick = { currentScreen = AppScreen.Search },
                            onMenuClick = { scope.launch { drawerState.open() } },
                            onAdminBadgeClick = { currentScreen = AppScreen.AdminDashboard },
                            onCategoryClick = { category ->
                                when (category) {
                                    "समागम" -> {
                                        infoNoticeDialog = Pair(
                                            "वार्षिक विशाल सत्संग समागम",
                                            "जगतगुरु तत्वदर्शी संत रामपाल जी महाराज के पावन सानिध्य में सभी सतलोक आश्रमों (बरवाला, बेतुल, शामली, धुरी, सोजत) में विशाल समागम आयोजित होते हैं। अखंड पाठ, तीन दिवसीय सत्संग, विशाल भंडारा एवं रक्तदान शिविर की व्यवस्था पूर्णतः निःशुल्क रहती है।"
                                        )
                                    }
                                    "चमत्कार" -> {
                                        infoNoticeDialog = Pair(
                                            "भक्तों के अद्भुत अनुभव व चमत्कार",
                                            "लाखों श्रद्धालुओं के कैंसर, लकवा जैसी असाध्य बीमारियां और घोर आर्थिक संकट संत रामपाल जी महाराज की शरण में आकर सतभक्ति करने से समाप्त हुए हैं। विस्तृत प्रमाण देखने के लिए यूट्यूब व मीडिया सेक्शन देखें।"
                                        )
                                    }
                                    "ई-पुस्तकें" -> {
                                        showFreeBookDialog = true
                                    }
                                    "लाइव" -> {
                                        AudioPlaybackManager.play(
                                            title = "लाइव सत्संग एवं आध्यात्मिक वाणी",
                                            speaker = "संत रामपाल जी महाराज",
                                            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
                                        )
                                    }
                                    "शंका समाधान" -> {
                                        currentScreen = AppScreen.Shastrarth
                                    }
                                    "APK डाउनलोड" -> {
                                        showApkDownloadDialog = true
                                    }
                                }
                            }
                        )
                    }
                },
                bottomBar = {
                    if (showBottomNav) {
                        val currentDestination = when (currentScreen) {
                            AppScreen.Home -> NavDestination.HOME
                            AppScreen.Media -> NavDestination.MEDIA
                            AppScreen.NaamDiksha -> NavDestination.NAAM_DIKSHA
                            AppScreen.Shorts -> NavDestination.SHORTS
                            AppScreen.Messages -> NavDestination.MESSAGES
                            else -> NavDestination.HOME
                        }
                        SatlokBottomNavBar(
                            currentDestination = currentDestination,
                            onNavigate = { dest ->
                                currentScreen = when (dest) {
                                    NavDestination.HOME -> AppScreen.Home
                                    NavDestination.MEDIA -> AppScreen.Media
                                    NavDestination.NAAM_DIKSHA -> AppScreen.NaamDiksha
                                    NavDestination.SHORTS -> AppScreen.Shorts
                                    NavDestination.MESSAGES -> AppScreen.Messages
                                }
                            }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Crossfade(
                        targetState = currentScreen,
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            AppScreen.Home -> HomeScreen(
                                onNavigateToAbout = { currentScreen = AppScreen.About },
                                onNavigateToSrishtiRachna = { currentScreen = AppScreen.SrishtiRachna },
                                onNavigateToScriptureEvidence = { currentScreen = AppScreen.ScriptureEvidence },
                                onNavigateToGranthSaar = { currentScreen = AppScreen.GranthSaar },
                                onNavigateToAllReligions = { currentScreen = AppScreen.AllReligions },
                                onNavigateToBhaktiBodh = { topic -> currentScreen = AppScreen.BhaktiBodhDetail(topic) },
                                onNavigateToNaamDiksha = { currentScreen = AppScreen.NaamDiksha },
                                onNavigateToShorts = { currentScreen = AppScreen.Shorts },
                                onNavigateToShastrarth = { currentScreen = AppScreen.Shastrarth },
                                onOpenFreeBookDialog = { showFreeBookDialog = true },
                                onOpenMalaCounter = { showMalaCounterDialog = true },
                                onOpenApkDownloadDialog = { showApkDownloadDialog = true },
                                onPlayVideo = { video -> currentScreen = AppScreen.VideoPlayer(video) }
                            )

                            AppScreen.Media -> MediaScreen(
                                onPlayVideo = { video -> currentScreen = AppScreen.VideoPlayer(video) },
                                onViewPhoto = { album, index -> selectedPhotoViewerState = Pair(album, index) },
                                onOpenDocument = { doc -> selectedDocument = doc }
                            )

                            AppScreen.NaamDiksha -> NaamDikshaScreen(
                                onBack = { currentScreen = AppScreen.Home }
                            )

                            AppScreen.Shorts -> ShortsScreen(
                                onPlayVideo = { video -> currentScreen = AppScreen.VideoPlayer(video) }
                            )

                            AppScreen.Messages -> MessagesScreen()

                            AppScreen.Shastrarth -> ShastrarthScreen(
                                onPlayVideo = { video -> currentScreen = AppScreen.VideoPlayer(video) }
                            )

                            AppScreen.AudioBook -> AudioBookScreen()

                            AppScreen.Campaigns -> CampaignScreen(
                                onBack = { currentScreen = AppScreen.Home }
                            )

                            AppScreen.SrishtiRachna -> SrishtiRachnaScreen(
                                onBack = { currentScreen = AppScreen.Home },
                                onPlayVideo = { video -> currentScreen = AppScreen.VideoPlayer(video) }
                            )

                            AppScreen.ScriptureEvidence -> ScriptureEvidenceScreen(
                                onBack = { currentScreen = AppScreen.Home },
                                onPlayVideo = { video -> currentScreen = AppScreen.VideoPlayer(video) }
                            )

                            AppScreen.GranthSaar -> GranthSaarScreen(
                                onBack = { currentScreen = AppScreen.Home },
                                onPlayVideo = { video -> currentScreen = AppScreen.VideoPlayer(video) }
                            )

                            AppScreen.AllReligions -> AllReligionsScreen(
                                onBack = { currentScreen = AppScreen.Home },
                                onPlayVideo = { video -> currentScreen = AppScreen.VideoPlayer(video) }
                            )

                            is AppScreen.BhaktiBodhDetail -> BhaktiBodhDetailScreen(
                                topic = screen.topic,
                                onBack = { currentScreen = AppScreen.Home },
                                onPlayVideo = { video -> currentScreen = AppScreen.VideoPlayer(video) }
                            )

                            AppScreen.Search -> SearchScreen(
                                onBack = { currentScreen = AppScreen.Home },
                                onSelectItem = { item ->
                                    when (item.type) {
                                        com.example.data.model.MediaType.VIDEO -> currentScreen = AppScreen.VideoPlayer(item)
                                        com.example.data.model.MediaType.DOCUMENT -> selectedDocument = item
                                        else -> currentScreen = AppScreen.Home
                                    }
                                }
                            )

                            is AppScreen.VideoPlayer -> VideoPlayerView(
                                video = screen.video,
                                onClose = { currentScreen = AppScreen.Home },
                                onRelatedVideoClick = { rel -> currentScreen = AppScreen.VideoPlayer(rel) }
                            )

                            AppScreen.AdminLogin -> AdminLoginScreen(
                                onBack = { currentScreen = AppScreen.Home },
                                onLoginSuccess = { currentScreen = AppScreen.AdminDashboard }
                            )

                            AppScreen.AdminDashboard -> AdminDashboardScreen(
                                onBack = { currentScreen = AppScreen.Home },
                                onLogout = { currentScreen = AppScreen.Home }
                            )

                            AppScreen.About -> AboutScreen(onBack = { currentScreen = AppScreen.Home })

                            AppScreen.Contact -> ContactScreen(onBack = { currentScreen = AppScreen.Home })

                            AppScreen.Privacy -> PolicyScreen(
                                title = "गोपनीयता नीति (Privacy Policy)",
                                content = """
                                    SATLOK WAVE एप्लिकेशन उपयोगकर्ताओं की निजता और सुरक्षा का पूर्ण सम्मान करता है।
                                    
                                    1. डेटा संग्रह: हम किसी भी सामान्य उपयोगकर्ता की कोई व्यक्तिगत गोपनीय जानकारी (जैसे पासवर्ड, बैंकिंग विवरण या फोन कॉन्टैक्ट्स) एकत्र नहीं करते हैं।
                                    2. सार्वजनिक पहुंच: सामान्य उपयोगकर्ताओं के लिए समस्त आध्यात्मिक ज्ञान, सत्संग, वीडियो, ऑडियो और पुस्तकें पूर्णतः निःशुल्क और बिना किसी लॉगिन के उपलब्ध हैं।
                                    3. सुरक्षा: एडमिन लॉगिन और डेटा संपादन केवल अधिकृत स्वामी के लिए एन्क्रिप्टेड क्रेडेंशियल्स द्वारा सुरक्षित है।
                                    4. थर्ड-पार्टी सेवाएं: वीडियो और मीडिया प्रदर्शन के लिए यूट्यूब और सुरक्षित क्लाउड सीडीएन का उपयोग किया जाता है।
                                """.trimIndent(),
                                onBack = { currentScreen = AppScreen.Home }
                            )

                            AppScreen.Terms -> PolicyScreen(
                                title = "नियम व शर्तें (Terms & Conditions)",
                                content = """
                                    SATLOK WAVE एप्लिकेशन का उपयोग करने के नियम व शर्तें:
                                    
                                    1. उद्देश्य: यह ऐप केवल आध्यात्मिक एवं सामाजिक उत्थान, नशामुक्ति और पूर्ण मोक्ष ज्ञान के प्रचार हेतु है।
                                    2. कॉपीराइट: समस्त सत्संग वीडियो एवं विचार जगतगुरु तत्वदर्शी संत रामपाल जी महाराज के अधिकृत आध्यात्मिक प्रवचनों पर आधारित हैं।
                                    3. उचित उपयोग: किसी भी सामग्री का दुरुपयोग, विकृतिकरण अथवा अनधिकृत व्यावसायिक उपयोग पूर्णतः वर्जित है।
                                    4. अधिकार: प्रशासक को किसी भी समय सामग्री को अद्यतन अथवा संशोधित करने का सर्वाधिकार सुरक्षित है।
                                """.trimIndent(),
                                onBack = { currentScreen = AppScreen.Home }
                            )
                        }
                    }

                    // Floating Mini Audio Player above bottom bar
                    if (showBottomNav) {
                        AudioMiniPlayer(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }

        // Full Screen Photo Viewer Dialog
        selectedPhotoViewerState?.let { (album, index) ->
            PhotoViewerDialog(
                album = album,
                initialIndex = index,
                onDismiss = { selectedPhotoViewerState = null }
            )
        }

        // Full Screen PDF Document Viewer Dialog
        selectedDocument?.let { doc ->
            PdfViewerDialog(
                document = doc,
                onDismiss = { selectedDocument = null }
            )
        }

        // Free Book Order Dialog
        if (showFreeBookDialog) {
            FreeBookOrderDialog(onDismiss = { showFreeBookDialog = false })
        }

        // Mala / Simran Counter Dialog
        if (showMalaCounterDialog) {
            MalaCounterDialog(onDismiss = { showMalaCounterDialog = false })
        }

        // Direct In-App APK Download Dialog
        if (showApkDownloadDialog) {
            ApkDownloadDialog(onDismiss = { showApkDownloadDialog = false })
        }

        // Samagam / Chamatkar Info Notice Dialog
        infoNoticeDialog?.let { (title, description) ->
            AlertDialog(
                onDismissRequest = { infoNoticeDialog = null },
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                text = {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { infoNoticeDialog = null },
                        colors = ButtonDefaults.buttonColors(containerColor = SpiritualOrange),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("ठीक है", color = Color.White)
                    }
                }
            )
        }
    }
}
