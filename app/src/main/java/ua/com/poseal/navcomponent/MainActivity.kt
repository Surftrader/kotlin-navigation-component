package ua.com.poseal.navcomponent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.com.poseal.navcomponent.model.ItemsRepository
import ua.com.poseal.navcomponent.screens.AddItemRoute
import ua.com.poseal.navcomponent.screens.ItemsRoute
import ua.com.poseal.navcomponent.screens.LocalNavController
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
    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        NavHost(
            navController = navController,
            startDestination = ItemsRoute,
            modifier = Modifier.fillMaxSize(),
        ) {
            composable(ItemsRoute) { ItemScreen() }
            composable(AddItemRoute) { AddItemScreen() }
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
