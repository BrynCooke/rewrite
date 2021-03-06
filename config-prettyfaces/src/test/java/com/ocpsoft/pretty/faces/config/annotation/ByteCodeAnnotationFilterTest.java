/*
 * Copyright 2010 Lincoln Baxter, III
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ocpsoft.pretty.faces.config.annotation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

public class ByteCodeAnnotationFilterTest
{
 
   private ByteCodeAnnotationFilter filter;
   
   @Before
   public void setup() {
      filter = new ByteCodeAnnotationFilter();
   }
   
   @After
   public void teardown() {
      filter = null;
   }
   
   public ByteCodeAnnotationFilterTest()
   {
      /*
      System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
      System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");
      */
   }

   @Test
   public void testInvalidClass() throws IOException
   {
      assertFalse( filter.accept( new ByteArrayInputStream( new byte[] {
            // some random bytes
            (byte) 0x12, (byte) 0x34, (byte) 0xd3, (byte) 0x45,
            (byte) 0x13, (byte) 0x35, (byte) 0xd4, (byte) 0x46,
            (byte) 0x14, (byte) 0x36, (byte) 0xd5, (byte) 0x47,
            (byte) 0x15, (byte) 0x37, (byte) 0xd6, (byte) 0x48
      })));
   }

   @Test
   public void testJdk14Class() throws IOException
   {
      assertFalse( filter.accept( new ByteArrayInputStream( new byte[] {
            (byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe,  // magic
            (byte) 0x00, (byte) 0x00,    // minor 
            (byte) 0x00, (byte)   48     // major 48 = JDK14
      })));
   }

   @Test
   public void testNoAnnotationsOnClass() throws IOException
   {
      assertFalse( filter.accept(getByteCodeInputStream(ClassWithoutAnnotations.class)) );
   }

   @Test
   public void testUrlMappingAnnotationOnClass() throws IOException
   {
      assertTrue( filter.accept(getByteCodeInputStream(ClassWithUrlMapping.class)) );
   }

   @Test
   public void testClassWithLongValueInConstantPool() throws IOException
   {
      assertFalse( filter.accept(getByteCodeInputStream(ClassWithLongValueInConstantPool.class)) );
   }
   
   @Test
   public void testUrlActionOnMethod() throws IOException
   {
      assertTrue( filter.accept(getByteCodeInputStream(ClassWithUrlAction.class)) );
   }
   
   private InputStream getByteCodeInputStream(Class<?> clazz)
   {
      String name = clazz.getName().replace(".", "/") + ".class";
      InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
      assertNotNull("Cannot find class file: "+name, stream);
      return stream;
   }

   /**
    * Class without any PrettyFaces annotations
    */
   public static class ClassWithoutAnnotations
   {

      private String someProperty;

      public String getSomeProperty()
      {
         return someProperty;
      }

   }

   /**
    * Class with some {@link Long} constant in the constant pool
    */
   public class ClassWithLongValueInConstantPool
   {
      public final static long value = 9223342036854775807l;
   }
   
   /**
    * Class with an {@link URLMapping} annotations
    */
   @URLMapping(id = "home", pattern = "/home", viewId = "/home.jsf")
   public static class ClassWithUrlMapping
   {

      private String someProperty;

      public String getSomeProperty()
      {
         return someProperty;
      }

   }

   /**
    * Class with an {@link URLMapping} annotations
    */
   public static class ClassWithUrlAction
   {

      private String someProperty;

      @URLAction
      public void urlAction()
      {
      // nothing
      }

      public String getSomeProperty()
      {
         return someProperty;
      }

   }

}
