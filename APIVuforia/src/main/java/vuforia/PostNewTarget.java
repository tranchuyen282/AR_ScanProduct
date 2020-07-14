package vuforia;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


// See the Vuforia Web Services Developer API Specification - https://developer.vuforia.com/resources/dev-guide/adding-target-cloud-database-api

public class PostNewTarget implements TargetStatusListener {

    //Server Keys
//    private String accessKey = "5a0691e8c5b4c5be3a99a8eb37eac44f7d74c55c";
//    private String secretKey = "c100a64eb8a6d6fdede98cb803402140f2243a93";

    // server keys: citigo
    private String accessKey = "9e2c9d5a4630b6a884d402b0b05769e61abd205e";
    private String secretKey = "3c7c3bcbc1b34f4b95afdccdc3c19f259b4a6cd5";

    private String url = "https://vws.vuforia.com";
    private String targetName ;
    private String metaData;
    private String imageLocation ;
    private String targetId;
//    private Product product;

    private TargetStatusPoller targetStatusPoller;

    private final float pollingIntervalMinutes =2;//poll at 1-m interval

    public PostNewTarget() {
    }


    public PostNewTarget(String targetName, String metaData, String imageLocation) {
        this.targetName = targetName;
        this.metaData = metaData;
        this.imageLocation = imageLocation;
    }

    private String postTarget() throws URISyntaxException, ClientProtocolException, IOException, JSONException {
        HttpPost postRequest = new HttpPost();
        HttpClient client = new DefaultHttpClient();
        postRequest.setURI(new URI(url + "/targets"));
        JSONObject requestBody = new JSONObject();

        setRequestBody(requestBody);
        postRequest.setEntity(new StringEntity(requestBody.toString()));
        setHeaders(postRequest); // Must be done after setting the body

        HttpResponse response = client.execute(postRequest);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println("responseBody: \n" + responseBody);

        JSONObject jobj = new JSONObject(responseBody);

        String uniqueTargetId = jobj.has("target_id") ? jobj.getString("target_id") : "";
        if (uniqueTargetId != null && !uniqueTargetId.isEmpty()) {
            System.out.println("Created target with id: " + uniqueTargetId );
        }else{
            System.out.println("PRODUCT " + this.targetName + " FAIL");
        }
        this.targetId = uniqueTargetId;
        return uniqueTargetId;
    }

    public static String getBase64EncodedImage(String imageURL) throws IOException {
        java.net.URL url = new java.net.URL(imageURL);
        InputStream is = url.openStream();
        byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
        return Base64.encodeBase64String(bytes);
    }

    private void setRequestBody(JSONObject requestBody) throws IOException, JSONException {
        File imageFile = new File(imageLocation);
        if(!imageFile.exists()) {
            System.out.println("File location does not exist!");
            System.exit(1);
        }
        byte[] image = FileUtils.readFileToByteArray(imageFile);
        requestBody.put("name", targetName); // Mandatory
        requestBody.put("width", 320.0); // Mandatory
        requestBody.put("image", Base64.encodeBase64String(image)); // Mandatory
        requestBody.put("active_flag", 1); // Optional
        requestBody.put("application_metadata", Base64.encodeBase64String(metaData.getBytes())); // Optional
    }

    private void setHeaders(HttpUriRequest request) {
        SignatureBuilder sb = new SignatureBuilder();
        request.setHeader(new BasicHeader("Date", DateUtils.formatDate(new Date()).replaceFirst("[+]00:00$", "")));
        request.setHeader(new BasicHeader("Content-Type", "application/json"));
        request.setHeader("Authorization", "VWS " + accessKey + ":" + sb.tmsSignature(request, secretKey));
    }

    /**
     * Posts a new target to the Cloud database;
     * then starts a periodic polling until 'status' of created target is reported as 'success'.
     */
    public void postTargetThenPollStatus() {
        String createdTargetId = "";
        try {
            createdTargetId = postTarget();
        } catch (URISyntaxException | IOException | JSONException e) {
            e.printStackTrace();
            return;
        }
        // Poll the target status until the 'status' is 'success'
        // The TargetState will be passed to the OnTargetStatusUpdate callback
        if (createdTargetId != null && !createdTargetId.isEmpty()) {
            targetStatusPoller = new TargetStatusPoller(pollingIntervalMinutes, createdTargetId, accessKey, secretKey, this );
            targetStatusPoller.startPolling();
        }else{
            return;
        }
    }

    // Called with each update of the target status received by the TargetStatusPoller
    @Override
    public void OnTargetStatusUpdate(TargetState target_state) {
        if (target_state.hasState) {

            String status = target_state.getStatus();

            System.out.println("Target status is: " + (status != null ? status : "unknown"));

            if (target_state.getActiveFlag() == true && "success".equalsIgnoreCase(status)) {

                targetStatusPoller.stopPolling();

                System.out.println("Target is now in 'success' status");
            }
        }
//        if (target_state.hasState) {
//
//            String status = target_state.getStatus();
//
//            System.out.println("Target status is: " + (status != null ? status : "unknown"));
//
//            if (target_state.getActiveFlag() == true && "success".equalsIgnoreCase(status)) {
//
//                targetStatusPoller.stopPolling();
//                System.out.println("Target "+targetName +" is now in 'success' status \n Next: check Rating");
//                try {
//                    GetTarget g = new GetTarget(targetId);
//                    int rating = g.getTarget();
//                    System.out.println("Rating " + targetName + " = " +rating);
//                    if(rating >= 3){
//                        System.out.println("Target: " + targetName + " OK -> Save");
//                    }else if (rating >= 0){
//                        System.out.println("delete target " + targetName);
//                        DeleteTarget deleteTarget = new DeleteTarget(targetId);
//                        deleteTarget.deactivateThenDeleteTarget();
//                    }else{
//                        System.out.println("No target in DB");
//                    }
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
    }

    public void writeOutput(Product p){
        String csvFile = "E:\\Android\\Unity\\output.csv";

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


    public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException, JSONException {
        String path = "E:\\AIN\\Citigo\\output.txt";
        File file = new File(path);
        try {
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = "";
            while((line = reader.readLine()) != null){
                String imageLocation = line;
                String str = line.split("\\\\")[8];
                String targetName = str.split(".jpg")[0];
                String metaData = targetName.split("_")[0] + ".json";
                PostNewTarget p = new PostNewTarget(targetName,metaData,imageLocation);
                p.postTargetThenPollStatus();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }





//        try (Reader reader = new FileReader(csvFile);
//             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);) {
//            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
//            PostNewTarget p;
//            int i = 0;
//            for (CSVRecord csvRecord : csvRecords) {
//                if(i == 0){
//                    i++;
//                }else {
//                    Product product = new Product(Integer.toString(i), csvRecord.get(1), csvRecord.get(2), csvRecord.get(3), csvRecord.get(4), csvRecord.get(5), csvRecord.get(6));
//                    System.out.println("\n"+ i + ".Product: " + product.getName());
//                    p = new PostNewTarget(product);
//                    p.postTargetThenPollStatus();
//                    i++;
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
