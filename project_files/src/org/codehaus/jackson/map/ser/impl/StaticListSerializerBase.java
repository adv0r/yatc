/*    */ package org.codehaus.jackson.map.ser.impl;
/*    */ 
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collection;
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.map.BeanProperty;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ import org.codehaus.jackson.map.ser.SerializerBase;
/*    */ import org.codehaus.jackson.node.ObjectNode;
/*    */ 
/*    */ public abstract class StaticListSerializerBase<T extends Collection<?>> extends SerializerBase<T>
/*    */ {
/*    */   protected final BeanProperty _property;
/*    */ 
/*    */   protected StaticListSerializerBase(Class<?> cls, BeanProperty property)
/*    */   {
/* 28 */     super(cls, false);
/* 29 */     this._property = property;
/*    */   }
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 35 */     ObjectNode o = createSchemaNode("array", true);
/* 36 */     o.put("items", contentSchema());
/* 37 */     return o;
/*    */   }
/*    */ 
/*    */   protected abstract JsonNode contentSchema();
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.StaticListSerializerBase
 * JD-Core Version:    0.6.0
 */