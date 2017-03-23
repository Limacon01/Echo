package echo.Computational;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 2.0
 *
 * Detects when sounds surpasses a certain threshold and alerts listeners
 */
public class Detective implements Runnable {
    // Variables are public for testing purposes:
    public static final float  SAMPLE_RATE = 32000f;    /* MHz  */
    public static final int    SAMPLE_SIZE = 16;        /* bits */
    public static final int    SAMPLE_CHANNELS = 1;     /* mono */
    public static final float  SAMPLE_THRESHOLD = 17500f;
    TargetDataLine dataLine;
    byte[] buffer;

    @Override
    public void run() {
        final int bufferSize = 2048;
        AudioFormat af = createAudioFormat();
        dataLine = createDataLine(bufferSize, af);
        buffer = new byte[bufferSize];
        float[] samples = new float[bufferSize / 2];

        dataLine.start();
        for (int readByte; (readByte = dataLine.read(buffer, 0, buffer.length)) > -1; ) {
            samples = convertBytesToSamples(readByte, samples);

            //Analyse samples
            for (float sample : samples) {
                float absSample = Math.abs(sample);

                if (absSample > SAMPLE_THRESHOLD) {
                    System.out.println("Sound detected");
                    dataLine.close();
                    return;
                }
            }
        }
    }



    /**
     * Converts a byte to a sample and normalises its range.
     * @param readByte      The current byte in the DataLine
     * @param samples       A list of previous samples
     * @return              The list of samples with the new byte data
     */
    public float[] convertBytesToSamples(int readByte, float[] samples) {
        for (int i = 0, s = 0; i < readByte; ) {
            int sample = 0;
            // Needs to be reversed if we use a big-endian
            sample |= buffer[i++] & 0xFF;
            sample |= buffer[i++] << 8;
            samples[s++] = sample;
        }
        return samples;
    }

    /**
     * Creates an audio format using Detective's final static
     * AudioFormat variables.
     * @return              New AudioFormat
     */
    public AudioFormat createAudioFormat() {
        return new AudioFormat(
                SAMPLE_RATE,
                SAMPLE_SIZE,
                SAMPLE_CHANNELS,
                true,
                false
        );
    }

    /**
     * Creates a new DataLine
     * @param bufferSize    Buffer size
     * @param af            Audio Format
     * @return              A new DataLine
     */
    public TargetDataLine createDataLine(int bufferSize, AudioFormat af) {
        try {
            dataLine = AudioSystem.getTargetDataLine(af);
            dataLine.open(af, bufferSize);
            return dataLine;
        } catch (LineUnavailableException e) {
            System.out.println("Couldn't open Target Data Line");
            e.printStackTrace();
            return null;
        }
    }
}