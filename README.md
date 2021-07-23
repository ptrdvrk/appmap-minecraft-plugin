## Summary

AppMapPlugin is a simple AppMap recorder for Minecraft Java Edition multiplayer servers. AppMap is a **dev tool**
for mapping and visualization of traces of executed code. See [AppMap documentation](https://appland.com/docs/)
for more details.

**This plugin is intended for use in dev environments, do not deploy to production Minecraft servers!**

## Instructions
1. Setup the `appmap-java` agent in your environment, see https://appland.com/docs/reference/appmap-java.html
2. Build the plugin with `mvn clean install`. JDK 16 recommended, tested with OpenJDK 16.0.1.
3. Copy `target/AppMapPlugin-?.?.?.jar` to `minecraft_home/plugins` to install
4. Start the Minecraft server with `java -javaagent:appmap.jar -jar server.jar`
5. In the Minecraft server console, run command `appmap help` for instructions
6. Interact with recorded AppMaps in [AppMap for VSCode or JetBrains](https://appland.com/docs/quickstart),
   or in AppMap Cloud

Join the [AppLand Discord server](https://discord.com/invite/N9VUap6) and share your experience with the AppMap community!

@ptrdvrk

