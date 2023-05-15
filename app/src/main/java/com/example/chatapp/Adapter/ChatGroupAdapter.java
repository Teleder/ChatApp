package com.example.chatapp.Adapter;

import static com.example.chatapp.Utils.CONSTS.TYPE_RECEIVED_AUDIO;
import static com.example.chatapp.Utils.CONSTS.TYPE_RECEIVED_FILE;
import static com.example.chatapp.Utils.CONSTS.TYPE_RECEIVED_IMAGE;
import static com.example.chatapp.Utils.CONSTS.TYPE_RECEIVED_STICKER;
import static com.example.chatapp.Utils.CONSTS.TYPE_RECEIVED_TEXT;
import static com.example.chatapp.Utils.CONSTS.TYPE_RECEIVED_VIDEO;
import static com.example.chatapp.Utils.CONSTS.TYPE_SENT_FILE;
import static com.example.chatapp.Utils.Utils.downloadFile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Utils.CONSTS;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;

public class ChatGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messageList;
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private Context context;
    private OnLoadMoreListener onLoadMoreListener;
    private APIService apiService;
    private RetrofitClient retrofitClient;
    private UserProfileDto currentUser;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private final MessageClickListener messageClickListener;


    UserProfileDto contact;
    //... add other message types here

    public ChatGroupAdapter(RecyclerView recyclerView, Context context, List<Message> messageList, APIService apiService, RetrofitClient retrofitClient, MessageClickListener messageClickListener) {
        this.retrofitClient = retrofitClient;
        this.apiService = apiService;
        this.context = context;
        this.messageList = messageList;
        this.messageClickListener = messageClickListener;
        currentUser = SharedPrefManager.getInstance(context).getUser();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getUserId_send() == null)
            return -1;
        if (message.getUserId_send().equals(currentUser.getId())) {
            switch (message.getType()) {
                case CONSTS.MESSAGE_PRIVATE:
                case CONSTS.MESSAGE_GROUP:
                    return CONSTS.TYPE_SENT_TEXT;
                case CONSTS.IMAGE:
                    return CONSTS.TYPE_SENT_IMAGE;
                case CONSTS.STICKER:
                    return CONSTS.TYPE_SENT_STICKER;
                case CONSTS.AUDIO:
                    return CONSTS.TYPE_SENT_AUDIO;
                case CONSTS.VIDEO:
                    return CONSTS.TYPE_SENT_VIDEO;
                case CONSTS.FILE:
                    return CONSTS.TYPE_SENT_FILE;
                default:
                    return -1;
            }
        } else {
            switch (message.getType()) {
                case CONSTS.MESSAGE_PRIVATE:
                case CONSTS.MESSAGE_GROUP:
                    return TYPE_RECEIVED_TEXT;
                case CONSTS.IMAGE:
                    return TYPE_RECEIVED_IMAGE;
                case CONSTS.STICKER:
                    return TYPE_RECEIVED_STICKER;
                case CONSTS.AUDIO:
                    return TYPE_RECEIVED_AUDIO;
                case CONSTS.VIDEO:
                    return TYPE_RECEIVED_VIDEO;
                case CONSTS.FILE:
                    return TYPE_RECEIVED_FILE;
                default:
                    return -1;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case CONSTS.TYPE_SENT_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text_sent, parent, false);
                return new SentTextViewHolder(view);
            case TYPE_RECEIVED_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text_receive, parent, false);
                return new ReceivedTextViewHolder(view);
            case CONSTS.TYPE_SENT_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_image_sent, parent, false);
                return new SentImageViewHolder(view);
            case TYPE_RECEIVED_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_image_received, parent, false);
                return new ReceivedImageViewHolder(view);
            case CONSTS.TYPE_SENT_STICKER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_sticker_sent, parent, false);
                return new SentStickerViewHolder(view);
            case TYPE_RECEIVED_STICKER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_sticker_received, parent, false);
                return new ReceivedStickerViewHolder(view);
            case CONSTS.TYPE_SENT_AUDIO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_audio_sent, parent, false);
                return new SentAudioViewHolder(view);
            case TYPE_RECEIVED_AUDIO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_audio_received, parent, false);
                return new ReceivedAudioViewHolder(view);
            case CONSTS.TYPE_SENT_VIDEO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_video_sent, parent, false);
                return new SentVideoViewHolder(view);
            case TYPE_RECEIVED_VIDEO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_video_received, parent, false);
                return new ReceivedVideoViewHolder(view);
            case CONSTS.TYPE_SENT_FILE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_file_sent, parent, false);
                return new SentFileViewHolder(view);
            case TYPE_RECEIVED_FILE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_file_received, parent, false);
                return new ReceivedFileViewHolder(view);
            case -1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_announce, parent, false);
                return new AnnounceViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        switch (holder.getItemViewType()) {
            case CONSTS.TYPE_SENT_TEXT:
                ((SentTextViewHolder) holder).bind(message);
                break;
            case TYPE_RECEIVED_TEXT:
                ((ReceivedTextViewHolder) holder).bind(message);
                break;
            case CONSTS.TYPE_SENT_IMAGE:
                ((SentImageViewHolder) holder).bind(message);
                break;
            case TYPE_RECEIVED_IMAGE:
                ((ReceivedImageViewHolder) holder).bind(message);
                break;
            case CONSTS.TYPE_SENT_STICKER:
                ((SentStickerViewHolder) holder).bind(message);
                break;
            case TYPE_RECEIVED_STICKER:
                ((ReceivedStickerViewHolder) holder).bind(message);
                break;
            case CONSTS.TYPE_SENT_AUDIO:
                ((SentAudioViewHolder) holder).bind(message);
                break;
            case TYPE_RECEIVED_AUDIO:
                ((ReceivedAudioViewHolder) holder).bind(message);
                break;
            case CONSTS.TYPE_SENT_VIDEO:
                ((SentVideoViewHolder) holder).bind(message);
                break;
            case TYPE_RECEIVED_VIDEO:
                ((ReceivedVideoViewHolder) holder).bind(message);
                break;
            case TYPE_SENT_FILE:
                ((SentFileViewHolder) holder).bind(message);
                break;
            case TYPE_RECEIVED_FILE:
                ((ReceivedFileViewHolder) holder).bind(message);
                break;
            case -1:
                ((AnnounceViewHolder) holder).bind(message);
                break;

        }
        if (position >= getItemCount() - 1 && onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore();
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // Text
    public class SentTextViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textDateTime;

        public SentTextViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.textMessage);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showMenu(v, getAdapterPosition(), 1);
                    return true;
                }
            });
        }
        public void bind(Message message) {
            textMessage.setText(message.getContent());
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
        }
    }

    public class ReceivedTextViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textDateTime;
        ImageView imageProfile;

        public ReceivedTextViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            imageProfile = itemView.findViewById(R.id.imageProfile);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showMenu(v, getAdapterPosition(), 1);
                    return true;
                }
            });
        }

        public void bind(Message message) {
            textMessage.setText(message.getContent());
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
            Glide.with(context).load(
                    contact.getAvatar() == null ? R.drawable.ic_people_24 :
                            contact.getAvatar().getUrl().replace("localhost:8080", "http://" + CONSTS.BASEURL)
            ).into(imageProfile);
            ;
        }
    }

    // Image
    public class SentImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageMessage;
        TextView textDateTime;
        Message message;
        private GestureDetector gestureDetector;

        public SentImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMessage = itemView.findViewById(R.id.imageMessage);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    // You have to return true here to allow further handling
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    // Intentionally left blank
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (message != null) {
                        messageClickListener.onImageClicked(message.getContent());
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    showMenu(itemView, getAdapterPosition(), 2);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }

        public void bind(Message message) {
            this.message = message;
            // TODO: Load image from message into imageMessage
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
            Glide.with(context).load(message.getContent().replace("localhost:8080", "http://" + CONSTS.BASEURL)).into(imageMessage);
        }
    }

    public class AnnounceViewHolder extends RecyclerView.ViewHolder {
        TextView textAnnounce;
        TextView textDateTime;

        public AnnounceViewHolder(@NonNull View itemView) {
            super(itemView);
            textAnnounce = itemView.findViewById(R.id.textAnnounce);
            textDateTime = itemView.findViewById(R.id.textDateTime);
        }

        public void bind(Message message) {
            // TODO: Load image from message into imageMessage
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
            textAnnounce.setText(message.getContent());
        }
    }

    public class ReceivedImageViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageProfile;
        ImageView imageMessage;
        TextView textDateTime;
        Message message;
        private GestureDetector gestureDetector;

        public ReceivedImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            imageMessage = itemView.findViewById(R.id.imageMessage);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    // You have to return true here to allow further handling
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    // Intentionally left blank
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (message != null) {
                        messageClickListener.onImageClicked(message.getContent());
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    showMenu(itemView, getAdapterPosition(), 2);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }

        public void bind(Message message) {
            this.message = message;
            // Load profile image from message into imageProfile
            // Load image from message into imageMessage
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
            Glide.with(context).load(message.getContent().replace("localhost:8080", "http://" + CONSTS.BASEURL)).into(imageMessage);
            Glide.with(context).load(
                    contact.getAvatar() == null ? R.drawable.ic_people_24 :
                            contact.getAvatar().getUrl().replace("localhost:8080", "http://" + CONSTS.BASEURL)
            ).into(imageProfile);
        }
    }


    // Video
    public class SentVideoViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnail;
        TextView textDateTime;
        ImageView playButton;
        Message message;
        private GestureDetector gestureDetector;

        public SentVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            playButton = itemView.findViewById(R.id.playButton);
            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    // You have to return true here to allow further handling
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    // Intentionally left blank
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (message != null) {
                        messageClickListener.onVideoClicked(message.getContent());
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    showMenu(itemView, getAdapterPosition(), 2);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }

        public void bind(Message message) {
            this.message = message;
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageClickListener.onVideoClicked(message.getContent());
                }
            });
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(message.getContent().replace("localhost:8080", "http://" + CONSTS.BASEURL),
                            MediaStore.Images.Thumbnails.MINI_KIND);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            videoThumbnail.setImageBitmap(thumb);
                        }
                    });
                }
            });
        }
    }

    public class ReceivedVideoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        ImageView videoThumbnail;
        TextView textDateTime;
        ImageView playButton;
        Message message;
        private GestureDetector gestureDetector;

        public ReceivedVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            playButton = itemView.findViewById(R.id.playButton);
            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    // You have to return true here to allow further handling
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    // Intentionally left blank
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (message != null) {
                        messageClickListener.onVideoClicked(message.getContent());
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    showMenu(itemView, getAdapterPosition(), 2);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

        }

        public void bind(Message message) {
            this.message = message;
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
            Glide.with(context).load(
                    contact.getAvatar() == null ? R.drawable.ic_people_24 :
                            contact.getAvatar().getUrl().replace("localhost:8080", "http://" + CONSTS.BASEURL)
            ).into(imageProfile);
            ;
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageClickListener.onVideoClicked(message.getContent());
                }
            });
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(message.getContent().replace("localhost:8080", "http://" + CONSTS.BASEURL),
                            MediaStore.Images.Thumbnails.MINI_KIND);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            videoThumbnail.setImageBitmap(thumb);
                        }
                    });
                }
            });
        }
    }

    // audio
    public class SentAudioViewHolder extends RecyclerView.ViewHolder {
        ImageView audioIcon;
        TextView textDateTime, audioDuration;
        Message message;
        private GestureDetector gestureDetector;

        public SentAudioViewHolder(@NonNull View itemView) {
            super(itemView);
            audioIcon = itemView.findViewById(R.id.audioIcon);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            audioDuration = itemView.findViewById(R.id.audioDuration);
            gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    // You have to return true here to allow further handling
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    // Intentionally left blank
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (message != null) {
                        messageClickListener.onAudioClicked(message.getContent(), audioDuration);
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    showMenu(itemView, getAdapterPosition(), 2);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });


        }

        public void bind(Message message) {
            this.message = message;
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
        }
    }

    public class ReceivedAudioViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile, audioIcon;
        TextView textDateTime, audioDuration;
        Message message;
        private GestureDetector gestureDetector;

        public ReceivedAudioViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            audioIcon = itemView.findViewById(R.id.audioIcon);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            audioDuration = itemView.findViewById(R.id.audioDuration);


            gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    // You have to return true here to allow further handling
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    // Intentionally left blank
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (message != null) {
                        messageClickListener.onAudioClicked(message.getContent(), audioDuration);
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    showMenu(itemView, getAdapterPosition(), 2);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }

        public void bind(Message message) {
            this.message = message;
            // TODO: Load profile image from message into imageProfile
            // TODO: Load image from message into imageMessage
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
//            audioIcon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    messageClickListener.onAudioClicked(message.getContent(), audioDuration);
//                }
//            });
        }
    }

    //    Sticker
    public class SentStickerViewHolder extends RecyclerView.ViewHolder {
        ImageView stickerImage;
        TextView textDateTime;

        public SentStickerViewHolder(@NonNull View itemView) {
            super(itemView);
            stickerImage = itemView.findViewById(R.id.stickerImage);
            textDateTime = itemView.findViewById(R.id.textDateTime);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showMenu(v, getAdapterPosition(), 3);
                    return true;
                }
            });
        }

        public void bind(Message message) {
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
            Glide.with(context).load(message.getContent().replace("localhost:8080", "http://" + CONSTS.BASEURL)).into(stickerImage);
        }
    }

    public class ReceivedStickerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile, stickerImage;
        TextView textDateTime;

        public ReceivedStickerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            stickerImage = itemView.findViewById(R.id.stickerImage);
            textDateTime = itemView.findViewById(R.id.textDateTime);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showMenu(v, getAdapterPosition(), 3);
                    return true;
                }
            });
        }

        public void bind(Message message) {
            // TODO: Load profile image from message into imageProfile
            // TODO: Load image from message into imageMessage
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
            Glide.with(context).load(message.getContent().replace("localhost:8080", "http://" + CONSTS.BASEURL)).into(stickerImage);
            Glide.with(context).load(
                    contact.getAvatar() == null ? R.drawable.ic_people_24 :
                            contact.getAvatar().getUrl().replace("localhost:8080", "http://" + CONSTS.BASEURL)
            ).into(imageProfile);
            ;

        }
    }

    // File
    public class SentFileViewHolder extends RecyclerView.ViewHolder {
        ImageView fileIcon;
        TextView textDateTime, fileName;
        Message message;
        private GestureDetector gestureDetector;

        public SentFileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileIcon = itemView.findViewById(R.id.fileIcon);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            fileName = itemView.findViewById(R.id.fileName);

            gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    // You have to return true here to allow further handling
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    // Intentionally left blank
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (message != null) {
                        messageClickListener.onFileClicked(message.getContent(), message.getFile().getName());
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    showMenu(itemView, getAdapterPosition(), 2);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }

        public void bind(Message message) {
            this.message = message;

            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
            fileName.setText(message.getFile().getName());
        }
    }

    // Define your ViewHolder for Received Image
    public class ReceivedFileViewHolder extends RecyclerView.ViewHolder {
        ImageView fileIcon, imageProfile;
        TextView textDateTime, fileName;
        Message message;
        private GestureDetector gestureDetector;

        public ReceivedFileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            fileIcon = itemView.findViewById(R.id.fileIcon);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            fileName = itemView.findViewById(R.id.fileName);

            gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    // You have to return true here to allow further handling
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    // Intentionally left blank
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (message != null) {
                        messageClickListener.onFileClicked(message.getContent(), message.getFile().getName());
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    showMenu(itemView, getAdapterPosition(), 2);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }

        public void bind(Message message) {
            this.message = message;
            textDateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getCreateAt()));
            fileName.setText(message.getFile().getName());
            Glide.with(context).load(
                    contact.getAvatar() == null ? R.drawable.ic_people_24 :
                            contact.getAvatar().getUrl().replace("localhost:8080", "http://" + CONSTS.BASEURL)
            ).into(imageProfile);
            ;
        }
    }


    // This method will show the menu when a message is long clicked
    private void showMenu(View view, int position, int type) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.chat_message_menu);
        MenuItem downloadItem = popupMenu.getMenu().findItem(R.id.download);
        MenuItem editItem = popupMenu.getMenu().findItem(R.id.edit);
        MenuItem deleteItem = popupMenu.getMenu().findItem(R.id.delete);
        switch (type) {
            case 1:
            case 3:
                downloadItem.setVisible(false);
                editItem.setVisible(false);
                deleteItem.setVisible(false);
                break;
            case 2:
                downloadItem.setVisible(true);
                editItem.setVisible(false);
                deleteItem.setVisible(false);
                break;
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.download) {
                    downloadFile(context, messageList.get(position).getContent().replace("localhost:8080", "http://" + CONSTS.BASEURL), String.valueOf(UUID.randomUUID()));
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public interface MessageClickListener {
        void onImageClicked(String imageUrl);

        void onAudioClicked(String audioUrl, TextView audioDuration);

        void onVideoClicked(String videoUrl);

        void onFileClicked(String fileUrl, String fileName);
    }


}