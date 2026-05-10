package com.example.app.ui.theme

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.Locale
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.app.data.PoemRepository
import com.example.app.model.Poem
import com.example.app.model.Poet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoemApp(
    poems: List<Poem>,
    poets: List<Poet>,
    specialPoems: List<Poem>,
    context: Context,
    language: String,
    darkTheme: Boolean,
    onLanguageChange: (String) -> Unit,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    val favorites = rememberSaveable { mutableStateOf(PoemRepository.loadFavorites(context)) }

    fun toggleFavorite(index: Int) {
        val newFavorites = if (favorites.value.contains(index)) favorites.value - index
        else favorites.value + index
        favorites.value = newFavorites
        PoemRepository.saveFavorites(context, newFavorites)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    selected = currentDestination == "list",
                    onClick = { navController.navigate("list") { popUpTo("list") { inclusive = true } } },
                    icon = { Icon(Icons.Default.Book, null) },
                    label = { Text(if (language == "kn") "ಕವನಗಳು" else "Poems") }
                )
                NavigationBarItem(
                    selected = currentDestination == "favorites",
                    onClick = { navController.navigate("favorites") },
                    icon = { Icon(if (currentDestination == "favorites") Icons.Default.Favorite else Icons.Default.FavoriteBorder, null) },
                    label = { Text(if (language == "kn") "ಮೆಚ್ಚಿನವುಗಳು" else "Favorites") }
                )
                NavigationBarItem(
                    selected = currentDestination?.startsWith("poet") == true,
                    onClick = { navController.navigate("poets") },
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text(if (language == "kn") "ಕವಿ ಮಂಟಪ" else "Kavi Mandapa") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "list",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("list") {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { 
                                Text(
                                    if (language == "kn") "ಕಾವ್ಯ-ಕಣಜ" else "Kavya-Kanaja",
                                    fontWeight = FontWeight.ExtraBold
                                ) 
                            },
                            actions = {
                                IconButton(onClick = {
                                    onLanguageChange(if (language == "kn") "en" else "kn")
                                }) {
                                    Text(
                                        text = if (language == "kn") "ಕ" else "A",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(onClick = onThemeToggle) {
                                    Icon(
                                        imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Theme"
                                    )
                                }
                            }
                        )
                    }
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        PoemListScreen(
                            poems,
                            specialPoems,
                            favorites.value,
                            ::toggleFavorite,
                            language
                        ) {
                            navController.navigate("detail/$it")
                        }
                    }
                }
            }

            composable("favorites") {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    if (language == "kn") "ನನ್ನ ಮೆಚ್ಚಿನವುಗಳು" else "My Favorites",
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        )
                    }
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        val favoritePoems = poems.mapIndexed { i, p -> i to p }.filter { favorites.value.contains(it.first) }
                        if (favoritePoems.isEmpty()) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    if (language == "kn") "ಇನ್ನೂ ಯಾವುದೇ ಮೆಚ್ಚಿನ ಕವನಗಳಿಲ್ಲ" else "No favorite poems yet",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                                itemsIndexed(favoritePoems) { _, (index, poem) ->
                                    PoemItem(
                                        poem,
                                        true,
                                        { toggleFavorite(index) },
                                        language
                                    ) {
                                        navController.navigate("detail/$index")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            composable("poets") {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { 
                                Text(
                                    if (language == "kn") "ಕವಿ ಮಂಟಪ" else "Kavi Mandapa",
                                    fontWeight = FontWeight.ExtraBold
                                ) 
                            }
                        )
                    }
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        PoetListScreen(poets, language) { poetIndex ->
                            navController.navigate("poet_detail/$poetIndex")
                        }
                    }
                }
            }

            composable("poet_detail/{index}",
                arguments = listOf(navArgument("index") { type = NavType.IntType })
            ) {
                val index = it.arguments?.getInt("index") ?: 0
                PoetDetailScreen(poets[index], language) {
                    navController.popBackStack()
                }
            }

            composable("detail/{index}",
                arguments = listOf(navArgument("index") { type = NavType.IntType })
            ) {
                val index = it.arguments?.getInt("index") ?: 0

                PoemDetailScreen(
                    poem = poems[index],
                    isFavorite = favorites.value.contains(index),
                    onToggleFavorite = { toggleFavorite(index) },
                    context = context,
                    language = language
                ) {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
fun PoemListScreen(
    poems: List<Poem>,
    specialPoems: List<Poem>,
    favorites: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    language: String,
    onPoemClick: (Int) -> Unit
) {

    var search by remember { mutableStateOf("") }

    val filtered = poems.mapIndexed { i, p -> i to p }.filter {
        val title = if (language == "kn") it.second.title else it.second.titleEn
        title.contains(search, true)
    }

    val poemOfDay = PoemRepository.getPoemOfTheDay(specialPoems)

    Column {

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text(if (language == "kn") "ಹುಡುಕಿ..." else "Search poems...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        )

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {

            item {
                PoemOfDayCard(poemOfDay, language)
            }

            itemsIndexed(filtered) { _, (index, poem) ->
                PoemItem(
                    poem,
                    favorites.contains(index),
                    { onToggleFavorite(index) },
                    language
                ) {
                    onPoemClick(index)
                }
            }
        }
    }
}

@Composable
fun PoetListScreen(
    poets: List<Poet>,
    language: String,
    onPoetClick: (Int) -> Unit
) {
    val context = LocalContext.current
    
    // Softer, more blended gradient for light mode
    val gradient = Brush.verticalGradient(
        0.0f to MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
        0.5f to MaterialTheme.colorScheme.surface,
        1.0f to MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.15f)
    )

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(gradient),
        contentPadding = PaddingValues(bottom = 24.dp, start = 16.dp, end = 16.dp, top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = if (language == "kn") "ಕವಿ ಮಂಟಪ" else "Kavi Mandapa",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (language == "kn") "ಕನ್ನಡ ಸಾಹಿತ್ಯದ ಧ್ರುವತಾರೆಗಳು" else "The guiding stars of Kannada literature",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        itemsIndexed(poets) { index, poet ->
            OutlinedCard(
                onClick = { onPoetClick(index) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ),
                border = CardDefaults.outlinedCardBorder(enabled = true).copy(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        )
                    )
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val imageRes = context.resources.getIdentifier(poet.image, "drawable", context.packageName)
                    if (imageRes != 0) {
                        Image(
                            painter = painterResource(imageRes),
                            contentDescription = poet.name,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (if (language == "kn") poet.name else poet.nameEn).take(1),
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(Modifier.width(20.dp))
                    
                    Column(Modifier.weight(1f)) {
                        Text(
                            if (language == "kn") poet.name else poet.nameEn,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Text(
                            if (language == "kn") poet.info else poet.infoEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 18.sp
                        )
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = if (language == "kn") "ಹೆಚ್ಚು ತಿಳಿಯಿರಿ →" else "Explore more →",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoetDetailScreen(
    poet: Poet,
    language: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.surface
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (language == "kn") poet.name else poet.nameEn) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(gradient)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageRes = context.resources.getIdentifier(poet.image, "drawable", context.packageName)
            if (imageRes != 0) {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = poet.name,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier.size(160.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    tonalElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = (if (language == "kn") poet.name else poet.nameEn).take(1),
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(32.dp))
            
            Text(
                if (language == "kn") "ಕವಿಯ ಪರಿಚಯ" else "About the Poet",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            
            HorizontalDivider(Modifier.padding(vertical = 12.dp).width(60.dp), thickness = 3.dp, color = MaterialTheme.colorScheme.primary)
            
            Text(
                if (language == "kn") poet.info else poet.infoEn,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 28.sp
            )
            
            Spacer(Modifier.height(40.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        if (language == "kn") "ಪ್ರಮುಖ ಸಾಧನೆಗಳು" else "Notable Achievements",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        if (language == "kn") poet.achievements else poet.achievementsEn,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PoemOfDayCard(poem: Poem, language: String) {

    val title = if (language == "kn") poem.title else poem.titleEn
    val content = if (language == "kn") poem.content else poem.contentEn

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(20.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    if (language == "kn") "🌅 ದಿನದ ಕವನ" else "🌅 Poem of the Day",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                title, 
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                content.take(120) + "...",
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f),
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun PoemItem(
    poem: Poem,
    isFav: Boolean,
    onFav: () -> Unit,
    language: String,
    onClick: () -> Unit
) {

    val title = if (language == "kn") poem.title else poem.titleEn
    val author = if (language == "kn") poem.author else poem.authorEn

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {

        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(Modifier.weight(1f)) {
                Text(
                    title, 
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    author, 
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            val scale by animateFloatAsState(
                if (isFav) 1.2f else 1f,
                animationSpec = spring()
            )

            IconButton(onClick = onFav) {
                Icon(
                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isFav) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.scale(scale)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoemDetailScreen(
    poem: Poem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    context: Context,
    language: String,
    onBack: () -> Unit
) {

    val mediaPlayer = remember { MediaPlayer() }
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var isTtsInitialized by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsInitialized = true
            }
        }
        tts = ttsInstance
        onDispose { 
            mediaPlayer.release()
            ttsInstance.stop()
            ttsInstance.shutdown()
        }
    }

    val title = if (language == "kn") poem.title else poem.titleEn
    val content = if (language == "kn") poem.content else poem.contentEn
    val meaning = if (language == "kn") poem.meaning else poem.meaningEn

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val shareText = "$title\n\n$content"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Poem"))
                    }) {
                        Icon(Icons.Default.Share, "Share")
                    }
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            null,
                            tint = if (isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                content,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 38.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 8.dp).clickable {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            if (language == "kn") "ಪದಗಳ ಅರ್ಥ ಮತ್ತು ಭಾವಾರ್ಥಕ್ಕಾಗಿ ಕೆಳಗೆ ನೋಡಿ" else "Check below for Word Meanings and Bhavartha"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    val resId = context.resources.getIdentifier(
                        poem.audio ?: "",
                        "raw",
                        context.packageName
                    )
                    if (resId != 0) {
                        try {
                            if (mediaPlayer.isPlaying) {
                                mediaPlayer.pause()
                            } else {
                                mediaPlayer.reset()
                                mediaPlayer.setDataSource(context, Uri.parse("android.resource://${context.packageName}/$resId"))
                                mediaPlayer.prepare()
                                mediaPlayer.start()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Audio error", Toast.LENGTH_SHORT).show()
                        }
                    } else if (isTtsInitialized) {
                        if (tts?.isSpeaking == true) {
                            tts?.stop()
                        } else {
                            val locale = if (language == "kn") Locale("kn", "IN") else Locale.US
                            tts?.language = locale
                            tts?.speak(content, TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    } else {
                        Toast.makeText(context, "Audio not available", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                val isPlaying = mediaPlayer.isPlaying || (tts?.isSpeaking == true)
                Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null)
                Spacer(Modifier.width(12.dp))
                Text(if (language == "kn") "ಕವನ ಕೇಳಿ" else "Listen to Poem", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text(
                        if (language == "kn") "ಭಾವಾರ್ಥ" else "Meaning",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        meaning, 
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 28.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
