package vn.haohan.soulanchor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class SoulAnchorPlugin extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {
    private NamespacedKey itemTypeKey;
    private NamespacedKey anchorIdKey;
    private NamespacedKey anchorNameKey;
    private NamespacedKey trustedPlayersKey;
    private NamespacedKey trustTargetKey;
    private NamespacedKey recipeKey;
    private File anchorsFile;
    private File messagesFile;
    private FileConfiguration anchorsConfig;
    private FileConfiguration messages;
    private final Map<UUID, Anchor> anchorsById = new HashMap<>();
    private final Map<String, UUID> anchorIdsByLocation = new HashMap<>();
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();
    private final Map<UUID, Warmup> warmups = new ConcurrentHashMap<>();
    private final List<UUID> idleParticleAnchorOrder = new ArrayList<>();
    private BukkitTask idleParticlesTask;
    private int idleParticleCursor;

    @Override
    public void onEnable() {
        itemTypeKey = new NamespacedKey(this, "item_type");
        anchorIdKey = new NamespacedKey(this, "anchor_id");
        anchorNameKey = new NamespacedKey(this, "anchor_name");
        trustedPlayersKey = new NamespacedKey(this, "trusted_players");
        trustTargetKey = new NamespacedKey(this, "trust_target");
        recipeKey = NamespacedKey.fromString(getConfig().getString("item.id", "haohansmp:soul_anchor"));
        if (recipeKey == null) {
            recipeKey = new NamespacedKey(this, "soul_anchor");
        }

        saveDefaultConfig();
        migrateLegacyConfig();
        saveResource("messages.yml", false);
        messagesFile = new File(getDataFolder(), "messages.yml");
        loadMessages();
        anchorsFile = new File(getDataFolder(), "anchors.yml");
        anchorsConfig = YamlConfiguration.loadConfiguration(anchorsFile);

        loadAnchors();
        registerRecipe();

        Objects.requireNonNull(getCommand("soulanchor")).setExecutor(this);
        Objects.requireNonNull(getCommand("soulanchor")).setTabCompleter(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        startIdleParticles();
        getLogger().info("Loaded " + anchorsById.size() + " Soul Anchors.");
    }

    private void migrateLegacyConfig() {
        boolean changed = false;
        String material = getConfig().getString("item.material", "BARRIER");
        if (material.equalsIgnoreCase("GRINDSTONE") || material.equalsIgnoreCase("JIGSAW")) {
            getConfig().set("item.material", "BARRIER");
            getLogger().info("Migrated Soul Anchor base material from " + material + " to BARRIER.");
            changed = true;
        }
        if (getConfig().getDouble("visuals.scale-y", 0.877D) == 1.6D) {
            getConfig().set("visuals.scale-y", 0.877D);
            changed = true;
        }
        if (getConfig().getDouble("visuals.interaction-height", 1.1D) == 2.0D) {
            getConfig().set("visuals.interaction-height", 1.1D);
            changed = true;
        }
        if (getConfig().getLong("visuals.idle-particle-interval-ticks", 20L) == 20L) {
            getConfig().set("visuals.idle-particle-interval-ticks", 40L);
            changed = true;
        }
        if (!getConfig().contains("visuals.idle-particle-batch-size")) {
            getConfig().set("visuals.idle-particle-batch-size", 32);
            changed = true;
        }
        List<String> lore = new ArrayList<>(getConfig().getStringList("item.lore"));
        boolean loreChanged = false;
        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).equals("&fPlus: &b1 Echo Shard")) {
                lore.set(i, "&fEcho Shard: &b1 beyond 2000 blocks");
                loreChanged = true;
                changed = true;
            }
        }
        if (loreChanged) {
            getConfig().set("item.lore", lore);
        }
        if (changed) {
            saveConfig();
        }
    }

    @Override
    public void onDisable() {
        for (Warmup warmup : new ArrayList<>(warmups.values())) {
            warmup.cancel(false);
        }
        warmups.clear();
        if (idleParticlesTask != null) {
            idleParticlesTask.cancel();
        }
        saveAnchors();
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        if (getConfig().getBoolean("recipe.discover-automatically", true)) {
            event.getPlayer().discoverRecipe(recipeKey);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlace(BlockPlaceEvent event) {
        ItemStack handItem = event.getItemInHand();
        if (!isSoulAnchorItem(handItem)) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission("soulanchor.place")) {
            event.setCancelled(true);
            send(player, "no-permission");
            return;
        }
        if (isWorldBlocked(event.getBlockPlaced().getWorld())) {
            event.setCancelled(true);
            send(player, "world-disabled");
            return;
        }

        int limit = getAnchorLimit(player);
        if (limit >= 0 && ownedAnchors(player.getUniqueId()).size() >= limit) {
            event.setCancelled(true);
            send(player, "anchor-limit", "{limit}", String.valueOf(limit));
            return;
        }

        Location location = event.getBlockPlaced().getLocation();
        location.getBlock().setType(getAnchorBlockMaterial(), false);

        String storedName = readPortableAnchorName(handItem);
        String name = storedName == null ? nextDefaultName(player.getUniqueId()) : storedName;
        Set<UUID> trustedPlayers = readPortableTrustedPlayers(handItem);
        trustedPlayers.remove(player.getUniqueId());
        Anchor anchor = new Anchor(UUID.randomUUID(), player.getUniqueId(), name, location, 0F, 0F,
                Instant.now().toEpochMilli(), trustedPlayers, null, null);
        anchor = spawnVisuals(anchor);
        anchorsById.put(anchor.id(), anchor);
        anchorIdsByLocation.put(locationKey(anchor.location()), anchor.id());
        idleParticleAnchorOrder.add(anchor.id());
        saveAnchors();

        send(player, "anchor-placed", "{name}", anchor.name());
        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.8F, 1.1F);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getAction().isLeftClick()) {
            return;
        }
        Anchor anchor = anchorAt(event.getClickedBlock()).orElse(null);
        if (anchor == null) {
            return;
        }

        event.setCancelled(true);
        Player player = event.getPlayer();
        if (!player.hasPermission("soulanchor.use")) {
            send(player, "no-permission");
            return;
        }
        if (!canAccessAnchor(player.getUniqueId(), anchor) && !player.hasPermission("soulanchor.admin")) {
            send(player, "anchor-not-owner");
            return;
        }
        openMenu(player, anchor);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Anchor anchor = anchorFromEntity(event.getRightClicked()).orElse(null);
        if (anchor == null) {
            return;
        }

        event.setCancelled(true);
        Player player = event.getPlayer();
        if (!player.hasPermission("soulanchor.use")) {
            send(player, "no-permission");
            return;
        }
        if (!canAccessAnchor(player.getUniqueId(), anchor) && !player.hasPermission("soulanchor.admin")) {
            send(player, "anchor-not-owner");
            return;
        }
        openMenu(player, anchor);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof TrustMenuHolder holder) {
            handleTrustMenuClick(event, holder);
            return;
        }
        if (event.getInventory().getHolder() instanceof SharedAnchorMenuHolder holder) {
            handleSharedAnchorMenuClick(event, holder);
            return;
        }
        if (!(event.getInventory().getHolder() instanceof AnchorMenuHolder holder)) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType().isAir()) {
            return;
        }
        UUID targetId = readAnchorId(item).orElse(null);
        if (targetId == null) {
            if (event.getRawSlot() == 22 && item.getType() == Material.ENDER_PEARL) {
                Anchor source = anchorsById.get(holder.sourceAnchorId());
                if (source != null) {
                    openSharedAnchorMenu(player, source, 0);
                }
                return;
            }
            if (item.getType() == Material.PLAYER_HEAD) {
                Anchor source = anchorsById.get(holder.sourceAnchorId());
                if (source != null && source.ownerId().equals(player.getUniqueId())) {
                    openTrustMenu(player, source, 0);
                }
                return;
            }
            if (item.getType() == Material.BARRIER) {
                player.closeInventory();
            }
            return;
        }
        Anchor source = anchorsById.get(holder.sourceAnchorId());
        Anchor target = anchorsById.get(targetId);
        if (source == null || target == null || source.id().equals(target.id())) {
            send(player, "teleport-failed");
            return;
        }
        player.closeInventory();
        requestTeleport(player, source.id(), target.id());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof AnchorMenuHolder
                || event.getInventory().getHolder() instanceof SharedAnchorMenuHolder
                || event.getInventory().getHolder() instanceof TrustMenuHolder) {
            event.getInventory().clear();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event) {
        Anchor anchor = anchorAt(event.getBlock()).orElse(null);
        if (anchor == null) {
            return;
        }
        Player player = event.getPlayer();
        boolean owner = anchor.ownerId().equals(player.getUniqueId());
        if (!owner || !player.hasPermission("soulanchor.break.own")) {
            event.setCancelled(true);
            send(player, "anchor-not-owner");
            return;
        }

        removeAnchor(anchor.id());
        cancelWarmupsTouching(anchor.id());
        if (getConfig().getBoolean("breaking.drop-item", true)) {
            event.setDropItems(false);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(0.5, 0.5, 0.5),
                    createPortableAnchorItem(anchor));
        }
        send(player, "anchor-broken", "{name}", anchor.name());
        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 0.8F, 1F);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPhysics(BlockPhysicsEvent event) {
        if (anchorAt(event.getBlock()).isPresent()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFluid(BlockFromToEvent event) {
        if (anchorAt(event.getToBlock()).isPresent()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (event.getBlocks().stream().anyMatch(block -> anchorAt(block).isPresent())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (event.getBlocks().stream().anyMatch(block -> anchorAt(block).isPresent())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> anchorAt(block).isPresent());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(block -> anchorAt(block).isPresent());
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        Warmup warmup = warmups.get(event.getPlayer().getUniqueId());
        if (warmup == null || event.getTo() == null) {
            return;
        }
        double tolerance = getConfig().getDouble("teleport.movement-tolerance", 0.5D);
        if (!sameWorld(warmup.startLocation, event.getTo())
                || warmup.startLocation.distanceSquared(event.getTo()) > tolerance * tolerance) {
            warmup.cancel(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            Warmup warmup = warmups.get(player.getUniqueId());
            if (warmup != null) {
                warmup.cancel(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDealDamage(EntityDamageByEntityEvent event) {
        Anchor anchor = anchorFromEntity(event.getEntity()).orElse(null);
        if (anchor != null) {
            event.setCancelled(true);
            Player breaker = damagerPlayer(event.getDamager());
            if (breaker == null) {
                return;
            }
            boolean owner = anchor.ownerId().equals(breaker.getUniqueId());
            if (!owner || !breaker.hasPermission("soulanchor.break.own")) {
                send(breaker, "anchor-not-owner");
                return;
            }
            removeAnchor(anchor.id());
            cancelWarmupsTouching(anchor.id());
            anchor.location().getBlock().setType(Material.AIR, false);
            if (getConfig().getBoolean("breaking.drop-item", true)) {
                breaker.getWorld().dropItemNaturally(anchor.location().clone().add(0.5, 0.5, 0.5),
                        createPortableAnchorItem(anchor));
            }
            send(breaker, "anchor-broken", "{name}", anchor.name());
            return;
        }

        Player player = damagerPlayer(event.getDamager());
        if (player == null) {
            return;
        }
        Warmup warmup = warmups.get(player.getUniqueId());
        if (warmup != null) {
            warmup.cancel(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH
                || !getConfig().getBoolean("echo-shard-sources.fishing.enabled", true)
                || !(event.getCaught() instanceof Item caughtItem)) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack rod = event.getHand() == EquipmentSlot.OFF_HAND
                ? player.getInventory().getItemInOffHand()
                : player.getInventory().getItemInMainHand();
        int luckLevel = rod.getEnchantmentLevel(Enchantment.LUCK_OF_THE_SEA);
        double baseChance = getConfig().getDouble("echo-shard-sources.fishing.base-chance", 0.01D);
        double luckBonus = getConfig().getDouble("echo-shard-sources.fishing.luck-bonus-per-level", 0.0025D);
        double chance = Math.max(0D, Math.min(1D, baseChance + luckLevel * luckBonus));
        if (ThreadLocalRandom.current().nextDouble() >= chance) {
            return;
        }

        caughtItem.setItemStack(new ItemStack(Material.ECHO_SHARD));
        send(player, "echo-shard-fished", "{chance}", formatPercent(chance));
        player.playSound(player.getLocation(), Sound.BLOCK_SCULK_CATALYST_BLOOM, 0.8F, 1.2F);
    }

    @EventHandler
    public void onWardenDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() != org.bukkit.entity.EntityType.WARDEN
                || !getConfig().getBoolean("echo-shard-sources.warden.enabled", true)
                || event.getEntity().getKiller() == null) {
            return;
        }

        double chance = Math.max(0D, Math.min(1D,
                getConfig().getDouble("echo-shard-sources.warden.drop-chance", 0.15D)));
        if (ThreadLocalRandom.current().nextDouble() >= chance) {
            return;
        }

        int amount = Math.max(1, getConfig().getInt("echo-shard-sources.warden.amount", 1));
        event.getDrops().add(new ItemStack(Material.ECHO_SHARD, amount));
        send(event.getEntity().getKiller(), "echo-shard-warden", "{amount}", String.valueOf(amount));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Warmup warmup = warmups.get(event.getPlayer().getUniqueId());
        if (warmup != null) {
            warmup.cancel(false);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                send(sender, "player-only");
                return true;
            }
            listAnchors(player, player);
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        if (sub.equals("give")) {
            return commandGive(sender, args);
        }
        if (sub.equals("list")) {
            return commandList(sender, args);
        }
        if (sub.equals("rename")) {
            return commandRename(sender, args);
        }
        if (sub.equals("share")) {
            return commandShare(sender, args);
        }
        if (sub.equals("reload")) {
            if (!sender.hasPermission("soulanchor.admin.reload")) {
                send(sender, "no-permission");
                return true;
            }
            reloadConfig();
            loadMessages();
            registerRecipe();
            refreshAnchorVisuals();
            restartIdleParticles();
            send(sender, "reloaded");
            return true;
        }
        sender.sendMessage(color("&3SoulAnchor &7commands: &f/soulanchor give|list|rename|share|reload"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], List.of("give", "list", "rename", "share", "reload"),
                    new ArrayList<>());
        }
        if (args.length == 2 && List.of("give", "list").contains(args[0].toLowerCase(Locale.ROOT))) {
            return null;
        }
        if (sender instanceof Player player && args.length == 2
                && List.of("rename", "share").contains(args[0].toLowerCase(Locale.ROOT))) {
            return StringUtil.copyPartialMatches(args[1],
                    ownedAnchors(player.getUniqueId()).stream().map(Anchor::name).toList(), new ArrayList<>());
        }
        if (sender instanceof Player && args.length >= 3 && args[0].equalsIgnoreCase("share")) {
            return StringUtil.copyPartialMatches(args[args.length - 1],
                    Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(), new ArrayList<>());
        }
        return List.of();
    }

    private boolean commandGive(CommandSender sender, String[] args) {
        if (!sender.hasPermission("soulanchor.admin.give")) {
            send(sender, "no-permission");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(color("&cUsage: /soulanchor give <player> [amount]"));
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(color("&cPlayer not found."));
            return true;
        }
        int amount = args.length >= 3 ? parseInt(args[2], 1) : 1;
        amount = Math.max(1, Math.min(64, amount));
        target.getInventory().addItem(createAnchorItem(amount)).values()
                .forEach(leftover -> target.getWorld().dropItemNaturally(target.getLocation(), leftover));
        send(sender, "given", "{amount}", String.valueOf(amount), "{player}", target.getName());
        return true;
    }

    private boolean commandList(CommandSender sender, String[] args) {
        if (args.length >= 2 && sender.hasPermission("soulanchor.admin")) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(color("&cPlayer not found."));
                return true;
            }
            listAnchors(sender, target);
            return true;
        }
        if (!(sender instanceof Player player)) {
            send(sender, "player-only");
            return true;
        }
        listAnchors(sender, player);
        return true;
    }

    private boolean commandRename(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            send(sender, "player-only");
            return true;
        }
        if (!player.hasPermission("soulanchor.rename")) {
            send(player, "no-permission");
            return true;
        }
        if (args.length < 3) {
            player.sendMessage(color("&cUsage: /soulanchor rename <anchor> <new-name>"));
            return true;
        }
        Anchor anchor = null;
        int newNameStart = -1;
        for (int split = args.length - 1; split >= 2; split--) {
            String anchorQuery = String.join(" ", List.of(args).subList(1, split));
            anchor = findOwnedAnchor(player.getUniqueId(), anchorQuery).orElse(null);
            if (anchor != null) {
                newNameStart = split;
                break;
            }
        }
        if (anchor == null) {
            send(player, "not-anchor");
            return true;
        }
        String name = sanitizeName(String.join(" ", List.of(args).subList(newNameStart, args.length)));
        Anchor renamed = anchor.withName(name);
        anchorsById.put(renamed.id(), renamed);
        saveAnchors();
        send(player, "anchor-renamed", "{name}", renamed.name());
        return true;
    }

    private boolean commandShare(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            send(sender, "player-only");
            return true;
        }
        if (!player.hasPermission("soulanchor.share")) {
            send(player, "no-permission");
            return true;
        }
        if (args.length < 3) {
            player.sendMessage(color("&cUsage: /soulanchor share <anchor> <player>"));
            return true;
        }

        String targetName = args[args.length - 1];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            send(player, "player-not-found");
            return true;
        }
        if (target.getUniqueId().equals(player.getUniqueId())) {
            send(player, "share-self");
            return true;
        }

        String anchorQuery = String.join(" ", List.of(args).subList(1, args.length - 1));
        Anchor anchor = findOwnedAnchor(player.getUniqueId(), anchorQuery).orElse(null);
        if (anchor == null) {
            send(player, "not-anchor");
            return true;
        }
        if (anchor.sharedWith().contains(target.getUniqueId())) {
            send(player, "anchor-already-shared", "{anchor}", anchor.name(), "{player}", target.getName());
            return true;
        }

        Anchor shared = anchor.withSharedPlayer(target.getUniqueId());
        anchorsById.put(shared.id(), shared);
        saveAnchors();
        send(player, "anchor-shared", "{anchor}", shared.name(), "{player}", target.getName());
        send(target, "anchor-shared-received", "{anchor}", shared.name(), "{player}", player.getName());
        return true;
    }

    private void listAnchors(CommandSender sender, Player owner) {
        List<Anchor> anchors = accessibleAnchors(owner.getUniqueId());
        int ownedCount = ownedAnchors(owner.getUniqueId()).size();
        int sharedCount = anchors.size() - ownedCount;
        sender.sendMessage(color("&3Soul Anchors available to &f" + owner.getName() + "&7 (owned: &f" + ownedCount
                + "/" + getAnchorLimit(owner) + "&7, shared: &f" + sharedCount + "&7)"));
        if (anchors.isEmpty()) {
            sender.sendMessage(color("&7No Soul Anchors yet."));
            return;
        }
        for (Anchor anchor : anchors) {
            Location loc = anchor.location();
            String access = anchor.ownerId().equals(owner.getUniqueId()) ? "" : " &d[shared]";
            sender.sendMessage(color("&b- &f" + anchor.name() + access + " &7" + loc.getWorld().getName() + " "
                    + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ()));
        }
    }

    private void requestTeleport(Player player, UUID sourceId, UUID targetId) {
        if (warmups.containsKey(player.getUniqueId())) {
            send(player, "warmup-cancelled");
            return;
        }
        long now = System.currentTimeMillis();
        long remaining = cooldowns.getOrDefault(player.getUniqueId(), 0L) - now;
        if (remaining > 0 && !player.hasPermission("soulanchor.bypass.cooldown")) {
            send(player, "cooldown", "{seconds}", String.valueOf((remaining + 999) / 1000));
            return;
        }

        Anchor source = anchorsById.get(sourceId);
        Anchor target = anchorsById.get(targetId);
        Validation validation = validateTeleport(player, source, target, false);
        if (!validation.ok()) {
            send(player, validation.messageKey(), validation.replacements());
            return;
        }

        int warmupSeconds = player.hasPermission("soulanchor.bypass.warmup") ? 0
                : getConfig().getInt("teleport.warmup-seconds", 3);
        if (warmupSeconds <= 0) {
            finishTeleport(player, sourceId, targetId);
            return;
        }

        send(player, "warmup-start", "{seconds}", String.valueOf(warmupSeconds));
        Warmup warmup = new Warmup(player, sourceId, targetId, player.getLocation().clone(), warmupSeconds);
        warmups.put(player.getUniqueId(), warmup);
        warmup.task = new BukkitRunnable() {
            int remainingTicks = warmupSeconds;

            @Override
            public void run() {
                if (!warmups.containsKey(player.getUniqueId())) {
                    cancel();
                    return;
                }
                if (remainingTicks > 0) {
                    send(player, "warmup-tick", "{seconds}", String.valueOf(remainingTicks));
                    player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation().add(0, 1, 0), 8, 0.4,
                            0.6, 0.4, 0.01);
                    remainingTicks--;
                    return;
                }
                warmups.remove(player.getUniqueId());
                cancel();
                finishTeleport(player, sourceId, targetId);
            }
        }.runTaskTimer(this, 0L, 20L);
    }

    private void finishTeleport(Player player, UUID sourceId, UUID targetId) {
        Anchor source = anchorsById.get(sourceId);
        Anchor target = anchorsById.get(targetId);
        Validation validation = validateTeleport(player, source, target, true);
        if (!validation.ok()) {
            send(player, validation.messageKey(), validation.replacements());
            return;
        }

        Cost cost = calculateCost(source.location(), target.location());
        Location destination = validation.safeDestination();

        if (!player.hasPermission("soulanchor.bypass.cost")) {
            removeEchoShards(player, cost.shards());
            player.giveExp(-cost.experiencePoints());
        }

        boolean teleported = player.teleport(destination);
        if (!teleported) {
            if (!player.hasPermission("soulanchor.bypass.cost")) {
                player.getInventory().addItem(new ItemStack(Material.ECHO_SHARD, cost.shards()));
                player.giveExp(cost.experiencePoints());
            }
            send(player, "teleport-failed");
            return;
        }

        int cooldownSeconds = getConfig().getInt("teleport.cooldown-seconds", 30);
        if (!player.hasPermission("soulanchor.bypass.cooldown")) {
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + cooldownSeconds * 1000L);
        }
        player.getWorld().spawnParticle(Particle.SCULK_SOUL, player.getLocation().add(0, 1, 0), 16, 0.5, 0.8, 0.5,
                0.02);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.9F, 1.15F);
        send(player, "teleport-success", "{anchor}", target.name());
    }

    private Validation validateTeleport(Player player, Anchor source, Anchor target, boolean requireSafeDestination) {
        if (source == null || target == null || source.id().equals(target.id())) {
            return Validation.fail("teleport-failed");
        }
        if ((!canAccessAnchor(player.getUniqueId(), source) || !canAccessAnchor(player.getUniqueId(), target))
                && !player.hasPermission("soulanchor.admin")) {
            return Validation.fail("anchor-not-owner");
        }
        if (!isAnchorStillPlaced(source) || !isAnchorStillPlaced(target)) {
            return Validation.fail("teleport-failed");
        }
        if (isWorldBlocked(target.location().getWorld())) {
            return Validation.fail("world-disabled");
        }
        if (!sameWorld(source.location(), target.location())
                && !getConfig().getBoolean("cross-dimension.enabled", true)) {
            return Validation.fail("teleport-failed");
        }

        Cost cost = calculateCost(source.location(), target.location());
        if (!player.hasPermission("soulanchor.bypass.cost")) {
            if (player.getLevel() < cost.requiredLevels()) {
                return Validation.fail("not-enough-levels", "{required}", String.valueOf(cost.requiredLevels()),
                        "{current}", String.valueOf(player.getLevel()));
            }
            if (countEchoShards(player) < cost.shards()) {
                return Validation.fail("not-enough-shards", "{amount}", String.valueOf(cost.shards()));
            }
        }

        if (requireSafeDestination) {
            target.location().getChunk().load(true);
            Location safe = findSafeLocation(target.location());
            if (safe == null) {
                return Validation.fail("unsafe-destination");
            }
            return Validation.ok(safe);
        }
        return Validation.ok(null);
    }

    private void openMenu(Player player, Anchor source) {
        Inventory inventory = Bukkit.createInventory(new AnchorMenuHolder(source.id()), 27,
                color("&3Soul Anchor Network"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, namedItem(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        List<Anchor> anchors = ownedAnchors(player.getUniqueId());
        int[] slots = { 11, 13, 15 };
        for (int i = 0; i < Math.min(slots.length, anchors.size()); i++) {
            Anchor target = anchors.get(i);
            inventory.setItem(slots[i], createTeleportIcon(source, target, null));
        }
        int sharedCount = sharedAnchors(player.getUniqueId()).size();
        inventory.setItem(4, namedItem(Material.ECHO_SHARD, "&bNetwork", List
                .of("&7Owned anchors: &f" + anchors.size() + "/" + getAnchorLimit(player),
                        "&7Shared with you: &f" + sharedCount)));
        if (source.ownerId().equals(player.getUniqueId()) && player.hasPermission("soulanchor.share")) {
            inventory.setItem(18, namedItem(Material.PLAYER_HEAD, "&bManage trust", List.of(
                    "&7Share &f" + source.name() + " &7with online players.",
                    "&7Trusted players: &f" + source.sharedWith().size(),
                    "", "&fClick to open")));
        }
        inventory.setItem(22, namedItem(Material.ENDER_PEARL, "&dShared anchors", List.of(
                "&7All teleport points shared with you.",
                "&7Available: &f" + sharedCount,
                "", "&fClick to open")));
        inventory.setItem(26, namedItem(Material.BARRIER, "&cClose"));
        player.openInventory(inventory);
    }

    private ItemStack createTeleportIcon(Anchor source, Anchor target, String ownerName) {
        Cost cost = calculateCost(source.location(), target.location());
        boolean current = source.id().equals(target.id());
        List<String> lore = new ArrayList<>();
        Location loc = target.location();
        if (ownerName != null) {
            lore.add("&dShared by: &f" + ownerName);
        }
        lore.add("&7World: &f" + loc.getWorld().getName());
        lore.add("&7Coords: &f" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
        if (current) {
            lore.add("");
            lore.add("&eYou are at this Soul Anchor.");
        } else {
            lore.add("&7Distance: &f" + formatDistance(source.location(), target.location()) + " blocks");
            lore.add("");
            String requirement = "&7Requires: &a" + cost.requiredLevels() + " levels";
            if (cost.shards() > 0) {
                requirement += " &7+ &b" + cost.shards() + " Echo Shard";
            }
            lore.add(requirement);
            lore.add("&7XP charged: &e" + cost.experiencePoints() + " points");
            lore.add("&fClick to teleport");
        }
        ItemStack icon = namedItem(current ? Material.LODESTONE : Material.RESPAWN_ANCHOR, "&b" + target.name(), lore);
        if (!current) {
            writeAnchorId(icon, target.id());
        }
        return icon;
    }

    private void openSharedAnchorMenu(Player player, Anchor source, int requestedPage) {
        List<SharedAnchorGroup> groups = sharedAnchorGroups(player.getUniqueId());
        int anchorCount = groups.stream().mapToInt(group -> group.anchors().size()).sum();
        int pageCount = Math.max(1, (groups.size() + 4) / 5);
        int page = Math.max(0, Math.min(requestedPage, pageCount - 1));
        Inventory inventory = Bukkit.createInventory(new SharedAnchorMenuHolder(source.id(), page), 54,
                color("&3Shared Soul Anchors"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, namedItem(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        int start = page * 5;
        for (int row = 0; row < 5 && start + row < groups.size(); row++) {
            SharedAnchorGroup group = groups.get(start + row);
            String ownerName = playerName(Bukkit.getOfflinePlayer(group.ownerId()));
            int rowStart = row * 9;
            inventory.setItem(rowStart + 1, namedItem(Material.NAME_TAG, "&d" + ownerName, List.of(
                    "&7Shared anchors: &f" + group.anchors().size())));
            int[] anchorSlots = { rowStart + 3, rowStart + 4, rowStart + 5 };
            for (int index = 0; index < Math.min(anchorSlots.length, group.anchors().size()); index++) {
                inventory.setItem(anchorSlots[index],
                        createTeleportIcon(source, group.anchors().get(index), ownerName));
            }
        }

        if (page > 0) {
            inventory.setItem(45, namedItem(Material.ARROW, "&bPrevious page"));
        }
        inventory.setItem(49, namedItem(Material.ENDER_PEARL, "&dShared anchors", List.of(
                "&7Available: &f" + anchorCount,
                "&7Players: &f" + groups.size(),
                "&7Page: &f" + (page + 1) + "/" + pageCount)));
        inventory.setItem(52, namedItem(Material.OAK_DOOR, "&eBack to owned anchors"));
        if (page + 1 < pageCount) {
            inventory.setItem(53, namedItem(Material.ARROW, "&bNext page"));
        } else {
            inventory.setItem(53, namedItem(Material.BARRIER, "&cClose"));
        }
        player.openInventory(inventory);
    }

    private void handleSharedAnchorMenuClick(InventoryClickEvent event, SharedAnchorMenuHolder holder) {
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        Anchor source = anchorsById.get(holder.sourceAnchorId());
        if (source == null || (!canAccessAnchor(player.getUniqueId(), source)
                && !player.hasPermission("soulanchor.admin"))) {
            player.closeInventory();
            send(player, "teleport-failed");
            return;
        }
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType().isAir()) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot == 45 && item.getType() == Material.ARROW) {
            openSharedAnchorMenu(player, source, holder.page() - 1);
            return;
        }
        if (slot == 52 && item.getType() == Material.OAK_DOOR) {
            openMenu(player, source);
            return;
        }
        if (slot == 53) {
            if (item.getType() == Material.ARROW) {
                openSharedAnchorMenu(player, source, holder.page() + 1);
            } else if (item.getType() == Material.BARRIER) {
                player.closeInventory();
            }
            return;
        }
        UUID targetId = readAnchorId(item).orElse(null);
        Anchor target = targetId == null ? null : anchorsById.get(targetId);
        if (target == null || !target.sharedWith().contains(player.getUniqueId())) {
            return;
        }
        player.closeInventory();
        requestTeleport(player, source.id(), target.id());
    }

    private void openTrustMenu(Player player, Anchor anchor, int requestedPage) {
        if (!anchor.ownerId().equals(player.getUniqueId()) || !player.hasPermission("soulanchor.share")) {
            send(player, "no-permission");
            return;
        }

        List<? extends Player> targets = Bukkit.getOnlinePlayers().stream()
                .filter(target -> !target.getUniqueId().equals(player.getUniqueId()))
                .sorted(Comparator.comparing(Player::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
        int pageCount = Math.max(1, (targets.size() + 44) / 45);
        int page = Math.max(0, Math.min(requestedPage, pageCount - 1));
        Inventory inventory = Bukkit.createInventory(new TrustMenuHolder(anchor.id(), page), 54,
                color("&3Trust: &f" + anchor.name()));

        int start = page * 45;
        for (int slot = 0; slot < 45 && start + slot < targets.size(); slot++) {
            Player target = targets.get(start + slot);
            boolean trusted = anchor.sharedWith().contains(target.getUniqueId());
            int used = ownedAnchors(target.getUniqueId()).size();
            int limit = getAnchorLimit(target);
            List<String> lore = new ArrayList<>();
            lore.add("&7Anchor: &f" + anchor.name());
            lore.add("&7Status: " + (trusted ? "&aTrusted" : "&cNot trusted"));
            lore.add("&7Owned anchors: &f" + used + "/" + (limit < 0 ? "unlimited" : limit));
            lore.add("");
            lore.add(trusted ? "&eClick to revoke access" : "&aClick to share this anchor");
            inventory.setItem(slot, playerHead(target, trusted ? "&a" + target.getName() : "&f" + target.getName(),
                    lore));
        }

        for (int slot = 45; slot < 54; slot++) {
            inventory.setItem(slot, namedItem(Material.GRAY_STAINED_GLASS_PANE, " "));
        }
        if (page > 0) {
            inventory.setItem(45, namedItem(Material.ARROW, "&bPrevious page"));
        }
        inventory.setItem(49, namedItem(Material.RESPAWN_ANCHOR, "&b" + anchor.name(), List.of(
                "&7Online players: &f" + targets.size(),
                "&7Trusted players: &f" + anchor.sharedWith().size(),
                "&7Page: &f" + (page + 1) + "/" + pageCount)));
        if (page + 1 < pageCount) {
            inventory.setItem(53, namedItem(Material.ARROW, "&bNext page"));
        } else {
            inventory.setItem(53, namedItem(Material.BARRIER, "&cBack"));
        }
        player.openInventory(inventory);
    }

    private void handleTrustMenuClick(InventoryClickEvent event, TrustMenuHolder holder) {
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        Anchor anchor = anchorsById.get(holder.anchorId());
        if (anchor == null || !anchor.ownerId().equals(player.getUniqueId())
                || !player.hasPermission("soulanchor.share")) {
            player.closeInventory();
            send(player, anchor == null ? "not-anchor" : "no-permission");
            return;
        }

        int slot = event.getRawSlot();
        if (slot == 45 && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.ARROW) {
            openTrustMenu(player, anchor, holder.page() - 1);
            return;
        }
        if (slot == 53 && event.getCurrentItem() != null) {
            if (event.getCurrentItem().getType() == Material.ARROW) {
                openTrustMenu(player, anchor, holder.page() + 1);
            } else if (event.getCurrentItem().getType() == Material.BARRIER) {
                openMenu(player, anchor);
            }
            return;
        }

        ItemStack item = event.getCurrentItem();
        UUID targetId = readTrustTarget(item).orElse(null);
        Player target = targetId == null ? null : Bukkit.getPlayer(targetId);
        if (target == null) {
            return;
        }
        if (anchor.sharedWith().contains(targetId)) {
            Anchor updated = anchor.withoutSharedPlayer(targetId);
            anchorsById.put(updated.id(), updated);
            saveAnchors();
            send(player, "anchor-unshared", "{anchor}", updated.name(), "{player}", target.getName());
            send(target, "anchor-unshared-received", "{anchor}", updated.name(), "{player}", player.getName());
            openTrustMenu(player, updated, holder.page());
            return;
        }

        Anchor updated = anchor.withSharedPlayer(targetId);
        anchorsById.put(updated.id(), updated);
        saveAnchors();
        send(player, "anchor-shared", "{anchor}", updated.name(), "{player}", target.getName());
        send(target, "anchor-shared-received", "{anchor}", updated.name(), "{player}", player.getName());
        openTrustMenu(player, updated, holder.page());
    }

    private void registerRecipe() {
        Bukkit.removeRecipe(recipeKey);
        recipeKey = NamespacedKey.fromString(getConfig().getString("item.id", "haohansmp:soul_anchor"));
        if (recipeKey == null) {
            recipeKey = new NamespacedKey(this, "soul_anchor");
        }
        if (!getConfig().getBoolean("recipe.enabled", true)) {
            return;
        }
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, createAnchorItem(1));
        recipe.shape(" S ", "#o#", "DOD");
        recipe.setIngredient('S', Material.SOUL_LANTERN);
        recipe.setIngredient('#', Material.SOUL_SAND);
        recipe.setIngredient('o', Material.ENDER_PEARL);
        recipe.setIngredient('D', Material.DEEPSLATE);
        recipe.setIngredient('O', Material.OBSIDIAN);
        Bukkit.addRecipe(recipe);
    }

    private ItemStack createAnchorItem(int amount) {
        ItemStack item = new ItemStack(getAnchorMaterial(), amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(getConfig().getString("item.display-name", "&b&lSoul Anchor")));
        meta.setLore(getConfig().getStringList("item.lore").stream().map(this::color).toList());
        NamespacedKey itemModel = NamespacedKey.fromString(
                getConfig().getString("item.item-model", "haohansmp:soul_anchor"));
        if (itemModel != null) {
            meta.setItemModel(itemModel);
        }
        // Keep CMD for legacy/debug item recognition; the resource pack uses item_model.
        meta.setCustomModelData(getConfig().getInt("item.custom-model-data", 910001));
        meta.getPersistentDataContainer().set(itemTypeKey, PersistentDataType.STRING,
                getConfig().getString("item.id", "haohansmp:soul_anchor"));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createPortableAnchorItem(Anchor anchor) {
        ItemStack item = createAnchorItem(1);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(anchorNameKey, PersistentDataType.STRING, anchor.name());
        if (!anchor.sharedWith().isEmpty()) {
            String trustedPlayers = anchor.sharedWith().stream().map(UUID::toString).sorted()
                    .collect(Collectors.joining(","));
            pdc.set(trustedPlayersKey, PersistentDataType.STRING, trustedPlayers);
        }
        List<String> lore = new ArrayList<>(meta.getLore() == null ? List.of() : meta.getLore());
        lore.add("");
        lore.add(color("&7Saved name: &f" + anchor.name()));
        lore.add(color("&7Saved trust: &f" + anchor.sharedWith().size() + " player(s)"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private String readPortableAnchorName(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        String name = item.getItemMeta().getPersistentDataContainer().get(anchorNameKey, PersistentDataType.STRING);
        return name == null || name.isBlank() ? null : sanitizeName(name);
    }

    private Set<UUID> readPortableTrustedPlayers(ItemStack item) {
        Set<UUID> trustedPlayers = new HashSet<>();
        if (item == null || !item.hasItemMeta()) {
            return trustedPlayers;
        }
        String raw = item.getItemMeta().getPersistentDataContainer()
                .get(trustedPlayersKey, PersistentDataType.STRING);
        if (raw == null || raw.isBlank()) {
            return trustedPlayers;
        }
        for (String entry : raw.split(",")) {
            UUID playerId = readUuid(entry);
            if (playerId != null) {
                trustedPlayers.add(playerId);
            }
        }
        return trustedPlayers;
    }

    private ItemStack playerHead(Player target, String displayName, List<String> lore) {
        ItemStack item = namedItem(Material.PLAYER_HEAD, displayName, lore);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setPlayerProfile(target.getPlayerProfile());
        meta.getPersistentDataContainer().set(trustTargetKey, PersistentDataType.STRING,
                target.getUniqueId().toString());
        item.setItemMeta(meta);
        return item;
    }

    private String playerName(OfflinePlayer player) {
        String name = player.getName();
        return name == null || name.isBlank() ? player.getUniqueId().toString().substring(0, 8) : name;
    }

    private Optional<UUID> readTrustTarget(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return Optional.empty();
        }
        return Optional.ofNullable(readUuid(item.getItemMeta().getPersistentDataContainer()
                .get(trustTargetKey, PersistentDataType.STRING)));
    }

    /**
     * ItemDisplay-only stack. Kept separate from the craft/place item so the model does not depend on the base item.
     */
    private ItemStack createAnchorDisplayItem() {
        // The backing material controls the render layer even when ITEM_MODEL replaces
        // the visible geometry. A non-block item such as PAPER is rendered on the
        // translucent item sheet by the client, which makes the whole custom model
        // look transparent with some shaders. Use an opaque block item so the model is
        // submitted to the solid render layer, like a furnace item model.
        ItemStack item = new ItemStack(getAnchorDisplayMaterial());
        ItemMeta meta = item.getItemMeta();
        NamespacedKey itemModel = NamespacedKey.fromString(
                getConfig().getString("item.item-model", "haohansmp:soul_anchor"));
        if (itemModel != null) {
            meta.setItemModel(itemModel);
        }
        item.setItemMeta(meta);
        return item;
    }

    private boolean isSoulAnchorItem(ItemStack item) {
        if (item == null || item.getType() != getAnchorMaterial() || !item.hasItemMeta()) {
            return false;
        }
        return getConfig().getString("item.id", "haohansmp:soul_anchor")
                .equals(item.getItemMeta().getPersistentDataContainer().get(itemTypeKey, PersistentDataType.STRING));
    }

    private ItemStack namedItem(Material material, String name) {
        return namedItem(material, name, List.of());
    }

    private ItemStack namedItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(name));
        if (!lore.isEmpty()) {
            meta.setLore(lore.stream().map(this::color).toList());
        }
        item.setItemMeta(meta);
        return item;
    }

    private void writeAnchorId(ItemStack item, UUID id) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(anchorIdKey, PersistentDataType.STRING, id.toString());
        item.setItemMeta(meta);
    }

    private Optional<UUID> readAnchorId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return Optional.empty();
        }
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        String raw = pdc.get(anchorIdKey, PersistentDataType.STRING);
        if (raw == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(raw));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    private void loadAnchors() {
        anchorsById.clear();
        anchorIdsByLocation.clear();
        idleParticleAnchorOrder.clear();
        ConfigurationSection section = anchorsConfig.getConfigurationSection("anchors");
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            ConfigurationSection node = section.getConfigurationSection(key);
            if (node == null) {
                continue;
            }
            World world = Bukkit.getWorld(UUID.fromString(node.getString("world-uuid")));
            if (world == null) {
                world = Bukkit.getWorld(node.getString("world-name", ""));
            }
            if (world == null) {
                getLogger().warning("Skipping Soul Anchor " + key + " because its world is not loaded.");
                continue;
            }
            Location location = new Location(world, node.getInt("x"), node.getInt("y"), node.getInt("z"));
            Set<UUID> sharedWith = node.getStringList("shared-with").stream()
                    .map(this::readUuid)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toUnmodifiableSet());
            Anchor anchor = new Anchor(
                    UUID.fromString(key),
                    UUID.fromString(node.getString("owner")),
                    node.getString("name", "Soul Anchor"),
                    location,
                    (float) node.getDouble("yaw", 0D),
                    (float) node.getDouble("pitch", 0D),
                    node.getLong("created-at", System.currentTimeMillis()),
                    sharedWith,
                    readUuid(node.getString("visual-uuid")),
                    readUuid(node.getString("interaction-uuid")));
            if (location.getBlock().getType() == Material.AIR
                    || location.getBlock().getType() == Material.GRINDSTONE
                    || location.getBlock().getType() == Material.JIGSAW) {
                location.getBlock().setType(getAnchorBlockMaterial(), false);
            }
            anchor = spawnVisuals(anchor);
            anchorsById.put(anchor.id(), anchor);
            anchorIdsByLocation.put(locationKey(location), anchor.id());
            idleParticleAnchorOrder.add(anchor.id());
        }
    }

    private void saveAnchors() {
        anchorsConfig.set("anchors", null);
        for (Anchor anchor : anchorsById.values()) {
            String path = "anchors." + anchor.id();
            Location loc = anchor.location();
            anchorsConfig.set(path + ".owner", anchor.ownerId().toString());
            anchorsConfig.set(path + ".name", anchor.name());
            anchorsConfig.set(path + ".world-uuid", loc.getWorld().getUID().toString());
            anchorsConfig.set(path + ".world-name", loc.getWorld().getName());
            anchorsConfig.set(path + ".x", loc.getBlockX());
            anchorsConfig.set(path + ".y", loc.getBlockY());
            anchorsConfig.set(path + ".z", loc.getBlockZ());
            anchorsConfig.set(path + ".yaw", anchor.yaw());
            anchorsConfig.set(path + ".pitch", anchor.pitch());
            anchorsConfig.set(path + ".created-at", anchor.createdAt());
            anchorsConfig.set(path + ".shared-with",
                    anchor.sharedWith().stream().map(UUID::toString).sorted().toList());
            anchorsConfig.set(path + ".visual-uuid", anchor.visualId() == null ? null : anchor.visualId().toString());
            anchorsConfig.set(path + ".interaction-uuid",
                    anchor.interactionId() == null ? null : anchor.interactionId().toString());
        }
        try {
            anchorsConfig.save(anchorsFile);
        } catch (IOException exception) {
            getLogger().severe("Could not save anchors.yml: " + exception.getMessage());
        }
    }

    private Optional<Anchor> anchorAt(Block block) {
        UUID id = anchorIdsByLocation.get(locationKey(block.getLocation()));
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(anchorsById.get(id));
    }

    private boolean isAnchorStillPlaced(Anchor anchor) {
        return anchor != null
                && anchorsById.containsKey(anchor.id())
                && anchor.location().getBlock().getType() == getAnchorBlockMaterial()
                && anchorIdsByLocation.containsKey(locationKey(anchor.location()));
    }

    private void removeAnchor(UUID id) {
        Anchor anchor = anchorsById.remove(id);
        if (anchor != null) {
            anchorIdsByLocation.remove(locationKey(anchor.location()));
            idleParticleAnchorOrder.remove(id);
            removeEntity(anchor.visualId());
            removeEntity(anchor.interactionId());
            saveAnchors();
        }
    }

    private void cancelWarmupsTouching(UUID anchorId) {
        for (Warmup warmup : new ArrayList<>(warmups.values())) {
            if (warmup.sourceId.equals(anchorId) || warmup.targetId.equals(anchorId)) {
                warmup.cancel(true);
            }
        }
    }

    private List<Anchor> ownedAnchors(UUID ownerId) {
        return anchorsById.values().stream()
                .filter(anchor -> anchor.ownerId().equals(ownerId))
                .sorted(Comparator.comparingLong(Anchor::createdAt))
                .collect(Collectors.toList());
    }

    private List<Anchor> accessibleAnchors(UUID playerId) {
        return anchorsById.values().stream()
                .filter(anchor -> canAccessAnchor(playerId, anchor))
                .sorted(Comparator.comparing((Anchor anchor) -> !anchor.ownerId().equals(playerId))
                        .thenComparingLong(Anchor::createdAt))
                .collect(Collectors.toList());
    }

    private List<Anchor> sharedAnchors(UUID playerId) {
        return anchorsById.values().stream()
                .filter(anchor -> !anchor.ownerId().equals(playerId) && anchor.sharedWith().contains(playerId))
                .sorted(Comparator.comparing((Anchor anchor) -> playerName(Bukkit.getOfflinePlayer(anchor.ownerId())),
                        String.CASE_INSENSITIVE_ORDER).thenComparingLong(Anchor::createdAt))
                .collect(Collectors.toList());
    }

    private List<SharedAnchorGroup> sharedAnchorGroups(UUID playerId) {
        Map<UUID, List<Anchor>> anchorsByOwner = new HashMap<>();
        for (Anchor anchor : sharedAnchors(playerId)) {
            anchorsByOwner.computeIfAbsent(anchor.ownerId(), ignored -> new ArrayList<>()).add(anchor);
        }
        return anchorsByOwner.entrySet().stream()
                .sorted(Comparator.comparing(
                        entry -> playerName(Bukkit.getOfflinePlayer(entry.getKey())),
                        String.CASE_INSENSITIVE_ORDER))
                .map(entry -> new SharedAnchorGroup(entry.getKey(), List.copyOf(entry.getValue())))
                .toList();
    }

    private boolean canAccessAnchor(UUID playerId, Anchor anchor) {
        return anchor.ownerId().equals(playerId) || anchor.sharedWith().contains(playerId);
    }

    private Optional<Anchor> findOwnedAnchor(UUID ownerId, String nameOrId) {
        String query = nameOrId.toLowerCase(Locale.ROOT);
        return ownedAnchors(ownerId).stream()
                .filter(anchor -> anchor.id().toString().equalsIgnoreCase(nameOrId)
                        || anchor.name().toLowerCase(Locale.ROOT).equals(query))
                .findFirst();
    }

    private String nextDefaultName(UUID ownerId) {
        Set<String> existing = ownedAnchors(ownerId).stream().map(Anchor::name)
                .collect(Collectors.toCollection(HashSet::new));
        for (int i = 1; i <= 99; i++) {
            String name = "Soul Anchor #" + i;
            if (!existing.contains(name)) {
                return name;
            }
        }
        return "Soul Anchor";
    }

    private int getAnchorLimit(Player player) {
        if (player.hasPermission("soulanchor.limit.unlimited")) {
            return -1;
        }
        int limit = getConfig().getInt("limits.default", 3);
        if (getConfig().getBoolean("limits.permission-based", true)) {
            for (String permission : player.getEffectivePermissions().stream().map(info -> info.getPermission())
                    .toList()) {
                if (permission.startsWith("soulanchor.limit.")) {
                    String raw = permission.substring("soulanchor.limit.".length());
                    if (!raw.equals("unlimited")) {
                        limit = Math.max(limit, parseInt(raw, limit));
                    }
                }
            }
        }
        return limit;
    }

    private Cost calculateCost(Location source, Location target) {
        int requiredLevels;
        int shards;
        if (!sameWorld(source, target)) {
            requiredLevels = Math.max(0, getConfig().getInt("cross-dimension.level-cost", 30));
            shards = Math.max(0, getConfig().getInt("cross-dimension.echo-shard-cost", 1));
        } else {
            double dx = target.getX() - source.getX();
            double dz = target.getZ() - source.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);
            int blocksPerTier = Math.max(1, getConfig().getInt("distance.blocks-per-tier", 1000));
            int levelsPerTier = Math.max(1, getConfig().getInt("distance.levels-per-tier", 10));
            int minCost = Math.max(0, getConfig().getInt("distance.minimum-level-cost", 10));
            requiredLevels = Math.max(minCost, (int) Math.ceil(distance / blocksPerTier) * levelsPerTier);
            double freeShardDistance = Math.max(0D, getConfig().getDouble("teleport.echo-shard-free-distance", 2000D));
            shards = distance <= freeShardDistance
                    ? 0
                    : Math.max(0, getConfig().getInt("teleport.echo-shard-cost", 1));
        }

        int pointsPerRequiredLevel = Math.max(0,
                getConfig().getInt("teleport.experience-points-per-required-level", 8));
        int experiencePoints = (int) Math.min(Integer.MAX_VALUE, (long) requiredLevels * pointsPerRequiredLevel);
        return new Cost(requiredLevels, experiencePoints, shards);
    }

    private String formatDistance(Location source, Location target) {
        if (!sameWorld(source, target)) {
            return "cross-dimension";
        }
        double dx = target.getX() - source.getX();
        double dz = target.getZ() - source.getZ();
        return String.valueOf((int) Math.round(Math.sqrt(dx * dx + dz * dz)));
    }

    private int countEchoShards(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.ECHO_SHARD) {
                count += item.getAmount();
            }
        }
        return count;
    }

    private void removeEchoShards(Player player, int amount) {
        int remaining = amount;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length && remaining > 0; i++) {
            ItemStack item = contents[i];
            if (item == null || item.getType() != Material.ECHO_SHARD) {
                continue;
            }
            int take = Math.min(remaining, item.getAmount());
            item.setAmount(item.getAmount() - take);
            remaining -= take;
            if (item.getAmount() <= 0) {
                contents[i] = null;
            }
        }
        player.getInventory().setContents(contents);
    }

    private Location findSafeLocation(Location anchorLocation) {
        int horizontal = getConfig().getInt("safe-location.horizontal-radius", 3);
        int vertical = getConfig().getInt("safe-location.vertical-radius", 4);
        World world = anchorLocation.getWorld();
        int baseX = anchorLocation.getBlockX();
        int baseY = anchorLocation.getBlockY();
        int baseZ = anchorLocation.getBlockZ();

        for (int dy = 0; dy <= vertical; dy++) {
            for (int sign : new int[] { 1, -1 }) {
                if (dy == 0 && sign < 0) {
                    continue;
                }
                int y = baseY + dy * sign;
                for (int radius = 1; radius <= horizontal; radius++) {
                    for (int x = baseX - radius; x <= baseX + radius; x++) {
                        for (int z = baseZ - radius; z <= baseZ + radius; z++) {
                            if (Math.max(Math.abs(x - baseX), Math.abs(z - baseZ)) != radius) {
                                continue;
                            }
                            Location candidate = new Location(world, x + 0.5D, y, z + 0.5D, anchorLocation.getYaw(),
                                    anchorLocation.getPitch());
                            if (isSafe(candidate)) {
                                return candidate;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isSafe(Location location) {
        World world = location.getWorld();
        if (world == null || location.getBlockY() <= world.getMinHeight()
                || location.getBlockY() >= world.getMaxHeight() - 2) {
            return false;
        }
        WorldBorder border = world.getWorldBorder();
        if (!border.isInside(location)) {
            return false;
        }
        Block feet = location.getBlock();
        Block head = feet.getRelative(0, 1, 0);
        Block below = feet.getRelative(0, -1, 0);
        return feet.isPassable()
                && head.isPassable()
                && below.getType().isSolid()
                && !isHazard(feet.getType())
                && !isHazard(head.getType())
                && !isHazard(below.getType());
    }

    private boolean isHazard(Material material) {
        return material == Material.LAVA
                || material == Material.FIRE
                || material == Material.SOUL_FIRE
                || material == Material.CACTUS
                || material == Material.POWDER_SNOW
                || material == Material.MAGMA_BLOCK
                || material == Material.CAMPFIRE
                || material == Material.SOUL_CAMPFIRE
                || material == Material.END_PORTAL
                || material == Material.NETHER_PORTAL;
    }

    private boolean isWorldBlocked(World world) {
        String mode = getConfig().getString("worlds.mode", "blacklist").toLowerCase(Locale.ROOT);
        List<String> worlds = getConfig().getStringList("worlds.list");
        boolean listed = worlds.stream().anyMatch(name -> name.equalsIgnoreCase(world.getName()));
        return mode.equals("blacklist") ? listed : !listed;
    }

    private Material getAnchorMaterial() {
        String configured = getConfig().getString("item.material", "BARRIER");
        Material material = Material.matchMaterial(configured);
        return material == null ? Material.BARRIER : material;
    }

    private Material getAnchorBlockMaterial() {
        String configured = getConfig().getString("item.placed-block", "BARRIER");
        Material material = Material.matchMaterial(configured);
        return material == null ? Material.BARRIER : material;
    }

    private Material getAnchorDisplayMaterial() {
        String configured = getConfig().getString("visuals.display-material", "STONE");
        Material material = Material.matchMaterial(configured);
        if (material == null || !material.isBlock() || !material.isOccluding()) {
            return Material.STONE;
        }
        return material;
    }

    private Anchor spawnVisuals(Anchor anchor) {
        Entity existingDisplay = anchor.visualId() == null ? null : Bukkit.getEntity(anchor.visualId());
        Entity existingInteraction = anchor.interactionId() == null ? null : Bukkit.getEntity(anchor.interactionId());
        UUID visualId = existingDisplay instanceof ItemDisplay ? existingDisplay.getUniqueId() : null;
        UUID interactionId = existingInteraction instanceof Interaction ? existingInteraction.getUniqueId() : null;

        if (existingDisplay instanceof ItemDisplay display) {
            configureAnchorDisplay(display, anchor);
        }
        if (existingInteraction instanceof Interaction interaction) {
            configureAnchorInteraction(interaction, anchor.id());
        }

        if (visualId == null) {
            Location displayLocation = anchor.location().clone().add(0.5D, 0.5D, 0.5D);
            ItemDisplay display = anchor.location().getWorld().spawn(displayLocation, ItemDisplay.class, entity -> {
                configureAnchorDisplay(entity, anchor);
            });
            visualId = display.getUniqueId();
        }

        if (interactionId == null) {
            Location interactionLocation = anchor.location().clone().add(0.5D, 0.2D, 0.5D);
            Interaction interaction = anchor.location().getWorld().spawn(interactionLocation, Interaction.class,
                    entity -> {
                        configureAnchorInteraction(entity, anchor.id());
                    });
            interactionId = interaction.getUniqueId();
        }

        return new Anchor(anchor.id(), anchor.ownerId(), anchor.name(), anchor.location(), anchor.yaw(), anchor.pitch(),
                anchor.createdAt(), anchor.sharedWith(), visualId, interactionId);
    }

    private void refreshAnchorVisuals() {
        for (Anchor anchor : new ArrayList<>(anchorsById.values())) {
            anchorsById.put(anchor.id(), spawnVisuals(anchor));
        }
        saveAnchors();
    }

    private void configureAnchorDisplay(ItemDisplay entity, Anchor anchor) {
        entity.teleport(anchor.location().clone().add(0.5D, 0.5D, 0.5D));
        entity.setItemStack(createAnchorDisplayItem());
        entity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
        float scaleX = (float) getConfig().getDouble("visuals.scale-x", 1.0D);
        float scaleY = (float) getConfig().getDouble("visuals.scale-y", 0.877D);
        float scaleZ = (float) getConfig().getDouble("visuals.scale-z", 1.0D);
        entity.setTransformation(new org.bukkit.util.Transformation(
                new org.joml.Vector3f(),
                new org.joml.Quaternionf(),
                new org.joml.Vector3f(scaleX, scaleY, scaleZ),
                new org.joml.Quaternionf()));
        entity.setGravity(false);
        entity.setPersistent(true);
        entity.setSilent(true);
        entity.getPersistentDataContainer().set(anchorIdKey, PersistentDataType.STRING, anchor.id().toString());
    }

    private void configureAnchorInteraction(Interaction entity, UUID anchorId) {
        entity.setInteractionWidth((float) getConfig().getDouble("visuals.interaction-width", 1.2D));
        entity.setInteractionHeight((float) getConfig().getDouble("visuals.interaction-height", 1.1D));
        entity.setGravity(false);
        entity.setPersistent(true);
        entity.setSilent(true);
        entity.getPersistentDataContainer().set(anchorIdKey, PersistentDataType.STRING, anchorId.toString());
    }

    private Optional<Anchor> anchorFromEntity(Entity entity) {
        String raw = entity.getPersistentDataContainer().get(anchorIdKey, PersistentDataType.STRING);
        if (raw == null) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(anchorsById.get(UUID.fromString(raw)));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    private void removeEntity(UUID id) {
        if (id == null) {
            return;
        }
        Entity entity = Bukkit.getEntity(id);
        if (entity != null) {
            entity.remove();
        }
    }

    private UUID readUuid(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(raw);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private void consumePlacedItem(Player player, EquipmentSlot hand) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        ItemStack stack = hand == EquipmentSlot.OFF_HAND ? player.getInventory().getItemInOffHand()
                : player.getInventory().getItemInMainHand();
        if (stack.getAmount() <= 1) {
            if (hand == EquipmentSlot.OFF_HAND) {
                player.getInventory().setItemInOffHand(null);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
            return;
        }
        stack.setAmount(stack.getAmount() - 1);
    }

    private Player damagerPlayer(Entity entity) {
        if (entity instanceof Player player) {
            return player;
        }
        return null;
    }

    private boolean sameWorld(Location left, Location right) {
        return left.getWorld() != null && right.getWorld() != null
                && left.getWorld().getUID().equals(right.getWorld().getUID());
    }

    private String locationKey(Location location) {
        return location.getWorld().getUID() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":"
                + location.getBlockZ();
    }

    private int parseInt(String raw, int fallback) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private String formatPercent(double chance) {
        double percent = chance * 100D;
        if (percent == Math.rint(percent)) {
            return String.format(Locale.ROOT, "%.0f%%", percent);
        }
        return String.format(Locale.ROOT, "%.2f%%", percent);
    }

    private String sanitizeName(String input) {
        String stripped = ChatColor.stripColor(color(input)).replaceAll("[\\p{Cntrl}<>]", "").trim();
        if (stripped.isEmpty()) {
            return "Soul Anchor";
        }
        return stripped.length() > 24 ? stripped.substring(0, 24) : stripped;
    }

    private String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input == null ? "" : input);
    }

    private void loadMessages() {
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        try (InputStream stream = getResource("messages.yml")) {
            if (stream == null) {
                return;
            }
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(stream, StandardCharsets.UTF_8));
            messages.setDefaults(defaults);
        } catch (IOException exception) {
            getLogger().warning("Could not load default messages: " + exception.getMessage());
        }
    }

    private void send(CommandSender sender, String key, String... replacements) {
        String text = messages.getString(key, key);
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            text = text.replace(replacements[i], replacements[i + 1]);
        }
        sender.sendMessage(color(messages.getString("prefix", "") + text));
    }

    private void startIdleParticles() {
        if (!getConfig().getBoolean("visuals.idle-particles", true)) {
            return;
        }
        long interval = Math.max(20L, getConfig().getLong("visuals.idle-particle-interval-ticks", 40L));
        int batchSize = Math.max(1, getConfig().getInt("visuals.idle-particle-batch-size", 32));
        double viewDistance = Math.max(1D, getConfig().getDouble("visuals.particle-view-distance", 24D));
        double viewDistanceSquared = viewDistance * viewDistance;
        idleParticlesTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (idleParticleAnchorOrder.isEmpty()) {
                    idleParticleCursor = 0;
                    return;
                }
                if (idleParticleCursor >= idleParticleAnchorOrder.size()) {
                    idleParticleCursor = 0;
                }

                int processed = 0;
                int scanned = 0;
                while (processed < batchSize && scanned < idleParticleAnchorOrder.size()) {
                    UUID anchorId = idleParticleAnchorOrder.get(idleParticleCursor);
                    idleParticleCursor = (idleParticleCursor + 1) % idleParticleAnchorOrder.size();
                    scanned++;

                    Anchor anchor = anchorsById.get(anchorId);
                    if (anchor == null) {
                        continue;
                    }
                    processed++;
                    spawnIdleParticleForNearbyPlayers(anchor, viewDistanceSquared);
                }
            }
        }.runTaskTimer(this, interval, interval);
    }

    private void restartIdleParticles() {
        if (idleParticlesTask != null) {
            idleParticlesTask.cancel();
            idleParticlesTask = null;
        }
        idleParticleCursor = 0;
        startIdleParticles();
    }

    private void spawnIdleParticleForNearbyPlayers(Anchor anchor, double viewDistanceSquared) {
        World world = anchor.location().getWorld();
        if (world == null) {
            return;
        }
        Location loc = anchor.location().clone().add(0.5D, 0.9D, 0.5D);
        for (Player player : world.getPlayers()) {
            if (player.getLocation().distanceSquared(loc) <= viewDistanceSquared) {
                player.spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 2, 0.15D, 0.25D, 0.15D, 0.005D);
            }
        }
    }

    private record Anchor(UUID id, UUID ownerId, String name, Location location, float yaw, float pitch, long createdAt,
            Set<UUID> sharedWith, UUID visualId, UUID interactionId) {
        Anchor {
            sharedWith = Set.copyOf(sharedWith);
        }

        Anchor withName(String newName) {
            return new Anchor(id, ownerId, newName, location, yaw, pitch, createdAt, sharedWith, visualId,
                    interactionId);
        }

        Anchor withSharedPlayer(UUID playerId) {
            Set<UUID> updated = new HashSet<>(sharedWith);
            updated.add(playerId);
            return new Anchor(id, ownerId, name, location, yaw, pitch, createdAt, updated, visualId, interactionId);
        }

        Anchor withoutSharedPlayer(UUID playerId) {
            Set<UUID> updated = new HashSet<>(sharedWith);
            updated.remove(playerId);
            return new Anchor(id, ownerId, name, location, yaw, pitch, createdAt, updated, visualId, interactionId);
        }
    }

    private record Cost(int requiredLevels, int experiencePoints, int shards) {
    }

    private record Validation(boolean ok, String messageKey, Location safeDestination, String[] replacements) {
        static Validation ok(Location safeDestination) {
            return new Validation(true, "", safeDestination, new String[0]);
        }

        static Validation fail(String messageKey, String... replacements) {
            return new Validation(false, messageKey, null, replacements);
        }
    }

    private static final class AnchorMenuHolder implements InventoryHolder {
        private final UUID sourceAnchorId;

        private AnchorMenuHolder(UUID sourceAnchorId) {
            this.sourceAnchorId = sourceAnchorId;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

        private UUID sourceAnchorId() {
            return sourceAnchorId;
        }
    }

    private record SharedAnchorGroup(UUID ownerId, List<Anchor> anchors) {
    }

    private static final class SharedAnchorMenuHolder implements InventoryHolder {
        private final UUID sourceAnchorId;
        private final int page;

        private SharedAnchorMenuHolder(UUID sourceAnchorId, int page) {
            this.sourceAnchorId = sourceAnchorId;
            this.page = page;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

        private UUID sourceAnchorId() {
            return sourceAnchorId;
        }

        private int page() {
            return page;
        }
    }

    private static final class TrustMenuHolder implements InventoryHolder {
        private final UUID anchorId;
        private final int page;

        private TrustMenuHolder(UUID anchorId, int page) {
            this.anchorId = anchorId;
            this.page = page;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

        private UUID anchorId() {
            return anchorId;
        }

        private int page() {
            return page;
        }
    }

    private final class Warmup {
        private final Player player;
        private final UUID sourceId;
        private final UUID targetId;
        private final Location startLocation;
        private BukkitTask task;

        private Warmup(Player player, UUID sourceId, UUID targetId, Location startLocation, int seconds) {
            this.player = player;
            this.sourceId = sourceId;
            this.targetId = targetId;
            this.startLocation = startLocation;
        }

        private void cancel(boolean notify) {
            warmups.remove(player.getUniqueId());
            if (task != null) {
                task.cancel();
            }
            if (notify && player.isOnline()) {
                send(player, "warmup-cancelled");
                player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 0.6F, 1F);
            }
        }
    }
}
