import com.google.common.base.Strings;
import okhttp3.*;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.http.HttpClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SeleniumClientFactory implements HttpClient.Factory {

    public static HttpCommandExecutor createExecutor(URL url, String proxyHost, int proxyPort, String proxyUsername, String proxyPassword) {
        return new HttpCommandExecutor(new HashMap<String, CommandInfo>(), url, new SeleniumClientFactory(proxyHost, proxyPort, proxyUsername, proxyPassword));
    }


    private String proxyHost;
    private int proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    public SeleniumClientFactory(String proxyHost, int proxyPort, String proxyUsername, String proxyPassword){
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
    }
    private final ConnectionPool pool = new ConnectionPool();

    public HttpClient.Builder builder() {
        return new org.openqa.selenium.remote.http.HttpClient.Builder() {
            public HttpClient createClient(URL url) {
                // configure the proxy
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                
                // configure the credentials and register in the builder
                Authenticator authenticator = null;
                if(proxyUsername != null && proxyPassword != null) {
                    String credential = Credentials.basic(proxyUsername, proxyPassword);
                    authenticator = new Authenticator() {
                        public Request authenticate(Route route, Response response) throws IOException {
                            return response.request().newBuilder()
                                    .header("Proxy-Authorization", credential)
                                    .build();
                        }
                    };
                }
                OkHttpClient.Builder client = (new OkHttpClient.Builder())
                        .connectionPool(SeleniumClientFactory.this.pool)
                        .followRedirects(true)
                        .followSslRedirects(true)
                        .proxy(this.proxy)
                        .readTimeout(this.readTimeout.toMillis(), TimeUnit.MILLISECONDS)
                        .connectTimeout(this.connectionTimeout.toMillis(), TimeUnit.MILLISECONDS).
                        .proxyAuthenticator(authenticator);
             
                return new org.openqa.selenium.remote.internal.OkHttpClient(client.build(), url);
            }
        };
    }

    public void cleanupIdleClients() {
        this.pool.evictAll();
    }
}
