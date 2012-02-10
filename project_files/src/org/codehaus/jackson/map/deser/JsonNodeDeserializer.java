/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.node.ArrayNode;
/*     */ import org.codehaus.jackson.node.ObjectNode;
/*     */ 
/*     */ public class JsonNodeDeserializer extends BaseNodeDeserializer<JsonNode>
/*     */ {
/*     */ 
/*     */   @Deprecated
/*  22 */   public static final JsonNodeDeserializer instance = new JsonNodeDeserializer();
/*     */ 
/*  24 */   protected JsonNodeDeserializer() { super(JsonNode.class);
/*     */   }
/*     */ 
/*     */   public static JsonDeserializer<? extends JsonNode> getDeserializer(Class<?> nodeClass)
/*     */   {
/*  31 */     if (nodeClass == ObjectNode.class) {
/*  32 */       return ObjectDeserializer.getInstance();
/*     */     }
/*  34 */     if (nodeClass == ArrayNode.class) {
/*  35 */       return ArrayDeserializer.getInstance();
/*     */     }
/*     */ 
/*  38 */     return instance;
/*     */   }
/*     */ 
/*     */   public JsonNode deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  56 */     return deserializeAny(jp, ctxt);
/*     */   }
/*     */ 
/*     */   static final class ArrayDeserializer extends BaseNodeDeserializer<ArrayNode>
/*     */   {
/*  94 */     protected static final ArrayDeserializer _instance = new ArrayDeserializer();
/*     */ 
/*     */     protected ArrayDeserializer() {
/*  97 */       super();
/*     */     }
/*     */     public static ArrayDeserializer getInstance() {
/* 100 */       return _instance;
/*     */     }
/*     */ 
/*     */     public ArrayNode deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 106 */       if (jp.isExpectedStartArrayToken()) {
/* 107 */         return deserializeArray(jp, ctxt);
/*     */       }
/* 109 */       throw ctxt.mappingException(ArrayNode.class);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class ObjectDeserializer extends BaseNodeDeserializer<ObjectNode>
/*     */   {
/*  68 */     protected static final ObjectDeserializer _instance = new ObjectDeserializer();
/*     */ 
/*     */     protected ObjectDeserializer() {
/*  71 */       super();
/*     */     }
/*     */     public static ObjectDeserializer getInstance() {
/*  74 */       return _instance;
/*     */     }
/*     */ 
/*     */     public ObjectNode deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/*  80 */       if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
/*  81 */         jp.nextToken();
/*  82 */         return deserializeObject(jp, ctxt);
/*     */       }
/*  84 */       if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
/*  85 */         return deserializeObject(jp, ctxt);
/*     */       }
/*  87 */       throw ctxt.mappingException(ObjectNode.class);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.JsonNodeDeserializer
 * JD-Core Version:    0.6.0
 */