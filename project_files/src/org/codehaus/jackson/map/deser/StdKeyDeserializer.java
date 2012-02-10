/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.KeyDeserializer;
/*     */ 
/*     */ public abstract class StdKeyDeserializer extends KeyDeserializer
/*     */ {
/*     */   protected final Class<?> _keyClass;
/*     */ 
/*     */   protected StdKeyDeserializer(Class<?> cls)
/*     */   {
/*  18 */     this._keyClass = cls;
/*     */   }
/*     */ 
/*     */   public final Object deserializeKey(String key, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  24 */     if (key == null)
/*  25 */       return null;
/*     */     try
/*     */     {
/*  28 */       Object result = _parse(key, ctxt);
/*  29 */       if (result != null)
/*  30 */         return result;
/*     */     }
/*     */     catch (Exception re) {
/*  33 */       throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation: " + re.getMessage());
/*     */     }
/*  35 */     throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation");
/*     */   }
/*     */   public Class<?> getKeyClass() {
/*  38 */     return this._keyClass;
/*     */   }
/*     */ 
/*     */   protected abstract Object _parse(String paramString, DeserializationContext paramDeserializationContext)
/*     */     throws Exception;
/*     */ 
/*     */   protected int _parseInt(String key)
/*     */     throws IllegalArgumentException
/*     */   {
/*  50 */     return Integer.parseInt(key);
/*     */   }
/*     */ 
/*     */   protected long _parseLong(String key) throws IllegalArgumentException
/*     */   {
/*  55 */     return Long.parseLong(key);
/*     */   }
/*     */ 
/*     */   protected double _parseDouble(String key) throws IllegalArgumentException
/*     */   {
/*  60 */     return Double.parseDouble(key);
/*     */   }
/*     */ 
/*     */   static final class StringFactoryKeyDeserializer extends StdKeyDeserializer
/*     */   {
/*     */     final Method _factoryMethod;
/*     */ 
/*     */     public StringFactoryKeyDeserializer(Method fm)
/*     */     {
/* 237 */       super();
/* 238 */       this._factoryMethod = fm;
/*     */     }
/*     */ 
/*     */     public Object _parse(String key, DeserializationContext ctxt)
/*     */       throws Exception
/*     */     {
/* 244 */       return this._factoryMethod.invoke(null, new Object[] { key });
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class StringCtorKeyDeserializer extends StdKeyDeserializer
/*     */   {
/*     */     final Constructor<?> _ctor;
/*     */ 
/*     */     public StringCtorKeyDeserializer(Constructor<?> ctor)
/*     */     {
/* 217 */       super();
/* 218 */       this._ctor = ctor;
/*     */     }
/*     */ 
/*     */     public Object _parse(String key, DeserializationContext ctxt)
/*     */       throws Exception
/*     */     {
/* 224 */       return this._ctor.newInstance(new Object[] { key });
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class EnumKD extends StdKeyDeserializer
/*     */   {
/*     */     final EnumResolver<?> _resolver;
/*     */ 
/*     */     EnumKD(EnumResolver<?> er)
/*     */     {
/* 193 */       super();
/* 194 */       this._resolver = er;
/*     */     }
/*     */ 
/*     */     public Enum<?> _parse(String key, DeserializationContext ctxt)
/*     */       throws JsonMappingException
/*     */     {
/* 200 */       Enum e = this._resolver.findEnum(key);
/* 201 */       if (e == null) {
/* 202 */         throw ctxt.weirdKeyException(this._keyClass, key, "not one of values for Enum class");
/*     */       }
/* 204 */       return e;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class FloatKD extends StdKeyDeserializer
/*     */   {
/*     */     FloatKD()
/*     */     {
/* 169 */       super();
/*     */     }
/*     */ 
/*     */     public Float _parse(String key, DeserializationContext ctxt)
/*     */       throws JsonMappingException
/*     */     {
/* 177 */       return Float.valueOf((float)_parseDouble(key));
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class DoubleKD extends StdKeyDeserializer
/*     */   {
/*     */     DoubleKD()
/*     */     {
/* 158 */       super();
/*     */     }
/*     */ 
/*     */     public Double _parse(String key, DeserializationContext ctxt) throws JsonMappingException
/*     */     {
/* 163 */       return Double.valueOf(_parseDouble(key));
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class LongKD extends StdKeyDeserializer
/*     */   {
/*     */     LongKD()
/*     */     {
/* 147 */       super();
/*     */     }
/*     */ 
/*     */     public Long _parse(String key, DeserializationContext ctxt) throws JsonMappingException
/*     */     {
/* 152 */       return Long.valueOf(_parseLong(key));
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class IntKD extends StdKeyDeserializer
/*     */   {
/*     */     IntKD()
/*     */     {
/* 136 */       super();
/*     */     }
/*     */ 
/*     */     public Integer _parse(String key, DeserializationContext ctxt) throws JsonMappingException
/*     */     {
/* 141 */       return Integer.valueOf(_parseInt(key));
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class CharKD extends StdKeyDeserializer
/*     */   {
/*     */     CharKD()
/*     */     {
/* 122 */       super();
/*     */     }
/*     */ 
/*     */     public Character _parse(String key, DeserializationContext ctxt) throws JsonMappingException
/*     */     {
/* 127 */       if (key.length() == 1) {
/* 128 */         return Character.valueOf(key.charAt(0));
/*     */       }
/* 130 */       throw ctxt.weirdKeyException(this._keyClass, key, "can only convert 1-character Strings");
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class ShortKD extends StdKeyDeserializer
/*     */   {
/*     */     ShortKD()
/*     */     {
/* 103 */       super();
/*     */     }
/*     */ 
/*     */     public Short _parse(String key, DeserializationContext ctxt) throws JsonMappingException
/*     */     {
/* 108 */       int value = _parseInt(key);
/* 109 */       if ((value < -32768) || (value > 32767)) {
/* 110 */         throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 16-bit value");
/*     */       }
/* 112 */       return Short.valueOf((short)value);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class ByteKD extends StdKeyDeserializer
/*     */   {
/*     */     ByteKD()
/*     */     {
/*  88 */       super();
/*     */     }
/*     */ 
/*     */     public Byte _parse(String key, DeserializationContext ctxt) throws JsonMappingException
/*     */     {
/*  93 */       int value = _parseInt(key);
/*  94 */       if ((value < -128) || (value > 127)) {
/*  95 */         throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 8-bit value");
/*     */       }
/*  97 */       return Byte.valueOf((byte)value);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class BoolKD extends StdKeyDeserializer
/*     */   {
/*     */     BoolKD()
/*     */     {
/*  71 */       super();
/*     */     }
/*     */ 
/*     */     public Boolean _parse(String key, DeserializationContext ctxt) throws JsonMappingException
/*     */     {
/*  76 */       if ("true".equals(key)) {
/*  77 */         return Boolean.TRUE;
/*     */       }
/*  79 */       if ("false".equals(key)) {
/*  80 */         return Boolean.FALSE;
/*     */       }
/*  82 */       throw ctxt.weirdKeyException(this._keyClass, key, "value not 'true' or 'false'");
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdKeyDeserializer
 * JD-Core Version:    0.6.0
 */