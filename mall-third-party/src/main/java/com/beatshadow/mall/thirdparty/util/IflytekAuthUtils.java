package com.beatshadow.mall.thirdparty.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 语音能力平台接口签名协议
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/12/15 11:16
 */
public class IflytekAuthUtils {
    /**
     * 生成用于鉴权的URL,websocket 接口
     * @param requestUrl
     * @param apiKey
     * @param apiSecret
     * @return final requestUrl
     */
    public static String assembleRequestUrl(String requestUrl, String apiKey, String apiSecret) {
        URL url;
        String  httpRequestUrl = requestUrl.replace("ws://", "http://").replace("wss://","https://" );
        try {
            url = new URL(httpRequestUrl);
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = format.format(new Date());
            String host = url.getHost();
            StringBuilder builder = new StringBuilder("host: ").append(host).append("\n").
                    append("date: ").append(date).append("\n").
                    append("GET ").append(url.getPath()).append(" HTTP/1.1");
            Charset charset = StandardCharsets.UTF_8;
            Mac mac = Mac.getInstance("hmacsha256");
            System.out.println(builder.toString());
            SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
            mac.init(spec);
            byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
            String sha = Base64.getEncoder().encodeToString(hexDigits);
            String authorization = String.format("hmac api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
            String authBase = Base64.getEncoder().encodeToString(authorization.getBytes(charset));
            return String.format("%s?authorization=%s&host=%s&date=%s", requestUrl, URLEncoder.encode(authBase), URLEncoder.encode(host), URLEncoder.encode(date));

        } catch (Exception e) {
            throw new RuntimeException("assemble requestUrl error:"+e.getMessage());
        }
    }
}
