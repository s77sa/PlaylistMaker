import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.TrackData
import com.example.playlistmaker.retrofit.Track


//class SearchAdapter (private val data: List<TrackData>) : RecyclerView.Adapter<SearchViewHolder> (){
class SearchAdapter (private val data: List<Track>) : RecyclerView.Adapter<SearchViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}