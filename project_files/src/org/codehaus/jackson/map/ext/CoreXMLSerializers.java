/*    */ package org.codehaus.jackson.map.ext;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map.Entry;
/*    */ import javax.xml.datatype.Duration;
/*    */ import javax.xml.datatype.XMLGregorianCalendar;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.codehaus.jackson.JsonGenerationException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.map.JsonMappingException;
/*    */ import org.codehaus.jackson.map.JsonSerializer;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ import org.codehaus.jackson.map.ser.SerializerBase;
/*    */ import org.codehaus.jackson.map.ser.StdSerializers.CalendarSerializer;
/*    */ import org.codehaus.jackson.map.ser.ToStringSerializer;
/*    */ import org.codehaus.jackson.map.util.Provider;
/*    */ 
/*    */ public class CoreXMLSerializers
/*    */   implements Provider<Map.Entry<Class<?>, JsonSerializer<?>>>
/*    */ {
/* 38 */   static final HashMap<Class<?>, JsonSerializer<?>> _serializers = new HashMap();
/*    */ 
/*    */   public Collection<Map.Entry<Class<?>, JsonSerializer<?>>> provide()
/*    */   {
/* 51 */     return _serializers.entrySet();
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 44 */     ToStringSerializer tss = ToStringSerializer.instance;
/* 45 */     _serializers.put(Duration.class, tss);
/* 46 */     _serializers.put(XMLGregorianCalendar.class, new XMLGregorianCalendarSerializer());
/* 47 */     _serializers.put(QName.class, tss);
/*    */   }
/*    */ 
/*    */   public static class XMLGregorianCalendarSerializer extends SerializerBase<XMLGregorianCalendar>
/*    */   {
/*    */     public XMLGregorianCalendarSerializer()
/*    */     {
/* 57 */       super();
/*    */     }
/*    */ 
/*    */     public void serialize(XMLGregorianCalendar value, JsonGenerator jgen, SerializerProvider provider)
/*    */       throws IOException, JsonGenerationException
/*    */     {
/* 63 */       StdSerializers.CalendarSerializer.instance.serialize(value.toGregorianCalendar(), jgen, provider);
/*    */     }
/*    */ 
/*    */     public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException
/*    */     {
/* 68 */       return StdSerializers.CalendarSerializer.instance.getSchema(provider, typeHint);
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ext.CoreXMLSerializers
 * JD-Core Version:    0.6.0
 */