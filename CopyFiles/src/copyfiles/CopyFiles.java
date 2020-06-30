/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package copyfiles;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Naota
 */
public class CopyFiles {

    public static void main(String[] args) throws Exception {
        
//        String tv = destinationDirectoryName("C:\\");
//        System.out.println(tv);
//        
//        File[] newArray = null;
//        newArray = arrayOfFiles("C:\\Users\\Naota\\Downloads\\FreeTellyWin");
//        
//        copyFiles(newArray, "C:\\Users\\Naota\\Desktop\\Copy Test");
//        
//        System.out.println();

        String originalDirectory;           // The original directory location
        String destinationDirectoryName;    // The Folder's exact Name We will be creating
        String finalDestination;            // The directory the files will be copied to
        File[] filesToCopy = null;          // Array of files
        int counter = 0;                    //counter for the files that need to be copied 
        
        // ask user for original Directory Path to copy
        originalDirectory = findDirectory();
        
        // find the Folder Directory Name They Are Trying to Copy
        destinationDirectoryName = destinationDirectoryName(originalDirectory);
        
        // ask user for the Destination Directory to transfer the files to
        // and create the destination folder files will be copied to
        finalDestination = findDestination(destinationDirectoryName);
        
        //Create Array of Files to Copy
        filesToCopy = arrayOfFiles(originalDirectory);        
        
        //Start Copying Files
        createDirAndCopyFiles(filesToCopy, finalDestination, counter, originalDirectory);
        
        //Tells user transfer is complete
        transferComplete();
        
        
    }
    
    public static String findDirectory(){
        // this method asks the user to enter a directory they want to copy
        // if the directory is not found it will ask the user to enter
        // a different directory
        
        boolean notFound = true;    //if true will ask user to enter a directory
        String directory = null;    //directory user has entered
        
        while (notFound){
            
            //create a scanner for user input
            Scanner kb = new Scanner(System.in);
            
            //ask user for original directory path
            System.out.println("What Directory would you like to make a copy of?");
            System.out.print("Enter Directory Path: ");
            directory = kb.nextLine();
            System.out.println();
            
            //create File object for directory Entered and see if it exists or not
            File originalDirectory = new File(directory);
            
            if(originalDirectory.isDirectory()){
                notFound = false;
            }
            else{
                System.out.println("Directory Cannot Be Found. Please Try A Different Path." + ".\n");
                notFound = true;
            } // end if/else
        } // end while notFound
        
        return directory;
    } // end findDirectory()
    
    public static String destinationDirectoryName(String a){
        // used to find the Original Directory Name and gives a generic name if
        // there is no directory name for copy
        
        //stores the name of the directory
        String directory = a.substring(a.lastIndexOf("\\")+1);
        
        //if there is no name at the end of the path return generic name back_up
        //otherwise return the found directory name
        if(directory.matches("")){
            
            return "back_up";
        }
        else{
            
            return directory;
        }  // end if/else
    } // end destinationDirectoryName()
    
    public static String findDestination(String originalFolderName){
        // used to grab the user input for the name of the directory where they
        // would like to create the new copy of original folder and if the folder
        // cannot be created asks them to input another directory name
        
        boolean found = false;    // if true will ask user to enter a directory
        String newDirectory = null;    // new directory to be created       
        
        // ask user to enter input till a directory can be created
        while (!found){
            Scanner kb = new Scanner(System.in);

            System.out.println("What Directory would you like the Original Directory to be copied to?");
            System.out.print("Enter Destination Directory Path: ");
            String directory = kb.nextLine();
            System.out.println();
            
            // try to create new directory and if new directory cannot be created
            // ask user for new destination path
            newDirectory = directory + "\\" + originalFolderName;
            found = createNewDirectory(newDirectory);
            
            if(!found){
                System.out.println("Try a Different Path To Copy Files To." + ".\n");
            } // end if notFound
        } // end while notFound
        
        return newDirectory;
    } // end findDestination

    public static boolean createNewDirectory(String destination){
        // used to create new directory
        
        // create file object to be created
        File dir = new File(destination);
        
        // attempt to create new directory and return true if directory is created
        // return false if directory could not be created
        boolean successful = dir.mkdir();
        if(successful){
            
            System.out.println(".\n" + "Directory '" + destination + "' was successfully created" + ".\n");
            return true;
        }
        else{
            
            System.out.println("Failed to create the directory.");
            return false;
        } // end if/else
    } // end createNewDirectory()
    
    public static File[] arrayOfFiles(String originalDestination){
        //this method creates an array of files for the destination given

        File[] entries = null;      // an array for the entries in the target directory

        // Create a File class object linked to the target 
        File target = new File(originalDestination);
        
        // if folder exists create the array of the directory and return it
        if (target.exists()) {
            if (target.isDirectory()) {
                entries = target.listFiles();                    
            }
        }        
        else {
            System.out.println("The item you asked about - " + target + " - does not exist.");
                        
        } // end if/else
        return entries;
    } // end arrayOfFiles()
    
