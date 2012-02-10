/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.sql.Time;
/*     */ import java.util.Calendar;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.JsonSerializable;
/*     */ import org.codehaus.jackson.map.JsonSerializableWithType;
/*     */ import org.codehaus.jackson.map.ObjectMapper;
/*     */ import org.codehaus.jackson.map.SerializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.node.ObjectNode;
/*     */ import org.codehaus.jackson.schema.JsonSerializableSchema;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ import org.codehaus.jackson.util.TokenBuffer;
/*     */ 
/*     */ public class StdSerializers
/*     */ {
/*     */   @JacksonStdImpl
/*     */   public static final class TokenBufferSerializer extends SerializerBase<TokenBuffer>
/*     */   {
/*     */     public TokenBufferSerializer()
/*     */     {
/* 591 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(TokenBuffer value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 597 */       value.serialize(jgen);
/*     */     }
/*     */ 
/*     */     public final void serializeWithType(TokenBuffer value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 617 */       typeSer.writeTypePrefixForScalar(value, jgen);
/* 618 */       serialize(value, jgen, provider);
/* 619 */       typeSer.writeTypeSuffixForScalar(value, jgen);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 629 */       return createSchemaNode("any", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class SerializableWithTypeSerializer extends SerializerBase<JsonSerializableWithType>
/*     */   {
/* 517 */     protected static final SerializableWithTypeSerializer instance = new SerializableWithTypeSerializer();
/*     */ 
/* 519 */     private SerializableWithTypeSerializer() { super();
/*     */     }
/*     */ 
/*     */     public void serialize(JsonSerializableWithType value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 526 */       value.serialize(jgen, provider);
/*     */     }
/*     */ 
/*     */     public final void serializeWithType(JsonSerializableWithType value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 534 */       value.serializeWithType(jgen, provider, typeSer);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 542 */       ObjectNode objectNode = createObjectNode();
/* 543 */       String schemaType = "any";
/* 544 */       String objectProperties = null;
/* 545 */       String itemDefinition = null;
/* 546 */       if (typeHint != null) {
/* 547 */         Class rawClass = TypeFactory.type(typeHint).getRawClass();
/* 548 */         if (rawClass.isAnnotationPresent(JsonSerializableSchema.class)) {
/* 549 */           JsonSerializableSchema schemaInfo = (JsonSerializableSchema)rawClass.getAnnotation(JsonSerializableSchema.class);
/* 550 */           schemaType = schemaInfo.schemaType();
/* 551 */           if (!"##irrelevant".equals(schemaInfo.schemaObjectPropertiesDefinition())) {
/* 552 */             objectProperties = schemaInfo.schemaObjectPropertiesDefinition();
/*     */           }
/* 554 */           if (!"##irrelevant".equals(schemaInfo.schemaItemDefinition())) {
/* 555 */             itemDefinition = schemaInfo.schemaItemDefinition();
/*     */           }
/*     */         }
/*     */       }
/* 559 */       objectNode.put("type", schemaType);
/* 560 */       if (objectProperties != null) {
/*     */         try {
/* 562 */           objectNode.put("properties", (JsonNode)new ObjectMapper().readValue(objectProperties, JsonNode.class));
/*     */         } catch (IOException e) {
/* 564 */           throw new IllegalStateException(e);
/*     */         }
/*     */       }
/* 567 */       if (itemDefinition != null) {
/*     */         try {
/* 569 */           objectNode.put("items", (JsonNode)new ObjectMapper().readValue(itemDefinition, JsonNode.class));
/*     */         } catch (IOException e) {
/* 571 */           throw new IllegalStateException(e);
/*     */         }
/*     */       }
/* 574 */       objectNode.put("optional", true);
/* 575 */       return objectNode;
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class SerializableSerializer extends SerializerBase<JsonSerializable>
/*     */   {
/* 439 */     protected static final SerializableSerializer instance = new SerializableSerializer();
/*     */ 
/* 441 */     private SerializableSerializer() { super();
/*     */     }
/*     */ 
/*     */     public void serialize(JsonSerializable value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 447 */       value.serialize(jgen, provider);
/*     */     }
/*     */ 
/*     */     public final void serializeWithType(JsonSerializable value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 459 */       if ((value instanceof JsonSerializableWithType))
/* 460 */         ((JsonSerializableWithType)value).serializeWithType(jgen, provider, typeSer);
/*     */       else
/* 462 */         serialize(value, jgen, provider);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 470 */       ObjectNode objectNode = createObjectNode();
/* 471 */       String schemaType = "any";
/* 472 */       String objectProperties = null;
/* 473 */       String itemDefinition = null;
/* 474 */       if (typeHint != null) {
/* 475 */         Class rawClass = TypeFactory.type(typeHint).getRawClass();
/* 476 */         if (rawClass.isAnnotationPresent(JsonSerializableSchema.class)) {
/* 477 */           JsonSerializableSchema schemaInfo = (JsonSerializableSchema)rawClass.getAnnotation(JsonSerializableSchema.class);
/* 478 */           schemaType = schemaInfo.schemaType();
/* 479 */           if (!"##irrelevant".equals(schemaInfo.schemaObjectPropertiesDefinition())) {
/* 480 */             objectProperties = schemaInfo.schemaObjectPropertiesDefinition();
/*     */           }
/* 482 */           if (!"##irrelevant".equals(schemaInfo.schemaItemDefinition())) {
/* 483 */             itemDefinition = schemaInfo.schemaItemDefinition();
/*     */           }
/*     */         }
/*     */       }
/* 487 */       objectNode.put("type", schemaType);
/* 488 */       if (objectProperties != null) {
/*     */         try {
/* 490 */           objectNode.put("properties", (JsonNode)new ObjectMapper().readValue(objectProperties, JsonNode.class));
/*     */         } catch (IOException e) {
/* 492 */           throw new IllegalStateException(e);
/*     */         }
/*     */       }
/* 495 */       if (itemDefinition != null) {
/*     */         try {
/* 497 */           objectNode.put("items", (JsonNode)new ObjectMapper().readValue(itemDefinition, JsonNode.class));
/*     */         } catch (IOException e) {
/* 499 */           throw new IllegalStateException(e);
/*     */         }
/*     */       }
/* 502 */       objectNode.put("optional", true);
/* 503 */       return objectNode;
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class SqlTimeSerializer extends ScalarSerializerBase<Time>
/*     */   {
/*     */     public SqlTimeSerializer()
/*     */     {
/* 405 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(Time value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 411 */       jgen.writeString(value.toString());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 417 */       return createSchemaNode("string", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class SqlDateSerializer extends ScalarSerializerBase<java.sql.Date>
/*     */   {
/*     */     public SqlDateSerializer()
/*     */     {
/* 384 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(java.sql.Date value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 390 */       jgen.writeString(value.toString());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 397 */       return createSchemaNode("string", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class UtilDateSerializer extends ScalarSerializerBase<java.util.Date>
/*     */   {
/* 354 */     public static final UtilDateSerializer instance = new UtilDateSerializer();
/*     */ 
/* 356 */     public UtilDateSerializer() { super();
/*     */     }
/*     */ 
/*     */     public void serialize(java.util.Date value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 362 */       provider.defaultSerializeDateValue(value, jgen);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 370 */       return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "number" : "string", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class CalendarSerializer extends ScalarSerializerBase<Calendar>
/*     */   {
/* 326 */     public static final CalendarSerializer instance = new CalendarSerializer();
/*     */ 
/* 328 */     public CalendarSerializer() { super();
/*     */     }
/*     */ 
/*     */     public void serialize(Calendar value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 334 */       provider.defaultSerializeDateValue(value.getTimeInMillis(), jgen);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 341 */       return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "number" : "string", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class NumberSerializer extends ScalarSerializerBase<Number>
/*     */   {
/* 277 */     public static final NumberSerializer instance = new NumberSerializer();
/*     */ 
/* 279 */     public NumberSerializer() { super();
/*     */     }
/*     */ 
/*     */     public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 286 */       if ((value instanceof BigDecimal))
/* 287 */         jgen.writeNumber((BigDecimal)value);
/* 288 */       else if ((value instanceof BigInteger)) {
/* 289 */         jgen.writeNumber((BigInteger)value);
/*     */       }
/* 294 */       else if ((value instanceof Double))
/* 295 */         jgen.writeNumber(((Double)value).doubleValue());
/* 296 */       else if ((value instanceof Float)) {
/* 297 */         jgen.writeNumber(((Float)value).floatValue());
/*     */       }
/*     */       else
/* 300 */         jgen.writeNumber(value.toString());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 307 */       return createSchemaNode("number", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class DoubleSerializer extends StdSerializers.NonTypedScalarSerializer<Double>
/*     */   {
/* 251 */     static final DoubleSerializer instance = new DoubleSerializer();
/*     */ 
/* 253 */     public DoubleSerializer() { super();
/*     */     }
/*     */ 
/*     */     public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 259 */       jgen.writeNumber(value.doubleValue());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 265 */       return createSchemaNode("number", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class FloatSerializer extends ScalarSerializerBase<Float>
/*     */   {
/* 222 */     static final FloatSerializer instance = new FloatSerializer();
/*     */ 
/* 224 */     public FloatSerializer() { super();
/*     */     }
/*     */ 
/*     */     public void serialize(Float value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 230 */       jgen.writeNumber(value.floatValue());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 236 */       return createSchemaNode("number", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class LongSerializer extends ScalarSerializerBase<Long>
/*     */   {
/* 200 */     static final LongSerializer instance = new LongSerializer();
/*     */ 
/* 202 */     public LongSerializer() { super();
/*     */     }
/*     */ 
/*     */     public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 208 */       jgen.writeNumber(value.longValue());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 214 */       return createSchemaNode("number", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class IntLikeSerializer extends ScalarSerializerBase<Number>
/*     */   {
/* 177 */     static final IntLikeSerializer instance = new IntLikeSerializer();
/*     */ 
/* 179 */     public IntLikeSerializer() { super();
/*     */     }
/*     */ 
/*     */     public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 185 */       jgen.writeNumber(value.intValue());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 192 */       return createSchemaNode("integer", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class IntegerSerializer extends StdSerializers.NonTypedScalarSerializer<Integer>
/*     */   {
/*     */     public IntegerSerializer()
/*     */     {
/* 151 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 157 */       jgen.writeNumber(value.intValue());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 164 */       return createSchemaNode("integer", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class StringSerializer extends StdSerializers.NonTypedScalarSerializer<String>
/*     */   {
/*     */     public StringSerializer()
/*     */     {
/* 118 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 124 */       jgen.writeString(value);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 130 */       return createSchemaNode("string", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class BooleanSerializer extends StdSerializers.NonTypedScalarSerializer<Boolean>
/*     */   {
/*     */     final boolean _forPrimitive;
/*     */ 
/*     */     public BooleanSerializer(boolean forPrimitive)
/*     */     {
/*  83 */       super();
/*  84 */       this._forPrimitive = forPrimitive;
/*     */     }
/*     */ 
/*     */     public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/*  91 */       jgen.writeBoolean(value.booleanValue());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 104 */       return createSchemaNode("boolean", !this._forPrimitive);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static abstract class NonTypedScalarSerializer<T> extends ScalarSerializerBase<T>
/*     */   {
/*     */     protected NonTypedScalarSerializer(Class<T> t)
/*     */     {
/*  45 */       super();
/*     */     }
/*     */ 
/*     */     public final void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/*  54 */       serialize(value, jgen, provider);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.StdSerializers
 * JD-Core Version:    0.6.0
 */