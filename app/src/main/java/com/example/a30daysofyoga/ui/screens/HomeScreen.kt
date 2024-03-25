package com.example.a30daysofyoga.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a30daysofyoga.R
import com.example.a30daysofyoga.data.YogaRepository
import com.example.a30daysofyoga.models.YogaPosition

@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreen() {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately
            targetState = true
        }
    }
    Scaffold(
        topBar = {
            YogaAppBar()
        }
    ) {innerPadding ->
        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy
                )
            ),
            exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(
                    count = YogaRepository.yogaPositions.size,
                    key = {
                        YogaRepository.yogaPositions[it].title
                    }
                ) { index ->
                    YogaPositionItem(
                        yogaPosition = YogaRepository.yogaPositions[index],
                        itemNumber = index + 1,
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = R.dimen.padding_medium),
                                end = dimensionResource(id = R.dimen.padding_medium),
                                top = dimensionResource(id = R.dimen.padding_medium)
                            )
                            // Animate each list item to slide in vertically
                            .animateEnterExit(
                                enter = slideInVertically(
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessMedium,
                                        dampingRatio = Spring.DampingRatioNoBouncy
                                    ),
                                    initialOffsetY = {
                                        it * (index + 1)
                                    } // staggered entrance
                                )
                            )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayLarge
            )
        }
    )
}

@Composable
fun YogaPositionItem(
    yogaPosition: YogaPosition,
    itemNumber: Int,
    modifier: Modifier = Modifier
) {
    var expanded by remember{
        mutableStateOf(false)
    }
    val color by animateColorAsState(
        targetValue = if(expanded) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer,
        label = ""
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .clickable {
                expanded = !expanded
            },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = color)
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Text(
                text = stringResource(id = R.string.day_s, itemNumber),
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = stringResource(id = yogaPosition.title),
                style = MaterialTheme.typography.displayMedium
            )
            YogaPositionImage(
                imageResourceId = yogaPosition.image,
                contentDescription = yogaPosition.title,
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.padding_small))
            )
            if(expanded){
                Text(
                    text = stringResource(id = yogaPosition.description),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun YogaPositionImage(
    @DrawableRes imageResourceId: Int,
    @StringRes contentDescription: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = imageResourceId),
        contentDescription = stringResource(id = contentDescription),
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
    )
}

