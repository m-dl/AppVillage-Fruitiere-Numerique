package theron_b.com.visitetablette.tool;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import java.util.HashMap;
import java.util.Locale;

import theron_b.com.visitetablette.tool.Listener.MyUtteranceProgressListener;

public class Speaker implements TextToSpeech.OnInitListener {

    private TextToSpeech    m_TextToSpeech;
    private String          m_Text;

    private boolean         m_Ready;
    private boolean         m_French;

    private MediaPlayer     m_MediaPlayer;

    private MyUtteranceProgressListener myUtteranceProgressListener;

    final private Context         m_Context;

    public Speaker(Context context, boolean french, String text) {
        m_Ready = false;
        m_French = french;
        m_TextToSpeech = new TextToSpeech(context, this);
        m_Text = text;
        m_Context = context;
    }

    private void createFile() {
        HashMap<String, String> myHashRender = new HashMap<>();
        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "end of wake up message ID");

        final String destFileName = Environment.getExternalStorageDirectory() + "/VisiteTablette/" + "tts_file.mp3";
        //todo
        m_TextToSpeech.synthesizeToFile(m_Text, myHashRender, destFileName);
        myUtteranceProgressListener = new MyUtteranceProgressListener(m_MediaPlayer, m_Context, destFileName);
        m_TextToSpeech.setOnUtteranceProgressListener(myUtteranceProgressListener);
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            if (m_French)
                m_TextToSpeech.setLanguage(Locale.FRANCE);
            else
                m_TextToSpeech.setLanguage(Locale.US);
            m_Ready = true;
            createFile();
        }else{
            m_Ready = false;
        }
    }

    public boolean speak(){
        if(m_Ready) {
            if (!myUtteranceProgressListener.isM_FileNotReady()) {
                m_MediaPlayer = myUtteranceProgressListener.getM_MediaPlayer();
                m_MediaPlayer.start();
            }
        }
        return !myUtteranceProgressListener.isM_FileNotReady();
    }

    public void pause(){
        m_MediaPlayer.pause();
    }

    public void destroy(){
        m_TextToSpeech.shutdown();
        if (m_MediaPlayer != null)
            m_MediaPlayer.release();
    }
}
