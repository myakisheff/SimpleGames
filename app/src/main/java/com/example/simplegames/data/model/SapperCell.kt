package com.example.simplegames.data.model

data class SapperCell (
    val id: Int,
    val isOpened: Boolean,
    val coordinates: Pair<Int, Int>,
    val hasMine: Boolean,
    val nearMinesCnt: Int,
    var isFlagged: Boolean
)