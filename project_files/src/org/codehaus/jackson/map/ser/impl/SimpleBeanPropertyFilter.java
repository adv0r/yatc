/*     */ package org.codehaus.jackson.map.ser.impl;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.ser.BeanPropertyFilter;
/*     */ import org.codehaus.jackson.map.ser.BeanPropertyWriter;
/*     */ 
/*     */ public abstract class SimpleBeanPropertyFilter
/*     */   implements BeanPropertyFilter
/*     */ {
/*     */   public static SimpleBeanPropertyFilter filterOutAllExcept(Set<String> properties)
/*     */   {
/*  31 */     return new FilterExceptFilter(properties);
/*     */   }
/*     */ 
/*     */   public static SimpleBeanPropertyFilter filterOutAllExcept(String[] propertyArray) {
/*  35 */     HashSet properties = new HashSet(propertyArray.length);
/*  36 */     Collections.addAll(properties, propertyArray);
/*  37 */     return new FilterExceptFilter(properties);
/*     */   }
/*     */ 
/*     */   public static SimpleBeanPropertyFilter serializeAllExcept(Set<String> properties) {
/*  41 */     return new SerializeExceptFilter(properties);
/*     */   }
/*     */ 
/*     */   public static SimpleBeanPropertyFilter serializeAllExcept(String[] propertyArray) {
/*  45 */     HashSet properties = new HashSet(propertyArray.length);
/*  46 */     Collections.addAll(properties, propertyArray);
/*  47 */     return new SerializeExceptFilter(properties);
/*     */   }
/*     */ 
/*     */   public static class SerializeExceptFilter extends SimpleBeanPropertyFilter
/*     */   {
/*     */     protected final Set<String> _propertiesToExclude;
/*     */ 
/*     */     public SerializeExceptFilter(Set<String> properties)
/*     */     {
/*  96 */       this._propertiesToExclude = properties;
/*     */     }
/*     */ 
/*     */     public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer)
/*     */       throws Exception
/*     */     {
/* 104 */       if (!this._propertiesToExclude.contains(writer.getName()))
/* 105 */         writer.serializeAsField(bean, jgen, provider);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class FilterExceptFilter extends SimpleBeanPropertyFilter
/*     */   {
/*     */     protected final Set<String> _propertiesToInclude;
/*     */ 
/*     */     public FilterExceptFilter(Set<String> properties)
/*     */     {
/*  69 */       this._propertiesToInclude = properties;
/*     */     }
/*     */ 
/*     */     public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer)
/*     */       throws Exception
/*     */     {
/*  77 */       if (this._propertiesToInclude.contains(writer.getName()))
/*  78 */         writer.serializeAsField(bean, jgen, provider);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter
 * JD-Core Version:    0.6.0
 */