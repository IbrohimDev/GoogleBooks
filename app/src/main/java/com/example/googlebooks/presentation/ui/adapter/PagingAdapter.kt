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
import com.example.googlebooks.data.local.dao.StarDao
import com.example.googlebooks.data.local.entity.StarEntity
import com.example.googlebooks.data.remote.response.Item
import com.example.googlebooks.databinding.ItemPagerBinding
import timber.log.Timber
import javax.inject.Inject

class PagingAdapter @Inject constructor(
    private val context: Context,
    private val starDao: StarDao,
) : PagingDataAdapter<Item, PagingAdapter.ViewHolder>(DiffUtils) {

    private var starDelete: ((String) -> (Unit))? = null
    private var starInsert: ((StarEntity) -> (Unit))? = null
    private var linkInfo: ((String) -> (Unit))? = null

    object DiffUtils : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemPagerBinding by viewBinding(ItemPagerBinding::bind)

        init {
            binding.bookLink.setOnClickListener {
                getItem(absoluteAdapterPosition)?.volumeInfo?.infoLink?.let { it1 ->
                    linkInfo?.invoke(
                        it1
                    )
                }
            }

            binding.starStatus.setOnClickListener {

                if (absoluteAdapterPosition != -1) {
                    val data = getItem(absoluteAdapterPosition)
                    if (data?.statusStar == true) {
                        data.statusStar = false
                        binding.starStatus.setImageResource(R.drawable.ic_start_border)
                        starDelete?.invoke(data.id)
                    } else {
                        data?.statusStar = true
                        binding.starStatus.setImageResource(R.drawable.ic_start_full)
                        data?.id?.let {
                            starInsert?.invoke(
                                StarEntity(
                                    0,
                                    data?.id,
                                    data?.volumeInfo?.authors?.get(0) ?: "",
                                    data?.volumeInfo?.title ?: "",
                                    data?.volumeInfo?.imageLinks?.smallThumbnail,
                                    data?.volumeInfo?.infoLink
                                )
                            )
                        }
                    }
                }else{
                    Timber.tag("IDABSOLUTE").d(absoluteAdapterPosition.toString())
                }


            }
        }


        fun onBind() {
            val data = getItem(absoluteAdapterPosition)

            data?.let { item ->


                if (item?.volumeInfo?.authors?.isNotEmpty() == true) {
                    binding.authorBook.text = item.volumeInfo.authors[0]
                }
                binding.nameBook.text = item.volumeInfo.title
                Timber.tag("HHHH").d(item?.volumeInfo?.imageLinks?.smallThumbnail)
                Glide.with(context).load(item?.volumeInfo?.imageLinks?.smallThumbnail)
                    .placeholder(R.drawable.no_image).into(binding.imageBook)

                if (item.statusStar)
                    binding.starStatus.setImageResource(R.drawable.ic_start_full)
                else
                    binding.starStatus.setImageResource(R.drawable.ic_start_border)
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

    fun setStarInsert(block: (StarEntity) -> (Unit)) {
        starInsert = block
    }

    fun setStarDelete(block: (String) -> Unit) {
        starDelete = block
    }

    fun setLinkInfo(block: (String) -> Unit) {
        linkInfo = block
    }


}