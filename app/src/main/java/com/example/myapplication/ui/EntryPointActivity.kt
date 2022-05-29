package com.example.myapplication.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.myapplication.model.Photo
import com.example.myapplication.ui.NavigationKeys.Arg.MATCH_ID
import com.example.myapplication.ui.NavigationKeys.Arg.PLAYER_ID
import com.example.myapplication.ui.NavigationKeys.Arg.TEAM_ID
import com.example.myapplication.ui.feature.match_details.MatchDetailsScreen
import com.example.myapplication.ui.feature.match_details.MatchDetailsViewModel
import com.example.myapplication.ui.feature.matches.MatchesScreen
import com.example.myapplication.ui.feature.matches.MatchesViewModel
import com.example.myapplication.ui.feature.profile.ProfileScreen
import com.example.myapplication.ui.feature.profile.ProfileViewModel
import com.example.myapplication.ui.feature.profile.uri
import com.example.myapplication.ui.feature.team_details.TeamDetailsScreen
import com.example.myapplication.ui.feature.team_details.TeamDetailsViewModel
import com.example.myapplication.ui.feature.teams.TeamsScreen
import com.example.myapplication.ui.feature.teams.TeamsViewModel
import com.example.myapplication.ui.navBar.NavBar
import com.example.myapplication.ui.theme.ComposeSampleTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow

var user: FirebaseUser? = null
@ExperimentalPermissionsApi
@AndroidEntryPoint
class EntryPointActivity : AppCompatActivity() {
    private lateinit var getCameraImage: ActivityResultLauncher<Uri>
    private lateinit var firestore: FirebaseFirestore
    private var storageReference = FirebaseStorage.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()){
                success ->
            if(success){
                Log.i("CAMERA IMAGE" , "IMAGE LOCATION :$uri")
                val photo = Photo(localUri = uri.toString())
                uploadPhoto(photo)
            }else{
                Log.i("CAMERA IMAGE" , "IMAGE NOT SAVED :$uri")
            }
        }
        setContent {
            ComposeSampleTheme {
                TeamsApp( getCameraImage)
            }
        }
    }

    private fun uploadPhoto(photo: Photo){
        var uri = Uri.parse(photo.localUri)
        val imageRef = storageReference.child("profile_pictures/${user?.uid}/${uri.lastPathSegment}")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            Log.i("ENTRY POINT UPLOAD OK" , "SUCCESS :$imageRef")
            var downloadUrl = imageRef.downloadUrl
            downloadUrl.addOnSuccessListener {
                remoteUri ->
                photo.remoteUri = remoteUri.toString()
                //updatePhotoDatabase(photo)
            }
        }
        uploadTask.addOnFailureListener{
            Log.e("ENTRY POINT UPLOAD" , "FAILED :"+it.message)
        }
    }

    private fun updatePhotoDatabase(photo: Photo) {
        user?.let {
            user ->
            var profilePhoto = firestore.collection("users")
                .document(user.uid)
                .collection("profile_pictures")

            var handle = profilePhoto.add(photo)
            handle.addOnSuccessListener {
                Log.i("ENTRY POINT UPLOAD OK" , "Upload photo metadata")
                photo.id = it.id;
                //profilePhoto = firestore.collection("users").document(it.uid).collection("profilePictures").document(photo.id).set(photo)
            }

            handle.addOnFailureListener{
                Log.e("ENTRY POINT updatePhotoDatabase" , "FAILED :"+it.message)
            }

        }
    }


}


