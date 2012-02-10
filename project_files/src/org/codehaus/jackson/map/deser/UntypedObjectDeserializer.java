/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ import org.codehaus.jackson.map.util.ObjectBuffer;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class UntypedObjectDeserializer extends StdDeserializer<Object>
/*     */ {
/*     */   public UntypedObjectDeserializer()
/*     */   {
/*  24 */     super(Object.class);
/*     */   }
/*     */ 
/*     */   public Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  36 */     switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()])
/*     */     {
/*     */     case 1:
/*  39 */       return jp.getText();
/*     */     case 2:
/*  45 */       if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS)) {
/*  46 */         return jp.getBigIntegerValue();
/*     */       }
/*  48 */       return jp.getNumberValue();
/*     */     case 3:
/*  54 */       if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/*  55 */         return jp.getDecimalValue();
/*     */       }
/*  57 */       return Double.valueOf(jp.getDoubleValue());
/*     */     case 4:
/*  60 */       return Boolean.TRUE;
/*     */     case 5:
/*  62 */       return Boolean.FALSE;
/*     */     case 6:
/*  64 */       return jp.getEmbeddedObject();
/*     */     case 7:
/*  67 */       return null;
/*     */     case 8:
/*  72 */       return mapArray(jp, ctxt);
/*     */     case 9:
/*     */     case 10:
/*  76 */       return mapObject(jp, ctxt);
/*     */     case 11:
/*     */     case 12:
/*     */     }
/*     */ 
/*  84 */     throw ctxt.mappingException(Object.class);
/*     */   }
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  92 */     JsonToken t = jp.getCurrentToken();
/*  93 */     switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()])
/*     */     {
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/* 101 */       return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
/*     */     case 1:
/* 107 */       return jp.getText();
/*     */     case 2:
/* 111 */       if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS)) {
/* 112 */         return jp.getBigIntegerValue();
/*     */       }
/* 114 */       return Integer.valueOf(jp.getIntValue());
/*     */     case 3:
/* 118 */       if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 119 */         return jp.getDecimalValue();
/*     */       }
/* 121 */       return Double.valueOf(jp.getDoubleValue());
/*     */     case 4:
/* 124 */       return Boolean.TRUE;
/*     */     case 5:
/* 126 */       return Boolean.FALSE;
/*     */     case 6:
/* 128 */       return jp.getEmbeddedObject();
/*     */     case 7:
/* 131 */       return null;
/*     */     }
/* 133 */     throw ctxt.mappingException(Object.class);
/*     */   }
/*     */ 
/*     */   protected List<Object> mapArray(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 146 */     if (jp.nextToken() == JsonToken.END_ARRAY) {
/* 147 */       return new ArrayList(4);
/*     */     }
/* 149 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 150 */     Object[] values = buffer.resetAndStart();
/* 151 */     int ptr = 0;
/* 152 */     int totalSize = 0;
/*     */     do {
/* 154 */       Object value = deserialize(jp, ctxt);
/* 155 */       totalSize++;
/* 156 */       if (ptr >= values.length) {
/* 157 */         values = buffer.appendCompletedChunk(values);
/* 158 */         ptr = 0;
/*     */       }
/* 160 */       values[(ptr++)] = value;
/* 161 */     }while (jp.nextToken() != JsonToken.END_ARRAY);
/*     */ 
/* 163 */     ArrayList result = new ArrayList(totalSize + (totalSize >> 3) + 1);
/* 164 */     buffer.completeAndClearBuffer(values, ptr, result);
/* 165 */     return result;
/*     */   }
/*     */ 
/*     */   protected Map<String, Object> mapObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 171 */     JsonToken t = jp.getCurrentToken();
/* 172 */     if (t == JsonToken.START_OBJECT) {
/* 173 */       t = jp.nextToken();
/*     */     }
/*     */ 
/* 176 */     if (t != JsonToken.FIELD_NAME)
/*     */     {
/* 178 */       return new LinkedHashMap(4);
/*     */     }
/* 180 */     String field1 = jp.getText();
/* 181 */     jp.nextToken();
/* 182 */     Object value1 = deserialize(jp, ctxt);
/* 183 */     if (jp.nextToken() != JsonToken.FIELD_NAME) {
/* 184 */       LinkedHashMap result = new LinkedHashMap(4);
/* 185 */       result.put(field1, value1);
/* 186 */       return result;
/*     */     }
/* 188 */     String field2 = jp.getText();
/* 189 */     jp.nextToken();
/* 190 */     Object value2 = deserialize(jp, ctxt);
/* 191 */     if (jp.nextToken() != JsonToken.FIELD_NAME) {
/* 192 */       LinkedHashMap result = new LinkedHashMap(4);
/* 193 */       result.put(field1, value1);
/* 194 */       result.put(field2, value2);
/* 195 */       return result;
/*     */     }
/*     */ 
/* 198 */     LinkedHashMap result = new LinkedHashMap();
/* 199 */     result.put(field1, value1);
/* 200 */     result.put(field2, value2);
/*     */     do {
/* 202 */       String fieldName = jp.getText();
/* 203 */       jp.nextToken();
/* 204 */       result.put(fieldName, deserialize(jp, ctxt));
/* 205 */     }while (jp.nextToken() != JsonToken.END_OBJECT);
/* 206 */     return result;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.UntypedObjectDeserializer
 * JD-Core Version:    0.6.0
 */