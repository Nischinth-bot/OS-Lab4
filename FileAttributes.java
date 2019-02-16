
class FileAttributes
{
    private String filePath;        //URL
    private String downloadDir;     //destination download directory
    private String fileName;        //destination file
    private long downloadAmt;       //amount downloaded

    //constructor
    FileAttributes(String filePath, String downloadDir)
    {
        this.downloadDir = downloadDir;  
        this.downloadAmt = 0;
        this.filePath = filePath;
        this.fileName = initFileName(filePath);
    }

    //strip off the piece after the right most / and use that
    //as the filename
    private String initFileName(String path)
    {
        String[] tokens = path.split("/");
        int index = tokens.length - 1;
        if (index < 0) return null;
        if (tokens[index] == "") index--;
        if (index < 0) return null;
        return tokens[index];
    }

    //called by Downloader object to update the current
    //amount downloaded
    public void updateDownload(long amount)
    {
        this.downloadAmt += amount;
    }

    //return the file name
    public String getFileName()
    {
        return fileName;
    }

    //return the url
    public String getFilePath()
    {
        return filePath;
    }

    //return the destination download directory
    public String getDownloadDir()
    {
        return downloadDir;
    }

    //return the downloaded amount
    public long getDownloadAmount()
    {
        return downloadAmt;
    }
}
