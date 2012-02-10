/*     */ package org.codehaus.jackson;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.Writer;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.URL;
/*     */ import org.codehaus.jackson.impl.ByteSourceBootstrapper;
/*     */ import org.codehaus.jackson.impl.ReaderBasedParser;
/*     */ import org.codehaus.jackson.impl.Utf8Generator;
/*     */ import org.codehaus.jackson.impl.WriterBasedGenerator;
/*     */ import org.codehaus.jackson.io.IOContext;
/*     */ import org.codehaus.jackson.io.UTF8Writer;
/*     */ import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
/*     */ import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
/*     */ import org.codehaus.jackson.util.BufferRecycler;
/*     */ import org.codehaus.jackson.util.VersionUtil;
/*     */ 
/*     */ public class JsonFactory
/*     */   implements Versioned
/*     */ {
/*  58 */   static final int DEFAULT_PARSER_FEATURE_FLAGS = JsonParser.Feature.collectDefaults();
/*     */ 
/*  64 */   static final int DEFAULT_GENERATOR_FEATURE_FLAGS = JsonGenerator.Feature.collectDefaults();
/*     */ 
/*  77 */   protected static final ThreadLocal<SoftReference<BufferRecycler>> _recyclerRef = new ThreadLocal();
/*     */ 
/*  85 */   protected CharsToNameCanonicalizer _rootCharSymbols = CharsToNameCanonicalizer.createRoot();
/*     */ 
/*  94 */   protected BytesToNameCanonicalizer _rootByteSymbols = BytesToNameCanonicalizer.createRoot();
/*     */   protected ObjectCodec _objectCodec;
/* 111 */   protected int _parserFeatures = DEFAULT_PARSER_FEATURE_FLAGS;
/*     */ 
/* 113 */   protected int _generatorFeatures = DEFAULT_GENERATOR_FEATURE_FLAGS;
/*     */ 
/*     */   public JsonFactory()
/*     */   {
/* 125 */     this(null);
/*     */   }
/* 127 */   public JsonFactory(ObjectCodec oc) { this._objectCodec = oc;
/*     */   }
/*     */ 
/*     */   public Version version()
/*     */   {
/* 138 */     return VersionUtil.versionFor(Utf8Generator.class);
/*     */   }
/*     */ 
/*     */   public final JsonFactory configure(JsonParser.Feature f, boolean state)
/*     */   {
/* 155 */     if (state)
/* 156 */       enable(f);
/*     */     else {
/* 158 */       disable(f);
/*     */     }
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */   public JsonFactory enable(JsonParser.Feature f)
/*     */   {
/* 170 */     this._parserFeatures |= f.getMask();
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */   public JsonFactory disable(JsonParser.Feature f)
/*     */   {
/* 181 */     this._parserFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */   public final boolean isEnabled(JsonParser.Feature f)
/*     */   {
/* 191 */     return (this._parserFeatures & f.getMask()) != 0;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final void enableParserFeature(JsonParser.Feature f)
/*     */   {
/* 201 */     enable(f);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final void disableParserFeature(JsonParser.Feature f)
/*     */   {
/* 209 */     disable(f);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final void setParserFeature(JsonParser.Feature f, boolean state)
/*     */   {
/* 217 */     configure(f, state);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final boolean isParserFeatureEnabled(JsonParser.Feature f)
/*     */   {
/* 225 */     return (this._parserFeatures & f.getMask()) != 0;
/*     */   }
/*     */ 
/*     */   public final JsonFactory configure(JsonGenerator.Feature f, boolean state)
/*     */   {
/* 241 */     if (state)
/* 242 */       enable(f);
/*     */     else {
/* 244 */       disable(f);
/*     */     }
/* 246 */     return this;
/*     */   }
/*     */ 
/*     */   public JsonFactory enable(JsonGenerator.Feature f)
/*     */   {
/* 257 */     this._generatorFeatures |= f.getMask();
/* 258 */     return this;
/*     */   }
/*     */ 
/*     */   public JsonFactory disable(JsonGenerator.Feature f)
/*     */   {
/* 268 */     this._generatorFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/* 269 */     return this;
/*     */   }
/*     */ 
/*     */   public final boolean isEnabled(JsonGenerator.Feature f)
/*     */   {
/* 278 */     return (this._generatorFeatures & f.getMask()) != 0;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final void enableGeneratorFeature(JsonGenerator.Feature f)
/*     */   {
/* 288 */     enable(f);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final void disableGeneratorFeature(JsonGenerator.Feature f)
/*     */   {
/* 296 */     disable(f);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final void setGeneratorFeature(JsonGenerator.Feature f, boolean state)
/*     */   {
/* 304 */     configure(f, state);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final boolean isGeneratorFeatureEnabled(JsonGenerator.Feature f)
/*     */   {
/* 312 */     return isEnabled(f);
/*     */   }
/*     */ 
/*     */   public JsonFactory setCodec(ObjectCodec oc)
/*     */   {
/* 322 */     this._objectCodec = oc;
/* 323 */     return this;
/*     */   }
/*     */   public ObjectCodec getCodec() {
/* 326 */     return this._objectCodec;
/*     */   }
/*     */ 
/*     */   public JsonParser createJsonParser(File f)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 349 */     return _createJsonParser(new FileInputStream(f), _createContext(f, true));
/*     */   }
/*     */ 
/*     */   public JsonParser createJsonParser(URL url)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 368 */     return _createJsonParser(_optimizedStreamFromURL(url), _createContext(url, true));
/*     */   }
/*     */ 
/*     */   public JsonParser createJsonParser(InputStream in)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 389 */     return _createJsonParser(in, _createContext(in, false));
/*     */   }
/*     */ 
/*     */   public JsonParser createJsonParser(Reader r)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 408 */     return _createJsonParser(r, _createContext(r, false));
/*     */   }
/*     */ 
/*     */   public JsonParser createJsonParser(byte[] data)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 414 */     return _createJsonParser(data, 0, data.length, _createContext(data, true));
/*     */   }
/*     */ 
/*     */   public JsonParser createJsonParser(byte[] data, int offset, int len)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 420 */     return _createJsonParser(data, offset, len, _createContext(data, true));
/*     */   }
/*     */ 
/*     */   public JsonParser createJsonParser(String content)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 427 */     Reader r = new StringReader(content);
/* 428 */     return _createJsonParser(r, _createContext(r, true));
/*     */   }
/*     */ 
/*     */   public JsonGenerator createJsonGenerator(OutputStream out, JsonEncoding enc)
/*     */     throws IOException
/*     */   {
/* 458 */     IOContext ctxt = _createContext(out, false);
/* 459 */     ctxt.setEncoding(enc);
/* 460 */     if (enc == JsonEncoding.UTF8) {
/* 461 */       return _createUTF8JsonGenerator(out, ctxt);
/*     */     }
/*     */ 
/* 470 */     return _createJsonGenerator(_createWriter(out, enc, ctxt), ctxt);
/*     */   }
/*     */ 
/*     */   public JsonGenerator createJsonGenerator(Writer out)
/*     */     throws IOException
/*     */   {
/* 489 */     IOContext ctxt = _createContext(out, false);
/* 490 */     return _createJsonGenerator(out, ctxt);
/*     */   }
/*     */ 
/*     */   public JsonGenerator createJsonGenerator(File f, JsonEncoding enc)
/*     */     throws IOException
/*     */   {
/* 510 */     OutputStream out = new FileOutputStream(f);
/*     */ 
/* 512 */     IOContext ctxt = _createContext(out, true);
/* 513 */     ctxt.setEncoding(enc);
/* 514 */     if (enc == JsonEncoding.UTF8) {
/* 515 */       return _createUTF8JsonGenerator(out, ctxt);
/*     */     }
/* 517 */     return _createJsonGenerator(_createWriter(out, enc, ctxt), ctxt);
/*     */   }
/*     */ 
/*     */   protected IOContext _createContext(Object srcRef, boolean resourceManaged)
/*     */   {
/* 532 */     return new IOContext(_getBufferRecycler(), srcRef, resourceManaged);
/*     */   }
/*     */ 
/*     */   protected JsonParser _createJsonParser(InputStream in, IOContext ctxt)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 542 */     return new ByteSourceBootstrapper(ctxt, in).constructParser(this._parserFeatures, this._objectCodec, this._rootByteSymbols, this._rootCharSymbols);
/*     */   }
/*     */ 
/*     */   protected JsonParser _createJsonParser(Reader r, IOContext ctxt)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 552 */     return new ReaderBasedParser(ctxt, this._parserFeatures, r, this._objectCodec, this._rootCharSymbols.makeChild(isEnabled(JsonParser.Feature.CANONICALIZE_FIELD_NAMES), isEnabled(JsonParser.Feature.INTERN_FIELD_NAMES)));
/*     */   }
/*     */ 
/*     */   protected JsonParser _createJsonParser(byte[] data, int offset, int len, IOContext ctxt)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 565 */     return new ByteSourceBootstrapper(ctxt, data, offset, len).constructParser(this._parserFeatures, this._objectCodec, this._rootByteSymbols, this._rootCharSymbols);
/*     */   }
/*     */ 
/*     */   protected JsonGenerator _createJsonGenerator(Writer out, IOContext ctxt)
/*     */     throws IOException
/*     */   {
/* 575 */     return new WriterBasedGenerator(ctxt, this._generatorFeatures, this._objectCodec, out);
/*     */   }
/*     */ 
/*     */   protected JsonGenerator _createUTF8JsonGenerator(OutputStream out, IOContext ctxt)
/*     */     throws IOException
/*     */   {
/* 586 */     return new Utf8Generator(ctxt, this._generatorFeatures, this._objectCodec, out);
/*     */   }
/*     */ 
/*     */   public BufferRecycler _getBufferRecycler()
/*     */   {
/* 597 */     SoftReference ref = (SoftReference)_recyclerRef.get();
/* 598 */     BufferRecycler br = ref == null ? null : (BufferRecycler)ref.get();
/*     */ 
/* 600 */     if (br == null) {
/* 601 */       br = new BufferRecycler();
/* 602 */       _recyclerRef.set(new SoftReference(br));
/*     */     }
/* 604 */     return br;
/*     */   }
/*     */ 
/*     */   protected Writer _createWriter(OutputStream out, JsonEncoding enc, IOContext ctxt)
/*     */     throws IOException
/*     */   {
/* 610 */     if (enc == JsonEncoding.UTF8) {
/* 611 */       return new UTF8Writer(ctxt, out);
/*     */     }
/*     */ 
/* 614 */     return new OutputStreamWriter(out, enc.getJavaName());
/*     */   }
/*     */ 
/*     */   protected InputStream _optimizedStreamFromURL(URL url)
/*     */     throws IOException
/*     */   {
/* 631 */     if ("file".equals(url.getProtocol()))
/*     */     {
/* 638 */       String host = url.getHost();
/* 639 */       if ((host == null) || (host.length() == 0)) {
/* 640 */         return new FileInputStream(url.getPath());
/*     */       }
/*     */     }
/* 643 */     return url.openStream();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.JsonFactory
 * JD-Core Version:    0.6.0
 */