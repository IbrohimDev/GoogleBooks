package com.example.googlebooks.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.googlebooks.R
import com.example.googlebooks.data.local.entity.StarEntity
import com.example.googlebooks.databinding.ItemPagerBinding
import javax.inject.Inject

class PagingLocalAdapter @Inject constructor(
    private val context: Context
) : PagingDataAdapter<StarEntity, PagingLocalAdapter.ViewHolder>(DiffUtils) {

    private var starDelete: ((String) -> (Unit))? = null

    private var linkInfo: ((String) -> (Unit))? = null


    object DiffUtils : DiffUtil.ItemCallback<StarEntity>() {
        override fun areItemsTheSame(oldItem: StarEntity, newItem: StarEntity): Boolean =
            oldItem.bookId == newItem.bookId

        override fun areContentsTheSame(oldItem: StarEntity, newItem: StarEntity): Boolean =
            oldItem == newItem

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemPagerBinding by viewBinding(ItemPagerBinding::bind)

        init {
            binding.starStatus.setOnClickListener {
                getItem(absoluteAdapterPosition)?.bookId?.let { it1 -> starDelete?.invoke(it1) }
            }
            binding.bookLink.setOnClickListener {
                getItem(absoluteAdapterPosition)?.infoLink?.let { it1 ->
                    linkInfo?.invoke(
                        it1
                    )
                }
            }
        }


        fun onBind() {
            val data = getItem(absoluteAdapterPosition)

            data?.let { item ->

                binding.authorBook.text = item.author
                binding.nameBook.text = item.title
                Glide.with(context).load(item.imageLink).placeholder(R.drawable.no_image)
                    .into(binding.imageBook)


                binding.starStatus.setImageResource(R.drawable.ic_start_full)

            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pager, parent, false)
        return ViewHolder(itemView)
    }

    fun setStarDelete(block: (String) -> Unit) {
        starDelete = block
    }

    fun setLinkInfo(block: (String) -> Unit) {
        linkInfo = block
    }


}