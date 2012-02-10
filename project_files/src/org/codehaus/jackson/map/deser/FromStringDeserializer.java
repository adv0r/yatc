/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Currency;
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Pattern;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ 
/*     */ public abstract class FromStringDeserializer<T> extends StdScalarDeserializer<T>
/*     */ {
/*     */   protected FromStringDeserializer(Class<?> vc)
/*     */   {
/*  22 */     super(vc);
/*     */   }
/*     */ 
/*     */   public static Iterable<FromStringDeserializer<?>> all()
/*     */   {
/*  27 */     ArrayList all = new ArrayList();
/*     */ 
/*  29 */     all.add(new UUIDDeserializer());
/*  30 */     all.add(new URLDeserializer());
/*  31 */     all.add(new URIDeserializer());
/*  32 */     all.add(new CurrencyDeserializer());
/*  33 */     all.add(new PatternDeserializer());
/*     */ 
/*  35 */     all.add(new LocaleDeserializer());
/*     */ 
/*  37 */     return all;
/*     */   }
/*     */ 
/*     */   public final T deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  45 */     if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
/*  46 */       String text = jp.getText().trim();
/*     */ 
/*  48 */       if (text.length() == 0)
/*  49 */         return null;
/*     */       try
/*     */       {
/*  52 */         Object result = _deserialize(text, ctxt);
/*  53 */         if (result != null)
/*  54 */           return result;
/*     */       }
/*     */       catch (IllegalArgumentException iae)
/*     */       {
/*     */       }
/*  59 */       throw ctxt.weirdStringException(this._valueClass, "not a valid textual representation");
/*     */     }
/*  61 */     if (jp.getCurrentToken() == JsonToken.VALUE_EMBEDDED_OBJECT)
/*     */     {
/*  63 */       Object ob = jp.getEmbeddedObject();
/*  64 */       if (ob == null) {
/*  65 */         return null;
/*     */       }
/*  67 */       if (this._valueClass.isAssignableFrom(ob.getClass())) {
/*  68 */         return ob;
/*     */       }
/*  70 */       return _deserializeEmbedded(ob, ctxt);
/*     */     }
/*  72 */     throw ctxt.mappingException(this._valueClass);
/*     */   }
/*     */ 
/*     */   protected abstract T _deserialize(String paramString, DeserializationContext paramDeserializationContext)
/*     */     throws IOException, JsonProcessingException;
/*     */ 
/*     */   protected T _deserializeEmbedded(Object ob, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  82 */     throw ctxt.mappingException("Don't know how to convert embedded Object of type " + ob.getClass().getName() + " into " + this._valueClass.getName());
/*     */   }
/*     */ 
/*     */   protected static class LocaleDeserializer extends FromStringDeserializer<Locale>
/*     */   {
/*     */     public LocaleDeserializer()
/*     */     {
/* 186 */       super();
/*     */     }
/*     */ 
/*     */     protected Locale _deserialize(String value, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 192 */       int ix = value.indexOf('_');
/* 193 */       if (ix < 0) {
/* 194 */         return new Locale(value);
/*     */       }
/* 196 */       String first = value.substring(0, ix);
/* 197 */       value = value.substring(ix + 1);
/* 198 */       ix = value.indexOf('_');
/* 199 */       if (ix < 0) {
/* 200 */         return new Locale(first, value);
/*     */       }
/* 202 */       String second = value.substring(0, ix);
/* 203 */       return new Locale(first, second, value.substring(ix + 1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class PatternDeserializer extends FromStringDeserializer<Pattern>
/*     */   {
/*     */     public PatternDeserializer()
/*     */     {
/* 167 */       super();
/*     */     }
/*     */ 
/*     */     protected Pattern _deserialize(String value, DeserializationContext ctxt)
/*     */       throws IllegalArgumentException
/*     */     {
/* 174 */       return Pattern.compile(value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class CurrencyDeserializer extends FromStringDeserializer<Currency>
/*     */   {
/*     */     public CurrencyDeserializer()
/*     */     {
/* 153 */       super();
/*     */     }
/*     */ 
/*     */     protected Currency _deserialize(String value, DeserializationContext ctxt)
/*     */       throws IllegalArgumentException
/*     */     {
/* 160 */       return Currency.getInstance(value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class URIDeserializer extends FromStringDeserializer<URI>
/*     */   {
/*     */     public URIDeserializer()
/*     */     {
/* 140 */       super();
/*     */     }
/*     */ 
/*     */     protected URI _deserialize(String value, DeserializationContext ctxt)
/*     */       throws IllegalArgumentException
/*     */     {
/* 146 */       return URI.create(value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class URLDeserializer extends FromStringDeserializer<URL>
/*     */   {
/*     */     public URLDeserializer()
/*     */     {
/* 127 */       super();
/*     */     }
/*     */ 
/*     */     protected URL _deserialize(String value, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 133 */       return new URL(value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class UUIDDeserializer extends FromStringDeserializer<UUID>
/*     */   {
/*     */     public UUIDDeserializer()
/*     */     {
/*  95 */       super();
/*     */     }
/*     */ 
/*     */     protected UUID _deserialize(String value, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 101 */       return UUID.fromString(value);
/*     */     }
/*     */ 
/*     */     protected UUID _deserializeEmbedded(Object ob, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 108 */       if ((ob instanceof byte[])) {
/* 109 */         byte[] bytes = (byte[])(byte[])ob;
/* 110 */         if (bytes.length != 16) {
/* 111 */           ctxt.mappingException("Can only construct UUIDs from 16 byte arrays; got " + bytes.length + " bytes");
/*     */         }
/*     */ 
/* 114 */         DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
/* 115 */         long l1 = in.readLong();
/* 116 */         long l2 = in.readLong();
/* 117 */         return new UUID(l1, l2);
/*     */       }
/* 119 */       super._deserializeEmbedded(ob, ctxt);
/* 120 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.FromStringDeserializer
 * JD-Core Version:    0.6.0
 */