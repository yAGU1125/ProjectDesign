package com.kit.projectdesign.ui.discount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kit.projectdesign.api.ApiClient
import com.kit.projectdesign.data.DiscountItem
import kotlinx.coroutines.launch

class DiscountViewModel : ViewModel() {

    private val _discountItems = MutableLiveData<List<DiscountItem>>()
    val discountItems: LiveData<List<DiscountItem>> = _discountItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchDiscountItems() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val response = ApiClient.service.getDiscountItems()
                if (response.isSuccessful) {
                    _discountItems.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
