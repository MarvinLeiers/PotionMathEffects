package de.marvinleiers.potionmatheffects.commands.subcommands;

import de.marvinleiers.marvinplugin.commands.Subcommand;
import de.marvinleiers.marvinplugin.utils.Messages;
import de.marvinleiers.potionmatheffects.PotionMathEffects;
import org.bukkit.entity.Player;

public class StartSubcommand extends Subcommand
{
    @Override
    public String getName()
    {
        return "enable";
    }

    @Override
    public String getDescription()
    {
        return "Enable math-broadcasting";
    }

    @Override
    public String getSyntax()
    {
        return "/math enable";
    }

    @Override
    public String getPermission()
    {
        return "math.enable";
    }

    @Override
    public void execute(Player player, String[] strings)
    {
        if (PotionMathEffects.getInstance().isBroadcastingEnabled())
        {
            player.sendMessage(Messages.get("already-enabled"));
            return;
        }

        PotionMathEffects.getInstance().setBroadcastingEnabled(true);

        player.sendMessage(Messages.get("broadcasting-enabled"));
    }
}
