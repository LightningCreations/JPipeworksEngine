package com.lightning.jpipeworks.audioengine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;

import com.portaudio.BlockingStream;
import com.portaudio.PortAudio;
import com.portaudio.StreamParameters;

public class AudioEngine implements Runnable {
    private static boolean initialized = false;
    private static BlockingStream stream;
    private static Thread updateThread;
    public static byte[] bgm = null;
    public static int bgmPos;
    private static boolean bgmLoop = true;
    private static float bgmVolume = 1;
    private static ArrayList<byte[]> sfxList;
    private static byte[] playingSFX;
    private static int[] playingSFXPos;
    
    static {
        init();
    }
    
    protected AudioEngine() {} // Can only instantiate to provide Thread or to establish inheritance
    
    public synchronized static void init() {
        if(!initialized) {
            System.out.println("Starting Lightning Creations Audio Engine...");
            PortAudio.initialize();
            StreamParameters params = new StreamParameters();
            params.channelCount = 2;
            params.device = PortAudio.getDefaultOutputDevice();
            params.sampleFormat = PortAudio.FORMAT_INT_16;
            stream = PortAudio.openStream(null, params, 44100, 1000, 0);
            updateThread = new Thread(new AudioEngine());
            updateThread.start();
            initialized = true;
            sfxList = new ArrayList<>();
            playingSFX = new byte[32]; // 32 SFX at a time
            playingSFXPos = new int[32];
            for(int i = 0; i < playingSFX.length; i++) {
                playingSFX[i] = -1;
                playingSFXPos[i] = 0;
            }
        }
    }
    
    public synchronized static void setBGM(AudioInputStream stream) throws IOException {
        bgm = null; // Clear current BGM
        System.out.println(stream.getFormat().toString());
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        bgm = result.toByteArray();
        bgmPos = 0;
    }
    
    public synchronized static int addSFX(AudioInputStream stream) throws IOException {
        System.out.println(stream.getFormat().toString());
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        byte[] sfx = result.toByteArray();
        sfxList.add(sfx);
        return sfxList.size()-1;
    }
    
    public synchronized static void playSFX(int index) {
        for(int i = 0; i < playingSFX.length; i++) {
            if(playingSFX[i] == -1) {
                playingSFX[i] = (byte) index;
                playingSFXPos[i] = 0;
                break;
            }
        }
        // If there are no more slots left, don't play
    }
    
    public void run() {
        stream.start();
        for(;;) {
            short[] next = new short[2000];
            
//            if(next.length == 0) {
//                try { Thread.sleep(1); } catch(InterruptedException e) {}
//                continue;
//            }
            
            if(bgm != null) {
                byte[] data = new byte[4000];
                for(int i = 0; i < data.length; i++) {
                    data[i] = bgmPos < bgm.length ? bgm[bgmPos] : 0;
                    bgmPos++;
                    if(bgmPos >= bgm.length && bgmLoop) bgmPos = 0;
                }
                for(int i = 0; i < 2000; i+=2) {
                    next[i  ] = (short) (((short) ((data[i*2  ]&0x00FF)|((data[i*2+1]&0x00FF)<<8)))*bgmVolume);
                    next[i+1] = (short) (((short) ((data[i*2+2]&0x00FF)|((data[i*2+3]&0x00FF)<<8)))*bgmVolume);
                }
            }
            
            for(int i = 0; i < playingSFX.length; i++) {
                byte[] data = new byte[4000];
                if(playingSFX[i] == -1) continue;
                byte[] curSFX = sfxList.get(playingSFX[i]);
                int j = 0;
                for(; j < data.length; j++) {
                    data[j] = curSFX[playingSFXPos[i]];
                    playingSFXPos[i]++;
                    if(playingSFXPos[i] >= curSFX.length) {
                        playingSFX[i] = -1;
                        break;
                    }
                }
                if(j < data.length) {
                    for(; j < data.length; j++) {
                        data[j] = 0;
                    }
                }
                for(j = 0; j < 2000; j+=2) {
                    next[j  ] += (short) (((short) ((data[j*2  ]&0x00FF)|((data[j*2+1]&0x00FF)<<8))) * 0.1);
                    next[j+1] += (short) (((short) ((data[j*2+2]&0x00FF)|((data[j*2+3]&0x00FF)<<8))) * 0.1);
                }
            }
            
            stream.write(next, 1000);
            Thread.yield();
        }
    }

    public static void setBGMLoop(boolean loop) {
        bgmLoop = loop;
    }
}
