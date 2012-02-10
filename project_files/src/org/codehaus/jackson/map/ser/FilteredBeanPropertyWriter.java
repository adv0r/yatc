/*    */ package org.codehaus.jackson.map.ser;
/*    */ 
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.map.JsonSerializer;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ 
/*    */ public abstract class FilteredBeanPropertyWriter
/*    */ {
/*    */   public static BeanPropertyWriter constructViewBased(BeanPropertyWriter base, Class<?>[] viewsToIncludeIn)
/*    */   {
/* 18 */     if (viewsToIncludeIn.length == 1) {
/* 19 */       return new SingleView(base, viewsToIncludeIn[0]);
/*    */     }
/* 21 */     return new MultiView(base, viewsToIncludeIn);
/*    */   }
/*    */ 
/*    */   private static final class MultiView extends BeanPropertyWriter
/*    */   {
/*    */     protected final Class<?>[] _views;
/*    */ 
/*    */     protected MultiView(BeanPropertyWriter base, Class<?>[] views)
/*    */     {
/* 68 */       super();
/* 69 */       this._views = views;
/*    */     }
/*    */ 
/*    */     protected MultiView(MultiView fromView, JsonSerializer<Object> ser) {
/* 73 */       super(ser);
/* 74 */       this._views = fromView._views;
/*    */     }
/*    */ 
/*    */     public BeanPropertyWriter withSerializer(JsonSerializer<Object> ser)
/*    */     {
/* 80 */       return new MultiView(this, ser);
/*    */     }
/*    */ 
/*    */     public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*    */       throws Exception
/*    */     {
/* 87 */       Class activeView = prov.getSerializationView();
/* 88 */       if (activeView != null) {
/* 89 */         int i = 0; int len = this._views.length;
/* 90 */         while ((i < len) && 
/* 91 */           (!this._views[i].isAssignableFrom(activeView))) {
/* 90 */           i++;
/*    */         }
/*    */ 
/* 94 */         if (i == len) {
/* 95 */           return;
/*    */         }
/*    */       }
/* 98 */       super.serializeAsField(bean, jgen, prov);
/*    */     }
/*    */   }
/*    */ 
/*    */   private static final class SingleView extends BeanPropertyWriter
/*    */   {
/*    */     protected final Class<?> _view;
/*    */ 
/*    */     protected SingleView(BeanPropertyWriter base, Class<?> view)
/*    */     {
/* 36 */       super();
/* 37 */       this._view = view;
/*    */     }
/*    */ 
/*    */     protected SingleView(SingleView fromView, JsonSerializer<Object> ser) {
/* 41 */       super(ser);
/* 42 */       this._view = fromView._view;
/*    */     }
/*    */ 
/*    */     public BeanPropertyWriter withSerializer(JsonSerializer<Object> ser)
/*    */     {
/* 48 */       return new SingleView(this, ser);
/*    */     }
/*    */ 
/*    */     public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*    */       throws Exception
/*    */     {
/* 55 */       Class activeView = prov.getSerializationView();
/* 56 */       if ((activeView == null) || (this._view.isAssignableFrom(activeView)))
/* 57 */         super.serializeAsField(bean, jgen, prov);
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.FilteredBeanPropertyWriter
 * JD-Core Version:    0.6.0
 */