package kiz.learnwithvel.topratedmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kiz.learnwithvel.topratedmovies.R;
import kiz.learnwithvel.topratedmovies.adapter.viewholder.VideoViewHolder;
import kiz.learnwithvel.topratedmovies.model.Video;

public class VideoRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<Video> videos;

    private final OnVideoClickListener listener;

    public VideoRecyclerAdapter(OnVideoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_list_item, parent, false);
        return new VideoViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return ((videos != null && videos.size() > 0) ? videos.size() : 0);
    }

    public void addList(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public interface OnVideoClickListener {

        void onClick(Video video);

    }

}
