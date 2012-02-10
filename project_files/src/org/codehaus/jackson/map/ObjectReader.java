/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.URL;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.codehaus.jackson.JsonFactory;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.Version;
/*     */ import org.codehaus.jackson.Versioned;
/*     */ import org.codehaus.jackson.map.deser.StdDeserializationContext;
/*     */ import org.codehaus.jackson.map.introspect.VisibilityChecker;
/*     */ import org.codehaus.jackson.map.jsontype.SubtypeResolver;
/*     */ import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.node.JsonNodeFactory;
/*     */ import org.codehaus.jackson.node.NullNode;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ import org.codehaus.jackson.util.VersionUtil;
/*     */ 
/*     */ public class ObjectReader
/*     */   implements Versioned
/*     */ {
/*  34 */   private static final JavaType JSON_NODE_TYPE = TypeFactory.type(JsonNode.class);
/*     */   protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
/*     */   protected final DeserializationConfig _config;
/*     */   protected final DeserializerProvider _provider;
/*     */   protected final JsonFactory _jsonFactory;
/*     */   protected TypeResolverBuilder<?> _defaultTyper;
/*     */   protected VisibilityChecker<?> _visibilityChecker;
/*     */   protected final SubtypeResolver _subtypeResolver;
/*     */   protected final JavaType _valueType;
/*     */   protected final Object _valueToUpdate;
/*     */ 
/*     */   protected ObjectReader(ObjectMapper mapper, JavaType valueType, Object valueToUpdate)
/*     */   {
/* 110 */     this._rootDeserializers = mapper._rootDeserializers;
/* 111 */     this._defaultTyper = mapper._defaultTyper;
/* 112 */     this._visibilityChecker = mapper._visibilityChecker;
/* 113 */     this._subtypeResolver = mapper._subtypeResolver;
/* 114 */     this._provider = mapper._deserializerProvider;
/* 115 */     this._jsonFactory = mapper._jsonFactory;
/*     */ 
/* 118 */     this._config = mapper._deserializationConfig.createUnshared(this._defaultTyper, this._visibilityChecker, this._subtypeResolver);
/*     */ 
/* 121 */     this._valueType = valueType;
/* 122 */     this._valueToUpdate = valueToUpdate;
/* 123 */     if ((valueToUpdate != null) && (valueType.isArrayType()))
/* 124 */       throw new IllegalArgumentException("Can not update an array value");
/*     */   }
/*     */ 
/*     */   protected ObjectReader(ObjectReader base, DeserializationConfig config, JavaType valueType, Object valueToUpdate)
/*     */   {
/* 134 */     this._rootDeserializers = base._rootDeserializers;
/* 135 */     this._defaultTyper = base._defaultTyper;
/* 136 */     this._visibilityChecker = base._visibilityChecker;
/* 137 */     this._provider = base._provider;
/* 138 */     this._jsonFactory = base._jsonFactory;
/* 139 */     this._subtypeResolver = base._subtypeResolver;
/*     */ 
/* 141 */     this._config = config;
/*     */ 
/* 143 */     this._valueType = valueType;
/* 144 */     this._valueToUpdate = valueToUpdate;
/* 145 */     if ((valueToUpdate != null) && (valueType.isArrayType()))
/* 146 */       throw new IllegalArgumentException("Can not update an array value");
/*     */   }
/*     */ 
/*     */   public Version version()
/*     */   {
/* 157 */     return VersionUtil.versionFor(getClass());
/*     */   }
/*     */ 
/*     */   public ObjectReader withType(JavaType valueType)
/*     */   {
/* 162 */     if (valueType == this._valueType) return this;
/*     */ 
/* 164 */     return new ObjectReader(this, this._config, valueType, this._valueToUpdate);
/*     */   }
/*     */ 
/*     */   public ObjectReader withType(Class<?> valueType)
/*     */   {
/* 169 */     return withType(TypeFactory.type(valueType));
/*     */   }
/*     */ 
/*     */   public ObjectReader withType(Type valueType)
/*     */   {
/* 174 */     return withType(TypeFactory.type(valueType));
/*     */   }
/*     */ 
/*     */   public ObjectReader withNodeFactory(JsonNodeFactory f)
/*     */   {
/* 180 */     if (f == this._config.getNodeFactory()) return this;
/* 181 */     DeserializationConfig cfg = this._config.createUnshared(f);
/* 182 */     return new ObjectReader(this, cfg, this._valueType, this._valueToUpdate);
/*     */   }
/*     */ 
/*     */   public ObjectReader withValueToUpdate(Object value)
/*     */   {
/* 187 */     if (value == this._valueToUpdate) return this;
/* 188 */     if (value == null) {
/* 189 */       throw new IllegalArgumentException("cat not update null value");
/*     */     }
/* 191 */     JavaType t = TypeFactory.type(value.getClass());
/* 192 */     return new ObjectReader(this, this._config, t, value);
/*     */   }
/*     */ 
/*     */   public <T> T readValue(JsonParser jp)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 206 */     return _bind(jp);
/*     */   }
/*     */ 
/*     */   public JsonNode readTree(JsonParser jp)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 212 */     return _bindAsTree(jp);
/*     */   }
/*     */ 
/*     */   public <T> T readValue(InputStream src)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 225 */     return _bindAndClose(this._jsonFactory.createJsonParser(src));
/*     */   }
/*     */ 
/*     */   public <T> T readValue(Reader src)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 232 */     return _bindAndClose(this._jsonFactory.createJsonParser(src));
/*     */   }
/*     */ 
/*     */   public <T> T readValue(String src)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 239 */     return _bindAndClose(this._jsonFactory.createJsonParser(src));
/*     */   }
/*     */ 
/*     */   public <T> T readValue(byte[] src)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 246 */     return _bindAndClose(this._jsonFactory.createJsonParser(src));
/*     */   }
/*     */ 
/*     */   public <T> T readValue(byte[] src, int offset, int length)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 253 */     return _bindAndClose(this._jsonFactory.createJsonParser(src, offset, length));
/*     */   }
/*     */ 
/*     */   public <T> T readValue(File src)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 260 */     return _bindAndClose(this._jsonFactory.createJsonParser(src));
/*     */   }
/*     */ 
/*     */   public <T> T readValue(URL src)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 267 */     return _bindAndClose(this._jsonFactory.createJsonParser(src));
/*     */   }
/*     */ 
/*     */   public <T> T readValue(JsonNode src)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 283 */     return _bindAndClose(src.traverse());
/*     */   }
/*     */ 
/*     */   public JsonNode readTree(InputStream in)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 289 */     return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(in));
/*     */   }
/*     */ 
/*     */   public JsonNode readTree(Reader r)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 295 */     return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(r));
/*     */   }
/*     */ 
/*     */   public JsonNode readTree(String content)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 301 */     return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(content));
/*     */   }
/*     */ 
/*     */   protected Object _bind(JsonParser jp)
/*     */     throws IOException, JsonParseException, JsonMappingException
/*     */   {
/* 320 */     JsonToken t = _initForReading(jp);
/*     */     Object result;
/*     */     Object result;
/* 321 */     if ((t == JsonToken.VALUE_NULL) || (t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 322 */       result = this._valueToUpdate;
/*     */     } else {
/* 324 */       DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
/*     */       Object result;
/* 325 */       if (this._valueToUpdate == null) {
/* 326 */         result = _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt);
/*     */       } else {
/* 328 */         _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt, this._valueToUpdate);
/* 329 */         result = this._valueToUpdate;
/*     */       }
/*     */     }
/*     */ 
/* 333 */     jp.clearCurrentToken();
/* 334 */     return result;
/*     */   }
/*     */ 
/*     */   protected Object _bindAndClose(JsonParser jp)
/*     */     throws IOException, JsonParseException, JsonMappingException
/*     */   {
/*     */     try
/*     */     {
/* 342 */       JsonToken t = _initForReading(jp);
/*     */       Object result;
/*     */       Object result;
/* 343 */       if ((t == JsonToken.VALUE_NULL) || (t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 344 */         result = this._valueToUpdate;
/*     */       } else {
/* 346 */         ctxt = _createDeserializationContext(jp, this._config);
/*     */         Object result;
/* 347 */         if (this._valueToUpdate == null) {
/* 348 */           result = _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt);
/*     */         } else {
/* 350 */           _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt, this._valueToUpdate);
/* 351 */           result = this._valueToUpdate;
/*     */         }
/*     */       }
/* 354 */       DeserializationContext ctxt = result;
/*     */       return ctxt;
/*     */     }
/*     */     finally
/*     */     {
/*     */       try
/*     */       {
/* 357 */         jp.close(); } catch (IOException ioe) {
/*     */       }
/* 358 */     }throw localObject1;
/*     */   }
/*     */ 
/*     */   protected JsonNode _bindAsTree(JsonParser jp)
/*     */     throws IOException, JsonParseException, JsonMappingException
/*     */   {
/* 366 */     JsonToken t = _initForReading(jp);
/*     */     JsonNode result;
/*     */     JsonNode result;
/* 367 */     if ((t == JsonToken.VALUE_NULL) || (t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 368 */       result = NullNode.instance;
/*     */     } else {
/* 370 */       DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
/*     */ 
/* 372 */       result = (JsonNode)_findRootDeserializer(this._config, JSON_NODE_TYPE).deserialize(jp, ctxt);
/*     */     }
/*     */ 
/* 375 */     jp.clearCurrentToken();
/* 376 */     return result;
/*     */   }
/*     */ 
/*     */   protected JsonNode _bindAndCloseAsTree(JsonParser jp) throws IOException, JsonParseException, JsonMappingException
/*     */   {
/*     */     try
/*     */     {
/* 383 */       JsonNode localJsonNode = _bindAsTree(jp);
/*     */       return localJsonNode;
/*     */     }
/*     */     finally
/*     */     {
/*     */       try
/*     */       {
/* 386 */         jp.close(); } catch (IOException ioe) {
/*     */       }
/* 387 */     }throw localObject;
/*     */   }
/*     */ 
/*     */   protected static JsonToken _initForReading(JsonParser jp)
/*     */     throws IOException, JsonParseException, JsonMappingException
/*     */   {
/* 398 */     JsonToken t = jp.getCurrentToken();
/* 399 */     if (t == null) {
/* 400 */       t = jp.nextToken();
/* 401 */       if (t == null) {
/* 402 */         throw new EOFException("No content to map to Object due to end of input");
/*     */       }
/*     */     }
/* 405 */     return t;
/*     */   }
/*     */ 
/*     */   protected JsonDeserializer<Object> _findRootDeserializer(DeserializationConfig cfg, JavaType valueType)
/*     */     throws JsonMappingException
/*     */   {
/* 415 */     JsonDeserializer deser = (JsonDeserializer)this._rootDeserializers.get(valueType);
/* 416 */     if (deser != null) {
/* 417 */       return deser;
/*     */     }
/*     */ 
/* 421 */     deser = this._provider.findTypedValueDeserializer(cfg, valueType, null);
/* 422 */     if (deser == null) {
/* 423 */       throw new JsonMappingException("Can not find a deserializer for type " + valueType);
/*     */     }
/* 425 */     this._rootDeserializers.put(valueType, deser);
/* 426 */     return deser;
/*     */   }
/*     */ 
/*     */   protected DeserializationContext _createDeserializationContext(JsonParser jp, DeserializationConfig cfg)
/*     */   {
/* 431 */     return new StdDeserializationContext(cfg, jp, this._provider);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ObjectReader
 * JD-Core Version:    0.6.0
 */