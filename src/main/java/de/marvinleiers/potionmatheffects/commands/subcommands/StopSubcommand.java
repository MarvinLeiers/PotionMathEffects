package de.marvinleiers.potionmatheffects.commands.subcommands;

import de.marvinleiers.marvinplugin.commands.Subcommand;
import de.marvinleiers.marvinplugin.utils.Messages;
import de.marvinleiers.potionmatheffects.PotionMathEffects;
import org.bukkit.entity.Player;

public class StopSubcommand extends Subcommand
{
    @Override
    public String getName()
    {
        return "disable";
    }

    @Override
    public String getDescription()
    {
        return "Disable math-broadcasting";
    }

    @Override
    public String getSyntax()
    {
        return "/math disable";
    }

    @Override
    public String getPermission()
    {
        return "math.disable";
    }

    @Override
    public void execute(Player player, String[] strings)
    {
        if (!PotionMathEffects.getInstance().isBroadcastingEnabled())
        {
            player.sendMessage(Messages.get("already-disabled"));
            return;
        }

        PotionMathEffects.getInstance().setBroadcastingEnabled(false);

        player.sendMessage(Messages.get("broadcasting-disabled"));
    }
}
