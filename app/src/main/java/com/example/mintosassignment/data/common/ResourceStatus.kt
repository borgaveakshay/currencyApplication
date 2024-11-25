package com.example.mintosassignment.data.common

sealed class ResourceStatus() {
    object Success : ResourceStatus()
    object Error : ResourceStatus()
}