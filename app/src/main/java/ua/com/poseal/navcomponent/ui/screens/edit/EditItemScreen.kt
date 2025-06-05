package ua.com.poseal.navcomponent.ui.screens.edit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ua.com.poseal.navcomponent.R
import ua.com.poseal.navcomponent.ui.components.ItemDetails
import ua.com.poseal.navcomponent.ui.components.ItemsDetailsState
import ua.com.poseal.navcomponent.ui.screens.action.ActionScreen
import ua.com.poseal.navcomponent.ui.screens.edit.EditItemViewModel.ScreenState

@Composable
fun EditItemScreen(index: Int) {
    val viewModel = hiltViewModel<EditItemViewModel, EditItemViewModel.Factory> { factory ->
        factory.create(index)
    }
    ActionScreen(
        delegate = viewModel,
        content = { (screenState, onExecuteAction) ->
            EditItemContent(screenState, onExecuteAction)
        }
    )
}

@Composable
fun EditItemContent(
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
