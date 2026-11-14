package com.app.lets_go_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.app.lets_go_app.navigation.*
import com.app.lets_go_app.ui.components.snackbar.*
import com.app.lets_go_app.ui.theme.Lab2Theme

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