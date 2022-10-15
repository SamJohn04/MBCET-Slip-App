package com.example.mbcetslipapp.ui.theme

import androidx.lifecycle.ViewModel
import com.example.mbcetslipapp.ui.theme.SlipUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SlipViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SlipUiState())
    val uiState: StateFlow<SlipUiState> = _uiState.asStateFlow()
    init {
        resetGame()
    }
    fun resetGame() {
        _uiState.value = SlipUiState(userType = "Student", selection = "Requested", name = "Samuel John", studentRoll = "B21CS1212")
    }
    fun updateUserType(type: String)
    {
        val selection = _uiState.value.selection
        val name = _uiState.value.name
        val studentRoll = _uiState.value.studentRoll
        _uiState.value = SlipUiState(type, selection, name, studentRoll)
    }
    fun updateSelection(type: String)
    {
        val user = _uiState.value.userType
        val name = _uiState.value.name
        val studentRoll = _uiState.value.studentRoll
        _uiState.value = SlipUiState(user, type, name, studentRoll)
    }
    fun updateUser(userType: String, name: String, studentRoll: String)
    {
        val selection = _uiState.value.selection
        _uiState.value = SlipUiState(userType, selection, name, studentRoll)
    }
}