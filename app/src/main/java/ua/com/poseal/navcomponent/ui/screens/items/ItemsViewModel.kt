package ua.com.poseal.navcomponent.ui.screens.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ua.com.poseal.navcomponent.model.ItemsRepository
import ua.com.poseal.navcomponent.model.LoadResult
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    itemsRepository: ItemsRepository,
) : ViewModel() {

    val stateFlow: StateFlow<LoadResult<ScreenState>> = itemsRepository.getItems()
        .map { LoadResult.Success(ScreenState(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LoadResult.Loading,
        )

    data class ScreenState(val items: List<String>)

}
