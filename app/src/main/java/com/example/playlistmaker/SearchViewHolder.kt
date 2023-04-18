import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.TrackData

class SearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    private val artWork: ImageView = itemView.findViewById((R.id.iv_artWork))
    private val trackName: TextView = itemView.findViewById((R.id.tv_trackName))
    private val artistName: TextView = itemView.findViewById((R.id.tv_artistName))
    private val trackTime: TextView = itemView.findViewById((R.id.tv_trackTime))

    fun bind(item: TrackData){
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = item.trackTime
        glideBind(item.artworkUrl100, artWork)
    }

    private fun glideBind(link: String, target: ImageView){
        Glide.with(target)
            .load(link)
            .placeholder(R.drawable.ic_playlist_stub)
            .centerInside()
            .transform(RoundedCorners(2))
            .into(target)
    }
}