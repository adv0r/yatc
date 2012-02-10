/*    */ package org.codehaus.jackson.map.ser.impl;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.codehaus.jackson.map.ser.BeanPropertyFilter;
/*    */ import org.codehaus.jackson.map.ser.FilterProvider;
/*    */ 
/*    */ public class SimpleFilterProvider extends FilterProvider
/*    */ {
/*    */   protected final Map<String, BeanPropertyFilter> _filtersById;
/*    */   protected BeanPropertyFilter _defaultFilter;
/*    */ 
/*    */   public SimpleFilterProvider()
/*    */   {
/* 32 */     this._filtersById = new HashMap();
/*    */   }
/*    */ 
/*    */   public SimpleFilterProvider(Map<String, BeanPropertyFilter> mapping)
/*    */   {
/* 39 */     this._filtersById = new HashMap();
/*    */   }
/*    */ 
/*    */   public SimpleFilterProvider setDefaultFilter(BeanPropertyFilter f)
/*    */   {
/* 50 */     this._defaultFilter = f;
/* 51 */     return this;
/*    */   }
/*    */ 
/*    */   public SimpleFilterProvider addFilter(String id, BeanPropertyFilter filter) {
/* 55 */     this._filtersById.put(id, filter);
/* 56 */     return this;
/*    */   }
/*    */ 
/*    */   public BeanPropertyFilter removeFilter(String id) {
/* 60 */     return (BeanPropertyFilter)this._filtersById.remove(id);
/*    */   }
/*    */ 
/*    */   public BeanPropertyFilter findFilter(Object filterId)
/*    */   {
/* 72 */     BeanPropertyFilter f = (BeanPropertyFilter)this._filtersById.get(filterId);
/* 73 */     return f == null ? this._defaultFilter : f;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.SimpleFilterProvider
 * JD-Core Version:    0.6.0
 */