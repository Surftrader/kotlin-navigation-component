package ua.com.poseal.navcomponent.ui.screens.edit

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.com.poseal.navcomponent.model.ItemsRepository
import ua.com.poseal.navcomponent.ui.screens.action.ActionViewModel.Delegate

@HiltViewModel(assistedFactory = EditItemViewModel.Factory::class)
class EditItemViewModel @AssistedInject constructor(
    @Assisted private val index: Int,
    private val itemsRepository: ItemsRepository,
) : ViewModel(), Delegate<EditItemViewModel.ScreenState, String> {

    override suspend fun loadState(): ScreenState {
        return ScreenState(loadedItem = itemsRepository.getByIndex(index))
    }

    override fun showProgress(input: ScreenState): ScreenState {
        return input.copy(isEditInProgress = true)
    }

    override fun hideProgress(input: ScreenState): ScreenState {
        return input.copy(isEditInProgress = false)
    }

    override suspend fun execute(action: String) {
        itemsRepository.update(index, action)
    }

    data class ScreenState(
        val loadedItem: String,
        val isEditInProgress: Boolean = false,
    )

    @AssistedFactory
    interface Factory {
        fun create(index: Int): EditItemViewModel
    }
}
