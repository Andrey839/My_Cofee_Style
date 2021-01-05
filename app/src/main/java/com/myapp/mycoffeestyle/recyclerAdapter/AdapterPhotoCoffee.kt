package com.myapp.mycoffeestyle.recyclerAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myapp.mycoffeestyle.R
import com.myapp.mycoffeestyle.database.PhotoInDatabase
import com.myapp.mycoffeestyle.databinding.ListItemPhotoBinding

class AdapterPhotoCoffee(
    private val listPhotos: ArrayList<PhotoInDatabase>,
    private val callBack: ListenerCallback
) : RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding: ListItemPhotoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_photo,
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.dataBinding.viewRecycler = listPhotos[position]

        val isExpandable: Boolean = listPhotos[position].expandable
        holder.dataBinding.constraintItem.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.dataBinding.photoCoffee.setOnClickListener {
            val myPosition = listPhotos[position]
            myPosition.expandable = !listPhotos[position].expandable
            notifyItemChanged(position)
        }
        holder.dataBinding.delete.setOnClickListener {
            callBack.onClick(it, listPhotos[position])
            notifyItemChanged(position)
        }
        holder.dataBinding.shared.setOnClickListener {
            callBack.onClick(it, listPhotos[position])
        }
    }

    override fun getItemCount() = listPhotos.size

    fun setData(newFavorite: ArrayList<PhotoInDatabase>) {
        val diffCallback = PhotoDiff(newFavorite, listPhotos)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listPhotos.clear()
        listPhotos.addAll(newFavorite)
        diffResult.dispatchUpdatesTo(this)
    }
}

interface ListenerCallback {
    fun onClick(view: View, data: PhotoInDatabase)
}


class PhotoViewHolder(val dataBinding: ListItemPhotoBinding) :
    RecyclerView.ViewHolder(dataBinding.root)

class PhotoDiff(
    private val newList: ArrayList<PhotoInDatabase>,
    private val oldList: ArrayList<PhotoInDatabase>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].position == newList[newItemPosition].position
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}