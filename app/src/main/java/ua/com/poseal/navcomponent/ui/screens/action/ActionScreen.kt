package ua.com.poseal.navcomponent.ui.screens.action

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.com.poseal.navcomponent.ui.components.ExceptionToMessageMapper
import ua.com.poseal.navcomponent.ui.components.LoadResultContent
import ua.com.poseal.navcomponent.ui.screens.EventConsumer
import ua.com.poseal.navcomponent.ui.screens.LocalNavController
import ua.com.poseal.navcomponent.ui.screens.action.ActionViewModel.Delegate

data class ActionContentState<State, Action>(
    val state: State,
    val onExecuteAction: (Action) -> Unit,
)

@Composable
fun <State, Action> ActionScreen(
    delegate: Delegate<State, Action>,
    content: @Composable (ActionContentState<State, Action>) -> Unit,
    modifier: Modifier = Modifier,
    exceptionToMessageMapper: ExceptionToMessageMapper = ExceptionToMessageMapper.Default,
) {
    val viewModel = viewModel<ActionViewModel<State, Action>> {
        ActionViewModel(delegate)
    }
    val navController = LocalNavController.current
    EventConsumer(channel = viewModel.exitChannel) {
        navController.popBackStack()
    }
    val context = LocalContext.current
    EventConsumer(channel = viewModel.errorChannel) { exception ->
        val message = exceptionToMessageMapper.getUserMessage(exception, context)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    val loadResult by viewModel.stateFlow.collectAsState()
    LoadResultContent(
        loadResult = loadResult,
        content = { state ->
            val actionContentState = ActionContentState(
                state = state,
                onExecuteAction = viewModel::execute
            )
            content(actionContentState)
        },
        modifier = modifier,
        onTryAgainAction = viewModel::load,
    )
}
