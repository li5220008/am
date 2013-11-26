
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
/**
 * Created with IntelliJ IDEA.
 * User: free
 * Date: 13-11-26
 * Time: 下午10:48
 * To change this template use File | Settings | File Templates.
 */
public class HttpClientService {
    //cookie保持策略，这里用默认的。
    private static final BasicCookieStore cookieStore = new BasicCookieStore();
    private static final CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    //单例
    static HttpClientService instance = null;
    //网关
    public String loginUri;
    //登陆的参数
    public List <NameValuePair> nvps;
    //提交的编码方式
    public String encoded;
    private HttpClientService(){}
    public static HttpClientService getInstance(){
        if(instance ==null){
            instance = new HttpClientService();
        }
        return instance;
    }

    public String doGet(){
        HttpGet httpget = new HttpGet("https://portal.sun.com/portal/dt");
        return null;
    }

    public String doPost(){
        return null;
    }

    public static void main(String[] args) throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        try {
            HttpGet httpget = new HttpGet("https://portal.sun.com/portal/dt");

            CloseableHttpResponse response1 = httpclient.execute(httpget);
            try {
                HttpEntity entity = response1.getEntity();

                System.out.println("Login form get: " + response1.getStatusLine());
                EntityUtils.consume(entity);

                System.out.println("Initial set of cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response1.close();
            }

            HttpPost httpost = new HttpPost("https://portal.sun.com/amserver/UI/Login?" +
                    "org=self_registered_users&" +
                    "goto=/portal/dt&" +
                    "gotoOnFail=/portal/dt?error=true");
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("IDToken1", "username"));
            nvps.add(new BasicNameValuePair("IDToken2", "password"));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            CloseableHttpResponse response2 = httpclient.execute(httpost);
            try {
                HttpEntity entity = response2.getEntity();

                System.out.println("Login form get: " + response2.getStatusLine());
                EntityUtils.consume(entity);

                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response2.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
