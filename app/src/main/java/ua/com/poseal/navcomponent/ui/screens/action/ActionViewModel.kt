package ua.com.poseal.navcomponent.ui.screens.action

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ua.com.poseal.navcomponent.model.LoadResult
import ua.com.poseal.navcomponent.model.tryUpdate

class ActionViewModel<State, Action>(
    private val delegate: Delegate<State, Action>,
) : ViewModel() {


    private val _stateFlow = MutableStateFlow<LoadResult<State>>(LoadResult.Loading)
    val stateFlow: StateFlow<LoadResult<State>> = _stateFlow

    private val _exitChannel = Channel<Unit>()
    val exitChannel: ReceiveChannel<Unit> = _exitChannel

    private val _errorChannel = Channel<Exception>()
    val errorChannel: ReceiveChannel<Exception> = _errorChannel

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _stateFlow.value = LoadResult.Loading
            _stateFlow.value = try {
                LoadResult.Success(delegate.loadState())
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    fun execute(action: Action) {
        viewModelScope.launch {
            showProgress()
            try {
                delegate.execute(action)
                goBack()
            } catch (e: Exception) {
                hideProgress()
                _errorChannel.send(e)
            }
        }
    }

    private fun showProgress() {
        _stateFlow.tryUpdate(delegate::showProgress)
    }

    private fun hideProgress() {
        _stateFlow.tryUpdate(delegate::hideProgress)
    }

    private suspend fun goBack() {
        _exitChannel.send(Unit)
    }

    interface Delegate<State, Action> {
        suspend fun loadState(): State
        fun showProgress(input: State): State
        fun hideProgress(input: State): State
        suspend fun execute(action: Action)
    }
}
