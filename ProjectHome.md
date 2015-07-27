# CRAM tool #

## Google Group ##
https://groups.google.com/d/forum/cram-tool

## Google Analytics ##
is in the preview-application.html web page, the build script converts it into index.html in the dist directory.

Shortcut--http://www.google.com/analytics/

The tracker id is UA-51814227-1

## User Guide ##
The master copy of the user guide is a Word document in Dropbox, named 'User Guide to CRAM tool v0.5.docx'. A PDF of the guide should be copied to CRAM-UI/src/uk/ac/lkl/cram/ui/resource/CRAMUserGuide.pdf **BEFORE** creating a package. This is copied to the dist directory, by the '-post-jar' target of the ant build script.

## Packaging ##
Relies on the 'package' ant target of the build.xml script in the CRAM-UI netbeans project.

### Packaging for Mac ###
is already included in the project.

### Packaging for Windows ###
relies on [launch4j](http://launch4j.sourceforge.net/). Edit the properties file 'launch4j.properties' (at the root of the netbeans project): change the value of the property 'launch4j.dir' and uncomment. The location should be the directory containing the launch4j files.

## Files to copy after packaging ##
Copy the following files from the 'dist' directory to the directory that corresponds to http://web.lkldev.ioe.ac.uk/cram/ (i.e. /var/www/cram)
  * CRAM Tool.exe
  * CRAM Tool.jar
  * CRAM Tool.zip <--this is the Mac application zipped for easier download
  * CRAMUserGuide.pdf
  * images/
  * css/
  * videos.html
  * index.html
  * Sample\_CRAM\_Modules.zip <--this zipped file contains the sample modules from the CRAM directory of Dropbox