/*
** 2016 March 11
**
** The author disclaims copyright to this source code. In place of
** a legal notice, here is a blessing:
**    May you do good and not evil.
**    May you find forgiveness for yourself and forgive others.
**    May you share freely, never taking more than you give.
 */
package com.TheRPGAdventurer.ROTD.cmd;

import com.TheRPGAdventurer.ROTD.client.gui.GuiDragonDebug;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.breeds.EnumDragonBreed;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.helper.DragonLifeStage;

import com.TheRPGAdventurer.ROTD.util.debugging.DebugSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class CommandDragonDebug extends CommandBaseNested implements IDragonModifier {
    
    public CommandDragonDebug() {
        addCommand(new CommandDragonLambda("toItem", dragon -> {
            dragon.getLifeStageHelper().transformToEgg();
        }));
        
        addCommand(new CommandDragonLambda("dumpNBT", dragon -> {
            File dumpFile = new File(Minecraft.getMinecraft().mcDataDir,
                    String.format("dragon_%08x.nbt", dragon.getEntityId()));

            try {
                NBTTagCompound nbt = dragon.serializeNBT();
                CompressedStreamTools.write(nbt, dumpFile);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }));
        
        addCommand(new CommandDragonLambda("toggleOverlay", (server, sender, args) -> {
            GuiDragonDebug.enabled = !GuiDragonDebug.enabled;
        }));

      addCommand(new CommandDragonLambda("parameter", (server, sender, args) -> {
        String paramName = args[0];
        double value = parseDouble(args[1], 0);
        DebugSettings.setDebugParameter(paramName,value);
      }));

      addCommand(new CommandDragonLambda("testBreeds", dragon -> {
            new Thread(() -> {
                try {
                    for (EnumDragonBreed breed : EnumDragonBreed.values()) {
                        dragon.setBreedType(breed);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException ex) {
                }
            }).start();
        }));
        
        addCommand(new CommandDragonLambda("testStages", dragon -> {
            new Thread(() -> {
                try {
                    for (DragonLifeStage stage : DragonLifeStage.values()) {
                        dragon.getLifeStageHelper().setLifeStage(stage);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException ex) {
                }
            }).start();
        }));
        
        addCommand(new CommandDragonLambda("testAge", dragon -> {
            dragon.getLifeStageHelper().setTicksSinceCreation(18000);
        }));
        
        addCommand(new CommandDragonLambda("testMount", (server, sender, args) -> {
            applyModifier(server, sender, dragon -> {
                if (!(sender instanceof EntityPlayerMP)) {
                    return;
                }

                EntityPlayerMP player = (EntityPlayerMP) sender;
                dragon.tamedFor(player, true);
                dragon.setSaddled(true);
                dragon.setCustomNameTag("Puff");
                player.startRiding(dragon);
            });
        }));
        
        addCommand(new CommandDragonLambda("kill", dragon -> {
            dragon.setHealth(0);
        }));
    }

    @Override
    public String getName() {
        return "debug";
    }
}
