package com.example.lab2

import android.os.Bundle
import androidx.compose.runtime.saveable.Saver
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lab2.model.Event
import com.example.lab2.model.EventProvider
import com.example.lab2.ui.theme.Lab2Theme
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

enum class AppSection(val nameId: Int, val icon: Int) {
    SEARCH(R.string.search_section, R.drawable.search),
    TRENDING(R.string.trending_section, R.drawable.trending),
    FAVORITES(R.string.favorite_section, R.drawable.favorites)
}

@Composable
fun EventsAndTicketsApp() {
    var selectedSection by rememberSaveable(
        stateSaver = Saver(
            save = { it.name },
            restore = { AppSection.valueOf(it) }
        )
    ) { mutableStateOf(AppSection.TRENDING) }

    MainScreen(selectedSection) {newSection ->
        selectedSection = newSection
    }
}

@Composable
fun NavigationBar(selectedSection: AppSection, changeSectionCallback: (newSection: AppSection) -> Unit) {
    NavigationBar {
        AppSection.entries.forEach { section ->
            NavigationBarItem(
                selected = section == selectedSection,
                onClick = {
                    changeSectionCallback(section)
                },
                icon = {
                    Icon(
                        painter = painterResource(section.icon),
                        contentDescription = stringResource(section.nameId)
                    )
                },
                label = { Text(stringResource(section.nameId)) },
                alwaysShowLabel = false
            )
        }
    }
}

@Composable
fun TrendingAppSection(paddingValues: PaddingValues) {
    var selectedEventId by rememberSaveable { mutableStateOf(-1) }
    val listState = rememberLazyListState()

    val selectedEvent = EventProvider.allEvents.find { it.id == selectedEventId }

    if (selectedEvent != null) {
        ExpandedEventCard(
            event = selectedEvent,
            onBack = { selectedEventId = -1 },
            modifier = Modifier.padding(paddingValues)
        )
    } else {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(EventProvider.allEvents) { event ->
                EventCard(
                    event = event,
                    isFavorite = EventProvider.isFavorite(event),
                    onClick = { selectedEventId = event.id }
                )
            }
        }
    }
}

@Composable
fun MainScreen(selectedSection: AppSection, changeSectionCallback: (newSection: AppSection) -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(selectedSection, changeSectionCallback)
        }
    ) { innerPadding ->
        when (selectedSection) {
            AppSection.SEARCH -> NotImplementedSection()
            AppSection.TRENDING -> TrendingAppSection(innerPadding)
            AppSection.FAVORITES -> NotImplementedSection()
        }
    }
}

@Composable
fun EventCard(event: Event, isFavorite: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
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
                onClick = { EventProvider.favoriteCallback(event, !isFavorite) },
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

@Composable
fun ExpandedEventCard(
    event: Event,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFavorite = EventProvider.isFavorite(event)

    val formattedDate = remember(event.id) {
        DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm")
            .format(event.datetime.atZone(ZoneId.systemDefault()))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 15.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = stringResource(R.string.back)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { EventProvider.favoriteCallback(event, !isFavorite) }
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
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Характеристика
            EventInfoRow(
                label = stringResource(R.string.event_date),
                value = formattedDate
            )
            HorizontalDivider()
            EventInfoRow(
                label = stringResource(R.string.event_price),
                value = "${event.price}₽"
            )
            HorizontalDivider()
            EventInfoRow(
                label = stringResource(R.string.event_favorited_times),
                value = event.timesFavorited.toString()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.about_event),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
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
                onClick = { //SnackBar
                     },
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
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun NotImplementedSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("This section is not implemented! ;(")
    }
}