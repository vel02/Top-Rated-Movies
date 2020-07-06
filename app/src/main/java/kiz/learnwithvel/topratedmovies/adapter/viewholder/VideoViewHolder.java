package kiz.learnwithvel.topratedmovies.adapter.viewholder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import kiz.learnwithvel.topratedmovies.R;
import kiz.learnwithvel.topratedmovies.adapter.BaseViewHolder;
import kiz.learnwithvel.topratedmovies.model.Video;

import static kiz.learnwithvel.topratedmovies.adapter.VideoRecyclerAdapter.OnVideoClickListener;

public class VideoViewHolder extends BaseViewHolder implements View.OnClickListener {

    TextView title, type, site;

    final OnVideoClickListener listener;

    public VideoViewHolder(@NonNull View itemView, OnVideoClickListener listener) {
        super(itemView);
        this.listener = listener;
        this.title = itemView.findViewById(R.id.content_video_title);
        this.type = itemView.findViewById(R.id.content_video_type);
        this.site = itemView.findViewById(R.id.content_video_site);
        this.itemView.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBind(Video video) {
        super.onBind(video);
        this.title.setText(video.getName());
        this.type.setText(video.getType());
        this.site.setText(video.getSite() + " - " + video.getKey());
    }

    @Override
    protected void clear() {
        this.title.setText("");
        this.type.setText("");
        this.site.setText("");
    }

    @Override
    protected void setupImage() {
    }

    @Override
    protected String format() {
        return null;
    }

    @Override
    public void onClick(View view) {
        this.listener.onClick(getVideo());
    }
}
