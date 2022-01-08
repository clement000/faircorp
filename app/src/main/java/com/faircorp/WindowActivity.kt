package com.faircorp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faircorp.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WindowActivity : BasicActivity() {

    val windowService = WindowService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val param = intent.getStringExtra(WINDOW_NAME_PARAM)
        val windowName = findViewById<TextView>(R.id.txt_window_name)
        windowName.text = param

        val id = intent.getLongExtra(WINDOW_NAME_PARAM, 0)
        var windowDto = WindowDto(-1, "Window not found", RoomDto(-11, "Room not found", 0.0, 0.0), Status.CLOSED)

        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().windowsApiService.findById(id).execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)

                        windowDto = it.body()!!
                        findViewById<TextView>(R.id.txt_window_name).text = windowDto.name
                        findViewById<TextView>(R.id.txt_room_name).text = windowDto.room.name
                        findViewById<TextView>(R.id.txt_window_current_temperature).text = windowDto.room.currentTemperature?.toString()
                        findViewById<TextView>(R.id.txt_window_target_temperature).text = windowDto.room.targetTemperature?.toString()
                        findViewById<TextView>(R.id.txt_window_status).text = windowDto.windowStatus.toString()
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Error on window loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}