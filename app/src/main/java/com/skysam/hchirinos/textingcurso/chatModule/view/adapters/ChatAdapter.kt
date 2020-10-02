package com.skysam.hchirinos.textingcurso.chatModule.view.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.pojo.Message

class ChatAdapter(private var mMessages: ArrayList<Message>, private var mlistener: onItemClickListener): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

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
        val message = mMessages[position]

        val maxMarginHorizontal = mContext.resources.getDimensionPixelSize(R.dimen.chat_margin_max_horizontal)
        val maxMarginTop = mContext.resources.getDimensionPixelSize(R.dimen.chat_margin_max_top)
        val minMargin = mContext.resources.getDimensionPixelSize(R.dimen.chat_margin_min)

        var gravity = Gravity.END
        var background: Drawable = ContextCompat.getDrawable(mContext, R.drawable.background_chat_me)!!
        var marginStart = maxMarginHorizontal
        var marginTop = minMargin
        var marginEnd = minMargin

        if (message.sentByMe!!) {
            gravity = Gravity.START
            background = ContextCompat.getDrawable(mContext, R.drawable.background_chat_friend)!!
            marginEnd = maxMarginHorizontal
            marginStart = minMargin
        }

        if ((position > 0) && (message.sentByMe!! != mMessages[position - 1].sentByMe)) {
            marginTop = maxMarginTop
        }

        val params = holder.tvMessage.layoutParams as LinearLayout.LayoutParams
        params.gravity = gravity
        params.setMargins(marginStart, marginTop, marginEnd, minMargin)

        if (message.photoUrl != null) {
            holder.tvMessage.visibility = View.GONE
            holder.imgPhoto.visibility = View.VISIBLE

            if (position > lastPhoto) {
                lastPhoto = position
            }

            val size = mContext.resources.getDimensionPixelSize(R.dimen.chat_size_image)
            params.width = size
            params.height = size

            val options = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_timer_sand_160)
                .error(R.drawable.ic_emoticon_sad)
                .centerCrop()

            Glide.with(mContext).asBitmap().load(message.photoUrl).apply(options)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return true
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        val dimension = size - mContext.resources.getDimensionPixelSize(R.dimen.chat_padding_image)
                        val bitmap = ThumbnailUtils.extractThumbnail(resource, dimension, dimension)
                        holder.imgPhoto.setImageBitmap(bitmap)
                        if (message.loaded != null) {
                            if (!message.loaded!!) {
                                message.loaded = true
                                if (position == lastPhoto) {
                                    mlistener.onImageLoaded()
                                }
                            }
                        } else {
                            message.loaded = true
                            if (!message.loaded!!) {
                                message.loaded = true
                                if (position == lastPhoto) {
                                    mlistener.onImageLoaded()
                                }
                            }
                        }
                        return true
                    }
                })
                .into(holder.imgPhoto)

            holder.imgPhoto.background = background
            holder.setOnClickListener(message, mlistener)
        } else {
            holder.tvMessage.visibility = View.VISIBLE
            holder.imgPhoto.visibility = View.GONE

            params.width = LinearLayout.LayoutParams.WRAP_CONTENT
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT

            holder.tvMessage.background = background
            holder.tvMessage.text = message.msg
        }

        holder.imgPhoto.layoutParams = params
        holder.tvMessage.layoutParams = params

    }

    override fun getItemCount(): Int = mMessages.size

    fun add(message: Message) {
        if (!mMessages.contains(message)) {
            mMessages.add(message)
            notifyItemInserted(mMessages.size - 1)
        }
    }

}