/*    */ package org.codehaus.jackson.map.ser;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Map;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.map.JsonMappingException;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*    */ 
/*    */ public class AnyGetterWriter
/*    */ {
/*    */   protected final Method _anyGetter;
/*    */   protected final MapSerializer _serializer;
/*    */ 
/*    */   public AnyGetterWriter(AnnotatedMethod anyGetter, MapSerializer serializer)
/*    */   {
/* 25 */     this._anyGetter = anyGetter.getAnnotated();
/* 26 */     this._serializer = serializer;
/*    */   }
/*    */ 
/*    */   public void getAndSerialize(Object bean, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws Exception
/*    */   {
/* 32 */     Object value = this._anyGetter.invoke(bean, new Object[0]);
/* 33 */     if (value == null) {
/* 34 */       return;
/*    */     }
/* 36 */     if (!(value instanceof Map)) {
/* 37 */       throw new JsonMappingException("Value returned by 'any-getter' (" + this._anyGetter.getName() + "()) not java.util.Map but " + value.getClass().getName());
/*    */     }
/*    */ 
/* 40 */     this._serializer.serializeFields((Map)value, jgen, provider);
/*    */   }
/*    */ 
/*    */   public void resolve(SerializerProvider provider) throws JsonMappingException
/*    */   {
/* 45 */     this._serializer.resolve(provider);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.AnyGetterWriter
 * JD-Core Version:    0.6.0
 */