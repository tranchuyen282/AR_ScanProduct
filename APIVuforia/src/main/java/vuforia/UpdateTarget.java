package vuforia;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;



//See the Vuforia Web Services Developer API Specification - https://developer.vuforia.com/resources/dev-guide/updating-target-cloud-database

public class UpdateTarget {

    //Server Keys
//    private String accessKey = "5a0691e8c5b4c5be3a99a8eb37eac44f7d74c55c";
//    private String secretKey = "c100a64eb8a6d6fdede98cb803402140f2243a93";

    // server keys: citigo
    private String accessKey = "9e2c9d5a4630b6a884d402b0b05769e61abd205e";
    private String secretKey = "3c7c3bcbc1b34f4b95afdccdc3c19f259b4a6cd5";

    private String targetId = "[ target id ]";
    private String url = "https://vws.vuforia.com";

    private void updateTarget() throws URISyntaxException, ClientProtocolException, IOException, JSONException {
        HttpPut putRequest = new HttpPut();
        HttpClient client = new DefaultHttpClient();
        putRequest.setURI(new URI(url + "/targets/" + targetId));
        JSONObject requestBody = new JSONObject();

        setRequestBody(requestBody);
        putRequest.setEntity(new StringEntity(requestBody.toString()));
        setHeaders(putRequest); // Must be done after setting the body

        HttpResponse response = client.execute(putRequest);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    private void setRequestBody(JSONObject requestBody) throws IOException, JSONException {
        //requestBody.put("active_flag", true); // Optional
        requestBody.put("application_metadata", Base64.encodeBase64String("Vuforia test metadata".getBytes())); // Optional
    }

    private void setHeaders(HttpUriRequest request) {
        SignatureBuilder sb = new SignatureBuilder();
        request.setHeader(new BasicHeader("Date", DateUtils.formatDate(new Date()).replaceFirst("[+]00:00$", "")));
        request.setHeader(new BasicHeader("Content-Type", "application/json"));
        request.setHeader("Authorization", "VWS " + accessKey + ":" + sb.tmsSignature(request, secretKey));
    }

    public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException, JSONException {
        UpdateTarget u = new UpdateTarget();
        u.updateTarget();
    }
}
