package com.korpaev.myhome;

import android.content.Context;
import android.media.AudioManager;

public class SoundManage
{
    private static AudioManager audioManager;
    private int currSoundVolume;
    SoundManage()
    {

    }
    SoundManage(Context context)
    {
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void SetSoundOn()
    {
        currSoundVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, currSoundVolume, 1);

        currSoundVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, currSoundVolume, 1);

        currSoundVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currSoundVolume, 1);

        //currSoundVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        //audioManager.setStreamVolume(AudioManager.STREAM_RING, currSoundVolume, 1);

        currSoundVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, currSoundVolume, 1);

    }

    public void SetSoundOff()
    {
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        //audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);

    }
}
