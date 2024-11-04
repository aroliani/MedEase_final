package com.example.medease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class ViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final int imageResId;
    private final String videoId;

    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_VIDEO = 1;

    public ViewPagerAdapter(Context context, int imageResId, String videoId) {
        this.context = context;
        this.imageResId = imageResId;
        this.videoId = videoId;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_IMAGE : TYPE_VIDEO;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_IMAGE) {
            view = LayoutInflater.from(context).inflate(R.layout.image_page, parent, false);
            return new ImageViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.video_page, parent, false);
            return new VideoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).imageView.setImageResource(imageResId);
        } else if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Dua halaman: gambar dan video
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView youTubePlayerView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.youtubePlayerView);
        }
    }
}
