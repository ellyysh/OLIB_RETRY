package com.example.olib_retry.ui


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.olib_retry.viewmodel.BooksViewModel

@Composable
fun OLibApp(viewModel: BooksViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {
        composable("search") {
            SearchScreenRoute(
                viewModel = viewModel,
                onOpenDetail = { workId ->
                    navController.navigate("detail/$workId")
                },
                onOpenFavourites = {
                    navController.navigate("favourites")
                }
            )
        }

        composable(
            route = "detail/{workId}",
            arguments = listOf(navArgument("workId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workId = backStackEntry.arguments?.getString("workId").orEmpty()
            DetailScreenRoute(
                viewModel = viewModel,
                workId = workId,
                onBack = { navController.popBackStack() }
            )
        }

        composable("favourites") {
            FavouritesScreenRoute(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenDetail = { workId ->
                    navController.navigate("detail/$workId")
                }
            )
        }
    }
}