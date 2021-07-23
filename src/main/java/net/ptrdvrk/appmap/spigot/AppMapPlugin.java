/*
 * Author: ptrdvrk
 * https://opensource.org/licenses/MIT
 */
package net.ptrdvrk.appmap.spigot;

import org.bukkit.plugin.java.JavaPlugin;

/*
 * AppMapPlugin is simple AppMap recorder for Minecraft Java multiplayer servers.
 * AppMap is a dev tool for mapping custom plugins. Do not deploy to production servers!
 *
 * Installation:
 *  1. Setup appmap-java agent, see https://appland.com/docs/reference/appmap-java.html
 *  2. Build the plugin with mvn install. JDK 16 recommended, tested with OpenJDK 16.0.1
 *  3. Copy target/AppMapPlugin-?.?.?.jar to minecraft_home/plugins to install
 *  4. Start server with java -javaagent:appmap.jar -jar server.jar
 *  5. In the console, run command appmap help for instructions
 *  6. Interact with recorded AppMaps in AppMap for VSCode or JetBrains, or in AppMap Cloud
 *
 *  Join the AppLand Discord server and share your experience with the AppMap community!
 *   -> https://discord.com/invite/N9VUap6
 */

public class AppMapPlugin extends JavaPlugin {
    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        getCommand("appmap").setExecutor(new AppMapCommand(this));
    }

    //dumb logic for tests and demos
    public String say(String what) {
        return what;
    }
}
