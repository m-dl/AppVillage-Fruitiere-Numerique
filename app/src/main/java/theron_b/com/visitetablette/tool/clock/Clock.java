package theron_b.com.visitetablette.tool.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.List;

public class Clock {
    private Time m_Time;
    private Handler m_Handler;
    private List<OnClockTickListner> m_OnClockTickListenerList = new ArrayList<>();

    private Runnable m_Ticker;

    private BroadcastReceiver m_IntentReceiver;

    Context m_Context;

    public Clock(Context context) {
        this.m_Context = context;
        this.m_Time = new Time();
        this.m_Time.setToNow();
        this.StartTickPerMinute();
    }

    private void Tick(long tickInMillis) {
        Clock.this.m_Time.set(Clock.this.m_Time.toMillis(true) + tickInMillis);
        this.NotifyOnTickListners();
    }

    private void NotifyOnTickListners() {
        for (OnClockTickListner listner : m_OnClockTickListenerList) {
            listner.OnMinuteTick(m_Time);
        }
    }

    private void StartTickPerMinute() {
        this.m_IntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Tick(60000);
            }
        };
        IntentFilter m_IntentFilter = new IntentFilter();
        m_IntentFilter.addAction(Intent.ACTION_TIME_TICK);
        this.m_Context.registerReceiver(this.m_IntentReceiver, m_IntentFilter, null, this.m_Handler);
    }

    public void StopTick() {
        if (this.m_IntentReceiver != null) {
            this.m_Context.unregisterReceiver(this.m_IntentReceiver);
        }
        if (this.m_Handler != null) {
            this.m_Handler.removeCallbacks(this.m_Ticker);
        }
    }

    public void AddClockTickListner(OnClockTickListner listner) {
        this.m_OnClockTickListenerList.add(listner);
    }
}