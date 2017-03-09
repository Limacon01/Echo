package echo.Computational;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.List;

public class Detective implements Runnable {
    private static final float  SAMPLE_RATE = 44100f;  /* MHz  */
    private static final int    SAMPLE_SIZE = 16;    /* bits */
    private static final int    SAMPLE_CHANNELS = 1;     /* mono */
    private static final int    SAMPLE_THRESHOLD = 1000;

    private List<SoundDetectedListener> soundDetectedListeners = new ArrayList<>();

    public void addListener(SoundDetectedListener sdl) {
        System.out.println("adding listener");
        soundDetectedListeners.add(sdl);
    }

    public Detective() {

    }

    //TODO Use setupStream from RecordSound.java (abstract both out)
    @Override
    public void run() {
        if (!soundDetectedListeners.isEmpty()) {
            AudioFormat af =
                    new AudioFormat(SAMPLE_RATE
                            , SAMPLE_SIZE
                            , SAMPLE_CHANNELS
                            , true /* signed */
                            , false /* big-endian */
                    );
            final int bufferSize = 2048;

            TargetDataLine dataLine;
            try {
                dataLine = AudioSystem.getTargetDataLine(af);
                dataLine.open(af, bufferSize);
            } catch (LineUnavailableException e) {
                System.out.println("Couldn't open Target Data Line");
                e.printStackTrace();
                return;
            }

            byte[] buffer = new byte[bufferSize];
            float[] samples = new float[bufferSize / 2];

            // We could put in an 'averager' later
            dataLine.start();
            for (int readByte; (readByte = dataLine.read(buffer, 0, buffer.length)) > -1; ) {

                // convert readBytes into samples:
                for (int i = 0, s = 0; i < readByte; ) {
                    int sample = 0;
                    //TODO change order if changed to big endian / using same method as recordSounds
                    sample |= buffer[i++] & 0xFF;
                    sample |= buffer[i++] << 8;

                    // normalize to a range of +/-1.0f
                    samples[s++] = sample;/// 32768f;
                }

                //Analyse samples
                for (float sample : samples) {
                    float absSample = Math.abs(sample);
                    if (absSample > SAMPLE_THRESHOLD) {
                        for (SoundDetectedListener sdl : soundDetectedListeners) {
                            dataLine.close();
                            sdl.soundDetected();
                            System.out.println("Sound detected");
                            return;
                        }
                    }
                }

            }
        }
    }
}