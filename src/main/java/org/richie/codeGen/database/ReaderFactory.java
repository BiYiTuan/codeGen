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
// Created on 2013-7-7

package org.richie.codeGen.database;

import org.richie.codeGen.core.exception.CGException;
import org.richie.codeGen.database.pdm.PdmReaderImpl;


/**
 * @author elfkingw
 *
 */
public class ReaderFactory {

   public static DatabaseReader getReaderInstance(String readType,String databaseType,String filePath) throws CGException{
       DatabaseReader reader = null;
       if(Constants.DATABASE_READER_TYPE_PDM.equals(readType)){
           reader = new PdmReaderImpl(databaseType,filePath);
       }else{
           throw new CGException("don't support the readType:"+readType);
       }
       return reader;
   }
}