@ExperimentalPermissionsApi
@Composable
private fun TeamsApp(getCameraImage: ActivityResultLauncher<Uri>) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {NavBar(navController)}
    ) {
    NavHost(navController, startDestination = NavigationKeys.Route.LOGIN) {

        composable(route = NavigationKeys.Route.LOGIN){
            LoginScreen(navController)
        }
        composable(route = NavigationKeys.Route.REGISTER){
            RegisterScreen(navController)
        }
        composable(route = NavigationKeys.Route.TEAMS_LIST) {

                TeamsDestination(navController)


        }
        composable(route = NavigationKeys.Route.MATCHES_LIST) {
            MatchesDestination(navController)
        }
        composable(
              route = NavigationKeys.Route.TEAM_DETAILS,
              arguments = listOf(
                  navArgument(NavigationKeys.Arg.TEAM_ID) {
                      type = NavType.StringType
                  }
              )
          ) {
              TeamDetailsDestination()
          }
        composable(
            route = NavigationKeys.Route.MATCH_DETAILS,
            arguments = listOf(
                navArgument(NavigationKeys.Arg.MATCH_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            MatchDetailsDestination()
        }
        composable(route = NavigationKeys.Route.PROFILE) {
            ProfileDestination(getCameraImage =  getCameraImage)
        }

          /*composable(route = NavigationKeys.Route.POSTS) {
            PostsDestination()
        }
        composable(route = NavigationKeys.Route.LOCATION) {
            LocationDestination()
        }*/
    }
    }
}
/*

@Composable
fun FormContainer(context : ComponentActivity){
    val auth = Firebase.auth
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        val emailValue = remember { mutableStateOf(TextField(TextFieldValue() )) }
        val passwordValue = remember { mutableStateOf(TextField( TextFieldValue() )) }


        OutlinedTextField(
            label =  { Text(text = "Email")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            value =  emailValue.value,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                emailValue.value = it
            }
        )

        OutlinedTextField(
            label =  { Text(text = "Password")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value =  passwordValue.value,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                passwordValue.value = it
            }
        )
        Spacer(modifier = Modifier.padding(8.dp))

        Button( modifier = Modifier.fillMaxWidth() ,
            onClick = { auth.createUserWithEmailAndPassword(
                 emailValue.value.text.trim(),
                passwordValue.value.text.trim(),
            ).addOnCompleteListener(context) {
                task ->
                if(task.isSuccessful){
                    Log.d("AUTH" , "SUCCESS")
                }else{
                    Log.d("AUTH" , "Failed")
                }
            }

            }) {
            Text(text = "Register")
        }
    }
}
*/


@Composable
fun LoginScreen(navController: NavHostController) {
    val auth = Firebase.auth
    val context = LocalContext.current
    val email = remember { mutableStateOf(TextFieldValue()) }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = buildAnnotatedString {
            append("Connexion")
        }, fontSize = 30.sp)
        Spacer(Modifier.size(16.dp))
        OutlinedTextField(
            value = email.value,
            onValueChange = {
                if (emailErrorState.value) {
                    emailErrorState.value = false
                }
                email.value = it
            },
            isError = emailErrorState.value,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Email*")
            },
        )
        if (emailErrorState.value) {
            Text(text = "Obligatoire")
        }
        Spacer(Modifier.size(16.dp))
        val passwordVisibility = remember { mutableStateOf(true) }
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                if (passwordErrorState.value) {
                    passwordErrorState.value = false
                }
                password.value = it
            },
            isError = passwordErrorState.value,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Password*")
            },
            visualTransformation = if (passwordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None
        )
        if (passwordErrorState.value) {
            Text(text = "Obligatoire")
        }
        Spacer(Modifier.size(16.dp))
        Button(
            onClick = { auth.signInWithEmailAndPassword(
                email.value.text.trim(),
                password.value.text.trim(),
            ).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    navController.navigate(NavigationKeys.Route.TEAMS_LIST) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }}
                }.addOnFailureListener{ exception ->
                    Toast.makeText(
                        context,
                        "Erreur de connexion !!",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("LOGIN" , "FAILED")
            } }
            ,
            content = {
                Text(text = "Connexion")
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.size(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextButton(onClick = {
                navController.navigate(NavigationKeys.Route.REGISTER) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }) {
                Text(text = "S'enregistrer ?")
            }
        }
    }
}

