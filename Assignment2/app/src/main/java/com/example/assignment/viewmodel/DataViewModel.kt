package com.example.assignment.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment.api.Api
import com.example.assignment.data.Data
import com.example.assignment.data.Model
import com.example.assignment.repository.DataDao
import com.example.assignment.repository.Repository
import com.example.assignment.view.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataViewModel : ViewModel() {

    private lateinit var employeeDao : DataDao

    private val _data = MutableLiveData<List<Data>>()

    val data : LiveData<List<Data>> get() = _data

    private val TAG = MainActivity::class.java.simpleName

    fun getAllData(applicationContext:Application) : LiveData<List<Data>>{
        val db = Repository.getDatabase(applicationContext)
        employeeDao = db.dataDao()
        GlobalScope.launch {
            _data.postValue(employeeDao.getAllEmployee())
        }
        return data
    }

    fun makeRequest() {
        val api: Api =
            Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        val call: Call<Model> = api.getData(1, 12)

        call.enqueue(object : Callback<Model> {
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                if (response.isSuccessful) {
                    GlobalScope.launch {
                        response.body()?.data?.let { employeeDao.insert(it.toList()) }
                    }
                }
            }
            override fun onFailure(call: Call<Model>, t: Throwable) {
                Log.e(TAG,t.message.toString())
            }
        })
    }

}