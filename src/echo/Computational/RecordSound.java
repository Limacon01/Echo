package echo.Computational;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;


public class RecordSound {
    private static final int     TIMER           = 5;      /* secs */
    private static final int     SAMPLE_RATE     = 32000;  /* MHz  */
    private static final int     SAMPLE_SIZE     = 16;     /* bits */
    private static final int     SAMPLE_CHANNELS = 1;      /* mono */
    private static boolean       recording = true;
    static TargetDataLine line;

    /*
     * Set up stream.
     */
    public static AudioInputStream setupStream() {
        try {
            AudioFormat af =
                    new AudioFormat( SAMPLE_RATE
                            , SAMPLE_SIZE
                            , SAMPLE_CHANNELS
                            , true /* signed */
                            , true /* little-endian */
                    );
            DataLine.Info    info = new DataLine.Info( TargetDataLine.class, af );
            line = (TargetDataLine) AudioSystem.getLine( info );
            AudioInputStream stm  = new AudioInputStream( line );
            line.open( af );
            line.start();
            return stm;
        } catch ( Exception ex ) {
            System.out.println( ex );
            ex.printStackTrace();
            System.out.println("1");
            System.exit( 1 );
            return null;
        }
    }

    /**
     *
     */
    public static void closeDataLine(){
        line.close();
    }

    /*
     * Read stream.
     */
    public static ByteArrayOutputStream readStream( AudioInputStream stm ) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int  bufferSize = SAMPLE_RATE * stm.getFormat().getFrameSize();
            byte buffer[]   = new byte[ bufferSize ];

            for ( int counter = TIMER; counter > 0; counter-- ) {
                if(recording) {
                    int n = stm.read(buffer, 0, buffer.length);
                    System.out.println("Listening...");
                    if (n > 0) {
                        bos.write(buffer, 0, n);
                    } else {
                        break;
                    }
                }
            }

            return bos;
        } catch ( Exception ex ) {
            System.out.println( ex );
            System.out.println("2");
            System.exit( 1 );
            return null;
        }
    }

    /*
     * Record sound.
     */
    public static void recordSound( String name, ByteArrayOutputStream bos ) {
        try {
            AudioFormat af =
                    new AudioFormat( SAMPLE_RATE
                            , SAMPLE_SIZE
                            , SAMPLE_CHANNELS
                            , true /* signed */
                            , true /* little-endian */
                    );
            byte[]           ba   = bos.toByteArray();
            InputStream      is   = new ByteArrayInputStream( ba );
            AudioInputStream ais  = new AudioInputStream( is, af, ba.length );
            File             file = new File( name );
            AudioSystem.write( ais, AudioFileFormat.Type.WAVE, file );
        } catch ( Exception ex ) {
            System.out.println();
            System.out.println( ex );
            System.out.println("3");
            System.exit( 1 );
        }
    }
}
