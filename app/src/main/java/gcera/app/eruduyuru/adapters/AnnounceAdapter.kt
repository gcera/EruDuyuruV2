package gcera.app.eruduyuru.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.models.Announce
import kotlinx.android.synthetic.main.announce_item.view.*
import java.util.*

class AnnounceAdapter(var data:List<Announce>,private val onClick:(pos:Int)->Unit):
    RecyclerView.Adapter<AnnounceAdapter.AnnounceViewHolder>() {
    class AnnounceViewHolder( val itemview: View):RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnounceViewHolder {
        return AnnounceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.announce_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AnnounceViewHolder, position: Int) {
        holder.itemview.apply {
            titleText.setOnClickListener {
                onClick(position)
            }
            var title=data[position].title

            title=if (title.length>75) (title.substring(0,74)+"..").toUpperCase(Locale.getDefault())
            else title.toUpperCase(Locale.getDefault())
            titleText.text=title
        }
    }

    override fun getItemCount(): Int =data.size
}