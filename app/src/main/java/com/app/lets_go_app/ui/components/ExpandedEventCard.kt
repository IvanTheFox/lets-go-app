package com.app.lets_go_app.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.lets_go_app.R
import com.app.lets_go_app.model.Event
import com.app.lets_go_app.navigation.navigator
import com.app.lets_go_app.ui.components.snackbar.snackbar
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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