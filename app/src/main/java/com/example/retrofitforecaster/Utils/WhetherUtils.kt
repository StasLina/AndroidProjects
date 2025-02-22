package com.example.retrofitforecaster.Utils

import com.example.retrofitforecaster.interfaces.IWhetherUtils
import com.example.retrofitforecaster.models.MainViewModel
import com.example.retrofitforecaster.whether.IWhetherApiService

class WhetherUtils(private var _data : MainViewModel) : IWhetherUtils{
    override fun getTemperatureAsString(value: Double): String {
        if(_data.getTypeOfTemperatureScale.value == IWhetherApiService.METRIC) {
            return "${value}° C";
        }else if(_data.getTypeOfTemperatureScale.value == IWhetherApiService.IMPERIAL) {
            return "${value}° F";
        }
        return "${value}";
    }

}