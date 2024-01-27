package me.basiqueevangelist.artsandcrafts.item;

import me.basiqueevangelist.artsandcrafts.ArtsAndCraftsSounds;
import me.basiqueevangelist.artsandcrafts.cca.HaircutComponent;
import me.basiqueevangelist.artsandcrafts.cca.ArtsAndCraftsCCA;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.world.World;

import java.util.UUID;

public class ScissorsItem extends Item {
    public ScissorsItem(Item.Settings settings) {
        super(settings);
    }

    static {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player.isSpectator()) return ActionResult.PASS;

            var stack = player.getStackInHand(hand);

            if (stack.getItem() instanceof ScissorsItem scissors) {
                return scissors.useOn(stack, player, entity, hand);
            }

            return ActionResult.PASS;
        });
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.IRON_INGOT);
    }

    public ActionResult useOn(ItemStack stack, PlayerEntity user, Entity entity, Hand hand) {
        if (hand == Hand.OFF_HAND) return ActionResult.PASS;

        if (entity instanceof EnderDragonPart part) entity = part.owner;

        ItemStack offStack = user.getOffHandStack();
        UUID haircutId = Util.NIL_UUID;
        HaircutComponent component = entity.getComponent(ArtsAndCraftsCCA.HAIRCUT);

        if (offStack.isOf(ArtsAndCraftsItems.TEMPLATE)) {
            haircutId = offStack.getOr(TemplateItem.HAIRCUT, Util.NIL_UUID);
        }

        if (component.haircutId().equals(haircutId))
            return ActionResult.FAIL;

        user.getWorld().playSoundFromEntity(user, entity, ArtsAndCraftsSounds.SCISSORS_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);

        if (user.getWorld().isClient) return ActionResult.SUCCESS;

        component.setHaircutId(haircutId);
        stack.damage(1, user, player -> player.sendToolBreakStatus(hand));

        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (hand == Hand.OFF_HAND) return TypedActionResult.pass(stack);
        if (!user.isSneaking()) return TypedActionResult.pass(stack);

        ItemStack offStack = user.getOffHandStack();
        UUID haircutId = Util.NIL_UUID;
        HaircutComponent component = user.getComponent(ArtsAndCraftsCCA.HAIRCUT);

        if (offStack.isOf(ArtsAndCraftsItems.TEMPLATE)) {
            haircutId = offStack.getOr(TemplateItem.HAIRCUT, Util.NIL_UUID);
        }

        if (component.haircutId().equals(haircutId))
            return TypedActionResult.fail(stack);

        user.getWorld().playSoundFromEntity(user, user, ArtsAndCraftsSounds.SCISSORS_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);

        if (user.getWorld().isClient) return TypedActionResult.success(stack);

        component.setHaircutId(haircutId);
        stack.damage(1, user, player -> player.sendToolBreakStatus(hand));

        return TypedActionResult.success(stack);
    }
}