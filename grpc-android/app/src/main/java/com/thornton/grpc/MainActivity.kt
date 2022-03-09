package com.thornton.grpc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thornton.ChatMessage
import com.thornton.grpc.api.ChatApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.grpc.stub.StreamObserver

class MainActivity : AppCompatActivity(), StreamObserver<ChatMessage> {

    private val scope by lazy { CoroutineScope(Dispatchers.IO) }
    private val chatApiService by lazy { ChatApiService() }
    private val messages = ArrayList<ChatMessage>()
    private val adapter by lazy { ChatRecyclerViewAdapter(messages, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            sendMessage()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        sendMessage()
    }

    override fun onStop() {
        super.onStop()
        chatApiService.stop()
    }

    override fun onNext(value: ChatMessage?) {
        val message = value ?: return
        runOnUiThread { adapter.addMessage(message) }
        Timber.d("New message ${message.message} - ${message.author}")
    }

    override fun onError(t: Throwable?) {
    }

    override fun onCompleted() {
    }

    private fun sendMessage() {
        scope.launch {
            chatApiService.startChannel()
            chatApiService.sendMessage("Message Example", "Author", this@MainActivity)
        }
    }
}