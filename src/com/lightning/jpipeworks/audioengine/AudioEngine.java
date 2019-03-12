package com.lightning.jpipeworks.audioengine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import com.portaudio.BlockingStream;
import com.portaudio.PortAudio;
import com.portaudio.StreamParameters;

public class AudioEngine implements Runnable {
    private static boolean initialized = false;
    private static BlockingStream stream;
    private static Thread updateThread;
    private static byte[] bgm = null;
    private static int bgmPos;
    
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
        }
    }
    
    public synchronized static void setBGM(AudioInputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        bgm = result.toByteArray();
    }
    
    public void run() {
        stream.start();
        for(;;) {
            short[] next = new short[2000];
            
//            if(next.length == 0) {
//                try { Thread.sleep(1); } catch(InterruptedException e) {}
//                continue;
//            }
            
            for(int i = 0; i < next.length; i+=2) {
                short value = (short)((Math.random())*2000);
                next[i] = value;
                next[i+1] = value;
            }
            
            if(bgm != null) {
                byte[] data = new byte[4000];
                for(int i = 0; i < data.length; i++) {
                    data[i] = bgm[bgmPos];
                    bgmPos++;
                    if(bgmPos >= bgm.length) bgmPos = 0;
                }
                for(int i = 0; i < 2000; i+=2) {
                    next[i  ] += (data[i*2  ]&0x00FF)|((data[i*2+1]&0x00FF)<<8);
                    next[i+1] += (data[i*2+2]&0x00FF)|((data[i*2+3]&0x00FF)<<8);
                }
            }
            
            stream.write(next, 1000);
            Thread.yield();
        }
    }
}
