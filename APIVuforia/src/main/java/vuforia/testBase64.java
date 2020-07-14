package vuforia;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;

public class testBase64 {
    public static String getBase64EncodedImage(String imageURL) throws IOException {
        java.net.URL url = new java.net.URL(imageURL);
        InputStream is = url.openStream();
        byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
        return Base64.encodeBase64String(bytes);
    }
    public void writeOutput(Product p){
        String csvFile = "E:\\Android\\Unity\\output.csv";
        System.out.println("save " + p.getName());
        try {
            FileWriter fw = new FileWriter(csvFile, true);
            BufferedWriter writer = new BufferedWriter(fw);
            try (CSVPrinter csvPrinter = new CSVPrinter(writer,
                    CSVFormat.DEFAULT);) {
                csvPrinter.printRecord(p.getStt(),p.getMaster_code(),p.getName(),p.getRetailer_key(),p.getQuantity(),p.getPrice(),p.getImage());
                csvPrinter.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done save product "+ p.getName());
    }


    public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());




    }
}
