/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.ResolvableSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ import org.codehaus.jackson.node.ObjectNode;
/*     */ 
/*     */ public final class ArraySerializers
/*     */ {
/*     */   @JacksonStdImpl
/*     */   public static final class DoubleArraySerializer extends ArraySerializers.AsArraySerializer<double[]>
/*     */   {
/*     */     public DoubleArraySerializer()
/*     */     {
/* 446 */       super(null, null);
/*     */     }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 455 */       return this;
/*     */     }
/*     */ 
/*     */     public void serializeContents(double[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 462 */       int i = 0; for (int len = value.length; i < len; i++)
/* 463 */         jgen.writeNumber(value[i]);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 470 */       ObjectNode o = createSchemaNode("array", true);
/* 471 */       o.put("items", createSchemaNode("number"));
/* 472 */       return o;
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class FloatArraySerializer extends ArraySerializers.AsArraySerializer<float[]>
/*     */   {
/*     */     public FloatArraySerializer()
/*     */     {
/* 416 */       this(null); } 
/* 417 */     public FloatArraySerializer(TypeSerializer vts) { super(vts, null); }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 421 */       return new FloatArraySerializer(vts);
/*     */     }
/*     */ 
/*     */     public void serializeContents(float[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 428 */       int i = 0; for (int len = value.length; i < len; i++)
/* 429 */         jgen.writeNumber(value[i]);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 436 */       ObjectNode o = createSchemaNode("array", true);
/* 437 */       o.put("items", createSchemaNode("number"));
/* 438 */       return o;
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class LongArraySerializer extends ArraySerializers.AsArraySerializer<long[]>
/*     */   {
/*     */     public LongArraySerializer()
/*     */     {
/* 386 */       this(null); } 
/* 387 */     public LongArraySerializer(TypeSerializer vts) { super(vts, null); }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 391 */       return new LongArraySerializer(vts);
/*     */     }
/*     */ 
/*     */     public void serializeContents(long[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 398 */       int i = 0; for (int len = value.length; i < len; i++)
/* 399 */         jgen.writeNumber(value[i]);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 406 */       ObjectNode o = createSchemaNode("array", true);
/* 407 */       o.put("items", createSchemaNode("number", true));
/* 408 */       return o;
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class IntArraySerializer extends ArraySerializers.AsArraySerializer<int[]>
/*     */   {
/*     */     public IntArraySerializer()
/*     */     {
/* 352 */       super(null, null);
/*     */     }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 361 */       return this;
/*     */     }
/*     */ 
/*     */     public void serializeContents(int[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 368 */       int i = 0; for (int len = value.length; i < len; i++)
/* 369 */         jgen.writeNumber(value[i]);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 376 */       ObjectNode o = createSchemaNode("array", true);
/* 377 */       o.put("items", createSchemaNode("integer"));
/* 378 */       return o;
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class CharArraySerializer extends SerializerBase<char[]>
/*     */   {
/*     */     public CharArraySerializer()
/*     */     {
/* 296 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(char[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 303 */       if (provider.isEnabled(SerializationConfig.Feature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
/* 304 */         jgen.writeStartArray();
/* 305 */         _writeArrayContents(jgen, value);
/* 306 */         jgen.writeEndArray();
/*     */       } else {
/* 308 */         jgen.writeString(value, 0, value.length);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void serializeWithType(char[] value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 318 */       if (provider.isEnabled(SerializationConfig.Feature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
/* 319 */         typeSer.writeTypePrefixForArray(value, jgen);
/* 320 */         _writeArrayContents(jgen, value);
/* 321 */         typeSer.writeTypeSuffixForArray(value, jgen);
/*     */       } else {
/* 323 */         typeSer.writeTypePrefixForScalar(value, jgen);
/* 324 */         jgen.writeString(value, 0, value.length);
/* 325 */         typeSer.writeTypeSuffixForScalar(value, jgen);
/*     */       }
/*     */     }
/*     */ 
/*     */     private final void _writeArrayContents(JsonGenerator jgen, char[] value)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 332 */       int i = 0; for (int len = value.length; i < len; i++)
/* 333 */         jgen.writeString(value, i, 1);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 340 */       ObjectNode o = createSchemaNode("array", true);
/* 341 */       ObjectNode itemSchema = createSchemaNode("string");
/* 342 */       itemSchema.put("type", "string");
/* 343 */       o.put("items", itemSchema);
/* 344 */       return o;
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class ShortArraySerializer extends ArraySerializers.AsArraySerializer<short[]>
/*     */   {
/*     */     public ShortArraySerializer()
/*     */     {
/* 257 */       this(null); } 
/* 258 */     public ShortArraySerializer(TypeSerializer vts) { super(vts, null); }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 262 */       return new ShortArraySerializer(vts);
/*     */     }
/*     */ 
/*     */     public void serializeContents(short[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 270 */       int i = 0; for (int len = value.length; i < len; i++)
/* 271 */         jgen.writeNumber(value[i]);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 279 */       ObjectNode o = createSchemaNode("array", true);
/* 280 */       o.put("items", createSchemaNode("integer"));
/* 281 */       return o;
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class ByteArraySerializer extends SerializerBase<byte[]>
/*     */   {
/*     */     public ByteArraySerializer()
/*     */     {
/* 223 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(byte[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 230 */       jgen.writeBinary(value);
/*     */     }
/*     */ 
/*     */     public void serializeWithType(byte[] value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 238 */       typeSer.writeTypePrefixForScalar(value, jgen);
/* 239 */       jgen.writeBinary(value);
/* 240 */       typeSer.writeTypeSuffixForScalar(value, jgen);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 246 */       ObjectNode o = createSchemaNode("array", true);
/* 247 */       ObjectNode itemSchema = createSchemaNode("string");
/* 248 */       o.put("items", itemSchema);
/* 249 */       return o;
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class BooleanArraySerializer extends ArraySerializers.AsArraySerializer<boolean[]>
/*     */   {
/*     */     public BooleanArraySerializer()
/*     */     {
/* 181 */       super(null, null);
/*     */     }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 190 */       return this;
/*     */     }
/*     */ 
/*     */     public void serializeContents(boolean[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 197 */       int i = 0; for (int len = value.length; i < len; i++)
/* 198 */         jgen.writeBoolean(value[i]);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 205 */       ObjectNode o = createSchemaNode("array", true);
/* 206 */       o.put("items", createSchemaNode("boolean"));
/* 207 */       return o;
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class StringArraySerializer extends ArraySerializers.AsArraySerializer<String[]>
/*     */     implements ResolvableSerializer
/*     */   {
/*     */     protected JsonSerializer<Object> _elementSerializer;
/*     */ 
/*     */     public StringArraySerializer(BeanProperty prop)
/*     */     {
/*  97 */       super(null, prop);
/*     */     }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 106 */       return this;
/*     */     }
/*     */ 
/*     */     public void serializeContents(String[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 113 */       int len = value.length;
/* 114 */       if (len == 0) {
/* 115 */         return;
/*     */       }
/* 117 */       if (this._elementSerializer != null) {
/* 118 */         serializeContentsSlow(value, jgen, provider, this._elementSerializer);
/* 119 */         return;
/*     */       }
/*     */ 
/* 129 */       for (int i = 0; i < len; i++) {
/* 130 */         String str = value[i];
/* 131 */         if (str == null) {
/* 132 */           jgen.writeNull();
/*     */         }
/*     */         else
/* 135 */           jgen.writeString(value[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void serializeContentsSlow(String[] value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 144 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 145 */         String str = value[i];
/* 146 */         if (str == null)
/* 147 */           provider.defaultSerializeNull(jgen);
/*     */         else
/* 149 */           ser.serialize(value[i], jgen, provider);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void resolve(SerializerProvider provider)
/*     */       throws JsonMappingException
/*     */     {
/* 161 */       JsonSerializer ser = provider.findValueSerializer(String.class, this._property);
/*     */ 
/* 163 */       if ((ser != null) && (ser.getClass().getAnnotation(JacksonStdImpl.class) == null))
/* 164 */         this._elementSerializer = ser;
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 171 */       ObjectNode o = createSchemaNode("array", true);
/* 172 */       o.put("items", createSchemaNode("string"));
/* 173 */       return o;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class AsArraySerializer<T> extends ContainerSerializerBase<T>
/*     */   {
/*     */     protected final TypeSerializer _valueTypeSerializer;
/*     */     protected final BeanProperty _property;
/*     */ 
/*     */     protected AsArraySerializer(Class<T> cls, TypeSerializer vts, BeanProperty property)
/*     */     {
/*  46 */       super();
/*  47 */       this._valueTypeSerializer = vts;
/*  48 */       this._property = property;
/*     */     }
/*     */ 
/*     */     public final void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/*  55 */       jgen.writeStartArray();
/*  56 */       serializeContents(value, jgen, provider);
/*  57 */       jgen.writeEndArray();
/*     */     }
/*     */ 
/*     */     public final void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/*  65 */       typeSer.writeTypePrefixForArray(value, jgen);
/*  66 */       serializeContents(value, jgen, provider);
/*  67 */       typeSer.writeTypeSuffixForArray(value, jgen);
/*     */     }
/*     */ 
/*     */     protected abstract void serializeContents(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */       throws IOException, JsonGenerationException;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.ArraySerializers
 * JD-Core Version:    0.6.0
 */