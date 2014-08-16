/*
 * Copyright (c) 2014 acmi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package acmi.l2.clientmod.unreal;

import acmi.l2.clientmod.io.UnrealPackageFile;

import java.nio.ByteBuffer;
import java.util.UUID;

import static acmi.l2.clientmod.util.BufferUtil.getCompactInt;

public class UClass extends State {
    public final int classFlags;
    public final UUID classUuid;

    ByteBuffer buffer;
    PropertiesUtil propertiesUtil;

    public UClass(ByteBuffer buffer, UnrealPackageFile.ExportEntry up, PropertiesUtil propertiesUtil) {
        super(buffer, up, propertiesUtil);

        classFlags = buffer.getInt();
        byte[] uuid = new byte[16];
        buffer.get(uuid);
        classUuid = UUID.nameUUIDFromBytes(uuid);
        int dependenciesCount = getCompactInt(buffer);
        for (int i = 0; i < dependenciesCount; i++) {
            getCompactInt(buffer);
            buffer.getInt();
            buffer.getInt();
        }
        int packageImportsCount = getCompactInt(buffer);
        for (int i = 0; i < packageImportsCount; i++) {
            getCompactInt(buffer);
        }
        getCompactInt(buffer);
        getCompactInt(buffer);
        int hideCategoriesListCount = getCompactInt(buffer);
        for (int i = 0; i < hideCategoriesListCount; i++) {
            getCompactInt(buffer);
        }

        this.buffer = buffer;
        this.propertiesUtil = propertiesUtil;
    }

    public void readProperties() {
        if (scriptSize == 0) {
            properties = propertiesUtil.readProperties(buffer, getEntry().getUnrealPackage(), getEntry().getObjectFullName());
            buffer = null;
        }
    }
}
