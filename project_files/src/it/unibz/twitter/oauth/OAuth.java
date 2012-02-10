/*     */ package it.unibz.twitter.oauth;
/*     */ 
/*     */ import it.unibz.twitter.model.Parameter;
/*     */ import it.unibz.twitter.model.Token;
/*     */ import it.unibz.twitter.utils.Utils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.security.SignatureException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class OAuth
/*     */ {
/*     */   private final String consumerSecret;
/*     */   private final String consumerKey;
/*     */   private final Utils utils;
/*  36 */   private final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
/*  37 */   private final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
/*  38 */   private final String AUTHORIZE_ENDPOINT = "http://api.twitter.com/oauth/authorize";
/*  39 */   private final String SIGN_METHOD = "HMAC-SHA1";
/*  40 */   private final String OAUTH_VERSION = "1.0";
/*     */   private URL authorizationURL;
/*     */ 
/*     */   public OAuth()
/*     */   {
/*  46 */     Keys k = new Keys();
/*  47 */     this.utils = new Utils(false);
/*  48 */     this.consumerKey = k.getConsumer_key();
/*  49 */     this.consumerSecret = k.getConsumer_secret();
/*     */   }
/*     */ 
/*     */   public Token getRequestToken()
/*     */     throws IOException, SignatureException
/*     */   {
/*  71 */     List params = new ArrayList();
/*  72 */     params.add(new Parameter("oauth_consumer_key", this.consumerKey));
/*  73 */     params.add(new Parameter("oauth_signature_method", "HMAC-SHA1"));
/*  74 */     params.add(new Parameter("oauth_timestamp", this.utils.getTimeStamp()));
/*  75 */     params.add(new Parameter("oauth_nonce", this.utils.getNonce()));
/*  76 */     params.add(new Parameter("oauth_version", "1.0"));
/*     */ 
/*  78 */     String petition = createPetition(true, "https://api.twitter.com/oauth/request_token", params);
/*     */ 
/*  80 */     this.utils.debug("Petition = " + petition);
/*  81 */     String sign = URLEncoder.encode(this.utils.calculateRFC2104HMAC(petition, this.consumerSecret + "&"), "UTF-8");
/*     */ 
/*  83 */     params.add(new Parameter("oauth_signature", sign));
/*     */ 
/*  86 */     StringBuilder response = doRequestToken(sign, params, "https://api.twitter.com/oauth/request_token");
/*  87 */     Token requestToken = this.utils.parseResponse(response.toString());
/*  88 */     setAuthorizationURL(requestToken);
/*  89 */     return requestToken;
/*     */   }
/*     */ 
/*     */   public Token getAccessToken(Token requestToken, String verifier)
/*     */     throws SignatureException, IOException
/*     */   {
/* 111 */     List params = new ArrayList();
/* 112 */     params.add(new Parameter("oauth_version", "1.0"));
/* 113 */     params.add(new Parameter("oauth_timestamp", this.utils.getTimeStamp()));
/* 114 */     params.add(new Parameter("oauth_signature_method", "HMAC-SHA1"));
/* 115 */     params.add(new Parameter("oauth_nonce", this.utils.getNonce()));
/* 116 */     params.add(new Parameter("oauth_consumer_key", this.consumerKey));
/* 117 */     params.add(new Parameter("oauth_token", requestToken.getToken()));
/* 118 */     params.add(new Parameter("oauth_verifier", verifier));
/*     */ 
/* 121 */     String petition = createPetition(true, "https://api.twitter.com/oauth/access_token", params);
/* 122 */     String sign = URLEncoder.encode(this.utils.calculateRFC2104HMAC(petition, this.consumerSecret + "&"), "UTF-8");
/*     */ 
/* 124 */     params.add(new Parameter("oauth_signature", sign));
/*     */ 
/* 126 */     StringBuilder response = doRequestToken(sign, params, "https://api.twitter.com/oauth/access_token");
/* 127 */     return this.utils.parseResponse(response.toString());
/*     */   }
/*     */ 
/*     */   private void setAuthorizationURL(Token t)
/*     */     throws MalformedURLException
/*     */   {
/* 136 */     this.authorizationURL = new URL("http://api.twitter.com/oauth/authorize?oauth_token=" + t.getToken());
/*     */   }
/*     */ 
/*     */   public URL getAuthorizationURL()
/*     */   {
/* 141 */     return this.authorizationURL;
/*     */   }
/*     */ 
/*     */   private String createPetition(boolean post, String url, List<Parameter> params)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 150 */     String petition = post ? "POST" : "GET";
/* 151 */     StringBuilder sb = new StringBuilder();
/* 152 */     sb.append(petition);
/* 153 */     sb.append("&");
/* 154 */     sb.append(URLEncoder.encode(url, "UTF-8"));
/* 155 */     sb.append("&");
/* 156 */     Collections.sort(params);
/* 157 */     for (Parameter pa : params) {
/* 158 */       sb.append(URLEncoder.encode(pa.key, "UTF-8"));
/* 159 */       sb.append("%3D");
/* 160 */       sb.append(URLEncoder.encode(pa.value, "UTF-8"));
/* 161 */       sb.append("%26");
/*     */     }
/*     */ 
/* 164 */     sb.delete(sb.length() - 3, sb.length());
/* 165 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private StringBuilder doRequestToken(String sign, List<Parameter> params, String urlReq) throws IOException
/*     */   {
/* 170 */     URL url = new URL(urlReq);
/* 171 */     HttpURLConnection conn = (HttpURLConnection)url.openConnection();
/*     */ 
/* 175 */     StringBuilder sb = new StringBuilder();
/* 176 */     sb.append("OAuth");
/* 177 */     for (Parameter pam : params) {
/* 178 */       sb.append(" " + pam.key + "=\"" + pam.value + "\",");
/*     */     }
/* 180 */     sb.delete(sb.length() - 1, sb.length());
/*     */ 
/* 182 */     String authHead = sb.toString();
/*     */ 
/* 184 */     conn.addRequestProperty("Authorization", authHead);
/* 185 */     conn.setRequestMethod("POST");
/*     */ 
/* 187 */     this.utils.debug(authHead);
/*     */ 
/* 189 */     conn.setDoOutput(true);
/* 190 */     OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
/* 191 */     wr.append("");
/* 192 */     wr.flush();
/*     */ 
/* 194 */     BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
/* 195 */     StringBuilder response = new StringBuilder();
/*     */     String line;
/* 197 */     while ((line = rd.readLine()) != null)
/*     */     {
/*     */       String line;
/* 198 */       response.append(line);
/*     */     }
/*     */     try {
/* 201 */       wr.close();
/* 202 */       rd.close();
/* 203 */       conn.disconnect();
/*     */     } catch (Exception e) {
/* 205 */       e.printStackTrace();
/*     */     }
/* 207 */     return response;
/*     */   }
/*     */ 
/*     */   private void authorizePetition(String urlString, List<Parameter> httpParameters, Token accessToken, HttpURLConnection conn, boolean usePost)
/*     */     throws UnsupportedEncodingException, SignatureException
/*     */   {
/* 215 */     String timestamp = this.utils.getTimeStamp();
/* 216 */     String nonce = this.utils.getNonce();
/*     */ 
/* 219 */     List parameters = new LinkedList();
/* 220 */     parameters.add(new Parameter("oauth_nonce", nonce));
/* 221 */     parameters.add(new Parameter("oauth_signature_method", "HMAC-SHA1"));
/* 222 */     parameters.add(new Parameter("oauth_timestamp", timestamp));
/* 223 */     parameters.add(new Parameter("oauth_consumer_key", this.consumerKey));
/* 224 */     parameters.add(new Parameter("oauth_token", accessToken.getToken()));
/* 225 */     parameters.add(new Parameter("oauth_version", "1.0"));
/* 226 */     Collections.sort(parameters);
/*     */ 
/* 228 */     List completeParameters = new LinkedList();
/* 229 */     completeParameters.addAll(parameters);
/*     */ 
/* 231 */     completeParameters.addAll(httpParameters);
/*     */ 
/* 233 */     String baseString = createPetition(usePost, urlString, completeParameters);
/* 234 */     String key = URLEncoder.encode(this.consumerSecret, "UTF-8") + "&" + URLEncoder.encode(accessToken.getSecret(), "UTF-8");
/* 235 */     String signature = this.utils.calculateRFC2104HMAC(baseString, key);
/*     */ 
/* 237 */     parameters.add(new Parameter("oauth_signature", URLEncoder.encode(signature, "UTF-8")));
/*     */ 
/* 242 */     StringBuilder sb = new StringBuilder();
/* 243 */     sb.append("OAuth");
/* 244 */     for (Parameter pam : parameters) {
/* 245 */       sb.append(" " + pam.key + "=\"" + pam.value + "\",");
/*     */     }
/* 247 */     sb.delete(sb.length() - 1, sb.length());
/*     */ 
/* 249 */     String authHead = sb.toString();
/*     */ 
/* 251 */     conn.setRequestProperty("Authorization", authHead);
/*     */   }
/*     */ 
/*     */   public String doPetition(String urlString, List<Parameter> httpParameters, Token accessToken, boolean usePost)
/*     */     throws UnsupportedEncodingException, SignatureException, MalformedURLException, IOException, ProtocolException
/*     */   {
/* 261 */     String params = this.utils.formatParameters(httpParameters);
/* 262 */     String completeUrl = urlString;
/*     */ 
/* 264 */     if ((!usePost) && (params.length() > 0)) {
/* 265 */       completeUrl = completeUrl + "?" + params;
/*     */     }
/* 267 */     URL url = new URL(completeUrl);
/*     */ 
/* 269 */     HttpURLConnection conn = (HttpURLConnection)url.openConnection();
/*     */ 
/* 271 */     if (accessToken != null) {
/* 272 */       authorizePetition(urlString, httpParameters, accessToken, conn, usePost);
/*     */     }
/*     */ 
/* 275 */     if (usePost) {
/* 276 */       conn.setRequestMethod("POST");
/* 277 */       conn.setDoOutput(true);
/* 278 */       DataOutputStream out = new DataOutputStream(conn.getOutputStream());
/* 279 */       out.writeBytes(params.toString());
/* 280 */       out.flush();
/* 281 */       out.close();
/*     */     } else {
/* 283 */       conn.setRequestMethod("GET");
/*     */     }
/*     */ 
/* 290 */     int respCode = conn.getResponseCode();
/*     */     InputStream r;
/*     */     InputStream r;
/* 291 */     if (respCode < 400) {
/* 292 */       r = conn.getInputStream();
/*     */     }
/*     */     else {
/* 295 */       r = conn.getErrorStream();
/*     */     }
/* 297 */     BufferedReader rd = new BufferedReader(new InputStreamReader(r));
/* 298 */     StringBuilder response = new StringBuilder();
/*     */     String line;
/* 300 */     while ((line = rd.readLine()) != null)
/*     */     {
/*     */       String line;
/* 301 */       response.append(line);
/*     */     }
/*     */     try {
/* 304 */       rd.close();
/* 305 */       conn.disconnect();
/*     */     } catch (Exception e) {
/* 307 */       e.printStackTrace();
/*     */     }
/* 309 */     return response.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.oauth.OAuth
 * JD-Core Version:    0.6.0
 */