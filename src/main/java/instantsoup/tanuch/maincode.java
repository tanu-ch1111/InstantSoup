package instantsoup.tanuch;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class maincode extends JavaPlugin implements Listener {
    private FileConfiguration config;

    @Override
    public void onEnable() {
        // Configの読み込み
        saveDefaultConfig();
        config = getConfig();

        // イベントの登録
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("instantsoup has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("instantsoup has been disabled!");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        // 手に持っているアイテムがきのこシチューか確認
        if (item != null && item.getType() == Material.MUSHROOM_SOUP) {
            event.setCancelled(true); // 通常のアイテム使用をキャンセル

            // 満腹度とHPを設定から取得
            int healthRestore = config.getInt("health-restore", 4);
            int hungerRestore = config.getInt("hunger-restore", 6);

            // 満腹度を回復
            int newHunger = Math.min(player.getFoodLevel() + hungerRestore, 20);
            player.setFoodLevel(newHunger);

            // HPを回復
            double newHealth = Math.min(player.getHealth() + healthRestore, player.getMaxHealth());
            player.setHealth(newHealth);

            // 手に持っているアイテムを消す
            player.setItemInHand(new ItemStack(Material.AIR));

        }
    }
}