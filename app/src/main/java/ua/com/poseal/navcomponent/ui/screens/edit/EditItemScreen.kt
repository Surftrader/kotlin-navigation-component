package ua.com.poseal.navcomponent.ui.screens.edit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ua.com.poseal.navcomponent.R
import ua.com.poseal.navcomponent.model.LoadResult
import ua.com.poseal.navcomponent.ui.components.ItemDetails
import ua.com.poseal.navcomponent.ui.components.ItemsDetailsState
import ua.com.poseal.navcomponent.ui.components.LoadResultContent
import ua.com.poseal.navcomponent.ui.screens.EditItemRoute
import ua.com.poseal.navcomponent.ui.screens.EventConsumer
import ua.com.poseal.navcomponent.ui.screens.LocalNavController
import ua.com.poseal.navcomponent.ui.screens.edit.EditItemViewModel.ScreenState
import ua.com.poseal.navcomponent.ui.screens.routeClass

@Composable
fun EditItemScreen(index: Int) {
    val viewModel = hiltViewModel<EditItemViewModel, EditItemViewModel.Factory> { factory ->
        factory.create(index)
    }
    val navController = LocalNavController.current
    EventConsumer(channel = viewModel.exitChannel) {
        if (navController.currentBackStackEntry.routeClass() == EditItemRoute::class) {
            navController.popBackStack()
        }
    }
    val loadResult by viewModel.stateFlow.collectAsState()
    EditItemContent(
        loadResult = loadResult,
        onEditButtonClicked = viewModel::update
    )
}

@Composable
fun EditItemContent(
    loadResult: LoadResult<ScreenState>,
    onEditButtonClicked: (String) -> Unit,
) {
    LoadResultContent(
        loadResult = loadResult,
        content = { screenState ->
            SuccessEditItemContent(
                state = screenState,
                onEditButtonClicked = onEditButtonClicked
            )
        },
    )

}

@Composable
fun SuccessEditItemContent(
    state: ScreenState,
    onEditButtonClicked: (String) -> Unit,
) {
    ItemDetails(
        state = ItemsDetailsState(
            loadedItem = state.loadedItem,
            textFieldPlaceholder = stringResource(id = R.string.edit_item_title),
            actionButtonText = stringResource(id = R.string.edit),
            isActionInProgress = state.isEditInProgress,
        ),
        onActionButtonClicked = onEditButtonClicked,
        modifier = Modifier.fillMaxSize(),
    )
}
