package tk.shanebee.nbt;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.nbt.nms.NBTApi;

import java.io.IOException;

public class NBeeT extends JavaPlugin {

    private static NBTApi nbtApi;
    private PluginDescriptionFile desc = getDescription();

    @Override
    public void onEnable() {
        if ((Bukkit.getPluginManager().getPlugin("Skript") != null) && (Skript.isAcceptRegistrations())) {
            String nms = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            SkriptAddon addon = Skript.registerAddon(this);
            try {
                nbtApi = (NBTApi) Class.forName(NBeeT.class.getPackage().getName() + ".nms.NBT_" + nms).newInstance();
                sendColConsole("&bCompatible NMS version: " + nms);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                sendColConsole("&cSk-NBeeT is not supported on this version [" +
                        ChatColor.AQUA + nms + ChatColor.RED + "] and will now be disabled");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            try {
                addon.loadClasses("tk.shanebee.nbt", "elements");
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            sendColConsole("&aSuccessfully enabled v" + desc.getVersion());
            if (desc.getVersion().contains("Beta")) {
                sendColConsole("&eThis is a BETA build, things may not work as expected, please report any bugs on GitHub");
                sendColConsole("&ehttps://github.com/ShaneBeee/Sk-NBeeT/issues");
            }
        } else {
            sendColConsole("&cDependency Skript was not found, plugin disabling");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {}

    public static NBTApi getNBTApi() {
        return nbtApi;
    }

    private void sendColConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7[&bSk-NBeeT&7] " + message));
    }

}