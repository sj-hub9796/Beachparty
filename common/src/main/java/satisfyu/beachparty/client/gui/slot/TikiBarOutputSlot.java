package satisfyu.beachparty.client.gui.slot;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import satisfyu.beachparty.entity.TikiBarBlockEntity;

public class TikiBarOutputSlot extends Slot {

    private final Player player;
    private int amount;

    public TikiBarOutputSlot(Player player, Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return super.mayPlace(stack) && stack.getCount() <= this.getMaxStackSize(stack);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 64;
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.amount += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.amount += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.level, this.player, this.amount);
        if (this.player instanceof ServerPlayer && this.container instanceof TikiBarBlockEntity && player.level instanceof ServerLevel) {
            ((TikiBarBlockEntity)this.container).dropExperience((ServerLevel) this.player.level, player.position());
        }
        this.amount = 0;
    }
}
