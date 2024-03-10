/*
 *
 *   Created by Amitskr & VnjVibhash on 2/21/24, 10:32 AM
 *   Copyright Ⓒ 2024. All rights reserved Ⓒ 2024 http://vivekajee.in/
 *   Last modified: 2/29/24, 1:59 PM
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 *   except in compliance with the License. You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENS... Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *    either express or implied. See the License for the specific language governing permissions and
 *    limitations under the License.
 * /
 */

package com.asvk.urlshield.utilities.wrappers;

import android.content.Context;

import com.asvk.urlshield.utilities.methods.JavaUtils;
import com.asvk.urlshield.utilities.methods.StreamUtils;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Represents an internal file, can be modified
 */
public class InternalFile {
    private final String fileName;
    private final Context cntx;

    public InternalFile(String fileName, Context cntx) {
        this.fileName = fileName;
        this.cntx = cntx;
    }

    /**
     * Returns the content, null if the file doesn't exists or can't be read
     */
    public String get() {
        try {
            return StreamUtils.inputStream2String(cntx.openFileInput(fileName));
        } catch (IOException ignored) {
            return null;
        }
    }

    /**
     * Streams the lines
     */
    public boolean stream(JavaUtils.Consumer<String> function) {
        try {
            StreamUtils.consumeLines(cntx.openFileInput(fileName), function);
            return true;
        } catch (IOException ignored) {
            // do nothing
            return false;
        }
    }

    /**
     * Sets a new file content
     */
    public boolean set(String content) {

        // the same, already saved
        if (content.equals(get())) {
            return true;
        }

        // store
        try (FileOutputStream fos = cntx.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes(StreamUtils.UTF_8));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes the file
     */
    public void delete() {
        cntx.deleteFile(fileName);
    }

}
