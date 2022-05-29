package com.example.myapplication.ui.feature.match_details

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.request.ImageRequest
import com.example.myapplication.model.match.Match
import com.example.myapplication.ui.feature.matches.TeamThumbnail
import kotlin.math.min


@ExperimentalCoilApi
@Composable
fun MatchDetailsScreen(state: MatchDetailsContract.State) {
    val scrollState = rememberLazyListState()
    val scrollOffset: Float = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )
    Surface(color = MaterialTheme.colors.background) {
        Column() {
            Surface(elevation = 4.dp) {
                MatchDetailsCollapsingToolbar(state.match, scrollOffset)
            }
            Spacer(modifier = Modifier.height(2.dp))

            MatchInfos(state.match)


        }
    }
}


@Composable
fun MatchInfos(
    item: Match?
){
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp,
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        val listState = rememberLazyListState()
      LazyColumn(
          state = listState,
          modifier = Modifier
          .fillMaxWidth()
          .padding(start = 16.dp, end = 16.dp, top = 4.dp)) {

            item {TeamsVsInfo(item = item)   }

            item {ScoreInfo(item = item)   }

            item { AreaInfo(item = item) }

            item { CompetitionInfo(item = item) }

            item {  SeasonInfo(item = item) }

            item {  OddsInfo(item = item) }

        }
    }
}


@Composable
fun TeamsVsInfo(
    item: Match?,
    iconTransformationBuilder: ImageRequest.Builder.() -> Unit = { }
){
    Row(modifier = Modifier
        .animateContentSize()
        .fillMaxWidth()) {

            TeamThumbnail(item?.homeTeam?.crest, iconTransformationBuilder)
            Box(
                modifier =  Modifier,

                contentAlignment = Alignment.Center
            ) {
                Text(
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 24.dp,
                    bottom = 0.dp
                    ),
                    text =  (item?.homeTeam?.shortName ?: "Inconnue")+"   VS    "+ (item?.awayTeam?.shortName ?: "Inconnue"),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            TeamThumbnail(item?.awayTeam?.crest, iconTransformationBuilder)

    }


}

@Composable
fun AreaInfo(
    item: Match?
){
    Row(modifier = Modifier
        .animateContentSize()
        .fillMaxWidth()
        .padding(top = 16.dp)) {
            Text(
                text = "Region :  ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = item?.area?.name ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
    }


}
@Composable
fun CompetitionInfo(
    item: Match?
){

    Column() {
        Row(modifier = Modifier.animateContentSize().fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Nom de la compétition :  ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = item?.competition?.name ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
        }
        Row(modifier = Modifier.animateContentSize().fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Type de  compétition :  ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = item?.competition?.type ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
        }


        Row(modifier = Modifier.animateContentSize().fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Code de la compétition :  ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = item?.competition?.code ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
        }
    }
}



@Composable
fun SeasonInfo(
    item: Match?
){

    Column() {
        Row(modifier = Modifier.animateContentSize().fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Debut de saison le :  ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = item?.season?.startDate ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
        }
        Row(modifier = Modifier.animateContentSize().fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Fin de saison le :  ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = item?.season?.endDate ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
        }


        Row(modifier = Modifier.animateContentSize().fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Jour actuel de match:  ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = item?.season?.currentMatchday ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
        }

        Row(modifier = Modifier.animateContentSize().fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Gagnant de la saison : ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = item?.season?.winner ?: "Inconnue",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
        }
    }
}



@Composable
fun OddsInfo(
    item: Match?
){

    Column() {
        Row(modifier = Modifier.animateContentSize().fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Club Domicile gagne : ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = item?.odds?.homeWin ?: "Pas de prono",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
        }
        Row(modifier = Modifier.animateContentSize().fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Club Visiteur gagne : ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = item?.odds?.awayWin ?: "Pas de prono",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
        }



    }
}




@Composable
fun ScoreInfo(
    item: Match?
){

    Column() {
        Row(modifier = Modifier.animateContentSize().fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Gagnant : ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = item?.score?.winner ?: "Inconnue",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
        }
        Row(modifier = Modifier.animateContentSize().fillMaxWidth().padding(top = 16.dp)) {
            Text(
                text = "Score Final  : ",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = (item?.score?.fullTime?.home ?: "")+"  - " + (item?.score?.fullTime?.away ?: ""),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
            )
        }





    }
}






@Composable
private fun MatchDetailsCollapsingToolbar(
    team: Match?,
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

           /* LazyRow( horizontalArrangement = Arrangement.Center){
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
            }*/

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