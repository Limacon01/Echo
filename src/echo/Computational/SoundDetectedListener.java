package echo.Computational;

/**
 * @version 1.0
 * Listener that is informed when the Echo device detects a sound above
 * a specific intensity threshold
 */
public interface SoundDetectedListener {
    void soundDetected();
}
