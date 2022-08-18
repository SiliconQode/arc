/*
 * The MIT License (MIT)
 *
 * Empirilytics Metrics
 * Copyright (c) 2015-2019 Emprilytics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.siliconcode.arc.quality.metrics

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class LoCProfileManager {

    static final String DEFAULT_LOCATION = "/edu/montana/gsoc/msusel/metrics/default.json"

    /**
     * Map of known profiles where the served extensions are the key
     */
    Map<String, LoCProfile> profileByExtension = [:]
    /**
     * Map of known profiles where the served language is the key
     */
    private Map<String, LoCProfile> profileByLanguage = [:]

    static { loadProfiles() }

    /**
     * Adds the given profile to this profile manager
     *
     * @param profile
     *            Profile to add
     */
    void addProfile(LoCProfile profile) {
        for (String ext : profile.getExtensions()) {
            profileByExtension.put(ext.toLowerCase(), profile)
        }

        profileByLanguage.put(profile.getLanguage().toLowerCase(), profile)
    }

    /**
     * Requests a profile for a given file extension (excluding the dot)
     *
     * @param ext
     *            File extension
     * @return The profile registered to the provided extension.
     * @throws UnknownProfileException
     *             When the provided extension has no registered profile
     * @throws IllegalArgumentException
     *             When the provided extension is null or empty
     */
    LoCProfile getProfileByExtension(String ext) throws UnknownProfileException {
        if (ext == null || ext.isEmpty())
            throw new IllegalArgumentException("Extension cannot be null or empty")
        if (!profileByExtension.containsKey(ext))
            throw new UnknownProfileException("No known LoC profile for extension " + ext)

        profileByExtension.get(ext.toLowerCase())
    }

    /**
     * Requests a profile for a given language
     *
     * @param lang
     *            Language name
     * @return The profile registered to the provided language
     * @throws UnknownProfileException
     *             When the provided language has no registered profile.
     * @throws IllegalArgumentException
     *             When the provided extension is null or empty
     */
    LoCProfile getProfileByLanguage(String lang) throws UnknownProfileException {
        if (lang == null || lang.isEmpty())
            throw new IllegalArgumentException("Language cannot be null or empty")
        if (!profileByExtension.containsKey(lang))
            throw new UnknownProfileException("No known LoC profile for language " + lang)

        profileByLanguage.get(lang.toLowerCase())
    }

    /**
     * Loads the default profiles from the JAR file
     */
    static void loadProfiles() {
        InputStream is = LoCProfileManager.class.getResourceAsStream(DEFAULT_LOCATION)

        Gson gson = new Gson()
        Type list = new TypeToken<List<LoCProfile>>() {
        }.getType()
        List<LoCProfile> profiles = gson.fromJson(gson.newJsonReader(new InputStreamReader(is)), list)

        for (LoCProfile profile : profiles) {
            LoCProfileManager.instance.addProfile(profile)
        }
    }
}
