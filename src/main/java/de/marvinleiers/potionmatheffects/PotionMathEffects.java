package de.marvinleiers.potionmatheffects;

import de.marvinleiers.customconfig.CustomConfig;
import de.marvinleiers.marvinplugin.MarvinPlugin;
import de.marvinleiers.marvinplugin.utils.Messages;
import de.marvinleiers.potionmatheffects.commands.MathCommand;
import de.marvinleiers.potionmatheffects.listener.MathListener;
import de.marvinleiers.potionmatheffects.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public final class PotionMathEffects extends MarvinPlugin
{
    private static Random random = new Random();
    private static CustomConfig customConfig;
    private boolean isBroadcastingEnabled;
    private double currentSolution = 0;
    private String currentName;
    private PotionEffect[] potionEffect = null;
    private int delayInSeconds;
    private int taskId;

    @Override
    public void onEnable()
    {
        super.onEnable();

        customConfig = new CustomConfig(this.getDataFolder().getPath() + "/config.yml");

        customConfig.addDefault("difficulty", 1);
        customConfig.addDefault("delay-in-seconds", 60);
        customConfig.addDefault("this-many-answers-allowed", 1);
        customConfig.addDefault("effect-duration-in-seconds", 30);

        this.isBroadcastingEnabled = customConfig.isSet("broadcasting-enabled") && customConfig.getConfig().getBoolean("broadcasting-enabled");
        this.delayInSeconds = customConfig.getInt("delay-in-seconds") * 20;

        add("correct-answer", "&6<v> &7gave the correct answer.");
        add("time-is-up", "&7Time is &bup&7!");
        add("dont-ruin-the-game", "&c<v>, stop ruining the game for others... You already got the right answer!!");
        add("broadcast-format", "&e<v> &7(&b<v>&7)");
        add("broadcasting-disabled", "&7Broadcasting has been &cdisabled!");
        add("broadcasting-enabled", "&7Broadcasting has been &aenabled!");
        add("already-disabled", "&7Broadcasting is already &cdisabled!");
        add("already-enabled", "&7Broadcasting is already &aenabled!");

        new MathCommand();

        this.getServer().getPluginManager().registerEvents(new MathListener(), this);

        startBroadcasting();
    }

    public void setCurrentSolution(double solution)
    {
        currentSolution = solution;
    }

    public void startBroadcasting()
    {
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            @Override
            public void run()
            {
                if (isBroadcastingEnabled())
                {
                    potionEffect = createPotionEffect();

                    Bukkit.broadcastMessage(Messages.get("broadcast-format", MathUtils.generateProblem(), currentName));

                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            Bukkit.broadcastMessage(Messages.get("time-is-up"));
                            MathListener.players.clear();
                            MathListener.alreadyAnswered = 0;

                            setCurrentSolution(29384135);
                        }
                    }.runTaskLater(PotionMathEffects.getInstance(), 600 % delayInSeconds);
                }
            }
        }, delayInSeconds, delayInSeconds);
    }

    private PotionEffect[] createPotionEffect()
    {
        boolean basic = random.nextBoolean();

        if (basic)
        {
            PotionEffect[] effect = new PotionEffect[]{PotionEffectType.values()[random.nextInt(PotionEffectType.values().length)].createEffect(
                    PotionMathEffects.getCustomConfig().getInt("effect-duration-in-seconds") * 20, 0)};
            currentName = beautifyName(effect[0].getType().getName());

            return effect;
        }
        else
        {
            String[] custom = {"JUMP", "SPEED", "TURTLE_MASTER", "TURTLE_MASTER_TWO", "HEAL", "HARM", "POISON",
                    "REGENERATION", "INCREASE_DAMAGE"};

            String customType = custom[random.nextInt(custom.length)];

            System.out.println(customType);

            PotionEffectType type = PotionEffectType.getByName(customType);
            PotionEffect[] effect = {};

            int duration = PotionMathEffects.getCustomConfig().getInt("effect-duration-in-seconds") * 20;

            if (customType.equals("TURTLE_MASTER"))
            {
                currentName = "Turtle Master I";
                effect = new PotionEffect[]{PotionEffectType.SLOW.createEffect(duration, 3),
                        PotionEffectType.DAMAGE_RESISTANCE.createEffect(duration, 2)};

                return effect;
            }
            else if (customType.equals("TURTLE_MASTER_TWO"))
            {
                currentName = "Turtle Master II";
                effect = new PotionEffect[]{PotionEffectType.SLOW.createEffect(duration, 5),
                        PotionEffectType.DAMAGE_RESISTANCE.createEffect(duration, 3)};

                return effect;
            }

            currentName = beautifyName(type.getName()) + " II";

            return new PotionEffect[] {type.createEffect(PotionMathEffects.getCustomConfig().getInt("effect-duration-in-seconds")
                    * 20, 1)};
        }
    }

    public void setDelayInSeconds(int delay)
    {
        if (delayInSeconds == delay)
            return;

        Bukkit.getScheduler().cancelTask(taskId);
        this.delayInSeconds = delay * 20;
        startBroadcasting();
    }

    public void setBroadcastingEnabled(boolean flag)
    {
        this.isBroadcastingEnabled = flag;

        customConfig.set("broadcasting-enabled", flag);
    }

    public boolean isBroadcastingEnabled()
    {
        return isBroadcastingEnabled;
    }

    public double getCurrentSolution()
    {
        return currentSolution;
    }

    public PotionEffect[] getPotionEffectType()
    {
        return potionEffect;
    }

    public static PotionMathEffects getInstance()
    {
        return getPlugin(PotionMathEffects.class);
    }

    public static CustomConfig getCustomConfig()
    {
        return customConfig;
    }

    private String beautifyName(String str)
    {
        str = str.toLowerCase().replace("_", " ");
        String name = "";

        for (String word : str.split(" "))
        {
            name += word.substring(0, 1).toUpperCase() + word.substring(1) + " ";
        }

        return name.trim();
    }
}
