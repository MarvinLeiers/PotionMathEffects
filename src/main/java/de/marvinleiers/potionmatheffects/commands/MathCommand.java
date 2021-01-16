package de.marvinleiers.potionmatheffects.commands;

import de.marvinleiers.marvinplugin.commands.RootCommand;
import de.marvinleiers.potionmatheffects.commands.subcommands.StartSubcommand;
import de.marvinleiers.potionmatheffects.commands.subcommands.StopSubcommand;

public class MathCommand extends RootCommand
{
    public MathCommand()
    {
        super("math");

        addSubcommand(new StartSubcommand());
        addSubcommand(new StopSubcommand());
    }
}
