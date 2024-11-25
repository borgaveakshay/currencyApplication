package com.example.mintosassignment.data.states

import com.example.mintosassignment.data.common.ResourceStatus

data class AddExchangeRatesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: ResourceStatus? = null
)
