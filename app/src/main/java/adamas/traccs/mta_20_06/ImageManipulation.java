package adamas.traccs.mta_20_06;


import org.kobjects.base64.Base64;

import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;


/**
 * @desc Image manipulation - Conversion
 *
 * @filename ImageManipulation.java
 * @author Jeevanandam M. (jeeva@myjeeva.com)
 * @copyright myjeeva.com
 */
public class ImageManipulation {

    public   ImageManipulation(String PicPath) {

        File file = new File(PicPath);

        try {
            // Reading a Image file from file system
            FileInputStream imageInFile = new FileInputStream(file);
            byte[] imageData = new byte[(int) file.length()];
            imageInFile.read(imageData);

            // Converting Image byte array into Base64 String
            String imageDataString = encodeImage(imageData);

            // Converting a Base64 String into Image byte array
            byte[] imageByteArray = decodeImage(imageDataString);

            // Write a image byte array into file system
            FileOutputStream imageOutFile = new FileOutputStream(
                    PicPath);

            imageOutFile.write(imageByteArray);

            imageInFile.close();
            imageOutFile.close();

            System.out.println("Image Successfully Manipulated!");
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
    }

    /**
     * Encodes the byte array into base64 string
     *
     * @param imageByteArray - byte array
     * @return String a {@link java.lang.String}
     */
    public  String encodeImage(byte[] imageByteArray) {
        return Base64.encode(imageByteArray);
    }

    /**
     * Decodes the base64 string into byte array
     *
     * @param imageDataString - a {@link java.lang.String}
     * @return byte array
     */
    public  byte[] decodeImage(String imageDataString) {
        return Base64.decode(imageDataString);
    }
}