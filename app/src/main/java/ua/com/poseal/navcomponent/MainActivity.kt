package ua.com.poseal.navcomponent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import ua.com.poseal.navcomponent.screens.AddItemRoute
import ua.com.poseal.navcomponent.screens.AppToolbar
import ua.com.poseal.navcomponent.screens.EditItemRoute
import ua.com.poseal.navcomponent.screens.ItemsRoute
import ua.com.poseal.navcomponent.screens.LocalNavController
import ua.com.poseal.navcomponent.screens.NavigateUpAction
import ua.com.poseal.navcomponent.screens.add.AddItemScreen
import ua.com.poseal.navcomponent.screens.edit.EditItemScreen
import ua.com.poseal.navcomponent.screens.items.ItemScreen
import ua.com.poseal.navcomponent.screens.routeClass
import ua.com.poseal.navcomponent.ui.theme.NavigationComponentTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationComponentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavApp()
                }
            }
        }
    }
}

@Composable
fun NavApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val titleRes = when (currentBackStackEntry.routeClass()) {
        ItemsRoute::class -> R.string.items_screen
        AddItemRoute::class -> R.string.add_item_screen
        EditItemRoute::class -> R.string.edit_item_screen
        else -> R.string.app_name
    }
    Scaffold(
        topBar = {
            AppToolbar(
                navigateUpAction = if (navController.previousBackStackEntry == null) {
                    NavigateUpAction.Hidden
                } else {
                    NavigateUpAction.Visible(
                        onClick = { navController.navigateUp() }
                    )
                },
                titleRes = titleRes,
            )
        },
        floatingActionButton = {
            if (currentBackStackEntry.routeClass() == ItemsRoute::class) {
                FloatingActionButton(
                    onClick = { navController.navigate(AddItemRoute) },
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }
    ) { paddingValues ->
        CompositionLocalProvider(
            LocalNavController provides navController
        ) {
            NavHost(
                navController = navController,
                startDestination = ItemsRoute,
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            ) {
                composable<ItemsRoute> { ItemScreen() }
                composable<AddItemRoute> { AddItemScreen() }
                composable<EditItemRoute> { entry ->
                    val route: EditItemRoute = entry.toRoute()
                    EditItemScreen(index = route.index)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavAppPreview() {
    NavigationComponentTheme {
        NavApp()
    }
}
