/*
 * Author: ptrdvrk
 * https://opensource.org/licenses/MIT
 */
package net.ptrdvrk.appmap.spigot;

import net.ptrdvrk.appmap.SimpleAppMapRecorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class AppMapCommand implements CommandExecutor {
    AppMapPlugin plugin;

    public AppMapCommand(AppMapPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();
        if (!cmdName.startsWith("appmap")) {
            return false;
        }

        if(!(sender.isOp())) {
            return true;
        }

        int args_count = args.length;

        if (args_count > 0 && args[0].startsWith("agent?")) {
            if(!SimpleAppMapRecorder.isAgentPresent()) {
                sender.sendMessage("AppMap agent not found in JVM!");
                return true;
            }
            sender.sendMessage("AppMap agent present.");
        }

        else if (args_count > 0 && args[0].startsWith("start")) {
            if(!SimpleAppMapRecorder.isAgentPresent()) {
                failure(sender, "AppMap agent not found in JVM.", SimpleAppMapRecorder.getLastException());
                return true;
            }
            String appmapName = "minecraft-appmap";
            if(args_count > 1) appmapName = args[1];
            Object metadata = SimpleAppMapRecorder.createMetadata();
            if(metadata == null) {
                sender.sendMessage("AppMap agent not found in JVM.");
                return true;
            }
            SimpleAppMapRecorder.setMetadataProperty(metadata, SimpleAppMapRecorder.APPMAP_NAME, appmapName);
            SimpleAppMapRecorder.setMetadataProperty(metadata, SimpleAppMapRecorder.RECORDER_NAME, "MinecraftRecorder");

            boolean result = SimpleAppMapRecorder.startRecording(metadata);
            if (result) {
                sender.sendMessage("AppMap recording started.");
            } else {
                failure(sender,"AppMap recording not started", SimpleAppMapRecorder.getLastException());
            }

        }

        else if (args_count > 0 && args[0].startsWith("stop")) {

            boolean result = SimpleAppMapRecorder.stopRecording();
            if (result) {
                sender.sendMessage("AppMap recording stopped and AppMap saved.");
            } else {
                failure(sender,"AppMap recording not stopped", SimpleAppMapRecorder.getLastException());
            }
        }

        else if (args_count > 0 && args[0].startsWith("say")) {
            sender.sendMessage("Plugin " + plugin.getName() + " says: " +
                    String.join(" ", Arrays.copyOfRange(args, 1, args_count)));
        }

        else {
            sender.sendMessage("Usage: appmap agent? | start [AppMap-name] | stop | say [something]");
        }

        return true;
    }

    //send a failure message
    private void failure(CommandSender sender, String message, Throwable e) {
        sender.sendMessage("FAIL:" + message + ": " + (e == null ? "<no message>" : e.getMessage()));
    }
}
