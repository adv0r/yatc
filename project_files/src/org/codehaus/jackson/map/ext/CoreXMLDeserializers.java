/*     */ package org.codehaus.jackson.map.ext;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import javax.xml.datatype.DatatypeConfigurationException;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.Duration;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.deser.FromStringDeserializer;
/*     */ import org.codehaus.jackson.map.deser.StdDeserializer;
/*     */ import org.codehaus.jackson.map.deser.StdScalarDeserializer;
/*     */ import org.codehaus.jackson.map.util.Provider;
/*     */ 
/*     */ public class CoreXMLDeserializers
/*     */   implements Provider<StdDeserializer<?>>
/*     */ {
/*     */   static final DatatypeFactory _dataTypeFactory;
/*     */ 
/*     */   public Collection<StdDeserializer<?>> provide()
/*     */   {
/*  56 */     return Arrays.asList(new StdDeserializer[] { new DurationDeserializer(), new GregorianCalendarDeserializer(), new QNameDeserializer() });
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  38 */       _dataTypeFactory = DatatypeFactory.newInstance();
/*     */     } catch (DatatypeConfigurationException e) {
/*  40 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class QNameDeserializer extends FromStringDeserializer<QName>
/*     */   {
/*     */     public QNameDeserializer()
/*     */     {
/* 104 */       super();
/*     */     }
/*     */ 
/*     */     protected QName _deserialize(String value, DeserializationContext ctxt)
/*     */       throws IllegalArgumentException
/*     */     {
/* 110 */       return QName.valueOf(value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class GregorianCalendarDeserializer extends StdScalarDeserializer<XMLGregorianCalendar>
/*     */   {
/*     */     public GregorianCalendarDeserializer()
/*     */     {
/*  85 */       super();
/*     */     }
/*     */ 
/*     */     public XMLGregorianCalendar deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/*  91 */       Date d = _parseDate(jp, ctxt);
/*  92 */       if (d == null) {
/*  93 */         return null;
/*     */       }
/*  95 */       GregorianCalendar calendar = new GregorianCalendar();
/*  96 */       calendar.setTime(d);
/*  97 */       return CoreXMLDeserializers._dataTypeFactory.newXMLGregorianCalendar(calendar);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DurationDeserializer extends FromStringDeserializer<Duration>
/*     */   {
/*     */     public DurationDeserializer()
/*     */     {
/*  72 */       super();
/*     */     }
/*     */ 
/*     */     protected Duration _deserialize(String value, DeserializationContext ctxt)
/*     */       throws IllegalArgumentException
/*     */     {
/*  78 */       return CoreXMLDeserializers._dataTypeFactory.newDuration(value);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ext.CoreXMLDeserializers
 * JD-Core Version:    0.6.0
 */