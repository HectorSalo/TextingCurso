package com.skysam.hchirinos.textingcurso.mainModule.view.adapters

import android.content.Context
import android.util.Log
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
import kotlinx.android.synthetic.main.item_request.view.*

class RequestAdapter (private var mUsers: ArrayList<User>, private var mListener: OnItemClickListener) :
    RecyclerView.Adapter<RequestAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val imgPhoto: CircleImageView = view.img_Photo
        val btnDeny: ImageButton = view.btn_deny
        val btnAccept: ImageButton = view.btn_accept
        val tvName: TextView = view.tv_name
        val tvEmail: TextView = view.tv_email

        fun setOnClickListener(user: User, listener: OnItemClickListener){
            btnAccept.setOnClickListener {
                listener.onAcceptRequest(user)
            }
            btnDeny.setOnClickListener {
                listener.onDenyRequest(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false) as View
        mContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestAdapter.ViewHolder, position: Int) {
        val user = mUsers[position]

        holder.setOnClickListener(user, mListener)

        holder.tvName.text = user.username
        holder.tvEmail.text = user.email

        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
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