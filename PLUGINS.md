# Lavalink Plugins
**Warning:** The plugin system is still in beta. Breaking changes may still be made to the plugin API if deemed necessary.

Lavalink supports third-party plugins to add additional functionality such as custom audio sources, custom filters,
WebSocket handling, REST endpoints, and much more. 

List of plugins:
- [Google Cloud TTS plugin](https://github.com/DuncteBot/tts-plugin) A text to speech plugin using the [google cloud tts api](https://cloud.google.com/text-to-speech/docs)
- [SponsorBlock plugin](https://github.com/TopiSenpai/Sponsorblock-Plugin) for skipping sponsor segments in YouTube videos
- [LavaSrc plugin](https://github.com/TopiSenpai/LavaSrc) adds Spotify, Apple Music & Deezer(native play) support
- [DuncteBot plugin](https://github.com/DuncteBot/skybot-lavalink-plugin) adds additional source managers that are not widely used
- [Extra Filter plugin](https://github.com/rohank05/lavalink-filter-plugin) adds additional audio filters to lavalink
- [XM plugin](https://github.com/esmBot/lava-xm-plugin) adds support for various [music tracker module](https://en.wikipedia.org/wiki/Module_file) formats

Lavalink loads all .jar files placed in the `plugins` directory, which you may need to create yourself. Lavalink can
also download plugin .jar files automatically by editing the configuration file. See the respective plugin repository
for instructions.

You can add your own plugin by submitting a pull-request to this file.

## Developing your own plugin

> **Note:**  
> If your plugin is developed in Kotlin make sure you are using **Kotlin v1.8.22**

Follow [these steps](https://github.com/lavalink-devs/lavalink-plugin-template#how-to-use-this-template) to setup a new Lavalink plugin

Now you can start writing your plugin. You can test your plugin against Lavalink by running Gradle with the
`:runLavalink` Gradle task. The [Gradle plugin documentation](https://github.com/lavalink-devs/lavalink-gradle-plugin#running-the-plugin) might be helpful.

Lavalink has a [plugin API](https://javadoc.io/doc/dev.arbjerg.lavalink/plugin-api/latest/dev/arbjerg/lavalink/api/package-summary.html) which you can integrate with. The API is
provided [as an artifact](https://central.sonatype.com/artifact/dev.arbjerg.lavalink/plugin-api) and is used by the template. It is also possible to integrate with internal parts og Lavalink,
but this is not recommended. Instead, open an issue or pull-request to change the API.

Lavalink is configured by plugins using the Spring Boot framework using Spring annotations.

You can define custom REST endpoints and configuration file properties. See the Spring Boot documentation for
[Spring Web MVC](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.servlet) and
[type-safe configuration](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.external-config.typesafe-configuration-properties).

If you simply want to add a Lavaplayer `AudioSourceManager` to Lavalink it is sufficient to simply provide it as a bean.
For example:

```java
import org.springframework.stereotype.Service;

@Service
class MyAudioSourceManager implements AudioSourceManager {
    // ...
} 
```

Likewise, you can also add new MediaContainerProbe this way, to be used with the HTTP and local sources:

```java
import org.springframework.stereotype.Service;

@Service
class MyMediaContainerProbe implements MediaContainerProbe {
    // ...
} 
```

To intercept and modify existing REST endpoints, you can implement the `RestInterceptor` interface:

```java
import org.springframework.stereotype.Service;
import dev.arbjerg.lavalink.api.RestInterceptor;

@Service
class TestRequestInterceptor implements RestInterceptor {
    // ...
}
```

To add custom info to track and playlist JSON, you can implement the `AudioPluginInfoModifier` interface:

```java
import org.springframework.stereotype.Service;
import dev.arbjerg.lavalink.api.AudioPluginInfoModifier;

@Service
class TestAudioPluginInfoModifier implements AudioPluginInfoModifier {
	// ...
}
```
