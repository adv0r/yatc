/*    */ package org.codehaus.jackson.map.ser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonGenerationException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ import org.codehaus.jackson.map.TypeSerializer;
/*    */ 
/*    */ public abstract class ScalarSerializerBase<T> extends SerializerBase<T>
/*    */ {
/*    */   protected ScalarSerializerBase(Class<T> t)
/*    */   {
/* 14 */     super(t);
/*    */   }
/*    */ 
/*    */   protected ScalarSerializerBase(Class<?> t, boolean dummy)
/*    */   {
/* 23 */     super(t);
/*    */   }
/*    */ 
/*    */   public void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 38 */     typeSer.writeTypePrefixForScalar(value, jgen);
/* 39 */     serialize(value, jgen, provider);
/* 40 */     typeSer.writeTypeSuffixForScalar(value, jgen);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.ScalarSerializerBase
 * JD-Core Version:    0.6.0
 */