/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationConfig;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.DeserializationProblemHandler;
/*     */ import org.codehaus.jackson.map.DeserializerProvider;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ import org.codehaus.jackson.map.util.LinkedNode;
/*     */ import org.codehaus.jackson.map.util.ObjectBuffer;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class StdDeserializationContext extends DeserializationContext
/*     */ {
/*     */   static final int MAX_ERROR_STR_LEN = 500;
/*     */   protected JsonParser _parser;
/*     */   protected final DeserializerProvider _deserProvider;
/*     */   protected ArrayBuilders _arrayBuilders;
/*     */   protected ObjectBuffer _objectBuffer;
/*     */   protected DateFormat _dateFormat;
/*     */ 
/*     */   public StdDeserializationContext(DeserializationConfig config, JsonParser jp, DeserializerProvider prov)
/*     */   {
/*  63 */     super(config);
/*  64 */     this._parser = jp;
/*  65 */     this._deserProvider = prov;
/*     */   }
/*     */ 
/*     */   public DeserializerProvider getDeserializerProvider()
/*     */   {
/*  76 */     return this._deserProvider;
/*     */   }
/*     */ 
/*     */   public JsonParser getParser()
/*     */   {
/*  88 */     return this._parser;
/*     */   }
/*     */ 
/*     */   public final ObjectBuffer leaseObjectBuffer()
/*     */   {
/*  99 */     ObjectBuffer buf = this._objectBuffer;
/* 100 */     if (buf == null)
/* 101 */       buf = new ObjectBuffer();
/*     */     else {
/* 103 */       this._objectBuffer = null;
/*     */     }
/* 105 */     return buf;
/*     */   }
/*     */ 
/*     */   public final void returnObjectBuffer(ObjectBuffer buf)
/*     */   {
/* 114 */     if ((this._objectBuffer == null) || (buf.initialCapacity() >= this._objectBuffer.initialCapacity()))
/*     */     {
/* 116 */       this._objectBuffer = buf;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final ArrayBuilders getArrayBuilders()
/*     */   {
/* 123 */     if (this._arrayBuilders == null) {
/* 124 */       this._arrayBuilders = new ArrayBuilders();
/*     */     }
/* 126 */     return this._arrayBuilders;
/*     */   }
/*     */ 
/*     */   public Date parseDate(String dateStr)
/*     */     throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 140 */       return getDateFormat().parse(dateStr); } catch (ParseException pex) {
/*     */     }
/* 142 */     throw new IllegalArgumentException(pex.getMessage());
/*     */   }
/*     */ 
/*     */   public Calendar constructCalendar(Date d)
/*     */   {
/* 152 */     Calendar c = Calendar.getInstance();
/* 153 */     c.setTime(d);
/* 154 */     return c;
/*     */   }
/*     */ 
/*     */   public boolean handleUnknownProperty(JsonParser jp, JsonDeserializer<?> deser, Object instanceOrClass, String propName)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 172 */     LinkedNode h = this._config.getProblemHandlers();
/* 173 */     if (h != null)
/*     */     {
/* 177 */       JsonParser oldParser = this._parser;
/* 178 */       this._parser = jp;
/*     */       try {
/* 180 */         while (h != null)
/*     */         {
/* 182 */           if (((DeserializationProblemHandler)h.value()).handleUnknownProperty(this, deser, instanceOrClass, propName)) {
/* 183 */             int i = 1;
/*     */             return i;
/*     */           }
/* 185 */           h = h.next();
/*     */         }
/*     */       } finally {
/* 188 */         this._parser = oldParser;
/*     */       }
/*     */     }
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */   public JsonMappingException mappingException(Class<?> targetClass)
/*     */   {
/* 197 */     String clsName = _calcName(targetClass);
/* 198 */     return JsonMappingException.from(this._parser, "Can not deserialize instance of " + clsName + " out of " + this._parser.getCurrentToken() + " token");
/*     */   }
/*     */ 
/*     */   public JsonMappingException instantiationException(Class<?> instClass, Exception e)
/*     */   {
/* 204 */     return JsonMappingException.from(this._parser, "Can not construct instance of " + instClass.getName() + ", problem: " + e.getMessage());
/*     */   }
/*     */ 
/*     */   public JsonMappingException instantiationException(Class<?> instClass, String msg)
/*     */   {
/* 210 */     return JsonMappingException.from(this._parser, "Can not construct instance of " + instClass.getName() + ", problem: " + msg);
/*     */   }
/*     */ 
/*     */   public JsonMappingException weirdStringException(Class<?> instClass, String msg)
/*     */   {
/* 220 */     return JsonMappingException.from(this._parser, "Can not construct instance of " + instClass.getName() + " from String value '" + _valueDesc() + "': " + msg);
/*     */   }
/*     */ 
/*     */   public JsonMappingException weirdNumberException(Class<?> instClass, String msg)
/*     */   {
/* 226 */     return JsonMappingException.from(this._parser, "Can not construct instance of " + instClass.getName() + " from number value (" + _valueDesc() + "): " + msg);
/*     */   }
/*     */ 
/*     */   public JsonMappingException weirdKeyException(Class<?> keyClass, String keyValue, String msg)
/*     */   {
/* 232 */     return JsonMappingException.from(this._parser, "Can not construct Map key of type " + keyClass.getName() + " from String \"" + _desc(keyValue) + "\": " + msg);
/*     */   }
/*     */ 
/*     */   public JsonMappingException wrongTokenException(JsonParser jp, JsonToken expToken, String msg)
/*     */   {
/* 238 */     return JsonMappingException.from(jp, "Unexpected token (" + jp.getCurrentToken() + "), expected " + expToken + ": " + msg);
/*     */   }
/*     */ 
/*     */   public JsonMappingException unknownFieldException(Object instanceOrClass, String fieldName)
/*     */   {
/* 244 */     return UnrecognizedPropertyException.from(this._parser, instanceOrClass, fieldName);
/*     */   }
/*     */ 
/*     */   public JsonMappingException unknownTypeException(JavaType type, String id)
/*     */   {
/* 250 */     return JsonMappingException.from(this._parser, "Could not resolve type id '" + id + "' into a subtype of " + type);
/*     */   }
/*     */ 
/*     */   protected DateFormat getDateFormat()
/*     */   {
/* 261 */     if (this._dateFormat == null)
/*     */     {
/* 263 */       this._dateFormat = ((DateFormat)this._config.getDateFormat().clone());
/*     */     }
/* 265 */     return this._dateFormat;
/*     */   }
/*     */ 
/*     */   protected String determineClassName(Object instance)
/*     */   {
/* 270 */     return ClassUtil.getClassDescription(instance);
/*     */   }
/*     */ 
/*     */   protected String _calcName(Class<?> cls)
/*     */   {
/* 281 */     if (cls.isArray()) {
/* 282 */       return _calcName(cls.getComponentType()) + "[]";
/*     */     }
/* 284 */     return cls.getName();
/*     */   }
/*     */ 
/*     */   protected String _valueDesc()
/*     */   {
/*     */     try {
/* 290 */       return _desc(this._parser.getText()); } catch (Exception e) {
/*     */     }
/* 292 */     return "[N/A]";
/*     */   }
/*     */ 
/*     */   protected String _desc(String desc)
/*     */   {
/* 298 */     if (desc.length() > 500) {
/* 299 */       desc = desc.substring(0, 500) + "]...[" + desc.substring(desc.length() - 500);
/*     */     }
/* 301 */     return desc;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdDeserializationContext
 * JD-Core Version:    0.6.0
 */