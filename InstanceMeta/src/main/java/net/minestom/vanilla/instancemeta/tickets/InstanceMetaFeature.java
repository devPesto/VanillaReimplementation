package net.minestom.vanilla.instancemeta.tickets;

import net.minestom.server.ServerProcess;
import net.minestom.server.event.instance.InstanceTickEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.NamespaceID;
import net.minestom.vanilla.VanillaReimplementation;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class InstanceMetaFeature implements VanillaReimplementation.Feature {

    public void hook(@NotNull VanillaReimplementation vri) {
        new Logic().hook(vri);
    }

    @Override
    public @NotNull NamespaceID namespaceID() {
        return NamespaceID.from("vri:instancemeta");
    }

    private static class Logic {

        private final @NotNull Map<Instance, TicketManager> instance2TicketManager =
                Collections.synchronizedMap(new WeakHashMap<>());

        private Logic() {
        }

        private void hook(@NotNull VanillaReimplementation vri) {
            vri.process().eventHandler().addListener(InstanceTickEvent.class, event -> tickInstance(event.getInstance()));
        }

        // Process all future tickets
        private void tickInstance(@NotNull Instance instance) {
            TicketManager ticketManager = instance2TicketManager.computeIfAbsent(instance, ignored -> new TicketManager());

            List<TicketManager.Ticket> waitingForceLoads = instance.getTag(TicketManager.WAITING_TICKETS_TAG);
            if (waitingForceLoads == null) {
                return;
            }

            for (TicketManager.Ticket waitingForceLoad : waitingForceLoads) {
                ticketManager.addTicket(waitingForceLoad);
            }

            instance.setTag(TicketManager.WAITING_TICKETS_TAG, List.of());
        }
    }
}
