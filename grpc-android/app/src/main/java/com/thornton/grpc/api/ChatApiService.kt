package com.thornton.grpc.api

import com.thornton.ChatMessage
import com.thornton.ChatServiceGrpcKt
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.lang.Exception
import java.net.URL

class ChatApiService {

    private lateinit var channel: ManagedChannel
    private lateinit var service: ChatServiceGrpcKt.ChatServiceCoroutineStub

    fun startChannel() {
        val url = URL("http://10.0.2.2:50051")
        val builder = ManagedChannelBuilder.forAddress(url.host, url.port)
        builder.usePlaintext()
        builder.keepAliveWithoutCalls(true)
        channel = builder.build()
        service = ChatServiceGrpcKt.ChatServiceCoroutineStub(channel)
    }

    suspend fun sendMessage(message: String, author: String, observer: StreamObserver<ChatMessage>) {
        try {
            val requests = flow {
                val message = ChatMessage.newBuilder()
                    .setAuthor(author)
                    .setMessage(message)
                    .build()
                emit(message)
            }
            val flow = service.chat(requests)
            flow.collect {
                observer.onNext(it)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error sending message")
        }
    }

    fun stop() {
        channel.shutdownNow()
    }

}