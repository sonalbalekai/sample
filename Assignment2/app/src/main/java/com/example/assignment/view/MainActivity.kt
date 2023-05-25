package com.example.assignment.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.api.Api
import com.example.assignment.data.Model
import com.example.assignment.databinding.ActivityMainBinding
import com.example.assignment.repository.DataDao
import com.example.assignment.repository.Repository
import com.example.assignment.viewmodel.DataViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var employeeDao: DataDao
    private lateinit var viewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup binding the View UI
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)
        // this creates a vertical layout Manager
        binding.sampleRecycler.layoutManager = LinearLayoutManager(this)
        binding.sampleRecycler.setHasFixedSize(true)
        viewModel.makeRequest()
        viewModel.getAllData(application).observe(this) {
            if (it != null) {
                val orderTypeAdapter = EmployeeRecyclerView(it)
                binding.sampleRecycler.adapter = orderTypeAdapter
            }
        }
    }
}