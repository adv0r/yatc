/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.node.ObjectNode;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class MapSerializer extends ContainerSerializerBase<Map<?, ?>>
/*     */   implements ResolvableSerializer
/*     */ {
/*  27 */   protected static final JavaType UNSPECIFIED_TYPE = TypeFactory.fastSimpleType(Object.class);
/*     */   protected final BeanProperty _property;
/*     */   protected final HashSet<String> _ignoredEntries;
/*     */   protected final boolean _valueTypeIsStatic;
/*     */   protected final JavaType _keyType;
/*     */   protected final JavaType _valueType;
/*     */   protected JsonSerializer<Object> _keySerializer;
/*     */   protected JsonSerializer<Object> _valueSerializer;
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */ 
/*     */   protected MapSerializer()
/*     */   {
/*  79 */     this((HashSet)null, null, null, false, null, null, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected MapSerializer(HashSet<String> ignoredEntries, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts)
/*     */   {
/*  92 */     this(ignoredEntries, UNSPECIFIED_TYPE, valueType, valueTypeIsStatic, vts, null, null);
/*     */   }
/*     */ 
/*     */   protected MapSerializer(HashSet<String> ignoredEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<Object> keySerializer, BeanProperty property)
/*     */   {
/* 100 */     super(Map.class, false);
/* 101 */     this._property = property;
/* 102 */     this._ignoredEntries = ignoredEntries;
/* 103 */     this._keyType = keyType;
/* 104 */     this._valueType = valueType;
/* 105 */     this._valueTypeIsStatic = valueTypeIsStatic;
/* 106 */     this._valueTypeSerializer = vts;
/* 107 */     this._keySerializer = keySerializer;
/*     */   }
/*     */ 
/*     */   public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/* 113 */     MapSerializer ms = new MapSerializer(this._ignoredEntries, this._keyType, this._valueType, this._valueTypeIsStatic, vts, this._keySerializer, this._property);
/*     */ 
/* 115 */     if (this._valueSerializer != null) {
/* 116 */       ms._valueSerializer = this._valueSerializer;
/*     */     }
/* 118 */     return ms;
/*     */   }
/*     */ 
/*     */   public static MapSerializer construct(String[] ignoredList, JavaType mapType, boolean staticValueType, TypeSerializer vts, BeanProperty property)
/*     */   {
/* 134 */     HashSet ignoredEntries = toSet(ignoredList);
/*     */     JavaType keyType;
/*     */     JavaType keyType;
/*     */     JavaType valueType;
/* 137 */     if (mapType == null)
/*     */     {
/*     */       JavaType valueType;
/* 138 */       keyType = valueType = UNSPECIFIED_TYPE;
/*     */     } else {
/* 140 */       keyType = mapType.getKeyType();
/* 141 */       valueType = mapType.getContentType();
/*     */     }
/*     */ 
/* 144 */     if (!staticValueType) {
/* 145 */       staticValueType = (valueType != null) && (valueType.isFinal());
/*     */     }
/* 147 */     return new MapSerializer(ignoredEntries, keyType, valueType, staticValueType, vts, null, property);
/*     */   }
/*     */ 
/*     */   private static HashSet<String> toSet(String[] ignoredEntries)
/*     */   {
/* 152 */     if ((ignoredEntries == null) || (ignoredEntries.length == 0)) {
/* 153 */       return null;
/*     */     }
/* 155 */     HashSet result = new HashSet(ignoredEntries.length);
/* 156 */     for (String prop : ignoredEntries) {
/* 157 */       result.add(prop);
/*     */     }
/* 159 */     return result;
/*     */   }
/*     */ 
/*     */   public void serialize(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 172 */     jgen.writeStartObject();
/* 173 */     if (!value.isEmpty()) {
/* 174 */       if (this._valueSerializer != null)
/* 175 */         serializeFieldsUsing(value, jgen, provider, this._valueSerializer);
/*     */       else {
/* 177 */         serializeFields(value, jgen, provider);
/*     */       }
/*     */     }
/* 180 */     jgen.writeEndObject();
/*     */   }
/*     */ 
/*     */   public void serializeWithType(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 188 */     typeSer.writeTypePrefixForObject(value, jgen);
/* 189 */     if (!value.isEmpty()) {
/* 190 */       if (this._valueSerializer != null)
/* 191 */         serializeFieldsUsing(value, jgen, provider, this._valueSerializer);
/*     */       else {
/* 193 */         serializeFields(value, jgen, provider);
/*     */       }
/*     */     }
/* 196 */     typeSer.writeTypeSuffixForObject(value, jgen);
/*     */   }
/*     */ 
/*     */   protected void serializeFields(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 212 */     if (this._valueTypeSerializer != null) {
/* 213 */       serializeTypedFields(value, jgen, provider);
/* 214 */       return;
/*     */     }
/* 216 */     JsonSerializer keySerializer = this._keySerializer;
/*     */ 
/* 218 */     JsonSerializer prevValueSerializer = null;
/* 219 */     Class prevValueClass = null;
/* 220 */     HashSet ignored = this._ignoredEntries;
/* 221 */     boolean skipNulls = !provider.isEnabled(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES);
/*     */ 
/* 223 */     for (Map.Entry entry : value.entrySet()) {
/* 224 */       Object valueElem = entry.getValue();
/*     */ 
/* 226 */       Object keyElem = entry.getKey();
/* 227 */       if (keyElem == null) {
/* 228 */         provider.getNullKeySerializer().serialize(null, jgen, provider);
/*     */       }
/*     */       else {
/* 231 */         if (((skipNulls) && (valueElem == null)) || (
/* 233 */           (ignored != null) && (ignored.contains(keyElem)))) continue;
/* 234 */         keySerializer.serialize(keyElem, jgen, provider);
/*     */       }
/*     */ 
/* 238 */       if (valueElem == null) {
/* 239 */         provider.defaultSerializeNull(jgen);
/*     */       } else {
/* 241 */         Class cc = valueElem.getClass();
/*     */         JsonSerializer currSerializer;
/*     */         JsonSerializer currSerializer;
/* 243 */         if (cc == prevValueClass) {
/* 244 */           currSerializer = prevValueSerializer;
/*     */         } else {
/* 246 */           currSerializer = provider.findValueSerializer(cc, this._property);
/* 247 */           prevValueSerializer = currSerializer;
/* 248 */           prevValueClass = cc;
/*     */         }
/*     */         try {
/* 251 */           currSerializer.serialize(valueElem, jgen, provider);
/*     */         }
/*     */         catch (Exception e) {
/* 254 */           String keyDesc = "" + keyElem;
/* 255 */           wrapAndThrow(provider, e, value, keyDesc);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void serializeFieldsUsing(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 270 */     JsonSerializer keySerializer = this._keySerializer;
/* 271 */     HashSet ignored = this._ignoredEntries;
/* 272 */     TypeSerializer typeSer = this._valueTypeSerializer;
/* 273 */     boolean skipNulls = !provider.isEnabled(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES);
/*     */ 
/* 275 */     for (Map.Entry entry : value.entrySet()) {
/* 276 */       Object valueElem = entry.getValue();
/* 277 */       Object keyElem = entry.getKey();
/* 278 */       if (keyElem == null) {
/* 279 */         provider.getNullKeySerializer().serialize(null, jgen, provider);
/*     */       }
/*     */       else {
/* 282 */         if (((skipNulls) && (valueElem == null)) || (
/* 283 */           (ignored != null) && (ignored.contains(keyElem)))) continue;
/* 284 */         keySerializer.serialize(keyElem, jgen, provider);
/*     */       }
/* 286 */       if (valueElem == null)
/* 287 */         provider.defaultSerializeNull(jgen);
/*     */       else
/*     */         try {
/* 290 */           if (typeSer == null)
/* 291 */             ser.serialize(valueElem, jgen, provider);
/*     */           else
/* 293 */             ser.serializeWithType(valueElem, jgen, provider, typeSer);
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 297 */           String keyDesc = "" + keyElem;
/* 298 */           wrapAndThrow(provider, e, value, keyDesc);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void serializeTypedFields(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 307 */     JsonSerializer keySerializer = this._keySerializer;
/* 308 */     JsonSerializer prevValueSerializer = null;
/* 309 */     Class prevValueClass = null;
/* 310 */     HashSet ignored = this._ignoredEntries;
/* 311 */     boolean skipNulls = !provider.isEnabled(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES);
/*     */ 
/* 313 */     for (Map.Entry entry : value.entrySet()) {
/* 314 */       Object valueElem = entry.getValue();
/*     */ 
/* 316 */       Object keyElem = entry.getKey();
/* 317 */       if (keyElem == null) {
/* 318 */         provider.getNullKeySerializer().serialize(null, jgen, provider);
/*     */       }
/*     */       else {
/* 321 */         if (((skipNulls) && (valueElem == null)) || (
/* 323 */           (ignored != null) && (ignored.contains(keyElem)))) continue;
/* 324 */         keySerializer.serialize(keyElem, jgen, provider);
/*     */       }
/*     */ 
/* 328 */       if (valueElem == null) {
/* 329 */         provider.defaultSerializeNull(jgen);
/*     */       } else {
/* 331 */         Class cc = valueElem.getClass();
/*     */         JsonSerializer currSerializer;
/*     */         JsonSerializer currSerializer;
/* 333 */         if (cc == prevValueClass) {
/* 334 */           currSerializer = prevValueSerializer;
/*     */         } else {
/* 336 */           currSerializer = provider.findValueSerializer(cc, this._property);
/* 337 */           prevValueSerializer = currSerializer;
/* 338 */           prevValueClass = cc;
/*     */         }
/*     */         try {
/* 341 */           currSerializer.serializeWithType(valueElem, jgen, provider, this._valueTypeSerializer);
/*     */         }
/*     */         catch (Exception e) {
/* 344 */           String keyDesc = "" + keyElem;
/* 345 */           wrapAndThrow(provider, e, value, keyDesc);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */   {
/* 354 */     ObjectNode o = createSchemaNode("object", true);
/*     */ 
/* 357 */     return o;
/*     */   }
/*     */ 
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 367 */     if (this._valueTypeIsStatic) {
/* 368 */       this._valueSerializer = provider.findValueSerializer(this._valueType, this._property);
/*     */     }
/*     */ 
/* 373 */     this._keySerializer = provider.getKeySerializer(this._keyType, this._property);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.MapSerializer
 * JD-Core Version:    0.6.0
 */