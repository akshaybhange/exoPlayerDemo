package com.ab.demo.ui.player

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class PlayerViewModel @Inject constructor() : ViewModel() {
    var playWhenReady: Boolean = true
    var currentWindow: Int = 0
    var playbackPosition: Long = 0


}
