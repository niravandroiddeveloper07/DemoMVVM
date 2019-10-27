package com.example.demomvvm.login.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.demomvvm.R
import com.example.demomvvm.database.User
import kotlinx.android.synthetic.main.item_user_list.view.*
import java.lang.StringBuilder

class UserListAdapter(private val items : ArrayList<User>, private val context: Context, var myCallback: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_user_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(context)
            .load(items[position].avatar)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.itemView.imgUser);

        holder.itemView.txtName.text=StringBuilder().append(items[position].firstName).append(" ").append(items.get(position).lastName)
        holder.itemView.txtEmail.text=items.get(position).email

        holder.itemView.txtDelete.setOnClickListener {
            myCallback(position)
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    }
}

