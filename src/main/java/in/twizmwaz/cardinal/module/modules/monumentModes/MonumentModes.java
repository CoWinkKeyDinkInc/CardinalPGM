package in.twizmwaz.cardinal.module.modules.monumentModes;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

public class MonumentModes implements TaskedModule {

    private final int after;
    private final Material material;
    private final int damageValue;
    private final String name;

    private boolean ran;

    public MonumentModes(final int after, final Material material, final int damageValue, final String name) {
        this.after = after;
        this.material = material;
        this.damageValue = damageValue;
        this.name = name;

        this.ran = false;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if (!this.ran && MatchTimer.getTimeInSeconds() >= this.after) {
                for (CoreObjective core : GameHandler.getGameHandler().getMatch().getModules().getModules(CoreObjective.class)) {
                    if (core.changesModes()) {
                        for (Block block : core.getCore()) {
                            if (core.partOfObjective(block)) {
                                block.setType(this.material);
                                block.setData((byte) this.damageValue);
                            }
                        }
                        core.setMaterial(this.material, this.damageValue);
                    }
                }
                for (DestroyableObjective destroyable : GameHandler.getGameHandler().getMatch().getModules().getModules(DestroyableObjective.class)) {
                    if (destroyable.changesModes()) {
                        for (Block block : destroyable.getMonument()) {
                            if (destroyable.partOfObjective(block)) {
                                block.setType(this.material);
                                block.setData((byte) this.damageValue);
                            }
                        }
                        destroyable.setMaterial(this.material, this.damageValue);
                    }
                }
                Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "> > > > " + ChatColor.RED + name + ChatColor.DARK_AQUA + " < < < <");
                this.ran = true;
            }
        }
    }

}
