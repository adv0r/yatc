/*    */ package org.codehaus.jackson.map.ser;
/*    */ 
/*    */ import org.codehaus.jackson.map.TypeSerializer;
/*    */ 
/*    */ public abstract class ContainerSerializerBase<T> extends SerializerBase<T>
/*    */ {
/*    */   protected ContainerSerializerBase(Class<T> t)
/*    */   {
/* 22 */     super(t);
/*    */   }
/*    */ 
/*    */   protected ContainerSerializerBase(Class<?> t, boolean dummy)
/*    */   {
/* 32 */     super(t, dummy);
/*    */   }
/*    */ 
/*    */   public ContainerSerializerBase<?> withValueTypeSerializer(TypeSerializer vts)
/*    */   {
/* 46 */     if (vts == null) return this;
/* 47 */     return _withValueTypeSerializer(vts);
/*    */   }
/*    */ 
/*    */   public abstract ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer);
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.ContainerSerializerBase
 * JD-Core Version:    0.6.0
 */