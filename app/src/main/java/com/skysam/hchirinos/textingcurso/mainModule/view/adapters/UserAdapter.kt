package com.skysam.hchirinos.textingcurso.mainModule.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.pojo.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.content_user.view.*
import kotlinx.android.synthetic.main.item_request.view.*
import kotlinx.android.synthetic.main.item_request.view.tv_name

class UserAdapter (private var mUsers: ArrayList<User>, private var listener: OnItemClickListener) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val imgPhoto: CircleImageView = view.img_photo
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvCountUnread: TextView = view.findViewById(R.id.tv_count_unread)

        fun setClickListener(user: User, listener: OnItemClickListener) {
            itemView.setOnClickListener {
                listener.onItemClick(user)
            }

            itemView.setOnLongClickListener {
                listener.onItemLongClick(user)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false) as View
        mContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = mUsers[position]

        holder.setClickListener(user, listener)

        holder.tvName.text = user.getUsernameValid()

        val messageUnread = user.messageUnread
        if (messageUnread > 0) {
            val countStr = if (messageUnread > 99) mContext.getString(R.string.main_item_max_messageUnread) else messageUnread.toString()
            holder.tvCountUnread.text = countStr
            holder.tvCountUnread.visibility = View.VISIBLE
        } else {
            holder.tvCountUnread.visibility = View.GONE
        }

        val options = RequestOptions()
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .error(R.drawable.ic_emoticon_sad)
            .placeholder(R.drawable.ic_emoticon_tongue)

        Glide.with(mContext).load(user.getPhotoValid())
            .apply(options).into(holder.imgPhoto)
    }

    override fun getItemCount(): Int = mUsers.size

    fun add(user: User) {
        if (!mUsers.contains(user)) {
            mUsers.add(user)
            notifyItemInserted(mUsers.size - 1)
        } else {
            update(user)
        }
    }

    fun update(user: User) {
        val index = mUsers.indexOf(user)
        mUsers[index] = user
        notifyItemChanged(index)
    }

    fun remove(user: User) {
        if (mUsers.contains(user)) {
            val index = mUsers.indexOf(user)
            mUsers.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}