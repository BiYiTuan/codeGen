/*
 * Copyright 2013 elfkingw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Created on 2013-7-6

package org.richie.codeGen.core.velocity;

import junit.framework.TestCase;

import org.richie.codeGen.core.exception.CGException;
import org.richie.codeGen.core.model.Table;

/**
 * @author elfkingw
 */
public class CodeGenTest extends TestCase {

    /**
     * Test method for
     * {@link org.richie.codeGen.core.velocity.CodeGen#genCode(java.lang.String, java.lang.String, java.lang.String)}.
     */
    public void testGenCode() {
        String templaetName = "add.js.vm";
        String templatesFolder = "E:\\workspace10\\codeGen\\src\\test\\java\\template";
        String outFileFolder = "E:\\workspace10\\codeGen\\src\\test\\java\\template\\out\\file";
        String outFileName = "add.js";
        try {
            // Table table = new Table();
            // table.setTableCode("FE_FUND");
            // CodeGen.initTableVelocityContext(table);
            // CodeGen.putToolsToVelocityContext("userName", "wanghua");
            // // String result = CodeGen.genCode(templaetName, templatesFolder);
            // // System.out.println(result);
            // CodeGen.genCode(templaetName, templatesFolder,outFileFolder,outFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Test method for {@link org.richie.codeGen.core.velocity.CodeGen#genCode(java.lang.String, java.lang.String)}.
     */
    public void testGenCodeStringString() {
    }

}
