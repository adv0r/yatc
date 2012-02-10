/*      */ package org.codehaus.jackson.map;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.net.URL;
/*      */ import java.util.Collection;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import org.codehaus.jackson.JsonEncoding;
/*      */ import org.codehaus.jackson.JsonFactory;
/*      */ import org.codehaus.jackson.JsonGenerationException;
/*      */ import org.codehaus.jackson.JsonGenerator;
/*      */ import org.codehaus.jackson.JsonGenerator.Feature;
/*      */ import org.codehaus.jackson.JsonNode;
/*      */ import org.codehaus.jackson.JsonParseException;
/*      */ import org.codehaus.jackson.JsonParser;
/*      */ import org.codehaus.jackson.JsonParser.Feature;
/*      */ import org.codehaus.jackson.JsonProcessingException;
/*      */ import org.codehaus.jackson.JsonToken;
/*      */ import org.codehaus.jackson.ObjectCodec;
/*      */ import org.codehaus.jackson.PrettyPrinter;
/*      */ import org.codehaus.jackson.Version;
/*      */ import org.codehaus.jackson.Versioned;
/*      */ import org.codehaus.jackson.annotate.JsonTypeInfo.As;
/*      */ import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
/*      */ import org.codehaus.jackson.io.SegmentedStringWriter;
/*      */ import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
/*      */ import org.codehaus.jackson.map.deser.StdDeserializationContext;
/*      */ import org.codehaus.jackson.map.deser.StdDeserializerProvider;
/*      */ import org.codehaus.jackson.map.introspect.BasicClassIntrospector;
/*      */ import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
/*      */ import org.codehaus.jackson.map.introspect.VisibilityChecker;
/*      */ import org.codehaus.jackson.map.introspect.VisibilityChecker.Std;
/*      */ import org.codehaus.jackson.map.jsontype.NamedType;
/*      */ import org.codehaus.jackson.map.jsontype.SubtypeResolver;
/*      */ import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
/*      */ import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
/*      */ import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
/*      */ import org.codehaus.jackson.map.ser.BeanSerializerFactory;
/*      */ import org.codehaus.jackson.map.ser.BeanSerializerModifier;
/*      */ import org.codehaus.jackson.map.ser.FilterProvider;
/*      */ import org.codehaus.jackson.map.ser.StdSerializerProvider;
/*      */ import org.codehaus.jackson.map.type.TypeFactory;
/*      */ import org.codehaus.jackson.node.ArrayNode;
/*      */ import org.codehaus.jackson.node.JsonNodeFactory;
/*      */ import org.codehaus.jackson.node.NullNode;
/*      */ import org.codehaus.jackson.node.ObjectNode;
/*      */ import org.codehaus.jackson.node.TreeTraversingParser;
/*      */ import org.codehaus.jackson.schema.JsonSchema;
/*      */ import org.codehaus.jackson.type.JavaType;
/*      */ import org.codehaus.jackson.type.TypeReference;
/*      */ import org.codehaus.jackson.util.ByteArrayBuilder;
/*      */ import org.codehaus.jackson.util.DefaultPrettyPrinter;
/*      */ import org.codehaus.jackson.util.TokenBuffer;
/*      */ import org.codehaus.jackson.util.VersionUtil;
/*      */ 
/*      */ public class ObjectMapper extends ObjectCodec
/*      */   implements Versioned
/*      */ {
/*  182 */   private static final JavaType JSON_NODE_TYPE = TypeFactory.type(JsonNode.class);
/*      */ 
/*  187 */   protected static final ClassIntrospector<? extends BeanDescription> DEFAULT_INTROSPECTOR = BasicClassIntrospector.instance;
/*      */ 
/*  190 */   protected static final AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR = new JacksonAnnotationIntrospector();
/*      */ 
/*  193 */   protected static final VisibilityChecker<?> STD_VISIBILITY_CHECKER = VisibilityChecker.Std.defaultInstance();
/*      */   protected final JsonFactory _jsonFactory;
/*      */   protected TypeResolverBuilder<?> _defaultTyper;
/*      */   protected VisibilityChecker<?> _visibilityChecker;
/*      */   protected SubtypeResolver _subtypeResolver;
/*      */   protected ClassLoader _valueClassLoader;
/*      */   protected SerializationConfig _serializationConfig;
/*      */   protected SerializerProvider _serializerProvider;
/*      */   protected SerializerFactory _serializerFactory;
/*      */   protected DeserializationConfig _deserializationConfig;
/*      */   protected DeserializerProvider _deserializerProvider;
/*  322 */   protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers = new ConcurrentHashMap(64, 0.6F, 2);
/*      */ 
/*      */   public ObjectMapper()
/*      */   {
/*  345 */     this(null, null, null);
/*      */   }
/*      */ 
/*      */   public ObjectMapper(JsonFactory jf)
/*      */   {
/*  355 */     this(jf, null, null);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectMapper(SerializerFactory sf)
/*      */   {
/*  368 */     this(null, null, null);
/*  369 */     setSerializerFactory(sf);
/*      */   }
/*      */ 
/*      */   public ObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp)
/*      */   {
/*  375 */     this(jf, sp, dp, null, null);
/*      */   }
/*      */ 
/*      */   public ObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp, SerializationConfig sconfig, DeserializationConfig dconfig)
/*      */   {
/*  398 */     this._jsonFactory = (jf == null ? new MappingJsonFactory(this) : jf);
/*      */ 
/*  400 */     this._visibilityChecker = STD_VISIBILITY_CHECKER;
/*  401 */     this._serializationConfig = (sconfig != null ? sconfig : new SerializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, this._visibilityChecker, null));
/*      */ 
/*  403 */     this._deserializationConfig = (dconfig != null ? dconfig : new DeserializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, this._visibilityChecker, null));
/*      */ 
/*  405 */     this._serializerProvider = (sp == null ? new StdSerializerProvider() : sp);
/*  406 */     this._deserializerProvider = (dp == null ? new StdDeserializerProvider() : dp);
/*      */ 
/*  409 */     this._serializerFactory = BeanSerializerFactory.instance;
/*      */   }
/*      */ 
/*      */   public Version version()
/*      */   {
/*  419 */     return VersionUtil.versionFor(getClass());
/*      */   }
/*      */ 
/*      */   public ObjectMapper setSerializerFactory(SerializerFactory f)
/*      */   {
/*  427 */     this._serializerFactory = f;
/*  428 */     return this;
/*      */   }
/*      */ 
/*      */   public ObjectMapper setSerializerProvider(SerializerProvider p)
/*      */   {
/*  436 */     this._serializerProvider = p;
/*  437 */     return this;
/*      */   }
/*      */ 
/*      */   public SerializerProvider getSerializerProvider()
/*      */   {
/*  444 */     return this._serializerProvider;
/*      */   }
/*      */ 
/*      */   public ObjectMapper setDeserializerProvider(DeserializerProvider p)
/*      */   {
/*  452 */     this._deserializerProvider = p;
/*  453 */     return this;
/*      */   }
/*      */ 
/*      */   public DeserializerProvider getDeserializerProvider()
/*      */   {
/*  460 */     return this._deserializerProvider;
/*      */   }
/*      */ 
/*      */   public ObjectMapper setNodeFactory(JsonNodeFactory f)
/*      */   {
/*  471 */     this._deserializationConfig.setNodeFactory(f);
/*  472 */     return this;
/*      */   }
/*      */ 
/*      */   public VisibilityChecker<?> getVisibilityChecker()
/*      */   {
/*  483 */     return this._visibilityChecker;
/*      */   }
/*      */ 
/*      */   public void setVisibilityChecker(VisibilityChecker<?> vc)
/*      */   {
/*  496 */     this._visibilityChecker = vc;
/*      */   }
/*      */ 
/*      */   public SubtypeResolver getSubtypeResolver()
/*      */   {
/*  503 */     if (this._subtypeResolver == null) {
/*  504 */       this._subtypeResolver = new StdSubtypeResolver();
/*      */     }
/*  506 */     return this._subtypeResolver;
/*      */   }
/*      */ 
/*      */   public void setSubtypeResolver(SubtypeResolver r)
/*      */   {
/*  513 */     this._subtypeResolver = r;
/*      */   }
/*      */ 
/*      */   public void registerSubtypes(Class<?>[] classes)
/*      */   {
/*  526 */     getSubtypeResolver().registerSubtypes(classes);
/*      */   }
/*      */ 
/*      */   public void registerSubtypes(NamedType[] types)
/*      */   {
/*  540 */     getSubtypeResolver().registerSubtypes(types);
/*      */   }
/*      */ 
/*      */   public void registerModule(Module module)
/*      */   {
/*  558 */     String name = module.getModuleName();
/*  559 */     if (name == null) {
/*  560 */       throw new IllegalArgumentException("Module without defined name");
/*      */     }
/*  562 */     Version version = module.version();
/*  563 */     if (version == null) {
/*  564 */       throw new IllegalArgumentException("Module without defined version");
/*      */     }
/*      */ 
/*  567 */     ObjectMapper mapper = this;
/*      */ 
/*  570 */     module.setupModule(new Module.SetupContext(mapper)
/*      */     {
/*      */       public Version getMapperVersion()
/*      */       {
/*  576 */         return ObjectMapper.this.version();
/*      */       }
/*      */ 
/*      */       public DeserializationConfig getDeserializationConfig() {
/*  580 */         return this.val$mapper.getDeserializationConfig();
/*      */       }
/*      */ 
/*      */       public SerializationConfig getSerializationConfig() {
/*  584 */         return this.val$mapper.getSerializationConfig();
/*      */       }
/*      */ 
/*      */       public SerializationConfig getSeserializationConfig()
/*      */       {
/*  589 */         return getSerializationConfig();
/*      */       }
/*      */ 
/*      */       public void addSerializers(Serializers s)
/*      */       {
/*  596 */         this.val$mapper._serializerFactory = this.val$mapper._serializerFactory.withAdditionalSerializers(s);
/*      */       }
/*      */ 
/*      */       public void addBeanSerializerModifier(BeanSerializerModifier modifier)
/*      */       {
/*  601 */         this.val$mapper._serializerFactory = this.val$mapper._serializerFactory.withSerializerModifier(modifier);
/*      */       }
/*      */ 
/*      */       public void addBeanDeserializerModifier(BeanDeserializerModifier modifier)
/*      */       {
/*  606 */         this.val$mapper._deserializerProvider = this.val$mapper._deserializerProvider.withDeserializerModifier(modifier);
/*      */       }
/*      */ 
/*      */       public void addDeserializers(Deserializers d)
/*      */       {
/*  611 */         this.val$mapper._deserializerProvider = this.val$mapper._deserializerProvider.withAdditionalDeserializers(d);
/*      */       }
/*      */ 
/*      */       public void insertAnnotationIntrospector(AnnotationIntrospector ai)
/*      */       {
/*  616 */         this.val$mapper._deserializationConfig.insertAnnotationIntrospector(ai);
/*  617 */         this.val$mapper._serializationConfig.insertAnnotationIntrospector(ai);
/*      */       }
/*      */ 
/*      */       public void appendAnnotationIntrospector(AnnotationIntrospector ai)
/*      */       {
/*  622 */         this.val$mapper._deserializationConfig.appendAnnotationIntrospector(ai);
/*  623 */         this.val$mapper._serializationConfig.appendAnnotationIntrospector(ai);
/*      */       }
/*      */ 
/*      */       public void setMixInAnnotations(Class<?> target, Class<?> mixinSource)
/*      */       {
/*  628 */         this.val$mapper._deserializationConfig.addMixInAnnotations(target, mixinSource);
/*  629 */         this.val$mapper._serializationConfig.addMixInAnnotations(target, mixinSource);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public SerializationConfig getSerializationConfig()
/*      */   {
/*  649 */     return this._serializationConfig;
/*      */   }
/*      */ 
/*      */   public SerializationConfig copySerializationConfig()
/*      */   {
/*  667 */     return this._serializationConfig.createUnshared(this._defaultTyper, this._visibilityChecker, this._subtypeResolver, null);
/*      */   }
/*      */ 
/*      */   public ObjectMapper setSerializationConfig(SerializationConfig cfg)
/*      */   {
/*  676 */     this._serializationConfig = cfg;
/*  677 */     return this;
/*      */   }
/*      */ 
/*      */   public ObjectMapper configure(SerializationConfig.Feature f, boolean state)
/*      */   {
/*  689 */     this._serializationConfig.set(f, state);
/*  690 */     return this;
/*      */   }
/*      */ 
/*      */   public DeserializationConfig getDeserializationConfig()
/*      */   {
/*  702 */     return this._deserializationConfig;
/*      */   }
/*      */ 
/*      */   public DeserializationConfig copyDeserializationConfig()
/*      */   {
/*  720 */     return this._deserializationConfig.createUnshared(this._defaultTyper, this._visibilityChecker, this._subtypeResolver);
/*      */   }
/*      */ 
/*      */   public ObjectMapper setDeserializationConfig(DeserializationConfig cfg)
/*      */   {
/*  729 */     this._deserializationConfig = cfg;
/*  730 */     return this;
/*      */   }
/*      */ 
/*      */   public ObjectMapper configure(DeserializationConfig.Feature f, boolean state)
/*      */   {
/*  742 */     this._deserializationConfig.set(f, state);
/*  743 */     return this;
/*      */   }
/*      */ 
/*      */   public JsonFactory getJsonFactory()
/*      */   {
/*  754 */     return this._jsonFactory;
/*      */   }
/*      */ 
/*      */   public ObjectMapper configure(JsonParser.Feature f, boolean state)
/*      */   {
/*  768 */     this._jsonFactory.configure(f, state);
/*  769 */     return this;
/*      */   }
/*      */ 
/*      */   public ObjectMapper configure(JsonGenerator.Feature f, boolean state)
/*      */   {
/*  784 */     this._jsonFactory.configure(f, state);
/*  785 */     return this;
/*      */   }
/*      */ 
/*      */   public JsonNodeFactory getNodeFactory()
/*      */   {
/*  801 */     return this._deserializationConfig.getNodeFactory();
/*      */   }
/*      */ 
/*      */   public ObjectMapper enableDefaultTyping()
/*      */   {
/*  817 */     return enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);
/*      */   }
/*      */ 
/*      */   public ObjectMapper enableDefaultTyping(DefaultTyping dti)
/*      */   {
/*  827 */     return enableDefaultTyping(dti, JsonTypeInfo.As.WRAPPER_ARRAY);
/*      */   }
/*      */ 
/*      */   public ObjectMapper enableDefaultTyping(DefaultTyping applicability, JsonTypeInfo.As includeAs)
/*      */   {
/*  840 */     TypeResolverBuilder typer = new DefaultTypeResolverBuilder(applicability);
/*      */ 
/*  842 */     typer = typer.init(JsonTypeInfo.Id.CLASS, null);
/*  843 */     typer = typer.inclusion(includeAs);
/*  844 */     return setDefaultTyping(typer);
/*      */   }
/*      */ 
/*      */   public ObjectMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName)
/*      */   {
/*  859 */     TypeResolverBuilder typer = new DefaultTypeResolverBuilder(applicability);
/*      */ 
/*  861 */     typer = typer.init(JsonTypeInfo.Id.CLASS, null);
/*  862 */     typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
/*  863 */     typer = typer.typeProperty(propertyName);
/*  864 */     return setDefaultTyping(typer);
/*      */   }
/*      */ 
/*      */   public ObjectMapper disableDefaultTyping()
/*      */   {
/*  874 */     return setDefaultTyping(null);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectMapper setDefaltTyping(TypeResolverBuilder<?> typer)
/*      */   {
/*  882 */     this._defaultTyper = typer;
/*  883 */     return this;
/*      */   }
/*      */ 
/*      */   public ObjectMapper setDefaultTyping(TypeResolverBuilder<?> typer)
/*      */   {
/*  896 */     this._defaultTyper = typer;
/*  897 */     return this;
/*      */   }
/*      */ 
/*      */   public <T> T readValue(JsonParser jp, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/*  925 */     return _readValue(copyDeserializationConfig(), jp, TypeFactory.type(valueType));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(JsonParser jp, Class<T> valueType, DeserializationConfig cfg)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/*  952 */     return _readValue(cfg, jp, TypeFactory.type(valueType));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/*  967 */     return _readValue(copyDeserializationConfig(), jp, TypeFactory.type(valueTypeRef));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef, DeserializationConfig cfg)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/*  990 */     return _readValue(cfg, jp, TypeFactory.type(valueTypeRef));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(JsonParser jp, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1004 */     return _readValue(copyDeserializationConfig(), jp, valueType);
/*      */   }
/*      */ 
/*      */   public <T> T readValue(JsonParser jp, JavaType valueType, DeserializationConfig cfg)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1026 */     return _readValue(cfg, jp, valueType);
/*      */   }
/*      */ 
/*      */   public JsonNode readTree(JsonParser jp)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1040 */     return readTree(jp, copyDeserializationConfig());
/*      */   }
/*      */ 
/*      */   public JsonNode readTree(JsonParser jp, DeserializationConfig cfg)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1065 */     JsonNode n = (JsonNode)_readValue(cfg, jp, JSON_NODE_TYPE);
/* 1066 */     return n == null ? NullNode.instance : n;
/*      */   }
/*      */ 
/*      */   public JsonNode readTree(InputStream in)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1084 */     JsonNode n = (JsonNode)readValue(in, JSON_NODE_TYPE);
/* 1085 */     return n == null ? NullNode.instance : n;
/*      */   }
/*      */ 
/*      */   public JsonNode readTree(Reader r)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1103 */     JsonNode n = (JsonNode)readValue(r, JSON_NODE_TYPE);
/* 1104 */     return n == null ? NullNode.instance : n;
/*      */   }
/*      */ 
/*      */   public JsonNode readTree(String content)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1122 */     JsonNode n = (JsonNode)readValue(content, JSON_NODE_TYPE);
/* 1123 */     return n == null ? NullNode.instance : n;
/*      */   }
/*      */ 
/*      */   public void writeValue(JsonGenerator jgen, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1141 */     SerializationConfig config = copySerializationConfig();
/* 1142 */     if ((config.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 1143 */       _writeCloseableValue(jgen, value, config);
/*      */     } else {
/* 1145 */       this._serializerProvider.serializeValue(config, jgen, value, this._serializerFactory);
/* 1146 */       if (config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE))
/* 1147 */         jgen.flush();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeValue(JsonGenerator jgen, Object value, SerializationConfig config)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1163 */     if ((config.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 1164 */       _writeCloseableValue(jgen, value, config);
/*      */     } else {
/* 1166 */       this._serializerProvider.serializeValue(config, jgen, value, this._serializerFactory);
/* 1167 */       if (config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE))
/* 1168 */         jgen.flush();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeTree(JsonGenerator jgen, JsonNode rootNode)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1181 */     SerializationConfig config = copySerializationConfig();
/* 1182 */     this._serializerProvider.serializeValue(config, jgen, rootNode, this._serializerFactory);
/* 1183 */     if (config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE))
/* 1184 */       jgen.flush();
/*      */   }
/*      */ 
/*      */   public void writeTree(JsonGenerator jgen, JsonNode rootNode, SerializationConfig cfg)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1198 */     this._serializerProvider.serializeValue(cfg, jgen, rootNode, this._serializerFactory);
/* 1199 */     if (cfg.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE))
/* 1200 */       jgen.flush();
/*      */   }
/*      */ 
/*      */   public ObjectNode createObjectNode()
/*      */   {
/* 1221 */     return this._deserializationConfig.getNodeFactory().objectNode();
/*      */   }
/*      */ 
/*      */   public ArrayNode createArrayNode()
/*      */   {
/* 1235 */     return this._deserializationConfig.getNodeFactory().arrayNode();
/*      */   }
/*      */ 
/*      */   public JsonParser treeAsTokens(JsonNode n)
/*      */   {
/* 1249 */     return new TreeTraversingParser(n, this);
/*      */   }
/*      */ 
/*      */   public <T> T treeToValue(JsonNode n, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1265 */     JsonParser jp = treeAsTokens(n);
/* 1266 */     return readValue(jp, valueType);
/*      */   }
/*      */ 
/*      */   public <T extends JsonNode> T valueToTree(Object fromValue)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1286 */     if (fromValue == null) return null; TokenBuffer buf = new TokenBuffer(this);
/*      */     JsonNode result;
/*      */     try { writeValue(buf, fromValue);
/* 1291 */       JsonParser jp = buf.asParser();
/* 1292 */       result = readTree(jp);
/* 1293 */       jp.close();
/*      */     } catch (IOException e) {
/* 1295 */       throw new IllegalArgumentException(e.getMessage(), e);
/*      */     }
/* 1297 */     return result;
/*      */   }
/*      */ 
/*      */   public boolean canSerialize(Class<?> type)
/*      */   {
/* 1318 */     return this._serializerProvider.hasSerializerFor(this._serializationConfig, type, this._serializerFactory);
/*      */   }
/*      */ 
/*      */   public boolean canDeserialize(JavaType type)
/*      */   {
/* 1333 */     return this._deserializerProvider.hasValueDeserializerFor(this._deserializationConfig, type);
/*      */   }
/*      */ 
/*      */   public <T> T readValue(File src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1349 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueType));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(File src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1356 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueTypeRef));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(File src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1363 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
/*      */   }
/*      */ 
/*      */   public <T> T readValue(URL src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1372 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueType));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(URL src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1379 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueTypeRef));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(URL src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1386 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
/*      */   }
/*      */ 
/*      */   public <T> T readValue(String content, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1395 */     return _readMapAndClose(this._jsonFactory.createJsonParser(content), TypeFactory.type(valueType));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(String content, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1402 */     return _readMapAndClose(this._jsonFactory.createJsonParser(content), TypeFactory.type(valueTypeRef));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(String content, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1409 */     return _readMapAndClose(this._jsonFactory.createJsonParser(content), valueType);
/*      */   }
/*      */ 
/*      */   public <T> T readValue(Reader src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1418 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueType));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(Reader src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1425 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueTypeRef));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(Reader src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1432 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
/*      */   }
/*      */ 
/*      */   public <T> T readValue(InputStream src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1441 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueType));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(InputStream src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1448 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueTypeRef));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(InputStream src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1455 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
/*      */   }
/*      */ 
/*      */   public <T> T readValue(byte[] src, int offset, int len, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1465 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), TypeFactory.type(valueType));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(byte[] src, int offset, int len, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1473 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), TypeFactory.type(valueTypeRef));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(byte[] src, int offset, int len, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1481 */     return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), valueType);
/*      */   }
/*      */ 
/*      */   public <T> T readValue(JsonNode root, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1499 */     return _readValue(copyDeserializationConfig(), root.traverse(), TypeFactory.type(valueType));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(JsonNode root, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1515 */     return _readValue(copyDeserializationConfig(), root.traverse(), TypeFactory.type(valueTypeRef));
/*      */   }
/*      */ 
/*      */   public <T> T readValue(JsonNode root, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1531 */     return _readValue(copyDeserializationConfig(), root.traverse(), valueType);
/*      */   }
/*      */ 
/*      */   public void writeValue(File resultFile, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1548 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(resultFile, JsonEncoding.UTF8), value);
/*      */   }
/*      */ 
/*      */   public void writeValue(OutputStream out, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1565 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8), value);
/*      */   }
/*      */ 
/*      */   public void writeValue(Writer w, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1581 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(w), value);
/*      */   }
/*      */ 
/*      */   public String writeValueAsString(Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1596 */     SegmentedStringWriter sw = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());
/* 1597 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(sw), value);
/* 1598 */     return sw.getAndClear();
/*      */   }
/*      */ 
/*      */   public byte[] writeValueAsBytes(Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1613 */     ByteArrayBuilder bb = new ByteArrayBuilder(this._jsonFactory._getBufferRecycler());
/* 1614 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(bb, JsonEncoding.UTF8), value);
/* 1615 */     byte[] result = bb.toByteArray();
/* 1616 */     bb.release();
/* 1617 */     return result;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void writeValueUsingView(JsonGenerator jgen, Object value, Class<?> viewClass)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1652 */     _configAndWriteValue(jgen, value, viewClass);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void writeValueUsingView(Writer w, Object value, Class<?> viewClass)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1672 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(w), value, viewClass);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void writeValueUsingView(OutputStream out, Object value, Class<?> viewClass)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1692 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8), value, viewClass);
/*      */   }
/*      */ 
/*      */   public ObjectWriter writer()
/*      */   {
/* 1709 */     return new ObjectWriter(this, null, null, null);
/*      */   }
/*      */ 
/*      */   public ObjectWriter viewWriter(Class<?> serializationView)
/*      */   {
/* 1719 */     return new ObjectWriter(this, serializationView, null, null);
/*      */   }
/*      */ 
/*      */   public ObjectWriter typedWriter(Class<?> rootType)
/*      */   {
/* 1732 */     JavaType t = rootType == null ? null : TypeFactory.type(rootType);
/* 1733 */     return new ObjectWriter(this, null, t, null);
/*      */   }
/*      */ 
/*      */   public ObjectWriter typedWriter(JavaType rootType)
/*      */   {
/* 1744 */     return new ObjectWriter(this, null, rootType, null);
/*      */   }
/*      */ 
/*      */   public ObjectWriter typedWriter(TypeReference<?> rootType)
/*      */   {
/* 1755 */     JavaType t = rootType == null ? null : TypeFactory.type(rootType);
/* 1756 */     return new ObjectWriter(this, null, t, null);
/*      */   }
/*      */ 
/*      */   public ObjectWriter prettyPrintingWriter(PrettyPrinter pp)
/*      */   {
/* 1767 */     if (pp == null) {
/* 1768 */       pp = ObjectWriter.NULL_PRETTY_PRINTER;
/*      */     }
/* 1770 */     return new ObjectWriter(this, null, null, pp);
/*      */   }
/*      */ 
/*      */   public ObjectWriter defaultPrettyPrintingWriter()
/*      */   {
/* 1780 */     return new ObjectWriter(this, null, null, _defaultPrettyPrinter());
/*      */   }
/*      */ 
/*      */   public ObjectWriter filteredWriter(FilterProvider filterProvider) {
/* 1784 */     return new ObjectWriter(this, filterProvider);
/*      */   }
/*      */ 
/*      */   public ObjectReader reader()
/*      */   {
/* 1801 */     return new ObjectReader(this, null, null);
/*      */   }
/*      */ 
/*      */   public ObjectReader updatingReader(Object valueToUpdate)
/*      */   {
/* 1818 */     JavaType t = TypeFactory.type(valueToUpdate.getClass());
/* 1819 */     return new ObjectReader(this, t, valueToUpdate);
/*      */   }
/*      */ 
/*      */   public ObjectReader reader(JavaType type)
/*      */   {
/* 1830 */     return new ObjectReader(this, type, null);
/*      */   }
/*      */ 
/*      */   public ObjectReader reader(Class<?> type)
/*      */   {
/* 1841 */     return reader(TypeFactory.type(type));
/*      */   }
/*      */ 
/*      */   public ObjectReader reader(TypeReference<?> type)
/*      */   {
/* 1852 */     return reader(TypeFactory.type(type));
/*      */   }
/*      */ 
/*      */   public ObjectReader reader(JsonNodeFactory f)
/*      */   {
/* 1863 */     return new ObjectReader(this, null, null).withNodeFactory(f);
/*      */   }
/*      */ 
/*      */   public <T> T convertValue(Object fromValue, Class<T> toValueType)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1888 */     return _convert(fromValue, TypeFactory.type(toValueType));
/*      */   }
/*      */ 
/*      */   public <T> T convertValue(Object fromValue, TypeReference toValueTypeRef)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1895 */     return _convert(fromValue, TypeFactory.type(toValueTypeRef));
/*      */   }
/*      */ 
/*      */   public <T> T convertValue(Object fromValue, JavaType toValueType)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1902 */     return _convert(fromValue, toValueType);
/*      */   }
/*      */ 
/*      */   protected Object _convert(Object fromValue, JavaType toValueType)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1909 */     if (fromValue == null) return null;
/*      */ 
/* 1913 */     TokenBuffer buf = new TokenBuffer(this);
/*      */     try {
/* 1915 */       writeValue(buf, fromValue);
/*      */ 
/* 1917 */       JsonParser jp = buf.asParser();
/* 1918 */       Object result = readValue(jp, toValueType);
/* 1919 */       jp.close();
/* 1920 */       return result; } catch (IOException e) {
/*      */     }
/* 1922 */     throw new IllegalArgumentException(e.getMessage(), e);
/*      */   }
/*      */ 
/*      */   public JsonSchema generateJsonSchema(Class<?> t)
/*      */     throws JsonMappingException
/*      */   {
/* 1942 */     return generateJsonSchema(t, copySerializationConfig());
/*      */   }
/*      */ 
/*      */   public JsonSchema generateJsonSchema(Class<?> t, SerializationConfig cfg)
/*      */     throws JsonMappingException
/*      */   {
/* 1956 */     return this._serializerProvider.generateJsonSchema(t, cfg, this._serializerFactory);
/*      */   }
/*      */ 
/*      */   protected PrettyPrinter _defaultPrettyPrinter()
/*      */   {
/* 1973 */     return new DefaultPrettyPrinter();
/*      */   }
/*      */ 
/*      */   protected final void _configAndWriteValue(JsonGenerator jgen, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1983 */     SerializationConfig cfg = copySerializationConfig();
/*      */ 
/* 1985 */     if (cfg.isEnabled(SerializationConfig.Feature.INDENT_OUTPUT)) {
/* 1986 */       jgen.useDefaultPrettyPrinter();
/*      */     }
/*      */ 
/* 1989 */     if ((cfg.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 1990 */       _configAndWriteCloseable(jgen, value, cfg);
/* 1991 */       return;
/*      */     }
/* 1993 */     boolean closed = false;
/*      */     try {
/* 1995 */       this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
/* 1996 */       closed = true;
/* 1997 */       jgen.close();
/*      */     }
/*      */     finally
/*      */     {
/* 2002 */       if (!closed)
/*      */         try {
/* 2004 */           jgen.close();
/*      */         }
/*      */         catch (IOException ioe)
/*      */         {
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void _configAndWriteValue(JsonGenerator jgen, Object value, Class<?> viewClass) throws IOException, JsonGenerationException, JsonMappingException {
/* 2013 */     SerializationConfig cfg = copySerializationConfig();
/* 2014 */     if (cfg.isEnabled(SerializationConfig.Feature.INDENT_OUTPUT)) {
/* 2015 */       jgen.useDefaultPrettyPrinter();
/*      */     }
/* 2017 */     cfg.setSerializationView(viewClass);
/*      */ 
/* 2019 */     if ((cfg.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 2020 */       _configAndWriteCloseable(jgen, value, cfg);
/* 2021 */       return;
/*      */     }
/* 2023 */     boolean closed = false;
/*      */     try {
/* 2025 */       this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
/* 2026 */       closed = true;
/* 2027 */       jgen.close();
/*      */     } finally {
/* 2029 */       if (!closed)
/*      */         try {
/* 2031 */           jgen.close();
/*      */         }
/*      */         catch (IOException ioe)
/*      */         {
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void _configAndWriteCloseable(JsonGenerator jgen, Object value, SerializationConfig cfg)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 2044 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 2046 */       this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
/* 2047 */       JsonGenerator tmpJgen = jgen;
/* 2048 */       jgen = null;
/* 2049 */       tmpJgen.close();
/* 2050 */       Closeable tmpToClose = toClose;
/* 2051 */       toClose = null;
/* 2052 */       tmpToClose.close();
/*      */     }
/*      */     finally
/*      */     {
/* 2057 */       if (jgen != null)
/*      */         try {
/* 2059 */           jgen.close();
/*      */         } catch (IOException ioe) {
/*      */         }
/* 2062 */       if (toClose != null)
/*      */         try {
/* 2064 */           toClose.close();
/*      */         }
/*      */         catch (IOException ioe)
/*      */         {
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void _writeCloseableValue(JsonGenerator jgen, Object value, SerializationConfig cfg)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 2077 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 2079 */       this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
/* 2080 */       if (cfg.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
/* 2081 */         jgen.flush();
/*      */       }
/* 2083 */       Closeable tmpToClose = toClose;
/* 2084 */       toClose = null;
/* 2085 */       tmpToClose.close();
/*      */     } finally {
/* 2087 */       if (toClose != null)
/*      */         try {
/* 2089 */           toClose.close();
/*      */         }
/*      */         catch (IOException ioe)
/*      */         {
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Object _readValue(DeserializationConfig cfg, JsonParser jp, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2106 */     JsonToken t = _initForReading(jp);
/*      */     Object result;
/*      */     Object result;
/* 2107 */     if ((t == JsonToken.VALUE_NULL) || (t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 2108 */       result = null;
/*      */     } else {
/* 2110 */       DeserializationContext ctxt = _createDeserializationContext(jp, cfg);
/*      */ 
/* 2112 */       result = _findRootDeserializer(cfg, valueType).deserialize(jp, ctxt);
/*      */     }
/*      */ 
/* 2115 */     jp.clearCurrentToken();
/* 2116 */     return result;
/*      */   }
/*      */ 
/*      */   protected Object _readMapAndClose(JsonParser jp, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/*      */     try
/*      */     {
/* 2125 */       JsonToken t = _initForReading(jp);
/*      */       Object result;
/*      */       Object result;
/* 2126 */       if ((t == JsonToken.VALUE_NULL) || (t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 2127 */         result = null;
/*      */       } else {
/* 2129 */         cfg = copyDeserializationConfig();
/* 2130 */         DeserializationContext ctxt = _createDeserializationContext(jp, cfg);
/* 2131 */         result = _findRootDeserializer(cfg, valueType).deserialize(jp, ctxt);
/*      */       }
/*      */ 
/* 2134 */       jp.clearCurrentToken();
/* 2135 */       DeserializationConfig cfg = result;
/*      */       return cfg;
/*      */     }
/*      */     finally
/*      */     {
/*      */       try
/*      */       {
/* 2138 */         jp.close(); } catch (IOException ioe) {
/*      */       }
/* 2139 */     }throw localObject1;
/*      */   }
/*      */ 
/*      */   protected JsonToken _initForReading(JsonParser jp)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2165 */     JsonToken t = jp.getCurrentToken();
/* 2166 */     if (t == null)
/*      */     {
/* 2168 */       t = jp.nextToken();
/* 2169 */       if (t == null)
/*      */       {
/* 2173 */         throw new EOFException("No content to map to Object due to end of input");
/*      */       }
/*      */     }
/* 2176 */     return t;
/*      */   }
/*      */ 
/*      */   protected JsonDeserializer<Object> _findRootDeserializer(DeserializationConfig cfg, JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/* 2192 */     JsonDeserializer deser = (JsonDeserializer)this._rootDeserializers.get(valueType);
/* 2193 */     if (deser != null) {
/* 2194 */       return deser;
/*      */     }
/*      */ 
/* 2197 */     deser = this._deserializerProvider.findTypedValueDeserializer(cfg, valueType, null);
/* 2198 */     if (deser == null) {
/* 2199 */       throw new JsonMappingException("Can not find a deserializer for type " + valueType);
/*      */     }
/* 2201 */     this._rootDeserializers.put(valueType, deser);
/* 2202 */     return deser;
/*      */   }
/*      */ 
/*      */   protected DeserializationContext _createDeserializationContext(JsonParser jp, DeserializationConfig cfg)
/*      */   {
/* 2208 */     return new StdDeserializationContext(cfg, jp, this._deserializerProvider);
/*      */   }
/*      */ 
/*      */   public static class DefaultTypeResolverBuilder extends StdTypeResolverBuilder
/*      */   {
/*      */     protected final ObjectMapper.DefaultTyping _appliesFor;
/*      */ 
/*      */     public DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping t)
/*      */     {
/*  129 */       this._appliesFor = t;
/*      */     }
/*      */ 
/*      */     public TypeDeserializer buildTypeDeserializer(JavaType baseType, Collection<NamedType> subtypes, BeanProperty property)
/*      */     {
/*  136 */       return useForType(baseType) ? super.buildTypeDeserializer(baseType, subtypes, property) : null;
/*      */     }
/*      */ 
/*      */     public TypeSerializer buildTypeSerializer(JavaType baseType, Collection<NamedType> subtypes, BeanProperty property)
/*      */     {
/*  143 */       return useForType(baseType) ? super.buildTypeSerializer(baseType, subtypes, property) : null;
/*      */     }
/*      */ 
/*      */     public boolean useForType(JavaType t)
/*      */     {
/*  156 */       switch (ObjectMapper.2.$SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping[this._appliesFor.ordinal()]) {
/*      */       case 1:
/*  158 */         if (!t.isArrayType()) break;
/*  159 */         t = t.getContentType();
/*      */       case 2:
/*  163 */         return (t.getRawClass() == Object.class) || (!t.isConcrete());
/*      */       case 3:
/*  165 */         if (t.isArrayType()) {
/*  166 */           t = t.getContentType();
/*      */         }
/*  168 */         return !t.isFinal();
/*      */       }
/*      */ 
/*  171 */       return t.getRawClass() == Object.class;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum DefaultTyping
/*      */   {
/*   87 */     JAVA_LANG_OBJECT, 
/*      */ 
/*   95 */     OBJECT_AND_NON_CONCRETE, 
/*      */ 
/*  102 */     NON_CONCRETE_AND_ARRAYS, 
/*      */ 
/*  111 */     NON_FINAL;
/*      */   }
/*      */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ObjectMapper
 * JD-Core Version:    0.6.0
 */