    public static void createDirAndCopyFiles(File[] fileArray, String newDestination, int i, String originalPath)throws Exception{
        // this method recursively creates an instance of a directory if a directory
        // is found or copies over the file if a found is found in the file array
        
        while (i < fileArray.length){
            
            // if the file in the array is a directory create the directory and then
            // call this method again to find all the files for the directory found
            if (fileArray[i].isDirectory()){
                
                // creates the new directory in the new destination
                createNewDirectory(newDestination + "\\" + destinationDirectoryName(fileArray[i].getPath()));
                
                // creates an array of the files in the found directory
                File[] foldersFiles = arrayOfFiles(fileArray[i].getPath());
                
                // recursively loops this method with the new info
                createDirAndCopyFiles(foldersFiles, newDestination + "\\" + destinationDirectoryName(fileArray[i].getPath()), 0, originalPath);
                
                // adds one to counter to try next file
                i++;
            } // end if
            
            // if the file is an actually file runs method to copy the file to
            // new destination
            if (i < fileArray.length){
                if (fileArray[i].isFile()){
                    
                    // copying file to new destination
                    copyFile(fileArray, i, newDestination);
                    
                    // adds one to counter to try next file
                    i++;
                } // end if
            } // end if
        } // end while loop
        
        //garbage
        fileArray = arrayOfFiles(originalPath);
    } //end createDirandCopyFiles()
    
    public static void copyFiles(File[] fileArray, String newDestination) throws Exception{
        
        // declare File 
        File sourceFile = null;
        File destFile = null;

        // declare stream variables
        FileInputStream sourceStream = null;
        FileOutputStream destStream = null;

        // declare buffering variables
        BufferedInputStream bufferedSource = null;
        BufferedOutputStream bufferedDestination = null;
        
        
        for (int i = 0; i < fileArray.length ; i++){ 
            
            try {
           
            // Create file objects for the source file
            sourceFile = new File(fileArray[i].getPath());
            
            // Gets the file name of the file being copied
            String newFileName = destinationDirectoryName(fileArray[i].getPath());
            
            // Create file object for the destination file
            destFile = new File(newDestination + "\\" + newFileName);

            // create file streams for the source and destination files
            sourceStream = new FileInputStream(sourceFile);
            destStream = new FileOutputStream(destFile);

            // buffer the file streams -- set the buffer sizes to 8K
            bufferedSource = new BufferedInputStream(sourceStream, 8182);
            bufferedDestination = new BufferedOutputStream(destStream, 6192);

            // use an integer to transfer data between files
            int transfer;

            // tell the user what is happening
            System.out.println("begining file copy:");
            System.out.println("\tcopying " + fileArray[i]);
            System.out.println("\tto      " + newDestination + "\\" + newFileName);

            // read a byte, checking for end of file (-1 is returned by read at EOF)
            while ((transfer = bufferedSource.read()) != -1) {

                // write a byte 
                bufferedDestination.write(transfer);

            } // end while
        
            } catch (IOException e) {

                e.printStackTrace();
                System.out.println(" An unexpected I/O error occurred.");

            }

            finally {

                // close file streams 
               if (bufferedSource != null) 
                   bufferedSource.close();

               if (bufferedDestination != null) 
                bufferedDestination.close();

               System.out.println("Files closed. Copy complete.");

            } // end finally
        } // end for loop
    } // end copyFiles()
    
    public static void copyFile(File[] fileArray, int fileNumber, String newDestination) throws Exception{
        //clone of copyFiles method and modified to only copy one file at a time
        
        // declare File 
        File sourceFile = null;
        File destFile = null;

        // declare stream variables
        FileInputStream sourceStream = null;
        FileOutputStream destStream = null;

        // declare buffering variables
        BufferedInputStream bufferedSource = null;
        BufferedOutputStream bufferedDestination = null;
        
        try {

        // Create file objects for the source file
        sourceFile = new File(fileArray[fileNumber].getPath());

        // Gets the file name of the file being copied
        String newFileName = destinationDirectoryName(fileArray[fileNumber].getPath());

        // Create file object for the destination file
        destFile = new File(newDestination + "\\" + newFileName);

        // create file streams for the source and destination files
        sourceStream = new FileInputStream(sourceFile);
        destStream = new FileOutputStream(destFile);

        // buffer the file streams -- set the buffer sizes to 8K
        bufferedSource = new BufferedInputStream(sourceStream, 8182);
        bufferedDestination = new BufferedOutputStream(destStream, 6192);

        // use an integer to transfer data between files
        int transfer;

        // tell the user what is happening
        System.out.println("begining file copy:");
        System.out.println("\tcopying " + fileArray[fileNumber]);
        System.out.println("\tto      " + newDestination + "\\" + newFileName);

        // read a byte, checking for end of file (-1 is returned by read at EOF)
        while ((transfer = bufferedSource.read()) != -1) {

            // write a byte 
            bufferedDestination.write(transfer);

        } // end while

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println(" An unexpected I/O error occurred.");

        }

        finally {

            // close file streams 
           if (bufferedSource != null) 
               bufferedSource.close();

           if (bufferedDestination != null) 
            bufferedDestination.close();

           System.out.println("Files closed. Copy complete.");

        } // end finally
    } // end copyFile()
    
    public static void transferComplete(){
        // tells user the file transfer has finished
        
        System.out.println(".\n" + "File Copying Has Been Completed!");
    } // end transferComplete()
    
}
    

