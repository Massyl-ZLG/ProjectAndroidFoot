package com.example.myapplication.ui.feature.team_details

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.myapplication.model.Player
import com.example.myapplication.model.Team
import kotlin.math.min


@Composable
fun TeamDetailsScreen(state: TeamDetailsContract.State) {
    val scrollState = rememberLazyListState()
    val scrollOffset: Float = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )
    Surface(color = MaterialTheme.colors.background) {
        Column {
                Surface(elevation = 4.dp) {
                    TeamDetailsCollapsingToolbar(state.team, scrollOffset)
                }
                Spacer(modifier = Modifier.height(2.dp))

                TeamInfos(state.team)

                Spacer(modifier = Modifier.height(2.dp))
                LazyColumn(
                        state = scrollState,
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(state.teamPlayersItems) { item ->
                            PlayerRow(
                                item = item )
                        }
                }
        }
    }
}


@Composable
fun TeamInfos(
    item: Team?
){
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp,
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                item{
                    Text(
                        modifier = Modifier.padding(end = 16.dp, top = 16.dp),
                        text = "Information de l'équipe :",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 2,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                item{
                    Text(
                    modifier = Modifier.padding(end = 16.dp, top = 16.dp),
                    text = "Nom : "+item?.name ?: "",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                        fontWeight = FontWeight.Bold
                    )
                }
                item{
                    Text(
                        modifier = Modifier.padding(end = 16.dp, top = 16.dp),
                        text ="Nom court : " +item?.shortName ?: "",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 2,
                        fontWeight = FontWeight.Bold
                    )
                }
                item{
                    Text(
                        modifier = Modifier.padding(end = 16.dp, top = 16.dp),
                        text ="Addresse : " + item?.address ?: "",
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    CustomLink(item)
                }
                /*item{
                    Text(
                        modifier = Modifier.padding(end = 16.dp, top = 16.dp , bottom = 16.dp),
                        text = "Site Web : " +item?.website ?: "",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 2,
                        fontWeight = FontWeight.Bold
                    )
                }*/
        }
    }
}

@Composable
fun PlayerRow(
    item: Player,
    itemShouldExpand: Boolean = false,
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
            PlayerDetails(
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
fun PlayerDetails(
    item: Player?,
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
        if (item?.position?.trim()?.isNotEmpty() == true)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = item.position.trim(),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    maxLines = expandedLines
                )
            }
    }
}


@Composable
private fun TeamDetailsCollapsingToolbar(
    team: Team?,
    scrollOffset: Float,
) {
    val imageSize by animateDpAsState(targetValue = max(72.dp, 128.dp * scrollOffset))
    Row {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = CircleShape,
            border = BorderStroke(
                width = 2.dp,
                color = Color.Black
            ),
            elevation = 4.dp
        ) {

            LazyRow( horizontalArrangement = Arrangement.Center){
                item {
                    Image(
                        painter = rememberImagePainter(
                            data = team?.crest,

                            builder = {
                                transformations(CircleCropTransformation())
                            },

                            ),

                        modifier = Modifier.size(max(72.dp, imageSize)),
                        contentDescription = "Food category thumbnail picture",
                    )
                }
            }

        }
       /* PlayerDetails(
            item = player,
            expandedLines = (max(3f, scrollOffset * 6)).toInt(),
            modifier = Modifier
                .padding(
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxWidth()
        )*/
    }
}


// Creating a composable function
// to create a Clickable Text
// Calling this function as content
// in the above function
@Composable
fun CustomLink(
    item : Team?
){

    // Creating an annonated string
    val mAnnotatedLinkString = buildAnnotatedString {

        // creating a string to display in the Text

        val mStr  = "Site Web :"+ item?.website

        append(mStr)
        addStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            ), start = 10, end = mStr.length
        )

        // attach a string annotation that
        // stores a URL to the text "link"
        addStringAnnotation(
            tag = "URL",
            annotation = "https://www.geeksforgeeks.org",
            start = 10,
            end = mStr.length
        )

    }

    // UriHandler parse and opens URI inside
    // AnnotatedString Item in Browse
    val mUriHandler = LocalUriHandler.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(end = 16.dp, bottom = 16.dp), verticalArrangement = Arrangement.Center) {

        // ???? Clickable text returns position of text
        // that is clicked in onClick callback
        ClickableText(
            modifier = Modifier.padding(end = 16.dp, top = 16.dp),
            text = mAnnotatedLinkString,
            onClick = {
                mAnnotatedLinkString
                    .getStringAnnotations("URL", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        mUriHandler.openUri(stringAnnotation.item)
                    }
            }
        )
    }
}

