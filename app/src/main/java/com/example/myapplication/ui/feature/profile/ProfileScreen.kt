package com.example.myapplication.ui.feature.profile

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.myapplication.R
import com.example.myapplication.model.User
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

var uri: Uri? = null
var currentImagePath: String = ""

@ExperimentalPermissionsApi
@Composable
fun ProfileScreen(
    state: ProfileContract.State,
    activityResultLauncher: ActivityResultLauncher<Uri>
)
{
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
    )
    {
        displayProfilInfos(state.user ,  activityResultLauncher )

    }
}



@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun displayProfilInfos(
    item: User?,
    activityResult: ActivityResultLauncher<Uri>
){
    val name = remember  { mutableStateOf(TextFieldValue()) }
    val nameErrorState = remember { mutableStateOf(false) }
    Log.d("USER NAME" , item?.name.toString())
    Column( modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {

        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)

        ) {
            Image(
                painterResource(R.drawable.ic_launcher_background),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
          /*  AsyncImage(
                model = "https://wallpaperaccess.com/full/137507.jpg" *//*ImageRequest.Builder(LocalContext.current)
                    .data("https://wallpaperaccess.com/full/137507.jpg")
                    .crossfade(true)
                    .build()*//*,
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "img desc",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )*/
        }
        Column(   modifier = Modifier
            .padding(20.dp)
            .background(Color.White)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.Start){
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Email actuel : "+(item?.email ?: " Aucun mail actuel") )

            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Nom actuel : "+(item?.name ?: " Aucun nom actuel") )


            Spacer(modifier = Modifier.padding(8.dp))
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
        }
        Column{
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
            Spacer(modifier = Modifier.padding(8.dp))

            FeatureThatRequiresCameraPermission(activityResult )

        }
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

@SuppressLint("RestrictedApi")
@ExperimentalPermissionsApi
@Composable
private fun FeatureThatRequiresCameraPermission(
    activityResult: ActivityResultLauncher<Uri>
) {
    val bitmapFromCamera = remember { mutableStateOf<Bitmap?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            bitmapFromCamera.value = it
        }
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val writeExternalStoragePermissionState =
        rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    when (cameraPermissionState.status) {
        // If the camera permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
                  when (writeExternalStoragePermissionState.status){
                    PermissionStatus.Granted -> {
                        val file = createImageFile()
                        try {
                            uri = FileProvider.getUriForFile(getApplicationContext(),"com.example.myapplication.fileprovider",file)
                            //Log.i("PROFILE SCREEN IMAGE SUCCESS" , "URI = ${uri}")
                        }catch (e: Exception){
                            //Log.e("PROFILE SCREEN IMAGE" , "ERROR:${e.message}")
                            var foo = e.message
                        }

                        //Log.i("LOL" , "URI = ${uri}")
                        if (bitmapFromCamera.value == null) {
                            Button(onClick = { activityResult.launch(uri) }, modifier = Modifier.fillMaxWidth()) {
                                Text("Prendre une photo")
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
                            val status = writeExternalStoragePermissionState.status as PermissionStatus.Denied
                            val textToShow = if (status.shouldShowRationale) {
                                "The camera is important for this app. Please grant the permission."
                            } else {
                                "Camera permission required for this feature to be available. " +
                                        "Please grant the permission"
                            }
                            Text(textToShow)
                            Button(onClick = { writeExternalStoragePermissionState.launchPermissionRequest() }) {
                                Text("Request permission")
                            }
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



@SuppressLint("RestrictedApi")
private fun createImageFile() : File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageDirectory = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "Profile_picture_${timestamp}",
        ".jpg",
        imageDirectory
    ).apply {
        currentImagePath = absolutePath
    }
}


