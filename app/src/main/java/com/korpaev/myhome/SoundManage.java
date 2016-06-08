package com.korpaev.myhome;

import android.content.Context;
import android.media.AudioManager;

public class SoundManage
{
    private static AudioManager audioManager;
    private int currSV_StreamNotif;
    private int currSV_StreamAlarm;
    private int currSV_StreamMusic;
    private int currSV_StreamSystem;

    SoundManage(Context context)
    {
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        SetCurrentSoundVal();
    }

    private void SetCurrentSoundVal()
    {
        currSV_StreamNotif = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        currSV_StreamAlarm = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        currSV_StreamMusic = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        currSV_StreamSystem = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
    }

    public void SetSoundOn()
    {
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, currSV_StreamNotif, 1);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, currSV_StreamAlarm, 1);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currSV_StreamMusic, 1);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, currSV_StreamSystem, 1);
    }

    public void SetSoundOff()
    {
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
    }
}
