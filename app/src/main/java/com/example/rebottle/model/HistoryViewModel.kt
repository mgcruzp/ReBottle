package com.example.rebottle.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.example.rebottle.model.SolicitudRequestRepository

data class Entrega(
    val fecha: String,
    val hora: String,
    val lugar: String,
    val estado: String
)

data class HistoryUiState(
    val items: List<Entrega> = emptyList(),
    val error: String? = null
)

class HistoryViewModel(
    private val repo: SolicitudRequestRepository = SolicitudRequestRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(HistoryUiState())
    val state: StateFlow<HistoryUiState> = _state

    init {
        viewModelScope.launch {
            repo.listenMyByStatuses(setOf("COMPLETADA", "CANCELADA"))
                .map { list -> list.map { it.toEntrega() } }
                .catch { _state.value = _state.value.copy(error = it.message) }
                .collect { entregas -> _state.value = HistoryUiState(items = entregas) }
        }
    }
}

private fun PickupRequest.toEntrega() = Entrega(
    fecha = this.date,
    hora = this.time,
    lugar = this.address,
    estado = when (this.status) {
        "COMPLETADA" -> "Completada"
        "CANCELADA"  -> "Cancelada"
        else         -> this.status
    }
)