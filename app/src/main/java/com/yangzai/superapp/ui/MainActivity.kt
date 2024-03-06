package com.yangzai.superapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.yangzai.superapp.R
import com.yangzai.superutils.Test

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        Test().A

    }
}

