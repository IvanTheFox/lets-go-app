package com.app.lets_go_app

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.lets_go_app.model.Event
import com.app.lets_go_app.model.EventProvider
import com.app.lets_go_app.ui.theme.Lab2Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

//region Snackbar notification implementation
fun interface PlaceholderNotifier {
    fun notifyPlaceholder()
}

val LocalPlaceholderNotifier = staticCompositionLocalOf<PlaceholderNotifier> {
    error("Notifications 'Not Implemented' were not provided")
}

val MaterialTheme.snackbar: PlaceholderNotifier
    @Composable @ReadOnlyComposable get() = LocalPlaceholderNotifier.current

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
//endregion

//region Navigation implementation
//region Navigation destinations
@Serializable
sealed class MainAppSection(
    val nameId: Int,
    val iconId: Int
): Destination

@Serializable
sealed interface Destination: NavKey {
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
//endregion

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No Navigator was provided.")
}
val MaterialTheme.navigator: Navigator
    @Composable @ReadOnlyComposable get() = LocalNavigator.current

class NavigationState(
    activeSection: MutableState<MainAppSection>,
    val backStacks: Map<MainAppSection, NavBackStack<NavKey>>
) {
    var activeSection by activeSection
    val currentBackStack: NavBackStack<NavKey>
        get() = backStacks[activeSection] ?: error("Tried to switch to an unknown app section.")
}

class Navigator(val state: NavigationState) {
    fun navigate(destination: Destination) {
        if (destination is MainAppSection) {
            state.activeSection = destination
        } else {
            state.currentBackStack.add(destination)
        }
    }

    fun goBack() {
        state.currentBackStack.removeLastOrNull()
    }
}

@Composable
fun rememberNavState(
    startAppSection: MainAppSection,
    appSections: Set<MainAppSection>
): NavigationState {
    val activeSection = rememberSerializable(startAppSection, appSections) {
        mutableStateOf(startAppSection)
    }

    val backStacks = appSections.associateWith {
        key: Destination -> rememberNavBackStack(key)
    }

    return remember(startAppSection, appSections) {
        NavigationState(activeSection, backStacks)
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navigator = MaterialTheme.navigator
    NavDisplay(
        modifier = modifier,
        backStack = navigator.state.currentBackStack,
        onBack = { navigator.goBack() },
        entryProvider = entryProvider {
            entry<Destination.TrendingAppSection> { TrendingAppSection() }
            entry<Destination.SearchAppSection> {
                val notifier = MaterialTheme.snackbar
                LaunchedEffect(Unit) {
                    notifier.notifyPlaceholder()
                }
            }
            entry<Destination.FavoritesAppSection> {
                val notifier = MaterialTheme.snackbar
                LaunchedEffect(Unit) {
                    notifier.notifyPlaceholder()
                }
            }
            entry<Destination.ExpandedEventCard> { key ->
                val event = EventProvider.getEventById(key.eventId)
                if (event == null) {
                    val notifier = MaterialTheme.snackbar
                    LaunchedEffect(Unit) {
                        notifier.notifyPlaceholder()
                    }
                } else {
                    ExpandedEventCard(event)
                }
            }
        }
    )
}

@Composable
fun AppNavigationBar() {
    val navigator = MaterialTheme.navigator
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        val mainSections: List<MainAppSection> = listOf(
            Destination.SearchAppSection,
            Destination.TrendingAppSection,
            Destination.FavoritesAppSection
        )
        mainSections.forEach { section ->
            NavigationBarItem(
                selected = navigator.state.activeSection == section,
                onClick = { navigator.navigate(section) },
                icon = { Icon(painterResource(section.iconId), stringResource(section.nameId)) },
                label = { Text(stringResource(section.nameId)) },
                alwaysShowLabel = false
            )
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
    val navigationState = rememberNavState(
        Destination.TrendingAppSection,
        setOf(
            Destination.SearchAppSection,
            Destination.TrendingAppSection,
            Destination.FavoritesAppSection
    ))
    val navigator = Navigator(navigationState)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val notImplementedMessage = "Feature not implemented yet"

    val placeholderNotifier = remember(notImplementedMessage, snackbarHostState) {
        PlaceholderNotifier {
            scope.showNotImplemented(snackbarHostState, notImplementedMessage)
        }
    }

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalPlaceholderNotifier provides placeholderNotifier
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = { AppNavigationBar() }
        ) { contentPadding ->
            AppNavigation(Modifier.padding(contentPadding))
        }
    }
}

//region Interface design
@Composable
fun TrendingAppSection(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
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
    val navigator = MaterialTheme.navigator
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = {
            navigator.navigate(Destination.ExpandedEventCard(event.id))
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
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
                            color = MaterialTheme.colorScheme.onSurface,
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
                            text = DateTimeFormatter.ofPattern("dd.MM | HH:mm").format(
                                event.datetime.atZone(ZoneId.systemDefault())
                            ),
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
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ExpandedEventCard(
    event: Event,
    modifier: Modifier = Modifier
) {
    val isFavorite = false

    val formattedDate = remember(event.id) {
        DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm")
            .format(event.datetime.atZone(ZoneId.systemDefault()))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 15.dp)
        ) {
            IconButton(onClick = MaterialTheme.navigator::goBack) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = MaterialTheme.snackbar::notifyPlaceholder
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isFavorite) R.drawable.favorites
                        else R.drawable.unfavorites
                    ),
                    contentDescription = stringResource(
                        if (isFavorite) R.string.remove_from_favorites
                        else R.string.add_to_favorites
                    ),
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(event.type.typeNameId),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Категория и название
            Text(
                text = stringResource(event.type.typeNameId).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Характеристика
            EventInfoRow(
                label = stringResource(R.string.event_date),
                value = formattedDate
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )
            EventInfoRow(
                label = stringResource(R.string.event_price),
                value = "${event.price}₽"
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            EventInfoRow(
                label = stringResource(R.string.event_favorited_times),
                value = event.timesFavorited.toString()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.about_event),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.event_description_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Купить билет
            Button(
                onClick = MaterialTheme.snackbar::notifyPlaceholder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(stringResource(R.string.buy_ticket))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun EventInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
//endregion