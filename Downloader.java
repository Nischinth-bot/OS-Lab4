import java.io.*;
import java.net.URL;

class Downloader extends Thread //Makes Downloader a subclass of Thread
{
    //File Manager provides the downloader with the
    //attributes of the next file to download
    private FileManager fm;
    //download file chunkSize bytes at a time
    private final int chunkSize = 1024;
    //constructor
    Downloader(FileManager fm)
    {
        this.fm = fm;
    }

    //get the attributes of files to download and download them.
    public void doDownloads()
    {
        //get attributes of next file from File Manager.
        //This statement is synchronized because we want each thread to access a different file 
        FileAttributes fileAttrs;
        synchronized(this){ fileAttrs = fm.getNextFile();}
        while (fileAttrs != null)
        {
        //    System.out.println(Thread.currentThread().getName() + " " + fileAttrs.getFileName());
            fileAttrs.setThreadName(Thread.currentThread().getName()); // set Thread name for the file attribute
            downloadFile(fileAttrs);  //go the download
            synchronized(this){ fileAttrs = fm.getNextFile(); }  //Threads will synchronize on which file to get. 
        }
    }

    public void downloadFile(FileAttributes fileAttrs)
    {
        //attributes of the file include the url, the filename to be used
        //for the destination, and the download directory 
        String fileURL = fileAttrs.getFilePath();
        String fileName = fileAttrs.getFileName();
        String downloadDir = fileAttrs.getDownloadDir();
        String destination = downloadDir + "/" + fileName;

        //download the object at the URL
        //store it in the destination
        try (BufferedInputStream in = new 
                BufferedInputStream(new URL(fileURL).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(destination)) 
        {
            //download in chunks of chunkSize bytes at a time
            byte dataBuffer[] = new byte[chunkSize];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, chunkSize)) != -1) 
            {
                //write the chunk read to the destination
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                
                //The code below is synchronized so that different threads do not attempt to update File Manager
                //info at the same time, which might lead to misreading.        
                synchronized(this) { 
                    fileAttrs.updateDownload(bytesRead); //update the downloaded amount that is maintained in the File Attributes
                    fm.updateTotalDownload(bytesRead); //update the total download amount that is maintained by the File Manager
                }
            }
        } catch (IOException e) 
        {
            System.out.println("Unable to read " + fileURL);
        }
    }


    //The run() method is what runs concurrently on differen threads.
    //In this case, each thread needs to call doDownloads() so that they all download the files at the same time.
    public void run()
    {
        doDownloads();
    }
}
