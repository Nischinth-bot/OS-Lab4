
class FileManager
{
    //These are the objects to download
    private String[] urls = 
    {
        "https://alvinalexander.com/java/java-string-array-reference-java-5-for-loop-syntax",
        "https://www.crimesceneinvestigatoredu.org/what-is-forensic-science/",
        "http://www.stewartonbibleschool.org.uk/bible/text/levit.txt",
        "http://shakespeare.mit.edu/richardiii/full.html",
        "https://www.tutorialspoint.com/java/java_multithreading.htm",
        "http://textfiles.com/etext/AUTHORS/SHAKESPEARE/shakespeare-coriolanus-24.txt",
        "https://archive.org/stream/warandpeace030164mbp/warandpeace030164mbp_djvu.txt",
        "https://www.olganon.org/home",
        "https://www.pcgamer.com/best-gaming-pc/",
        "http://textfiles.com/etext/AUTHORS/SHAKESPEARE/shakespeare-tragedy-58.txt",
        "http://www.stewartonbibleschool.org.uk/bible/text/genesis.txt",
        "http://triggs.djvu.org/djvu-editions.com/SHAKESPEARE/SONNETS/Download.pdf",
        "https://www.folgerdigitaltexts.org/download/pdf/Son.pdf",
        "http://shakespeare.mit.edu/merry_wives/full.html", 
    };

    private int next;                      //all files up to next have already been downloaded
    private int totalDownload;             //total amount downloaded (all files)
    private FileAttributes[] fileAttrsArr; //attributes of each file

    //create a FileManager object
    //downloadDir is the destination directory for the downloads
    FileManager(String downLoadDir)
    {
        int i;
        int size = urls.length;
        //create a FileAttributes object for each file
        fileAttrsArr = new FileAttributes[size];
        for (i = 0; i < size; i++)
           fileAttrsArr[i] = new FileAttributes(urls[i], downLoadDir);
        //next is the next file to download (initialize to 0)
        next = 0; 
        //totalDownload is the total amount downloaded (all files)
        totalDownload = 0;
    }

    //return the attributes of the next file to download or null
    //if all files have been downloaded
    public FileAttributes getNextFile()
    {
        if (next == fileAttrsArr.length) return null;
        next++;
        return (fileAttrsArr[next - 1]);
    }

    //update the total download amount (all files)
    public void updateTotalDownload(int amount)
    {
        totalDownload += amount;
    }

    //print an update of how much of each file has been downloaded
    public void printUpdate()
    {
        int i;
        System.out.print("\033[H\033[2J");
        for (i = 0; i < fileAttrsArr.length; i++)
        {
            System.out.println(fileAttrsArr[i].getThreadName() + " " + fileAttrsArr[i].getFileName() + ": " +
                               fileAttrsArr[i].getDownloadAmount());
        }
        System.out.println("Total download: " + totalDownload);
    }
}
