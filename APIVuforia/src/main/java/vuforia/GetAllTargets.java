package vuforia;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;




//See the Vuforia Web Services Developer API Specification - https://developer.vuforia.com/resources/dev-guide/listing-targets-cloud-database

public class GetAllTargets {

    //Server Keys
//    private String accessKey = "5a0691e8c5b4c5be3a99a8eb37eac44f7d74c55c";
//    private String secretKey = "c100a64eb8a6d6fdede98cb803402140f2243a93";

    // citigo
    private String accessKey = "9e2c9d5a4630b6a884d402b0b05769e61abd205e";
    private String secretKey = "3c7c3bcbc1b34f4b95afdccdc3c19f259b4a6cd5";

    private String url = "https://vws.vuforia.com";

    private void getTargets() throws URISyntaxException, ClientProtocolException, IOException {
        HttpGet getRequest = new HttpGet();
        HttpClient client = new DefaultHttpClient();
        getRequest.setURI(new URI(url + "/targets"));
        setHeaders(getRequest);

        HttpResponse response = client.execute(getRequest);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    private void setHeaders(HttpUriRequest request) {
        SignatureBuilder sb = new SignatureBuilder();
        request.setHeader(new BasicHeader("Date", DateUtils.formatDate(new Date()).replaceFirst("[+]00:00$", "")));
        request.setHeader("Authorization", "VWS " + accessKey + ":" + sb.tmsSignature(request, secretKey));
    }

    public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException {
        GetAllTargets g = new GetAllTargets();
        g.getTargets();
    }
}
