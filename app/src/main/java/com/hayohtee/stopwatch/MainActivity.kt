package com.hayohtee.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {
    private lateinit var stopwatch: Chronometer
    var running = false
    var offset: Long = 0

    // Key strings for use with the bundle
    private val OFFSET_KEY = "offset"
    private val RUNNING_KEY = "running"
    private val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stopwatch = findViewById(R.id.stopwatch)

        // Restore previous state
        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)

            if (running) {
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else {
                setBaseTime()
            }
        }

        // Starts the stopwatch if it's not running
        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            if (!running) {
                setBaseTime()
                stopwatch.start()
                running = true
            }
        }

        // Pauses the stopwatch if it's running
        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            if (running) {
                saveOffset()
                stopwatch.stop()
                running = false
            }
        }

        // Sets the offset and stopwatch to 0
        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }
    }

    override fun onStop() {
        super.onStop()

        if (running) {
            saveOffset()
            stopwatch.stop()
        }
    }

    override fun onRestart() {
        super.onRestart()

        if (running) {
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }

    // Save activity states before destroying
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(OFFSET_KEY, offset)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY, stopwatch.base)

        super.onSaveInstanceState(outState)
    }

    // Update the stopwatch.base time, allowing for any offset
    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    // Record the offset
    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }
}