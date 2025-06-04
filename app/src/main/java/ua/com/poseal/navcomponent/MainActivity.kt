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
import dagger.hilt.android.AndroidEntryPoint
import ua.com.poseal.navcomponent.model.ItemsRepository
import ua.com.poseal.navcomponent.screens.AddItemRoute
import ua.com.poseal.navcomponent.screens.AppToolbar
import ua.com.poseal.navcomponent.screens.ItemsRoute
import ua.com.poseal.navcomponent.screens.LocalNavController
import ua.com.poseal.navcomponent.screens.NavigateUpAction
import ua.com.poseal.navcomponent.screens.add.AddItemScreen
import ua.com.poseal.navcomponent.screens.items.ItemScreen
import ua.com.poseal.navcomponent.ui.theme.NavigationComponentTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var itemsRepository: ItemsRepository

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
    val titleRes = when (currentBackStackEntry?.destination?.route) {
        ItemsRoute -> R.string.items_screen
        AddItemRoute -> R.string.add_item_screen
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
            if (currentBackStackEntry?.destination?.route == ItemsRoute) {
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
                composable(ItemsRoute) { ItemScreen() }
                composable(AddItemRoute) { AddItemScreen() }
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
