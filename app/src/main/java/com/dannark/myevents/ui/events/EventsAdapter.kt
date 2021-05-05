package com.dannark.myevents.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dannark.myevents.databinding.ItemListEventBinding
import com.dannark.myevents.databinding.ItemListHeaderBinding
import com.dannark.myevents.domain.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

/**
 * @author Daniel Queiroz
 *
 * An Adapter that provides a list of [Events] or [Header] to a RecyclerView
 * This Adapter uses a design pattern call Adapter Pattern
 * Setting [data] will cause the displayed list to update.
 *
 * Features:
 *  - It uses DiffUtil class to find the minimal changes in the list and update only that item
 *  - DiffUtil will also animes any changes on the list such as Adding new Item or Removing it..
 *  - The list can accept multiple type of ViewHolders
 */
class EventAdapter(val clickListener: EventItemListener)
    : ListAdapter<DataItem, RecyclerView.ViewHolder>(EventAdapterDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.EventItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
        }
    }

    fun addHeaderAndSubmitList(list: List<Event>){
        val dataList = list.map { DataItem.EventItem(it) }
        val header = listOf(DataItem.Header(list.size))

        adapterScope.launch {
            val items = when(list){
                null -> header
                else -> header + dataList
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder -> {
                val eventItem = getItem(position) as DataItem.EventItem
                holder.bind(eventItem.event, clickListener)
            }
            is TextViewHolder -> {
                val eventItem = getItem(position) as DataItem.Header
                holder.bind(eventItem.size)
            }
        }
    }

    class ViewHolder private constructor(val binding: ItemListEventBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Event, clickListener: EventItemListener){
            binding.event = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListEventBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class EventAdapterDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class EventItemListener(val clickListener: (view: View, event: Event) -> Unit) {
    fun onClick(view: View, event: Event) = clickListener(view, event)
}

sealed class DataItem {
    data class EventItem(val event: Event): DataItem(){
        override val id = event.id
    }

    data class Header(val size: Int): DataItem(){
        override val id = Long.MIN_VALUE.toString()
    }

    abstract val id: String
}

class TextViewHolder private constructor(val binding: ItemListHeaderBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(size: Int){
        binding.size = size
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): TextViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemListHeaderBinding .inflate(layoutInflater, parent, false)
            return TextViewHolder(binding)
        }
    }
}