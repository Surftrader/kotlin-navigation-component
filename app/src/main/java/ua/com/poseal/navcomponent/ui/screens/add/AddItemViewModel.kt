package ua.com.poseal.navcomponent.ui.screens.add

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.com.poseal.navcomponent.model.ItemsRepository
import ua.com.poseal.navcomponent.ui.screens.action.ActionViewModel.Delegate
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
) : ViewModel(), Delegate<AddItemViewModel.ScreenState, String> {

    override suspend fun loadState(): ScreenState {
        return ScreenState()
    }

    override fun showProgress(input: ScreenState): ScreenState {
        return input.copy(isProgressVisible = true)
    }

    override suspend fun execute(action: String) {
        itemsRepository.add(action)
    }

    data class ScreenState(
        val isProgressVisible: Boolean = false,
    )
}
