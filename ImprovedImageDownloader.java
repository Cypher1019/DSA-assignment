package Question6;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.SwingUtilities;

/**
 * A Java Swing application for asynchronously downloading images from URLs.
 */
public class ImprovedImageDownloader extends javax.swing.JFrame {

    private ExecutorService threadPool;
    private static final String DOWNLOAD_DIRECTORY = "downloaded_files/";
    private List<Future<?>> downloadTasks;
    private Map<Future<?>, DownloadInfo> downloadInfoMap;

    /**
     * Constructor for ImprovedImageDownloader class.
     */
    public ImprovedImageDownloader() {
        initComponents();
        threadPool = Executors.newFixedThreadPool(10); // Increased thread pool size for better concurrency
        downloadTasks = new ArrayList<>();
        downloadInfoMap = new ConcurrentHashMap<>();
    }

    /**
     * Method to initialize GUI components.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        // GUI components initialization code
        // Omitted for brevity
    }

    /**
     * Action performed when download button is clicked.
     * Downloads images from the URLs entered in the text field.
     */
    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String urlsText = urlTextField.getText();
        String[] urls = urlsText.split("[,\\s]+");

        for (String url : urls) {
            if (!url.isEmpty()) {
                downloadImage(url.trim());
            }
        }
    }

    /**
     * Action performed when pause button is clicked.
     * Pauses all active image downloads.
     */
    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        pauseDownloads();
    }

    /**
     * Action performed when resume button is clicked.
     * Resumes paused image downloads.
     */
    private void resumeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        resumeDownloads();
    }

    /**
     * Action performed when cancel button is clicked.
     * Cancels all active image downloads.
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        cancelDownloads();
    }

    /**
     * Downloads an image from the specified URL.
     * @param urlString The URL of the image to download.
     */
    private void downloadImage(String urlString) {
        Runnable downloadTask = () -> {
            DownloadInfo downloadInfo = downloadInfoMap.get(Thread.currentThread());
            int progress = downloadInfo != null ? downloadInfo.getProgress() : 0;

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                if (progress > 0) {
                    connection.setRequestProperty("Range", "bytes=" + progress + "-");
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL) {
                    int contentLength = connection.getContentLength();
                    InputStream inputStream = connection.getInputStream();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        progress += bytesRead;
                        int currentProgress = (int) ((progress / (double) contentLength) * 100);
                        SwingUtilities.invokeLater(() -> progress_bar.setValue(currentProgress));

                        if (Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException("Download interrupted");
                        }

                        Thread.sleep(50);
                    }

                    String fileName = "image_" + System.currentTimeMillis() + ".jpg";
                    saveImage(outputStream.toByteArray(), fileName);

                    inputStream.close();
                    outputStream.close();
                } else {
                    throw new IOException("Failed to download image. Response code: " + responseCode);
                }
            } catch (IOException | InterruptedException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                e.printStackTrace(); // Display error to console
            }
        };

        Future<?> task = threadPool.submit(downloadTask);
        downloadTasks.add(task);
        downloadInfoMap.put(task, new DownloadInfo(urlString, 0));
    }

    /**
     * Saves the downloaded image data to a file.
     * @param imageData The byte array containing the image data.
     * @param fileName The name of the file to save.
     */
    private void saveImage(byte[] imageData, String fileName) {
        File directory = new File(DOWNLOAD_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fullPath = DOWNLOAD_DIRECTORY + fileName;

        try {
            FileOutputStream outputStream = new FileOutputStream(fullPath);
            outputStream.write(imageData);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace(); // Display error to console
        }
    }

    /**
     * Resumes paused image downloads.
     */
    private void resumeDownloads() {
        for (Future<?> task : downloadTasks) {
            if (task.isCancelled()) {
                DownloadInfo downloadInfo = downloadInfoMap.get(task);
                if (downloadInfo != null) {
                    downloadImage(downloadInfo.getUrl());
                }
            }
        }
    }

    /**
     * Pauses all active image downloads.
     */
    private void pauseDownloads() {
        for (Future<?> task : downloadTasks) {
            if (!task.isDone() && !task.isCancelled()) {
                task.cancel(true);
            }
        }
    }

    /**
     * Cancels all active image downloads.
     */
    private void cancelDownloads() {
        for (Future<?> task : downloadTasks) {
            task.cancel(true);
        }
        progress_bar.setValue(0);
    }

    /**
     * Inner class to hold download information.
     */
    private class DownloadInfo {
        private String url;
        private int progress;

        /**
         * Constructor for DownloadInfo class.
         * @param url The URL of the download.
         * @param progress The progress of the download.
         */
        public DownloadInfo(String url, int progress) {
            this.url = url;
            this.progress = progress;
        }

        /**
         * Gets the URL of the download.
         * @return The URL of the download.
         */
        public String getUrl() {
            return url;
        }

        /**
         * Gets the progress of the download.
         * @return The progress of the download.
         */
        public int getProgress() {
            return progress;
        }
    }

    /**
     * Main method to launch the application.
     * @param args Command-line arguments (not used).
     */
    public static void main(String args[]) {
        // Set look and feel code
        // Omitted for brevity

        java.awt.EventQueue.invokeLater(() -> {
            new ImprovedImageDownloader().setVisible(true);
        });
    }

    // GUI components declaration (omitted for brevity)
    private javax.swing.JButton cancelbtn;
    private javax.swing.JButton downloadbtn;
    private javax.swing.JTextField urlTextField;
    private javax.swing.JButton pausebtn;
    private javax.swing.JProgressBar progress_bar;
    private javax.swing.JButton resumebtn;
}
