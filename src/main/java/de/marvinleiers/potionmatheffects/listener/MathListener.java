package de.marvinleiers.potionmatheffects.listener;

import de.marvinleiers.marvinplugin.utils.Messages;
import de.marvinleiers.potionmatheffects.PotionMathEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class MathListener implements Listener
{
    public static int alreadyAnswered = 1;
    public static ArrayList<Player> players = new ArrayList<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        if (!PotionMathEffects.getInstance().isBroadcastingEnabled())
            return;

        Player player = event.getPlayer();

        double solution = PotionMathEffects.getInstance().getCurrentSolution();

        if (String.valueOf(solution).startsWith(event.getMessage()))
        {
            if (alreadyAnswered > PotionMathEffects.getCustomConfig().getInt("this-many-answers-allowed"))
            {
                PotionMathEffects.getInstance().setCurrentSolution(-29384135);
                players.clear();
                alreadyAnswered = 0;
            }
            else if (!players.contains(player))
            {
                Bukkit.broadcastMessage(Messages.get("correct-answer", player.getName()));
                players.add(player);
                event.setCancelled(true);
                alreadyAnswered += 1;

                new BukkitRunnable(){
                    @Override
                    public void run()
                    {
                        for (int i = 0; i < PotionMathEffects.getInstance().getPotionEffectType().length; i++)
                            player.addPotionEffect(PotionMathEffects.getInstance().getPotionEffectType()[i]);
                    }
                }.runTask(PotionMathEffects.getInstance());
            }
            else
            {
                player.sendMessage(Messages.get("dont-ruin-the-game", player.getName()));
                event.setCancelled(true);
            }
        }
    }
}
