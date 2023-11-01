package com.example.mydiary.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mydiary.R
import com.example.mydiary.common.Constants.WRITE_SCREE_ARGUMENT_KEY
import com.example.mydiary.ui.auth_screen.AuthScreen
import com.example.mydiary.ui.auth_screen.AuthViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

/**
 * @author : Mingaleev D
 * @data : 01.11.2023
 */

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController
) {

   NavHost(navController = navController, startDestination = startDestination) {
      authenticationRoute()
      // homeRoute()
      // writeRoute()
   }
}

fun NavGraphBuilder.authenticationRoute() {
   composable(route = Screen.Authentication.route) {

      val viewModel: AuthViewModel = viewModel()
      val loadingState by viewModel.loadingState
      val oneTapState = rememberOneTapSignInState()
      val messageBarState = rememberMessageBarState()

      AuthScreen(
          loadingState = loadingState,
          oneTapState = oneTapState,
          messageBarState = messageBarState,
          onButtonClicked = {
             oneTapState.open()
             viewModel.setLoading(true)
          },
          onTokenIdReceived = { tokenId ->
             messageBarState.addSuccess("successfully_authenticated")
             viewModel.signInWithMongoAtlas(
                 tokenId = tokenId,
                 onSuccess = {
                    if(it){
                       messageBarState.addSuccess("successfully_authenticated")
                       viewModel.setLoading(false)
                    }
                 },
                 onError = { ex ->
                    messageBarState.addError(ex)
                    viewModel.setLoading(false)
                 }
             )
          },
          onDialogDismissed = { message ->
             messageBarState.addError(Exception(message))
          }
      )
   }
}

fun NavGraphBuilder.homeRoute() {
   composable(route = Screen.Home.route) {
      // todo homeScreen
   }
}

fun NavGraphBuilder.writeRoute() {
   composable(
       route = Screen.Write.route,
       arguments = listOf(navArgument(name = WRITE_SCREE_ARGUMENT_KEY) {
          type = NavType.StringType
          nullable = true
          defaultValue = null
       }
       )) {

   }
}