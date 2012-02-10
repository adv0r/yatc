/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonParser.NumberType;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.node.ArrayNode;
/*     */ import org.codehaus.jackson.node.JsonNodeFactory;
/*     */ import org.codehaus.jackson.node.ObjectNode;
/*     */ 
/*     */ abstract class BaseNodeDeserializer<N extends JsonNode> extends StdDeserializer<N>
/*     */ {
/*     */   public BaseNodeDeserializer(Class<N> nodeClass)
/*     */   {
/* 123 */     super(nodeClass);
/*     */   }
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 134 */     return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
/*     */   }
/*     */ 
/*     */   protected void _reportProblem(JsonParser jp, String msg)
/*     */     throws JsonMappingException
/*     */   {
/* 146 */     throw new JsonMappingException(msg, jp.getTokenLocation());
/*     */   }
/*     */ 
/*     */   protected void _handleDuplicateField(String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue)
/*     */     throws JsonProcessingException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected final ObjectNode deserializeObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 179 */     ObjectNode node = ctxt.getNodeFactory().objectNode();
/* 180 */     JsonToken t = jp.getCurrentToken();
/* 181 */     if (t == JsonToken.START_OBJECT) {
/* 182 */       t = jp.nextToken();
/*     */     }
/* 184 */     for (; t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/* 185 */       String fieldName = jp.getCurrentName();
/* 186 */       jp.nextToken();
/* 187 */       JsonNode value = deserializeAny(jp, ctxt);
/* 188 */       JsonNode old = node.put(fieldName, value);
/* 189 */       if (old != null) {
/* 190 */         _handleDuplicateField(fieldName, node, old, value);
/*     */       }
/*     */     }
/* 193 */     return node;
/*     */   }
/*     */ 
/*     */   protected final ArrayNode deserializeArray(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 199 */     ArrayNode node = ctxt.getNodeFactory().arrayNode();
/* 200 */     while (jp.nextToken() != JsonToken.END_ARRAY) {
/* 201 */       node.add(deserializeAny(jp, ctxt));
/*     */     }
/* 203 */     return node;
/*     */   }
/*     */ 
/*     */   protected final JsonNode deserializeAny(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 209 */     JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
/* 210 */     switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
/*     */     case 1:
/*     */     case 2:
/* 213 */       return deserializeObject(jp, ctxt);
/*     */     case 3:
/* 216 */       return deserializeArray(jp, ctxt);
/*     */     case 4:
/* 219 */       return nodeFactory.textNode(jp.getText());
/*     */     case 5:
/* 223 */       JsonParser.NumberType nt = jp.getNumberType();
/* 224 */       if ((nt == JsonParser.NumberType.BIG_INTEGER) || (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS)))
/*     */       {
/* 226 */         return nodeFactory.numberNode(jp.getBigIntegerValue());
/*     */       }
/* 228 */       if (nt == JsonParser.NumberType.INT) {
/* 229 */         return nodeFactory.numberNode(jp.getIntValue());
/*     */       }
/* 231 */       return nodeFactory.numberNode(jp.getLongValue());
/*     */     case 6:
/* 236 */       JsonParser.NumberType nt = jp.getNumberType();
/* 237 */       if ((nt == JsonParser.NumberType.BIG_DECIMAL) || (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS)))
/*     */       {
/* 239 */         return nodeFactory.numberNode(jp.getDecimalValue());
/*     */       }
/* 241 */       return nodeFactory.numberNode(jp.getDoubleValue());
/*     */     case 7:
/* 245 */       return nodeFactory.booleanNode(true);
/*     */     case 8:
/* 248 */       return nodeFactory.booleanNode(false);
/*     */     case 9:
/* 251 */       return nodeFactory.nullNode();
/*     */     case 10:
/*     */     case 11:
/*     */     }
/*     */ 
/* 259 */     throw ctxt.mappingException(getValueClass());
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.BaseNodeDeserializer
 * JD-Core Version:    0.6.0
 */