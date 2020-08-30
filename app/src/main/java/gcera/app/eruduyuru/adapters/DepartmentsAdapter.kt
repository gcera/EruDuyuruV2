package gcera.app.eruduyuru.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.models.DepartsListItem
import kotlinx.android.synthetic.main.departs_item.view.*

class DepartmentsAdapter(
    var data:List<DepartsListItem>,
    val clickAction:(pos:Int)->Unit,
    val onLongPress:(pos:Int)->Boolean
)
    :RecyclerView.Adapter<DepartmentsAdapter.DepartmentsViewHolder>() {
    class DepartmentsViewHolder(view:View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentsViewHolder {
        return DepartmentsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.departs_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: DepartmentsViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(this).load(data[position].resId).into(logoImage)
            departCard.setOnClickListener { clickAction(position) }
            departCard.setOnLongClickListener { onLongPress(position)}
            departmentText.text=data[position].name
        }
    }

    override fun getItemCount(): Int =data.size
}