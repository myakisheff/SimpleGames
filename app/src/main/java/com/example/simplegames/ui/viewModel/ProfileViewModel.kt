package com.example.simplegames.ui.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.simplegames.data.model.ShopItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState : StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun updateImageUri(uri: Uri) {
        _uiState.update { state ->
            state.copy(
                currentProfileImage = uri
            )
        }
    }

    fun updateItems(shopItems: List<ShopItem>) {
        _uiState.update { state ->
            state.copy(
                availableItems = shopItems
            )
        }
    }
}