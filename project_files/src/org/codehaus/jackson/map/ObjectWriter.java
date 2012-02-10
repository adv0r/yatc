/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import org.codehaus.jackson.JsonEncoding;
/*     */ import org.codehaus.jackson.JsonFactory;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.PrettyPrinter;
/*     */ import org.codehaus.jackson.Version;
/*     */ import org.codehaus.jackson.Versioned;
/*     */ import org.codehaus.jackson.io.SegmentedStringWriter;
/*     */ import org.codehaus.jackson.map.introspect.VisibilityChecker;
/*     */ import org.codehaus.jackson.map.jsontype.SubtypeResolver;
/*     */ import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
/*     */ import org.codehaus.jackson.map.ser.FilterProvider;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ import org.codehaus.jackson.type.TypeReference;
/*     */ import org.codehaus.jackson.util.ByteArrayBuilder;
/*     */ import org.codehaus.jackson.util.DefaultPrettyPrinter;
/*     */ import org.codehaus.jackson.util.MinimalPrettyPrinter;
/*     */ import org.codehaus.jackson.util.VersionUtil;
/*     */ 
/*     */ public class ObjectWriter
/*     */   implements Versioned
/*     */ {
/*  38 */   protected static final PrettyPrinter NULL_PRETTY_PRINTER = new MinimalPrettyPrinter();
/*     */   protected final SerializationConfig _config;
/*     */   protected final SerializerProvider _provider;
/*     */   protected final SerializerFactory _serializerFactory;
/*     */   protected final JsonFactory _jsonFactory;
/*     */   protected final TypeResolverBuilder<?> _defaultTyper;
/*     */   protected final VisibilityChecker<?> _visibilityChecker;
/*     */   protected final SubtypeResolver _subtypeResolver;
/*     */   protected final Class<?> _serializationView;
/*     */   protected final JavaType _rootType;
/*     */   protected final PrettyPrinter _prettyPrinter;
/*     */ 
/*     */   protected ObjectWriter(ObjectMapper mapper, Class<?> view, JavaType rootType, PrettyPrinter pp)
/*     */   {
/* 110 */     this._defaultTyper = mapper._defaultTyper;
/* 111 */     this._visibilityChecker = mapper._visibilityChecker;
/* 112 */     this._subtypeResolver = mapper._subtypeResolver;
/*     */ 
/* 114 */     this._config = mapper._serializationConfig.createUnshared(this._defaultTyper, this._visibilityChecker, this._subtypeResolver, null);
/*     */ 
/* 116 */     this._config.setSerializationView(view);
/*     */ 
/* 118 */     this._provider = mapper._serializerProvider;
/* 119 */     this._serializerFactory = mapper._serializerFactory;
/*     */ 
/* 121 */     this._jsonFactory = mapper._jsonFactory;
/*     */ 
/* 123 */     this._serializationView = view;
/* 124 */     this._rootType = rootType;
/* 125 */     this._prettyPrinter = pp;
/*     */   }
/*     */ 
/*     */   protected ObjectWriter(ObjectMapper mapper, FilterProvider filterProvider)
/*     */   {
/* 136 */     this._defaultTyper = mapper._defaultTyper;
/* 137 */     this._visibilityChecker = mapper._visibilityChecker;
/* 138 */     this._subtypeResolver = mapper._subtypeResolver;
/*     */ 
/* 140 */     this._config = mapper._serializationConfig.createUnshared(this._defaultTyper, this._visibilityChecker, this._subtypeResolver, filterProvider);
/*     */ 
/* 142 */     this._provider = mapper._serializerProvider;
/* 143 */     this._serializerFactory = mapper._serializerFactory;
/* 144 */     this._jsonFactory = mapper._jsonFactory;
/* 145 */     this._serializationView = null;
/* 146 */     this._rootType = null;
/* 147 */     this._prettyPrinter = null;
/*     */   }
/*     */ 
/*     */   protected ObjectWriter(ObjectWriter base, SerializationConfig config, Class<?> view, JavaType rootType, PrettyPrinter pp)
/*     */   {
/* 156 */     this._config = config;
/* 157 */     this._provider = base._provider;
/* 158 */     this._serializerFactory = base._serializerFactory;
/*     */ 
/* 160 */     this._jsonFactory = base._jsonFactory;
/* 161 */     this._defaultTyper = base._defaultTyper;
/* 162 */     this._visibilityChecker = base._visibilityChecker;
/* 163 */     this._subtypeResolver = base._subtypeResolver;
/*     */ 
/* 165 */     this._serializationView = view;
/* 166 */     this._rootType = rootType;
/* 167 */     this._prettyPrinter = pp;
/*     */   }
/*     */ 
/*     */   protected ObjectWriter(ObjectWriter base, SerializationConfig config)
/*     */   {
/* 177 */     this._config = config;
/*     */ 
/* 179 */     this._provider = base._provider;
/* 180 */     this._serializerFactory = base._serializerFactory;
/*     */ 
/* 182 */     this._jsonFactory = base._jsonFactory;
/* 183 */     this._defaultTyper = base._defaultTyper;
/* 184 */     this._visibilityChecker = base._visibilityChecker;
/* 185 */     this._subtypeResolver = base._subtypeResolver;
/*     */ 
/* 187 */     this._serializationView = base._serializationView;
/* 188 */     this._rootType = base._rootType;
/* 189 */     this._prettyPrinter = base._prettyPrinter;
/*     */   }
/*     */ 
/*     */   public Version version()
/*     */   {
/* 200 */     return VersionUtil.versionFor(getClass());
/*     */   }
/*     */ 
/*     */   public ObjectWriter withView(Class<?> view)
/*     */   {
/* 216 */     if (view == this._serializationView) return this;
/*     */ 
/* 218 */     SerializationConfig config = this._config.createUnshared(this._defaultTyper, this._visibilityChecker, this._subtypeResolver);
/*     */ 
/* 220 */     config.setSerializationView(view);
/* 221 */     return new ObjectWriter(this, config);
/*     */   }
/*     */ 
/*     */   public ObjectWriter withType(JavaType rootType)
/*     */   {
/* 231 */     if (rootType == this._rootType) return this;
/*     */ 
/* 233 */     return new ObjectWriter(this, this._config, this._serializationView, rootType, this._prettyPrinter);
/*     */   }
/*     */ 
/*     */   public ObjectWriter withType(Class<?> rootType)
/*     */   {
/* 243 */     return withType(TypeFactory.type(rootType));
/*     */   }
/*     */ 
/*     */   public ObjectWriter withType(TypeReference<?> rootType)
/*     */   {
/* 251 */     return withType(TypeFactory.type(rootType));
/*     */   }
/*     */ 
/*     */   public ObjectWriter withPrettyPrinter(PrettyPrinter pp)
/*     */   {
/* 263 */     if (pp == null) {
/* 264 */       pp = NULL_PRETTY_PRINTER;
/*     */     }
/* 266 */     return new ObjectWriter(this, this._config, this._serializationView, this._rootType, pp);
/*     */   }
/*     */ 
/*     */   public ObjectWriter withDefaultPrettyPrinter()
/*     */   {
/* 277 */     return withPrettyPrinter(new DefaultPrettyPrinter());
/*     */   }
/*     */ 
/*     */   public ObjectWriter withFilters(FilterProvider filterProvider)
/*     */   {
/* 287 */     if (filterProvider == this._config.getFilterProvider()) {
/* 288 */       return this;
/*     */     }
/* 290 */     return new ObjectWriter(this, this._config.withFilters(filterProvider));
/*     */   }
/*     */ 
/*     */   public void writeValue(JsonGenerator jgen, Object value)
/*     */     throws IOException, JsonGenerationException, JsonMappingException
/*     */   {
/* 306 */     if ((this._config.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 307 */       _writeCloseableValue(jgen, value, this._config);
/*     */     } else {
/* 309 */       if (this._rootType == null)
/* 310 */         this._provider.serializeValue(this._config, jgen, value, this._serializerFactory);
/*     */       else {
/* 312 */         this._provider.serializeValue(this._config, jgen, value, this._rootType, this._serializerFactory);
/*     */       }
/* 314 */       if (this._config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE))
/* 315 */         jgen.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeValue(File resultFile, Object value)
/*     */     throws IOException, JsonGenerationException, JsonMappingException
/*     */   {
/* 333 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(resultFile, JsonEncoding.UTF8), value);
/*     */   }
/*     */ 
/*     */   public void writeValue(OutputStream out, Object value)
/*     */     throws IOException, JsonGenerationException, JsonMappingException
/*     */   {
/* 350 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8), value);
/*     */   }
/*     */ 
/*     */   public void writeValue(Writer w, Object value)
/*     */     throws IOException, JsonGenerationException, JsonMappingException
/*     */   {
/* 366 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(w), value);
/*     */   }
/*     */ 
/*     */   public String writeValueAsString(Object value)
/*     */     throws IOException, JsonGenerationException, JsonMappingException
/*     */   {
/* 379 */     SegmentedStringWriter sw = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());
/* 380 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(sw), value);
/* 381 */     return sw.getAndClear();
/*     */   }
/*     */ 
/*     */   public byte[] writeValueAsBytes(Object value)
/*     */     throws IOException, JsonGenerationException, JsonMappingException
/*     */   {
/* 394 */     ByteArrayBuilder bb = new ByteArrayBuilder(this._jsonFactory._getBufferRecycler());
/* 395 */     _configAndWriteValue(this._jsonFactory.createJsonGenerator(bb, JsonEncoding.UTF8), value);
/* 396 */     byte[] result = bb.toByteArray();
/* 397 */     bb.release();
/* 398 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean canSerialize(Class<?> type)
/*     */   {
/* 409 */     return this._provider.hasSerializerFor(this._config, type, this._serializerFactory);
/*     */   }
/*     */ 
/*     */   protected final void _configAndWriteValue(JsonGenerator jgen, Object value)
/*     */     throws IOException, JsonGenerationException, JsonMappingException
/*     */   {
/* 425 */     if (this._prettyPrinter != null) {
/* 426 */       PrettyPrinter pp = this._prettyPrinter;
/* 427 */       jgen.setPrettyPrinter(pp == NULL_PRETTY_PRINTER ? null : pp);
/* 428 */     } else if (this._config.isEnabled(SerializationConfig.Feature.INDENT_OUTPUT)) {
/* 429 */       jgen.useDefaultPrettyPrinter();
/*     */     }
/*     */ 
/* 432 */     if ((this._config.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 433 */       _configAndWriteCloseable(jgen, value, this._config);
/* 434 */       return;
/*     */     }
/* 436 */     boolean closed = false;
/*     */     try {
/* 438 */       if (this._rootType == null)
/* 439 */         this._provider.serializeValue(this._config, jgen, value, this._serializerFactory);
/*     */       else {
/* 441 */         this._provider.serializeValue(this._config, jgen, value, this._rootType, this._serializerFactory);
/*     */       }
/* 443 */       closed = true;
/* 444 */       jgen.close();
/*     */     }
/*     */     finally
/*     */     {
/* 449 */       if (!closed)
/*     */         try {
/* 451 */           jgen.close();
/*     */         }
/*     */         catch (IOException ioe)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void _configAndWriteCloseable(JsonGenerator jgen, Object value, SerializationConfig cfg)
/*     */     throws IOException, JsonGenerationException, JsonMappingException
/*     */   {
/* 464 */     Closeable toClose = (Closeable)value;
/*     */     try {
/* 466 */       if (this._rootType == null)
/* 467 */         this._provider.serializeValue(cfg, jgen, value, this._serializerFactory);
/*     */       else {
/* 469 */         this._provider.serializeValue(cfg, jgen, value, this._rootType, this._serializerFactory);
/*     */       }
/* 471 */       JsonGenerator tmpJgen = jgen;
/* 472 */       jgen = null;
/* 473 */       tmpJgen.close();
/* 474 */       Closeable tmpToClose = toClose;
/* 475 */       toClose = null;
/* 476 */       tmpToClose.close();
/*     */     }
/*     */     finally
/*     */     {
/* 481 */       if (jgen != null)
/*     */         try {
/* 483 */           jgen.close();
/*     */         } catch (IOException ioe) {
/*     */         }
/* 486 */       if (toClose != null)
/*     */         try {
/* 488 */           toClose.close();
/*     */         }
/*     */         catch (IOException ioe)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void _writeCloseableValue(JsonGenerator jgen, Object value, SerializationConfig cfg)
/*     */     throws IOException, JsonGenerationException, JsonMappingException
/*     */   {
/* 501 */     Closeable toClose = (Closeable)value;
/*     */     try {
/* 503 */       if (this._rootType == null)
/* 504 */         this._provider.serializeValue(cfg, jgen, value, this._serializerFactory);
/*     */       else {
/* 506 */         this._provider.serializeValue(cfg, jgen, value, this._rootType, this._serializerFactory);
/*     */       }
/* 508 */       if (this._config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
/* 509 */         jgen.flush();
/*     */       }
/* 511 */       Closeable tmpToClose = toClose;
/* 512 */       toClose = null;
/* 513 */       tmpToClose.close();
/*     */     } finally {
/* 515 */       if (toClose != null)
/*     */         try {
/* 517 */           toClose.close();
/*     */         }
/*     */         catch (IOException ioe)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ObjectWriter
 * JD-Core Version:    0.6.0
 */