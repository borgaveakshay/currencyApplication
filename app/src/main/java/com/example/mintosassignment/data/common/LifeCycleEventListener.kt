package com.example.mintosassignment.data.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun LifecycleEventListener(
    onEvent: (Lifecycle.Event) -> Unit
) {
    // Get the current LifecycleOwner
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create a DisposableEffect to observe lifecycle events
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            onEvent(event)
        }
        // Add the observer to the LifecycleOwner
        lifecycleOwner.lifecycle.addObserver(observer)

        // Cleanup when the DisposableEffect is disposed
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}