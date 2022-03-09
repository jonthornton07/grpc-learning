package com.thornton.grpc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thornton.ChatMessage

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView = itemView.findViewById<TextView>(R.id.text)
}

class ChatRecyclerViewAdapter(
    startingMessages: ArrayList<ChatMessage>,
    context: Context
): RecyclerView.Adapter<ChatViewHolder>() {

    private val messages: ArrayList<ChatMessage> = startingMessages
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view: View = inflater.inflate(R.layout.recyclerview_row, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.textView.text = message.message
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyDataSetChanged()
    }
}