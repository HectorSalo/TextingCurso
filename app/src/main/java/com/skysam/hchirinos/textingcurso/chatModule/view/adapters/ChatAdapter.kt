package com.skysam.hchirinos.textingcurso.chatModule.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.pojo.Message

class ChatAdapter(private var messages: ArrayList<Message>, private var listener: onItemClickListener): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private lateinit var mContext: Context
    private var lastPhoto: Int = 0

    class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val tvMessage: TextView = view.findViewById(R.id.tv_message)
        val imgPhoto: AppCompatImageView = view.findViewById(R.id.img_photo)

        fun setOnClickListener(message: Message, listener: onItemClickListener) {
            imgPhoto.setOnClickListener {
                listener.onClickIamge(message)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false) as View
        mContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = messages.size


}