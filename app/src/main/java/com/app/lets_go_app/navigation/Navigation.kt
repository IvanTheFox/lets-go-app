package com.app.lets_go_app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.app.lets_go_app.LetsGoApp
import com.app.lets_go_app.model.Event
import com.app.lets_go_app.ui.components.ExpandedEventCard
import com.app.lets_go_app.ui.components.snackbar.snackbar
import com.app.lets_go_app.ui.trending_screen.TrendingAppSection

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No Navigator was provided.")
}
val MaterialTheme.navigator: Navigator
    @Composable @ReadOnlyComposable get() = LocalNavigator.current

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
            entry<Destination.TrendingAppSection> { TrendingAppSection()  }
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
                val repository = (LocalContext.current.applicationContext as LetsGoApp).container.repository

                var event by remember(key.eventId) { mutableStateOf<Event?>(null) }
                var isLoading by remember(key.eventId) { mutableStateOf(true) }

                LaunchedEffect(key.eventId) {
                    event = repository.getEventById(key.eventId)
                    isLoading = false
                }

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    event == null -> {
                        val notifier = MaterialTheme.snackbar
                        LaunchedEffect(Unit) {
                            notifier.notifyPlaceholder()
                        }
                    }
                    else -> {
                        ExpandedEventCard(event!!)
                    }
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