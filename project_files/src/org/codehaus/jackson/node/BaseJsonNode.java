/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonParser.NumberType;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.JsonSerializableWithType;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ 
/*     */ public abstract class BaseJsonNode extends JsonNode
/*     */   implements JsonSerializableWithType
/*     */ {
/*     */   public JsonNode findValue(String fieldName)
/*     */   {
/*  33 */     return null;
/*     */   }
/*     */ 
/*     */   public final JsonNode findPath(String fieldName)
/*     */   {
/*  39 */     JsonNode value = findValue(fieldName);
/*  40 */     if (value == null) {
/*  41 */       return MissingNode.getInstance();
/*     */     }
/*  43 */     return value;
/*     */   }
/*     */ 
/*     */   public ObjectNode findParent(String fieldName)
/*     */   {
/*  49 */     return null;
/*     */   }
/*     */ 
/*     */   public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/*  54 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */   public List<String> findValuesAsText(String fieldName, List<String> foundSoFar)
/*     */   {
/*  59 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */   public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/*  64 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */   public JsonParser traverse()
/*     */   {
/*  75 */     return new TreeTraversingParser(this);
/*     */   }
/*     */ 
/*     */   public abstract JsonToken asToken();
/*     */ 
/*     */   public JsonParser.NumberType getNumberType()
/*     */   {
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */   public abstract void serialize(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */     throws IOException, JsonProcessingException;
/*     */ 
/*     */   public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 120 */     serialize(jgen, provider);
/*     */   }
/*     */ 
/*     */   public final void writeTo(JsonGenerator jgen)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 141 */     serialize(jgen, null);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.BaseJsonNode
 * JD-Core Version:    0.6.0
 */