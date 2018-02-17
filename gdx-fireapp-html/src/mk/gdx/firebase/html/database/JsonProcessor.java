/*
 * Copyright 2017 mk
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

package mk.gdx.firebase.html.database;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import java.util.List;
import java.util.Map;

import mk.gdx.firebase.annotations.NestedGenericType;
import mk.gdx.firebase.html.exceptions.NestedGenericTypeAnnotationMissingException;
import mk.gdx.firebase.reflection.AnnotationFinder;

/**
 * Process json string to generic type.
 */
public class JsonProcessor
{

    /**
     * Converts json string into java object.
     *
     * @param wantedType        Wanted type
     * @param genericTypeKeeper Object with wanted type inside generic argument
     * @param jsonString        Json string data
     * @param <R>               Return type
     * @return
     */
    public static <R> R process(Class<?> wantedType, Object genericTypeKeeper, String jsonString)
    {
        Json json = new Json();
        json.setIgnoreUnknownFields(true);
        json.setTypeName(null);
        R result = null;
        if (ClassReflection.isAssignableFrom(List.class, wantedType)
                || ClassReflection.isAssignableFrom(Map.class, wantedType)) {
            NestedGenericType nestedGenericType = AnnotationFinder.getMethodAnnotation(NestedGenericType.class, genericTypeKeeper);
            if (nestedGenericType == null) throw new NestedGenericTypeAnnotationMissingException();
            json.setDefaultSerializer(new JsonListMapDeserializer(wantedType, nestedGenericType.value()));
            result = (R) json.fromJson(wantedType, jsonString);
        } else {
            result = (R) json.fromJson(wantedType, jsonString);
        }
        return result;
    }
}
