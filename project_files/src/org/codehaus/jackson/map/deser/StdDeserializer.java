/*      */ package org.codehaus.jackson.map.deser;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.Calendar;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import org.codehaus.jackson.Base64Variant;
/*      */ import org.codehaus.jackson.Base64Variants;
/*      */ import org.codehaus.jackson.JsonParser;
/*      */ import org.codehaus.jackson.JsonParser.NumberType;
/*      */ import org.codehaus.jackson.JsonProcessingException;
/*      */ import org.codehaus.jackson.JsonToken;
/*      */ import org.codehaus.jackson.io.NumberInput;
/*      */ import org.codehaus.jackson.map.BeanProperty;
/*      */ import org.codehaus.jackson.map.DeserializationConfig;
/*      */ import org.codehaus.jackson.map.DeserializationConfig.Feature;
/*      */ import org.codehaus.jackson.map.DeserializationContext;
/*      */ import org.codehaus.jackson.map.DeserializerProvider;
/*      */ import org.codehaus.jackson.map.JsonDeserializer;
/*      */ import org.codehaus.jackson.map.JsonMappingException;
/*      */ import org.codehaus.jackson.map.ResolvableDeserializer;
/*      */ import org.codehaus.jackson.map.TypeDeserializer;
/*      */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*      */ import org.codehaus.jackson.map.type.TypeFactory;
/*      */ import org.codehaus.jackson.type.JavaType;
/*      */ import org.codehaus.jackson.util.TokenBuffer;
/*      */ 
/*      */ public abstract class StdDeserializer<T> extends JsonDeserializer<T>
/*      */ {
/*      */   protected final Class<?> _valueClass;
/*      */ 
/*      */   protected StdDeserializer(Class<?> vc)
/*      */   {
/*   38 */     this._valueClass = vc;
/*      */   }
/*      */ 
/*      */   protected StdDeserializer(JavaType valueType)
/*      */   {
/*   45 */     this._valueClass = (valueType == null ? null : valueType.getRawClass());
/*      */   }
/*      */ 
/*      */   public Class<?> getValueClass()
/*      */   {
/*   54 */     return this._valueClass;
/*      */   }
/*      */ 
/*      */   public JavaType getValueType()
/*      */   {
/*   61 */     return null;
/*      */   }
/*      */ 
/*      */   protected boolean isDefaultSerializer(JsonDeserializer<?> deserializer)
/*      */   {
/*   73 */     return (deserializer != null) && (deserializer.getClass().getAnnotation(JacksonStdImpl.class) != null);
/*      */   }
/*      */ 
/*      */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*   92 */     return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
/*      */   }
/*      */ 
/*      */   protected final boolean _parseBooleanPrimitive(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  104 */     JsonToken t = jp.getCurrentToken();
/*  105 */     if (t == JsonToken.VALUE_TRUE) {
/*  106 */       return true;
/*      */     }
/*  108 */     if (t == JsonToken.VALUE_FALSE) {
/*  109 */       return false;
/*      */     }
/*  111 */     if (t == JsonToken.VALUE_NULL) {
/*  112 */       return false;
/*      */     }
/*      */ 
/*  115 */     if (t == JsonToken.VALUE_NUMBER_INT) {
/*  116 */       return jp.getIntValue() != 0;
/*      */     }
/*      */ 
/*  119 */     if (t == JsonToken.VALUE_STRING) {
/*  120 */       String text = jp.getText().trim();
/*  121 */       if ("true".equals(text)) {
/*  122 */         return true;
/*      */       }
/*  124 */       if (("false".equals(text)) || (text.length() == 0)) {
/*  125 */         return Boolean.FALSE.booleanValue();
/*      */       }
/*  127 */       throw ctxt.weirdStringException(this._valueClass, "only \"true\" or \"false\" recognized");
/*      */     }
/*      */ 
/*  130 */     throw ctxt.mappingException(this._valueClass);
/*      */   }
/*      */ 
/*      */   protected final Boolean _parseBoolean(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  136 */     JsonToken t = jp.getCurrentToken();
/*  137 */     if (t == JsonToken.VALUE_TRUE) {
/*  138 */       return Boolean.TRUE;
/*      */     }
/*  140 */     if (t == JsonToken.VALUE_FALSE) {
/*  141 */       return Boolean.FALSE;
/*      */     }
/*  143 */     if (t == JsonToken.VALUE_NULL) {
/*  144 */       return null;
/*      */     }
/*      */ 
/*  147 */     if (t == JsonToken.VALUE_NUMBER_INT) {
/*  148 */       return jp.getIntValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
/*      */     }
/*      */ 
/*  151 */     if (t == JsonToken.VALUE_STRING) {
/*  152 */       String text = jp.getText().trim();
/*  153 */       if ("true".equals(text)) {
/*  154 */         return Boolean.TRUE;
/*      */       }
/*  156 */       if (("false".equals(text)) || (text.length() == 0)) {
/*  157 */         return Boolean.FALSE;
/*      */       }
/*  159 */       throw ctxt.weirdStringException(this._valueClass, "only \"true\" or \"false\" recognized");
/*      */     }
/*      */ 
/*  162 */     throw ctxt.mappingException(this._valueClass);
/*      */   }
/*      */ 
/*      */   protected final Short _parseShort(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  168 */     JsonToken t = jp.getCurrentToken();
/*  169 */     if (t == JsonToken.VALUE_NULL) {
/*  170 */       return null;
/*      */     }
/*  172 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  173 */       return Short.valueOf(jp.getShortValue());
/*      */     }
/*  175 */     int value = _parseIntPrimitive(jp, ctxt);
/*      */ 
/*  177 */     if ((value < -32768) || (value > 32767)) {
/*  178 */       throw ctxt.weirdStringException(this._valueClass, "overflow, value can not be represented as 16-bit value");
/*      */     }
/*  180 */     return Short.valueOf((short)value);
/*      */   }
/*      */ 
/*      */   protected final short _parseShortPrimitive(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  186 */     int value = _parseIntPrimitive(jp, ctxt);
/*      */ 
/*  188 */     if ((value < -32768) || (value > 32767)) {
/*  189 */       throw ctxt.weirdStringException(this._valueClass, "overflow, value can not be represented as 16-bit value");
/*      */     }
/*  191 */     return (short)value;
/*      */   }
/*      */ 
/*      */   protected final int _parseIntPrimitive(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  197 */     JsonToken t = jp.getCurrentToken();
/*      */ 
/*  200 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  201 */       return jp.getIntValue();
/*      */     }
/*  203 */     if (t == JsonToken.VALUE_STRING)
/*      */     {
/*  207 */       String text = jp.getText().trim();
/*      */       try {
/*  209 */         int len = text.length();
/*  210 */         if (len > 9) {
/*  211 */           long l = Long.parseLong(text);
/*  212 */           if ((l < -2147483648L) || (l > 2147483647L)) {
/*  213 */             throw ctxt.weirdStringException(this._valueClass, "Overflow: numeric value (" + text + ") out of range of int (" + -2147483648 + " - " + 2147483647 + ")");
/*      */           }
/*      */ 
/*  216 */           return (int)l;
/*      */         }
/*  218 */         if (len == 0) {
/*  219 */           return 0;
/*      */         }
/*  221 */         return NumberInput.parseInt(text);
/*      */       } catch (IllegalArgumentException iae) {
/*  223 */         throw ctxt.weirdStringException(this._valueClass, "not a valid int value");
/*      */       }
/*      */     }
/*  226 */     if (t == JsonToken.VALUE_NULL) {
/*  227 */       return 0;
/*      */     }
/*      */ 
/*  230 */     throw ctxt.mappingException(this._valueClass);
/*      */   }
/*      */ 
/*      */   protected final Integer _parseInteger(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  236 */     JsonToken t = jp.getCurrentToken();
/*  237 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  238 */       return Integer.valueOf(jp.getIntValue());
/*      */     }
/*  240 */     if (t == JsonToken.VALUE_STRING) {
/*  241 */       String text = jp.getText().trim();
/*      */       try {
/*  243 */         int len = text.length();
/*  244 */         if (len > 9) {
/*  245 */           long l = Long.parseLong(text);
/*  246 */           if ((l < -2147483648L) || (l > 2147483647L)) {
/*  247 */             throw ctxt.weirdStringException(this._valueClass, "Overflow: numeric value (" + text + ") out of range of Integer (" + -2147483648 + " - " + 2147483647 + ")");
/*      */           }
/*      */ 
/*  250 */           return Integer.valueOf((int)l);
/*      */         }
/*  252 */         if (len == 0) {
/*  253 */           return null;
/*      */         }
/*  255 */         return Integer.valueOf(NumberInput.parseInt(text));
/*      */       } catch (IllegalArgumentException iae) {
/*  257 */         throw ctxt.weirdStringException(this._valueClass, "not a valid Integer value");
/*      */       }
/*      */     }
/*  260 */     if (t == JsonToken.VALUE_NULL) {
/*  261 */       return null;
/*      */     }
/*      */ 
/*  264 */     throw ctxt.mappingException(this._valueClass);
/*      */   }
/*      */ 
/*      */   protected final Long _parseLong(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  270 */     JsonToken t = jp.getCurrentToken();
/*      */ 
/*  273 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  274 */       return Long.valueOf(jp.getLongValue());
/*      */     }
/*      */ 
/*  277 */     if (t == JsonToken.VALUE_STRING)
/*      */     {
/*  279 */       String text = jp.getText().trim();
/*  280 */       if (text.length() == 0)
/*  281 */         return null;
/*      */       try
/*      */       {
/*  284 */         return Long.valueOf(NumberInput.parseLong(text));
/*      */       } catch (IllegalArgumentException iae) {
/*  286 */         throw ctxt.weirdStringException(this._valueClass, "not a valid Long value");
/*      */       }
/*      */     }
/*  288 */     if (t == JsonToken.VALUE_NULL) {
/*  289 */       return null;
/*      */     }
/*      */ 
/*  292 */     throw ctxt.mappingException(this._valueClass);
/*      */   }
/*      */ 
/*      */   protected final long _parseLongPrimitive(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  298 */     JsonToken t = jp.getCurrentToken();
/*  299 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  300 */       return jp.getLongValue();
/*      */     }
/*  302 */     if (t == JsonToken.VALUE_STRING) {
/*  303 */       String text = jp.getText().trim();
/*  304 */       if (text.length() == 0)
/*  305 */         return 0L;
/*      */       try
/*      */       {
/*  308 */         return NumberInput.parseLong(text);
/*      */       } catch (IllegalArgumentException iae) {
/*  310 */         throw ctxt.weirdStringException(this._valueClass, "not a valid long value");
/*      */       }
/*      */     }
/*  312 */     if (t == JsonToken.VALUE_NULL) {
/*  313 */       return 0L;
/*      */     }
/*  315 */     throw ctxt.mappingException(this._valueClass);
/*      */   }
/*      */ 
/*      */   protected final Float _parseFloat(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  322 */     JsonToken t = jp.getCurrentToken();
/*      */ 
/*  324 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  325 */       return Float.valueOf(jp.getFloatValue());
/*      */     }
/*      */ 
/*  328 */     if (t == JsonToken.VALUE_STRING) {
/*  329 */       String text = jp.getText().trim();
/*  330 */       if (text.length() == 0) {
/*  331 */         return null;
/*      */       }
/*  333 */       switch (text.charAt(0)) {
/*      */       case 'I':
/*  335 */         if ((!"Infinity".equals(text)) && (!"INF".equals(text))) break;
/*  336 */         return Float.valueOf((1.0F / 1.0F));
/*      */       case 'N':
/*  340 */         if (!"NaN".equals(text)) break;
/*  341 */         return Float.valueOf((0.0F / 0.0F));
/*      */       case '-':
/*  345 */         if ((!"-Infinity".equals(text)) && (!"-INF".equals(text))) break;
/*  346 */         return Float.valueOf((1.0F / -1.0F));
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  351 */         return Float.valueOf(Float.parseFloat(text));
/*      */       } catch (IllegalArgumentException iae) {
/*  353 */         throw ctxt.weirdStringException(this._valueClass, "not a valid Float value");
/*      */       }
/*      */     }
/*  355 */     if (t == JsonToken.VALUE_NULL) {
/*  356 */       return null;
/*      */     }
/*      */ 
/*  359 */     throw ctxt.mappingException(this._valueClass);
/*      */   }
/*      */ 
/*      */   protected final float _parseFloatPrimitive(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  365 */     JsonToken t = jp.getCurrentToken();
/*      */ 
/*  367 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  368 */       return jp.getFloatValue();
/*      */     }
/*  370 */     if (t == JsonToken.VALUE_STRING) {
/*  371 */       String text = jp.getText().trim();
/*  372 */       if (text.length() == 0) {
/*  373 */         return 0.0F;
/*      */       }
/*  375 */       switch (text.charAt(0)) {
/*      */       case 'I':
/*  377 */         if ((!"Infinity".equals(text)) && (!"INF".equals(text))) break;
/*  378 */         return (1.0F / 1.0F);
/*      */       case 'N':
/*  382 */         if (!"NaN".equals(text)) break;
/*  383 */         return (0.0F / 0.0F);
/*      */       case '-':
/*  387 */         if ((!"-Infinity".equals(text)) && (!"-INF".equals(text))) break;
/*  388 */         return (1.0F / -1.0F);
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  393 */         return Float.parseFloat(text);
/*      */       } catch (IllegalArgumentException iae) {
/*  395 */         throw ctxt.weirdStringException(this._valueClass, "not a valid float value");
/*      */       }
/*      */     }
/*  397 */     if (t == JsonToken.VALUE_NULL) {
/*  398 */       return 0.0F;
/*      */     }
/*      */ 
/*  401 */     throw ctxt.mappingException(this._valueClass);
/*      */   }
/*      */ 
/*      */   protected final Double _parseDouble(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  407 */     JsonToken t = jp.getCurrentToken();
/*      */ 
/*  409 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  410 */       return Double.valueOf(jp.getDoubleValue());
/*      */     }
/*  412 */     if (t == JsonToken.VALUE_STRING) {
/*  413 */       String text = jp.getText().trim();
/*  414 */       if (text.length() == 0) {
/*  415 */         return null;
/*      */       }
/*  417 */       switch (text.charAt(0)) {
/*      */       case 'I':
/*  419 */         if ((!"Infinity".equals(text)) && (!"INF".equals(text))) break;
/*  420 */         return Double.valueOf((1.0D / 0.0D));
/*      */       case 'N':
/*  424 */         if (!"NaN".equals(text)) break;
/*  425 */         return Double.valueOf((0.0D / 0.0D));
/*      */       case '-':
/*  429 */         if ((!"-Infinity".equals(text)) && (!"-INF".equals(text))) break;
/*  430 */         return Double.valueOf((-1.0D / 0.0D));
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  435 */         return Double.valueOf(Double.parseDouble(text));
/*      */       } catch (IllegalArgumentException iae) {
/*  437 */         throw ctxt.weirdStringException(this._valueClass, "not a valid Double value");
/*      */       }
/*      */     }
/*  439 */     if (t == JsonToken.VALUE_NULL) {
/*  440 */       return null;
/*      */     }
/*      */ 
/*  443 */     throw ctxt.mappingException(this._valueClass);
/*      */   }
/*      */ 
/*      */   protected final double _parseDoublePrimitive(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  450 */     JsonToken t = jp.getCurrentToken();
/*      */ 
/*  452 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  453 */       return jp.getDoubleValue();
/*      */     }
/*      */ 
/*  456 */     if (t == JsonToken.VALUE_STRING) {
/*  457 */       String text = jp.getText().trim();
/*  458 */       if (text.length() == 0) {
/*  459 */         return 0.0D;
/*      */       }
/*  461 */       switch (text.charAt(0)) {
/*      */       case 'I':
/*  463 */         if ((!"Infinity".equals(text)) && (!"INF".equals(text))) break;
/*  464 */         return (1.0D / 0.0D);
/*      */       case 'N':
/*  468 */         if (!"NaN".equals(text)) break;
/*  469 */         return (0.0D / 0.0D);
/*      */       case '-':
/*  473 */         if ((!"-Infinity".equals(text)) && (!"-INF".equals(text))) break;
/*  474 */         return (-1.0D / 0.0D);
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  479 */         return Double.parseDouble(text);
/*      */       } catch (IllegalArgumentException iae) {
/*  481 */         throw ctxt.weirdStringException(this._valueClass, "not a valid double value");
/*      */       }
/*      */     }
/*  483 */     if (t == JsonToken.VALUE_NULL) {
/*  484 */       return 0.0D;
/*      */     }
/*      */ 
/*  487 */     throw ctxt.mappingException(this._valueClass);
/*      */   }
/*      */ 
/*      */   protected java.util.Date _parseDate(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  494 */     JsonToken t = jp.getCurrentToken();
/*      */     try {
/*  496 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/*  497 */         return new java.util.Date(jp.getLongValue());
/*      */       }
/*  499 */       if (t == JsonToken.VALUE_STRING)
/*      */       {
/*  503 */         String str = jp.getText().trim();
/*  504 */         if (str.length() == 0) {
/*  505 */           return null;
/*      */         }
/*  507 */         return ctxt.parseDate(str);
/*      */       }
/*  509 */       throw ctxt.mappingException(this._valueClass); } catch (IllegalArgumentException iae) {
/*      */     }
/*  511 */     throw ctxt.weirdStringException(this._valueClass, "not a valid representation (error: " + iae.getMessage() + ")");
/*      */   }
/*      */ 
/*      */   protected JsonDeserializer<Object> findDeserializer(DeserializationConfig config, DeserializerProvider provider, JavaType type, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  536 */     JsonDeserializer deser = provider.findValueDeserializer(config, type, property);
/*  537 */     return deser;
/*      */   }
/*      */ 
/*      */   protected void handleUnknownProperty(JsonParser jp, DeserializationContext ctxt, Object instanceOrClass, String propName)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  567 */     if (instanceOrClass == null) {
/*  568 */       instanceOrClass = getValueClass();
/*      */     }
/*      */ 
/*  571 */     if (ctxt.handleUnknownProperty(jp, this, instanceOrClass, propName)) {
/*  572 */       return;
/*      */     }
/*      */ 
/*  575 */     reportUnknownProperty(ctxt, instanceOrClass, propName);
/*      */ 
/*  580 */     jp.skipChildren();
/*      */   }
/*      */ 
/*      */   protected void reportUnknownProperty(DeserializationContext ctxt, Object instanceOrClass, String fieldName)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  588 */     if (ctxt.isEnabled(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES))
/*  589 */       throw ctxt.unknownFieldException(instanceOrClass, fieldName);
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static class TokenBufferDeserializer extends StdScalarDeserializer<TokenBuffer>
/*      */   {
/*      */     public TokenBufferDeserializer()
/*      */     {
/* 1246 */       super();
/*      */     }
/*      */ 
/*      */     public TokenBuffer deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/* 1252 */       TokenBuffer tb = new TokenBuffer(jp.getCodec());
/*      */ 
/* 1254 */       tb.copyCurrentStructure(jp);
/* 1255 */       return tb;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class StackTraceElementDeserializer extends StdScalarDeserializer<StackTraceElement>
/*      */   {
/*      */     public StackTraceElementDeserializer()
/*      */     {
/* 1194 */       super();
/*      */     }
/*      */ 
/*      */     public StackTraceElement deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/* 1200 */       JsonToken t = jp.getCurrentToken();
/*      */ 
/* 1202 */       if (t == JsonToken.START_OBJECT) {
/* 1203 */         String className = ""; String methodName = ""; String fileName = "";
/* 1204 */         int lineNumber = -1;
/*      */ 
/* 1206 */         while ((t = jp.nextValue()) != JsonToken.END_OBJECT) {
/* 1207 */           String propName = jp.getCurrentName();
/* 1208 */           if ("className".equals(propName))
/* 1209 */             className = jp.getText();
/* 1210 */           else if ("fileName".equals(propName))
/* 1211 */             fileName = jp.getText();
/* 1212 */           else if ("lineNumber".equals(propName)) {
/* 1213 */             if (t.isNumeric())
/* 1214 */               lineNumber = jp.getIntValue();
/*      */             else
/* 1216 */               throw JsonMappingException.from(jp, "Non-numeric token (" + t + ") for property 'lineNumber'");
/*      */           }
/* 1218 */           else if ("methodName".equals(propName))
/* 1219 */             methodName = jp.getText();
/* 1220 */           else if (!"nativeMethod".equals(propName))
/*      */           {
/* 1223 */             handleUnknownProperty(jp, ctxt, this._valueClass, propName);
/*      */           }
/*      */         }
/* 1226 */         return new StackTraceElement(className, methodName, fileName, lineNumber);
/*      */       }
/* 1228 */       throw ctxt.mappingException(this._valueClass);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class SqlDateDeserializer extends StdScalarDeserializer<java.sql.Date>
/*      */   {
/*      */     public SqlDateDeserializer()
/*      */     {
/* 1174 */       super();
/*      */     }
/*      */ 
/*      */     public java.sql.Date deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/* 1180 */       java.util.Date d = _parseDate(jp, ctxt);
/* 1181 */       return d == null ? null : new java.sql.Date(d.getTime());
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static class CalendarDeserializer extends StdScalarDeserializer<Calendar>
/*      */   {
/*      */     Class<? extends Calendar> _calendarClass;
/*      */ 
/*      */     public CalendarDeserializer()
/*      */     {
/* 1140 */       this(null);
/*      */     }
/* 1142 */     public CalendarDeserializer(Class<? extends Calendar> cc) { super();
/* 1143 */       this._calendarClass = cc;
/*      */     }
/*      */ 
/*      */     public Calendar deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/* 1150 */       java.util.Date d = _parseDate(jp, ctxt);
/* 1151 */       if (d == null) {
/* 1152 */         return null;
/*      */       }
/* 1154 */       if (this._calendarClass == null)
/* 1155 */         return ctxt.constructCalendar(d);
/*      */       try
/*      */       {
/* 1158 */         Calendar c = (Calendar)this._calendarClass.newInstance();
/* 1159 */         c.setTimeInMillis(d.getTime());
/* 1160 */         return c; } catch (Exception e) {
/*      */       }
/* 1162 */       throw ctxt.instantiationException(this._calendarClass, e);
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static class BigIntegerDeserializer extends StdScalarDeserializer<BigInteger>
/*      */   {
/*      */     public BigIntegerDeserializer()
/*      */     {
/* 1088 */       super();
/*      */     }
/*      */ 
/*      */     public BigInteger deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/* 1094 */       JsonToken t = jp.getCurrentToken();
/*      */ 
/* 1097 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 1098 */         switch (StdDeserializer.1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
/*      */         case 1:
/*      */         case 2:
/* 1101 */           return BigInteger.valueOf(jp.getLongValue());
/*      */         }
/*      */       } else {
/* 1103 */         if (t == JsonToken.VALUE_NUMBER_FLOAT)
/*      */         {
/* 1107 */           return jp.getDecimalValue().toBigInteger();
/* 1108 */         }if (t != JsonToken.VALUE_STRING)
/*      */         {
/* 1110 */           throw ctxt.mappingException(this._valueClass);
/*      */         }
/*      */       }
/* 1112 */       String text = jp.getText().trim();
/* 1113 */       if (text.length() == 0)
/* 1114 */         return null;
/*      */       try
/*      */       {
/* 1117 */         return new BigInteger(text); } catch (IllegalArgumentException iae) {
/*      */       }
/* 1119 */       throw ctxt.weirdStringException(this._valueClass, "not a valid representation");
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static class BigDecimalDeserializer extends StdScalarDeserializer<BigDecimal>
/*      */   {
/*      */     public BigDecimalDeserializer()
/*      */     {
/* 1053 */       super();
/*      */     }
/*      */ 
/*      */     public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/* 1059 */       JsonToken t = jp.getCurrentToken();
/* 1060 */       if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 1061 */         return jp.getDecimalValue();
/*      */       }
/*      */ 
/* 1064 */       if (t == JsonToken.VALUE_STRING) {
/* 1065 */         String text = jp.getText().trim();
/* 1066 */         if (text.length() == 0)
/* 1067 */           return null;
/*      */         try
/*      */         {
/* 1070 */           return new BigDecimal(text);
/*      */         } catch (IllegalArgumentException iae) {
/* 1072 */           throw ctxt.weirdStringException(this._valueClass, "not a valid representation");
/*      */         }
/*      */       }
/*      */ 
/* 1076 */       throw ctxt.mappingException(this._valueClass);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class AtomicReferenceDeserializer extends StdScalarDeserializer<AtomicReference<?>>
/*      */     implements ResolvableDeserializer
/*      */   {
/*      */     protected final JavaType _referencedType;
/*      */     protected final BeanProperty _property;
/*      */     protected JsonDeserializer<?> _valueDeserializer;
/*      */ 
/*      */     public AtomicReferenceDeserializer(JavaType type, BeanProperty property)
/*      */     {
/* 1017 */       super();
/* 1018 */       JavaType[] refTypes = TypeFactory.findParameterTypes(type, AtomicReference.class);
/* 1019 */       if (refTypes == null)
/* 1020 */         this._referencedType = TypeFactory.type(Object.class);
/*      */       else {
/* 1022 */         this._referencedType = refTypes[0];
/*      */       }
/* 1024 */       this._property = property;
/*      */     }
/*      */ 
/*      */     public AtomicReference<?> deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/* 1031 */       return new AtomicReference(this._valueDeserializer.deserialize(jp, ctxt));
/*      */     }
/*      */ 
/*      */     public void resolve(DeserializationConfig config, DeserializerProvider provider)
/*      */       throws JsonMappingException
/*      */     {
/* 1038 */       this._valueDeserializer = provider.findValueDeserializer(config, this._referencedType, this._property);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class AtomicBooleanDeserializer extends StdScalarDeserializer<AtomicBoolean>
/*      */   {
/*      */     public AtomicBooleanDeserializer()
/*      */     {
/*  988 */       super();
/*      */     }
/*      */ 
/*      */     public AtomicBoolean deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  995 */       return new AtomicBoolean(_parseBooleanPrimitive(jp, ctxt));
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static final class NumberDeserializer extends StdScalarDeserializer<Number>
/*      */   {
/*      */     public NumberDeserializer()
/*      */     {
/*  902 */       super();
/*      */     }
/*      */ 
/*      */     public Number deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  908 */       JsonToken t = jp.getCurrentToken();
/*  909 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/*  910 */         if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS)) {
/*  911 */           return jp.getBigIntegerValue();
/*      */         }
/*  913 */         return jp.getNumberValue();
/*  914 */       }if (t == JsonToken.VALUE_NUMBER_FLOAT)
/*      */       {
/*  918 */         if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/*  919 */           return jp.getDecimalValue();
/*      */         }
/*  921 */         return Double.valueOf(jp.getDoubleValue());
/*      */       }
/*      */ 
/*  927 */       if (t == JsonToken.VALUE_STRING) {
/*  928 */         String text = jp.getText().trim();
/*      */         try {
/*  930 */           if (text.indexOf('.') >= 0)
/*      */           {
/*  932 */             if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/*  933 */               return new BigDecimal(text);
/*      */             }
/*  935 */             return new Double(text);
/*      */           }
/*      */ 
/*  938 */           if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS)) {
/*  939 */             return new BigInteger(text);
/*      */           }
/*  941 */           long value = Long.parseLong(text);
/*  942 */           if ((value <= 2147483647L) && (value >= -2147483648L)) {
/*  943 */             return Integer.valueOf((int)value);
/*      */           }
/*  945 */           return Long.valueOf(value);
/*      */         } catch (IllegalArgumentException iae) {
/*  947 */           throw ctxt.weirdStringException(this._valueClass, "not a valid number");
/*      */         }
/*      */       }
/*      */ 
/*  951 */       throw ctxt.mappingException(this._valueClass);
/*      */     }
/*      */ 
/*      */     public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  965 */       switch (StdDeserializer.1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()])
/*      */       {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*  970 */         return deserialize(jp, ctxt);
/*      */       }
/*  972 */       return typeDeserializer.deserializeTypedFromScalar(jp, ctxt);
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static final class DoubleDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Double>
/*      */   {
/*      */     public DoubleDeserializer(Class<Double> cls, Double nvl)
/*      */     {
/*  867 */       super(nvl);
/*      */     }
/*      */ 
/*      */     public Double deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  874 */       return _parseDouble(jp, ctxt);
/*      */     }
/*      */ 
/*      */     public Double deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  884 */       return _parseDouble(jp, ctxt);
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static final class FloatDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Float>
/*      */   {
/*      */     public FloatDeserializer(Class<Float> cls, Float nvl)
/*      */     {
/*  847 */       super(nvl);
/*      */     }
/*      */ 
/*      */     public Float deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  857 */       return _parseFloat(jp, ctxt);
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static final class LongDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Long>
/*      */   {
/*      */     public LongDeserializer(Class<Long> cls, Long nvl)
/*      */     {
/*  830 */       super(nvl);
/*      */     }
/*      */ 
/*      */     public Long deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  837 */       return _parseLong(jp, ctxt);
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static final class IntegerDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Integer>
/*      */   {
/*      */     public IntegerDeserializer(Class<Integer> cls, Integer nvl)
/*      */     {
/*  803 */       super(nvl);
/*      */     }
/*      */ 
/*      */     public Integer deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  810 */       return _parseInteger(jp, ctxt);
/*      */     }
/*      */ 
/*      */     public Integer deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  820 */       return _parseInteger(jp, ctxt);
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static final class CharacterDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Character>
/*      */   {
/*      */     public CharacterDeserializer(Class<Character> cls, Character nvl)
/*      */     {
/*  771 */       super(nvl);
/*      */     }
/*      */ 
/*      */     public Character deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  778 */       JsonToken t = jp.getCurrentToken();
/*      */ 
/*  781 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/*  782 */         int value = jp.getIntValue();
/*  783 */         if ((value >= 0) && (value <= 65535))
/*  784 */           return Character.valueOf((char)value);
/*      */       }
/*  786 */       else if (t == JsonToken.VALUE_STRING)
/*      */       {
/*  788 */         String text = jp.getText();
/*  789 */         if (text.length() == 1) {
/*  790 */           return Character.valueOf(text.charAt(0));
/*      */         }
/*      */       }
/*  793 */       throw ctxt.mappingException(this._valueClass);
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static final class ShortDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Short>
/*      */   {
/*      */     public ShortDeserializer(Class<Short> cls, Short nvl)
/*      */     {
/*  754 */       super(nvl);
/*      */     }
/*      */ 
/*      */     public Short deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  761 */       return _parseShort(jp, ctxt);
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static final class ByteDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Byte>
/*      */   {
/*      */     public ByteDeserializer(Class<Byte> cls, Byte nvl)
/*      */     {
/*  732 */       super(nvl);
/*      */     }
/*      */ 
/*      */     public Byte deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  739 */       int value = _parseIntPrimitive(jp, ctxt);
/*      */ 
/*  741 */       if ((value < -128) || (value > 127)) {
/*  742 */         throw ctxt.weirdStringException(this._valueClass, "overflow, value can not be represented as 8-bit value");
/*      */       }
/*  744 */       return Byte.valueOf((byte)value);
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static final class BooleanDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Boolean>
/*      */   {
/*      */     public BooleanDeserializer(Class<Boolean> cls, Boolean nvl)
/*      */     {
/*  705 */       super(nvl);
/*      */     }
/*      */ 
/*      */     public Boolean deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  712 */       return _parseBoolean(jp, ctxt);
/*      */     }
/*      */ 
/*      */     public Boolean deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  722 */       return _parseBoolean(jp, ctxt);
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static final class ClassDeserializer extends StdScalarDeserializer<Class<?>>
/*      */   {
/*      */     public ClassDeserializer()
/*      */     {
/*  674 */       super();
/*      */     }
/*      */ 
/*      */     public Class<?> deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  680 */       JsonToken curr = jp.getCurrentToken();
/*      */ 
/*  682 */       if (curr == JsonToken.VALUE_STRING) {
/*      */         try {
/*  684 */           return Class.forName(jp.getText());
/*      */         } catch (ClassNotFoundException e) {
/*  686 */           throw ctxt.instantiationException(this._valueClass, e);
/*      */         }
/*      */       }
/*  689 */       throw ctxt.mappingException(this._valueClass);
/*      */     }
/*      */   }
/*      */ 
/*      */   @JacksonStdImpl
/*      */   public static final class StringDeserializer extends StdScalarDeserializer<String>
/*      */   {
/*      */     public StringDeserializer()
/*      */     {
/*  629 */       super();
/*      */     }
/*      */ 
/*      */     public String deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  635 */       JsonToken curr = jp.getCurrentToken();
/*      */ 
/*  637 */       if (curr == JsonToken.VALUE_STRING) {
/*  638 */         return jp.getText();
/*      */       }
/*      */ 
/*  641 */       if (curr == JsonToken.VALUE_EMBEDDED_OBJECT) {
/*  642 */         Object ob = jp.getEmbeddedObject();
/*  643 */         if (ob == null) {
/*  644 */           return null;
/*      */         }
/*  646 */         if ((ob instanceof byte[])) {
/*  647 */           return Base64Variants.getDefaultVariant().encode((byte[])(byte[])ob, false);
/*      */         }
/*      */ 
/*  650 */         return ob.toString();
/*      */       }
/*      */ 
/*  653 */       if (curr.isScalarValue()) {
/*  654 */         return jp.getText();
/*      */       }
/*  656 */       throw ctxt.mappingException(this._valueClass);
/*      */     }
/*      */ 
/*      */     public String deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*      */       throws IOException, JsonProcessingException
/*      */     {
/*  666 */       return deserialize(jp, ctxt);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static abstract class PrimitiveOrWrapperDeserializer<T> extends StdScalarDeserializer<T>
/*      */   {
/*      */     final T _nullValue;
/*      */ 
/*      */     protected PrimitiveOrWrapperDeserializer(Class<T> vc, T nvl)
/*      */     {
/*  609 */       super();
/*  610 */       this._nullValue = nvl;
/*      */     }
/*      */ 
/*      */     public final T getNullValue()
/*      */     {
/*  615 */       return this._nullValue;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdDeserializer
 * JD-Core Version:    0.6.0
 */