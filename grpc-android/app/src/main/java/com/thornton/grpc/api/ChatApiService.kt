package com.thornton.grpc.api

import android.content.Context
import com.thornton.ChatMessage
import com.thornton.ChatServiceGrpcKt
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.lang.Exception
import java.net.URL

class ChatApiService(context: Context) {

    private lateinit var channel: ManagedChannel
    private lateinit var service: ChatServiceGrpcKt.ChatServiceCoroutineStub

    fun startChannel() {
        val url = URL("http://10.0.2.2:50051")
        val builder = ManagedChannelBuilder.forAddress(url.host, url.port)
        builder.usePlaintext()
        channel = builder.build()
        service = ChatServiceGrpcKt.ChatServiceCoroutineStub(channel)
    }

    suspend fun sendMessage(message: ChatMessage) {
        try {
            val requests = flow {
                emit(ChatMessage.newBuilder().setAuthor(message.author).setMessage(message.message).build())
            }
            val returnedMessage = service.chat(requests)
            returnedMessage.collect {
                Timber.d("New message ${it.message} from ${it.author}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error sending message")
        }
    }

    fun endChannel() {
        channel.shutdownNow()
    }
}