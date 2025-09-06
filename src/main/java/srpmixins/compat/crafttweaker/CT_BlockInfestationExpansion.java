package srpmixins.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.IEventHandle;
import crafttweaker.api.event.IEventManager;
import crafttweaker.util.EventList;
import crafttweaker.util.IEventHandler;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@SuppressWarnings("unused")
@ZenExpansion("crafttweaker.events.IEventManager")
@ZenRegister
public class CT_BlockInfestationExpansion {
    private static final EventList<CT_BlockInfestationEvent> HANDLERS = new EventList<>();

    @ZenMethod
    public static IEventHandle onBlockInfestation(IEventManager manager, IEventHandler<CT_BlockInfestationEvent> ev) {
        return HANDLERS.add(ev);
    }

    public static boolean hasBlockHarvestDrops() {
        return HANDLERS.hasHandlers();
    }

    public static void publishBlockHarvestDrops(CT_BlockInfestationEvent event) {
        HANDLERS.publish(event);
    }

    @ZenMethod
    public static void clearBlockInfestationHandlers(IEventManager manager) {
        HANDLERS.clear();
    }
}