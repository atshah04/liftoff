package com.example.liftoff

import androidx.compose.ui.text.input.TextFieldValue
import com.example.liftoff.data.viewmodel.GenerateViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GVMTest {
    private lateinit var viewModel: GenerateViewModel

    @Before
    fun setup() {
        viewModel = GenerateViewModel()
    }

    @Test
    fun `setMinutesInput updates minutesInput flow`() {
        val input = TextFieldValue("10")
        viewModel.setMinutesInput(input)

        assertEquals(input, viewModel.minutesInput.value)
    }

    @Test
    fun `setWeightInput updates weight flow`() {
        val input = TextFieldValue("75")
        viewModel.setWeightInput(input)

        assertEquals(input, viewModel.weight.value)
    }

    @Test
    fun `setCheckedState updates the correct index`() {
        val indexToCheck = 1
        viewModel.setCheckedState(indexToCheck, true)

        val updatedStates = viewModel.checkedStates.value
        assertTrue(updatedStates[indexToCheck])
        assertFalse(updatedStates[0]) // Ensure other indices are unaffected
        assertFalse(updatedStates[2]) // Ensure other indices are unaffected
    }

    @Test
    fun `setError updates error flow`() {
        viewModel.setError(true)
        assertTrue(viewModel.error.value)

        viewModel.setError(false)
        assertFalse(viewModel.error.value)
    }

    @Test
    fun `setOverride updates override flow`() {
        viewModel.setOverride(true)
        assertTrue(viewModel.override.value)

        viewModel.setOverride(false)
        assertFalse(viewModel.override.value)
    }

    @Test
    fun `default checkedStates is a list of false`() {
        val initialStates = viewModel.checkedStates.value
        assertEquals(viewModel.Exercices.size, initialStates.size)
        assertTrue(initialStates.all { !it })
    }

    @Test
    fun `default flows are initialized correctly`() {
        assertEquals(TextFieldValue(""), viewModel.minutesInput.value)
        assertEquals(TextFieldValue(""), viewModel.weight.value)
        assertFalse(viewModel.error.value)
        assertFalse(viewModel.override.value)
    }
}
