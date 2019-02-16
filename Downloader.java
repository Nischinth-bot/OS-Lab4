import java.io.*;
import java.net.URL;

class Downloader
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
       //get attributes of next file from File Manager
       FileAttributes fileAttrs = fm.getNextFile();
       while (fileAttrs != null)
       {
           //go the download
           downloadFile(fileAttrs);
           fileAttrs = fm.getNextFile();
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
                //update the downloaded amount that is maintained in the File Attributes
                fileAttrs.updateDownload(bytesRead);
                //update the total download amount that is maintained by the File Manager
                fm.updateTotalDownload(bytesRead);
            }
        } catch (IOException e) 
        {
            System.out.println("Unable to read " + fileURL);
        }
    }
}
