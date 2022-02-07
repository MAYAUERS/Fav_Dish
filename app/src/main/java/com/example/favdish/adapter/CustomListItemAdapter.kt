package com.example.favdish.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favdish.R
import com.example.favdish.databinding.ItemCustomeListBinding
import com.example.favdish.view.activities.AddUpdateActivity

class CustomListItemAdapter(private val activity:Activity,
                            private val listItems:List<String>,
                            private val selection:String) :RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomListItemAdapter.ViewHolder {
      val binding: ItemCustomeListBinding = ItemCustomeListBinding.inflate(LayoutInflater.from(activity),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomListItemAdapter.ViewHolder, position: Int) {
                val item=listItems[position]
                holder.tvText.text=item

        holder.itemView.setOnClickListener{
            if (activity is AddUpdateActivity){
                activity.selectedListItem(item,selection)
            }
        }
    }

    class ViewHolder(view: ItemCustomeListBinding):RecyclerView.ViewHolder(view.root) {

        val tvText=view.tvItemTitle

    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}