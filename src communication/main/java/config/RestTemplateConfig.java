//package config;
//
//import org.apache.http.client.HttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class RestTemplateConfig {
//    @Bean
//    public RestTemplate restTemplate() {
//        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
//        poolingConnManager.setMaxTotal(100); // Set the max connections per route
//        poolingConnManager.setDefaultMaxPerRoute(20); // Set the max connections per route
//
//        // Create the HttpClient with the custom connection manager
//        HttpClient httpClient = HttpClients.custom()
//                .setConnectionManager(poolingConnManager)
//                .build();
//
//        // Use HttpComponentsClientHttpRequestFactory with the custom HttpClient
//        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
//    }
//}
