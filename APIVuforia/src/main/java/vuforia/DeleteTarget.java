package vuforia;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;



public class DeleteTarget implements TargetStatusListener {

    //Server Keys
//    private String accessKey = "5a0691e8c5b4c5be3a99a8eb37eac44f7d74c55c";
//    private String secretKey = "c100a64eb8a6d6fdede98cb803402140f2243a93";

    // citigo
    private String accessKey = "9e2c9d5a4630b6a884d402b0b05769e61abd205e";
    private String secretKey = "3c7c3bcbc1b34f4b95afdccdc3c19f259b4a6cd5";

    private String targetId ;
    private String url = "https://vws.vuforia.com";

    private TargetStatusPoller targetStatusPoller;

    private final float pollingIntervalMinutes = 1;//poll at 1-hour interval

    public DeleteTarget(String targetId) {
        this.targetId = targetId;
    }



    private void deleteTarget() throws URISyntaxException, ClientProtocolException, IOException {
        HttpDelete deleteRequest = new HttpDelete();
        HttpClient client = new DefaultHttpClient();
        deleteRequest.setURI(new URI(url + "/targets/" + targetId));
        setHeaders(deleteRequest);

        HttpResponse response = client.execute(deleteRequest);
        System.out.println("Delete Response " + EntityUtils.toString(response.getEntity()));
    }

    private void setHeaders(HttpUriRequest request) {
        SignatureBuilder sb = new SignatureBuilder();
        request.setHeader(new BasicHeader("Date", DateUtils.formatDate(new Date()).replaceFirst("[+]00:00$", "")));
        request.setHeader("Authorization", "VWS " + accessKey + ":" + sb.tmsSignature(request, secretKey));
    }

    // sets the targets active_flag to the Boolean value of the argument
    private void updateTargetActivation(Boolean b) throws URISyntaxException, ClientProtocolException, IOException, JSONException {
        HttpPut putRequest = new HttpPut();
        HttpClient client = new DefaultHttpClient();
        putRequest.setURI(new URI(url + "/targets/" + targetId));

        JSONObject requestBody = new JSONObject();
        requestBody.put("active_flag", b );// add a JSON field for the active_flag

        putRequest.setEntity(new StringEntity(requestBody.toString()));
        // Set the Headers for this Put request
        // Must be done after setting the body
        SignatureBuilder sb = new SignatureBuilder();
        putRequest.setHeader(new BasicHeader("Date", DateUtils.formatDate(new Date()).replaceFirst("[+]00:00$", "")));
        putRequest.setHeader(new BasicHeader("Content-Type", "application/json"));
        putRequest.setHeader("Authorization", "VWS " + accessKey + ":" + sb.tmsSignature(putRequest, secretKey));

        HttpResponse response = client.execute(putRequest);
        System.out.println("Product Update Response "+EntityUtils.toString(response.getEntity()));
    }

    public void deactivateThenDeleteTarget() {
        // Update the target's active_flag to false and then Delete the target when the state change has been processed;
        try {
            updateTargetActivation( false );
        } catch ( URISyntaxException | IOException | JSONException e) {
            e.printStackTrace();
            return;
        }

        // Poll the target status until the active_flag is confirmed to be set to false
        // The TargetState will be passed to the OnTargetStatusUpdate callback
        targetStatusPoller = new TargetStatusPoller(pollingIntervalMinutes, targetId, accessKey, secretKey, this );
        targetStatusPoller.startPolling();
//        try {
//            System.out.println("Process delete ....wait....\n");
//            Thread.sleep(120000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    // Called with each update of the target status received by the TargetStatusPoller
    @Override
    public void OnTargetStatusUpdate(TargetState target_state) {
        if (target_state.hasState) {

            if (target_state.getActiveFlag() == false) {

                targetStatusPoller.stopPolling();

                try {
                    System.out.println(".. deleting target ..");

                    deleteTarget();
                    System.out.println("Delete done");

                } catch ( URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException {
        DeleteTarget d = new DeleteTarget("avd");
        d.deactivateThenDeleteTarget();
    }
}
