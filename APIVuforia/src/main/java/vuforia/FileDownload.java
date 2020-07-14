package vuforia;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
//import org.asynchttpclient.*;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutionException;

public class FileDownload {

    public static void downloadWithJavaIO(String url, String localFilename) {

        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream()); FileOutputStream fileOutputStream = new FileOutputStream(localFilename)) {

            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadWithJava7IO(String url, String localFilename) {

        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(localFilename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadWithJavaNIO(String fileURL, String localFilename) throws IOException {

        URL url = new URL(fileURL);
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(localFilename); FileChannel fileChannel = fileOutputStream.getChannel()) {

            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
        }
    }

    public static void downloadWithApacheCommons(String url, String localFilename) {

        int CONNECT_TIMEOUT = 10000;
        int READ_TIMEOUT = 10000;
        try {
            FileUtils.copyURLToFile(new URL(url), new File(localFilename), CONNECT_TIMEOUT, READ_TIMEOUT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void downloadWithAHC(String url, String localFilename) throws ExecutionException, InterruptedException, IOException {

//        FileOutputStream stream = new FileOutputStream(localFilename);
//        AsyncHttpClient client = Dsl.asyncHttpClient();
//
//        client.prepareGet(url)
//                .execute(new AsyncCompletionHandler<FileOutputStream>() {
//
//                    @Override
//                    public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
//                        stream.getChannel()
//                                .write(bodyPart.getBodyByteBuffer());
//                        return State.CONTINUE;
//                    }
//
//                    @Override
//                    public FileOutputStream onCompleted(Response response) throws Exception {
//                        return stream;
//                    }
//                })
//                .get();
//
//        stream.getChannel().close();
//        client.close();
    }

    public static void main(String[] args) {
        String local = "E:\\Citigo\\Product\\";
        String csvFile = "E:\\Citigo\\TopProduct.csv";
        try (Reader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            int i = 0;
            for (CSVRecord csvRecord : csvRecords) {
                if(i == 0){
                    i++;
                }else {
                    String url = csvRecord.get(6);
                    String name = csvRecord.get(0);
                    String fileName;
//                    if(url.endsWith("jpg")){
//                        fileName = local + name + ".jpg";
//                        downloadWithApacheCommons(url,fileName);
//                    }else if(url.endsWith("jpeg")){
//                        fileName = local + name + ".jpeg";
//                        downloadWithApacheCommons(url,fileName);
//                    }else if(url.endsWith("png")){
//                        fileName = local + name + ".png";
//                        downloadWithApacheCommons(url,fileName);
//                    }else{
//                        fileName = local + name + ".jpg";
//                        downloadWithApacheCommons(url,fileName);
//                    }
                    fileName = local + name + ".jpg";
                    downloadWithApacheCommons(url,fileName);
                    i++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}