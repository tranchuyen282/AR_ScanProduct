package ghn;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GetProvince {
    public void getAllProvince()throws URISyntaxException, ClientProtocolException, IOException, JSONException {
        HttpPost postRequest = new HttpPost();
        HttpClient client = new DefaultHttpClient();
        postRequest.setURI(new URI(Utils.URL_GET_PROVINCE));
        JSONObject requestBody = new JSONObject();

        //setRequestBody(requestBody);

       // postRequest.setEntity(new StringEntity(requestBody.toString()));
        setHeaders(postRequest); // Must be done after setting the body

        HttpResponse response = client.execute(postRequest);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println("responseBody: \n" + responseBody);

        JSONObject jobj = new JSONObject(responseBody);
        System.out.println(jobj.getJSONArray("data"));



    }
    private void setRequestBody(JSONObject requestBody) throws IOException, JSONException {

    }
    private void setHeaders(HttpUriRequest request) {
        request.setHeader(new BasicHeader("Content-Type", "application/json"));
        request.setHeader(new BasicHeader("token", Utils.TOKEN));

    }

    public static void main(String[] args) throws JSONException, IOException, URISyntaxException {
        GetProvince getProvince = new GetProvince();
        getProvince.getAllProvince();
    }
}
