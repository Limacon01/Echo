package echo.Computational;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.1
 * Detects when sounds surpasses a certain threshold and alerts listeners
 */
public class Detective implements Runnable {
    private static final float  SAMPLE_RATE = 44100f;    /* MHz  */
    private static final int    SAMPLE_SIZE = 16;        /* bits */
    private static final int    SAMPLE_CHANNELS = 1;     /* mono */
    private static final float  SAMPLE_THRESHOLD = 30000f;

    @Override
    /**
     * if we have a listener in in our list, we open a target data line
     */
    public void run() {

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

        ///TODO We could put in an 'averager' later
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
                    dataLine.close();
                    System.out.println("Sound detected in Detecive class");
                    return;
                }
            }
        }
    }
}

