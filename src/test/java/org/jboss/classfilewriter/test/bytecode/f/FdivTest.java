/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2012 Red Hat, Inc.
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
package org.jboss.classfilewriter.test.bytecode.f;

import org.junit.Assert;

import org.jboss.classfilewriter.code.CodeAttribute;
import org.jboss.classfilewriter.test.bytecode.MethodTester;
import org.junit.Test;

public class FdivTest {

    @Test
    public void fdivTest() {
        MethodTester<Float> mt = new MethodTester<Float>(float.class, float.class, float.class);
        CodeAttribute ca = mt.getCodeAttribute();
        ca.fload(0);
        ca.fload(1);
        ca.fdiv();
        ca.returnInstruction();
        Assert.assertEquals(10.0f / 5.0f, mt.invoke(10.0f, 5.0f), 0);
        Assert.assertEquals(12.0f / 2.0f, mt.invoke(12.0f, 2.0f), 0);
    }


}
