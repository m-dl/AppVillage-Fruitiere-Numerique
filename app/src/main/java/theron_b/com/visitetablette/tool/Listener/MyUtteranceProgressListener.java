package theron_b.com.visitetablette.tool.Listener;

import android.content.Context;
import android.media.MediaPlayer;
import android.speech.tts.UtteranceProgressListener;

import java.io.IOException;

import theron_b.com.visitetablette.tool.MediaPlayerController;

public class MyUtteranceProgressListener extends UtteranceProgressListener {
    private MediaPlayer m_MediaPlayer;
    private Context     m_Context;
    private boolean     m_FileNotReady;

    private String      m_DestFileName;

    public MyUtteranceProgressListener (MediaPlayer mediaPlayer, Context context, String destFileName) {
        m_MediaPlayer = mediaPlayer;
        m_Context = context;
        m_FileNotReady = true;
        m_DestFileName = destFileName;
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {
        m_FileNotReady = false;
        final MediaPlayerController mediaPlayerController = new MediaPlayerController();
        m_MediaPlayer = mediaPlayerController.getMediaPlayer(m_Context);
        try {
            m_MediaPlayer.setDataSource(m_DestFileName);
            m_MediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String utteranceId) {

    }

    public boolean isM_FileNotReady() {
        return m_FileNotReady;
    }

    public MediaPlayer getM_MediaPlayer() {
        return m_MediaPlayer;
    }
}