@Composable
fun RegisterScreen(navController: NavHostController) {
    val context = LocalContext.current
    val name = remember {
        mutableStateOf(TextFieldValue())
    }
    val auth = Firebase.auth
    val email = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val confirmPassword = remember { mutableStateOf(TextFieldValue()) }

    val nameErrorState = remember { mutableStateOf(false) }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }
    val confirmPasswordErrorState = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
    ) {

        Text(text = buildAnnotatedString {
                append("S'enregistrer")
        }, fontSize = 30.sp)

        Spacer(Modifier.size(16.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = {
                if (emailErrorState.value) {
                    emailErrorState.value = false
                }
                email.value = it
            },

            modifier = Modifier.fillMaxWidth(),
            isError = emailErrorState.value,
            label = {
                Text(text = "Email*")
            },
        )
        if (emailErrorState.value) {
            Text(text = "Obligatoire")
        }
        Spacer(modifier = Modifier.size(16.dp))

        Spacer(Modifier.size(16.dp))
        val passwordVisibility = remember { mutableStateOf(true) }
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                if (passwordErrorState.value) {
                    passwordErrorState.value = false
                }
                password.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Password*")
            },
            isError = passwordErrorState.value,

            visualTransformation = if (passwordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None
        )
        if (passwordErrorState.value) {
            Text(text = "Required")
        }

        Spacer(Modifier.size(16.dp))
        val cPasswordVisibility = remember { mutableStateOf(true) }
        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = {
                if (confirmPasswordErrorState.value) {
                    confirmPasswordErrorState.value = false
                }
                confirmPassword.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            isError = confirmPasswordErrorState.value,
            label = {
                Text(text = "Confirmer le  password*")
            },
            visualTransformation = if (cPasswordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None
        )
        if (confirmPasswordErrorState.value) {
            val msg = if (confirmPassword.value.text.isEmpty()) {
                "Obligatoire"
            } else if (confirmPassword.value.text != password.value.text) {
                "Password ne match pas"
            } else {
                ""
            }
            Text(text = msg)
        }
        Spacer(Modifier.size(16.dp))
        Button(
            onClick = {
                when {
                    email.value.text.isEmpty() -> {
                        emailErrorState.value = true
                    }
                    password.value.text.isEmpty() -> {
                        passwordErrorState.value = true
                    }
                    confirmPassword.value.text.isEmpty() -> {
                        confirmPasswordErrorState.value = true
                    }
                    confirmPassword.value.text != password.value.text -> {
                        confirmPasswordErrorState.value = true
                    }
                    else -> {
                        auth.createUserWithEmailAndPassword(
                            email.value.text.trim(),
                            password.value.text.trim(),
                        ).addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                Toast.makeText(
                                    context,
                                    "Enregistement Réussi !!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate(NavigationKeys.Route.TEAMS_LIST) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        }.addOnFailureListener{ exception ->
                            Toast.makeText(
                                context,
                                "Erreur de connexion !!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("LOGIN" , "FAILED")
                        }

                    }
                }
            },
            content = {
                Text(text = "S'enregistrer")
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.size(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextButton(onClick = {
                navController.navigate(NavigationKeys.Route.TEAMS_LIST) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }) {
                Text(text = "Connexion")
            }
        }
    }
}


@Composable
private fun TeamsDestination(navController: NavHostController) {
    val viewModel: TeamsViewModel = hiltViewModel()
    TeamsScreen(
        state = viewModel.state,
        effectFlow = viewModel.effects.receiveAsFlow(),
        onNavigationRequested = { itemId ->
            navController.navigate("${NavigationKeys.Route.TEAMS_LIST}/$itemId")
        }
    )
}

@Composable
private fun TeamDetailsDestination() {
    val viewModel: TeamDetailsViewModel = hiltViewModel()
    TeamDetailsScreen(viewModel.state)
}


@OptIn(ExperimentalCoilApi::class)
@Composable
private fun MatchesDestination(navController: NavHostController) {
    val viewModel: MatchesViewModel = hiltViewModel()
    MatchesScreen(
        state = viewModel.state,
        effectFlow = viewModel.effects.receiveAsFlow(),
        onNavigationRequested = { itemId ->
            navController.navigate("${NavigationKeys.Route.MATCHES_LIST}/$itemId")
        }
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun MatchDetailsDestination() {
    val viewModel: MatchDetailsViewModel = hiltViewModel()
    MatchDetailsScreen(viewModel.state)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ProfileDestination(getCameraImage :ActivityResultLauncher<Uri> ) {
    val viewModel: ProfileViewModel = hiltViewModel()
    ProfileScreen(viewModel.state , getCameraImage)
}





@OptIn(ExperimentalCoilApi::class)
@Composable
private fun TeamDetailsPlayerDestination() {
    val viewModel: TeamDetailsViewModel = hiltViewModel()
    TeamDetailsScreen(viewModel.state)
}




object NavigationKeys {

    object Arg {
        const val TEAM_ID = "teamId"
        const val PLAYER_ID = "playerId"
        const val MATCH_ID = "matchId"
    }

    object Route {
        const val PROFILE = "profile"
        const val LOGIN = "login"
        const val REGISTER = "register"
        // handle matches routes
        const val MATCHES_LIST = "matches_list"
        const val MATCH_DETAILS = "$MATCHES_LIST/{$MATCH_ID}"


        // handle teams routes
        const val TEAMS_LIST = "teams_list"
        const val TEAM_DETAILS = "$TEAMS_LIST/{$TEAM_ID}"
        const val TEAM_DETAILS_PLAYER = "$TEAMS_LIST/{$TEAM_ID}/{$PLAYER_ID}"
    }
}

