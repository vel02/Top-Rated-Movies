package kiz.learnwithvel.topratedmovies.adapter.viewholder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import kiz.learnwithvel.topratedmovies.R;
import kiz.learnwithvel.topratedmovies.adapter.BaseViewHolder;
import kiz.learnwithvel.topratedmovies.model.Video;

public class VideoViewHolder extends BaseViewHolder {

    TextView title, type, site;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.title = itemView.findViewById(R.id.content_video_title);
        this.type = itemView.findViewById(R.id.content_video_type);
        this.site = itemView.findViewById(R.id.content_video_site);
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
}
