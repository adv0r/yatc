/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.net.URL;
/*     */ import org.codehaus.jackson.JsonFactory;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.node.JsonNodeFactory;
/*     */ import org.codehaus.jackson.node.NullNode;
/*     */ 
/*     */ @Deprecated
/*     */ public class TreeMapper extends JsonNodeFactory
/*     */ {
/*     */   protected ObjectMapper _objectMapper;
/*     */ 
/*     */   public TreeMapper()
/*     */   {
/*  43 */     this(null);
/*     */   }
/*     */ 
/*     */   public TreeMapper(ObjectMapper m)
/*     */   {
/*  48 */     this._objectMapper = m;
/*     */   }
/*     */ 
/*     */   public JsonFactory getJsonFactory()
/*     */   {
/*  58 */     return objectMapper().getJsonFactory();
/*     */   }
/*     */ 
/*     */   public JsonNode readTree(JsonParser jp)
/*     */     throws IOException, JsonParseException
/*     */   {
/*  88 */     JsonToken t = jp.getCurrentToken();
/*  89 */     if (t == null) {
/*  90 */       t = jp.nextToken();
/*  91 */       if (t == null) {
/*  92 */         return null;
/*     */       }
/*     */     }
/*     */ 
/*  96 */     return objectMapper().readTree(jp);
/*     */   }
/*     */ 
/*     */   public JsonNode readTree(File src)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 103 */     JsonNode n = (JsonNode)objectMapper().readValue(src, JsonNode.class);
/* 104 */     return n == null ? NullNode.instance : n;
/*     */   }
/*     */ 
/*     */   public JsonNode readTree(URL src)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 110 */     JsonNode n = (JsonNode)objectMapper().readValue(src, JsonNode.class);
/* 111 */     return n == null ? NullNode.instance : n;
/*     */   }
/*     */ 
/*     */   public JsonNode readTree(InputStream src)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 117 */     JsonNode n = (JsonNode)objectMapper().readValue(src, JsonNode.class);
/* 118 */     return n == null ? NullNode.instance : n;
/*     */   }
/*     */ 
/*     */   public JsonNode readTree(Reader src)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 124 */     JsonNode n = (JsonNode)objectMapper().readValue(src, JsonNode.class);
/* 125 */     return n == null ? NullNode.instance : n;
/*     */   }
/*     */ 
/*     */   public JsonNode readTree(String jsonContent)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 131 */     JsonNode n = (JsonNode)objectMapper().readValue(jsonContent, JsonNode.class);
/* 132 */     return n == null ? NullNode.instance : n;
/*     */   }
/*     */ 
/*     */   public JsonNode readTree(byte[] jsonContent)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 138 */     JsonNode n = (JsonNode)objectMapper().readValue(jsonContent, 0, jsonContent.length, JsonNode.class);
/* 139 */     return n == null ? NullNode.instance : n;
/*     */   }
/*     */ 
/*     */   public void writeTree(JsonNode rootNode, File dst)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 152 */     objectMapper().writeValue(dst, rootNode);
/*     */   }
/*     */ 
/*     */   public void writeTree(JsonNode rootNode, Writer dst)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 158 */     objectMapper().writeValue(dst, rootNode);
/*     */   }
/*     */ 
/*     */   public void writeTree(JsonNode rootNode, OutputStream dst)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 164 */     objectMapper().writeValue(dst, rootNode);
/*     */   }
/*     */ 
/*     */   protected synchronized ObjectMapper objectMapper()
/*     */   {
/* 175 */     if (this._objectMapper == null) {
/* 176 */       this._objectMapper = new ObjectMapper();
/*     */     }
/* 178 */     return this._objectMapper;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.TreeMapper
 * JD-Core Version:    0.6.0
 */