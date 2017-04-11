package helper;

import java.text.DecimalFormat;

/**
 * Helper methods for the terminal user interface.
 */
public class TuiHelper {
    /**
     * Start time in nano seconds.
     */
    private long startTime;

    /**
     * End time in nano seconds.
     */
    private long endTime;

    /**
     * Singleton instance.
     */
    private final static TuiHelper instance = new TuiHelper();

    /**
     * Returns the singleton instance.
     *
     * @return Singleton instance
     */
    public static TuiHelper getInstance() {
        return TuiHelper.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private TuiHelper() {}

    /**
     * Width of progress bar.
     */
    private int progressBarWidth = ConfigurationHelper.getInstance().getPropertyInteger("ProgressBarWidth", 10);

    /**
     * Shows a progress bar on the command line.
     * @param progress Current absolute progress
     * @param max Maximum progress
     */
    private String showProgressBar(int progress, int max) {
        StringBuilder out = new StringBuilder("[");
        int numberHashes = (int) (((float)progress / (float)max * 100f) / 10f * (progressBarWidth/10));
        for (int i = 0; i < numberHashes; i++) {
            out.append("#");
        }
        for (int i = numberHashes; i < progressBarWidth; i++) {
            out.append(" ");
        }
        out.append("]");

        return out.toString();
    }

    /**
     * Shows a progress on the command line.
     * @param progress Current absolute progress
     * @param max Maximum progress
     */
    public void showProgress(int progress, int max) {
        if (progress == 0) {
            startTime = System.nanoTime();
        }

        float percentage = (float) progress / (float) max * 100f;
        System.out.print(String.format("\rCalculation progress: %s %s%% (%d/%d)",
                showProgressBar(progress, max),
                (new DecimalFormat("0.00##")).format(percentage),
                progress, max));

        // add line-feed, if 100%
        if (progress == max) {
            endTime = System.nanoTime();
            System.out.println(" - Calculation took " +
                    (new DecimalFormat("0.00##")).format((endTime - startTime)/1000000000d)
                    + " seconds");
        }
    }
}
