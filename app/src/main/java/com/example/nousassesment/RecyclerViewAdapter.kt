package com.example.nousassesment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nousassesment.data.Item
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(val context: Context): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var mList = ArrayList<Item>()

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
//        val description: TextView = itemView.findViewById(R.id.desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = mList[position]


        val imageUrl = if (currentItem.imageUrl.endsWith("/preview")) currentItem.imageUrl
        else currentItem.imageUrl.plus("/preview")
        // sets the image to the imageview from our itemHolder class
        Picasso.with(context).load(imageUrl).into(holder.image)
        holder.title.text = currentItem.title

        holder.itemView.setOnClickListener {
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

//        // sets the text to the textview from our itemHolder class
//        holder.description.text = currentItem.description
    }

    fun updatePosts(postList: List<Item>) {
        this.mList.clear()
        this.mList.addAll(postList)
        notifyDataSetChanged()
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

}