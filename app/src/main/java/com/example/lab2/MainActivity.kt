package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.lab2.model.Event
import com.example.lab2.model.EventProvider
import com.example.lab2.ui.theme.Lab2Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState


//region Snackbar notification implementation
fun interface PlaceholderNotifier {
    fun notifyPlaceholder()
}

val LocalPlaceholderNotifier = staticCompositionLocalOf<PlaceholderNotifier> {
    error("Notifications 'Not Implemented' were not provided")
}

fun CoroutineScope.showNotImplemented(snackbarHostState: SnackbarHostState, notImplementedMessage: String) {
    launch {
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(
            message = notImplementedMessage,
            withDismissAction = true,
            duration = SnackbarDuration.Short
        )
    }
}

val MaterialTheme.snackbar: PlaceholderNotifier
    @Composable @ReadOnlyComposable get() = LocalPlaceholderNotifier.current
//endregion

//region Navigation implementation
val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No navigation controller provided")
}
val MaterialTheme.navController: NavHostController
    @Composable @ReadOnlyComposable get() = LocalNavController.current

@Serializable
abstract class MainAppSection(
    val nameId: Int,
    val iconId: Int
): Destination

@Serializable
sealed interface Destination {
    @Serializable
    object TrendingAppSection :
        MainAppSection(R.string.trending_section, R.drawable.trending)
    @Serializable
    object SearchAppSection :
        MainAppSection(R.string.search_section, R.drawable.search)
    @Serializable
    object FavoritesAppSection :
        MainAppSection(R.string.favorite_section, R.drawable.favorites)
    @Serializable
    class ExpandedEventCard(val eventId: Int) : Destination
}

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Destination.TrendingAppSection> {
            TrendingAppSection()
        }

        composable<Destination.SearchAppSection> {
            val notifier = MaterialTheme.snackbar
            LaunchedEffect(Unit) {
                notifier.notifyPlaceholder()
            }
        }

        composable<Destination.FavoritesAppSection> {
            val notifier = MaterialTheme.snackbar
            LaunchedEffect(Unit) {
                notifier.notifyPlaceholder()
            }
        }

        composable<Destination.ExpandedEventCard> { backStackEntry ->
            val expandedEvent = backStackEntry.toRoute<Destination.ExpandedEventCard>()
            val event = EventProvider.getEventById(expandedEvent.eventId)
            MaterialTheme.snackbar::notifyPlaceholder
        }
    }
}

@Composable
fun AppNavigationBar(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.TrendingAppSection

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                val mainSections: List<MainAppSection> = listOf(
                    Destination.SearchAppSection,
                    Destination.TrendingAppSection,
                    Destination.FavoritesAppSection
                )
                mainSections.forEach { section ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(section::class)
                        } == true,
                        onClick = {
                            navController.navigate(section) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(painterResource(section.iconId), stringResource(section.nameId)) },
                        label = { Text(stringResource(section.nameId)) },
                        alwaysShowLabel = false
                    )
                }
            }
        }
    ) { contentPadding ->
        CompositionLocalProvider(LocalNavController provides navController) {
            AppNavHost(navController, startDestination, modifier = Modifier.padding(contentPadding))
        }
    }
}
//endregion

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab2Theme {
                EventsAndTicketsApp()
            }
        }
    }
}

@Composable
fun EventsAndTicketsApp() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val notImplementedMessage = "Feature not implemented yet"

    val placeholderNotifier = remember(notImplementedMessage, snackbarHostState) {
        PlaceholderNotifier {
            scope.showNotImplemented(snackbarHostState, notImplementedMessage)
        }
    }

    CompositionLocalProvider(LocalPlaceholderNotifier provides placeholderNotifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            AppNavigationBar(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun TrendingAppSection(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(EventProvider.allEvents) { event ->
            EventCard(event, false)
        }
    }
}

@Composable
fun EventCard(event: Event, isFavorite: Boolean) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                ) {
                    // Image(...)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 8.dp, top = 16.dp, bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(end = 48.dp)
                    ) {
                        Text(
                            text = event.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2
                        )
                        Text(
                            text = stringResource(event.type.typeNameId),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Дата и цена размещены внизу
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = DateTimeFormatter.ofPattern("MM.dd | HH:mm").format(event.datetime.atZone(
                                ZoneId.systemDefault())),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "${event.price}₽",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1
                        )
                    }
                }
            }

            IconButton(
                onClick = MaterialTheme.snackbar::notifyPlaceholder,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                val starIcon = if (isFavorite) {
                    R.drawable.favorites
                } else {
                    R.drawable.unfavorites
                }

                Icon(
                    painter = painterResource(id = starIcon),
                    contentDescription = "Add to Favorites",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}