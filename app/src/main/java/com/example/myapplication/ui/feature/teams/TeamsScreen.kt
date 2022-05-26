package com.example.myapplication.ui.feature.teams

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.myapplication.model.Team
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach


@ExperimentalCoilApi
@Composable
fun TeamsScreen(
    state: TeamsContract.State,
    effectFlow: Flow<TeamsContract.Effect>?,
    onNavigationRequested: (itemId: String) -> Unit
){
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    // Listen for side effects from the VM
   /*LaunchedEffect(effectFlow) {
        effectFlow?.onEach { effect ->
            if (effect is TeamsContract.Effect.DataWasLoaded)
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Teams are loaded.",
                    duration = SnackbarDuration.Short
                )
        }?.collect()
    }*/
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TeamsAppBar()
        },
    ) {
        Box {
            TeamsList(teams = state.teams) { itemId ->
                onNavigationRequested(itemId)
            }
            if (state.isLoading)
                LoadingBar()
        }
    }
}

@Composable
private fun TeamsAppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Home,
                modifier = Modifier.padding(horizontal = 12.dp),
                contentDescription = "Action icon"
            )
        },
        title ={"app NAme  "},
        backgroundColor = MaterialTheme.colors.background
    )
}

@Composable
fun TeamsList(
    teams: List<Team>,
    onItemClicked: (id: String) -> Unit = { }
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(teams) { team ->
            TeamRow(item = team, itemShouldExpand = true, onItemClicked = onItemClicked)
        }
    }
}


@Composable
fun TeamRow(
    item: Team,
    itemShouldExpand: Boolean = false,
    iconTransformationBuilder: ImageRequest.Builder.() -> Unit = { },
    onItemClicked: (id: String) -> Unit = { }
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { onItemClicked(item.id) }
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        Row(modifier = Modifier.animateContentSize()) {
            Box(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                TeamThumbnail(item.crest, iconTransformationBuilder)
            }
            TeamDetails(
                item = item,
                expandedLines = if (expanded) 10 else 2,
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 24.dp,
                        bottom = 24.dp
                    )
                    .fillMaxWidth(0.80f)
                    .align(Alignment.CenterVertically)
            )
            /*if (itemShouldExpand)
                Box(
                    modifier = Modifier
                        .align(if (expanded) Alignment.Bottom else Alignment.CenterVertically)
                        .noRippleClickable { expanded = !expanded }
                ) {
                    ExpandableContentIcon(expanded)
                }*/
        }
    }
}

@Composable
private fun ExpandableContentIcon(expanded: Boolean) {
    Icon(
        imageVector = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown,
        contentDescription = "Expand row icon",
        modifier = Modifier
            .padding(all = 16.dp)
    )
}

@Composable
fun TeamDetails(
    item: Team?,
    expandedLines: Int,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = item?.name ?: "",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (item?.name?.trim()?.isNotEmpty() == true)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = item.name.trim(),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    maxLines = expandedLines
                )
            }
    }
}


@Composable
fun TeamThumbnail(
    crest: String,
    iconTransformationBuilder: ImageRequest.Builder.() -> Unit
) {
    Image(
        painter = rememberImagePainter(
            data = crest,
            builder = iconTransformationBuilder
        ),
        modifier = Modifier
            .size(88.dp)
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
        contentDescription = "Team picture",
    )
}

@Composable
fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

