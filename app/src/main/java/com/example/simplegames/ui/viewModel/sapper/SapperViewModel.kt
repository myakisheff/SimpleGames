package com.example.simplegames.ui.viewModel.sapper

import androidx.lifecycle.ViewModel
import com.example.simplegames.data.model.SapperCell
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SapperViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SapperUiState())
    val uiState: StateFlow<SapperUiState> = _uiState.asStateFlow()

    init {
        createCells()
    }

    private fun createCells() {
        val size = uiState.value.currentSizeMode.size
        val cells = mutableListOf<SapperCell>()
        val columns = uiState.value.maxCellsInRow

        for(i in (0 until size * size)) {
            val currentCoordinates = Pair(i % columns, i / columns)
            cells.add(
                SapperCell(
                    id = i,
                    isOpened = false,
                    coordinates = currentCoordinates,
                    hasMine = false,
                    nearMinesCnt = 0,
                    isFlagged = false,
                )
            )
        }

        _uiState.update { state ->
            state.copy(
                cells = cells,
                countOfMines = 0,
                flaggedCount = 0,
                openedCells = 0,
                isFlaggingMode = false,
                isStarted = false
            )
        }
    }

    fun cellClick(id: Int) {
        if(!uiState.value.isStarted) {
            generateMines(uiState.value.cells.find { it.id == id }!!.coordinates)
        }

        if(uiState.value.isFlaggingMode) {
            setFlag(id)
        }
        else if(!uiState.value.cells.find { it.id == id }!!.isFlagged &&
            !uiState.value.cells.find { it.id == id }!!.isOpened) {
            openCell(uiState.value.cells.find { it.id == id }!!.coordinates)

            if(uiState.value.cells.find { it.id == id }!!.hasMine) {
                _uiState.update { state ->
                    state.copy(
                        isGameWon = false
                    )
                }
                endGame()
            }
        }

        _uiState.update { state ->
            state.copy(
                isStarted = true
            )
        }
    }

    private fun openCell(coordinates: Pair<Int, Int>) {
        val size = uiState.value.currentSizeMode.size
        val cells = mutableListOf<SapperCell>()
        val columns = uiState.value.maxCellsInRow

        for(i in (0 until size * size)) {
            val currentCoordinates = Pair(i % columns, i / columns)
            cells.add(
                SapperCell(
                    id = i,
                    isOpened =
                    uiState.value.cells.find { it.coordinates == currentCoordinates }!!.isOpened
                            || coordinates == currentCoordinates,
                    coordinates = currentCoordinates,
                    hasMine =
                    uiState.value.cells.find { it.coordinates == currentCoordinates }!!.hasMine,
                    nearMinesCnt =
                    uiState.value.cells.find { it.coordinates == currentCoordinates }!!.nearMinesCnt,
                    isFlagged =
                    uiState.value.cells.find { it.coordinates == currentCoordinates }!!.isFlagged
                )
            )
        }

        val openedCells = uiState.value.openedCells + 1

        val minesCount = uiState.value.countOfMines
        val flaggedCount = uiState.value.flaggedCount
        if (size * size - openedCells == minesCount || minesCount == flaggedCount) {
            _uiState.update { state ->
                state.copy(
                    isGameWon = true
                )
            }
            endGame()
        }

        _uiState.update { state ->
            state.copy(
                cells = cells,
                openedCells = openedCells
            )
        }
    }

    private fun setFlag(id: Int) {
        val cells = uiState.value.cells
        var flaggedCount =  uiState.value.flaggedCount

        cells.forEach {
            if(it.id == id) {
                it.isFlagged = !it.isFlagged

                if(it.isFlagged) {
                    flaggedCount++
                } else {
                    flaggedCount--
                }
            }
        }

        _uiState.update { state ->
            state.copy(
                flaggedCount = flaggedCount
            )
        }
    }

    private fun generateMines(firstCellCoordinates: Pair<Int, Int>) {
        val size = uiState.value.currentSizeMode.size
        val minesCount = size * size * uiState.value.currentDifficultMode.percentOfMines / 100
        val cells = mutableListOf<SapperCell>()
        val columns = uiState.value.maxCellsInRow

        // generate coordinates for mines
        val minesCoordinates = generateCoordinates(
            count = minesCount,
            size = size * size,
            columns = columns,
            firstCellCoordinates = firstCellCoordinates
        )

        for(i in (0 until size * size)) {
            val currentCoordinates = Pair(i % columns, i / columns)
            cells.add(
                SapperCell(
                    id = i,
                    isOpened = false,
                    coordinates = currentCoordinates,
                    hasMine = minesCoordinates.contains(currentCoordinates),
                    nearMinesCnt = calcMinesCount(currentCoordinates, minesCoordinates),
                    isFlagged = false
                )
            )
        }

        _uiState.update { state ->
            state.copy(
                cells = cells,
                countOfMines = minesCount
            )
        }
    }

    private fun calcMinesCount(
        currentCoordinates: Pair<Int, Int>,
        minesCoordinates: List<Pair<Int, Int>>
    ): Int {
        var count = 0

        // yee hardcoding

        val cellsToCheck : MutableList<Pair<Int, Int>> = mutableListOf()
        cellsToCheck.add(Pair(currentCoordinates.first - 1, currentCoordinates.second))
        cellsToCheck.add(Pair(currentCoordinates.first, currentCoordinates.second - 1))
        cellsToCheck.add(Pair(currentCoordinates.first - 1, currentCoordinates.second - 1))
        cellsToCheck.add(Pair(currentCoordinates.first + 1, currentCoordinates.second))
        cellsToCheck.add(Pair(currentCoordinates.first, currentCoordinates.second + 1))
        cellsToCheck.add(Pair(currentCoordinates.first + 1, currentCoordinates.second + 1))
        cellsToCheck.add(Pair(currentCoordinates.first - 1, currentCoordinates.second + 1))
        cellsToCheck.add(Pair(currentCoordinates.first + 1, currentCoordinates.second - 1))

        minesCoordinates.forEach {
            if(cellsToCheck.contains(it)) {
                count++
            }
        }

        return count
    }

    private fun generateCoordinates(
        count: Int,
        size: Int,
        columns: Int,
        firstCellCoordinates: Pair<Int, Int>
    ) : List<Pair<Int,Int>> {
        val takenCoordinates : MutableList<Pair<Int,Int>> = mutableListOf()

        val cellsToCheck : MutableList<Pair<Int, Int>> = mutableListOf()
        cellsToCheck.add(Pair(firstCellCoordinates.first - 1, firstCellCoordinates.second))
        cellsToCheck.add(Pair(firstCellCoordinates.first, firstCellCoordinates.second - 1))
        cellsToCheck.add(Pair(firstCellCoordinates.first - 1, firstCellCoordinates.second - 1))
        cellsToCheck.add(Pair(firstCellCoordinates.first + 1, firstCellCoordinates.second))
        cellsToCheck.add(Pair(firstCellCoordinates.first, firstCellCoordinates.second + 1))
        cellsToCheck.add(Pair(firstCellCoordinates.first + 1, firstCellCoordinates.second + 1))
        cellsToCheck.add(Pair(firstCellCoordinates.first - 1, firstCellCoordinates.second + 1))
        cellsToCheck.add(Pair(firstCellCoordinates.first + 1, firstCellCoordinates.second - 1))
        cellsToCheck.add(Pair(firstCellCoordinates.first, firstCellCoordinates.second))

        while (takenCoordinates.size < count) {
            val newCoordinates: Pair<Int, Int> = Pair(
                (0 until columns).random(),
                (0 until size / columns).random()
            )
            if(!takenCoordinates.contains(newCoordinates)
                && !cellsToCheck.contains(newCoordinates)) {
                takenCoordinates.add(newCoordinates)
            }
        }

        return takenCoordinates
    }

    fun setAward(award: Int) {
        _uiState.update { state ->
            state.copy(
                currentAward = calcAward(
                    sizeMode = uiState.value.currentSizeMode,
                    difficultMode = uiState.value.currentDifficultMode
                ),
                defaultAward = award
            )
        }
    }

    private fun endGame() {
        _uiState.update { state ->
            state.copy(
                isGameEnd = true
            )
        }
    }

    private fun calcAward(sizeMode: SapperSizeMode, difficultMode: SapperDifficultMode) : Int {
        var award = uiState.value.defaultAward * sizeMode.size / 3
        award = when(difficultMode) {
            SapperDifficultMode.EASY -> award / 2
            SapperDifficultMode.MEDIUM -> award
            SapperDifficultMode.HARD -> award * 2
        }
        return award
    }

    fun changeDifficultMode(mode: SapperDifficultMode) {
        _uiState.update { state ->
            state.copy(
                currentDifficultMode = mode,
                currentAward = calcAward(
                    sizeMode = uiState.value.currentSizeMode,
                    difficultMode = mode
                )
            )
        }
        createCells()
    }

    fun changeSizeMode(mode: SapperSizeMode) {
        _uiState.update { state ->
            state.copy(
                currentSizeMode = mode,
                currentAward = calcAward(
                    sizeMode = mode,
                    difficultMode = uiState.value.currentDifficultMode
                )
            )
        }
        createCells()
    }

    fun hideSettingsZone() {
        _uiState.update { state ->
            state.copy(
                isSettingsZoneHidden = !state.isSettingsZoneHidden
            )
        }
    }

    fun changeFlaggingMode(isOn: Boolean) {
        _uiState.update { state ->
            state.copy(
                isFlaggingMode = isOn
            )
        }
    }
}