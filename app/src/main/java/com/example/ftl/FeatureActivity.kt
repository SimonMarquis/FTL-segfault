package com.example.ftl

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeatureActivity : AppCompatActivity() {

    private val viewModel: FeatureViewModel by viewModels()

    private val textView by lazy { TextView(this).apply { id = android.R.id.text1 } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(textView)
        viewModel.state.observe(this) { textView.text = it.id.toString() }
        /* HERE! If this line is executed, random segfault will happen! */
        lifecycleScope.launch {}
    }

}
