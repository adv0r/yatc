/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map.Entry;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.ResolvableSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.map.util.EnumValues;
/*     */ import org.codehaus.jackson.node.JsonNodeFactory;
/*     */ import org.codehaus.jackson.node.ObjectNode;
/*     */ import org.codehaus.jackson.schema.JsonSchema;
/*     */ import org.codehaus.jackson.schema.SchemaAware;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class EnumMapSerializer extends ContainerSerializerBase<EnumMap<? extends Enum<?>, ?>>
/*     */   implements ResolvableSerializer
/*     */ {
/*     */   protected final boolean _staticTyping;
/*     */   protected final EnumValues _keyEnums;
/*     */   protected final JavaType _valueType;
/*     */   protected final BeanProperty _property;
/*     */   protected JsonSerializer<Object> _valueSerializer;
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */ 
/*     */   public EnumMapSerializer(JavaType valueType, boolean staticTyping, EnumValues keyEnums, TypeSerializer vts, BeanProperty property)
/*     */   {
/*  61 */     super(EnumMap.class, false);
/*  62 */     this._staticTyping = ((staticTyping) || ((valueType != null) && (valueType.isFinal())));
/*  63 */     this._valueType = valueType;
/*  64 */     this._keyEnums = keyEnums;
/*  65 */     this._valueTypeSerializer = vts;
/*  66 */     this._property = property;
/*     */   }
/*     */ 
/*     */   public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  72 */     return new EnumMapSerializer(this._valueType, this._staticTyping, this._keyEnums, vts, this._property);
/*     */   }
/*     */ 
/*     */   public void serialize(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  79 */     jgen.writeStartObject();
/*  80 */     if (!value.isEmpty()) {
/*  81 */       serializeContents(value, jgen, provider);
/*     */     }
/*  83 */     jgen.writeEndObject();
/*     */   }
/*     */ 
/*     */   public void serializeWithType(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  91 */     typeSer.writeTypePrefixForObject(value, jgen);
/*  92 */     if (!value.isEmpty()) {
/*  93 */       serializeContents(value, jgen, provider);
/*     */     }
/*  95 */     typeSer.writeTypeSuffixForObject(value, jgen);
/*     */   }
/*     */ 
/*     */   protected void serializeContents(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 101 */     if (this._valueSerializer != null) {
/* 102 */       serializeContentsUsing(value, jgen, provider, this._valueSerializer);
/* 103 */       return;
/*     */     }
/* 105 */     JsonSerializer prevSerializer = null;
/* 106 */     Class prevClass = null;
/* 107 */     EnumValues keyEnums = this._keyEnums;
/*     */ 
/* 109 */     for (Map.Entry entry : value.entrySet())
/*     */     {
/* 111 */       Enum key = (Enum)entry.getKey();
/* 112 */       if (keyEnums == null)
/*     */       {
/* 118 */         SerializerBase ser = (SerializerBase)provider.findValueSerializer(key.getDeclaringClass(), this._property);
/*     */ 
/* 120 */         keyEnums = ((EnumSerializer)ser).getEnumValues();
/*     */       }
/* 122 */       jgen.writeFieldName(keyEnums.serializedValueFor(key));
/*     */ 
/* 124 */       Object valueElem = entry.getValue();
/* 125 */       if (valueElem == null) {
/* 126 */         provider.defaultSerializeNull(jgen);
/*     */       } else {
/* 128 */         Class cc = valueElem.getClass();
/*     */         JsonSerializer currSerializer;
/*     */         JsonSerializer currSerializer;
/* 130 */         if (cc == prevClass) {
/* 131 */           currSerializer = prevSerializer;
/*     */         } else {
/* 133 */           currSerializer = provider.findValueSerializer(cc, this._property);
/* 134 */           prevSerializer = currSerializer;
/* 135 */           prevClass = cc;
/*     */         }
/*     */         try {
/* 138 */           currSerializer.serialize(valueElem, jgen, provider);
/*     */         }
/*     */         catch (Exception e) {
/* 141 */           wrapAndThrow(provider, e, value, ((Enum)entry.getKey()).name());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void serializeContentsUsing(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> valueSer)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 151 */     EnumValues keyEnums = this._keyEnums;
/* 152 */     for (Map.Entry entry : value.entrySet()) {
/* 153 */       Enum key = (Enum)entry.getKey();
/* 154 */       if (keyEnums == null)
/*     */       {
/* 156 */         SerializerBase ser = (SerializerBase)provider.findValueSerializer(key.getDeclaringClass(), this._property);
/*     */ 
/* 158 */         keyEnums = ((EnumSerializer)ser).getEnumValues();
/*     */       }
/* 160 */       jgen.writeFieldName(keyEnums.serializedValueFor(key));
/* 161 */       Object valueElem = entry.getValue();
/* 162 */       if (valueElem == null)
/* 163 */         provider.defaultSerializeNull(jgen);
/*     */       else
/*     */         try {
/* 166 */           valueSer.serialize(valueElem, jgen, provider);
/*     */         } catch (Exception e) {
/* 168 */           wrapAndThrow(provider, e, value, ((Enum)entry.getKey()).name());
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 178 */     if (this._staticTyping)
/* 179 */       this._valueSerializer = provider.findValueSerializer(this._valueType, this._property);
/*     */   }
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 188 */     ObjectNode o = createSchemaNode("object", true);
/* 189 */     if ((typeHint instanceof ParameterizedType)) {
/* 190 */       Type[] typeArgs = ((ParameterizedType)typeHint).getActualTypeArguments();
/* 191 */       if (typeArgs.length == 2) {
/* 192 */         JavaType enumType = TypeFactory.type(typeArgs[0]);
/* 193 */         JavaType valueType = TypeFactory.type(typeArgs[1]);
/* 194 */         ObjectNode propsNode = JsonNodeFactory.instance.objectNode();
/* 195 */         Class enumClass = enumType.getRawClass();
/* 196 */         for (Enum enumValue : (Enum[])enumClass.getEnumConstants()) {
/* 197 */           JsonSerializer ser = provider.findValueSerializer(valueType.getRawClass(), this._property);
/* 198 */           JsonNode schemaNode = (ser instanceof SchemaAware) ? ((SchemaAware)ser).getSchema(provider, null) : JsonSchema.getDefaultSchemaNode();
/*     */ 
/* 201 */           propsNode.put(provider.getConfig().getAnnotationIntrospector().findEnumValue(enumValue), schemaNode);
/*     */         }
/* 203 */         o.put("properties", propsNode);
/*     */       }
/*     */     }
/* 206 */     return o;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.EnumMapSerializer
 * JD-Core Version:    0.6.0
 */