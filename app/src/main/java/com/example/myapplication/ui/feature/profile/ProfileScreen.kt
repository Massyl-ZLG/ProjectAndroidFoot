package com.example.myapplication.ui.feature.profile

import android.Manifest
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.User
import com.example.myapplication.ui.NavigationKeys
import com.example.myapplication.ui.feature.teams.TeamsContract
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalPermissionsApi
@Composable
fun ProfileScreen(
    state: ProfileContract.State)
{
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
    )
    {

        displayProfilInfos(state.user)
        /*when (val state = viewModel.state.collectAsState().value) {
            is State.Loading ->
                LoadingBar()
            is State.Failed -> {
                LaunchedEffect(scaffoldState.snackbarHostState) {
                    scaffoldState.snackbarHostState.showSnackbar(
                        "Rien à afficher",
                        duration = SnackbarDuration.Long
                    )
                }
            }
            is State.Success -> Column {
                Text(text = state.data.toString())
                PostCreator {
                    viewModel.addPost(it)
                }
            }
        }
    }*/
        //FeatureThatRequiresCameraPermission()
    }
}



@Composable
private fun displayProfilInfos(item : User?){
    val name = remember  { mutableStateOf(TextFieldValue()) }
    val nameErrorState = remember { mutableStateOf(false) }
    Log.d("USER NAME" , item?.name.toString())
    Column( modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center) {

        Text(text = buildAnnotatedString {
            append("Profil")
        }, fontSize = 30.sp)

        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = "Email actuel : "+(item?.email ?: " Aucun mail actuel") )

        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = "Nom actuel : "+(item?.name ?: " Aucun nom actuel") )

        OutlinedTextField(
            value = name.value,
            onValueChange = {
               if (nameErrorState.value) {
                    nameErrorState.value = false
                }
                name.value = it
                Log.d("PROFIL SCREEN" , item?.name.toString())
            },
            isError = nameErrorState.value,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Nom")
            },
        )

        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            onClick = {
                updateProfile(name.value.text)

            },
            content = {
                Text(text = "Updater le profil")
            },
            modifier = Modifier.fillMaxWidth(),
        )
        
        Button(
            onClick = {
                updateProfile(name.value.text)

            },
            content = {
                Text(text = "Updater le profil")
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
private fun updateProfile( name : String ) {
    Log.d("NAME" , name);
    val auth = Firebase.auth
    val user = auth.currentUser
    val context = LocalContext
    user?.let { user ->
        val username = name
        //val photoURI = Uri.parse("android.resource://$packageName/${R.drawable.logo_black_square}")
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
                user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                       Log.d("PROFIL SCREEN" , "PROFIL UPDATED")
                    }
                }.addOnFailureListener{ exception ->
                    Log.d("PROFIL SCREEN" , "PROFIL NOTTT UPDATED")
                }
        }
    }
}

@ExperimentalPermissionsApi
@Composable
private fun FeatureThatRequiresCameraPermission() {
    val bitmapFromCamera = remember { mutableStateOf<Bitmap?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            bitmapFromCamera.value = it
        }
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    when (cameraPermissionState.status) {
        // If the camera permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
            if (bitmapFromCamera.value == null) {
                Button(onClick = { launcher.launch() }) {
                    Text("Take a picture")
                }
            } else {
                bitmapFromCamera.let {
                    val data = it.value
                    if (data != null) {
                        Image(
                            bitmap = data.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
        is PermissionStatus.Denied -> {
            Column {
                val status = cameraPermissionState.status as PermissionStatus.Denied
                val textToShow = if (status.shouldShowRationale) {
                    "The camera is important for this app. Please grant the permission."
                } else {
                    "Camera permission required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow)
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }
}
