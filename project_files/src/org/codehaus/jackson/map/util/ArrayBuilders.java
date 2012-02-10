/*     */ package org.codehaus.jackson.map.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public final class ArrayBuilders
/*     */ {
/*  15 */   BooleanBuilder _booleanBuilder = null;
/*     */ 
/*  19 */   ByteBuilder _byteBuilder = null;
/*  20 */   ShortBuilder _shortBuilder = null;
/*  21 */   IntBuilder _intBuilder = null;
/*  22 */   LongBuilder _longBuilder = null;
/*     */ 
/*  24 */   FloatBuilder _floatBuilder = null;
/*  25 */   DoubleBuilder _doubleBuilder = null;
/*     */ 
/*     */   public BooleanBuilder getBooleanBuilder()
/*     */   {
/*  31 */     if (this._booleanBuilder == null) {
/*  32 */       this._booleanBuilder = new BooleanBuilder();
/*     */     }
/*  34 */     return this._booleanBuilder;
/*     */   }
/*     */ 
/*     */   public ByteBuilder getByteBuilder()
/*     */   {
/*  39 */     if (this._byteBuilder == null) {
/*  40 */       this._byteBuilder = new ByteBuilder();
/*     */     }
/*  42 */     return this._byteBuilder;
/*     */   }
/*     */ 
/*     */   public ShortBuilder getShortBuilder() {
/*  46 */     if (this._shortBuilder == null) {
/*  47 */       this._shortBuilder = new ShortBuilder();
/*     */     }
/*  49 */     return this._shortBuilder;
/*     */   }
/*     */ 
/*     */   public IntBuilder getIntBuilder() {
/*  53 */     if (this._intBuilder == null) {
/*  54 */       this._intBuilder = new IntBuilder();
/*     */     }
/*  56 */     return this._intBuilder;
/*     */   }
/*     */ 
/*     */   public LongBuilder getLongBuilder() {
/*  60 */     if (this._longBuilder == null) {
/*  61 */       this._longBuilder = new LongBuilder();
/*     */     }
/*  63 */     return this._longBuilder;
/*     */   }
/*     */ 
/*     */   public FloatBuilder getFloatBuilder()
/*     */   {
/*  68 */     if (this._floatBuilder == null) {
/*  69 */       this._floatBuilder = new FloatBuilder();
/*     */     }
/*  71 */     return this._floatBuilder;
/*     */   }
/*     */ 
/*     */   public DoubleBuilder getDoubleBuilder() {
/*  75 */     if (this._doubleBuilder == null) {
/*  76 */       this._doubleBuilder = new DoubleBuilder();
/*     */     }
/*  78 */     return this._doubleBuilder;
/*     */   }
/*     */ 
/*     */   public static <T> HashSet<T> arrayToSet(T[] elements)
/*     */   {
/* 147 */     HashSet result = new HashSet();
/* 148 */     if (elements != null) {
/* 149 */       for (Object elem : elements) {
/* 150 */         result.add(elem);
/*     */       }
/*     */     }
/* 153 */     return result;
/*     */   }
/*     */ 
/*     */   public static <T> List<T> addToList(List<T> list, T element)
/*     */   {
/* 170 */     if (list == null) {
/* 171 */       list = new ArrayList();
/*     */     }
/* 173 */     list.add(element);
/* 174 */     return list;
/*     */   }
/*     */ 
/*     */   public static <T> T[] insertInList(T[] array, T element)
/*     */   {
/* 183 */     int len = array.length;
/*     */ 
/* 185 */     Object[] result = (Object[])(Object[])Array.newInstance(array.getClass().getComponentType(), len + 1);
/* 186 */     if (len > 0) {
/* 187 */       System.arraycopy(array, 0, result, 1, len);
/*     */     }
/* 189 */     result[0] = element;
/* 190 */     return result;
/*     */   }
/*     */ 
/*     */   public static <T> Iterator<T> arrayAsIterator(T[] array)
/*     */   {
/* 201 */     return new ArrayIterator(array);
/*     */   }
/*     */ 
/*     */   public static <T> Iterable<T> arrayAsIterable(T[] array)
/*     */   {
/* 206 */     return new ArrayIterator(array);
/*     */   }
/*     */ 
/*     */   private static final class ArrayIterator<T>
/*     */     implements Iterator<T>, Iterable<T>
/*     */   {
/*     */     private final T[] _array;
/*     */     private int _index;
/*     */ 
/*     */     public ArrayIterator(T[] array)
/*     */     {
/* 229 */       this._array = array;
/* 230 */       this._index = 0;
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 234 */       return this._index < this._array.length;
/*     */     }
/*     */ 
/*     */     public T next()
/*     */     {
/* 240 */       if (this._index >= this._array.length) {
/* 241 */         throw new NoSuchElementException();
/*     */       }
/* 243 */       return this._array[(this._index++)];
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 247 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public Iterator<T> iterator()
/*     */     {
/* 252 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class DoubleBuilder extends PrimitiveArrayBuilder<double[]>
/*     */   {
/*     */     public final double[] _constructArray(int len)
/*     */     {
/* 136 */       return new double[len];
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class FloatBuilder extends PrimitiveArrayBuilder<float[]>
/*     */   {
/*     */     public final float[] _constructArray(int len)
/*     */     {
/* 129 */       return new float[len];
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class LongBuilder extends PrimitiveArrayBuilder<long[]>
/*     */   {
/*     */     public final long[] _constructArray(int len)
/*     */     {
/* 121 */       return new long[len];
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class IntBuilder extends PrimitiveArrayBuilder<int[]>
/*     */   {
/*     */     public final int[] _constructArray(int len)
/*     */     {
/* 114 */       return new int[len];
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class ShortBuilder extends PrimitiveArrayBuilder<short[]>
/*     */   {
/*     */     public final short[] _constructArray(int len)
/*     */     {
/* 107 */       return new short[len];
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class ByteBuilder extends PrimitiveArrayBuilder<byte[]>
/*     */   {
/*     */     public final byte[] _constructArray(int len)
/*     */     {
/* 100 */       return new byte[len];
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class BooleanBuilder extends PrimitiveArrayBuilder<boolean[]>
/*     */   {
/*     */     public final boolean[] _constructArray(int len)
/*     */     {
/*  92 */       return new boolean[len];
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.ArrayBuilders
 * JD-Core Version:    0.6.0
 */