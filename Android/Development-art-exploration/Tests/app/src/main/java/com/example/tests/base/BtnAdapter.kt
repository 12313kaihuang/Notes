package com.example.tests.base

import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

/**
 * @auther hy
 * create on 2021/07/30 下午3:32
 */
class BtnAdapter : ListAdapter<BtnAdapter.BtnItem, BtnAdapter.BtnHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BtnHolder {
        val btn = Button(parent.context).apply {
            isAllCaps = false
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return BtnHolder(btn)
    }


    override fun onBindViewHolder(holder: BtnHolder, position: Int) =
        holder.bindItem(getItem(position))

    class BtnHolder(private val btn: Button) : RecyclerView.ViewHolder(btn) {
        fun bindItem(item: BtnItem) {
            btn.text = item.text
            btn.isAllCaps = false
            btn.setOnClickListener {
                try {
                    item.clickListener?.invoke()
                } catch (e: Exception) {
                    Toast.makeText(
                        it.context,
                        "error:${Log.getStackTraceString(e)}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("BtnAdapter", "btn(${item.text}) onLick error ", e)
                }
            }
        }
    }

    data class BtnItem(val text: String, val clickListener: (() -> Unit)?)

    class DiffCallback : DiffUtil.ItemCallback<BtnItem>() {
        override fun areItemsTheSame(oldItem: BtnItem, newItem: BtnItem): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: BtnItem, newItem: BtnItem): Boolean =
            oldItem == newItem

    }
}