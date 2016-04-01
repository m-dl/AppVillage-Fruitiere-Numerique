package theron_b.com.visitetablette.tool;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MediaPlayerController {
    public MediaPlayerController() {

    }

    public MediaPlayer getMediaPlayer(Context context){
        MediaPlayer mediaplayer = new MediaPlayer();

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }

        try {
            Class<?> cMediaTimeProvider = Class.forName( "android.media.MediaTimeProvider" );
            Class<?> cSubtitleController = Class.forName( "android.media.SubtitleController" );
            Class<?> iSubtitleControllerAnchor = Class.forName( "android.media.SubtitleController$Anchor" );
            Class<?> iSubtitleControllerListener = Class.forName( "android.media.SubtitleController$Listener" );

            Constructor constructor = cSubtitleController.getConstructor(Context.class, cMediaTimeProvider, iSubtitleControllerListener);

            Object subtitleInstance = constructor.newInstance(context, null, null);

            Field f = cSubtitleController.getDeclaredField("mHandler");

            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            }
            catch (IllegalAccessException e) {return mediaplayer;}
            finally {
                f.setAccessible(false);
            }

            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);

            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
        } catch (Exception e) {
            //todo
        }
        return mediaplayer;
    }}
