/*
 * Copyright 2011 <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
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
package com.ocpsoft.rewrite.config;

import com.ocpsoft.rewrite.context.EvaluationContext;
import com.ocpsoft.rewrite.event.Rewrite;

/**
 * Build a {@link Configuration} rule.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class RuleBuilder implements Rule
{
   private Condition condition;
   private Operation operation;

   public static RuleBuilder define()
   {
      return new RuleBuilder();
   }

   public RuleBuilder when(final Condition condition)
   {
      this.condition = condition;
      return this;
   }

   public RuleBuilder perform(final Operation operation)
   {
      this.operation = operation;
      return this;
   }

   @Override
   public boolean evaluate(final Rewrite event, final EvaluationContext context)
   {
      return condition.evaluate(event, context);
   }

   @Override
   public void perform(final Rewrite event, final EvaluationContext context)
   {
      operation.perform(event, context);
   }
}