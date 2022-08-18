package dev.siliconcode.qatch.service.lang.providers;

import dev.siliconcode.qatch.service.lang.LanguageProvider;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

/**
 * Class to load language providers as needed based on the language name
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Slf4j
public class ProviderLoader {

  private Map<String, LanguageProvider> providers;

  /** Private singleton constructor */
  private ProviderLoader() {}

  /**
   * Internal class used to hold the Singleton while also providing both Lazy Loading and Thread
   * Safety
   */
  private static final class InstanceHolder {
    private static final ProviderLoader INSTANCE = new ProviderLoader();
  }

  /**
   * Returns the initialzed ProviderLoader singleton instance
   *
   * @return The Singleton Instance
   */
  public static ProviderLoader instance() {
    ProviderLoader inst = InstanceHolder.INSTANCE;
    if (inst.providers == null) {
      inst.initProviders();
    }
    return inst;
  }

  /**
   * Retrieves the provider for the given language, or null if the provided language is unknown
   *
   * @param language Name of the language to retrieve a provider for, cannot be null
   * @return The language provider for the input language, or null if that language has no known
   *     provider
   */
  public LanguageProvider getProvider(@NonNull String language) {
    if (providers != null && providers.containsKey(language.toLowerCase()))
      return providers.get(language);
    return null;
  }

  /** Initialzes the map of providers from the jar resource file: /providers.json */
  private void initProviders() {
    providers = Maps.newHashMap();
    Map<String, String> map = null;
    try (BufferedReader br =
        new BufferedReader(
            new InputStreamReader(
                Objects.requireNonNull(
                    ProviderLoader.class.getResourceAsStream("/providers.json"))))) {
      Gson gson = new Gson();
      Type type = new TypeToken<Map<String, String>>() {}.getType();
      map = gson.fromJson(br, type);
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }

    if (map != null) {
      map.forEach(
          (key, value) -> {
            LanguageProvider provider;
            try {
              Class<?> cls = Class.forName(value);
              Constructor<?> m = cls.getConstructor();
              provider = (LanguageProvider) m.newInstance();
              providers.put(key, provider);
            } catch (ClassNotFoundException
                | NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e) {
              e.printStackTrace();
            }
          });
    }
  }
}
