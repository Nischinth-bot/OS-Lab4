import java.io.File;
import java.lang.*;

//driver class for downloading files
class Grab
{
    boolean cleanOnly;   //true if only cleaning up a downloads directory
    String downloadDir;  //destination directory for the downloads

    //control starts here
    public static void main(String args[])
    {
        //calculate the time it takes to perform the download
        long startTime = System.currentTimeMillis();

        //parse command line arguments and create a Grab object
        Grab grabber = new Grab(args);
        //download all of the files
        grabber.grabFiles();

        //report the amount of time it took to do all of the downloads
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time (ms): " + 
                           (endTime - startTime));
    }

    //constructor
    Grab(String args[])
    {
        //program executed in one of these ways:
        //java Grab Downloads - download the files and put them in the 
        //                      Downloads directory (creating the directory
        //                      first; deleting old directory if necessary)
        //java Grab Downloads clean - delete Downloads directory and stop
        cleanOnly = false;
        if (args.length < 1) printUsage();
        if (args[0].equals("clean")) 
        {
            //don't let the downloads directory be named clean
            System.out.println("Provide the name of the downloads directory.");
            printUsage();
        }
        downloadDir = args[0];
        if (args.length > 1 && args[1].equals("clean")) cleanOnly = true;
    }

    //use a FileManager object and a Downloader object to do the actual
    //download
    public void grabFiles()
    {
        //delete old downloads directory and create a new one
        //unless program is only performing a clean
        cleanUp(downloadDir);
        if (cleanOnly == false)
        {
         
            //FileManager object provides the attributes of each file to download
            FileManager fm = new FileManager(downloadDir);
            //Downloader object performs the downloads, getting the attributes of
            //each file from the FileManager
            Downloader dl = new Downloader(fm);
            dl.doDownloads();
            //print the result of the download
            fm.printUpdate();
        } else
        {
            //if clean option used, print message and exit
            System.out.println("Removed directory " + downloadDir);
            System.exit(0);
        }

    }

    //print usage information
    public void printUsage()
    {
        System.out.println("usage: java Download <download directory> [clean]");
        System.out.println("       if clean option given, stop after deleting");
        System.out.println("       download directory and contents.");
        System.exit(0);
    }


    //delete and old downloads directory and create a new one
    //if a download is to be performed
    public void cleanUp(String downloadDir)
    {
        File dir = new File(downloadDir);
        if (dir.exists())
        {
            //perform a recursive delete of the directory and its contents
            deleteDirectory(dir);
        }
        try
        {
            //if not simply deleting the downloads directory, create a new one
            if (cleanOnly == false)
            {
                dir.mkdir();
                if (!dir.exists()) badDirectory();
            }
        } catch (Exception e)
        {
            badDirectory();
        }
    }
   
    //called when program is not able to create downloads directory
    private void badDirectory()
    {
        System.out.println("Unable to create download directory: " 
                           + downloadDir);
        printUsage(); 
    }

    //perform a recursive delete of the contents of a directory
    private boolean deleteDirectory(File file) 
    {
        //if the file is a directory, delete contents first
        if (file.isDirectory()) 
        {
            //get the files in the directory
            File[] children = file.listFiles();
            for (int i = 0; i < children.length; i++) 
            {
                //call deleteDirectory on each child
                boolean success = deleteDirectory(children[i]);
                if (!success) return false;
            }
        }
        //delete the file or empty directory
        return file.delete();
    }
}
