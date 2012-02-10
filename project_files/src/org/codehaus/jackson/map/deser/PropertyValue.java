/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ 
/*     */ abstract class PropertyValue
/*     */ {
/*     */   public final PropertyValue next;
/*     */   public final Object value;
/*     */ 
/*     */   protected PropertyValue(PropertyValue next, Object value)
/*     */   {
/*  21 */     this.next = next;
/*  22 */     this.value = value;
/*     */   }
/*     */ 
/*     */   public abstract void assign(Object paramObject)
/*     */     throws IOException, JsonProcessingException;
/*     */ 
/*     */   static final class Map extends PropertyValue
/*     */   {
/*     */     final Object _key;
/*     */ 
/*     */     public Map(PropertyValue next, Object value, Object key)
/*     */     {
/* 102 */       super(value);
/* 103 */       this._key = key;
/*     */     }
/*     */ 
/*     */     public void assign(Object bean)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 111 */       ((Map)bean).put(this._key, this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Any extends PropertyValue
/*     */   {
/*     */     final SettableAnyProperty _property;
/*     */     final String _propertyName;
/*     */ 
/*     */     public Any(PropertyValue next, Object value, SettableAnyProperty prop, String propName)
/*     */     {
/*  78 */       super(value);
/*  79 */       this._property = prop;
/*  80 */       this._propertyName = propName;
/*     */     }
/*     */ 
/*     */     public void assign(Object bean)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/*  87 */       this._property.set(bean, this._propertyName, this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Regular extends PropertyValue
/*     */   {
/*     */     final SettableBeanProperty _property;
/*     */ 
/*     */     public Regular(PropertyValue next, Object value, SettableBeanProperty prop)
/*     */     {
/*  50 */       super(value);
/*  51 */       this._property = prop;
/*     */     }
/*     */ 
/*     */     public void assign(Object bean)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/*  58 */       this._property.set(bean, this.value);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.PropertyValue
 * JD-Core Version:    0.6.0
 */