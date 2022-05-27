package com.example.myapplication.ui.feature.matches

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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.myapplication.model.match.Match
import kotlinx.coroutines.flow.Flow


@ExperimentalCoilApi
@Composable
fun MatchesScreen(
    state: MatchesContract.State,
    effectFlow: Flow<MatchesContract.Effect>?,
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
            MatchesAppBar()
        },
    ) {
        Box {
            MatchesList(matches = state.matches) { itemId ->
                onNavigationRequested(itemId)
            }
            if (state.isLoading)
                LoadingBar()
        }
    }
}

@Composable
private fun MatchesAppBar() {
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
fun MatchesList(
    matches: List<Match>,
    onItemClicked: (id: String) -> Unit = { }
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(matches) { match ->
            MatchRow(item = match, itemShouldExpand = true, onItemClicked = onItemClicked)
        }
    }
}


@Composable
fun MatchRow(
    item: Match,
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
                TeamThumbnail(item?.homeTeam?.crest, iconTransformationBuilder)
            }
            MatchDetails(
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
            Box(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                TeamThumbnail(item?.awayTeam?.crest, iconTransformationBuilder)
            }
        }
    }
}


@Composable
fun MatchDetails(
    item: Match?,
    expandedLines: Int,
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {



        Text(
            text = (item?.homeTeam?.shortName ?: "")+"   VS    "+ (item?.awayTeam?.shortName ?: ""),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //TeamThumbnail()


        Text(
            modifier = modifier.fillMaxWidth(),
            text = (item?.score?.fullTime?.home ?: "")+"  - " + (item?.score?.fullTime?.away ?: ""),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            maxLines = expandedLines ,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
            )

    }
}


@Composable
fun TeamThumbnail(
    crest: String?,
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
